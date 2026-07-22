package ir.kitgroup.hotel.feature.card.model

import ir.kitgroup.hotel.core.ui.util.CardStatus

data class CardModel(
    val title: String,
    val cardNumber: String,
    val hotelName: String,
    val date: String,
    val status: CardStatus
)