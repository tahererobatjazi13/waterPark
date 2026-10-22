package ir.kitgroup.partnerManagement.feature.meeting.ui.add

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
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
import ir.kitgroup.partnerManagement.core.ui.SessionViewModel
import ir.kitgroup.partnerManagement.core.ui.components.*
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.MeetingStatus
import ir.kitgroup.partnerManagement.core.ui.util.MeetingType
import ir.kitgroup.partnerManagement.core.ui.util.UserRole
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate
import ir.kitgroup.partnerManagement.feature.organization.ui.detail.ImagesSection
import ir.kitgroup.partnerManagement.feature.organization.ui.list.OrganizationListBottomSheet
import saman.zamani.persiandate.PersianDate
import java.util.Calendar
import java.util.Locale

@Composable
fun AddEditMeetingScreen(
    onBackClick: () -> Unit,
    onSaved: () -> Unit,
    viewModel: AddEditMeetingViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()

    // val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val organizationPersons by
    viewModel.organizationPersons.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    val isEditMode = viewModel.meetingId != null

    LaunchedEffect(Unit) {
        viewModel.saveSuccess.collect {
            /*  val message = if (isEditMode) "بازدید با موفقیت ویرایش شد" else "بازدید با موفقیت ثبت شد"

              // نمایش اسنک‌بار در همین فرم
              val result = snackBarHostState.showSnackbar(
                  message = message,
                  duration = SnackbarDuration.Short
              )*/

            // بعد از نمایش (یا بستن شدن اسنک‌بار) خروج انجام شود
            onSaved()
        }
    }

    // لیسنر مستقیم SharedFlow خطایاب
    LaunchedEffect(Unit) {
        viewModel.errorMessage.collect { message ->
            snackBarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }
    AddEditMeetingContent(
        meetingId = viewModel.meetingId,
        preselectedOrganizationId = viewModel.preselectedOrganizationId,
        preselectedOrganization = uiState.preloadedOrganization,
        existingMeeting = uiState.existingMeeting,
        organizationsList = uiState.organizations,
        visitSubjects = uiState.visitSubjects,
        snackBarHostState = snackBarHostState,
        isSaving = isSaving,
        allVisitors = uiState.allVisitors,
        defaultVisitorId = uiState.defaultVisitorId,
        defaultVisitorName = uiState.defaultVisitorName,
        onBackClick = onBackClick,
        onSubmitClick = viewModel::createMeeting,
        organizationPersons = organizationPersons,
        onOrganizationSelected = {
            viewModel.loadOrganizationPersons(it.organization.organizationId)
        }
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditMeetingContent(
    meetingId: String?,
    preselectedOrganizationId: String?,
    preselectedOrganization: OrganizationWithDetail? = null,
    existingMeeting: MeetingWithDetail? = null,
    organizationsList: List<OrganizationWithDetail> = emptyList(),
    visitSubjects: List<VisitSubjectEntity> = emptyList(),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    allVisitors: List<VisitorItem> = emptyList(),
    defaultVisitorId: String? = null,
    defaultVisitorName: String? = null,
    isSaving: Boolean = false,
    onBackClick: () -> Unit,
    onSubmitClick: (MeetingEntity) -> Unit = {},
    organizationPersons: List<OrganizationPersonEntity> = emptyList(),

    onOrganizationSelected: (OrganizationWithDetail) -> Unit = {},
    sessionViewModel: SessionViewModel = hiltViewModel(),
) {
    val roleCode by sessionViewModel.roleCode.collectAsState()

    val currentRole = remember(roleCode) { UserRole.fromCode(roleCode.toString()) }
    val isSupervisor = currentRole == UserRole.SUPERVISOR

    var selectedVisitorId by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedVisitorName by rememberSaveable { mutableStateOf("") }
    val isFromOrganizationDetail =
        !preselectedOrganizationId.isNullOrBlank() || preselectedOrganization != null


    // ست کردن اولیه نام بازاریاب از اطلاعات دریافتی
    LaunchedEffect(defaultVisitorId, defaultVisitorName) {
        if (selectedVisitorId.isNullOrBlank() && !defaultVisitorId.isNullOrBlank()) {
            selectedVisitorId = defaultVisitorId
            selectedVisitorName = defaultVisitorName.orEmpty()
        } else if (selectedVisitorName.isBlank() && !defaultVisitorName.isNullOrBlank()) {
            selectedVisitorName = defaultVisitorName
        }
    }


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

    val timePickerState =
        rememberTimePickerState(
            initialHour = currentHour,
            initialMinute = currentMinute,
            is24Hour = true
        )

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

    // ۳. اصلاح LaunchedEffect مربوط به حالت ویرایش (Edit Mode):
    LaunchedEffect(existingMeeting, visitSubjects, allVisitors) {
        existingMeeting?.let { item ->
            val entity = item.meeting

            val targetVisitorId = entity.visitorId
                ?: item.organization?.activeVisitorId
                ?: item.organization?.visitorId

            selectedVisitorId = targetVisitorId
            selectedVisitorName = entity.visitorName?.takeIf { it.isNotBlank() }
                ?: allVisitors.firstOrNull { it.id == targetVisitorId }?.fullName
                    .orEmpty()

            selectedVisitType = MeetingType.fromValue(entity.type)
            selectedStatus = MeetingStatus.fromId(entity.status)
            selectedSubjectVisitId = entity.subjectVisitId

            // تبدیل تاریخ برنامه‌ریزی شده به شمسی در حالت ویرایش
            entity.visitDate
                ?.takeIf { it.isNotBlank() }
                ?.let { dateStr ->
                    visitScheduledDate = formatJalaliDate(dateStr)
                }

            // تبدیل تاریخ واقعی به شمسی در حالت ویرایش
            entity.visitRealDate
                ?.takeIf { it.isNotBlank() }
                ?.let { dateStr ->
                    visitRealDate = formatJalaliDate(dateStr)
                }

            entity.visitTime
                ?.takeIf { it.isNotBlank() }
                ?.let {
                    visitRealTime = it
                }

            description = entity.description.orEmpty()
            currentLatitude = entity.latitude
            currentLongitude = entity.longitude
            selectedOrganizationPersonId = entity.organizationPersonId
            selectedOrganizationPersonName = entity.organizationPersonName.orEmpty()
        }
    }

    val images = mutableStateListOf<Uri>()
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            /* if (formState.images.size < 10) {
                 formState.images.add(it)
             }*/
        }
    }
    val openGallery = {
        if (images.size < 10) {
            galleryLauncher.launch("image/*")
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
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
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
                        }, showTime = false

                        /*   time =
                           visitScheduledTime,

                           onTimeClick = {
                               showVisitScheduledTimePicker =
                                   true
                           }*/
                    )

                    // =====================================================
// بازاریاب
// =====================================================
                    if (isSupervisor) {
                        // اگر سرپرست بود: می‌تواند از لیست همه بازاریاب‌ها انتخاب کند
                        val visitorTitles = allVisitors.map { it.fullName }

                        DropdownSelectorField(
                            value = selectedVisitorName,
                            label = stringResource(R.string.label_visitor_name),
                            placeholder = stringResource(R.string.hint_choose_visitor),
                            items = visitorTitles,
                            isRequired = true,
                            onItemSelected = { name ->
                                val visitor = allVisitors.firstOrNull { it.fullName == name }
                                selectedVisitorId = visitor?.id
                                selectedVisitorName = name
                            }
                        )
                    }
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

                        isRequired = false,

                        onItemSelected = { selectedName ->

                            val selectedPerson =
                                organizationPersons.firstOrNull {
                                    it.fullName == selectedName
                                }

                            selectedOrganizationPersonId =
                                selectedPerson?.orgPersonId

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
                if (!isEditMode) {

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
                }
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
                              R.string.label_images
                          ),onClick={openGallery}
                      )
                   /* SectionTitle(stringResource(R.string.label_images))

                    ImagesSection(
                        isLoading = isImagesLoading,
                        attachments = attachments,
                        baseUrl = baseUrl
                    )*/
                    /*   GpsLocationField(
                        label =
                        stringResource(
                            R.string.label_register_gps
                        )
                    )*/
                }

                Spacer(
                    modifier =
                    Modifier.height(8.dp)
                )
