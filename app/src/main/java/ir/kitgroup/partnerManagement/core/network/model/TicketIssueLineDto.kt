package ir.kitgroup.partnerManagement.core.network.model

import com.google.gson.annotations.SerializedName

data class TicketIssueLineDto(
    @SerializedName("ticketIssueLineId")
    val ticketIssueLineId: String,

    @SerializedName("ticketIssueId")
    val ticketIssueId: String,

    @SerializedName("serialNumber")
    val serialNumber: String?,

    @SerializedName("issueNumber")
    val issueNumber: String?,

    @SerializedName("customerName")
    val customerName: String?,

    @SerializedName("sourceCreate")
    val sourceCreate: String?,

    @SerializedName("organizationId")
    val organizationId: String?,

    @SerializedName("organizationName")
    val organizationName: String?,

    @SerializedName("issuedOn")
    val issuedOn: String?,

    @SerializedName("contractOfferId")
    val contractOfferId: String?,

    @SerializedName("productServiceId")
    val productServiceId: String?,

    @SerializedName("productServiceName")
    val productServiceName: String?,

    @SerializedName("finalProductServiceId")
    val finalProductServiceId: String?,

    @SerializedName("finalProductServiceName")
    val finalProductServiceName: String?,

    @SerializedName("recreationCenterPartId")
    val recreationCenterPartId: String?,

    @SerializedName("visitorId")
    val visitorId: String?,

    @SerializedName("approvedById")
    val approvedById: String?,

    @SerializedName("ticketStatus")
    val ticketStatus: Int?,

    @SerializedName("ticketStatusName")
    val ticketStatusName: String?,

    @SerializedName("statusUsage")
    val statusUsage: Int?,

    @SerializedName("statusUsageName")
    val statusUsageName: String?,

    @SerializedName("usageDate")
    val usageDate: String?,

    @SerializedName("useTime")
    val useTime: String?,

    @SerializedName("approvedDate")
    val approvedDate: String?,

    @SerializedName("personCategory")
    val personCategory: Int?,

    @SerializedName("consumedAgeCategory")
    val consumedAgeCategory: Int?,

    @SerializedName("gender")
    val gender: Int?,

    @SerializedName("consumedGender")
    val consumedGender: Int?,

    @SerializedName("finalCustomerType")
    val finalCustomerType: Int?,

    @SerializedName("count")
    val count: Int?,

    @SerializedName("realCount")
    val realCount: Int?,

    @SerializedName("unitPrice")
    val unitPrice: Long?,

    @SerializedName("discountPrice")
    val discountPrice: Long?,

    @SerializedName("netPrice")
    val netPrice: Long?,

    @SerializedName("sumPrice")
    val sumPrice: Long?,

    @SerializedName("discountType")
    val discountType: Int?,

    @SerializedName("discountAmount")
    val discountAmount: Double?,

    @SerializedName("commissionType")
    val commissionType: Int?,

    @SerializedName("commissionAmount")
    val commissionAmount: Long?,

    @SerializedName("totalCommission")
    val totalCommission: Long?,

    @SerializedName("stateCode")
    val stateCode: Int?,

    @SerializedName("createdOn")
    val createdOn: String?,

    @SerializedName("modifiedOn")
    val modifiedOn: String?
)
