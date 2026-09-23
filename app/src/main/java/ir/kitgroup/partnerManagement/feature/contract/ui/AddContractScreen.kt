package ir.kitgroup.partnerManagement.feature.contract.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationEntity
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomDateTimeFields
import ir.kitgroup.partnerManagement.core.ui.components.CustomDescriptionField
import ir.kitgroup.partnerManagement.core.ui.components.CustomEditTextField
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.CustomSelectorField
import ir.kitgroup.partnerManagement.core.ui.components.DatePickerDialog
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.ContractStatus
import ir.kitgroup.partnerManagement.core.ui.util.CooperationModel
import ir.kitgroup.partnerManagement.core.ui.util.SettlementPeriodType
import ir.kitgroup.partnerManagement.core.ui.util.demoOrganizations
import ir.kitgroup.partnerManagement.feature.organization.ui.OrganizationBottomSheet
import saman.zamani.persiandate.PersianDate
import java.util.Locale
import java.util.UUID

@Composable
fun AddContractScreen(
    preselectedOrganizationId: String? = null,
    onBackClick: () -> Unit,
    onSaveClick: (ContractEntity) -> Unit
) {
    val context = LocalContext.current
    val appColors = LocalPartnerManagementColors.current

    var contractName by rememberSaveable { mutableStateOf("") }
    var contractNumber by rememberSaveable { mutableStateOf("") }
    var defaultCommissionPercent by rememberSaveable { mutableStateOf("") }
    var defaultDiscountPercent by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    var selectedOrganization by remember { mutableStateOf<OrganizationEntity?>(null) }
    var showOrganizationSheet by remember { mutableStateOf(false) }

    // نگاشت Enumها با مقادیر متنی جهت استفاده مستقیم در DropdownSelectorField
    val cooperationModelMap = remember {
        CooperationModel.entries.associateWith { context.getString(it.titleRes) }
    }
    var selectedCooperationModel by remember { mutableStateOf<CooperationModel?>(null) }

    val settlementPeriodTypeMap = remember {
        SettlementPeriodType.entries.associateWith { context.getString(it.titleRes) }
    }
    var selectedSettlementPeriodType by remember { mutableStateOf<SettlementPeriodType?>(null) }

    val contractStatusMap = remember {
        ContractStatus.entries.associateWith { context.getString(it.titleRes) }
    }
    var selectedContractStatus by remember { mutableStateOf(ContractStatus.DRAFT) }

    // مقداردهی اولیه سازمان
    LaunchedEffect(preselectedOrganizationId) {
        if (preselectedOrganizationId != null) {
            selectedOrganization = demoOrganizations.find {
                it.organizationId == preselectedOrganizationId
            }
        }
    }

    val today = remember { PersianDate() }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    var startDate by rememberSaveable {
        mutableStateOf(
            "${today.shYear}/${String.format(Locale.US, "%02d", today.shMonth)}/${
                String.format(Locale.US, "%02d", today.shDay)
            }"
        )
    }
    var endDate by rememberSaveable {
        mutableStateOf(
            "${today.shYear}/${String.format(Locale.US, "%02d", today.shMonth)}/${
                String.format(Locale.US, "%02d", today.shDay)
            }"
        )
    }

    Scaffold(
        topBar = {
            CustomHeader(
                title = R.string.label_contract_organization,
                showBackButton = true,
                onBackClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.primary
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = appColors.screenBackground
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (preselectedOrganizationId == null) {
                    CustomSelectorField(
                        value = selectedOrganization?.name ?: "",
                        label = stringResource(R.string.label_organization_name),
                        placeholder = stringResource(R.string.hint_choose_organization_name),
                        isExpanded = showOrganizationSheet,
                        onClick = { showOrganizationSheet = true }
                    )
                }

                CustomEditTextField(
                    value = contractName,
                    onValueChange = { contractName = it },
                    label = stringResource(R.string.label_contract_title),
                    placeholder = stringResource(R.string.hint_enter_contract_title),
                    isRequired = true,
                    leadingIcon = null
                )

                DropdownSelectorField(
                    value = cooperationModelMap[selectedCooperationModel] ?: "",
                    label = stringResource(R.string.label_cooperation_model),
                    placeholder = stringResource(R.string.hint_choose_cooperation_model),
                    items = cooperationModelMap.values.toList(),
                    isRequired = false,
                    onItemSelected = { selectedTitle ->
                        selectedCooperationModel =
                            cooperationModelMap.entries.firstOrNull { it.value == selectedTitle }?.key
                    }
                )

                DropdownSelectorField(
                    value = settlementPeriodTypeMap[selectedSettlementPeriodType] ?: "",
                    label = stringResource(R.string.label_settlement_period_type),
                    placeholder = stringResource(R.string.hint_choose_settlement_period_type),
                    items = settlementPeriodTypeMap.values.toList(),
                    isRequired = false,
                    onItemSelected = { selectedTitle ->
                        selectedSettlementPeriodType =
                            settlementPeriodTypeMap.entries.firstOrNull { it.value == selectedTitle }?.key
                    }
                )

                CustomDateTimeFields(
                    label = stringResource(R.string.label_start_date),
                    date = startDate,
                    onDateClick = { showStartDatePicker = true },
                    showTime = false,
                    isRequired = false
                )

                CustomDateTimeFields(
                    label = stringResource(R.string.label_end_date),
                    date = endDate,
                    onDateClick = { showEndDatePicker = true },
                    showTime = false,
                    isRequired = false
                )

                DropdownSelectorField(
                    value = contractStatusMap[selectedContractStatus] ?: "",
                    label = stringResource(R.string.label_contract_Status),
                    placeholder = stringResource(R.string.hint_choose_status),
                    items = contractStatusMap.values.toList(),
                    isRequired = false,
                    onItemSelected = { selectedTitle ->
                        selectedContractStatus =
                            contractStatusMap.entries.firstOrNull { it.value == selectedTitle }?.key
                                ?: ContractStatus.DRAFT
                    }
                )

                CustomEditTextField(
                    value = defaultCommissionPercent,
                    onValueChange = { defaultCommissionPercent = it },
                    label = stringResource(R.string.label_default_commission_percent),
                    placeholder = stringResource(R.string.hint_enter_default_commission_percent),
                    isRequired = false,
                    leadingIcon = null
                )

                CustomEditTextField(
                    value = defaultDiscountPercent,
                    onValueChange = { defaultDiscountPercent = it },
                    label = stringResource(R.string.label_default_discount_percent),
                    placeholder = stringResource(R.string.hint_enter_default_discount_percent),
                    isRequired = false,
                    leadingIcon = null
                )

                CustomDescriptionField(
                    label = stringResource(R.string.label_description),
                    value = description,
                    onValueChange = { description = it },
                    placeholder = stringResource(R.string.hint_description)
                )

                Spacer(modifier = Modifier.height(8.dp))

                CustomButton(
                    text = stringResource(R.string.label_registration),
                    onClick = {
                        val newContract = ContractEntity(
                            contractId = UUID.randomUUID().toString(),
                            name = contractName.ifBlank { null },
                            contractNumber = contractNumber.ifBlank { null },
                            organizationId = selectedOrganization?.organizationId
                                ?: preselectedOrganizationId,
                            cooperationModel = selectedCooperationModel?.id,
                            settlementPeriodType = selectedSettlementPeriodType?.id,
                            startDate = startDate.ifBlank { null },
                            endDate = endDate.ifBlank { null },
                            contractStatus = selectedContractStatus.id,
                            defaultCommissionPercent = defaultCommissionPercent.toDoubleOrNull(),
                            defaultDiscountPercent = defaultDiscountPercent.toDoubleOrNull(),
                            description = description.ifBlank { null }
                        )
                        onSaveClick(newContract)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    if (showOrganizationSheet && preselectedOrganizationId == null) {
        OrganizationBottomSheet(
            list = demoOrganizations,
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
                startDate = "${date.year}/${String.format(Locale.US, "%02d", date.month)}/${
                    String.format(Locale.US, "%02d", date.day)
                }"
                showStartDatePicker = false
            }
        )
    }

    if (showEndDatePicker) {
        DatePickerDialog(
            onDismiss = { showEndDatePicker = false },
            onDateSelected = { date ->
                endDate = "${date.year}/${String.format(Locale.US, "%02d", date.month)}/${
                    String.format(Locale.US, "%02d", date.day)
                }"
                showEndDatePicker = false
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
            onBackClick = {},
            onSaveClick = {}
        )
    }
}
