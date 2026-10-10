package ir.kitgroup.partnerManagement.feature.organization.model


data class CreateOrganizationWarningRequest(
    val organizationWarningId: String?,
    val organizationId: String,
    val warningId: String,
    val visitorId: String,
    val meetingId: String?,
    val warningDate: String,
    val description: String
)