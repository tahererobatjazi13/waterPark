package ir.kitgroup.partnerManagement.feature.organization.model

data class VisitorOrganization(
    val id: Long = System.currentTimeMillis(),
    val name: String,
    val startDate: String,
    val endDate: String,
)
