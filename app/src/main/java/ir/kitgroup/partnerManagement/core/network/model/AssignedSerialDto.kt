package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class AssignedSerialDto(

    val serialAssignmentId: String,

    val name: String?,

    val organizationId: String?,

    val organizationName: String?,

    val offerId: String?,

    val offerName: String?,

    val contractId: String?,

    val serialPrefix: String?,

    val fromSerial: Int?,

    val toSerial: Int?,

    val count: Int?,

    val assignmentDate: String?,

    val contractOfferStatus: Int?,

    val contractOfferStatusName: String?,

    val meetingId: String?,

    val lastSerialUsed: Int?,

    val remainingSerialCount: Int?,

    val stateCode: Int?

)
