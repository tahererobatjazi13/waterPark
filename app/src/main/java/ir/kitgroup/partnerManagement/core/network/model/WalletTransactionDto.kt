package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class WalletTransactionDto(
    val walletTransactionId: String,

    val name: String? = null,

    val amount: String? = null,

    val createSource: Int? = null,

    val description: String? = null,

    val type: Int? = null,

    val organizationPersonId: String? = null,

    val ticketIssueLineId: String? = null
)