package ir.kitgroup.partnerManagement.feature.organization.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.entity.AssignedSerialEntity
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationPersonEntity
import ir.kitgroup.partnerManagement.core.database.entity.WarningEntity
import ir.kitgroup.partnerManagement.core.database.model.AssignedSerialWithDetail
import ir.kitgroup.partnerManagement.core.database.model.AssignedStandWithDetail
import ir.kitgroup.partnerManagement.core.database.model.MeetingWithDetail
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import ir.kitgroup.partnerManagement.core.database.model.WarningWithDetail
import ir.kitgroup.partnerManagement.core.repository.AssignedSerialsRepository
import ir.kitgroup.partnerManagement.core.repository.AssignedStandsRepository
import ir.kitgroup.partnerManagement.core.repository.ContractRepository
import ir.kitgroup.partnerManagement.core.repository.MeetingRepository
import ir.kitgroup.partnerManagement.core.repository.OrganizationPersonRepository
import ir.kitgroup.partnerManagement.core.repository.OrganizationRepository
import ir.kitgroup.partnerManagement.core.repository.WarningRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val assignedSerialsRepository: AssignedSerialsRepository
) : ViewModel() {

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
}