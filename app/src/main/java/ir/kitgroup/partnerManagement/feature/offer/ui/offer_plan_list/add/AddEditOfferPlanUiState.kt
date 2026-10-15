package ir.kitgroup.partnerManagement.feature.offer.ui.offer_plan_list.add

import ir.kitgroup.partnerManagement.core.ui.util.OfferPlanStatus

data class AddEditOfferPlanUiState(
    val offerId: String? = null,
    val offerName: String = "",
    val fromDate: String = "",
    val toDate: String = "",
    val status: OfferPlanStatus = OfferPlanStatus.ACTIVE,
    val isEditMode: Boolean = false,
    val isLoading: Boolean = false,
    val isInitialLoading: Boolean = false
)