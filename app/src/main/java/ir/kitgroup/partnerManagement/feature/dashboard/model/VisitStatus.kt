package ir.kitgroup.partnerManagement.feature.dashboard.model

import ir.kitgroup.partnerManagement.core.ui.util.Status
import ir.kitgroup.partnerManagement.core.ui.util.VisitType

data class VisitItem(
    val id: Int,
    val time: String,
    val organizationName: String,
    val city: String,
    val district: String,
    val rating: Int,
    val status: Status,
    val visitType: VisitType,
    val visitorName: String? = null
)

