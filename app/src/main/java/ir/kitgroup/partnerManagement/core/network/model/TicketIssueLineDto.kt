package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class TicketIssueLineDto(
    val ticketIssueLineId: String,

    val name: String? = null,

    val consumedAgeCategory: Int? = null,

    val consumedDate: String? = null,

    val consumedGender: Int? = null,

    val discountPrice: String? = null,

    val gender: Int? = null,

    val personCategory: Int? = null,

    val productServiceId: String? = null,

    val serialNumber: Int? = null,

    val ticketIssueId: String? = null,

    val ticketStatus: Int? = null,

    val unitPrice: String? = null,

    val count: Int? = null,

    val realCount: Int? = null,

    val contractOfferId: String? = null,

    val sumPrice: String? = null,

    val finalProductServiceId: String? = null,

    val finalCustomerType: Int? = null,

    val usageDate: String? = null,

    val useTime: String? = null,

    val visitorId: String? = null,

    val recreationCenterPartId: String? = null,

    val totalCommission: String? = null,

    val statusUsage: Int? = null,

    val approvedBy: String? = null,

    val approvedDate: String? = null
)