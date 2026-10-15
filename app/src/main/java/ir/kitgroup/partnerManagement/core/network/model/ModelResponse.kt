package ir.kitgroup.partnerManagement.core.network.model


data class ModelResponse(
    val success: Boolean,
    val message: String,
    val data: String?
)