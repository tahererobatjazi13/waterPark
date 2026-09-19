package ir.kitgroup.partnerManagement.core.network.model


import kotlinx.serialization.Serializable

@Serializable
data class WarningsDto(
    val warningsId: Int,
    val name: String,
    val score: String,
    val receationCenterId: Int,
    val code: String
)

