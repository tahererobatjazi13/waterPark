package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class OfferDetailDto(

    val offerDetailId: String,

    val offerId: String,

    val productId: String?,

    val productName: String?,

    val discountType: Int?,

    val discountTypeName: String?,

    val discountPercent: Double?,

    val discountAmount: Double?,

    val commissionType: Int?,

    val commissionTypeName: String?,

    val commissionPercent: Double?,

    val commissionAmount: Double?,

    val personCategory: Int?,

    val personCategoryName: String?,

    val gender: Int?,

    val genderName: String?,

    val status: Int?,

    val statusName: String?,

    val stateCode: Int?

)