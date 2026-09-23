package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class AdvertisingStandDto(

    val advertisingStandId: String,

    val name: String? = null,

    val code: String? = null,

    val description: String? = null,

    val displayType: Int? = null,

    val installationType: Int? = null,

    val recreationCenterId: String? = null,

    val standType: Int? = null
)