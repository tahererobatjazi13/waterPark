package ir.kitgroup.partnerManagement.feature.contract.ui.add
import androidx.compose.runtime.*


@Stable
class AddContractFormState {

    var organizationId by mutableStateOf("")

    var contractName by mutableStateOf("")

    var contractNumber by mutableStateOf("")

    var startDate by mutableStateOf("")

    var settlementPeriodType by mutableStateOf(0)

    var cooperationModel by mutableStateOf(0)

    var defaultDiscountPercent by mutableStateOf("")

    var defaultCommissionPercent by mutableStateOf("")


    var status by mutableStateOf(0)


}