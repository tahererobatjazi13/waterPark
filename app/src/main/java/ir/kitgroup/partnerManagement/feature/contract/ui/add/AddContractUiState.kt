package ir.kitgroup.partnerManagement.feature.contract.ui.add

import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail


data class AddContractUiState(
    val organizations: List<OrganizationWithDetail> = emptyList(),
    val preselectedOrganization: OrganizationWithDetail? = null,
    val contract: ContractEntity? = null,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val message: String? = null,
    val createdContractId: String? = null
)