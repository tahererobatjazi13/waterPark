package ir.kitgroup.partnerManagement.feature.organization.ui.detail.warning

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.WarningEntity
import ir.kitgroup.partnerManagement.core.database.model.WarningWithDetail
import ir.kitgroup.partnerManagement.core.repository.WarningRepository
import ir.kitgroup.partnerManagement.core.ui.util.AppErrorMapper
import ir.kitgroup.partnerManagement.core.ui.util.SyncStatus
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import ir.kitgroup.partnerManagement.core.ui.util.datastore.MainPreferences
import ir.kitgroup.partnerManagement.feature.organization.model.CreateOrganizationWarningRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class OrganizationWarningViewModel @Inject constructor(
    private val warningRepository: WarningRepository,
    @ApplicationContext private val context: Context,
    private val mainPreferences: MainPreferences,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val organizationIdArg: String? = savedStateHandle["organizationId"]
    private val warningRecordIdArg: String? = savedStateHandle["warningId"]
    val isEditMode: Boolean = !warningRecordIdArg.isNullOrBlank()

    private val _warnings = MutableStateFlow<List<WarningWithDetail>>(emptyList())
    val warnings = _warnings.asStateFlow()

    private val _uiState = MutableStateFlow(
        AddEditOrganizationWarningUiState(
            organizationId = organizationIdArg.orEmpty(),
            warningRecordId = warningRecordIdArg,
            isEditMode = isEditMode,
            meetingId = null
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        loadWarningTypes()
        if (isEditMode && !warningRecordIdArg.isNullOrBlank()) {
            loadWarningDetail(warningRecordIdArg)
        }
    }

    private fun loadWarningTypes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isInitialLoading = true) }
            val types = warningRepository.observeWarningTypes().firstOrNull().orEmpty()
            _uiState.update { current ->
                current.copy(
                    warningTypes = types,
                    isInitialLoading = false
                )
            }
        }
    }

    private fun loadWarningDetail(warningId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isInitialLoading = true) }
            val existing = warningRepository.observeWarningById(warningId).firstOrNull()
            if (existing != null) {
                _uiState.update { current ->
                    current.copy(
                        organizationId = existing.warning.organizationId.orEmpty(),
                        warningRecordId = existing.warning.warningId,
                        warningTypeId = existing.warning.warningTypeId.orEmpty(),
                        description = existing.warning.description.orEmpty(),
                        warningDate = existing.warning.warningDate.orEmpty(),
                        meetingId = existing.warning.meetingId,
                        isEditMode = true,
                        isInitialLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(isInitialLoading = false) }
            }
        }
    }
    fun observeOrganizationWarnings(organizationId: String) {
        viewModelScope.launch {
            warningRepository.observeByOrganization(organizationId).collect { list ->
                _warnings.value = list
            }
        }
    }

    fun onWarningTypeSelected(warningTypeId: String) {
        _uiState.update { it.copy(warningTypeId = warningTypeId) }
    }

    fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun setupForEdit(warning: WarningWithDetail) {
        _uiState.update {
            it.copy(
                isEditMode = true,
                warningRecordId = warning.warning.warningId,
                organizationId = warning.warning.organizationId.orEmpty(),
                warningTypeId = warning.warning.warningTypeId.orEmpty(),
                description = warning.warning.description.orEmpty(),
                warningDate = warning.warning.warningDate.orEmpty(),
                meetingId = warning.warning.meetingId
            )
        }
    }

    /**
     * ریست کامل فرم برای ثبت جدید همراه با تعیین صریح شناسه سازمان
     */
    fun resetForm(targetOrgId: String? = null, targetMeetingId: String? = null) {
        _uiState.update { current ->
            AddEditOrganizationWarningUiState(
                organizationId = targetOrgId ?: organizationIdArg.orEmpty(),
                warningRecordId = null,
                isEditMode = false,
                warningTypes = current.warningTypes,
                meetingId = targetMeetingId
            )
        }
    }

    private fun getCurrentIsoUtcTimestamp(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(Date())
    }

    /**
     * ثبت و ویرایش تذکر با پشتیبانی کامل از ذخیره‌سازی آفلاین و سینک
     */
    fun submitWarning(
        organizationId: String? = null,
        warningTypeId: String = _uiState.value.warningTypeId,
        description: String = _uiState.value.description,
        onSuccessCallback: () -> Unit
    ) {
        // بروزرسانی صریح استیت با مقادیر ورودی
        _uiState.update { current ->
            current.copy(
                organizationId = organizationId ?: current.organizationId,
                warningTypeId = warningTypeId,
                description = description
            )
        }

        val state = _uiState.value
        if (state.isLoading) return

        // بررسی اینکه آیا شناسه سازمان موجود است
        if (state.organizationId.isBlank() || state.warningTypeId.isBlank()) {
            _uiEvent.tryEmit(UiEvent.ShowError(UiText.StringResource(R.string.error_warning_create)))
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val currentVisitorId = mainPreferences.getVisitorId()
                val currentTimestamp = System.currentTimeMillis()
                val isoTimestamp = getCurrentIsoUtcTimestamp()
                val selectedType = state.warningTypes.firstOrNull { it.warningTypeId == state.warningTypeId }
                val score = selectedType?.score ?: 0
                val effectiveDate = state.warningDate.ifBlank { isoTimestamp }

                // شرط دقیق ورود به حالت ویرایش: باید حتماً هم isEditMode فعال باشد و هم شناسه تذکر وجود داشته باشد
                if (state.isEditMode && !state.warningRecordId.isNullOrBlank()) {
                    // --- حالت ویرایش ---
                    val targetWarningId = state.warningRecordId
                    val existingWarning = warningRepository.observeWarningById(targetWarningId).firstOrNull()
                    val targetVisitorId = existingWarning?.warning?.visitorId?.takeIf { it.isNotBlank() } ?: currentVisitorId

                    val updateRequest = CreateOrganizationWarningRequest(
                        organizationWarningId = targetWarningId,
                        organizationId = state.organizationId,
                        warningId = state.warningTypeId,
                        visitorId = targetVisitorId,
                        meetingId = state.meetingId,
                        warningDate = effectiveDate,
                        description = state.description.trim()
                    )

                    val syncResult = warningRepository.updateOrganizationWarningAndSync(updateRequest)

                    syncResult.fold(
                        onSuccess = { serverMsg ->
                            val msg = if (!serverMsg.isNullOrBlank()) UiText.DynamicString(serverMsg)
                            else UiText.StringResource(R.string.label_warnings_organization)
                            _uiEvent.emit(UiEvent.ShowMessage(msg))
                            delay(300)
                            resetForm(state.organizationId, state.meetingId)
                            onSuccessCallback()
                        },
                        onFailure = {
                            val nextSyncStatus = if (existingWarning?.warning?.syncStatus == SyncStatus.SYNCED) {
                                SyncStatus.PENDING_UPDATE
                            } else {
                                existingWarning?.warning?.syncStatus ?: SyncStatus.PENDING_INSERT
                            }

                            val offlineEntity = WarningEntity(
                                warningId = targetWarningId,
                                organizationId = state.organizationId,
                                warningTypeId = state.warningTypeId,
                                visitorId = targetVisitorId,
                                meetingId = state.meetingId,
                                warningDate = effectiveDate,
                                score = score,
                                description = state.description.trim(),
                                modifiedOn = isoTimestamp,
                                syncStatus = nextSyncStatus,
                                isLocallyModified = true,
                                lastModifiedTimestamp = currentTimestamp
                            )

                            warningRepository.saveLocal(offlineEntity)
                            _uiEvent.emit(UiEvent.ShowMessage(UiText.StringResource(R.string.msg_changes_saved_offline_synchronized_connection)))
                            delay(300)
                            resetForm(state.organizationId, state.meetingId)
                            onSuccessCallback()
                        }
                    )
                } else {
                    // --- حالت ثبت جدید (تکمیل کامل کد) ---
                    val localId = UUID.randomUUID().toString()
                    val newRequest = CreateOrganizationWarningRequest(
                        organizationWarningId = null,
                        organizationId = state.organizationId,
                        warningId = state.warningTypeId,
                        visitorId = currentVisitorId,
                        meetingId = state.meetingId,
                        warningDate = effectiveDate,
                        description = state.description.trim()
                    )

                    val syncResult = warningRepository.createOrganizationWarningAndSync(newRequest)

                    syncResult.fold(
                        onSuccess = { serverMsg ->
                            val msg = if (!serverMsg.isNullOrBlank()) UiText.DynamicString(serverMsg)
                            else UiText.StringResource(R.string.label_warnings_organization)
                            _uiEvent.emit(UiEvent.ShowMessage(msg))
                            delay(300)
                            resetForm(state.organizationId, state.meetingId)
                            onSuccessCallback()
                        },
                        onFailure = {
                            val offlineEntity = WarningEntity(
                                warningId = localId,
                                organizationId = state.organizationId,
                                warningTypeId = state.warningTypeId,
                                visitorId = currentVisitorId,
                                meetingId = state.meetingId,
                                warningDate = effectiveDate,
                                score = score,
                                description = state.description.trim(),
                                modifiedOn = isoTimestamp,
                                syncStatus = SyncStatus.PENDING_INSERT,
                                isLocallyModified = true,
                                lastModifiedTimestamp = currentTimestamp
                            )

                            warningRepository.saveLocal(offlineEntity)
                            _uiEvent.emit(UiEvent.ShowMessage(UiText.StringResource(R.string.msg_changes_saved_offline_synchronized_connection)))
                            delay(300)
                            resetForm(state.organizationId, state.meetingId)
                            onSuccessCallback()
                        }
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(e)))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun deleteWarning(warningId: String) {
        viewModelScope.launch {
            warningRepository.deleteById(warningId)
        }
    }
}
