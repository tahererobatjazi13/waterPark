package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "offer_details",
    indices = [
        Index("offerId"),
        Index("productId")
    ]
)
data class OfferDetailEntity(

    @PrimaryKey
    val offerDetailId: String,

    val offerId: String,

    val productId: String?,

    val productName: String? = null,
    // 0 = بدون تخفیف، 1 = درصدی، 2 = مبلغ ثابت
    val discountType: Int? = null,

    val discountTypeName: String? = null,

    val discountPercent: Double? = null,

    val discountAmount: Double? = null,

    // 0 = بدون پورسانت، 1 = درصدی، 2 = مبلغ ثابت
    val commissionType: Int? = null,

    val commissionTypeName: String? = null,

    val commissionPercent: Double? = null,

    val commissionAmount: Double? = null,

    val personCategory: Int? = null,

    val personCategoryName: String? = null,

    val gender: Int? = null,

    val genderName: String? = null,

    //  0 = فعال، 1 = موقتا متوقف، 2 = ابطال شده
    val status: Int? = null,

    val statusName: String? = null,

    val stateCode: Int? = null

)