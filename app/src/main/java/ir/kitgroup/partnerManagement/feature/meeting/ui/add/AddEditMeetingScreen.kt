package ir.kitgroup.partnerManagement.feature.meeting.ui.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import ir.kitgroup.partnerManagement.core.ui.components.*
import ir.kitgroup.partnerManagement.core.ui.util.MeetingStatus
import ir.kitgroup.partnerManagement.core.ui.util.MeetingType
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.feature.organization.ui.list.OrganizationListBottomSheet
import java.util.Calendar
import java.util.Locale
import androidx.activity.compose.rememberLauncherForActivityResult
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import ir.kitgroup.partnerManagement.core.network.model.AttachmentUploadResponse
import ir.kitgroup.partnerManagement.core.ui.util.AttachmentContext
import ir.kitgroup.partnerManagement.core.ui.util.AttachmentFileType
import ir.kitgroup.partnerManagement.core.ui.util.DataState
import ir.kitgroup.partnerManagement.core.ui.util.isDocumentUri
import ir.kitgroup.partnerManagement.feature.organization.ui.add.ImageDetailInputDialog

@Composable
fun AddEditMeetingScreen(
    onSuccessSave: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddEditMeetingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    val locationManager = remember {
        context.getSystemService(android.content.Context.LOCATION_SERVICE) as LocationManager
    }
    val baseUrl by viewModel.baseUrlFlow.collectAsStateWithLifecycle()
    val uploadState by viewModel.uploadState.collectAsStateWithLifecycle()

    @SuppressLint("MissingPermission")
    fun fetchCurrentLocation() {
        val isGpsEnabled =
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled =
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        val provider = when {
            isGpsEnabled -> LocationManager.GPS_PROVIDER
            isNetworkEnabled -> LocationManager.NETWORK_PROVIDER
            else -> null
        }

        if (provider == null) {
            // نمایش پیام به کاربر یا باز کردن تنظیمات مکان
            context.startActivity(
                android.content.Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            )
            return
        }

        val lastKnownLocation = locationManager.getLastKnownLocation(provider)
        if (lastKnownLocation != null) {
            viewModel.onLocationUpdated(
                lastKnownLocation.latitude,
                lastKnownLocation.longitude
            )
        } else {
            locationManager.requestSingleUpdate(
                provider,
                object : android.location.LocationListener {
                    override fun onLocationChanged(location: Location) {
                        viewModel.onLocationUpdated(
                            location.latitude,
                            location.longitude
                        )
                        locationManager.removeUpdates(this)
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onStatusChanged(
                        provider: String?,
                        status: Int,
                        extras: android.os.Bundle?
                    ) = Unit

                    override fun onProviderEnabled(provider: String) = Unit
                    override fun onProviderDisabled(provider: String) = Unit
                },
                android.os.Looper.getMainLooper()
            )
        }
    }


    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            fetchCurrentLocation()
        }
    }
