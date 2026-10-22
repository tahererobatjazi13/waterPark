package ir.kitgroup.partnerManagement.feature.organization.ui.detail

import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.dao.VisitSubjectDao
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationPersonEntity
import ir.kitgroup.partnerManagement.core.database.entity.VisitSubjectEntity
import ir.kitgroup.partnerManagement.core.database.model.AssignedStandWithDetail
import ir.kitgroup.partnerManagement.core.database.model.MeetingWithDetail
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import ir.kitgroup.partnerManagement.core.repository.AssignedSerialsRepository
import ir.kitgroup.partnerManagement.core.repository.AssignedStandsRepository
import ir.kitgroup.partnerManagement.core.repository.ContractRepository
import ir.kitgroup.partnerManagement.core.repository.MeetingRepository
import ir.kitgroup.partnerManagement.core.repository.OrganizationPersonRepository
import ir.kitgroup.partnerManagement.core.repository.OrganizationRepository
import ir.kitgroup.partnerManagement.core.repository.VisitorRepository
import ir.kitgroup.partnerManagement.core.repository.WarningRepository
import ir.kitgroup.partnerManagement.core.ui.util.AppErrorMapper
import ir.kitgroup.partnerManagement.core.ui.util.DataState
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationStatus
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import ir.kitgroup.partnerManagement.core.ui.util.datastore.MainPreferences
import ir.kitgroup.partnerManagement.feature.dashboard.repository.SyncRepository
import ir.kitgroup.partnerManagement.feature.organization.model.ChangeStatusOrganizationRequest
import ir.kitgroup.partnerManagement.feature.organization.ui.add.model.AddOrganizationPersonRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException


