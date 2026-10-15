package ir.kitgroup.partnerManagement.feature.offer.model

data class OfferDetailPlanRequest(
    val offerTicketPlanLineId: String? = null,
    val offerTicketPlanId: String? = null,
    val productServiceId: String? = null,
    val discountType: Int,
    val discountPercent: Int,
    val discountAmount: Int,
    val commissionType: Int,
    val commissionPercent: Int,
    val commissionAmount: Int,
    val personCategory: Int,
    val gender: Int,
    val status: Int,
)