package ir.kitgroup.partnerManagement.feature.contract.model

import ir.kitgroup.partnerManagement.core.ui.util.Status


data class ContractUi(
    val id: String,
    val organizationName: String,
    val contractTitle: String,
    val cooperationModel: String,
    val settlementPeriodType: String,
    val startDate: String,
    val endDate: String,
    val status: Status,
    val defaultDiscountPercent: Double,
    val defaultCommissionPercent: Double,
    val description: String
)
