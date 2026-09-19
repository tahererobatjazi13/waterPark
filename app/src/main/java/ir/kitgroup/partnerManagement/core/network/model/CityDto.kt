package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class CityDto(
    val cityId: Int,
    val name: String
)

