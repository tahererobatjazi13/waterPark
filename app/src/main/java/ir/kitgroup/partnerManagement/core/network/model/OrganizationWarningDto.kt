package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class OrganizationWarningDto(
    val organizationWarningId: String,

    val name: String? = null,

    val organizationId: String? = null,

    val warningId: String? = null,

    val visitorId: String? = null,

    val dateWarning: String? = null,

    val description: String? = null,

    val score: Int? = null,

    val meetingId: String? = null
)