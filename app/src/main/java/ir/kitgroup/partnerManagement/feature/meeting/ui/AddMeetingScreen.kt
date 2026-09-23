package ir.kitgroup.partnerManagement.feature.meeting.ui

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
import ir.kitgroup.partnerManagement.core.database.entity.MeetingEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationEntity
import ir.kitgroup.partnerManagement.core.ui.components.*
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.MeetingStatus
import ir.kitgroup.partnerManagement.core.ui.util.MeetingType
import ir.kitgroup.partnerManagement.core.ui.util.demoMeetings
import ir.kitgroup.partnerManagement.core.ui.util.demoOrganizations
import ir.kitgroup.partnerManagement.feature.organization.ui.OrganizationBottomSheet
import saman.zamani.persiandate.PersianDate
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMeetingScreen(
    meetingId: String?,
    preselectedOrganizationId: String?,
    onBackClick: () -> Unit,
    onSubmitClick: (MeetingEntity) -> Unit = {}
) {
    val isFromOrganizationDetail = preselectedOrganizationId != null
    val isEditMode = meetingId != null
    val appColors = LocalPartnerManagementColors.current

    var showOrganizationSheet by remember { mutableStateOf(false) }
    var selectedOrganization by remember { mutableStateOf<OrganizationEntity?>(null) }

    // لیست و مقدار انتخابی نوع بازدید
    val meetingTypes = remember { MeetingType.entries }
    var selectedVisitType by remember { mutableStateOf(MeetingType.PHONE) }

    // شرط‌های نمایشی بر اساس نام‌های صحیح Enum
    val isScheduledInPersonVisit = selectedVisitType == MeetingType.PLANNED_IN_PERSON
    val isUnscheduledInPersonVisit = selectedVisitType == MeetingType.UNPLANNED_IN_PERSON

    // موضوع بازدید
    var visitSubject by rememberSaveable { mutableStateOf("") }
    val visitSubjectList = remember { listOf("ثبت نام", "وصول مطالبات", "تحویل استند", "سایر") }


    //  وضعیت بازدید (با مقدار پیش‌فرض PLANNED)
    val statusTypes = remember { MeetingStatus.entries }
    var selectedStatus by remember { mutableStateOf(MeetingStatus.PLANNED) }


    // تاریخ امروز جلالی
    val today = remember { PersianDate() }
    val initialDate = remember {
        "${today.shYear}/${
            String.format(
                Locale.US,
                "%02d",
                today.shMonth
            )
        }/${String.format(Locale.US, "%02d", today.shDay)}"
    }

    // زمان حال
    val calendar = remember { Calendar.getInstance() }
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = calendar.get(Calendar.MINUTE)
    val initialTime = remember {
        String.format(Locale.US, "%02d:%02d", currentHour, currentMinute)
    }

    var showVisitScheduledDatePicker by remember { mutableStateOf(false) }
    var showVisitScheduledTimePicker by remember { mutableStateOf(false) }
    var visitScheduledDate by rememberSaveable { mutableStateOf(initialDate) }
    val visitScheduledTime by rememberSaveable { mutableStateOf(initialTime) }

    var showVisitRealDatePicker by remember { mutableStateOf(false) }
    var showVisitRealTimePicker by remember { mutableStateOf(false) }
    var visitRealDate by rememberSaveable { mutableStateOf(initialDate) }
    var visitRealTime by rememberSaveable { mutableStateOf(initialTime) }

    val timePickerState = rememberTimePickerState(
        initialHour = currentHour,
        initialMinute = currentMinute,
        is24Hour = true
    )

    var visitorName by rememberSaveable { mutableStateOf("") }
    val visitorsList = remember { listOf("علی علوی", "رضا رضایی", "محمد محمدی", "حسین حسینی") }

    var person by rememberSaveable { mutableStateOf("") }
    val personList = remember { listOf("علی علوی", "رضا رضایی", "محمد محمدی", "حسین حسینی") }

    var description by rememberSaveable { mutableStateOf("") }

    // لوکیشن GPS
    var currentLatitude by rememberSaveable { mutableStateOf<Double?>(null) }
    var currentLongitude by rememberSaveable { mutableStateOf<Double?>(null) }

    // لود اولیه سازمان در صورت پاس داده شدن از صفحه قبل
    LaunchedEffect(preselectedOrganizationId) {
        if (preselectedOrganizationId != null) {
            selectedOrganization = demoOrganizations.find {
                it.organizationId == preselectedOrganizationId
            }
        }
    }

    // حالت ویرایش
    LaunchedEffect(meetingId) {
        if (isEditMode) {
            val existingItem = demoMeetings.find { it.meetingId == meetingId }
            existingItem?.let { meeting ->
                selectedVisitType = MeetingType.fromValue(meeting.type)
                selectedStatus = MeetingStatus.fromId(meeting.status)

                selectedOrganization = demoOrganizations.find {
                    it.organizationId == meeting.organizationId
                }

                meeting.visitDate?.let { visitRealDate = it }
                meeting.visitTime?.let { visitRealTime = it }

                description = meeting.description.orEmpty()
                currentLatitude = meeting.latitude
                currentLongitude = meeting.longitude
                visitorName = meeting.visitorId.orEmpty()
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomHeader(
                title = if (isEditMode) R.string.label_edit_visit else R.string.label_submit_visit,
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


                // نوع بازدید
                val meetingTypeTitles = meetingTypes.map { stringResource(id = it.titleRes) }
                DropdownSelectorField(
                    value = stringResource(id = selectedVisitType.titleRes),
                    label = stringResource(R.string.label_visit_type),
                    placeholder = stringResource(R.string.hint_choose_visit_type),
                    items = meetingTypeTitles,
                    isRequired = true,
                    onItemSelected = { selectedTitle ->
                        val index = meetingTypeTitles.indexOf(selectedTitle)
                        if (index != -1) {
                            selectedVisitType = meetingTypes[index]
                        }
                    }
                )

                // موضوع بازدید
                DropdownSelectorField(
                    value = visitSubject,
                    label = stringResource(R.string.label_visit_subject),
                    placeholder = stringResource(R.string.hint_enter_visit_subject),
                    items = visitSubjectList,
                    isRequired = true,
                    onItemSelected = { visitSubject = it }
                )

                // سازمان
                if (!isFromOrganizationDetail) {
                    CustomSelectorField(
                        value = selectedOrganization?.name ?: "",
                        label = stringResource(R.string.label_organization_name),
                        placeholder = stringResource(R.string.hint_choose_organization_name),
                        isExpanded = showOrganizationSheet,
                        onClick = { showOrganizationSheet = true }
                    )
                }

                // فیلدهای مختص بازدید برنامه‌ریزی شده
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

                // فیلدهای مختص بازدید غیربرنامه‌ریزی شده یا تلفنی
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

                // زمان واقعی انجام بازدید
                CustomDateTimeFields(
                    label = stringResource(R.string.label_visit_real_date_time),
                    date = visitRealDate,
                    onDateClick = { showVisitRealDatePicker = true },
                    time = visitRealTime,
                    onTimeClick = { showVisitRealTimePicker = true }
                )

                // وضعیت بازدید
                val statusTitles = statusTypes.map { stringResource(id = it.titleRes) }
                DropdownSelectorField(
                    value = stringResource(id = selectedStatus.titleRes),
                    label = stringResource(R.string.label_visit_status),
                    placeholder = "",
                    items = statusTitles,
                    isRequired = false,
                    onItemSelected = { selectedTitle ->
                        val index = statusTitles.indexOf(selectedTitle)
                        if (index != -1) {
                            selectedStatus = statusTypes[index]
                        }
                    }
                )

                // فیلد توضیحات
                CustomDescriptionField(
                    label = stringResource(R.string.label_description),
                    value = description,
                    onValueChange = { description = it },
                    placeholder = stringResource(R.string.hint_description)
                )

                // فیلدهای تکمیلی بازدید سرزده / غیر برنامه‌ریزی شده
                if (isUnscheduledInPersonVisit) {
                    ImageUploadField(
                        label = stringResource(R.string.label_visit_image)
                    )

                    GpsLocationField(
                        label = stringResource(R.string.label_register_gps)
                    )
                }

                // دکمه تایید و ذخیره
                CustomButton(
                    text = if (isEditMode) {
                        stringResource(R.string.label_edit_visit)
                    } else {
                        stringResource(R.string.label_submit_visit)
                    },
                    onClick = {
                        val entityToSave = MeetingEntity(
                            meetingId = meetingId ?: java.util.UUID.randomUUID().toString(),
                            name = visitSubject,
                            organizationId = selectedOrganization?.organizationId,
                            visitorId = visitorName.ifBlank { null },
                            type = selectedVisitType.value,
                            status = selectedStatus.id,
                            visitDate = visitRealDate,
                            visitTime = visitRealTime,
                            description = description,
                            latitude = currentLatitude,
                            longitude = currentLongitude
                        )
                        onSubmitClick(entityToSave)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )
            }
        }
    }

    // دیالوگ‌های زمان و تاریخ
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
            onDismiss = { showVisitRealDatePicker = false },
            onDateSelected = { date ->
                visitRealDate = "${date.year}/${String.format(Locale.US, "%02d", date.month)}/${
                    String.format(
                        Locale.US,
                        "%02d",
                        date.day
                    )
                }"
                showVisitRealDatePicker = false
            }
        )
    }

    if (showVisitScheduledDatePicker) {
        DatePickerDialog(
            onDismiss = { showVisitScheduledDatePicker = false },
            onDateSelected = { date ->
                visitScheduledDate =
                    "${date.year}/${String.format(Locale.US, "%02d", date.month)}/${
                        String.format(
                            Locale.US,
                            "%02d",
                            date.day
                        )
                    }"
                showVisitScheduledDatePicker = false
            }
        )
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
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun AddMeetingScreenPreview() {
    AppScreenPreview {
        AddMeetingScreen(
            meetingId = null,
            preselectedOrganizationId = null,
            onBackClick = {},
            onSubmitClick = {}
        )
    }
}
