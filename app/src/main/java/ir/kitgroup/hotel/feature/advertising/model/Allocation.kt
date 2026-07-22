package ir.kitgroup.hotel.feature.advertising.model

import ir.kitgroup.hotel.core.ui.util.AllocationStatus

data class Allocation(
    val id: String,
    val visitorName: String,
    val hotelName: String,
    val itemType: String,
    val itemIconName: String,
    val count: Int,
    val allocatedDate: String,
    val status: AllocationStatus
)