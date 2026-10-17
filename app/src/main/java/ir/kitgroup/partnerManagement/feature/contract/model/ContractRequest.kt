package ir.kitgroup.partnerManagement.feature.contract.model

import com.google.gson.annotations.SerializedName

data class ContractRequest(

    @SerializedName("contractId")
    val contractId: String? = null,

    @SerializedName("organizationId")
    val organizationId: String,

    @SerializedName("startDate")
    val startDate: String,

    @SerializedName("settlementPeriodType")
    val settlementPeriodType: Int,

    @SerializedName("cooperationModel")
    val cooperationModel: Int,

    @SerializedName("defaultDiscountPercent")
    val defaultDiscountPercent: Double?,

    @SerializedName("defaultCommissionPercent")
    val defaultCommissionPercent: Double?,

    @SerializedName("status")
    val status: Int
)