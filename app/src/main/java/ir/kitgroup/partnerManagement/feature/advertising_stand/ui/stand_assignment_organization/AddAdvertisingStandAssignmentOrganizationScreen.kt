package ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand_assignment_organization

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomDateTimeFields
import ir.kitgroup.partnerManagement.core.ui.components.CustomDescriptionField
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.CustomSelectorField
import ir.kitgroup.partnerManagement.core.ui.components.DatePickerDialog
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.feature.advertising_stand.model.AdvertisingStandItemUi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.Status
import ir.kitgroup.partnerManagement.feature.organization.model.OrganizationModel
import ir.kitgroup.partnerManagement.feature.organization.ui.OrganizationBottomSheet
import saman.zamani.persiandate.PersianDate
import java.util.Locale

@Composable
fun AddAdvertisingStandAssignmentOrganizationScreen(
    preselectedOrganizationId: Int? = null,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    var assignmentType by rememberSaveable { mutableStateOf("") }
    val assignmentTypeList =
        remember { listOf("تخصیص به سازمان", "عودت", "جمع آوری", "خاتمه") }

    var assignmentMode by rememberSaveable { mutableStateOf("") }
    val assignmentModeList =
        remember { listOf("تبلیغاتی ", "امانی", "اجاره ای") }

    var description by rememberSaveable { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var showPlannedReturnDatePicker by remember { mutableStateOf(false) }
    var showActualReturnDatePicker by remember { mutableStateOf(false) }

    val today = remember { PersianDate() }
    var deliveryOrganizationDate by rememberSaveable {
        mutableStateOf(
            "${today.shYear}/${String.format(Locale.US, "%02d", today.shMonth)}/${
                String.format(Locale.US, "%02d", today.shDay)
            }"
        )
    }
    var plannedReturnDate by rememberSaveable {
        mutableStateOf(
            "${today.shYear}/${String.format(Locale.US, "%02d", today.shMonth)}/${
                String.format(Locale.US, "%02d", today.shDay)
            }"
        )
    }

    var actualReturnDate by rememberSaveable {
        mutableStateOf(
            "${today.shYear}/${String.format(Locale.US, "%02d", today.shMonth)}/${
                String.format(Locale.US, "%02d", today.shDay)
            }"
        )
    }
    val isTrustMode = assignmentMode.trim() == "امانی"
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
    LaunchedEffect(preselectedOrganizationId) {
        if (preselectedOrganizationId != null && preselectedOrganizationId != -1) {
            selectedOrganization =
                organizationList.find { it.receationcenterid == preselectedOrganizationId }
        }
    }

    val items = remember {
        mutableStateListOf(
            AdvertisingStandItemUi(
                title = "استند رومیزی",
                available = 15,
                delivered = 0,
                selected = false
            ),
            AdvertisingStandItemUi(
                title = "بنر",
                available = 8,
                delivered = 0,
                selected = false
            ),
            AdvertisingStandItemUi(
                title = "کیوسک",
                available = 3,
                delivered = 0,
                selected = false
            )
        )
    }
    var status by mutableStateOf("فعال")

    val statusList = remember {
        listOf("پیش نویس", "فعال", "عودت شده", "لغو شده")
    }
    val appColors = LocalPartnerManagementColors.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomHeader(
                title = R.string.label_allocation_record,
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
            shape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp
            ),
            color = appColors.screenBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    DropdownSelectorField(
                        value = assignmentType,
                        label = stringResource(R.string.label_assignment_type),
                        placeholder = stringResource(R.string.hint_choose_assignment_type),
                        items = assignmentTypeList,
                        isRequired = true,
                        onItemSelected = { assignmentType = it }
                    )

                    DropdownSelectorField(
                        value = assignmentMode,
                        label = stringResource(R.string.label_assignment_mode),
                        placeholder = stringResource(R.string.hint_choose_assignment_mode),
                        items = assignmentModeList,
                        isRequired = true,
                        onItemSelected = { assignmentMode = it }
                    )
                    if (preselectedOrganizationId == null) {

                        CustomSelectorField(
                            value = selectedOrganization?.name ?: "",
                            label = stringResource(R.string.label_organization_receiving_name),
                            placeholder = stringResource(R.string.hint_choose_organization_name),
                            isExpanded = showOrganizationSheet,
                            onClick = {
                                showOrganizationSheet = true
                            }
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SectionTitle(
                            stringResource(R.string.label_advertising_stands),
                            Icons.Filled.Checklist
                        )

                        items.forEachIndexed { index, item ->
                            AdvertisingItemCard(
                                item = item,
                                onToggleSelected = {
                                    items[index] = item.copy(selected = !item.selected)
                                },
                                onIncrease = {
                                    if (item.delivered < item.available) {
                                        items[index] =
                                            item.copy(
                                                delivered = item.delivered + 1,
                                                selected = true
                                            )
                                    }
                                },
                                onDecrease = {
                                    if (item.delivered > 0) {
                                        val newValue = item.delivered - 1
                                        items[index] = item.copy(
                                            delivered = newValue,
                                            selected = newValue > 0
                                        )
                                    }
                                }
                            )
                        }
                    }

                    CustomDateTimeFields(
                        label = stringResource(R.string.label_delivery_organization_date),
                        date = deliveryOrganizationDate,
                        onDateClick = { showDatePicker = true },
                        showTime = false
                    )
                    if (isTrustMode) {
                        CustomDateTimeFields(
                            label = stringResource(R.string.label_planned_return_date),
                            date = plannedReturnDate,
                            onDateClick = { showPlannedReturnDatePicker = true },
                            showTime = false
                        )

                        CustomDateTimeFields(
                            label = stringResource(R.string.label_actual_return_date),
                            date = actualReturnDate,
                            onDateClick = { showActualReturnDatePicker = true },
                            showTime = false
                        )
                    }
                    DropdownSelectorField(
                        value = status,
                        label = stringResource(R.string.label_status),
                        placeholder = "",
                        items = statusList,
                        isRequired = false,
                        onItemSelected = { status = it }
                    )
                    CustomDescriptionField(
                        label = stringResource(R.string.label_description),
                        value = description,
                        onValueChange = { newValue ->
                            description = newValue
                        },
                        placeholder = stringResource(R.string.hint_description)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    CustomButton(
                        text = stringResource(R.string.label_allocation_record),
                        onClick = onSaveClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }


    if (showDatePicker) {
        DatePickerDialog(
            onDismiss = {
                showDatePicker = false
            },
            onDateSelected = { date ->
                deliveryOrganizationDate =
                    "${date.year}/${String.format(Locale.US, "%02d", date.month)}/${
                        String.format(Locale.US, "%02d", date.day)
                    }"
                showDatePicker = false
            }
        )
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

    if (showPlannedReturnDatePicker) {
        DatePickerDialog(
            onDismiss = {
                showPlannedReturnDatePicker = false
            },
            onDateSelected = { date ->
                plannedReturnDate =
                    "${date.year}/${String.format(Locale.US, "%02d", date.month)}/${
                        String.format(Locale.US, "%02d", date.day)
                    }"
                showPlannedReturnDatePicker = false
            }
        )
    }

    if (showActualReturnDatePicker) {
        DatePickerDialog(
            onDismiss = {
                showActualReturnDatePicker = false
            },
            onDateSelected = { date ->
                actualReturnDate =
                    "${date.year}/${String.format(Locale.US, "%02d", date.month)}/${
                        String.format(Locale.US, "%02d", date.day)
                    }"
                showActualReturnDatePicker = false
            }
        )
    }

}

