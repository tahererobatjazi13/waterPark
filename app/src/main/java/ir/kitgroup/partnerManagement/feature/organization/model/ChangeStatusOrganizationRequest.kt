package ir.kitgroup.partnerManagement.feature.organization.model


data class ChangeStatusOrganizationRequest(
    val organizationId: String,
    val statusOrg: Int,
    val deactiveDate: String?,
    val reason: String?,
    val visitDate: String?,
    val subjectVisitId: String?,
)