// هر بار که نوع بازدید به حضوری تغییر کند، لوکیشن دریافت می‌شود
    LaunchedEffect(uiState.visitType) {
        val isInPerson = uiState.visitType == MeetingType.PLANNED_IN_PERSON ||
                uiState.visitType == MeetingType.UNPLANNED_IN_PERSON

        if (isInPerson) {
            val hasFine = ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            val hasCoarse = ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            if (hasFine || hasCoarse) {
                fetchCurrentLocation()
            } else {
                locationPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }
    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowMessage -> snackBarHostState.showSnackbar(
                    event.message.asString(
                        context
                    )
                )

                is UiEvent.ShowError -> snackBarHostState.showSnackbar(event.error.asString(context))
            }
        }
    }
    // متغیرهای موقت برای نگهداری URI و نمایش دیالوگ تنظیمات تصویر
    var pendingImageUri by remember { mutableStateOf<Uri?>(null) }
    var showImageDetailDialog by remember { mutableStateOf(false) }

    // لانچر انتخاب تصویر از گالری دستگاه
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            pendingImageUri = it
            showImageDetailDialog = true
        }
    }

    if (showImageDetailDialog && pendingImageUri != null) {
        val isDocument = remember(pendingImageUri) {
            isDocumentUri(context, pendingImageUri!!)
        }

        val allowedTypes = remember(isDocument) {
            AttachmentFileType.getAvailableTypes(
                context = AttachmentContext.MEETING,
                isDocument = isDocument
            )
        }

        ImageDetailInputDialog(
            allowedTypes = allowedTypes,
            onDismiss = {
                pendingImageUri = null
                showImageDetailDialog = false
            },
            onConfirm = { selectedFileType, description ->
                pendingImageUri?.let { uri ->
                    viewModel.onAddLocalAttachment(
                        uri = uri,
                        fileType = selectedFileType,
                        description = description
                    )
                }
                pendingImageUri = null
                showImageDetailDialog = false
            }
        )
    }


    AddEditMeetingContent(
        uiState = uiState,
        uploadState = uploadState,
        baseUrl = baseUrl,
        isEditMode = viewModel.isEditMode,
        snackBarHostState = snackBarHostState,
        isPreselected = !viewModel.organizationIdArg.isNullOrBlank(),
        onOrganizationSelected = { org ->
            viewModel.onOrganizationSelected(
                org.organization.organizationId,
                org.organization.name.orEmpty()
            )
        },
        onVisitTypeChanged = viewModel::onVisitTypeChanged,
        onSubjectSelected = viewModel::onSubjectChanged,
        onScheduledDateSelected = viewModel::onScheduledDateChanged,
        onRealDateSelected = viewModel::onRealDateChanged,
        onRealTimeSelected = viewModel::onRealTimeChanged,
        onVisitorSelected = viewModel::onVisitorSelected,
        onPersonSelected = viewModel::onPersonSelected,
        onStatusChanged = viewModel::onStatusChanged,
        onDescriptionChanged = viewModel::onDescriptionChanged,
        onSubmit = { viewModel.submitMeeting(onSuccessSave) },
        modifier = modifier,
        onAddImageClick = {
            val mimeTypes = arrayOf(
                "image/*",
                "application/pdf",
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            )
            imagePickerLauncher.launch(mimeTypes)
        },
        onRemoveImageClick = viewModel::onRemoveImage,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditMeetingContent(
    uiState: AddEditMeetingUiState,
    uploadState: DataState<AttachmentUploadResponse>,
    snackBarHostState: SnackbarHostState,
    isPreselected: Boolean,
    baseUrl: String,
    isEditMode: Boolean,
    onOrganizationSelected: (OrganizationWithDetail) -> Unit,
    onVisitTypeChanged: (MeetingType) -> Unit,
    onSubjectSelected: (String) -> Unit,
    onScheduledDateSelected: (String) -> Unit,
    onRealDateSelected: (String) -> Unit,
    onRealTimeSelected: (String) -> Unit,
    onVisitorSelected: (id: String, name: String) -> Unit,
    onPersonSelected: (id: String?, name: String) -> Unit,
    onStatusChanged: (MeetingStatus) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    onAddImageClick: () -> Unit,
    onRemoveImageClick: (Int) -> Unit,

    ) {
    val context = LocalContext.current
    var showOrganizationSheet by remember { mutableStateOf(false) }
    var showVisitScheduledDatePicker by remember { mutableStateOf(false) }
    var showVisitRealDatePicker by remember { mutableStateOf(false) }
    var showVisitRealTimePicker by remember { mutableStateOf(false) }

    val calendar = remember { Calendar.getInstance() }
    val timePickerState = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE),
        is24Hour = true
    )

    val meetingTypeMap = remember(context) {
        MeetingType.entries.associateBy { context.getString(it.titleRes) }
    }
    val statusMap = remember(context) {
        MeetingStatus.entries.associateBy { context.getString(it.titleRes) }
    }

    val selectedSubject =
        uiState.visitSubjects.firstOrNull { it.subjectVisitId == uiState.subjectVisitId }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background
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
                    // نوع بازدید
                    DropdownSelectorField(
                        value = stringResource(uiState.visitType.titleRes),
                        label = stringResource(R.string.label_visit_type),
                        placeholder = stringResource(R.string.hint_choose_visit_type),
                        items = meetingTypeMap.keys.toList(),
                        isRequired = true,
                        onItemSelected = { title ->
                            meetingTypeMap[title]?.let { onVisitTypeChanged(it) }
                        }
                    )
                    // انتخاب سازمان
                    if (!isPreselected && !uiState.isEditMode) {
                        CustomSelectorField(
                            value = uiState.organizationName,
                            label = stringResource(R.string.label_organization_name),
                            placeholder = stringResource(R.string.hint_choose_organization_name),
                            isExpanded = showOrganizationSheet,
                            isRequired = true,
                            onClick = { showOrganizationSheet = true }
                        )
                    }

                    // موضوع بازدید
                    DropdownSelectorField(
                        value = selectedSubject?.title.orEmpty(),
                        label = stringResource(R.string.label_visit_subject),
                        placeholder = stringResource(R.string.hint_enter_visit_subject),
                        items = uiState.visitSubjects.mapNotNull { it.title?.takeIf { t -> t.isNotBlank() } },
                        isRequired = true,
                        onItemSelected = { selectedTitle ->
                            uiState.visitSubjects.firstOrNull { it.title == selectedTitle }
                                ?.let {
                                    onSubjectSelected(it.subjectVisitId)
                                }
                        }
                    )

                    // فیلدهای بازدید حضوری برنامه‌ریزی شده
                    if (uiState.isScheduledInPersonVisit) {
                        CustomDateTimeFields(
                            label = stringResource(R.string.label_visit_Scheduled_date),
                            date = uiState.visitScheduledDate,
                            onDateClick = { showVisitScheduledDatePicker = true },
                            showTime = false,
                            isRequired = true
                        )

                        if (uiState.isSupervisor) {
                            DropdownSelectorField(
                                value = uiState.visitorName,
                                label = stringResource(R.string.label_visitor_name),
                                placeholder = stringResource(R.string.hint_choose_visitor),
                                items = uiState.allVisitors.map { it.fullName },
                                isRequired = true,
                                onItemSelected = { name ->
                                    uiState.allVisitors.firstOrNull { it.fullName == name }
                                        ?.let {
                                            onVisitorSelected(it.id, it.fullName)
                                        }
                                }
                            )
                        }
                    }

                    // اشخاص سازمان (برای بازدید تلفنی یا غیر حضوری)
                    if (!uiState.isScheduledInPersonVisit) {
                        DropdownSelectorField(
                            value = uiState.organizationPersonName,
                            label = stringResource(R.string.label_organization_person),
                            placeholder = stringResource(R.string.hint_choose_organization_person),
                            items = uiState.organizationPersons.mapNotNull { it.fullName?.takeIf { f -> f.isNotBlank() } },
                            isRequired = false,
                            onItemSelected = { name ->
                                val person =
                                    uiState.organizationPersons.firstOrNull { it.fullName == name }
                                onPersonSelected(
                                    person?.orgPersonId,
                                    person?.fullName.orEmpty()
                                )
                            }
                        )
                    }

                    // تاریخ و ساعت واقعی
                    CustomDateTimeFields(
                        label = stringResource(R.string.label_visit_real_date_time),
                        date = uiState.visitRealDate,
                        time = uiState.visitRealTime,
                        onDateClick = { showVisitRealDatePicker = true },
                        onTimeClick = { showVisitRealTimePicker = true },
                        isRequired = true
                    )

                    // وضعیت بازدید
                    if (!uiState.isEditMode) {
                        DropdownSelectorField(
                            value = stringResource(uiState.status.titleRes),
                            label = stringResource(R.string.label_visit_status),
                            placeholder = "",
                            items = statusMap.keys.toList(),
                            isRequired = false,
                            onItemSelected = { title ->
                                statusMap[title]?.let { onStatusChanged(it) }
                            }
                        )
                    }

                    // توضیحات
                    CustomDescriptionField(
                        label = stringResource(R.string.label_description),
                        value = uiState.description,
                        onValueChange = onDescriptionChanged,
                        placeholder = stringResource(R.string.hint_description)
                    )
                    // بخش تصاویر
                    ImageAddEditSection(
                        images = uiState.images,
                        baseUrl = baseUrl,
                        isEditMode = isEditMode,
                        onAddImage = onAddImageClick,
                        onRemoveImage = onRemoveImageClick
                    )


                    Spacer(modifier = Modifier.height(32.dp))
                }

                CustomButton(
                    text = if (uiState.isEditMode) {
                        stringResource(R.string.label_edit_visit)
                    } else {
                        stringResource(R.string.label_submit_visit)
                    },
                    isLoading = uiState.isLoading,
                    enabled = uiState.isFormValid && uploadState !is DataState.Loading,
                    onClick = onSubmit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )
            }

        }

        if (showOrganizationSheet && !isPreselected) {
            OrganizationListBottomSheet(
                list = uiState.organizations,
                onDismiss = { showOrganizationSheet = false },
                onItemSelected = { item ->
                    onOrganizationSelected(item)
                    showOrganizationSheet = false
                }
            )
        }

        if (showVisitScheduledDatePicker) {
            DatePickerDialog(
                onDismiss = { showVisitScheduledDatePicker = false },
                onDateSelected = { date ->
                    val formatted = "${date.year}/${String.format(Locale.US, "%02d", date.month)}/${
                        String.format(Locale.US, "%02d", date.day)
                    }"
                    onScheduledDateSelected(formatted)
                    showVisitScheduledDatePicker = false
                }
            )
        }

        if (showVisitRealDatePicker) {
            DatePickerDialog(
                onDismiss = { showVisitRealDatePicker = false },
                onDateSelected = { date ->
                    val formatted = "${date.year}/${String.format(Locale.US, "%02d", date.month)}/${
                        String.format(Locale.US, "%02d", date.day)
                    }"
                    onRealDateSelected(formatted)
                    showVisitRealDatePicker = false
                }
            )
        }

        if (showVisitRealTimePicker) {
            TimePickerDialog(
                timePickerState = timePickerState,
                onConfirm = { hour, minute ->
                    val formattedTime = String.format(Locale.US, "%02d:%02d", hour, minute)
                    onRealTimeSelected(formattedTime)
                    showVisitRealTimePicker = false
                },
                onDismiss = { showVisitRealTimePicker = false }
            )
        }
    }
}
