package ir.kitgroup.partnerManagement.feature.offer.model

data class CreateOfferPlanRequest(
    val offerTicketPlanId: String? = null,
    val title: String,
    val status: Int,
    val validFromDate: String? = null,
    val validToDate: String? = null,
)
