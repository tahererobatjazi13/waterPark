package ir.kitgroup.partnerManagement.feature.organization.ui.detail.warning

import ir.kitgroup.partnerManagement.core.database.entity.WarningTypeEntity

data class AddEditOrganizationWarningUiState(
    val organizationId: String = "",
    val warningRecordId: String? = null,
    val warningTypeId: String = "",
    val description: String = "",
    val warningDate: String = "",
    val warningTypes: List<WarningTypeEntity> = emptyList(),
    val isEditMode: Boolean = false,
    val isLoading: Boolean = false,
    val isInitialLoading: Boolean = true
) {
    val isFormValid: Boolean
        get() = organizationId.isNotBlank() && warningTypeId.isNotBlank()
}
