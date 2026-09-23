package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contract_offer")
data class ContractOfferEntity(

    @PrimaryKey
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