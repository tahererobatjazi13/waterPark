package ir.kitgroup.partnerManagement.feature.organization.ui

import androidx.compose.runtime.*
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationStatus
import ir.kitgroup.partnerManagement.feature.organization.model.PersonOrganization


@Stable
class AddOrganizationFormState {
    var organizationName by mutableStateOf("")
    var organizationType by mutableStateOf("")
    var organizationLevel by mutableStateOf("معمولی")
    var organizationStatus by mutableStateOf(OrganizationStatus.INITIAL_REGISTRATION)
    var inactiveReason by mutableStateOf("")

    var ownerName by mutableStateOf("")
    var englishName by mutableStateOf("")
    var nationalId by mutableStateOf("")
    var landLine by mutableStateOf("")
    var phone by mutableStateOf("")
    var mobile by mutableStateOf("")
    var email by mutableStateOf("")
    var address by mutableStateOf("")
    var city  by mutableStateOf("")
    var region by mutableStateOf("")

    var organizationGrade by mutableIntStateOf(3)
    var ticketSaleCountHistory by mutableStateOf("")
    var customerCapacity by mutableStateOf("")

    var statusExtenalCutomer by mutableStateOf(false)
    var statusGetStand by mutableStateOf(false)

    var description by mutableStateOf("")

    val organizationPersons = mutableStateListOf<PersonOrganization>()

    val images = mutableStateListOf(
        R.drawable.ic_logo,
        R.drawable.ic_logo,
        R.drawable.ic_logo,
        R.drawable.ic_logo
    )

    fun resetPersonFields(personState: AddPersonFormState) {
        personState.name = ""
        personState.mobile = ""
        personState.phone = ""
        personState.gender = ""
        personState.status = "فعال"
        personState.description = ""
    }
}

@Stable
class AddPersonFormState {
    var name by mutableStateOf("")
    var mobile by mutableStateOf("")
    var phone by mutableStateOf("")
    var gender by mutableStateOf("")
    var status by mutableStateOf("فعال")
    var description by mutableStateOf("")
}

@Composable
 fun rememberAddOrganizationFormState(): AddOrganizationFormState {
    return remember { AddOrganizationFormState() }
}

@Composable
 fun rememberAddPersonFormState(): AddPersonFormState {
    return remember { AddPersonFormState() }
}
