package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ContractDto(

    val contractId: String,

    val name: String? = null,

    val contractNumber: String? = null,

    val cooperationModel: Int? = null,

    val defaultCommissionPercent: Double? = null,

    val defaultDiscountPercent: Double? = null,

    val description: String? = null,

    val endDate: String? = null,

    val organizationId: String? = null,

    val settlementPeriodType: Int? = null,

    val startDate: String? = null,

    val contractStatus: Int? = null
)