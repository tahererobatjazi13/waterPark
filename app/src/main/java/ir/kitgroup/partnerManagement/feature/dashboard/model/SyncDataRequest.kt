package ir.kitgroup.partnerManagement.feature.dashboard.model


data class SyncDataRequest(
    val systemUserId: String,
    val visitorId: String,
    val roleCode: Int = 0,
    val pendingOrganizations: List<String> = emptyList(),
    val pendingPersons: List<String> = emptyList(),
    val pendingMeetings: List<String> = emptyList(),
    val pendingWarnings: List<String> = emptyList(),
    val pendingAssignedStands: List<String> = emptyList()
)
