package ir.kitgroup.partnerManagement.feature.advertising_stand.model

import ir.kitgroup.partnerManagement.core.ui.util.Status

data class AdvertisingStandAssignmentDetail(
    val id: String,
    val visitorName: String,
    val organizationName: String,
    val assignmentType: String,
    val assignmentMode: String,
    val allocatedDate: String,
    val status: Status,
    val items: List<AllocatedStandItem>,
    val description: String? = null
) {
    val totalCount: Int
        get() = items.sumOf { it.count }
}

data class AllocatedStandItem(
    val id: String,
    val title: String,
    val type: String,
    val iconName: String,
    val count: Int,
    val code: String? = null,
    val description: String? = null
)
