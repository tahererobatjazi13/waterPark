package ir.kitgroup.partnerManagement.core.network.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class MeetingChangeStatusDto(
    @SerializedName("meetingId")
    val meetingId: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("visitRealDate")
    val visitRealDate: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("description")
    val description: String
)
