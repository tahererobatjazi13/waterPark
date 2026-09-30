package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class AssignedStandDto(

    val assignedStandId: String,

    val organizationId: String?,

    val organizationName: String?,

    val standTypeId: String?,

    val standTypeName: String?,

    val visitorId: String?,

    val visitorName: String?,

    val description: String?,

    val assignmentDate: String?,

    val plannedReturnDate: String?,

    val actualReturnDate: String?,

    val deliveryToOrgDate: String?,

    val deliveryToVisitorDate: String?,

    val count: Int?,

    val assignmentType: Int?,

    val assignmentTypeName: String?,

    val assignmentMode: Int?,

    val assignmentModeName: String?,

    val meetingId: String?,

    val statusAssign: Int?,

    val statusAssignName: String?,

    val stateCode: Int?,
)