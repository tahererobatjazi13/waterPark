package ir.kitgroup.partnerManagement.feature.card.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.*
import saman.zamani.persiandate.PersianDate
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.Status
import ir.kitgroup.partnerManagement.feature.organization.model.OrganizationModel
import ir.kitgroup.partnerManagement.feature.organization.ui.OrganizationBottomSheet
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

    val collaborativelist = remember {
        listOf(
            OrganizationModel(1, "هتل پارسیان آزادی", "مشهد،یوسفی", Status.ACTIVE, 5),
            OrganizationModel(2, " مجموعه پالاس", "مشهد،قاسم آباد", Status.ACTIVE, 4),
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
    }

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
            list = collaborativelist,
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
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme {
            AddCardScreen(
                onBackClick = {},
                onSaveClick = {}
            )
        }
    }
}
