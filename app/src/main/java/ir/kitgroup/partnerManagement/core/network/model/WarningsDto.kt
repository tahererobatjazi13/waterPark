package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class WarningsDto(
    val warningsId: String,

    val name: String? = null,

    val score: Int? = null,

    val recreationCenterId: String? = null,

    val code: String? = null
)