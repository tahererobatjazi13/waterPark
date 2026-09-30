package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offers")
data class OfferEntity(

    @PrimaryKey
    val offerId: String,

    val title: String? = null,

    val code: String? = null,

    val fromDate: String? = null,

    val toDate: String? = null,

    val statusOffer: Int? = null,

    val statusOfferName: String? = null,

    val stateCode: Int? = null
)