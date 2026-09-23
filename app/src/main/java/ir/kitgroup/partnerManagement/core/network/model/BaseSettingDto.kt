package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class BaseSettingDto(

    val baseSettingId: String,

    val name: String? = null,

    val key: String? = null,

    val value: String? = null
)