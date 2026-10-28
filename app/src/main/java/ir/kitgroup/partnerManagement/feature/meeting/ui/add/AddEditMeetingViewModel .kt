package ir.kitgroup.partnerManagement.feature.meeting.ui.add

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.dao.AttachmentFileDao
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationDao
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationPersonDao
import ir.kitgroup.partnerManagement.core.database.dao.VisitSubjectDao
import ir.kitgroup.partnerManagement.core.database.dao.VisitorDao
import ir.kitgroup.partnerManagement.core.database.entity.MeetingEntity
import ir.kitgroup.partnerManagement.core.database.mapper.toDto
import ir.kitgroup.partnerManagement.core.network.model.AttachmentUploadResponse
import ir.kitgroup.partnerManagement.core.repository.AttachmentFileRepository
import ir.kitgroup.partnerManagement.core.repository.MeetingRepository
import ir.kitgroup.partnerManagement.core.ui.model.OrganizationImageItem
import ir.kitgroup.partnerManagement.core.ui.util.AppErrorMapper
import ir.kitgroup.partnerManagement.core.ui.util.DataState
import ir.kitgroup.partnerManagement.core.ui.util.MeetingStatus
import ir.kitgroup.partnerManagement.core.ui.util.MeetingType
import ir.kitgroup.partnerManagement.core.ui.util.SyncStatus
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import ir.kitgroup.partnerManagement.core.ui.util.UserRole
import ir.kitgroup.partnerManagement.core.ui.util.datastore.MainPreferences
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate
import ir.kitgroup.partnerManagement.core.ui.util.isNetworkError
import ir.kitgroup.partnerManagement.core.ui.util.jalaliToGregorian
import ir.kitgroup.partnerManagement.feature.meeting.model.CreateMeetingRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import saman.zamani.persiandate.PersianDate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import java.io.File

