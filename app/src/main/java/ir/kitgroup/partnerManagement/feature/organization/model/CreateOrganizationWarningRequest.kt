package ir.kitgroup.partnerManagement.feature.organization.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateOrganizationWarningRequest(
    val organizationWarningId: String?,
    val organizationId: String,
    val warningId: String,
    val visitorId: String,
    val meetingId: String?,
    val warningDate: String,
    val description: String
)