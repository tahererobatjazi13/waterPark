package ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail_plan_list.add

import ir.kitgroup.partnerManagement.core.database.entity.ProductEntity

data class AddEditOfferDetailUiState(
    val planHeaderName: String = "",
    val offerTicketPlanLineId: String = "",
    val offerTicketPlanId: String = "",
    val productServiceId: String = "",
    val productServiceName: String = "",
    val discountType: Int = 0,
    val discountPercent: String = "",
    val discountAmount: String = "",
    val commissionType: Int = 0,
    val commissionPercent: String = "",
    val commissionAmount: String = "",
    val personCategory: Int = 0,
    val gender: Int = 0,
    val status: Int = 0,
    val availableProducts: List<ProductEntity> = emptyList(),
    val isLoading: Boolean = false,
    val isEditMode: Boolean = false
)
