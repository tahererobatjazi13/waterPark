package ir.kitgroup.partnerManagement.feature.offer.model


data class CreateSerialOfferRequest(
    val id: String? = null,
    val contractId: String? = null,
    val organizationId: String? = null,
    val offerTicketPlanId: String? = null,
    val meetingId: String? = null,
    val contractOfferStatus: Int,
    val countSerial: Int,
    val serialFrom: Int,
    val serialPrefix: String? = null
)
