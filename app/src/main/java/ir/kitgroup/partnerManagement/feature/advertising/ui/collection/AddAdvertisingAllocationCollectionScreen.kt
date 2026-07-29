package ir.kitgroup.partnerManagement.feature.advertising.ui.collection

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
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberTimePickerState
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
import ir.kitgroup.partnerManagement.core.ui.components.TimePickerDialog
import ir.kitgroup.partnerManagement.feature.advertising.model.AdvertisingItemUi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.Status
import ir.kitgroup.partnerManagement.feature.collaborative_collection.model.CollectionModel
import ir.kitgroup.partnerManagement.feature.collaborative_collection.ui.CollaborativeBottomSheet
import saman.zamani.persiandate.PersianDate
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAdvertisingAllocationCollectionScreen(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showCollaborativeSheet by remember { mutableStateOf(false) }
    var selectedCollaborative by rememberSaveable { mutableStateOf<CollectionModel?>(null) }

    var description by rememberSaveable { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val today = remember { PersianDate() }
    var allocationDate by rememberSaveable {
        mutableStateOf(
            "${today.shYear}/${String.format(Locale.US, "%02d", today.shMonth)}/${
                String.format(Locale.US, "%02d", today.shDay)
            }"
        )
    }

    val calendar = remember { Calendar.getInstance() }
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = calendar.get(Calendar.MINUTE)

    var allocationTime by rememberSaveable {
        mutableStateOf(String.format(Locale.US, "%02d:%02d", currentHour, currentMinute))
    }

    val timePickerState = rememberTimePickerState(
        initialHour = currentHour,
        initialMinute = currentMinute,
        is24Hour = true
    )

    val collaborativelist = remember {
        listOf(
            CollectionModel(1, "هتل پارسیان آزادی", "مشهد، یوسفی", Status.ACTIVE, 5),
            CollectionModel(2, "مجموعه پالاس", "مشهد، قاسم آباد", Status.ACTIVE, 4),
            CollectionModel(3, "سازمان نوید", "مشهد، پیروزی", Status.ACTIVE, 3),
            CollectionModel(4, "هتل مرکزی", "مشهد، امام رضا", Status.ACTIVE, 5),
            CollectionModel(5, "کیوسک اطلس", "مشهد، کوهسنگی", Status.ACTIVE, 3),
            CollectionModel(6, "هتل الماس", "مشهد، پاستور", Status.ACTIVE, 2),
            CollectionModel(7, "هتل وفا", "مشهد، وکیل آباد", Status.ACTIVE, 3),
            CollectionModel(8, "سازمان مهندسی", "مشهد، فاطمی", Status.ACTIVE, 4),
            CollectionModel(9, "هتل امیر", "مشهد، رضاییه", Status.ACTIVE, 2),
            CollectionModel(10, "آپارتمان ملل", "مشهد، ستاری", Status.ACTIVE, 3),
            CollectionModel(11, "مهمانسرا اسپیناس", "مشهد، مرکزی", Status.INACTIVE, 5),
            CollectionModel(12, "چالیدره", "مشهد، طرقبه", Status.INACTIVE, 2)
        )
    }

    val items = remember {
        mutableStateListOf(
            AdvertisingItemUi(
                title = "استند رومیزی",
                available = 15,
                delivered = 0,
                selected = false,
                icon = Icons.Filled.ViewInAr
            ),
            AdvertisingItemUi(
                title = "بنر",
                available = 8,
                delivered = 0,
                selected = false,
                icon = Icons.Filled.Storefront
            ),
            AdvertisingItemUi(
                title = "کیوسک",
                available = 3,
                delivered = 0,
                selected = false,
                icon = Icons.Filled.LocationCity
            )
        )
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
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CustomSelectorField(
                        value = selectedCollaborative?.name ?: "",
                        label = stringResource(R.string.label_name_collaborative),
                        placeholder = stringResource(R.string.hint_choose_collaborative),
                        isExpanded = showCollaborativeSheet,
                        onClick = {
                            showCollaborativeSheet = true
                        }
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SectionTitle(
                            stringResource(R.string.label_advertising),
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
                        label = stringResource(R.string.label_allocation_date_time),
                        date = allocationDate,
                        onDateClick = { showDatePicker = true },
                        time = allocationTime,
                        onTimeClick = { showTimePicker = true }
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

    if (showTimePicker) {
        TimePickerDialog(
            timePickerState = timePickerState,
            onConfirm = { hour, minute ->
                allocationTime = String.format(Locale.US, "%02d:%02d", hour, minute)
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismiss = {
                showDatePicker = false
            },
            onDateSelected = { date ->
                allocationDate = "${date.year}/${String.format(Locale.US, "%02d", date.month)}/${
                    String.format(Locale.US, "%02d", date.day)
                }"
                showDatePicker = false
            }
        )
    }

    if (showCollaborativeSheet) {
        CollaborativeBottomSheet(
            list = collaborativelist,
            onDismiss = { showCollaborativeSheet = false },
            onItemSelected = { item ->
                selectedCollaborative = item
                showCollaborativeSheet = false
            }
        )
    }
}

@Composable
private fun AdvertisingItemCard(
    item: AdvertisingItemUi,
    onToggleSelected: () -> Unit,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {

    // رنگ پس‌زمینه در صورت فعال بودن تغییر می‌کند اما حاشیه (border) ثابت می‌ماند
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
                checkedColor = MaterialTheme.colorScheme.primary, // تغییر به پرایمری در صورت انتخاب شدن
                uncheckedColor = LocalPartnerManagementColors.current.textSecondary
            )
        )

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    if (item.selected) LocalPartnerManagementColors.current.cardBackground else LocalPartnerManagementColors.current.cardBackgroundAlt,
                    RoundedCornerShape(24.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = item.title,
                color = LocalPartnerManagementColors.current.textPrimary,
                style = typography.titleMedium,
                textAlign = TextAlign.Start,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${stringResource(R.string.label_stock)}:",
                    color = LocalPartnerManagementColors.current.textSecondary,
                    style = typography.labelSmall,
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "${item.available}",
                    color = LocalPartnerManagementColors.current.textPrimary,
                    style = typography.titleMedium,
                )
            }
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
private fun AddAdvertisingAllocationCollectionScreenPreview() {
    AppScreenPreview {
        AddAdvertisingAllocationCollectionScreen(
            onBackClick = {},
            onSaveClick = {}
        )
    }
}
