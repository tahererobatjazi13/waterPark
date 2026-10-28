package ir.kitgroup.partnerManagement.feature.meeting.ui.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.dao.AttachmentFileDao
import ir.kitgroup.partnerManagement.core.database.mapper.toDto
import ir.kitgroup.partnerManagement.core.database.model.MeetingWithDetail
import ir.kitgroup.partnerManagement.core.repository.AttachmentFileRepository
import ir.kitgroup.partnerManagement.core.repository.MeetingRepository
import ir.kitgroup.partnerManagement.core.ui.util.AppErrorMapper
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import ir.kitgroup.partnerManagement.core.ui.util.datastore.MainPreferences
import ir.kitgroup.partnerManagement.core.ui.util.isNetworkError
import ir.kitgroup.partnerManagement.feature.meeting.model.ChangeStatusMeetingRequest
import ir.kitgroup.partnerManagement.feature.organization.ui.detail.AttachmentsFileUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class MeetingDetailViewModel @Inject constructor(
    private val meetingRepository: MeetingRepository,
    private val attachmentFileRepository: AttachmentFileRepository,
    private val attachmentFileDao: AttachmentFileDao,
    @ApplicationContext private val context: Context,
    mainPreferences: MainPreferences,
) : ViewModel() {

    private val _meeting = MutableStateFlow<MeetingWithDetail?>(null)
    val meeting: StateFlow<MeetingWithDetail?> = _meeting.asStateFlow()

    private var currentMeetingId: String? = null
    private var currentOrgId: String? = null

    fun observeMeetingById(meetingId: String) {
        viewModelScope.launch {
            meetingRepository
                .observeMeetingById(meetingId)
                .collect { meetingData ->
                    _meeting.value = meetingData

                    val orgId = meetingData?.meeting?.organizationId
                    val mId = meetingData?.meeting?.meetingId

                    if (!orgId.isNullOrBlank() && !mId.isNullOrBlank() && mId != currentMeetingId) {
                        currentOrgId = orgId
                        currentMeetingId = mId
                        loadMeetingAttachments(orgId, mId)
                    }
                }
        }
    }

    val baseUrlFlow: StateFlow<String> = mainPreferences.baseUrlFlow
        .map { it?.trim()?.trimEnd('/').orEmpty() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ""
        )

    private val _uiStateChange = MutableStateFlow(MeetingDetailUiState())
    val uiStateChange: StateFlow<MeetingDetailUiState> = _uiStateChange.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    fun changeStatusMeeting(
        meetingId: String,
        newStatus: Int,
        onErrorCallback: (() -> Unit)? = null,
        onSuccessCallback: () -> Unit
    ) {
        if (_uiStateChange.value.isChangeStatus) return

        viewModelScope.launch {
            _uiStateChange.update { it.copy(isChangeStatus = true) }

            try {
                val currentMeeting = _meeting.value?.meeting
                val request = ChangeStatusMeetingRequest(
                    meetingId = meetingId,
                    status = newStatus,
                    visitRealDate = currentMeeting?.visitRealDate,
                    latitude = currentMeeting?.latitude,
                    longitude = currentMeeting?.longitude,
                    description = currentMeeting?.description
                )

                val result = meetingRepository.changeStatusMeeting(request)

                result.fold(
                    onSuccess = { serverMessage ->
                        val uiMessage = if (!serverMessage.isNullOrBlank()) {
                            UiText.DynamicString(serverMessage)
                        } else {
                            UiText.StringResource(R.string.msg_change_status_success)
                        }

                        _uiEvent.emit(UiEvent.ShowMessage(uiMessage))
                        delay(500)
                        onSuccessCallback()
                    },
                    onFailure = { error ->
                        if (error.isNetworkError()) {

                            // تنها در صورت قطعی واقعی اینترنت در دیتابیس لوکال کش می‌شود
                            try {
                                meetingRepository.saveMeetingStatusChange(
                                    meetingId = currentMeeting!!.meetingId,
                                    status = newStatus,
                                    visitRealDate = currentMeeting.visitRealDate!!,
                                    latitude = currentMeeting.latitude!!,
                                    longitude = currentMeeting.longitude!!,
                                    description = currentMeeting.description!!,
                                )

                                _uiEvent.emit(
                                    UiEvent.ShowMessage(
                                        UiText.StringResource(R.string.msg_changes_saved_offline_synchronized_connection)
                                    )
                                )
                                delay(500)
                                onSuccessCallback()
                            } catch (dbError: Exception) {
                                onErrorCallback?.invoke()
                                _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(dbError)))
                            }
                        } else {
                            onErrorCallback?.invoke()
                            _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(error)))
                        }
                    }
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(e)))
            } finally {
                _uiStateChange.update { it.copy(isChangeStatus = false) }
            }
        }
    }


    private val _uiState = MutableStateFlow(AttachmentsFileUiState())
    val uiState: StateFlow<AttachmentsFileUiState> = _uiState.asStateFlow()


    fun loadMeetingAttachments(orgId: String, meetingId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isImagesLoading = true) }

            try {
                val cachedEntities = attachmentFileDao.getMeetingAttachments(orgId, meetingId)
                if (cachedEntities.isNotEmpty()) {
                    val cachedDtos = cachedEntities.map { it.toDto() }
                    _uiState.update {
                        it.copy(
                            attachments = cachedDtos,
                            isImagesLoading = false
                        )
                    }
                }
            } catch (_: Exception) {
            }

            val result = attachmentFileRepository.getAttachmentsByOrgId(orgId)

            result.onSuccess { dtoList ->
                // ذخیره در دیتابیس با انتقال meetingId مشخص
                attachmentFileRepository.saveAttachmentsToDatabase(
                    orgId = orgId,
                    meetingId = meetingId,
                    attachments = dtoList,
                    baseUrl = baseUrlFlow.value
                )

                _uiState.update {
                    it.copy(
                        attachments = dtoList,
                        isImagesLoading = false
                    )
                }
            }.onFailure { error ->
                val cachedEntities = attachmentFileDao.getMeetingAttachments(orgId, meetingId)
                val cachedDtos = cachedEntities.map { it.toDto() }

                _uiState.update {
                    it.copy(
                        attachments = cachedDtos,
                        isImagesLoading = false,
                        errorMessage = if (cachedDtos.isEmpty()) error.localizedMessage else null
                    )
                }
            }
        }
    }

    fun downloadAttachmentManually(attachmentId: String, fileExtension: String?) {
        viewModelScope.launch {
            val imagesDir = File(context.filesDir, "attachments").apply { mkdirs() }
            val localFile = File(imagesDir, "$attachmentId${fileExtension ?: ".jpg"}")

            val result = attachmentFileRepository.downloadAndSaveFile(attachmentId, localFile)
            if (result.isSuccess) {
                // بروزرسانی localPath در دیتابیس
                attachmentFileDao.updateLocalPath(attachmentId, localFile.absolutePath)
                // رفرش UI یا State
            }
        }
    }
}
