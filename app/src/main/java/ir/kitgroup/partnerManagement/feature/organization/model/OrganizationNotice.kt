package ir.kitgroup.partnerManagement.feature.organization.model

data class OrganizationNotice(
    val id: Int,
    val type: String,
    val score: Int,
    val description: String,
    val createdAt: String
)
