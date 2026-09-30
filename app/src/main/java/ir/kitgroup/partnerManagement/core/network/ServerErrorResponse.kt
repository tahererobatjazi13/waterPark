package ir.kitgroup.partnerManagement.core.network

import com.google.gson.annotations.SerializedName

data class ServerErrorResponse(
    @SerializedName("success")
    val success: Boolean?,

    @SerializedName("message")
    val message: String?
)