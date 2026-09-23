package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class MeetingDto(

    val meetingId: String,

    val name: String? = null,

    val description: String? = null,

    val organizationId: String? = null,

    val status: Int? = null,

    val type: Int? = null,

    val visitDate: String? = null,

    val visitorId: String? = null,

    val visitRealDate: String? = null,

    val subjectVisitId: String? = null,

    val visitTime: String? = null,

    val personId: String? = null,

    val longitude: Double? = null,

    val latitude: Double? = null
)