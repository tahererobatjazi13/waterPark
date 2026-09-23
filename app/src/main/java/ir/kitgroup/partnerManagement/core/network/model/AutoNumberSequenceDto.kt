package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class AutoNumberSequenceDto(

    val autoNumberSequenceId: String,

    val name: String? = null,

    val currentNumber: Int? = null,

    val prefix: String? = null,

    val format: String? = null,

    val step: Int? = null,

    val recreationCenterId: String? = null
)