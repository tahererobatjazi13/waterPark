package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class StandAssignmentDto(
    val standAssignmentId: String,

    val name: String? = null,

    val actualReturnDate: String? = null,

    val advertisingStandId: String? = null,

    val assignmentDate: String? = null,

    val assignmentMode: Int? = null,

    val assignmentType: Int? = null,

    val deliveryToOrgDate: String? = null,

    val deliveryToVisitorDate: String? = null,

    val organizationId: String? = null,

    val plannedReturnDate: String? = null,

    val status: Int? = null,

    val visitorId: String? = null,

    val count: Int? = null,

    val description: String? = null,

    val meetingId: String? = null
)