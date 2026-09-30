package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ContractDto(

    val contractId: String,

    val title: String?,

    val organizationId: String?,

    val contractNumber: String?,

    val startDate: String?,

    val endDate: String?,

    val description: String?,

    val settlementPeriodType: Int?,

    val settlementPeriodTypeName: String?,

    val cooperationModel: Int?,

    val cooperationModelName: String?,

    val defaultDiscountPercent: Double?,

    val defaultCommissionPercent: Double?,

    val status: Int?,

    val statusName: String?,

    val stateCode: Int?

)