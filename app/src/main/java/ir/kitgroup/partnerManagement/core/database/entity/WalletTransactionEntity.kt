package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallet_transaction")
data class WalletTransactionEntity(
    @PrimaryKey
    val walletTransactionId: String,

    val name: String? = null,

    val amount: String? = null,

    val createSource: Int? = null,

    val description: String? = null,

    val type: Int? = null,

    val organizationPersonId: String? = null,

    val ticketIssueLineId: String? = null
)