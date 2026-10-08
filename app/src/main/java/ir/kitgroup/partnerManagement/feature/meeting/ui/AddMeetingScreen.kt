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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.MeetingEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationPersonEntity
import ir.kitgroup.partnerManagement.core.database.entity.VisitSubjectEntity
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
    onSaved: () -> Unit,
    viewModel: AddMeetingViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val organizationPersons by
    viewModel.organizationPersons.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {

        viewModel.saveSuccess.collect {
            onSaved()
        }
    }

    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrBlank()) {

            // اینجا می‌توانی Toast یا Snackbar استفاده کنی
            viewModel.clearError()
        }
    }

    AddMeetingScreen(
        meetingId = viewModel.meetingId,
        preselectedOrganizationId =
        viewModel.preselectedOrganizationId,
        preselectedOrganization =
        uiState.preloadedOrganization,
        existingMeeting =
        uiState.existingMeeting,
        organizationsList =
        uiState.organizations,
        visitSubjects =
        uiState.visitSubjects,
        isSaving = isSaving,
        onBackClick = onBackClick,
        onSubmitClick = viewModel::createMeeting,
        organizationPersons = organizationPersons,
        onOrganizationSelected = {
            viewModel.loadOrganizationPersons(
                it.organization.organizationId
            )
        },

    )
}


@Composable
fun AddMeetingScreen(
    meetingId: String?,
    preselectedOrganizationId: String?,
    preselectedOrganization: OrganizationWithDetail? = null,
    existingMeeting: MeetingWithDetail? = null,
    organizationsList: List<OrganizationWithDetail> = emptyList(),
    visitSubjects: List<VisitSubjectEntity> = emptyList(),
    isSaving: Boolean = false,
    onBackClick: () -> Unit,
    onSubmitClick: (MeetingEntity) -> Unit = {},
    organizationPersons: List<OrganizationPersonEntity> = emptyList(),

    onOrganizationSelected: (OrganizationWithDetail) -> Unit = {},
) {


    val isFromOrganizationDetail =
        preselectedOrganizationId != null

    val isEditMode =
        meetingId != null

    LocalPartnerManagementColors.current

    var showOrganizationSheet by
    remember {
        mutableStateOf(false)
    }

    var selectedOrganization by
    remember {
        mutableStateOf<OrganizationWithDetail?>(null)
    }

    // -----------------------------
    // نوع بازدید
    // -----------------------------

    val meetingTypes =
        remember {
            MeetingType.entries
        }

    var selectedVisitType by
    remember {
        mutableStateOf(
            MeetingType.PHONE
        )
    }

    val isScheduledInPersonVisit =
        selectedVisitType ==
                MeetingType.PLANNED_IN_PERSON

    val isUnscheduledInPersonVisit =
        selectedVisitType ==
                MeetingType.UNPLANNED_IN_PERSON

    // -----------------------------
    // موضوع بازدید
    // -----------------------------

    var selectedSubjectVisitId by
    rememberSaveable {
        mutableStateOf<String?>(null)
    }

    val selectedVisitSubject =
        visitSubjects.firstOrNull {
            it.subjectVisitId == selectedSubjectVisitId
        }

    // -----------------------------
    // وضعیت
    // -----------------------------

    val statusTypes =
        remember {
            MeetingStatus.entries
        }

    var selectedStatus by
    remember {
        mutableStateOf(
            MeetingStatus.PLANNED
        )
    }

    // -----------------------------
    // تاریخ اولیه
    // -----------------------------

    val today =
        remember {
            PersianDate()
        }

    val initialDate =
        remember {
            "${today.shYear}/${
                String.format(
                    Locale.US,
                    "%02d",
                    today.shMonth
                )
            }/${
                String.format(
                    Locale.US,
                    "%02d",
                    today.shDay
                )
            }"
        }

    // -----------------------------
    // ساعت اولیه
    // -----------------------------

    val calendar =
        remember {
            Calendar.getInstance()
        }

    val currentHour =
        calendar.get(
            Calendar.HOUR_OF_DAY
        )

    val currentMinute =
        calendar.get(
            Calendar.MINUTE
        )

    val initialTime =
        remember {
            String.format(
                Locale.US,
                "%02d:%02d",
                currentHour,
                currentMinute
            )
        }

    // -----------------------------
    // زمان‌بندی بازدید
    // -----------------------------

    var showVisitScheduledDatePicker by
    remember {
        mutableStateOf(false)
    }

    var showVisitScheduledTimePicker by
    remember {
        mutableStateOf(false)
    }

    var visitScheduledDate by
    rememberSaveable {
        mutableStateOf(initialDate)
    }

    var visitScheduledTime by
    rememberSaveable {
        mutableStateOf(initialTime)
    }

    // -----------------------------
    // زمان واقعی بازدید
    // -----------------------------

    var showVisitRealDatePicker by
    remember {
        mutableStateOf(false)
    }

    var showVisitRealTimePicker by
    remember {
        mutableStateOf(false)
    }

    var visitRealDate by
    rememberSaveable {
        mutableStateOf(initialDate)
    }

    var visitRealTime by
    rememberSaveable {
        mutableStateOf(initialTime)
    }
    /*
        val timePickerState =
            rememberTimePickerState(
                initialHour = currentHour,
                initialMinute = currentMinute,
                is24Hour = true
            )*/

    // -----------------------------
    // بازدید کننده
    // -----------------------------

    var visitorName by
    rememberSaveable {
        mutableStateOf("")
    }

    val visitorsList =
        remember {
            listOf(
                "علی علوی",
                "رضا رضایی",
                "محمد محمدی",
                "حسین حسینی"
            )
        }

