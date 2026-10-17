package ir.kitgroup.partnerManagement.feature.contract.ui.add

import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import ir.kitgroup.partnerManagement.core.ui.util.ContractStatus
import ir.kitgroup.partnerManagement.core.ui.util.CooperationModel
import ir.kitgroup.partnerManagement.core.ui.util.SettlementPeriodType


data class AddEditContractUiState(
    val organizationId: String = "",
    val organizationName: String = "",
    val startDate: String = "",
    val settlementPeriodType: SettlementPeriodType = SettlementPeriodType.MONTHLY,
    val cooperationModel: CooperationModel = CooperationModel.REFERRAL_VOUCHER,
    val defaultDiscountPercent: String = "",
    val defaultCommissionPercent: String = "",
    val status: ContractStatus = ContractStatus.DRAFT,
    val organizations: List<OrganizationWithDetail> = emptyList(),
    val preselectedOrganization: OrganizationWithDetail? = null,
    val isEditMode: Boolean = false,
    val isLoading: Boolean = false,
    val isInitialLoading: Boolean = true,
) {
    val isFormValid: Boolean
        get() = organizationId.isNotBlank() && startDate.isNotBlank()
}
