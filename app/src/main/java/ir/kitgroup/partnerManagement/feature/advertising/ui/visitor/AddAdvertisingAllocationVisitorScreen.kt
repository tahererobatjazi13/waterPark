package ir.kitgroup.partnerManagement.feature.advertising.ui.visitor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
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
import ir.kitgroup.partnerManagement.core.ui.components.DatePickerDialog
import ir.kitgroup.partnerManagement.core.ui.components.TimePickerDialog
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import saman.zamani.persiandate.PersianDate
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAdvertisingAllocationVisitorScreen(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val visitorsList = remember { listOf("علی علوی", "رضا رضایی", "محمد محمدی", "حسین حسینی") }
    val standTypesList =
        remember { listOf("استند ایستاده A", "استند رومیزی B", "استند چرخ‌دار C", "استند نوری D") }

    var visitorName by rememberSaveable { mutableStateOf("") }
    var itemType by rememberSaveable { mutableStateOf("") }
    var count by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    var isVisitorExpanded by remember { mutableStateOf(false) }
    var isItemTypeExpanded by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val today = remember { PersianDate() }
    var allocationDate by rememberSaveable {
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
                    // انتخاب‌گر ویزیتور به همراه DropdownMenu
                    Box(modifier = Modifier.fillMaxWidth()) {
                        CustomSelectorField(
                            value = visitorName,
                            label = stringResource(R.string.label_choose_visitor),
                            placeholder = stringResource(R.string.hint_choose_visitor),
                            isExpanded = isVisitorExpanded,
                            onClick = {
                                isVisitorExpanded = !isVisitorExpanded
                                isItemTypeExpanded = false
                            }
                        )
                        DropdownMenu(
                            expanded = isVisitorExpanded,
                            onDismissRequest = { isVisitorExpanded = false },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(appColors.cardBackground)
                        ) {
                            visitorsList.forEachIndexed { index, visitor ->
                                val backgroundColor =
                                    if (index % 2 == 0) appColors.cardBackground else appColors.cardBackgroundAlt
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = backgroundColor
                                ) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = visitor,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = appColors.textPrimary
                                            )
                                        },
                                        onClick = {
                                            visitorName = visitor
                                            isVisitorExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // انتخاب‌گر نوع استند به همراه DropdownMenu
                    Box(modifier = Modifier.fillMaxWidth()) {
                        CustomSelectorField(
                            value = itemType,
                            label = stringResource(R.string.label_type_stand),
                            placeholder = stringResource(R.string.hint_choose_stand),
                            isExpanded = isItemTypeExpanded,
                            onClick = {
                                isItemTypeExpanded = !isItemTypeExpanded
                                isVisitorExpanded = false
                            }
                        )
                        DropdownMenu(
                            expanded = isItemTypeExpanded,
                            onDismissRequest = { isItemTypeExpanded = false },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(appColors.cardBackground)
                        ) {
                            standTypesList.forEachIndexed { index, stand ->
                                val backgroundColor =
                                    if (index % 2 == 0) appColors.cardBackground else appColors.cardBackgroundAlt
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = backgroundColor
                                ) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = stand,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = appColors.textPrimary
                                            )
                                        },
                                        onClick = {
                                            itemType = stand
                                            isItemTypeExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // فیلد تعداد
                    CustomEditTextField(
                        value = count,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() }) {
                                count = newValue
                            }
                        },
                        label = stringResource(R.string.label_allocated_count),
                        placeholder = stringResource(R.string.hint_allocated_count),
                        leadingIcon = rememberVectorPainter(Icons.Default.Numbers),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )

                    // فیلد تاریخ و ساعت
                    CustomDateTimeFields(
                        label = stringResource(R.string.label_allocation_date_time),
                        date = allocationDate,
                        onDateClick = { showDatePicker = true },
                        time = allocationTime,
                        onTimeClick = { showTimePicker = true }
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

    if (showTimePicker) {
        TimePickerDialog(
            timePickerState = timePickerState,
            onConfirm = { hour, minute ->
                allocationTime = String.format(Locale.US, "%02d:%02d", hour, minute)
                showTimePicker = false
            },
            onDismiss = {
                showTimePicker = false
            }
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismiss = {
                showDatePicker = false
            },
            onDateSelected = { date ->
                allocationDate = "${date.year}/${String.format(Locale.US, "%02d", date.month)}/${
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
}


@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun AddAdvertisingAllocationVisitorScreenPreview() {
    AppScreenPreview {
        AddAdvertisingAllocationVisitorScreen(
            onBackClick = {},
            onSaveClick = {}
        )
    }
}
