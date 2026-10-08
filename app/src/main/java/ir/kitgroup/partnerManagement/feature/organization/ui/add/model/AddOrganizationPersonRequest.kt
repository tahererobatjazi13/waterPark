package ir.kitgroup.partnerManagement.feature.organization.ui.add.model

data class AddOrganizationPersonRequest(
    val organizationId: String,
    val fullName: String,
    val role: Int,
    val mobile: String?,
    val phone: String?,
    val gender: Int?,
    val description: String?,
    val isCommissionEligible: Boolean
)