package ir.kitgroup.partnerManagement.core.network.model

import com.google.gson.annotations.SerializedName

// کلاس DTO دریافت داده از وب‌سرویس باید دقیقاً این انوتیشن‌ها را داشته باشد:
data class OfferDto(
    @SerializedName("offerId") // یا @SerialName("offerId")
    val offerId: String,

    @SerializedName("code")
    val code: String? = null,

    @SerializedName("title")
    val title: String?,

    @SerializedName("fromDate")
    val fromDate: String?,

    @SerializedName("toDate")
    val toDate: String?,

    @SerializedName("statusOffer")
    val statusOffer: Int?,

    @SerializedName("statusOfferName")
    val statusOfferName: String?,

    @SerializedName("stateCode")
    val stateCode: Int?
)
