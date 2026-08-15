package ir.kitgroup.partnerManagement.feature.advertising_stand.model

import ir.kitgroup.partnerManagement.core.ui.util.AllocationStatus

data class AdvertisingStandAssignment(
    val id: String,
    val visitorName: String,
    val organizationName: String,
    val itemType: String,
    val itemIconName: String,
    val count: Int,
    val allocatedDate: String,
    val status: AllocationStatus
)

