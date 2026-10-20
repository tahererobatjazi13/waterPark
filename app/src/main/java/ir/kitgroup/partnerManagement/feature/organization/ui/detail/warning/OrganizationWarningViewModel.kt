package ir.kitgroup.partnerManagement.feature.organization.ui.detail.warning

import android.content.Context
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
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

    // وضعیت لیست تذکرات سازمان برای نمایش در تب
    private val _warnings = MutableStateFlow<List<WarningWithDetail>>(emptyList())
    val warnings = _warnings.asStateFlow()

    // وضعیت فرم ثبت و ویرایش تذکر
    private val _uiState = MutableStateFlow(
        AddEditOrganizationWarningUiState(
            organizationId = organizationIdArg.orEmpty(),
            warningRecordId = warningRecordIdArg,
            isEditMode = isEditMode
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
                        organizationId = existing.warning.organizationId!!,
                        warningRecordId = existing.warning.warningId,
                        warningTypeId = existing.warning.warningTypeId.orEmpty(),
                        description = existing.warning.description.orEmpty(),
                        warningDate = existing.warning.warningDate.orEmpty(),
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

    // متدهای دریافت ورودی‌های کاربر
    fun onWarningTypeSelected(warningTypeId: String) {
        _uiState.update { it.copy(warningTypeId = warningTypeId) }
    }

    fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    /**
     * مقداردهی داده‌های ویرایش در صورتی که دیالوگ باز شود
     */
    fun setupForEdit(warning: WarningWithDetail) {
        _uiState.update {
            it.copy(
                isEditMode = true,
                warningRecordId = warning.warning.warningId,
                organizationId = warning.warning.organizationId!!,
                warningTypeId = warning.warning.warningTypeId.orEmpty(),
                description = warning.warning.description.orEmpty(),
                warningDate = warning.warning.warningDate.orEmpty()
            )
        }
    }

    fun resetForm() {
        _uiState.update {
            AddEditOrganizationWarningUiState(
                organizationId = it.organizationId,
                warningTypes = it.warningTypes
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

    /**
     * ثبت و ویرایش تذکر با پشتیبانی کامل از ذخیره‌سازی آفلاین و سینک
     */
    fun submitWarning(
        warningTypeId: String = _uiState.value.warningTypeId,
        description: String = _uiState.value.description,
        onSuccessCallback: () -> Unit
    ) {
        // بروزرسانی مقادیر قبل از اعتبارسنجی
        _uiState.update {
            it.copy(
                warningTypeId = warningTypeId,
                description = description
            )
        }

        val state = _uiState.value
        if (state.isLoading) return

        if (!state.isFormValid) {
            _uiEvent.tryEmit(UiEvent.ShowError(UiText.StringResource(R.string.error_warning_create)))
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val currentVisitorId = mainPreferences.getVisitorId()
                val currentTimestamp = System.currentTimeMillis()
                val isoTimestamp = getCurrentIsoUtcTimestamp()
                val selectedType =
                    state.warningTypes.firstOrNull { it.warningTypeId == state.warningTypeId }
                val score = selectedType?.score ?: 0
                val effectiveDate = state.warningDate.ifBlank { isoTimestamp }

                if (state.isEditMode && !state.warningRecordId.isNullOrBlank()) {
                    // --- حالت ویرایش ---
                    val targetWarningId = state.warningRecordId
                    val existingWarning =
                        warningRepository.observeWarningById(targetWarningId).firstOrNull()
                    val targetVisitorId =
                        existingWarning?.warning?.visitorId?.takeIf { it.isNotBlank() }
                            ?: currentVisitorId

                    val updateRequest = CreateOrganizationWarningRequest(
                        organizationWarningId = targetWarningId,
                        organizationId = state.organizationId,
                        warningId = state.warningTypeId,
                        visitorId = targetVisitorId,
                        meetingId = existingWarning?.warning?.meetingId,
                        warningDate = effectiveDate,
                        description = state.description.trim()
                    )

                    val syncResult =
                        warningRepository.updateOrganizationWarningAndSync(updateRequest)

                    syncResult.fold(
                        onSuccess = { serverMsg ->
                            val msg =
                                if (!serverMsg.isNullOrBlank()) UiText.DynamicString(serverMsg)
                                else UiText.StringResource(R.string.label_warnings_organization)
                            _uiEvent.emit(UiEvent.ShowMessage(msg))
                            delay(400)
                            onSuccessCallback()
                        },
                        onFailure = {
                            val nextSyncStatus =
                                if (existingWarning?.warning?.syncStatus == SyncStatus.SYNCED) {
                                    SyncStatus.PENDING_UPDATE
                                } else {
                                    existingWarning?.warning?.syncStatus
                                        ?: SyncStatus.PENDING_INSERT
                                }

                            val offlineEntity = WarningEntity(
                                warningId = targetWarningId,
                                organizationId = state.organizationId,
                                warningTypeId = state.warningTypeId,
                                visitorId = targetVisitorId,
                                meetingId = existingWarning?.warning?.meetingId,
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
                            delay(400)
                            onSuccessCallback()
                        }
                    )
                } else {
                    // --- حالت ثبت جدید ---
                    val newRequest = CreateOrganizationWarningRequest(
                        organizationWarningId = null,
                        organizationId = state.organizationId,
                        warningId = state.warningTypeId,
                        visitorId = currentVisitorId,
                        meetingId = null,
                        warningDate = effectiveDate,
                        description = state.description.trim()
                    )

                    val result = warningRepository.createOrganizationWarningAndSync(newRequest)

                    result.fold(
                        onSuccess = { serverMsg ->
                            val msg =
                                if (!serverMsg.isNullOrBlank()) UiText.DynamicString(serverMsg)
                                else UiText.StringResource(R.string.label_register_organization_warning)
                            _uiEvent.emit(UiEvent.ShowMessage(msg))
                            delay(400)
                            onSuccessCallback()
                        },
                        onFailure = {
                            val localUUID = UUID.randomUUID().toString()

                            val offlineEntity = WarningEntity(
                                warningId = localUUID,
                                organizationId = state.organizationId,
                                warningTypeId = state.warningTypeId,
                                visitorId = currentVisitorId,
                                meetingId = null,
                                warningDate = effectiveDate,
                                score = score,
                                description = state.description.trim(),
                                modifiedOn = isoTimestamp,
                                syncStatus = SyncStatus.PENDING_INSERT,
                                isLocallyModified = true,
                                lastModifiedTimestamp = currentTimestamp
                            )

                            warningRepository.saveLocal(offlineEntity)
                            _uiEvent.emit(UiEvent.ShowMessage(UiText.StringResource(R.string.msg_saved_local_storage_awaiting_synchronization)))
                            delay(400)
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
}