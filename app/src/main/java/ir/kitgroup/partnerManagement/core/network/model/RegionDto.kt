package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class RegionDto(

    val regionId: String,

    val name: String?,

    val cityId: String?,

    val stateCode: Int?,

    )