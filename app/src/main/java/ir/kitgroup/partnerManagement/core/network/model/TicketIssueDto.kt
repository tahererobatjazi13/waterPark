package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class TicketIssueDto(
    val ticketIssueId: String,

    val name: String? = null,

    val count: Int? = null,

    val customerMobile: String? = null,

    val customerName: String? = null,

    val description: String? = null,

    val issuedOn: String? = null,

    val organizationPersonId: String? = null,

    val customerType: Int? = null,

    val sumPrice: String? = null,

    val sumDiscountPrice: String? = null,

    val organizationId: String? = null,

    val sourceCreate: Int? = null
)