package ir.kitgroup.partnerManagement.feature.stand.ui.assigned_stands_organization


import ir.kitgroup.partnerManagement.core.database.entity.StandTypeEntity
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail

data class AddAssignedStandsOrganizationUiState(
    val organizationId: String = "",
    val advertisingStandId: String = "",
    val count: String = "1",
    val assignmentType: Int = 2,
    val assignmentMode: Int = 1,
    val assignmentDate: String = "",
    val plannedReturnDate: String = "",
    val deliveryToVisitorDate: String = "",
    val deliveryToOrgDate: String = "",
    val description: String = "",
    val status: Int = 1,

    val organizations: List<OrganizationWithDetail> = emptyList(),
    val stands: List<StandTypeEntity> = emptyList(),

    val isEditMode: Boolean = false,
    val isLoading: Boolean = false,
    val isInitialLoading: Boolean = false
) {
    val isFormValid: Boolean
        get() = organizationId.isNotBlank() &&
                advertisingStandId.isNotBlank() &&
                count.toIntOrNull() != null &&
                assignmentDate.isNotBlank()
}

