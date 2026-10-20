package ir.kitgroup.partnerManagement.core.network.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class AssignStandChangeStatusDto(
    @SerializedName("standAssignmentId")
    val standAssignmentId: String,
    @SerializedName("organizationId")
    val organizationId: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("actualReturnDate")
    val actualReturnDate: String,
    @SerializedName("deliveryToOrgDate")
    val deliveryToOrgDate: String)
