package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class CityDto(

    val cityId: String,

    val name: String? = null
)

