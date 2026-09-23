package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offer_ticket_plan_line")
data class OfferTicketPlanLineEntity(

    @PrimaryKey
    val offerTicketPlanLineId: String,

    val name: String? = null,

    val commissionAmount: String? = null,

    val commissionPercent: String? = null,

    // 0 = بدون پورسانت، 1 = درصدی، 2 = مبلغ ثابت
    val commissionType: Int? = null,

    val discountAmount: String? = null,

    val discountPercent: String? = null,

    // 0 = بدون تخفیف، 1 = درصدی، 2 = مبلغ ثابت
    val discountType: Int? = null,

    val gender: Int? = null,

    val offerTicketPlanId: String? = null,

    val personCategory: Int? = null,

    val productServiceId: String? = null,

    //  0 = فعال، 1 = موقتا متوقف، 2 = ابطال شده
    val status: Int? = null
)