package ir.kitgroup.hotel.feature.advertising.model
data class AdvertisingItem(
    val id: String,
    val title: String,
    val type: String,
    val stock: Int,
    val isActive: Boolean,
    val iconName: String = "default"
)