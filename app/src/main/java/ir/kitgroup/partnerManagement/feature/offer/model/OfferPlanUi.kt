package ir.kitgroup.partnerManagement.feature.offer.model

import ir.kitgroup.partnerManagement.core.ui.util.Status

data class OfferPlanUi(
    val id: String,
    val planCode: String,
    val planName: String,
    val startDate: String,
    val endDate: String,
    val status: Status
)
