package ir.kitgroup.partnerManagement.feature.contract.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomDateTimeFields
import ir.kitgroup.partnerManagement.core.ui.components.CustomDescriptionField
import ir.kitgroup.partnerManagement.core.ui.components.CustomEditTextField
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.CustomSelectorField
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.Status
import ir.kitgroup.partnerManagement.feature.organization.model.OrganizationModel
import saman.zamani.persiandate.PersianDate
import java.util.Locale
import ir.kitgroup.partnerManagement.core.ui.components.DatePickerDialog
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField
import ir.kitgroup.partnerManagement.feature.organization.ui.OrganizationBottomSheet

@Composable
fun AddContractScreen(
    preselectedOrganizationId: Int? = null,

    onBackClick: () -> Unit, onSaveClick: () -> Unit,
) {
    val appColors = LocalPartnerManagementColors.current
    var contractTitle by rememberSaveable { mutableStateOf("") }
    var defaultCommissionPercent by rememberSaveable { mutableStateOf("") }
    var defaultDiscountPercent by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    val organizationList = listOf(
        OrganizationModel(
            1,
            "هتل پارسیان آزادی",
            city = "مشهد",
            region = "منطقه 2",
            "یوسفی",
            Status.ACTIVE,
            5, 0,
            36.2845,
            59.5892
        ),

        OrganizationModel(
            2,
            name = "سازمان پالاس",
            city = "مشهد",
            region = "منطقه 2",
            address = "قاسم آباد",
            Status.ACTIVE,
            4, 4,
            36.3562,
            59.5084
        ),

        OrganizationModel(
            3,
            name = "سازمان نوید",
            city = "مشهد",
            region = "منطقه 2",
            address = "پیروزی",
            status = Status.ACTIVE,
            3, 3,
            latitude = 36.3015,
            longitude = 59.5289
        ),

        OrganizationModel(
            4,
            name = "هتل مرکزی",
            city = "مشهد",
            region = "منطقه 2",
            address = "امام رضا",
            status = Status.ACTIVE,
            5, 5,
            latitude = 36.2820,
            longitude = 59.6190
        ),

        OrganizationModel(
            5,
            name = "کیوسک اطلس", city = "مشهد",
            region = "منطقه 2",
            address = "کوهسنگی",
            status = Status.ACTIVE,
            3, 3,
            latitude = 36.2736,
            longitude = 59.5694
        ),

        OrganizationModel(
            6,
            name = "هتل الماس", city = "مشهد",
            region = "منطقه 2",
            address = "پاستور",
            status = Status.ACTIVE,
            2, 2,
            latitude = 36.2994,
            longitude = 59.5772
        ),

        OrganizationModel(
            7,
            name = "هتل وفا", city = "مشهد",
            region = "منطقه 2",
            address = "وکیل آباد",
            status = Status.ACTIVE,
            3, 3,
            latitude = 36.3312,
            longitude = 59.4851
        ),

        OrganizationModel(
            8,
            name = "سازمان مهندسی", city = "مشهد",
            region = "منطقه 2",
            address = "فاطمی",
            status = Status.ACTIVE,
            4, 4,
            latitude = 36.3078,
            longitude = 59.5935
        ),

        OrganizationModel(
            9,
            name = "هتل امیر", city = "مشهد",
            region = "منطقه 2",
            address = " رضاییه",
            status = Status.ACTIVE,
            2, 2,
            latitude = 36.2768,
            longitude = 59.6385
        ),

        OrganizationModel(
            10,
            name = "آپارتمان ملل", city = "مشهد",
            region = "منطقه 2",
            address = " ستاری",
            status = Status.ACTIVE,
            3, 3,
            latitude = 36.3421,
            longitude = 59.5208
        ),

        OrganizationModel(
            11,
            "مهمانسرا اسپیناس",
            "مشهد",
            "منطقه 2",
            "مرکزی",
            Status.INACTIVE,
            5,
            5,
            36.3051,
            59.6059
        ),

        OrganizationModel(
            12,
            name = "چالیدره", city = "مشهد",
            region = "منطقه 2",
            address = "طرقبه",
            status = Status.INACTIVE,
            2, 2,
            latitude = 36.3198,
            longitude = 59.3482
        )
    )
    var selectedOrganization by remember { mutableStateOf<OrganizationModel?>(null) }
    var showOrganizationSheet by remember { mutableStateOf(false) }
    // مقداردهی اولیه سازمان بر اساس آیدی دریافتی
    LaunchedEffect(preselectedOrganizationId) {
        if (preselectedOrganizationId != null) {
            selectedOrganization =
                organizationList.find { it.receationcenterid == preselectedOrganizationId }
        }
    }

    var cooperationModel by rememberSaveable { mutableStateOf("") }
    var isCooperationModelExpanded by remember { mutableStateOf(false) }
    val cooperationModelList = remember { listOf("پورسانتی", "بلیط تخفیف دار", "پورسانت و تخفیف") }

    var settlementPeriodType by rememberSaveable { mutableStateOf("") }
    var isSettlementPeriodTypeExpanded by remember { mutableStateOf(false) }
    val settlementPeriodTypeList =
        remember { listOf("بدون تسویه", "روزانه", "هفتگی", "ماهیانه", "سایر") }

    var contractStatus by rememberSaveable { mutableStateOf("") }
    val contractStatusList = remember {
        listOf("پیش نویس", "فعال", "معلق", "غیرفعال")
    }
    val today = remember { PersianDate() }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    var startDate by rememberSaveable {
        mutableStateOf(
            "${today.shYear}/${String.format(Locale.US, "%02d", today.shMonth)}/${
                String.format(
                    Locale.US,
                    "%02d",
                    today.shDay
                )
            }"
        )
    }
    var endDate by rememberSaveable {
        mutableStateOf(
            "${today.shYear}/${String.format(Locale.US, "%02d", today.shMonth)}/${
                String.format(
                    Locale.US,
                    "%02d",
                    today.shDay
                )
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
                    value = contractTitle,
                    onValueChange = { contractTitle = it },
                    label = stringResource(R.string.label_contract_title),
                    placeholder = stringResource(R.string.hint_enter_contract_title),
                    isRequired = true,
                    leadingIcon = null
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    CustomSelectorField(
                        value = cooperationModel,
                        label = stringResource(R.string.label_cooperation_model),
                        placeholder = stringResource(R.string.hint_choose_cooperation_model),
                        isExpanded = isCooperationModelExpanded,
                        onClick = {
                            isCooperationModelExpanded = !isCooperationModelExpanded
                        }
                    )
                    DropdownMenu(
                        expanded = isCooperationModelExpanded,
                        onDismissRequest = { isCooperationModelExpanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(appColors.cardBackground)
                    ) {
                        cooperationModelList.forEachIndexed { index, cooperationModelItem ->
                            val backgroundColor =
                                if (index % 2 == 0) appColors.cardBackground else appColors.cardBackgroundAlt
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = backgroundColor
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = cooperationModelItem,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = appColors.textPrimary
                                        )
                                    },
                                    onClick = {
                                        cooperationModel = cooperationModelItem
                                        isCooperationModelExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Box(modifier = Modifier.fillMaxWidth()) {
                    CustomSelectorField(
                        value = settlementPeriodType,
                        label = stringResource(R.string.label_settlement_period_type),
                        placeholder = stringResource(R.string.hint_choose_settlement_period_type),
                        isExpanded = isSettlementPeriodTypeExpanded,
                        onClick = {
                            isSettlementPeriodTypeExpanded = !isSettlementPeriodTypeExpanded
                        }
                    )
                    DropdownMenu(
                        expanded = isSettlementPeriodTypeExpanded,
                        onDismissRequest = { isSettlementPeriodTypeExpanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(appColors.cardBackground)
                    ) {
                        settlementPeriodTypeList.forEachIndexed { index, settlementPeriodTypeItem ->
                            val backgroundColor =
                                if (index % 2 == 0) appColors.cardBackground else appColors.cardBackgroundAlt
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = backgroundColor
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = settlementPeriodTypeItem,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = appColors.textPrimary
                                        )
                                    },
                                    onClick = {
                                        settlementPeriodType = settlementPeriodTypeItem
                                        isSettlementPeriodTypeExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

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
                    value = contractStatus,
                    label = stringResource(R.string.label_contract_Status),
                    placeholder = stringResource(R.string.hint_choose_status),
                    items = contractStatusList,
                    isRequired = false,
                    onItemSelected = { contractStatus = it }
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


                Spacer(Modifier.weight(1f))

                CustomDescriptionField(
                    label = stringResource(R.string.label_description),
                    value = description,
                    onValueChange = { description = it },
                    placeholder = stringResource(R.string.hint_description)
                )

                CustomButton(
                    text = stringResource(R.string.label_registration),
                    onClick = onSaveClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    if (showOrganizationSheet && preselectedOrganizationId == null) {
        OrganizationBottomSheet(
            list = organizationList,
            onDismiss = { showOrganizationSheet = false },
            onItemSelected = { item ->
                selectedOrganization = item
                showOrganizationSheet = false
            }
        )
    }

    if (showStartDatePicker) {
        DatePickerDialog(
            onDismiss = {
                showStartDatePicker = false
            },
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
            onDismiss = {
                showEndDatePicker = false
            },
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
        AddContractScreen(preselectedOrganizationId = -1,

            onBackClick = {},
            onSaveClick = {})
    }
}