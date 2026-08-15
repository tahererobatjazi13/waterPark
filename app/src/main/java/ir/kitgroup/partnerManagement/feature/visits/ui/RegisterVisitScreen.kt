package ir.kitgroup.partnerManagement.feature.visits.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.*
import saman.zamani.persiandate.PersianDate
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.ui.tooling.preview.Preview
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.Status
import ir.kitgroup.partnerManagement.feature.organization.model.OrganizationModel
import ir.kitgroup.partnerManagement.feature.organization.ui.OrganizationBottomSheet
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterVisitScreen(
    visitId: Int = -1,
    onBackClick: () -> Unit,
    onSubmitClick: () -> Unit = {}
) {
    val isEditMode = visitId != -1

    var visitType by rememberSaveable { mutableStateOf("") }
    val visitTypeList =
        remember { listOf("تلفنی", "حضوری برنامه ریزی شده", "حضوری غیر برنامه ریزی شده") }
    val isScheduledInPersonVisit = visitType == "حضوری برنامه ریزی شده"
    val isUnscheduledInPersonVisit = visitType == "حضوری غیر برنامه ریزی شده"

    var visitSubject by rememberSaveable { mutableStateOf("") }
    val visitSubjectList =
        remember { listOf("ثبت نام", "وصول مطالبات", "تحویل استند", "سایر") }


    val today = remember { PersianDate() }
    var showVisitScheduledDatePicker by remember { mutableStateOf(false) }

    var visitScheduledDate by rememberSaveable {
        mutableStateOf(
            "${today.shYear}/${String.format(Locale.US, "%02d", today.shMonth)}/${
                String.format(Locale.US, "%02d", today.shDay)
            }"
        )
    }
    var showOrganizationSheet by remember { mutableStateOf(false) }
    var selectedOrganization by rememberSaveable { mutableStateOf<OrganizationModel?>(null) }

    var showVisitRealDatePicker by remember { mutableStateOf(false) }
    var showVisitRealTimePicker by remember { mutableStateOf(false) }
    var showVisitScheduledTimePicker by remember { mutableStateOf(false) }

    var visitRealDate by rememberSaveable {
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

    var visitRealTime by rememberSaveable {
        mutableStateOf(String.format(Locale.US, "%02d:%02d", currentHour, currentMinute))
    }
    val visitScheduledTime by rememberSaveable {
        mutableStateOf(String.format(Locale.US, "%02d:%02d", currentHour, currentMinute))
    }
    val timePickerState = rememberTimePickerState(
        initialHour = currentHour,
        initialMinute = currentMinute,
        is24Hour = true
    )

    var visitorName by rememberSaveable { mutableStateOf("") }
    val visitorsList = remember { listOf("علی علوی", "رضا رضایی", "محمد محمدی", "حسین حسینی") }
    var person by rememberSaveable { mutableStateOf("") }
    val personList = remember { listOf("علی علوی", "رضا رضایی", "محمد محمدی", "حسین حسینی") }

    var status by mutableStateOf("فعال")

    val statusList = remember {
        listOf("برنامه ریزی شده", "انجام شده", "لغو شده", "سایر")
    }

    var description by rememberSaveable { mutableStateOf("") }

    val appColors = LocalPartnerManagementColors.current

    val organizationList = listOf(

        OrganizationModel(
            1,
            "هتل پارسیان آزادی", "مشهد،یوسفی", Status.ACTIVE,
            5
        ),
        OrganizationModel(
            2,
            " مجموعه پالاس", "مشهد،قاسم آباد", Status.ACTIVE, 4
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


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomHeader(
                title =
                if (isEditMode)
                    R.string.label_edit_visit
                else
                    R.string.label_submit_visit,
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
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                DropdownSelectorField(
                    value = visitType,
                    label = stringResource(R.string.label_visit_type),
                    placeholder = stringResource(R.string.hint_choose_visit_type),
                    items = visitTypeList,
                    isRequired = true,
                    onItemSelected = { visitType = it }
                )
                DropdownSelectorField(
                    value = visitSubject,
                    label = stringResource(R.string.label_visit_subject),
                    placeholder = stringResource(R.string.hint_enter_visit_subject),
                    items = visitSubjectList,
                    isRequired = true,
                    onItemSelected = { visitSubject = it }
                )

                CustomSelectorField(
                    value = selectedOrganization?.name ?: "",
                    label = stringResource(R.string.label_organization_name),
                    placeholder = stringResource(R.string.hint_choose_organization_name),
                    isExpanded = showOrganizationSheet,
                    onClick = {
                        showOrganizationSheet = true
                    }
                )

                if (isScheduledInPersonVisit) {
                    CustomDateTimeFields(
                        label = stringResource(R.string.label_visit_Scheduled_date),
                        date = visitScheduledDate,
                        onDateClick = { showVisitScheduledDatePicker = true },
                        time = visitScheduledTime,
                        onTimeClick = { showVisitScheduledTimePicker = true }
                    )

                    DropdownSelectorField(
                        value = visitorName,
                        label = stringResource(R.string.label_visitor_name),
                        placeholder = stringResource(R.string.hint_choose_visitor),
                        items = visitorsList,
                        isRequired = true,
                        onItemSelected = { visitorName = it }
                    )
                }

                if (!isScheduledInPersonVisit) {

                    DropdownSelectorField(
                        value = person,
                        label = stringResource(R.string.label_organization_person),
                        placeholder = stringResource(R.string.hint_choose_organization_person),
                        items = personList,
                        isRequired = true,
                        onItemSelected = { person = it }
                    )
                }
                CustomDateTimeFields(
                    label = stringResource(R.string.label_visit_real_date_time),
                    date = visitRealDate,
                    onDateClick = { showVisitRealDatePicker = true },
                    time = visitRealTime,
                    onTimeClick = { showVisitRealTimePicker = true },
                )

                DropdownSelectorField(
                    value = status,
                    label = stringResource(R.string.label_visit_status),
                    placeholder = "",
                    items = statusList,
                    isRequired = false,
                    onItemSelected = { status = it }
                )

                CustomDescriptionField(
                    label = stringResource(R.string.label_description),
                    value = description,
                    onValueChange = { description = it },
                    placeholder = stringResource(R.string.hint_description)
                )

                if (isUnscheduledInPersonVisit) {
                    ImageUploadField(
                        label = stringResource(R.string.label_visit_image)
                    )

                    GpsLocationField(
                        label = stringResource(R.string.label_register_gps)
                    )
                }
                CustomButton(
                    text = if (isEditMode) {
                        stringResource(R.string.label_edit_visit)
                    } else {
                        stringResource(R.string.label_submit_visit)
                    },
                    onClick = onSubmitClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )

            }
        }
    }
    if (showVisitRealTimePicker) {
        TimePickerDialog(
            timePickerState = timePickerState,
            onConfirm = { hour, minute ->
                visitRealTime = String.format(Locale.US, "%02d:%02d", hour, minute)
                showVisitRealTimePicker = false
            },
            onDismiss = { showVisitRealTimePicker = false }
        )
    }

    if (showVisitRealDatePicker) {
        DatePickerDialog(
            onDismiss = {
                showVisitRealDatePicker = false
            },
            onDateSelected = { date ->
                visitRealDate = "${date.year}/${String.format(Locale.US, "%02d", date.month)}/${
                    String.format(Locale.US, "%02d", date.day)
                }"
                showVisitRealDatePicker = false
            }
        )
    }

    if (showVisitScheduledDatePicker) {
        DatePickerDialog(
            onDismiss = {
                showVisitScheduledDatePicker = false
            },
            onDateSelected = { date ->
                visitScheduledDate =
                    "${date.year}/${String.format(Locale.US, "%02d", date.month)}/${
                        String.format(Locale.US, "%02d", date.day)
                    }"
                showVisitScheduledDatePicker = false
            }
        )
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
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun RegisterVisitScreenPreview() {
    AppScreenPreview {
        RegisterVisitScreen(
            visitId = -1,
            onBackClick = {},
            onSubmitClick = {}
        )
    }
}

