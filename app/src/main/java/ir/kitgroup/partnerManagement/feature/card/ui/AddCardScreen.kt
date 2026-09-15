package ir.kitgroup.partnerManagement.feature.card.ui

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
import ir.kitgroup.partnerManagement.core.ui.components.*
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.Status
import ir.kitgroup.partnerManagement.feature.organization.model.OrganizationModel
import ir.kitgroup.partnerManagement.feature.organization.ui.OrganizationBottomSheet
import saman.zamani.persiandate.PersianDate
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardScreen(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit = {}
) {
    val appColors = LocalPartnerManagementColors.current

    var cardNumber by rememberSaveable { mutableStateOf("") }
    var showCollaborativeSheet by remember { mutableStateOf(false) }
    var selectedCollaborative by rememberSaveable { mutableStateOf<OrganizationModel?>(null) }
    var recipientName by rememberSaveable { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val today = remember { PersianDate() }
    var cardDate by rememberSaveable {
        mutableStateOf(
            "${today.shYear}/${String.format(Locale.US, "%02d", today.shMonth)}/${
                String.format(Locale.US, "%02d", today.shDay)
            }"
        )
    }

    val calendar = remember { Calendar.getInstance() }
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = calendar.get(Calendar.MINUTE)

    var cardTime by rememberSaveable {
        mutableStateOf(String.format(Locale.US, "%02d:%02d", currentHour, currentMinute))
    }

    val timePickerState = rememberTimePickerState(
        initialHour = currentHour,
        initialMinute = currentMinute,
        is24Hour = true
    )

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
            11, "مهمانسرا اسپیناس", "مشهد", "منطقه 2", "مرکزی", Status.INACTIVE, 5, 5, 36.3051, 59.6059
        ),

        OrganizationModel(
            12,
            name = "چالیدره",     city = "مشهد",
            region = "منطقه 2",
            address = "طرقبه",
            status = Status.INACTIVE,
            2, 2,
            latitude = 36.3198,
            longitude = 59.3482
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomHeader(
                title = R.string.label_registration_delivery_card,
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
                    CustomEditTextField(
                        value = cardNumber,
                        onValueChange = { cardNumber = it },
                        label = stringResource(R.string.label_card_number),
                        placeholder = stringResource(R.string.hint_enter_card_number),
                        leadingIcon = null,
                    )

                    CustomSelectorField(
                        value = selectedCollaborative?.name ?: "",
                        label = stringResource(R.string.label_organization_name),
                        placeholder = stringResource(R.string.hint_choose_organization_name),
                        isExpanded = showCollaborativeSheet,
                        onClick = { showCollaborativeSheet = true }
                    )

                    CustomEditTextField(
                        value = recipientName,
                        onValueChange = { recipientName = it },
                        label = stringResource(R.string.label_recipient_name),
                        placeholder = stringResource(R.string.hint_enter_recipient_name),
                        leadingIcon = null,
                    )

                    // فیلد تاریخ و ساعت
                    CustomDateTimeFields(
                        label = stringResource(R.string.label_date_time),
                        date = cardDate,
                        onDateClick = { showDatePicker = true },
                        time = cardTime,
                        onTimeClick = { showTimePicker = true }
                    )

                    CustomDescriptionField(
                        label = stringResource(R.string.label_description),
                        value = description,
                        onValueChange = { description = it },
                        placeholder = stringResource(R.string.hint_description)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                CustomButton(
                    text = stringResource(R.string.label_registration),
                    onClick = onSaveClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismiss = {
                showDatePicker = false
            },
            onDateSelected = { date ->
                cardDate = "${date.year}/${String.format(Locale.US, "%02d", date.month)}/${
                    String.format(Locale.US, "%02d", date.day)
                }"
                showDatePicker = false
            }
        )
    }

    if (showTimePicker) {
        TimePickerDialog(
            timePickerState = timePickerState,
            onConfirm = { hour, minute ->
                cardTime = String.format(Locale.US, "%02d:%02d", hour, minute)
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }

    if (showCollaborativeSheet) {
        OrganizationBottomSheet(
            list = organizationList,
            onDismiss = { showCollaborativeSheet = false },
            onItemSelected = { item ->
                selectedCollaborative = item
                showCollaborativeSheet = false
            }
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun AddCardScreenPreview() {
        AppScreenPreview {
            AddCardScreen(
                onBackClick = {},
                onSaveClick = {}
            )
        }
}