@HiltViewModel
class AddEditMeetingViewModel @Inject constructor(
    private val organizationDao: OrganizationDao,
    private val visitSubjectDao: VisitSubjectDao,
    private val organizationPersonDao: OrganizationPersonDao,
    private val visitorDao: VisitorDao,
    private val meetingRepository: MeetingRepository,
    private val mainPreferences: MainPreferences,
    private val attachmentFileRepository: AttachmentFileRepository,
    private val attachmentFileDao: AttachmentFileDao,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val baseUrlFlow: StateFlow<String> = mainPreferences.baseUrlFlow
        .map { it?.trim()?.trimEnd('/').orEmpty() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ""
        )

    private val meetingIdArg: String? = savedStateHandle["meetingId"]
    val organizationIdArg: String? = savedStateHandle["organizationId"]
    val isEditMode: Boolean = !meetingIdArg.isNullOrBlank()

    private val _uiState = MutableStateFlow(
        AddEditMeetingUiState(
            isEditMode = isEditMode,
            organizationId = organizationIdArg.orEmpty()
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        initializeDefaultDateTime()
        loadInitialData()
    }

    private fun initializeDefaultDateTime() {
        val today = PersianDate()
        val defaultDate = "${today.shYear}/${String.format(Locale.US, "%02d", today.shMonth)}/${
            String.format(Locale.US, "%02d", today.shDay)
        }"
        val calendar = Calendar.getInstance()
        val defaultTime = String.format(
            Locale.US,
            "%02d:%02d",
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE)
        )

        _uiState.update {
            it.copy(
                visitScheduledDate = defaultDate,
                visitScheduledTime = defaultTime,
                visitRealDate = defaultDate,
                visitRealTime = defaultTime
            )
        }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isInitialLoading = true) }
            try {
                val currentVisitorId = mainPreferences.getVisitorId()
                val roleCode =
                    mainPreferences.getRoleCode()
                val isSupervisor = UserRole.fromCode(roleCode.toString()) == UserRole.SUPERVISOR

                val orgs = organizationDao.observeAll().firstOrNull().orEmpty()
                val subjects = visitSubjectDao.observeAll().firstOrNull().orEmpty()
                val visitors = visitorDao.observeAll().firstOrNull().orEmpty().map {
                    VisitorItem(id = it.visitorId, fullName = it.name.orEmpty())
                }

                val preselectedOrg =
                    orgs.firstOrNull { it.organization.organizationId == organizationIdArg }

                val defaultVisitorId = preselectedOrg?.organization?.activeVisitorId
                    ?.takeIf { it.isNotBlank() && it != "00000000-0000-0000-0000-000000000000" }
                    ?: preselectedOrg?.organization?.visitorId
                        ?.takeIf { it.isNotBlank() && it != "00000000-0000-0000-0000-000000000000" }
                    ?: currentVisitorId

                val defaultVisitorName =
                    visitors.firstOrNull { it.id == defaultVisitorId }?.fullName.orEmpty()

                _uiState.update {
                    it.copy(
                        organizations = orgs,
                        visitSubjects = subjects,
                        allVisitors = visitors,
                        isSupervisor = isSupervisor,
                        organizationName = preselectedOrg?.organization?.name.orEmpty(),
                        visitorId = defaultVisitorId,
                        visitorName = defaultVisitorName
                    )
                }

                // لود لیست اشخاص سازمان انتخابی اولیه
                if (!organizationIdArg.isNullOrBlank()) {
                    loadPersonsForOrg(organizationIdArg)
                }

                // در حالت ویرایش: لود اطلاعات جلسه
                if (isEditMode && !meetingIdArg.isNullOrBlank()) {
                    val meetingWithDetail =
                        meetingRepository.observeMeetingById(meetingIdArg).firstOrNull()
                    meetingWithDetail?.let { detail ->
                        val entity = detail.meeting
                        val targetOrgId = entity.organizationId.orEmpty()
                        val targetVisitorId = entity.visitorId
                            ?: detail.organization?.activeVisitorId
                            ?: detail.organization?.visitorId
                            ?: defaultVisitorId

                        val vName = entity.visitorName?.takeIf { it.isNotBlank() }
                            ?: visitors.firstOrNull { it.id == targetVisitorId }?.fullName.orEmpty()

                        loadPersonsForOrg(targetOrgId)

                        _uiState.update { current ->
                            current.copy(
                                organizationId = targetOrgId,
                                organizationName = detail.organization?.name.orEmpty(),
                                subjectVisitId = entity.subjectVisitId.orEmpty(),
                                organizationPersonId = entity.organizationPersonId,
                                organizationPersonName = entity.organizationPersonName.orEmpty(),
                                visitorId = targetVisitorId,
                                visitorName = vName,
                                visitType = MeetingType.fromValue(entity.type),
                                status = MeetingStatus.fromId(entity.status),
                                visitScheduledDate = entity.visitDate?.takeIf { it.isNotBlank() }
                                    ?.let { formatJalaliDate(it) } ?: current.visitScheduledDate,
                                visitRealDate = entity.visitRealDate?.takeIf { it.isNotBlank() }
                                    ?.let { formatJalaliDate(it) } ?: current.visitRealDate,
                                visitRealTime = entity.visitTime.orEmpty()
                                    .ifBlank { current.visitRealTime },
                                description = entity.description.orEmpty(),
                                latitude = entity.latitude,
                                longitude = entity.longitude
                            )
                        }
                        val currentMeetingId = meetingIdArg

                        val currentBaseUrl =
                            mainPreferences.baseUrlFlow.firstOrNull().orEmpty().trimEnd('/')
                        val cachedEntities = try {
                            attachmentFileDao.getMeetingAttachments(targetOrgId, currentMeetingId)
                        } catch (e: Exception) {
                            emptyList()
                        }

                        val attachments = if (cachedEntities.isNotEmpty()) {
                            cachedEntities.map { it.toDto() }
                        } else {
                            // اگر در کش نبود، از طریق ریپازیتوری از سرور می‌گیریم
                            val result = attachmentFileRepository.getAttachmentsByOrgId(targetOrgId)
                            result.getOrDefault(emptyList())
                        }

                        val loadedImages = attachments.map { attachment ->
                            val imagesDir = File(context.filesDir, "attachments")
                            val extension = if (!attachment.fileExtension.isNullOrBlank()) {
                                if (attachment.fileExtension.startsWith(".")) attachment.fileExtension else ".${attachment.fileExtension}"
                            } else ".jpg"

                            val localFile =
                                File(imagesDir, "${attachment.attachmentFileId}$extension")
                            val localUri = if (localFile.exists()) Uri.fromFile(localFile) else null

                            val serverImageUrl = when {
                                !attachment.fileUrl.isNullOrBlank() && attachment.fileUrl.startsWith(
                                    "http",
                                    true
                                ) -> {
                                    attachment.fileUrl
                                }

                                !attachment.fileUrl.isNullOrBlank() -> {
                                    val cleanPath = attachment.fileUrl.removePrefix("/")
                                    "$currentBaseUrl/$cleanPath"
                                }

                                else -> {
                                    "$currentBaseUrl/api/OrgAttachmentFiles/download/${attachment.attachmentFileId}"
                                }
                            }

                            // فرض بر این است که از مدل مشابه OrganizationImageItem یا MeetingImageItem استفاده می‌کنید
                            OrganizationImageItem(
                                uri = localUri,
                                fileType = attachment.fileType,
                                description = attachment.description.orEmpty(),
                                uploadedFileId = attachment.attachmentFileId,
                                serverImageUrl = serverImageUrl
                            )
                        }
                        _uiState.update { it.copy(images = loadedImages) }
                    }
                }
            } catch (e: Exception) {
                Log.e("AddEditMeetingVM", "Error loading data", e)
                _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(e)))
            } finally {
                _uiState.update { it.copy(isInitialLoading = false) }
            }
        }
    }

    private fun loadPersonsForOrg(orgId: String) {
        viewModelScope.launch {
            val persons =
                organizationPersonDao.observeByOrganizationId(orgId).firstOrNull().orEmpty()
            _uiState.update { it.copy(organizationPersons = persons) }
        }
    }

    // هندلرهای تغییر فیلدهای فرم
    fun onOrganizationSelected(orgId: String, orgName: String) {
        _uiState.update {
            it.copy(
                organizationId = orgId,
                organizationName = orgName,
                organizationPersonId = null,
                organizationPersonName = ""
            )
        }
        loadPersonsForOrg(orgId)
    }

    fun onSubjectChanged(subjectId: String) =
        _uiState.update { it.copy(subjectVisitId = subjectId) }

    fun onVisitTypeChanged(type: MeetingType) = _uiState.update { it.copy(visitType = type) }
    fun onStatusChanged(status: MeetingStatus) = _uiState.update { it.copy(status = status) }
    fun onVisitorSelected(id: String, name: String) =
        _uiState.update { it.copy(visitorId = id, visitorName = name) }

    fun onPersonSelected(personId: String?, personName: String) = _uiState.update {
        it.copy(
            organizationPersonId = personId,
            organizationPersonName = personName
        )
    }

    fun onScheduledDateChanged(date: String) =
        _uiState.update { it.copy(visitScheduledDate = date) }

    fun onScheduledTimeChanged(time: String) =
        _uiState.update { it.copy(visitScheduledTime = time) }

    fun onRealDateChanged(date: String) = _uiState.update { it.copy(visitRealDate = date) }
    fun onRealTimeChanged(time: String) = _uiState.update { it.copy(visitRealTime = time) }
    fun onDescriptionChanged(desc: String) = _uiState.update { it.copy(description = desc) }
    fun onLocationUpdated(latitude: Double, longitude: Double) {
        _uiState.update {
            it.copy(
                latitude = latitude,
                longitude = longitude
            )
        }
    }

    private fun getCurrentIsoUtcTimestamp(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(Date())
    }

    fun submitMeeting(onSuccessCallback: () -> Unit) {
        val state = _uiState.value
        if (state.isLoading) return


        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val actualVisitorId = if (state.isSupervisor) {
                    state.visitorId?.takeIf { it.isNotBlank() && it != "00000000-0000-0000-0000-000000000000" }
                } else {
                    mainPreferences.getVisitorId().ifBlank { null }
                }

                val formattedVisitDate = jalaliToGregorian(
                    if (state.isScheduledInPersonVisit) state.visitScheduledDate else state.visitRealDate
                ).orEmpty()

                val formattedRealDate = jalaliToGregorian(state.visitRealDate).orEmpty()
                val currentTimestamp = System.currentTimeMillis()
                val isoTimestamp = getCurrentIsoUtcTimestamp()
                val isVisitInPerson = state.visitType == MeetingType.PLANNED_IN_PERSON ||
                        state.visitType == MeetingType.UNPLANNED_IN_PERSON

                val requestLatitude = if (isVisitInPerson) state.latitude else null
                val requestLongitude = if (isVisitInPerson) state.longitude else null


                val request = CreateMeetingRequest(
                    meetingId = if (isEditMode) meetingIdArg else null,
                    organizationId = state.organizationId,
                    organizationPersonId = state.organizationPersonId?.takeIf { it.isNotBlank() && it != "00000000-0000-0000-0000-000000000000" },
                    visitorId = actualVisitorId,
                    subjectVisitId = state.subjectVisitId,
                    description = state.description.trim().ifEmpty { null },
                    visitDate = formattedVisitDate,
                    visitRealDate = formattedRealDate,
                    visitTime = state.visitRealTime,
                    status = state.status.id,
                    type = state.visitType.value,
                    latitude = requestLatitude,
                    longitude = requestLongitude
                )
                Log.d("requestrequest", request.toString())

                if (isEditMode) {
                    val targetId = meetingIdArg!!
                    val existingMeeting =
                        meetingRepository.observeMeetingById(targetId).firstOrNull()?.meeting
                    val result = meetingRepository.updateMeetingAndSync(request)

                    result.fold(
                        onSuccess = { serverMsg ->
                            val msg =
                                if (!serverMsg.isNullOrBlank()) UiText.DynamicString(serverMsg)
                                else UiText.StringResource(R.string.msg_meeting_updated)
                            _uiEvent.emit(UiEvent.ShowMessage(msg))
                            delay(400)
                            onSuccessCallback()
                        },
                        onFailure = {
                            val nextSyncStatus =
                                if (existingMeeting?.syncStatus == SyncStatus.SYNCED) {
                                    SyncStatus.PENDING_UPDATE
                                } else {
                                    existingMeeting?.syncStatus ?: SyncStatus.PENDING_INSERT
                                }

                            val offlineEntity = MeetingEntity(
                                meetingId = targetId,
                                subjectVisitId = state.subjectVisitId,
                                organizationId = state.organizationId,
                                visitorId = actualVisitorId,
                                organizationPersonId = state.organizationPersonId,
                                organizationPersonName = state.organizationPersonName.ifBlank { null },
                                type = state.visitType.value,
                                status = state.status.id,
                                visitDate = formattedVisitDate,
                                visitRealDate = formattedRealDate,
                                visitTime = state.visitRealTime,
                                description = state.description,
                                latitude = state.latitude,
                                longitude = state.longitude,
                                stateCode = 0,
                                modifiedOn = isoTimestamp,
                                syncStatus = nextSyncStatus,
                                isLocallyModified = true,
                                lastModifiedTimestamp = currentTimestamp
                            )
                            meetingRepository.saveLocal(offlineEntity)
                            _uiEvent.emit(UiEvent.ShowMessage(UiText.StringResource(R.string.msg_changes_saved_offline_synchronized_connection)))
                            delay(400)
                            onSuccessCallback()
                        }
                    )
                } else {
                    val result = meetingRepository.createMeetingAndSync(request)

                    result.fold(
                        onSuccess = { response ->
                            val newMeetingId = response.data
                            if (newMeetingId.isNullOrBlank()) {
                                _uiEvent.emit(
                                    UiEvent.ShowError(
                                        UiText.DynamicString("شناسه بازدید از سمت سرور دریافت نشد.")
                                    )
                                )
                                return@fold
                            }
                            if (_uiState.value.images.isNotEmpty()) {
                                uploadImagesForMeeting(newMeetingId, state.organizationId)
                            }

                            val msg = response.message
                                ?.takeIf { it.isNotBlank() }
                                ?.let { UiText.DynamicString(it) }
                                ?: UiText.StringResource(R.string.msg_meeting_created)
                            _uiEvent.emit(UiEvent.ShowMessage(msg))
                            delay(400)

                            onSuccessCallback()
                        },
                        onFailure = { error ->
                            if (error.isNetworkError()) {
                                val localUUID = UUID.randomUUID().toString()
                                val offlineEntity = MeetingEntity(
                                    meetingId = localUUID,
                                    subjectVisitId = state.subjectVisitId,
                                    organizationId = state.organizationId,
                                    visitorId = actualVisitorId,
                                    organizationPersonId = state.organizationPersonId,
                                    organizationPersonName = state.organizationPersonName.ifBlank { null },
                                    type = state.visitType.value,
                                    status = state.status.id,
                                    visitDate = formattedVisitDate,
                                    visitRealDate = formattedRealDate,
                                    visitTime = state.visitRealTime,
                                    description = state.description,
                                    latitude = state.latitude,
                                    longitude = state.longitude,
                                    stateCode = 0,
                                    modifiedOn = isoTimestamp,
                                    syncStatus = SyncStatus.PENDING_INSERT,
                                    isLocallyModified = true,
                                    lastModifiedTimestamp = currentTimestamp
                                )
                                meetingRepository.saveLocal(offlineEntity)
                                _uiEvent.emit(UiEvent.ShowMessage(UiText.StringResource(R.string.msg_changes_saved_offline_synchronized_connection)))
                                delay(400)
                                onSuccessCallback()
                            } else {
                                _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(error)))
                            }
                        }
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("submitMeeting", "Error occurred", e)
                _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(e)))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private val _uploadState =
        MutableStateFlow<DataState<AttachmentUploadResponse>>(DataState.Idle)
    val uploadState = _uploadState.asStateFlow()

    private fun uploadFileForMeeting(
        fileUri: Uri,
        organizationId: String,
        meetingId: String,
        fileType: Int,
        fileName: String?,
        description: String?
    ) {
        viewModelScope.launch {
            _uploadState.value = DataState.Loading

            val result = attachmentFileRepository.uploadAttachmentFile(
                context = context,
                uri = fileUri,
                organizationId = organizationId,
                meetingId = meetingId,
                fileType = fileType,
                name = fileName,
                description = description
            )

            result.onSuccess { response ->
                _uploadState.value = DataState.Success(response)
                _uiEvent.emit(UiEvent.ShowMessage(UiText.StringResource(R.string.msg_file_uploaded_successfully)))

                attachmentFileRepository.upsertUploadedAttachment(
                    orgId = organizationId,
                    meetingId = meetingId,
                    response = response,
                    fileType = fileType,
                    description = description
                )
            }.onFailure { error ->
                _uploadState.value = DataState.Error(
                    error.localizedMessage?.let { UiText.DynamicString(it) }
                        ?: UiText.StringResource(R.string.error_unknown)
                )
            }
        }
    }

    fun onRemoveImage(index: Int) {
        val currentList = _uiState.value.images.toMutableList()
        if (index in currentList.indices) {
            currentList.removeAt(index)
            _uiState.update { it.copy(images = currentList) }
        }
    }

    fun onAddLocalAttachment(uri: Uri, fileType: Int, description: String) {
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri) ?: ""
        val extension = when {
            mimeType.contains("pdf", ignoreCase = true) -> ".pdf"
            mimeType.contains("png", ignoreCase = true) -> ".png"
            else -> ".jpg"
        }

        val currentList = _uiState.value.images.toMutableList()
        currentList.add(
            OrganizationImageItem(
                uri = uri,
                fileType = fileType,
                description = description,
                // اگر فیلدی برای پسوند یا mimeType در مدل دارید اینجا مقداردهی کنید
            )
        )
        _uiState.update { it.copy(images = currentList) }

        val currentMeetingId = meetingIdArg
        val currentOrgId = _uiState.value.organizationId

        if (_uiState.value.isEditMode && !currentMeetingId.isNullOrBlank()) {
            uploadFileForMeeting(
                fileUri = uri,
                organizationId = currentOrgId,
                meetingId = currentMeetingId,
                fileType = fileType,
                fileName = "org_${System.currentTimeMillis()}$extension",
                description = description
            )
        }
    }

    private suspend fun uploadImagesForMeeting(meetingId: String, organizationId: String) {
        val imagesToUpload = _uiState.value.images.filter { it.uploadedFileId == null }

        for (img in imagesToUpload) {
            val fileName = "org_${System.currentTimeMillis()}_${img.fileType}.jpg"

            attachmentFileRepository.uploadAttachmentFile(
                context = context,
                uri = img.uri!!,
                organizationId = organizationId,
                meetingId = meetingId,
                fileType = img.fileType,
                name = fileName,
                description = img.description
            ).fold(
                onSuccess = { uploadResponse ->
                    attachmentFileRepository.upsertUploadedAttachment(
                        orgId = organizationId,
                        meetingId = meetingId,
                        response = uploadResponse,
                        fileType = img.fileType,
                        description = img.description
                    )
                },
                onFailure = { error ->
                    error.printStackTrace()
                }
            )
        }
    }
}
