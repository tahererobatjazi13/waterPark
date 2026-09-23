package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class VisitorOrganizationDto(
    val visitorOrganizationId: String,

    val name: String? = null,

    val dateEnd: String? = null,

    val dateStart: String? = null,

    val organizationId: String? = null,

    val visitorId: String? = null,

    val statusRelation: Int? = null
)