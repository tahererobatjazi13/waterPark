package ir.kitgroup.partnerManagement.core.network.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class OrganizationChangeStatusDto(
    @SerializedName("organizationId")
    val organizationId: String,
    @SerializedName("statusOrg")
    val statusOrg: Int,
    @SerializedName("deactiveDate")
    val deactiveDate: String,
    @SerializedName("reason")
    val reason: String,
    @SerializedName("visitDate")
    val visitDate: String,
    @SerializedName("subjectVisitId")
    val subjectVisitId: String
)

