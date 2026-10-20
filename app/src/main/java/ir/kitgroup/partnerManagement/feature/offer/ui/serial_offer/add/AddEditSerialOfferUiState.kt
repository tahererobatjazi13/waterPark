package ir.kitgroup.partnerManagement.feature.offer.ui.serial_offer.add

import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.database.entity.OfferEntity


data class AddEditSerialOfferUiState(
    val id: String? = null,
    val selectedOfferPlanId: String? = null,
    val selectedOfferPlanTitle: String = "",
    val organizationId: String? = null,
    val selectedContractId: String? = null,
    val selectedContractTitle: String = "",
    val serialPrefix: String = "",
    val countSerialText: String = "",
    val serialFromText: String = "",
    val selectedStatusId: Int = 0,
    val offerPlans: List<OfferEntity> = emptyList(),
    val contracts: List<ContractEntity> = emptyList(),
    val isEditMode: Boolean = false,
    val isLoading: Boolean = false,
    val isInitialLoading: Boolean = false
)
