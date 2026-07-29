package ir.kitgroup.partnerManagement.feature.advertising.model

import ir.kitgroup.partnerManagement.core.ui.util.AllocationStatus

data class Allocation(
    val id: String,
    val visitorName: String,
    val collectionName: String,
    val itemType: String,
    val itemIconName: String,
    val count: Int,
    val allocatedDate: String,
    val status: AllocationStatus
)

