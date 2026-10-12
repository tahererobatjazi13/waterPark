package ir.kitgroup.partnerManagement.feature.offer.ui.serial_offer


data class AddContractOfferUiState(
    val isEditMode: Boolean = false,
    val id: String? = null,

    val selectedOfferPlanId: String? = null,
    val selectedOfferPlanTitle: String = "",

    val selectedContractId: String? = null,
    val selectedContractTitle: String = "",

    val serialPrefix: String = "",
    val countSerialText: String = "",
    val serialFromText: String = "",

    val selectedStatusId: Int = 0,

    val offerPlans: List<DropDownItem> = emptyList(),
    val contracts: List<DropDownItem> = emptyList(),
)

data class DropDownItem(
    val id: String,
    val title: String
)
