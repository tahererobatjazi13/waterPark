package ir.kitgroup.partnerManagement.feature.card.model

import ir.kitgroup.partnerManagement.core.ui.util.Status

data class CardModel(
    val title: String,
    val cardNumber: String,
    val collectionName: String,
    val date: String,
    val status: Status
)