@HiltViewModel
class OrganizationDetailViewModel @Inject constructor(
    private val organizationRepository: OrganizationRepository,
    private val meetingRepository: MeetingRepository,
    private val organizationPersonRepository: OrganizationPersonRepository,
    private val contractRepository: ContractRepository,
    private val warningRepository: WarningRepository,
    private val assignedStandsRepository: AssignedStandsRepository,
    private val assignedSerialsRepository: AssignedSerialsRepository,
    private val visitorRepository: VisitorRepository,
    private val visitSubjectDao: VisitSubjectDao,
    private val syncRepository: SyncRepository,
    private val mainPreferences: MainPreferences,
    savedStateHandle: SavedStateHandle,

    ) : ViewModel() {
    val organizationId: String = checkNotNull(savedStateHandle["organizationId"])

    val baseUrlFlow: StateFlow<String> = mainPreferences.baseUrlFlow
        .map { it?.trim()?.trimEnd('/').orEmpty() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ""
        )

    private val _statusUpdateState = MutableStateFlow<DataState<Unit>>(DataState.Idle)
    val statusUpdateState = _statusUpdateState.asStateFlow()

    private val _statusUiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val statusUiEvent = _statusUiEvent.asSharedFlow()

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

    private val _meetings = MutableStateFlow<List<MeetingWithDetail>>(emptyList())
    val meetings: StateFlow<List<MeetingWithDetail>> =
        _meetings.asStateFlow()


    fun observeOrganizationMeetings(organizationId: String) {
        viewModelScope.launch {
            meetingRepository
                .observeMeetingsByOrganization(organizationId)
                .collect { meetings ->
                    _meetings.value = meetings
                }
        }
    }

    private val _persons =
        MutableStateFlow<List<OrganizationPersonEntity>>(emptyList())

    val persons: StateFlow<List<OrganizationPersonEntity>> =
        _persons.asStateFlow()

    fun observeOrganizationPersons(
        organizationId: String
    ) {
        viewModelScope.launch {
            organizationPersonRepository
                .observeByOrganization(organizationId)
                .collect { persons ->
                    _persons.value = persons
                }
        }
    }

    private val _contracts = MutableStateFlow<List<ContractEntity>>(emptyList())
    val contracts: StateFlow<List<ContractEntity>> = _contracts.asStateFlow()

    fun observeOrganizationContracts(
        organizationId: String
    ) {
        viewModelScope.launch {
            contractRepository
                .observeByOrganization(organizationId)
                .collect { contracts ->
                    _contracts.value = contracts
                }
        }
    }

    private val _assignedStands = MutableStateFlow<List<AssignedStandWithDetail>>(emptyList())
    val assignedStands: StateFlow<List<AssignedStandWithDetail>> =
        _assignedStands.asStateFlow()

    fun observeOrganizationAssignedStands(
        organizationId: String
    ) {
        viewModelScope.launch {
            assignedStandsRepository
                .observeByOrganizationWithDetail(organizationId)
                .collect { assignedStands ->
                    _assignedStands.value = assignedStands
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

    fun resetStatusUpdateState() {
        _statusUpdateState.value = DataState.Idle
    }

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
                    onFailure = {
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
                            _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(dbError)))
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

    /*  fun changeStatusOrganization(
          organizationId: String,
          status: OrganizationStatus,
          deactiveDate: String?,
          reason: String?,
          visitDate: String?,
          subjectVisitId: String?
      ) {
          viewModelScope.launch {
              _statusUpdateState.value = DataState.Loading

              try {
                  val request = ChangeStatusOrganizationRequest(
                      organizationId = organizationId,
                      statusOrg = status.id,
                      deactiveDate = deactiveDate,
                      reason = reason,
                      visitDate = visitDate,
                      subjectVisitId = subjectVisitId
                  )
                  Log.d("ChangeOrganizationStatusRequest", request.toString())

                  val result = organizationRepository.changeStatusOrganization(request)

                  result.onSuccess {
                      _statusUpdateState.value = DataState.Success(Unit)
                      _statusUiEvent.emit(
                          UiEvent.ShowMessage(
                              UiText.DynamicString("وضعیت سازمان با موفقیت به‌روزرسانی شد.")
                          )
                      )
                      Log.d("ChangeOrganizationStatusRequest1", "ok")

                  }.onFailure { error ->
                      val message = error.localizedMessage ?: "خطا در تغییر وضعیت سازمان"
                      _statusUpdateState.value = DataState.Error(UiText.DynamicString(message))
                      _statusUiEvent.emit(
                          UiEvent.ShowError(UiText.DynamicString(message))
                      )
                      Log.d("ChangeOrganizationStatusRequest2", "ok")

                  }
              } catch (e: Exception) {

                  // در صورتی که تابع ریپازیتوری اکولایز نشده باشد یا Exception پرتاب کند
                  val message = e.localizedMessage ?: "خطای ناشناخته در برقراری ارتباط با سرور"
                  _statusUpdateState.value = DataState.Error(UiText.DynamicString(message))
                  _statusUiEvent.emit(
                      UiEvent.ShowError(UiText.DynamicString(message))
                  )
              }
          }
      }*/


    private val _addPersonState =
        MutableStateFlow<DataState<Unit>>(DataState.Idle)

    val addPersonState: StateFlow<DataState<Unit>> =
        _addPersonState.asStateFlow()


    fun addOrganizationPerson(
        organizationId: String,
        fullName: String,
        role: Int,
        mobile: String?,
        phone: String?,
        gender: Int?,
        description: String?,
        isCommissionEligible: Boolean
    ) {
        viewModelScope.launch {

            _addPersonState.value = DataState.Loading

            val request = AddOrganizationPersonRequest(
                organizationId = organizationId,
                fullName = fullName.trim(),
                role = role,
                mobile = mobile
                    ?.trim()
                    ?.ifBlank { null },
                phone = phone
                    ?.trim()
                    ?.ifBlank { null },
                gender = gender,
                description = description
                    ?.trim()
                    ?.ifBlank { null },
                isCommissionEligible = isCommissionEligible
            )

            Log.d(
                "AddOrganizationPerson",
                "request = $request"
            )

            val result =
                organizationRepository.addOrganizationPerson(request)

            result
                .onSuccess { response ->
                    syncRepository.syncData()
                    Log.d(
                        "AddOrganizationPerson",
                        "success = ${response.success}, " +
                                "message = ${response.message}, " +
                                "data = ${response.data}"
                    )

                    _addPersonState.value =
                        DataState.Success(Unit)
                }
                .onFailure { error ->

                    val message =
                        error.message
                            ?: "خطا در ثبت شخص"

                    Log.e(
                        "AddOrganizationPerson",
                        "error = $message"
                    )

                    _addPersonState.value =
                        DataState.Error(
                            UiText.DynamicString(message)
                        )
                }
        }
    }


    fun resetAddPersonState() {
        _addPersonState.value = DataState.Idle
    }


    private val _uiState = MutableStateFlow(AttachmentsFileUiState())
    val uiState: StateFlow<AttachmentsFileUiState> = _uiState.asStateFlow()

    init {
        loadAttachments()
    }

    fun loadAttachments() {
        viewModelScope.launch {
            _uiState.update { it.copy(isImagesLoading = true) }
            val result = organizationRepository.getAttachmentsByOrgId(organizationId)

            result.onSuccess { dtoList ->
                result.onSuccess { attachments ->
                    organizationRepository.saveAttachmentsToDb(
                        orgId = organizationId,
                        attachments = attachments,
                        baseUrl = baseUrlFlow.value
                    )
                }


                _uiState.update {
                    it.copy(
                        attachments = dtoList,
                        isImagesLoading = false
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isImagesLoading = false,
                        errorMessage = error.localizedMessage
                    )
                }
            }
        }
    }
}