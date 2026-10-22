package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class VisitorOrganizationDto(
    val orgVisitorId: String,
    val visitorId: String?,
    val organizationId: String?,
    val statusRelation: Int?,
    val statusRelationName: String?,
    val dateStart: String?,
    val dateEnd: String?,
    val name: String?,
    val status: Int?
)