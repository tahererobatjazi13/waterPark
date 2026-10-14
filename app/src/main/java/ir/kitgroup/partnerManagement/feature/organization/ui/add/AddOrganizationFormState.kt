package ir.kitgroup.partnerManagement.feature.organization.ui.add

import androidx.compose.runtime.*
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.PersonEntity
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationStatus


@Stable
class AddOrganizationFormState {

    var organizationName by mutableStateOf("")
    var organizationType by mutableIntStateOf(0)
    var organizationLevel by mutableIntStateOf(0)
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
    var cityId by mutableStateOf("")
    var regionId by mutableStateOf("")
    var latitude by mutableStateOf<Double?>(null)
    var longitude by mutableStateOf<Double?>(null)
    var organizationGrade by mutableIntStateOf(1)
    var ticketSaleCountHistory by mutableStateOf("")
    var customerCapacity by mutableStateOf("")
    var statusExtenalCutomer by mutableStateOf(false)
    var statusGetStand by mutableStateOf(false)
    var description by mutableStateOf("")
    val organizationPersons = mutableStateListOf<PersonEntity>()

    val images = mutableStateListOf(
        R.drawable.ic_logo,
        R.drawable.ic_logo
    )

    fun resetPersonFields(personState: AddPersonFormState) {
        personState.name = ""
        personState.mobile = ""
    }
}

class AddPersonFormState {
    var name by mutableStateOf("")
    var mobile by mutableStateOf("")
    var phone by mutableStateOf("")
    var gender by mutableStateOf<Int?>(null)
    var role by mutableStateOf<Int?>(null)
    var isCommissionEligible by mutableStateOf(false)
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
