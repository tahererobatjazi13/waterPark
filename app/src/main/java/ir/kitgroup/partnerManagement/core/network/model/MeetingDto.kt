package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class MeetingDto(

    val meetingId: String,

    val organizationId: String,

    val organizationName: String,

    val visitorId: String,

    val visitorName: String?,

    val subjectVisitId: String?,

    val subjectVisitName: String?,

    val organizationPersonId: String?,

    val organizationPersonName: String?,

    val description: String?,

    val visitDate: String?,

    val visitRealDate: String?,

    val visitTime: String?,

    val status: Int?,

    val statusName: String?,

    val type: Int?,

    val typeName: String?,

    val longitude: String?,

    val latitude: String?,

    val stateCode: Int?

)