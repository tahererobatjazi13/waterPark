package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class StandTypeDto(

    val standTypeId: String,

    val title: String?,

    val code: String?,

    val description: String?,

    val displayTypeName: String?,

    val installationTypeName: String?,

    val standTypeName: String?,

    val displayType: Int?,

    val installationType: Int?,

    val standType: Int?,

    val stateCode: Int?

)