package ir.kitgroup.partnerManagement.feature.meeting.model

data class ChangeStatusMeetingRequest(
    val meetingId: String? = null,
    val status: Int,
    val visitRealDate: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val description: String? = null,
)