package ir.kitgroup.partnerManagement.feature.stand.ui.assigned_stands_organization


import ir.kitgroup.partnerManagement.core.database.entity.StandTypeEntity
import ir.kitgroup.partnerManagement.core.database.model.AssignedStandWithDetail
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail

data class AddAssignedStandsOrganizationUiState(
    val organizations: List<OrganizationWithDetail> = emptyList(),
    val preselectedOrganization: OrganizationWithDetail? = null,
    val stands: List<StandTypeEntity> = emptyList(),
    val editStandDetail: AssignedStandWithDetail? = null,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)
