package ir.kitgroup.partnerManagement.feature.stand.ui.assigned_stands_organization

import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.dao.AssignedStandDao
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationDao
import ir.kitgroup.partnerManagement.core.database.dao.StandTypeDao
import ir.kitgroup.partnerManagement.core.database.entity.AssignedStandEntity
import ir.kitgroup.partnerManagement.core.repository.AssignedStandsRepository
import ir.kitgroup.partnerManagement.core.ui.util.AppErrorMapper
import ir.kitgroup.partnerManagement.core.ui.util.SyncStatus
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import ir.kitgroup.partnerManagement.core.ui.util.datastore.MainPreferences
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate
import ir.kitgroup.partnerManagement.core.ui.util.isNetworkError
import ir.kitgroup.partnerManagement.core.ui.util.jalaliToGregorian
import ir.kitgroup.partnerManagement.feature.stand.model.CreateAssignStandRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import saman.zamani.persiandate.PersianDate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class AddAssignedStandsOrganizationViewModel @Inject constructor(
    private val organizationDao: OrganizationDao,
    private val standTypeDao: StandTypeDao,
    private val assignedStandDao: AssignedStandDao,
    private val assignedStandsRepository: AssignedStandsRepository,
    private val mainPreferences: MainPreferences,
    @ApplicationContext private val context: Context,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val preselectedOrganizationId: String? = savedStateHandle["organizationId"]
    val isOrganizationLocked: Boolean = !preselectedOrganizationId.isNullOrBlank()


    private val assignedStandIdArg: String? =
        savedStateHandle["assignedStandId"]

    val isEditMode: Boolean = !assignedStandIdArg.isNullOrBlank()

    private val _uiState = MutableStateFlow(
        AddAssignedStandsOrganizationUiState(
            isEditMode = isEditMode,
            organizationId = preselectedOrganizationId.orEmpty()
        )
    )

    val uiState: StateFlow<AddAssignedStandsOrganizationUiState> =
        _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>(
        extraBufferCapacity = 1
    )

    val uiEvent = _uiEvent.asSharedFlow()

    private fun getTodayJalaliDate(): String {
        val today = PersianDate()

        return buildString {
            append(today.shYear)
            append("/")
            append(String.format(Locale.US, "%02d", today.shMonth))
            append("/")
            append(String.format(Locale.US, "%02d", today.shDay))
        }
    }

    private fun initializeDefaultDates() {
        val today = getTodayJalaliDate()

        _uiState.update { state ->
            state.copy(
                assignmentDate = today,
                plannedReturnDate = today
            )
        }
    }

    init {
        initializeDefaultDates()
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isInitialLoading = true)
            }

            try {
                val organizations =
                    organizationDao.observeAll().firstOrNull().orEmpty()

                val standTypes =
                    standTypeDao.observeAll().firstOrNull().orEmpty()

                _uiState.update {
                    it.copy(
                        organizations = organizations,
                        stands = standTypes
                    )
                }

                if (isEditMode && !assignedStandIdArg.isNullOrBlank()) {
                    val detail = assignedStandsRepository
                        .observeByIdWithDetail(assignedStandIdArg)
                        .firstOrNull()

                    detail?.let { item ->
                        val assignedStand = item.assignedStand
                        val convertedAssignmentDate =
                            formatJalaliDate(assignedStand.assignmentDate.orEmpty()).takeIf { !it.isNullOrBlank() }
                                ?: assignedStand.assignmentDate.orEmpty()

                        val convertedPlannedReturnDate =
                            formatJalaliDate(assignedStand.plannedReturnDate.orEmpty()).takeIf { !it.isNullOrBlank() }
                                ?: assignedStand.plannedReturnDate.orEmpty()

                        _uiState.update { state ->
                            state.copy(
                                organizationId = assignedStand.organizationId.orEmpty(),
                                advertisingStandId = assignedStand.standTypeId.orEmpty(),
                                count = assignedStand.count?.toString() ?: "1",
                                assignmentType = assignedStand.assignmentType ?: 1,
                                assignmentMode = assignedStand.assignmentMode ?: 1,
                                assignmentDate = convertedAssignmentDate,
                                plannedReturnDate = convertedPlannedReturnDate,
                                deliveryToVisitorDate = assignedStand.deliveryToVisitorDate.orEmpty(),
                                deliveryToOrgDate = assignedStand.deliveryToOrgDate.orEmpty(),
                                description = assignedStand.description.orEmpty(),
                                status = assignedStand.statusAssign ?: 1
                            )
                        }
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(
                    "AddAssignedStandVM",
                    "Error loading assigned stand data",
                    e
                )

                _uiEvent.emit(
                    UiEvent.ShowError(
                        AppErrorMapper.map(e)
                    )
                )
            } finally {
                _uiState.update {
                    it.copy(isInitialLoading = false)
                }
            }
        }
    }

    fun onOrganizationChanged(id: String) {
        _uiState.update {
            it.copy(organizationId = id)
        }
    }

    fun onStandChanged(id: String) {
        _uiState.update {
            it.copy(advertisingStandId = id)
        }
    }

    fun onCountChanged(count: String) {
        _uiState.update {
            it.copy(count = count)
        }
    }

    fun onAssignmentTypeChanged(type: Int) {
        _uiState.update {
            it.copy(assignmentType = type)
        }
    }

    fun onAssignmentModeChanged(mode: Int) {
        _uiState.update {
            it.copy(assignmentMode = mode)
        }
    }

    fun onAssignmentDateChanged(date: String) {
        _uiState.update {
            it.copy(assignmentDate = date)
        }
    }

    fun onPlannedReturnDateChanged(date: String) {
        _uiState.update {
            it.copy(plannedReturnDate = date)
        }
    }

    fun onDeliveryToVisitorDateChanged(date: String) {
        _uiState.update {
            it.copy(deliveryToVisitorDate = date)
        }
    }

    fun onDeliveryToOrgDateChanged(date: String) {
        _uiState.update {
            it.copy(deliveryToOrgDate = date)
        }
    }

    fun onDescriptionChanged(description: String) {
        _uiState.update {
            it.copy(description = description)
        }
    }

    fun onStatusChanged(status: Int) {
        _uiState.update {
            it.copy(status = status)
        }
    }

    private fun getCurrentIsoUtcTimestamp(): String {
        val dateFormat = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.US
        )

        dateFormat.timeZone = TimeZone.getTimeZone("UTC")

        return dateFormat.format(Date())
    }

    fun submitAssignStand(
        onSuccessCallback: () -> Unit
    ) {
        val state = _uiState.value

        if (state.isLoading) {
            return
        }

        if (!state.isFormValid) {
            viewModelScope.launch {
                _uiEvent.emit(
                    UiEvent.ShowError(
                        UiText.DynamicString(
                            "لطفاً فیلدهای اجباری را تکمیل کنید."
                        )
                    )
                )
            }

            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }

            try {
                val actualVisitorId =
                    mainPreferences.getVisitorId()
                        .ifBlank { null }

                val count =
                    state.count.toIntOrNull() ?: 1

                val standTypeId =    state.advertisingStandId.orEmpty()

                val formattedAssignmentDate =
                    jalaliToGregorian(state.assignmentDate).orEmpty()

                val formattedPlannedReturnDate =
                    jalaliToGregorian(state.plannedReturnDate).orEmpty()

                val formattedDeliveryToVisitorDate =
                    jalaliToGregorian(state.deliveryToVisitorDate)

                val formattedDeliveryToOrgDate =
                    jalaliToGregorian(state.deliveryToOrgDate)

                val currentTimestamp =
                    System.currentTimeMillis()

                val isoTimestamp =
                    getCurrentIsoUtcTimestamp()

                val request = CreateAssignStandRequest(
                    standAssignmentId = assignedStandIdArg,
                    advertisingStandId = state.advertisingStandId,
                    visitorId = actualVisitorId,
                    organizationId = state.organizationId,
                    meetingId = null,
                    count = count,
                    assignmentType = state.assignmentType,
                    assignmentMode = state.assignmentMode,
                    assignmentDate = formattedAssignmentDate,
                    plannedReturnDate = formattedPlannedReturnDate,
                    deliveryToVisitorDate = formattedDeliveryToVisitorDate,
                    deliveryToOrgDate = formattedDeliveryToOrgDate,
                    description = state.description
                        .trim()
                        .ifEmpty { null },
                    status = state.status
                )

                if (isEditMode) {
                    updateAssignedStand(
                        state = state,
                        request = request,
                        targetId = assignedStandIdArg!!,
                        actualVisitorId = actualVisitorId,
                        count = count,
                        standTypeId = state.advertisingStandId,
                        formattedAssignmentDate = formattedAssignmentDate,
                        formattedPlannedReturnDate = formattedPlannedReturnDate,
                        formattedDeliveryToVisitorDate = formattedDeliveryToVisitorDate,
                        formattedDeliveryToOrgDate = formattedDeliveryToOrgDate,
                        currentTimestamp = currentTimestamp,
                        isoTimestamp = isoTimestamp,
                        onSuccessCallback = onSuccessCallback
                    )
                } else {
                    createAssignedStand(
                        state = state,
                        request = request,
                        actualVisitorId = actualVisitorId,
                        count = count,
                        standTypeId =state.advertisingStandId ,
                        formattedAssignmentDate = formattedAssignmentDate,
                        formattedPlannedReturnDate = formattedPlannedReturnDate,
                        formattedDeliveryToVisitorDate = formattedDeliveryToVisitorDate,
                        formattedDeliveryToOrgDate = formattedDeliveryToOrgDate,
                        currentTimestamp = currentTimestamp,
                        isoTimestamp = isoTimestamp,
                        onSuccessCallback = onSuccessCallback
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiEvent.emit(
                    UiEvent.ShowError(
                        AppErrorMapper.map(e)
                    )
                )
            } finally {
                _uiState.update {
                    it.copy(isLoading = false)
                }
            }
        }
    }

    private suspend fun updateAssignedStand(
        state: AddAssignedStandsOrganizationUiState,
        request: CreateAssignStandRequest,
        targetId: String,
        actualVisitorId: String?,
        standTypeId: String,
        count: Int,
        formattedAssignmentDate: String,
        formattedPlannedReturnDate: String,
        formattedDeliveryToVisitorDate: String?,
        formattedDeliveryToOrgDate: String?,
        currentTimestamp: Long,
        isoTimestamp: String,
        onSuccessCallback: () -> Unit
    ) {
        val existingItem =
            assignedStandDao
                .observeByIdWithDetail(targetId)
                .firstOrNull()

        val result =
            assignedStandsRepository.updateAssignStandAndSync(request)

        result.fold(
            onSuccess = { serverMsg ->
                val message =
                    if (!serverMsg.isNullOrBlank()) {
                        UiText.DynamicString(serverMsg)
                    } else {
                        UiText.StringResource(
                            R.string.msg_assigned_stand_updated
                        )
                    }

                _uiEvent.emit(
                    UiEvent.ShowMessage(message)
                )

                delay(400)
                onSuccessCallback()
            },
            onFailure = { error ->
                if (!error.isNetworkError()) {
                    _uiEvent.emit(
                        UiEvent.ShowError(
                            AppErrorMapper.map(error)
                        )
                    )
                } else {
                    val existingEntity =
                        existingItem?.assignedStand

                    val nextSyncStatus =
                        when (existingEntity?.syncStatus) {
                            SyncStatus.SYNCED ->
                                SyncStatus.PENDING_UPDATE

                            SyncStatus.PENDING_INSERT ->
                                SyncStatus.PENDING_INSERT

                            SyncStatus.PENDING_UPDATE ->
                                SyncStatus.PENDING_UPDATE

                            null ->
                                SyncStatus.PENDING_UPDATE

                            else ->
                                SyncStatus.PENDING_UPDATE
                        }

                    val offlineEntity = AssignedStandEntity(
                        assignedStandId = targetId,
                        organizationId = state.organizationId,
                        visitorId = actualVisitorId,
                        standTypeId = standTypeId,
                        count = count,
                        assignmentType = state.assignmentType,
                        assignmentMode = state.assignmentMode,
                        assignmentDate = formattedAssignmentDate,
                        plannedReturnDate = formattedPlannedReturnDate,
                        deliveryToVisitorDate = formattedDeliveryToVisitorDate,
                        deliveryToOrgDate = formattedDeliveryToOrgDate,
                        description = state.description
                            .trim()
                            .ifEmpty { null },
                        statusAssign = state.status,
                        modifiedOn = isoTimestamp,
                        syncStatus = nextSyncStatus,
                        isLocallyModified = true,
                        lastModifiedTimestamp = currentTimestamp
                    )

                    assignedStandsRepository.saveLocal(offlineEntity)

                    _uiEvent.emit(
                        UiEvent.ShowMessage(
                            UiText.StringResource(
                                R.string.msg_changes_saved_offline_synchronized_connection
                            )
                        )
                    )

                    delay(400)
                    onSuccessCallback()
                }
            }
        )
    }

    private suspend fun createAssignedStand(
        state: AddAssignedStandsOrganizationUiState,
        request: CreateAssignStandRequest,
        actualVisitorId: String?,
        count: Int,
        standTypeId: String,
        formattedAssignmentDate: String,
        formattedPlannedReturnDate: String,
        formattedDeliveryToVisitorDate: String?,
        formattedDeliveryToOrgDate: String?,
        currentTimestamp: Long,
        isoTimestamp: String,
        onSuccessCallback: () -> Unit
    ) {
        val result =
            assignedStandsRepository.createAssignStandAndSync(request)

        result.fold(
            onSuccess = { serverMsg ->
                val message =
                    if (!serverMsg.isNullOrBlank()) {
                        UiText.DynamicString(serverMsg)
                    } else {
                        UiText.StringResource(
                            R.string.msg_assigned_stand_create
                        )
                    }

                _uiEvent.emit(
                    UiEvent.ShowMessage(message)
                )

                delay(400)
                onSuccessCallback()
            },
            onFailure = { error ->
                if (!error.isNetworkError()) {
                    _uiEvent.emit(
                        UiEvent.ShowError(
                            AppErrorMapper.map(error)
                        )
                    )
                } else {
                    val localId =
                        UUID.randomUUID().toString()

                    val offlineEntity = AssignedStandEntity(
                        assignedStandId = localId,
                        organizationId = state.organizationId,
                        visitorId = actualVisitorId,
                        standTypeId = standTypeId,
                        count = count,
                        assignmentType = state.assignmentType,
                        assignmentMode = state.assignmentMode,
                        assignmentDate = formattedAssignmentDate,
                        plannedReturnDate = formattedPlannedReturnDate,
                        deliveryToVisitorDate = formattedDeliveryToVisitorDate,
                        deliveryToOrgDate = formattedDeliveryToOrgDate,
                        description = state.description
                            .trim()
                            .ifEmpty { null },
                        statusAssign = state.status,
                        modifiedOn = isoTimestamp,
                        syncStatus = SyncStatus.PENDING_INSERT,
                        isLocallyModified = true,
                        lastModifiedTimestamp = currentTimestamp
                    )

                    assignedStandsRepository.saveLocal(offlineEntity)

                    _uiEvent.emit(
                        UiEvent.ShowMessage(
                            UiText.StringResource(
                                R.string.msg_saved_local_storage_awaiting_synchronization
                            )
                        )
                    )

                    delay(400)
                    onSuccessCallback()
                }
            }
        )
    }
}
