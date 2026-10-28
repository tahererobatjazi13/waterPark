package ir.kitgroup.partnerManagement.feature.organization.ui.detail.stand


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.model.AssignedStandWithDetail
import ir.kitgroup.partnerManagement.core.repository.AssignedStandsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class OrganizationStandsViewModel @Inject constructor(
    private val assignedStandsRepository: AssignedStandsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val organizationId: String? = savedStateHandle["organizationId"]

    private val _assignedStands = MutableStateFlow<List<AssignedStandWithDetail>>(emptyList())
    val assignedStands: StateFlow<List<AssignedStandWithDetail>> =
        _assignedStands.asStateFlow()


    fun observeOrganizationContracts(orgId: String) {
        assignedStandsRepository.observeByOrganizationWithDetail(orgId)
            .onEach { contractList ->
                _assignedStands.value = contractList
            }
            .launchIn(viewModelScope)
    }
}
