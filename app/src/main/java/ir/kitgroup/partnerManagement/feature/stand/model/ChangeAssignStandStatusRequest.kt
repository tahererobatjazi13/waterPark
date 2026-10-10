package ir.kitgroup.partnerManagement.feature.stand.model


data class ChangeAssignStandStatusRequest(
    val standAssignmentId: String,
    val organizationId: String,
    val status: Int,
    val actualReturnDate: String?,
    val deliveryToOrgDate: String?,
)