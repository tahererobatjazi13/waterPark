package ir.kitgroup.partnerManagement.feature.organization.ui.detail.contract

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.repository.ContractRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class OrganizationContractsViewModel @Inject constructor(
    private val contractRepository: ContractRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val organizationId: String? = savedStateHandle["organizationId"]

    private val _contracts = MutableStateFlow<List<ContractEntity>>(emptyList())
    val contracts: StateFlow<List<ContractEntity>> = _contracts.asStateFlow()

    fun observeOrganizationContracts(orgId: String) {
        contractRepository.observeByOrganization(orgId)
            .onEach { contractList ->
                _contracts.value = contractList
            }
            .launchIn(viewModelScope)
    }
}
