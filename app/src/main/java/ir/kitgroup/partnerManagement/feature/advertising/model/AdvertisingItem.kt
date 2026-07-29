package ir.kitgroup.partnerManagement.feature.advertising.model

import ir.kitgroup.partnerManagement.core.ui.util.AllocationStatus

data class AdvertisingItem(
    val id: String,
    val title: String,
    val type: String,
    val stock: Int,
    val isActive: Boolean,
    val iconName: String = "default",
    val status: AllocationStatus
)