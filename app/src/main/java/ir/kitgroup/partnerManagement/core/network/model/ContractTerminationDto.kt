package ir.kitgroup.partnerManagement.core.network.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Serializable
data class ContractTerminationDto(
    @SerializedName("contractId")
    val contractId: String,
    @SerializedName("organizationId")
    val organizationId: String,
    @SerializedName("terminationDate")
    val terminationDate: String
)

