package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class WarningDto(

    val warningId: String,

    val organizationId: String?,

    val organizationName: String?,

    val meetingId: String?,

    val visitorId: String?,

    val visitorName: String?,

    val warningTypeId: String?,

    val warningTypeName: String?,

    val score: Int?,

    val description: String?,

    val warningDate: String?,

    val stateCode: Int?
)