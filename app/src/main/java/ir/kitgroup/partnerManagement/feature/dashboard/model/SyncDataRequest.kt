package ir.kitgroup.partnerManagement.feature.dashboard.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SyncDataRequest(
    @SerialName("statusSync")
    val statusSync: Boolean = true,

    @SerialName("SystemUserId")
    val systemUserId: String,

    @SerialName("VisitorId")
    val visitorId: String,

    @SerialName("roleCode")
    val roleCode: Int = 0,

    @SerialName("pendingOrganizations")
    val pendingOrganizations: List<String> = emptyList(),

    @SerialName("pendingOrgStatusChanges")
    val pendingOrgStatusChanges: List<String> = emptyList(),

    @SerialName("pendingPersons")
    val pendingPersons: List<String> = emptyList(),

    @SerialName("pendingContracts")
    val pendingContracts: List<String> = emptyList(),

    @SerialName("pendingContractTerminations")
    val pendingContractTerminations: List<String> = emptyList(),

    @SerialName("pendingMeetings")
    val pendingMeetings: List<String> = emptyList(),

    @SerialName("pendingMeetingStatusChanges")
    val pendingMeetingStatusChanges: List<String> = emptyList(),

    @SerialName("pendingWarnings")
    val pendingWarnings: List<String> = emptyList(),

    @SerialName("pendingUpdateStands")
    val pendingUpdateStands: List<String> = emptyList(),

    @SerialName("pendingStandStatusChanges")
    val pendingStandStatusChanges: List<String> = emptyList()
)
