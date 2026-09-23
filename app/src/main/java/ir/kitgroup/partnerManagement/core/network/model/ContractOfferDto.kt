package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ContractOfferDto(

    val contractOfferId: String,

    val name: String? = null,

    val contractId: String? = null,

    val countSerial: Int? = null,

    val lastSerialUsed: Int? = null,

    val serialFrom: Int? = null,

    val serialPrefix: String? = null,

    val serialTo: Int? = null,

    val offerTicketPlanId: String? = null,

    val contractOfferStatus: Int? = null,

    val organizationId: String? = null,

    val remainSerialCount: Int? = null,

    val meetingId: String? = null
)