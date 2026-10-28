package ir.kitgroup.partnerManagement.feature.organization.ui.detail

import android.content.Context
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.dao.AttachmentFileDao
import ir.kitgroup.partnerManagement.core.database.dao.VisitSubjectDao
import ir.kitgroup.partnerManagement.core.database.entity.VisitSubjectEntity
import ir.kitgroup.partnerManagement.core.database.mapper.toDto
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import ir.kitgroup.partnerManagement.core.repository.AttachmentFileRepository
import ir.kitgroup.partnerManagement.core.repository.ContractRepository
import ir.kitgroup.partnerManagement.core.repository.OrganizationRepository
import ir.kitgroup.partnerManagement.core.repository.VisitorRepository
import ir.kitgroup.partnerManagement.core.ui.util.AppErrorMapper
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationStatus
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import ir.kitgroup.partnerManagement.core.ui.util.datastore.MainPreferences
import ir.kitgroup.partnerManagement.core.ui.util.isNetworkError
import ir.kitgroup.partnerManagement.feature.organization.model.ChangeStatusOrganizationRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException


@HiltViewModel
class OrganizationDetailViewModel @Inject constructor(
    private val organizationRepository: OrganizationRepository,
    private val attachmentFileRepository: AttachmentFileRepository,
    private val contractRepository: ContractRepository,
    private val visitorRepository: VisitorRepository,
    visitSubjectDao: VisitSubjectDao,
    private val attachmentFileDao: AttachmentFileDao,
    mainPreferences: MainPreferences,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val organizationId: String = checkNotNull(savedStateHandle["organizationId"])

    val baseUrlFlow: StateFlow<String> = mainPreferences.baseUrlFlow
        .map { it?.trim()?.trimEnd('/').orEmpty() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ""
        )

    private val _activeVisitorName = MutableStateFlow<String?>(null)
    val activeVisitorName: StateFlow<String?> = _activeVisitorName.asStateFlow()

    private val _activeContractName = MutableStateFlow<String?>(null)
    val activeContractName: StateFlow<String?> = _activeContractName.asStateFlow()

    private val _organization =
        MutableStateFlow<OrganizationWithDetail?>(null)

    val organization: StateFlow<OrganizationWithDetail?> =
        _organization.asStateFlow()

    fun getOrganizationById(organizationId: String) {
        viewModelScope.launch {
            organizationRepository
                .observeById(organizationId)
                .collect { organization ->
                    _organization.value = organization
                    val activeVisitorId = organization?.organization?.activeVisitorId
                    val activeContractId = organization?.organization?.activeContractId

                    if (!activeVisitorId.isNullOrBlank()) {
                        viewModelScope.launch {
                            visitorRepository.getAllVisitors().collect { visitors ->
                                _activeVisitorName.value =
                                    visitors.firstOrNull { it.visitorId == activeVisitorId }?.name
                            }
                        }
                    } else {
                        _activeVisitorName.value = null
                    }

                    if (!activeContractId.isNullOrBlank()) {
                        viewModelScope.launch {
                            contractRepository.observeAll().collect { contracts ->
                                _activeContractName.value =
                                    contracts.firstOrNull { it.contractId == activeContractId }?.title
                            }
                        }
                    } else {
                        _activeContractName.value = null
                    }
                }
        }
    }

    val visitSubjects: StateFlow<List<VisitSubjectEntity>> =
        visitSubjectDao.observeAll()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    private val _uiStateChange = MutableStateFlow(OrganizationDetailUiState())
    val uiStateChange: StateFlow<OrganizationDetailUiState> = _uiStateChange.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    fun changeStatusOrganization(
        organizationId: String,
        status: OrganizationStatus,
        deactiveDate: String?,
        reason: String?,
        visitDate: String?,
        subjectVisitId: String?,
        onErrorCallback: (() -> Unit)? = null,
        onSuccessCallback: () -> Unit
    ) {
        if (_uiStateChange.value.isChangeStatus) return

        viewModelScope.launch {
            _uiStateChange.update { it.copy(isChangeStatus = true) }

            try {
                val request = ChangeStatusOrganizationRequest(
                    organizationId = organizationId,
                    statusOrg = status.id,
                    deactiveDate = deactiveDate,
                    reason = reason,
                    visitDate = visitDate,
                    subjectVisitId = subjectVisitId
                )

                val result = organizationRepository.changeStatusOrganization(request)

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
                            val activeVisitorId = _organization.value?.organization?.activeVisitorId
                            val hasActiveVisitor = !activeVisitorId.isNullOrBlank() &&
                                    activeVisitorId != "00000000-0000-0000-0000-000000000000"
                            if (!hasActiveVisitor) {
                                // بستن دیالوگ و جلوگیری از ثبت تغییرات آفلاین
                                onErrorCallback?.invoke()
                                _uiEvent.emit(
                                    UiEvent.ShowError(
                                        UiText.StringResource(R.string.error_no_active_visitor) // یا متن داینامیک: UiText.DynamicString("برای این سازمان ویزیتور فعال مشخص نشده است.")
                                    )
                                )
                                return@fold
                            }
                            // تنها در صورت قطعی واقعی اینترنت در دیتابیس لوکال کش می‌شود
                            try {
                                organizationRepository.saveOfflineOrganizationStatusChange(
                                    organizationId = organizationId,
                                    statusOrg = status.id,
                                    deactiveDate = deactiveDate,
                                    reason = reason,
                                    visitDate = visitDate,
                                    subjectVisitId = subjectVisitId
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

    init {
        loadAttachments()
    }

    fun loadAttachments() {
        viewModelScope.launch {
            _uiState.update { it.copy(isImagesLoading = true) }

            // ۱. ابتدا دیتای ذخیره‌شده قبلی را از دیتابیس لوکال می‌خوانیم تا کاربر بلافاصله عکس‌ها را ببیند
            try {
                val cachedEntities =
                    attachmentFileDao.getOrganizationAttachments(organizationId)
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

            //  تلاش برای دریافت دیتای جدید از سرور
            val result = attachmentFileRepository.getAttachmentsByOrgId(organizationId)

            result.onSuccess { dtoList ->
                // ذخیره دیتای سرور در دیتابیس
                attachmentFileRepository.saveAttachmentsToDatabase(
                    orgId = organizationId,
                    meetingId = null,
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
                // ۳. اگر اینترنت قطع بود یا سرور خطا داد، مطمئن می‌شویم لودینگ بسته شود و دیتابیس لوکال اگر لود نشده خوانده شود
                val cachedEntities =
                    attachmentFileDao.getOrganizationAttachments(organizationId)
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