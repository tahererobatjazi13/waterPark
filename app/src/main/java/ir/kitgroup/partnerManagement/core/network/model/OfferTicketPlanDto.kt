package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class OfferTicketPlanDto(

    val offerTicketPlanId: String,

    val name: String? = null,

    val code: String? = null,

    val recreationCenterId: String? = null,

    val validFromDate: String? = null,

    val validToDate: String? = null,

    val status: Int? = null
)