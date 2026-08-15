package ir.kitgroup.partnerManagement.feature.advertising_stand.model


data class AdvertisingStandItem(
    val id: String,
    val name: String,
    val standType: String,
    val installationType: String,
    val displayType: String,
    val description: String
)