@Composable
private fun AdvertisingItemCard(
    item: AdvertisingStandItemUi,
    onToggleSelected: () -> Unit,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {

    val cardBackground =
        if (item.selected) LocalPartnerManagementColors.current.cardBackgroundAlt else LocalPartnerManagementColors.current.cardBackground

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(cardBackground, RoundedCornerShape(16.dp))
            .border(1.dp, LocalPartnerManagementColors.current.border, RoundedCornerShape(16.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.selected,
            onCheckedChange = { onToggleSelected() },
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = LocalPartnerManagementColors.current.textSecondary
            )
        )

        Spacer(modifier = Modifier.width(6.dp))


        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = item.title,
                color = MaterialTheme.colorScheme.primary,
                style = typography.titleMedium,
                textAlign = TextAlign.Start,
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.label_delivery_quantity),
                color = LocalPartnerManagementColors.current.textPrimary,
                style = typography.labelSmall,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                CounterButton(
                    text = "+",
                    onClick = onIncrease
                )

                CounterValue(
                    value = item.delivered
                )

                CounterButton(
                    text = "−",
                    onClick = onDecrease
                )
            }
        }
    }
}

@Composable
private fun CounterButton(
    text: String, onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .size(width = 36.dp, height = 36.dp)
            .border(1.dp, LocalPartnerManagementColors.current.border, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.secondary,
            style = typography.titleMedium,
        )
    }
}

@Composable
private fun CounterValue(
    value: Int
) {
    Box(
        modifier = Modifier
            .width(48.dp)
            .height(36.dp)
            .border(1.dp, LocalPartnerManagementColors.current.border, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value.toString(),
            style = typography.titleMedium,
            color = LocalPartnerManagementColors.current.textPrimary,
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun AddAdvertisingStandAssignmentOrganizationScreenPreview() {
    AppScreenPreview {
        AddAdvertisingStandAssignmentOrganizationScreen(
            onBackClick = {},
            onSaveClick = {}
        )
    }
}
