package ir.kitgroup.partnerManagement.feature.stand.model

data class CreateAssignStandRequest(
    val standAssignmentId: String? = null,
    val advertisingStandId: String,
    val visitorId: String? = null,
    val organizationId: String? = null,
    val count: Int,
    val assignmentType: Int,
    val assignmentMode: Int,
    val plannedReturnDate: String,
    val actualReturnDate: String,
    val deliveryToOrgDate: String,
    val description: String? = null,
    val roleCode: Int,
    val status: Int,

    )