// -----------------------------
// شخص سازمان
// -----------------------------

    var selectedOrganizationPersonId by
    rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var selectedOrganizationPersonName by
    rememberSaveable {
        mutableStateOf("")
    }


    // -----------------------------
    // سایر
    // -----------------------------

    var description by
    rememberSaveable {
        mutableStateOf("")
    }

    var currentLatitude by
    rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var currentLongitude by
    rememberSaveable {
        mutableStateOf<String?>(null)
    }

    // -----------------------------
    // سازمان پیش‌فرض
    // -----------------------------

    LaunchedEffect(
        preselectedOrganization
    ) {

        if (
            preselectedOrganization != null &&
            selectedOrganization == null
        ) {

            selectedOrganization =
                preselectedOrganization
        }
    }

    // -----------------------------
    // بارگذاری حالت ویرایش
    // -----------------------------

    LaunchedEffect(
        existingMeeting,
        visitSubjects
    ) {

        existingMeeting?.let { item ->

            val entity =
                item.meeting

            selectedVisitType =
                MeetingType.fromValue(
                    entity.type
                )

            selectedStatus =
                MeetingStatus.fromId(
                    entity.status
                )

            selectedSubjectVisitId =
                entity.subjectVisitId

            entity.visitDate
                ?.takeIf {
                    it.isNotBlank()
                }
                ?.let {
                    visitRealDate = it
                }

            entity.visitTime
                ?.takeIf {
                    it.isNotBlank()
                }
                ?.let {
                    visitRealTime = it
                }

            description =
                entity.description.orEmpty()

            currentLatitude =
                entity.latitude

            currentLongitude =
                entity.longitude
            selectedOrganizationPersonId =
                entity.organizationPersonId

            selectedOrganizationPersonName =
                entity.organizationPersonName.orEmpty()
            visitorName =
                entity.visitorId.orEmpty()
        }
    }

    Scaffold(

        modifier =
        Modifier.fillMaxSize(),

        topBar = {

            CustomHeader(

                title =
                if (isEditMode) {
                    R.string.label_edit_visit
                } else {
                    R.string.label_submit_visit
                },

                showBackButton = true,

                onBackClick =
                onBackClick
            )
        },

        containerColor =
        MaterialTheme.colorScheme.background

    ) { innerPadding ->

        Surface(

            modifier =
            Modifier
                .fillMaxSize()
                .padding(
                    top =
                    innerPadding
                        .calculateTopPadding()
                ),

            shape =
            RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp
            ),

            color =
            MaterialTheme
                .colorScheme
                .background

        ) {

            Column(

                modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(20.dp),

                verticalArrangement =
                Arrangement.spacedBy(16.dp)

            ) {

                // =====================================================
                // نوع بازدید
                // =====================================================

                val meetingTypeTitles =
                    meetingTypes.map {
                        stringResource(
                            id = it.titleRes
                        )
                    }

                DropdownSelectorField(

                    value =
                    stringResource(
                        id =
                        selectedVisitType
                            .titleRes
                    ),

                    label =
                    stringResource(
                        R.string.label_visit_type
                    ),

                    placeholder =
                    stringResource(
                        R.string.hint_choose_visit_type
                    ),

                    items =
                    meetingTypeTitles,

                    isRequired = true,

                    onItemSelected = { selectedTitle ->

                        val index =
                            meetingTypeTitles
                                .indexOf(
                                    selectedTitle
                                )

                        if (index != -1) {

                            selectedVisitType =
                                meetingTypes[index]
                        }
                    }
                )

                // =====================================================
                // موضوع بازدید از دیتابیس
                // =====================================================

                val visitSubjectTitles = visitSubjects
                    .mapNotNull { subject ->
                        subject.title?.takeIf { it.isNotBlank() }
                    }

                DropdownSelectorField(
                    value = selectedVisitSubject?.title.orEmpty(),

                    label = stringResource(
                        R.string.label_visit_subject
                    ),

                    placeholder = stringResource(
                        R.string.hint_enter_visit_subject
                    ),

                    items = visitSubjectTitles,

                    isRequired = true,

                    onItemSelected = { selectedTitle ->

                        selectedSubjectVisitId =
                            visitSubjects
                                .firstOrNull {
                                    it.title == selectedTitle
                                }
                                ?.subjectVisitId
                    }
                )

                // =====================================================
                // سازمان
                // =====================================================

                if (
                    !isFromOrganizationDetail
                ) {

                    CustomSelectorField(

                        value =
                        selectedOrganization
                            ?.organization
                            ?.name
                            .orEmpty(),

                        label =
                        stringResource(
                            R.string.label_organization_name
                        ),

                        placeholder =
                        stringResource(
                            R.string.hint_choose_organization_name
                        ),

                        isExpanded =
                        showOrganizationSheet,

                        onClick = {
                            showOrganizationSheet = true
                        }
                    )
                }

                // =====================================================
                // بازدید حضوری برنامه‌ریزی شده
                // =====================================================

                if (
                    isScheduledInPersonVisit
                ) {

                    CustomDateTimeFields(

                        label =
                        stringResource(
                            R.string.label_visit_Scheduled_date
                        ),

                        date =
                        visitScheduledDate,

                        onDateClick = {
                            showVisitScheduledDatePicker =
                                true
                        },

                        time =
                        visitScheduledTime,

                        onTimeClick = {
                            showVisitScheduledTimePicker =
                                true
                        }
                    )

                    DropdownSelectorField(

                        value =
                        visitorName,

                        label =
                        stringResource(
                            R.string.label_visitor_name
                        ),

                        placeholder =
                        stringResource(
                            R.string.hint_choose_visitor
                        ),

                        items =
                        visitorsList,

                        isRequired = true,

                        onItemSelected = {
                            visitorName = it
                        }
                    )
                }

                // =====================================================
                // بازدید تلفنی / غیر برنامه‌ریزی شده
                // =====================================================
                val organizationPersonNames =
                    organizationPersons.mapNotNull { person ->
                        person.fullName?.takeIf {
                            it.isNotBlank()
                        }
                    }
                if (!isScheduledInPersonVisit) {



                    DropdownSelectorField(

                        value =
                        selectedOrganizationPersonName,

                        label =
                        stringResource(
                            R.string.label_organization_person
                        ),

                        placeholder =
                        stringResource(
                            R.string.hint_choose_organization_person
                        ),

                        items =
                        organizationPersonNames,

                        isRequired = true,

                        onItemSelected = { selectedName ->

                            val selectedPerson =
                                organizationPersons.firstOrNull {
                                    it.fullName == selectedName
                                }

                            selectedOrganizationPersonId =
                                selectedPerson?.mainPersonId

                            selectedOrganizationPersonName =
                                selectedPerson?.fullName.orEmpty()
                        }
                    )
                }

                // =====================================================
                // تاریخ واقعی بازدید
                // =====================================================

                CustomDateTimeFields(

                    label =
                    stringResource(
                        R.string.label_visit_real_date_time
                    ),

                    date =
                    visitRealDate,

                    onDateClick = {
                        showVisitRealDatePicker =
                            true
                    },

                    time =
                    visitRealTime,

                    onTimeClick = {
                        showVisitRealTimePicker =
                            true
                    }
                )

                // =====================================================
                // وضعیت
                // =====================================================

                val statusTitles =
                    statusTypes.map {
                        stringResource(
                            id = it.titleRes
                        )
                    }

                DropdownSelectorField(

                    value =
                    stringResource(
                        id =
                        selectedStatus
                            .titleRes
                    ),

                    label =
                    stringResource(
                        R.string.label_visit_status
                    ),

                    placeholder = "",

                    items =
                    statusTitles,

                    isRequired = false,

                    onItemSelected = { selectedTitle ->

                        val index =
                            statusTitles
                                .indexOf(
                                    selectedTitle
                                )

                        if (index != -1) {

                            selectedStatus =
                                statusTypes[index]
                        }
                    }
                )

                // =====================================================
                // توضیحات
                // =====================================================

                CustomDescriptionField(

                    label =
                    stringResource(
                        R.string.label_description
                    ),

                    value =
                    description,

                    onValueChange = {
                        description = it
                    },

                    placeholder =
                    stringResource(
                        R.string.hint_description
                    )
                )

                // =====================================================
                // GPS
                // =====================================================

                if (
                    isUnscheduledInPersonVisit
                ) {

                    ImageUploadField(
                        label =
                        stringResource(
                            R.string.label_visit_image
                        )
                    )

                    GpsLocationField(
                        label =
                        stringResource(
                            R.string.label_register_gps
                        )
                    )
                }

                Spacer(
                    modifier =
                    Modifier.height(8.dp)
                )

                // =====================================================
                // ثبت / ویرایش
                // =====================================================

                CustomButton(

                    text =
                    when {

                        isSaving ->
                            if (isEditMode) {
                                "در حال ویرایش..."
                            } else {
                                "در حال ثبت..."
                            }

                        isEditMode ->
                            stringResource(
                                R.string.label_edit_visit
                            )

                        else ->
                            stringResource(
                                R.string.label_submit_visit
                            )
                    },

                    onClick = {

                        if (isSaving) {
                            return@CustomButton
                        }

                        val orgId =
                            selectedOrganization
                                ?.organization
                                ?.organizationId
                                ?: preselectedOrganizationId
                                ?: ""

                        if (orgId.isBlank()) {
                            return@CustomButton
                        }

                        val subjectId =
                            selectedSubjectVisitId

                        if (
                            subjectId.isNullOrBlank()
                        ) {
                            return@CustomButton
                        }

                        val entityToSave =
                            MeetingEntity(

                                meetingId =
                                meetingId
                                    ?: java.util.UUID
                                        .randomUUID()
                                        .toString(),

                                subjectVisitId =
                                subjectId,

                                subjectVisitName =
                                selectedVisitSubject
                                    ?.title,

                                organizationId =
                                orgId,

                                visitorId =
                                visitorName
                                    .ifBlank {
                                        null
                                    },
                                organizationPersonId =
                                selectedOrganizationPersonId,

                                organizationPersonName =
                                selectedOrganizationPersonName
                                    .ifBlank {
                                        null
                                    },
                                type =
                                selectedVisitType
                                    .value,

                                status =
                                selectedStatus.id,

                                visitDate =
                                visitRealDate,

                                visitTime =
                                visitRealTime,

                                description =
                                description,

                                latitude =
                                currentLatitude,

                                longitude =
                                currentLongitude,

                                stateCode = 0
                            )

                        onSubmitClick(
                            entityToSave
                        )
                    },

                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 10.dp
                        )
                )
            }
        }
    }

    // =========================================================
    // Time Picker - Real
    // =========================================================
    /*
        if (
            showVisitRealTimePicker
        ) {

            TimePickerDialog(

                timePickerState =
                timePickerState,

                onConfirm = { hour,
                              minute ->

                    visitRealTime =
                        String.format(
                            Locale.US,
                            "%02d:%02d",
                            hour,
                            minute
                        )

                    showVisitRealTimePicker =
                        false
                },

                onDismiss = {
                    showVisitRealTimePicker =
                        false
                }
            )
        }*/

    // =========================================================
    // Date Picker - Real
    // =========================================================

    if (
        showVisitRealDatePicker
    ) {

        DatePickerDialog(

            onDismiss = {
                showVisitRealDatePicker =
                    false
            },

            onDateSelected = { date ->

                visitRealDate =
                    "${date.year}/${
                        String.format(
                            Locale.US,
                            "%02d",
                            date.month
                        )
                    }/${
                        String.format(
                            Locale.US,
                            "%02d",
                            date.day
                        )
                    }"

                showVisitRealDatePicker =
                    false
            }
        )
    }

    // =========================================================
    // Date Picker - Scheduled
    // =========================================================

    if (
        showVisitScheduledDatePicker
    ) {

        DatePickerDialog(

            onDismiss = {
                showVisitScheduledDatePicker =
                    false
            },

            onDateSelected = { date ->

                visitScheduledDate =
                    "${date.year}/${
                        String.format(
                            Locale.US,
                            "%02d",
                            date.month
                        )
                    }/${
                        String.format(
                            Locale.US,
                            "%02d",
                            date.day
                        )
                    }"

                showVisitScheduledDatePicker =
                    false
            }
        )
    }

    // =========================================================
    // Organization BottomSheet
    // =========================================================

    if (
        showOrganizationSheet &&
        preselectedOrganizationId == null
    ) {

        OrganizationListBottomSheet(

            list =
            organizationsList,

            onDismiss = {
                showOrganizationSheet =
                    false
            },

            onItemSelected = { item ->

                selectedOrganization = item

                selectedOrganizationPersonId = null
                selectedOrganizationPersonName = ""

                onOrganizationSelected(item)

                showOrganizationSheet = false
            }
        )
    }
}