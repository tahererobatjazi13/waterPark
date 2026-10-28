package ir.kitgroup.partnerManagement.feature.organization.ui.detail.person
import ir.kitgroup.partnerManagement.core.ui.util.PersonOrganizationRole
import ir.kitgroup.partnerManagement.core.ui.util.isValidIranianMobile

data class AddOrganizationPersonUiState(
    val fullName: String = "",
    val role: Int = PersonOrganizationRole.MANAGER.id,
    val mobile: String = "",
    val phone: String = "",
    val gender: Int? = null,
    val description: String = "",
    val isCommissionEligible: Boolean = false,
    val isSaving: Boolean = false,
    val mobileError: String? = null,
    ) {

    val isFormValid: Boolean
        get() =
            fullName.isNotBlank() &&
                    mobile.isNotBlank() &&
                    mobileError == null &&
                    isValidIranianMobile(mobile) &&
                    role != 0
}
