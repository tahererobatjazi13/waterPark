package ir.kitgroup.partnerManagement.feature.meeting.model

data class CreateMeetingRequest(
    val meetingId: String? = null,
    val organizationId: String,
    val organizationPersonId: String? = null,
    val visitorId: String? = null,
    val subjectVisitId: String,
    val description: String? = null,
    val visitDate: String? = null,
    val visitRealDate: String? = null,
    val visitTime: String? = null,
    val status: Int,
    val type: Int,
    val latitude: Double? = null,
    val longitude: Double? = null
)