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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.MeetingEntity
import ir.kitgroup.partnerManagement.core.database.model.MeetingWithDetail
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import ir.kitgroup.partnerManagement.core.ui.components.*
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.MeetingStatus
import ir.kitgroup.partnerManagement.core.ui.util.MeetingType
import ir.kitgroup.partnerManagement.feature.organization.ui.OrganizationListBottomSheet
import saman.zamani.persiandate.PersianDate
import java.util.Calendar
import java.util.Locale

@Composable
fun AddMeetingRoute(
    onBackClick: () -> Unit,
    onSubmitClick: (MeetingEntity) -> Unit,
    viewModel: AddMeetingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AddMeetingScreen(
        meetingId = viewModel.meetingId,
        preselectedOrganizationId = viewModel.preselectedOrganizationId,
        preselectedOrganization = uiState.preloadedOrganization,
        existingMeeting = uiState.existingMeeting,
        organizationsList = uiState.organizations,
        onBackClick = onBackClick,
        onSubmitClick = onSubmitClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMeetingScreen(
    meetingId: String?,
    preselectedOrganizationId: String?,
    preselectedOrganization: OrganizationWithDetail? = null,
    existingMeeting: MeetingWithDetail? = null,
    organizationsList: List<OrganizationWithDetail> = emptyList(),
    onBackClick: () -> Unit,
    onSubmitClick: (MeetingEntity) -> Unit = {}
) {
    val isFromOrganizationDetail = preselectedOrganizationId != null
    val isEditMode = meetingId != null
    val appColors = LocalPartnerManagementColors.current

    var showOrganizationSheet by remember { mutableStateOf(false) }
    var selectedOrganization by remember { mutableStateOf<OrganizationWithDetail?>(null) }

    // نوع و وضعیت بازدید
    val meetingTypes = remember { MeetingType.entries }
    var selectedVisitType by remember { mutableStateOf(MeetingType.PHONE) }

    val isScheduledInPersonVisit = selectedVisitType == MeetingType.PLANNED_IN_PERSON
    val isUnscheduledInPersonVisit = selectedVisitType == MeetingType.UNPLANNED_IN_PERSON

    var visitSubject by rememberSaveable { mutableStateOf("") }
    val visitSubjectList = remember { listOf("ثبت نام", "وصول مطالبات", "تحویل استند", "سایر") }

    val statusTypes = remember { MeetingStatus.entries }
    var selectedStatus by remember { mutableStateOf(MeetingStatus.PLANNED) }

    // زمان‌بندی جلالی اولیه
    val today = remember { PersianDate() }
    val initialDate = remember {
        "${today.shYear}/${String.format(Locale.US, "%02d", today.shMonth)}/${String.format(Locale.US, "%02d", today.shDay)}"
    }

    val calendar = remember { Calendar.getInstance() }
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = calendar.get(Calendar.MINUTE)
    val initialTime = remember {
        String.format(Locale.US, "%02d:%02d", currentHour, currentMinute)
    }

    var showVisitScheduledDatePicker by remember { mutableStateOf(false) }
    var showVisitScheduledTimePicker by remember { mutableStateOf(false) }
    var visitScheduledDate by rememberSaveable { mutableStateOf(initialDate) }
    var visitScheduledTime by rememberSaveable { mutableStateOf(initialTime) }

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
    var currentLatitude by rememberSaveable { mutableStateOf<String?>(null) }
    var currentLongitude by rememberSaveable { mutableStateOf<String?>(null) }

    // بایند کردن سازمان پیش‌فرض
    LaunchedEffect(preselectedOrganization) {
        if (preselectedOrganization != null && selectedOrganization == null) {
            selectedOrganization = preselectedOrganization
        }
    }

    // بارگذاری داده‌های واقعی جلسه در حالت ویرایش (جایگزین demoMeetings)
    LaunchedEffect(existingMeeting) {
        existingMeeting?.let { item ->
            val entity = item.meeting
            selectedVisitType = MeetingType.fromValue(entity.type)
            selectedStatus = MeetingStatus.fromId(entity.status) ?: MeetingStatus.PLANNED
            visitSubject = item.subjectVisitName ?: ""

            entity.visitDate?.let { visitRealDate = it }
            entity.visitTime?.let { visitRealTime = it }

            description = entity.description.orEmpty()
            currentLatitude = entity.latitude
            currentLongitude = entity.longitude
            visitorName = entity.visitorId.orEmpty()
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

                // انتخاب سازمان
                if (!isFromOrganizationDetail) {
                    CustomSelectorField(
                        value = selectedOrganization?.organization?.name.orEmpty(),
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

                // توضیحات
                CustomDescriptionField(
                    label = stringResource(R.string.label_description),
                    value = description,
                    onValueChange = { description = it },
                    placeholder = stringResource(R.string.hint_description)
                )

                if (isUnscheduledInPersonVisit) {
                    ImageUploadField(label = stringResource(R.string.label_visit_image))
                    GpsLocationField(label = stringResource(R.string.label_register_gps))
                }

                // دکمه ثبت / ذخیره
                CustomButton(
                    text = if (isEditMode) {
                        stringResource(R.string.label_edit_visit)
                    } else {
                        stringResource(R.string.label_submit_visit)
                    },
                    onClick = {
                        val orgId = selectedOrganization?.organization?.organizationId.orEmpty()
                        val entityToSave = MeetingEntity(
                            meetingId = meetingId ?: java.util.UUID.randomUUID().toString(),
                            subjectVisitName = visitSubject,
                            organizationId = orgId,
                            visitorId = visitorName.ifBlank { null },
                            type = selectedVisitType.value,
                            status = selectedStatus.id,
                            visitDate = visitRealDate,
                            visitTime = visitRealTime,
                            description = description,
                            latitude = currentLatitude,
                            longitude = currentLongitude,
                            stateCode = 0
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

    // دیالوگ‌های تاریخ و ساعت
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
                    String.format(Locale.US, "%02d", date.day)
                }"
                showVisitRealDatePicker = false
            }
        )
    }

    if (showVisitScheduledDatePicker) {
        DatePickerDialog(
            onDismiss = { showVisitScheduledDatePicker = false },
            onDateSelected = { date ->
                visitScheduledDate = "${date.year}/${String.format(Locale.US, "%02d", date.month)}/${
                    String.format(Locale.US, "%02d", date.day)
                }"
                showVisitScheduledDatePicker = false
            }
        )
    }

    // باتم شیت لیست سازمان‌ها
    if (showOrganizationSheet && preselectedOrganizationId == null) {
        OrganizationListBottomSheet(
            list = organizationsList,
            onDismiss = { showOrganizationSheet = false },
            onItemSelected = { item ->
                selectedOrganization = item
                showOrganizationSheet = false
            }
        )
    }
}
