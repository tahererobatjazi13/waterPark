package ir.kitgroup.partnerManagement.feature.organization.model

import ir.kitgroup.partnerManagement.core.ui.util.Status

data class VisitorOrganization(
    val id: Int,
    val name: String,
    val startDate: String,
    val endDate: String,
    val status: Status,
)
