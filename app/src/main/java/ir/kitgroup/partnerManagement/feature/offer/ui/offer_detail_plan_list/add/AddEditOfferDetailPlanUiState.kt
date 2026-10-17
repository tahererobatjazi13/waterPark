package ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail_plan_list.add

import ir.kitgroup.partnerManagement.core.database.entity.ProductEntity
import ir.kitgroup.partnerManagement.core.ui.util.CommissionType
import ir.kitgroup.partnerManagement.core.ui.util.DiscountType
import ir.kitgroup.partnerManagement.core.ui.util.GenderType
import ir.kitgroup.partnerManagement.core.ui.util.OfferDetailPlanStatus
import ir.kitgroup.partnerManagement.core.ui.util.PersonCategory

data class AddEditOfferDetailPlanUiState(
    val offerTicketPlanName: String = "",
    val offerTicketPlanLineId: String? = null,
    val offerTicketPlanId: String = "",
    val productServiceId: String = "",
    val productServiceName: String = "",
    val discountType: DiscountType = DiscountType.NONE,
    val discountPercent: String = "",
    val discountAmount: String = "",
    val commissionType: CommissionType = CommissionType.NONE,
    val commissionPercent: String = "",
    val commissionAmount: String = "",
    val personCategory: PersonCategory = PersonCategory.UNASSIGNED,
    val gender: GenderType = GenderType.UNASSIGNED,
    val status: OfferDetailPlanStatus = OfferDetailPlanStatus.ACTIVE,
    val availableProducts: List<ProductEntity> = emptyList(),
    val isLoading: Boolean = false,
    val isEditMode: Boolean = false,
    val isInitialLoading: Boolean = false
) {
    val isFormValid: Boolean
        get() {
            if (productServiceId.isBlank() || isLoading || isInitialLoading) return false

            val isDiscountValid = when (discountType) {
                DiscountType.PERCENTAGE -> {
                    val p = discountPercent.toIntOrNull()
                    p != null && p in 1..100
                }

                DiscountType.FIXED_AMOUNT -> {
                    val a = discountAmount.toLongOrNull()
                    a != null && a > 0L
                }

                DiscountType.NONE -> true
            }

            val isCommissionValid = when (commissionType) {
                CommissionType.PERCENTAGE -> {
                    val p = commissionPercent.toIntOrNull()
                    p != null && p in 1..100
                }

                CommissionType.FIXED_AMOUNT -> {
                    val a = commissionAmount.toLongOrNull()
                    a != null && a > 0L
                }

                CommissionType.NONE -> true
            }

            return isDiscountValid && isCommissionValid
        }
}
