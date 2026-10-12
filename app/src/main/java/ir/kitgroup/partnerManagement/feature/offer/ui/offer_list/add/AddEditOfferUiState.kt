package ir.kitgroup.partnerManagement.feature.offer.ui.offer_list.add


import ir.kitgroup.partnerManagement.core.ui.util.OfferPlanStatus


data class AddEditOfferUiState(
    val offerId: String? = null,
    val offerName: String = "",
    val fromDate: String = "",
    val toDate: String = "",
    val status: OfferPlanStatus = OfferPlanStatus.ACTIVE,
    val isLoading: Boolean = false,
    val isEditMode: Boolean = false
)
