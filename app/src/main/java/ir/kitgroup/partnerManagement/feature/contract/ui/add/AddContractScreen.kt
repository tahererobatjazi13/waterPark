package ir.kitgroup.partnerManagement.feature.contract.ui.add

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomDateTimeFields
import ir.kitgroup.partnerManagement.core.ui.components.CustomEditTextField
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.CustomSelectorField
import ir.kitgroup.partnerManagement.core.ui.components.DatePickerDialog
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.ContractStatus
import ir.kitgroup.partnerManagement.core.ui.util.CooperationModel
import ir.kitgroup.partnerManagement.core.ui.util.SettlementPeriodType
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate
import ir.kitgroup.partnerManagement.core.ui.util.formatPercentForInput
import ir.kitgroup.partnerManagement.feature.organization.ui.OrganizationListBottomSheet
import saman.zamani.persiandate.PersianDate
import java.util.Locale

@Composable
fun AddContractRoute(
    onSaved: () -> Unit,
    viewModel: AddContractViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState
        .collectAsStateWithLifecycle()

    LaunchedEffect(uiState.saveSuccess) {

        if (uiState.saveSuccess) {
            onSaved()
        }
    }

    AddContractScreen(
        preselectedOrganizationId =
        viewModel.organizationId,

        preselectedOrganization =
        uiState.preselectedOrganization,

        organizationsList =
        uiState.organizations,

        contract =
        uiState.contract,

        isEditMode =
        viewModel.isEditMode,

        isSaving =
        uiState.isSaving,

        errorMessage =
        uiState.message,

        onMessageShown =
        viewModel::clearMessage,

        onSaveClick = { request ->

            viewModel.saveContract(
                request
            )
        }
    )
}


