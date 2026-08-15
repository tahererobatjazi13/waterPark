package ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand_assignment_visitor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.traceEventEnd
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomDateTimeFields
import ir.kitgroup.partnerManagement.core.ui.components.CustomDescriptionField
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.CustomSelectorField
import ir.kitgroup.partnerManagement.core.ui.components.DatePickerDialog
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.feature.advertising_stand.model.AdvertisingStandItem
import ir.kitgroup.partnerManagement.feature.advertising_stand.model.AdvertisingStandItemUi
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.AdvertisingStandBottomSheet
import saman.zamani.persiandate.PersianDate
import java.util.Locale

@Composable
fun AddAdvertisingStandAssignmentVisitorScreen(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    var selectedStand by rememberSaveable { mutableStateOf<AdvertisingStandItem?>(null) }
    var showStandSheet by remember { mutableStateOf(false) }

    val standList = listOf(
        AdvertisingStandItem(
            "1", "استند رومیزی",
            "استند رومیزی", "پرتال", "دیجیتال", ""
        ),
        AdvertisingStandItem(
            "2",
            "بروشور معرفی",
            "استند بروشور",
            "ایستاده", "چاپی", "توضیحات استند"
        ),
        AdvertisingStandItem("3", "استند لابی", "کیوسک", "ثابت", "دیجیتال", "")
    )
    var assignmentType by rememberSaveable { mutableStateOf("") }
    val assignmentTypeList =
        remember { listOf("تخصیص به بازاریاب", "عودت", "جمع آوری", "خاتمه") }

    var assignmentMode by rememberSaveable { mutableStateOf("") }
    val assignmentModeList =
        remember { listOf("تبلیغاتی ", "امانی", "اجاره ای") }

    var visitorName by rememberSaveable { mutableStateOf("") }
    val visitorsList = remember { listOf("علی علوی", "رضا رضایی", "محمد محمدی", "حسین حسینی") }

    var description by rememberSaveable { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    var showPlannedReturnDatePicker by remember { mutableStateOf(false) }
    var showActualReturnDatePicker by remember { mutableStateOf(false) }


    val today = remember { PersianDate() }
    var deliveryVisitorDate by rememberSaveable {
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
        modifier = modifier.fillMaxSize(),
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
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
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

                    DropdownSelectorField(
                        value = visitorName,
                        label = stringResource(R.string.label_choose_visitor_recipient),
                        placeholder = stringResource(R.string.hint_choose_visitor),
                        items = visitorsList,
                        isRequired = true,
                        onItemSelected = { visitorName = it }
                    )

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
                        label = stringResource(R.string.label_delivery_visitor_date),
                        date = deliveryVisitorDate,
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
                    // فیلد توضیحات
                    CustomDescriptionField(
                        label = stringResource(R.string.label_description),
                        value = description,
                        onValueChange = { description = it },
                        placeholder = stringResource(R.string.hint_description)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                CustomButton(
                    text = stringResource(R.string.label_allocation_record),
                    onClick = onSaveClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )
            }
        }
    }

    if (showStandSheet) {
        AdvertisingStandBottomSheet(
            list = standList,
            onDismiss = { showStandSheet = false },
            onItemSelected = { item ->
                selectedStand = item
                showStandSheet = false
            }
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismiss = {
                showDatePicker = false
            },
            onDateSelected = { date ->
                deliveryVisitorDate =
                    "${date.year}/${String.format(Locale.US, "%02d", date.month)}/${
                        String.format(
                            Locale.US,
                            "%02d",
                            date.day
                        )
                    }"
                showDatePicker = false
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

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun AddAdvertisingStandAssignmentVisitorScreenPreview() {
    AppScreenPreview {
        AddAdvertisingStandAssignmentVisitorScreen(
            onBackClick = {},
            onSaveClick = {}
        )
    }
}