// =====================================================
// ثبت / ویرایش
// =====================================================
                CustomButton(
                    text = when {
                        isSaving -> if (isEditMode) "در حال ویرایش..." else "در حال ثبت..."
                        isEditMode -> stringResource(R.string.label_edit_visit)
                        else -> stringResource(R.string.label_submit_visit)
                    },
                    onClick = {
                        if (isSaving) return@CustomButton

                        val orgId = selectedOrganization?.organization?.organizationId
                            ?: preselectedOrganizationId
                            ?: ""

                        val subjectId = selectedSubjectVisitId ?: ""

                        val entityToSave = MeetingEntity(
                            meetingId = meetingId ?: java.util.UUID.randomUUID().toString(),
                            subjectVisitId = subjectId,
                            subjectVisitName = selectedVisitSubject?.title,
                            organizationId = orgId,
                            visitorId = if (isSupervisor) {
                                selectedVisitorId.takeIf { !it.isNullOrBlank() && it != "00000000-0000-0000-0000-000000000000" }
                            } else {
                                null
                            },
                            organizationPersonId = selectedOrganizationPersonId.takeIf {
                                !it.isNullOrBlank() && it != "00000000-0000-0000-0000-000000000000"
                            },
                            organizationPersonName = selectedOrganizationPersonName.ifBlank { null },
                            type = selectedVisitType.value,
                            status = if (isEditMode) (existingMeeting?.meeting?.status ?: selectedStatus.id) else selectedStatus.id,
                            visitDate = if (isScheduledInPersonVisit) visitScheduledDate else visitRealDate,
                            visitRealDate = visitRealDate,
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

    // =========================================================
    // Time Picker - Real
    // =========================================================

    if (showVisitRealTimePicker) {
        TimePickerDialog(
            timePickerState = timePickerState,
            onConfirm = { hour, minute ->
                visitRealTime = String.format(
                    Locale.US,
                    "%02d:%02d",
                    hour,
                    minute
                )
                showVisitRealTimePicker = false
            },
            onDismiss = {
                showVisitRealTimePicker = false
            }
        )
    }

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

                if (isSupervisor) {
                    // دریافت ویزیتور فعال سازمان
                    val targetVisitorId = item.organization.activeVisitorId
                        ?: item.organization.visitorId

                    selectedVisitorId = targetVisitorId
                    // استخراج نام ویزیتور از لیست بازاریاب‌ها بر اساس ID
                    selectedVisitorName =
                        allVisitors.firstOrNull { it.id == targetVisitorId }?.fullName
                            ?: ""
                }

                onOrganizationSelected(item)
                showOrganizationSheet = false
            })
    }


}