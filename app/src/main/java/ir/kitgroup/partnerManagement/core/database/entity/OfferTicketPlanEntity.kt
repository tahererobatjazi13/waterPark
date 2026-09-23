package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offer_ticket_plan")
data class OfferTicketPlanEntity(

    @PrimaryKey
    val offerTicketPlanId: String,

    val name: String? = null,

    val code: String? = null,

    val recreationCenterId: String? = null,

    val validFromDate: String? = null,

    val validToDate: String? = null,

    val status: Int? = null
)