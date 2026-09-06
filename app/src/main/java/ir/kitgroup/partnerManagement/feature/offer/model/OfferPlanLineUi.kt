package ir.kitgroup.partnerManagement.feature.offer.model

import ir.kitgroup.partnerManagement.core.ui.util.Status

data class OfferPlanLineUi(
    val id: String,
    val planHeader: String,
    val relatedProductOrService: String,
    val ageCategory: String?,
    val gender: String?,
    val name: String,
    val commissionType: String,
    val commissionPercent: Double?,
    val commissionAmount: Long?,
    val discountType: String,
    val discountPercent: Double?,
    val discountAmount: Long?,
    val status: Status
)
