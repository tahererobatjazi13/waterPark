package ir.kitgroup.partnerManagement.core.network.model


import kotlinx.serialization.Serializable

@Serializable
data class RegionDto(
    val regionId: Int,
    val name: String,
    val cityId: Int,
    val receationCenterId: Int
)
