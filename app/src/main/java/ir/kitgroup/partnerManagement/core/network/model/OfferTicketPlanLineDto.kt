package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class OfferTicketPlanLineDto(

    val offerTicketPlanLineId: String,

    val name: String? = null,

    val commissionAmount: String? = null,

    val commissionPercent: String? = null,

    val commissionType: Int? = null,

    val discountAmount: String? = null,

    val discountPercent: String? = null,

    val discountType: Int? = null,

    val gender: Int? = null,

    val offerTicketPlanId: String? = null,

    val personCategory: Int? = null,

    val productServiceId: String? = null,

    val status: Int? = null
)