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
    onBackClick: () -> Unit, onSaveClick: () -> Unit,
) {
    val appColors = LocalPartnerManagementColors.current
    var selectedOrganization by rememberSaveable { mutableStateOf<OrganizationModel?>(null) }
    var showOrganizationSheet by remember { mutableStateOf(false) }
    var contractTitle by rememberSaveable { mutableStateOf("") }
    var defaultCommissionPercent by rememberSaveable { mutableStateOf("") }
    var defaultDiscountPercent by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    val organizationList = listOf(

        OrganizationModel(
            1,
            "هتل پارسیان آزادی", "مشهد،یوسفی", Status.ACTIVE,
            5
        ),
        OrganizationModel(
            2,
            " سازمان پالاس", "مشهد،قاسم آباد", Status.ACTIVE, 4
        ),
        OrganizationModel(3, "سازمان نوید", "مشهد،پیروزی", Status.ACTIVE, 3),
        OrganizationModel(4, "هتل مرکزی", "مشهد، امام رضا", Status.ACTIVE, 5),
        OrganizationModel(5, "کیوسک اطلس", "مشهد، کوهسنگی", Status.ACTIVE, 3),
        OrganizationModel(6, "هتل الماس", "مشهد، پاستور", Status.ACTIVE, 2),
        OrganizationModel(7, "هتل وفا", "مشهد، وکیال آباد", Status.ACTIVE, 3),
        OrganizationModel(8, "سازمان مهندسی", "مشهد، فاطمی", Status.ACTIVE, 4),
        OrganizationModel(9, "هتل امیر", "مشهد، رضاییه", Status.ACTIVE, 2),
        OrganizationModel(10, "آپارتمان ملل", "مشهد، ستاری", Status.ACTIVE, 3),
        OrganizationModel(11, "مهمانسرا اسپیناس", "مشهد، مرکزی", Status.INACTIVE, 5),
        OrganizationModel(12, "چالیدره", "مشهد، طرقبه", Status.INACTIVE, 2)
    )

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
                CustomSelectorField(
                    value = selectedOrganization?.name ?: "",
                    label = stringResource(R.string.label_organization_name),
                    placeholder = stringResource(R.string.hint_choose_organization_name),
                    isExpanded = showOrganizationSheet,
                    onClick = { showOrganizationSheet = true }
                )

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

        if (showOrganizationSheet) {
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
        AddContractScreen(
            onBackClick = {},
            onSaveClick = {})
    }
}