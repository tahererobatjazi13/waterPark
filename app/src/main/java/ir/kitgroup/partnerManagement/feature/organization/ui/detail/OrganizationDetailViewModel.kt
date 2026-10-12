package ir.kitgroup.partnerManagement.feature.organization.ui.detail

import android.content.ContentValues.TAG
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.dao.VisitSubjectDao
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationPersonEntity
import ir.kitgroup.partnerManagement.core.database.entity.VisitSubjectEntity
import ir.kitgroup.partnerManagement.core.database.entity.VisitorOrganizationEntity
import ir.kitgroup.partnerManagement.core.database.entity.WarningEntity
import ir.kitgroup.partnerManagement.core.database.entity.WarningTypeEntity
import ir.kitgroup.partnerManagement.core.database.model.AssignedSerialWithDetail
import ir.kitgroup.partnerManagement.core.database.model.AssignedStandWithDetail
import ir.kitgroup.partnerManagement.core.database.model.MeetingWithDetail
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import ir.kitgroup.partnerManagement.core.database.model.WarningWithDetail
import ir.kitgroup.partnerManagement.core.network.apiService.ApiService
import ir.kitgroup.partnerManagement.core.repository.AssignedSerialsRepository
import ir.kitgroup.partnerManagement.core.repository.AssignedStandsRepository
import ir.kitgroup.partnerManagement.core.repository.ContractRepository
import ir.kitgroup.partnerManagement.core.repository.MeetingRepository
import ir.kitgroup.partnerManagement.core.repository.OrganizationPersonRepository
import ir.kitgroup.partnerManagement.core.repository.OrganizationRepository
import ir.kitgroup.partnerManagement.core.repository.VisitorRepository
import ir.kitgroup.partnerManagement.core.repository.WarningRepository
import ir.kitgroup.partnerManagement.core.ui.util.DataState
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationStatus
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import ir.kitgroup.partnerManagement.core.ui.util.datastore.MainPreferences
import ir.kitgroup.partnerManagement.feature.organization.model.ChangeOrganizationStatusRequest
import ir.kitgroup.partnerManagement.feature.organization.model.CreateOrganizationWarningRequest
import ir.kitgroup.partnerManagement.feature.organization.ui.add.model.AddOrganizationPersonRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


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
    private val apiService: ApiService,
    private val mainPreferences: MainPreferences

) : ViewModel() {

    private val _statusUpdateState = MutableStateFlow<DataState<Unit>>(DataState.Idle)
    val statusUpdateState = _statusUpdateState.asStateFlow()

    private val _statusUiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val statusUiEvent = _statusUiEvent.asSharedFlow()

    private val _activeVisitorName = MutableStateFlow<String?>(null)
    val activeVisitorName: StateFlow<String?> = _activeVisitorName.asStateFlow()

    private val _activeContractName= MutableStateFlow<String?>(null)
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
                        visitorRepository.getAllVisitors().collect { visitors ->
                            _activeVisitorName.value =
                                visitors.firstOrNull { it.visitorId == activeVisitorId }?.name
                        }
                    } else {
                        _activeVisitorName.value = null
                    }

                    if (!activeContractId.isNullOrBlank()) {
                        contractRepository.observeAllContracts().collect { contracts ->
                            _activeContractName.value =
                                contracts.firstOrNull { it.contractId == activeContractId }?.title
                        }
                    } else {
                        _activeVisitorName.value = null
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


    private val _warnings = MutableStateFlow<List<WarningWithDetail>>(emptyList())
    val warnings: StateFlow<List<WarningWithDetail>> =
        _warnings.asStateFlow()

    fun observeOrganizationWarnings(
        organizationId: String
    ) {
        viewModelScope.launch {
            warningRepository
                .observeByOrganization(organizationId)
                .collect { warnings ->
                    _warnings.value = warnings
                }
        }
    }

    fun deleteWarning(warningId: String) {
        viewModelScope.launch {
            warningRepository.deleteById(warningId)
        }
    }

    fun addWarning(
        organizationId: String,
        organizationName: String?,
        warningTypeId: String?,
        warningTypeName: String?,
        description: String
    ) {
        viewModelScope.launch {

            val warning = WarningEntity(
                warningId = UUID.randomUUID().toString(),
                organizationId = organizationId,
                organizationName = organizationName,
                warningTypeId = warningTypeId,
                warningTypeName = warningTypeName,
                description = description,
                warningDate = null,
                score = null,
                stateCode = 1
            )

            warningRepository.upsertAll(
                listOf(warning)
            )
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

    private val _assignedSerials = MutableStateFlow<List<AssignedSerialWithDetail>>(emptyList())
    val assignedSerials: StateFlow<List<AssignedSerialWithDetail>> =
        _assignedSerials.asStateFlow()

    fun observeOrganizationAssignedSerials(
        organizationId: String
    ) {
        viewModelScope.launch {
            assignedSerialsRepository
                .observeByOrganization(organizationId)
                .collect { assignedSerials ->
                    _assignedSerials.value = assignedSerials
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

    fun updateOrganizationStatus(
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
                val request = ChangeOrganizationStatusRequest(
                    organizationId = organizationId,
                    statusOrg = status.id,
                    deactiveDate = deactiveDate,
                    reason = reason,
                    visitDate = visitDate,
                    subjectVisitId = subjectVisitId
                )
                Log.d("ChangeOrganizationStatusRequest", request.toString())

                val result = organizationRepository.changeOrganizationStatus(request)

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
    }


    // درون OrganizationDetailViewModel:
    private val _visitorsState =
        MutableStateFlow<DataState<List<VisitorOrganizationEntity>>>(DataState.Idle)
    val visitorsState: StateFlow<DataState<List<VisitorOrganizationEntity>>> =
        _visitorsState.asStateFlow()

    fun fetchOrganizationVisitors(organizationId: String) {
        viewModelScope.launch {
            _visitorsState.value = DataState.Loading

            organizationRepository.getOrganizationVisitors(organizationId)
                .onSuccess { visitors ->
                    _visitorsState.value = DataState.Success(visitors)
                }
                .onFailure { error ->
                    _visitorsState.value = DataState.Error(
                        UiText.DynamicString(
                            error.localizedMessage ?: "خطا در دریافت ویزیتورهای سازمان"
                        )
                    )
                }
        }
    }


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


    private val _addWarningState = MutableStateFlow<DataState<Unit>>(DataState.Idle)
    val addWarningState = _addWarningState.asStateFlow()

    /*    private val _addWarningState =
            MutableStateFlow(AddOrganizationWarningState())

        val addWarningState: StateFlow<AddOrganizationWarningState> =
            _addWarningState.asStateFlow()*/

    fun observeWarningTypes(): Flow<List<WarningTypeEntity>> =
        warningRepository.observeWarningTypes()


    fun createOrganizationWarning(
        organizationId: String,
        warningId: String,
        visitorId: String,
        meetingId: String? = null,
        warningDate: String,
        description: String,
        isEdit: Boolean = false,
        warningRecordId: String? = null
    ) {
        viewModelScope.launch {
            _addWarningState.value = DataState.Loading

            try {
                val actualVisitorId = visitorId.ifBlank {
                    mainPreferences.getVisitorId()
                }
                val request = CreateOrganizationWarningRequest(
                    organizationWarningId = warningRecordId,
                    organizationId = organizationId,
                    warningId = warningId,
                    visitorId = actualVisitorId,
                    meetingId = meetingId,
                    warningDate = warningDate,
                    description = description.trim()
                )

                Log.d("ChangeOrganizationStatusRequest", request.toString())
                val isEditMode: Boolean =
                    !warningRecordId.isNullOrBlank()

                val result = if (isEditMode) {
                    warningRepository.updateOrganizationWarning(request)
                } else {
                    warningRepository.createOrganizationWarning(request)
                }

                result.onSuccess {
                    _addWarningState.value = DataState.Success(Unit)
                    _statusUiEvent.emit(
                        UiEvent.ShowMessage(
                            UiText.DynamicString("تذکر با موفقیت ثبت شد.")
                        )
                    )
                    Log.d("createOrganizationWarning1", "ok")

                }.onFailure { error ->
                    val message = error.localizedMessage ?: "خطا در ثبت تذکر"
                    _addWarningState.value = DataState.Error(UiText.DynamicString(message))
                    _statusUiEvent.emit(
                        UiEvent.ShowError(UiText.DynamicString(message))
                    )
                    Log.d("createOrganizationWarning2", "ok")

                }
            } catch (e: Exception) {

                // در صورتی که تابع ریپازیتوری اکولایز نشده باشد یا Exception پرتاب کند
                val message = e.localizedMessage ?: "خطای ناشناخته در برقراری ارتباط با سرور"
                _addWarningState.value = DataState.Error(UiText.DynamicString(message))
                _statusUiEvent.emit(
                    UiEvent.ShowError(UiText.DynamicString(message))
                )
            }
        }
    }


    /*
        fun resetAddWarningState() {
            _addWarningState.value = AddOrganizationWarningState()
        }
    */


    fun resetAddWarningState() {
        _addWarningState.value = DataState.Idle
    }
}