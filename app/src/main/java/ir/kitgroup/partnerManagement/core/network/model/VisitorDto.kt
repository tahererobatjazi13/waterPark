package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class VisitorDto(
    val visitorId: String,

    val name: String? = null,

    val code: String? = null,

    val mobile: String? = null,

    val workType: Int? = null,

    val workTypeName: String? = null,

    val stateCode: Int?

)