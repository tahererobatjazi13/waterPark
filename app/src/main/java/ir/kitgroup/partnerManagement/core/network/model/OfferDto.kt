package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class OfferDto(

    val offerId: String,

    val title: String?,

    val code: String?,

    val fromDate: String?,

    val toDate: String?,

    val statusOffer: Int?,

    val statusOfferName: String?,

    val stateCode: Int?

)