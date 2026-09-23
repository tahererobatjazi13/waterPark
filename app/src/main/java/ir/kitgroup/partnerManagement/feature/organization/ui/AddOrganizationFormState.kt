package ir.kitgroup.partnerManagement.feature.organization.ui

import androidx.compose.runtime.*
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.PersonEntity
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationStatus


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

    val organizationPersons = mutableStateListOf<PersonEntity>()

    val images = mutableStateListOf(
        R.drawable.ic_logo,
        R.drawable.ic_logo,
        R.drawable.ic_logo,
        R.drawable.ic_logo
    )

    fun resetPersonFields(personState: AddPersonFormState) {
        personState.name = ""
        personState.mobile = ""
        personState.phone1 = ""
        personState.gender = 0
        personState.status = 1
        personState.description = ""
    }
}



@Stable
class AddPersonFormState {
    var name by mutableStateOf("")
    var description by mutableStateOf("")
    var gender by mutableIntStateOf(0)
    var mobile by mutableStateOf("")
    var phone1 by mutableStateOf("")
    var status by mutableIntStateOf(0)
}

@Composable
 fun rememberAddOrganizationFormState(): AddOrganizationFormState {
    return remember { AddOrganizationFormState() }
}

@Composable
 fun rememberAddPersonFormState(): AddPersonFormState {
    return remember { AddPersonFormState() }
}
