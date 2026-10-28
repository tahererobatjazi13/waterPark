package ir.kitgroup.partnerManagement.feature.dashboard.model

import ir.kitgroup.partnerManagement.core.database.dao.AssignedStandDao
import ir.kitgroup.partnerManagement.core.database.entity.AssignedStandEntity
import ir.kitgroup.partnerManagement.core.network.model.AssignStandChangeStatusDto
import ir.kitgroup.partnerManagement.core.network.model.AssignedStandDto
import ir.kitgroup.partnerManagement.core.network.model.ContractDto
import ir.kitgroup.partnerManagement.core.network.model.ContractTerminationDto
import ir.kitgroup.partnerManagement.core.network.model.MeetingChangeStatusDto
import ir.kitgroup.partnerManagement.core.network.model.MeetingDto
import ir.kitgroup.partnerManagement.core.network.model.OrganizationDto
import ir.kitgroup.partnerManagement.core.network.model.OrganizationPersonDto
import ir.kitgroup.partnerManagement.core.network.model.OrganizationChangeStatusDto
import ir.kitgroup.partnerManagement.core.network.model.WarningDto
import ir.kitgroup.partnerManagement.feature.stand.model.CreateAssignStandRequest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SyncRequestDto(
    @SerialName("statusSync")
    val statusSync: Boolean = true,

    @SerialName("SystemUserId")
    val systemUserId: String,

    @SerialName("VisitorId")
    val visitorId: String,

    @SerialName("roleCode")
    val roleCode: Int = 0,

    @SerialName("pendingOrganizations")
    val pendingOrganizations: List<OrganizationDto> = emptyList(),

    @SerialName("pendingOrgStatusChanges")
    val pendingOrgStatusChanges: List<OrganizationChangeStatusDto> = emptyList(),

    @SerialName("pendingPersons")
    val pendingPersons: List<OrganizationPersonDto> = emptyList(),

    @SerialName("pendingContracts")
    val pendingContracts: List<ContractDto> = emptyList(),

    @SerialName("pendingContractTerminations")
    val pendingContractTerminations: List<ContractTerminationDto> = emptyList(),


    @SerialName("pendingMeetings")
    val pendingMeetings: List<MeetingDto> = emptyList(),

    @SerialName("pendingMeetingStatusChanges")
    val pendingMeetingStatusChanges: List<MeetingChangeStatusDto> = emptyList(),

    @SerialName("pendingWarnings")
    val pendingWarnings: List<WarningDto> = emptyList(),

    @SerialName("pendingUpdateStands")
    val pendingAssignedStands: List<CreateAssignStandRequest> = emptyList(),

    @SerialName("pendingStandStatusChanges")
    val pendingStandStatusChanges: List<AssignStandChangeStatusDto> = emptyList()
)
