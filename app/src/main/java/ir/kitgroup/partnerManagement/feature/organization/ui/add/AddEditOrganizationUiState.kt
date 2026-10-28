package ir.kitgroup.partnerManagement.feature.organization.ui.add

import ir.kitgroup.partnerManagement.core.database.entity.CityEntity
import ir.kitgroup.partnerManagement.core.database.entity.PersonEntity
import ir.kitgroup.partnerManagement.core.database.entity.RegionEntity
import ir.kitgroup.partnerManagement.core.database.entity.VisitorEntity
import ir.kitgroup.partnerManagement.core.ui.model.OrganizationImageItem
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationStatus

data class AddEditOrganizationUiState(
    val organizationId: String? = null,
    val organizationName: String = "",
    val organizationType: Int = 0,
    val organizationLevel: Int = 0,
    val organizationStatus: OrganizationStatus = OrganizationStatus.INITIAL_REGISTRATION,
    val inactiveReason: String = "",
    val ownerName: String = "",
    val englishName: String = "",
    val nationalId: String = "",
    val landLine: String = "",
    val phone: String = "",
    val mobile: String = "",
    val mobileError: String? = null,
    val email: String = "",
    val address: String = "",
    val cityId: String = "",
    val regionId: String = "",
    var latitude: Double? = null,
    var longitude: Double? = null,
    var visitorId: String= "",
    val organizationGrade: Int = 1,
    val ticketSaleCountHistory: String = "",
    val customerCapacity: String = "",
    val statusExternalCustomer: Boolean = false,
    val statusGetStand: Boolean = false,
    val description: String = "",
    val organizationPersons: List<PersonEntity> = emptyList(),
    val images: List<OrganizationImageItem> = emptyList(),

    val cities: List<CityEntity> = emptyList(),
    val regions: List<RegionEntity> = emptyList(),
    val visitors: List<VisitorEntity> = emptyList(),

    val isEditMode: Boolean = false,
    val isLoading: Boolean = false,
    val isInitialLoading: Boolean = true
) {
    val isFormValid: Boolean
        get() = organizationName.isNotBlank() &&
                organizationType != 0 &&
                cityId.isNotBlank() &&
                (isEditMode || (ownerName.isNotBlank() && mobile.isNotBlank() && mobileError == null))


}
