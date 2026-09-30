package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class WarningTypeDto(
    val warningTypeId: String,

    val title: String?,

    val code: String?,

    val score: Int?,

    val stateCode: Int?

)