@Composable
fun AddContractScreen(
    contract: ContractEntity? = null,

    preselectedOrganizationId: String? = null,

    preselectedOrganization:
    OrganizationWithDetail? = null,

    organizationsList:
    List<OrganizationWithDetail> =
        emptyList(),

    isEditMode: Boolean = false,

    isSaving: Boolean = false,

    errorMessage: String? = null,

    onMessageShown: () -> Unit = {},


    onSaveClick:
        (AddContractFormState) -> Unit
) {
    val context = LocalContext.current
    val appColors = LocalPartnerManagementColors.current

    var contractName by rememberSaveable { mutableStateOf("") }
    var contractNumber by rememberSaveable { mutableStateOf("") }

    var description by rememberSaveable { mutableStateOf("") }
    val today = remember { PersianDate() }

    var selectedOrganization
            by remember {
                mutableStateOf<
                        OrganizationWithDetail?
                        >(null)
            }
    val todayStartDate = remember {
        "${today.shYear}/${
            String.format(Locale.US, "%02d", today.shMonth)
        }/${
            String.format(Locale.US, "%02d", today.shDay)
        }"
    }
    var showOrganizationSheet by remember { mutableStateOf(false) }
    val formState = remember {
        AddContractFormState().apply {
            startDate = todayStartDate
        }
    }

    var formInitialized by
    remember(contract?.contractId) {
        mutableStateOf(false)
    }
    // نگاشت Enumها با مقادیر متنی جهت استفاده مستقیم در DropdownSelectorField
    val cooperationModelMap = remember {
        CooperationModel.entries.associateWith { context.getString(it.titleRes) }
    }
    val selectedCooperationModel =
        CooperationModel.entries.firstOrNull {
            it.id == formState.cooperationModel
        }
    val settlementPeriodTypeMap = remember {
        SettlementPeriodType.entries.associateWith { context.getString(it.titleRes) }
    }
    var selectedSettlementPeriodType by remember { mutableStateOf<SettlementPeriodType?>(null) }
    val selectedSettlementPeriod =
        SettlementPeriodType.entries.firstOrNull {
            it.id == formState.settlementPeriodType
        }
    val selectedStatus =
        ContractStatus.entries.firstOrNull {
            it.id == formState.status
        } ?: ContractStatus.DRAFT

    val contractStatusMap = remember {
        ContractStatus.entries.associateWith { context.getString(it.titleRes) }
    }
    var selectedContractStatus by remember { mutableStateOf(ContractStatus.DRAFT) }

    // ست کردن مقدار اولیه سازمان از دیتابیس
    LaunchedEffect(
        preselectedOrganization,
        contract,
        organizationsList
    ) {

        if (selectedOrganization != null) {
            return@LaunchedEffect
        }

        selectedOrganization =
            preselectedOrganization
                ?: organizationsList.firstOrNull {
                    it.organization.organizationId ==
                            formState.organizationId
                }
    }


    LaunchedEffect(contract) {

        val item =
            contract
                ?: return@LaunchedEffect

        if (formInitialized) {
            return@LaunchedEffect
        }

        formInitialized = true


        formState.organizationId =
            item.organizationId.orEmpty()

        formState.contractName =
            item.title.orEmpty()

        formState.contractNumber =
            item.contractNumber.orEmpty()

        formState.startDate =
            item.startDate
                ?.takeIf { it.isNotBlank() }
                ?.let { formatJalaliDate(it) }
                .orEmpty()

        formState.settlementPeriodType =
            item.settlementPeriodType ?: 0

        formState.cooperationModel =
            item.cooperationModel ?: 0

        formState.defaultDiscountPercent =
            formatPercentForInput(item.defaultDiscountPercent)

        formState.defaultCommissionPercent =
            formatPercentForInput(item.defaultCommissionPercent)

        formState.status =
            item.status!!
    }
    LaunchedEffect(errorMessage) {

        if (!errorMessage.isNullOrBlank()) {

            Toast
                .makeText(
                    context,
                    errorMessage,
                    Toast.LENGTH_LONG
                )
                .show()

            onMessageShown()
        }
    }
    var showStartDatePicker by remember { mutableStateOf(false) }




    Scaffold(

        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (preselectedOrganizationId == null) {
                    CustomSelectorField(
                        value = selectedOrganization?.organization?.name ?: "",
                        label = stringResource(R.string.label_organization_name),
                        placeholder = stringResource(R.string.hint_choose_organization_name),
                        isExpanded = showOrganizationSheet,
                        onClick = { showOrganizationSheet = true }
                    )
                }

                /*        CustomEditTextField(
                            value = contractName,
                            onValueChange = { contractName = it },
                            label = stringResource(R.string.label_contract_title),
                            placeholder = stringResource(R.string.hint_enter_contract_title),
                            isRequired = true,
                            leadingIcon = null
                        )*/

                DropdownSelectorField(
                    value =
                    selectedCooperationModel
                        ?.let {
                            context.getString(it.titleRes)
                        }
                        .orEmpty(),

                    label =
                    stringResource(
                        R.string.label_cooperation_model
                    ),

                    placeholder =
                    stringResource(
                        R.string.hint_choose_cooperation_model
                    ),

                    items =
                    CooperationModel.entries.map {
                        context.getString(it.titleRes)
                    },

                    isRequired = true,

                    onItemSelected = { title ->

                        CooperationModel.entries
                            .firstOrNull {
                                context.getString(
                                    it.titleRes
                                ) == title
                            }
                            ?.let {
                                formState.cooperationModel =
                                    it.id
                            }
                    }
                )

                DropdownSelectorField(
                    value =
                    selectedSettlementPeriod
                        ?.let {
                            context.getString(it.titleRes)
                        }
                        .orEmpty(),

                    label =
                    stringResource(
                        R.string.label_settlement_period_type
                    ),

                    placeholder =
                    stringResource(
                        R.string.hint_choose_settlement_period_type
                    ),

                    items =
                    SettlementPeriodType.entries.map {
                        context.getString(it.titleRes)
                    },

                    isRequired = true,

                    onItemSelected = { title ->

                        SettlementPeriodType.entries
                            .firstOrNull {
                                context.getString(
                                    it.titleRes
                                ) == title
                            }
                            ?.let {
                                formState.settlementPeriodType =
                                    it.id
                            }
                    }
                )

                CustomDateTimeFields(
                    label = stringResource(R.string.label_start_date),
                    date = formState.startDate,
                    onDateClick = {
                        showStartDatePicker = true
                    },
                    showTime = false,
                    isRequired = false
                )


                DropdownSelectorField(
                    value =
                    context.getString(
                        selectedStatus.titleRes
                    ),

                    label =
                    stringResource(
                        R.string.label_contract_Status
                    ),

                    placeholder =
                    stringResource(
                        R.string.hint_choose_status
                    ),

                    items =
                    ContractStatus.entries.map {
                        context.getString(it.titleRes)
                    },

                    isRequired = true,

                    onItemSelected = { title ->

                        ContractStatus.entries
                            .firstOrNull {
                                context.getString(
                                    it.titleRes
                                ) == title
                            }
                            ?.let {
                                formState.status =
                                    it.id
                            }
                    }
                )

                CustomEditTextField(
                    value = formState.defaultCommissionPercent,
                    onValueChange = {
                        formState.defaultCommissionPercent = it
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    label = stringResource(
                        R.string.label_default_commission_percent
                    ),
                    placeholder = stringResource(
                        R.string.hint_enter_default_commission_percent
                    ),
                    isRequired = false,
                    leadingIcon = null
                )

                CustomEditTextField(
                    value = formState.defaultDiscountPercent,
                    onValueChange = {
                        formState.defaultDiscountPercent = it
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    label = stringResource(
                        R.string.label_default_discount_percent
                    ),
                    placeholder = stringResource(
                        R.string.hint_enter_default_discount_percent
                    ),
                    isRequired = false,
                    leadingIcon = null
                )

                /*   CustomDescriptionField(
                       label = stringResource(R.string.label_description),
                       value = description,
                       onValueChange = { description = it },
                       placeholder = stringResource(R.string.hint_description)
                   )*/

                Spacer(modifier = Modifier.height(8.dp))

                CustomButton(

                    text =
                    if (isSaving) {
                        if (isEditMode) {
                            "در حال ویرایش..."
                        } else {
                            "در حال ثبت..."
                        }
                    } else {
                        if (isEditMode) {
                            "ویرایش قرارداد"
                        } else {
                            stringResource(
                                R.string.label_registration
                            )
                        }
                    },
                    onClick = {

                        if (!isSaving) {

                            formState.organizationId =
                                selectedOrganization
                                    ?.organization
                                    ?.organizationId
                                    ?: preselectedOrganizationId
                                            ?: formState.organizationId

                            onSaveClick(formState)
                        }
                    },

                    modifier =
                    Modifier.fillMaxWidth()
                )
            }
        }
    }

    // باز شدن باتم شیت با لیست ارسالی از دیتابیس
    if (showOrganizationSheet && preselectedOrganizationId == null) {
        OrganizationListBottomSheet(
            list = organizationsList,
            onDismiss = { showOrganizationSheet = false },
            onItemSelected = { item ->
                selectedOrganization = item
                showOrganizationSheet = false
            }
        )
    }

    if (showStartDatePicker) {
        DatePickerDialog(
            onDismiss = { showStartDatePicker = false },
            onDateSelected = { date ->
                formState.startDate =
                    "${date.year}/${
                        String.format(
                            Locale.US,
                            "%02d",
                            date.month
                        )
                    }/${
                        String.format(
                            Locale.US,
                            "%02d",
                            date.day
                        )
                    }"

                showStartDatePicker = false
            }
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun AddContractScreenPreview() {
    AppScreenPreview {
        AddContractScreen(
            preselectedOrganizationId = null,
            preselectedOrganization = null,
            organizationsList = emptyList(),
            onSaveClick = {}
        )
    }
}
