package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class PersonDto(
    val personId: String,

    val name: String? = null,

    val description: String? = null,

    val gender: Int? = null,

    val mobile: String? = null,

    val phone1: String? = null,

    val status: Int? = null
)