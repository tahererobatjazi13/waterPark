package ir.kitgroup.partnerManagement.feature.contract.model

import com.google.gson.annotations.SerializedName

data class TerminateActiveContractRequest(

    @SerializedName("contractId")
    val contractId: String? = null,

    @SerializedName("organizationId")
    val organizationId: String,

    @SerializedName("terminationDate")
    val terminationDate: String,

)