package ir.kitgroup.partnerManagement.feature.organization.ui.add.model


data class CreateOrganizationRequest(
    val organizationId: String? = null,
    val name: String,
    val ownerName: String?,
    val nationalId: String?,
    val organizationType: Int,
    val grade: Int,
    val statusGetStand: Boolean,
    val level: Int,
    val status: Int,
    val phone: String?,
    val landLine: String?,
    val mobile: String?,
    val address: String?,
    val cityId: String?,
    val regionId: String?,
    val latitude: Double?,
    val longitude: Double?,
    val visitorId: String?,
    val statusExternalCustomer: Boolean,
    val englishName: String?,
    val document: String? = null,
    val customerCapacity: Int?,
    val email: String?,
    val ticketSaleCountHistory: Int?
)
