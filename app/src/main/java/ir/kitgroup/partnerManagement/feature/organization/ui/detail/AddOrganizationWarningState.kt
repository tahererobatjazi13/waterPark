package ir.kitgroup.partnerManagement.feature.organization.ui.detail

data class AddOrganizationWarningState(
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)