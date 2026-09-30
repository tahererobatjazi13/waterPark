package ir.kitgroup.partnerManagement.feature.contract.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationDao
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class AddContractUiState(
    val organizations: List<OrganizationWithDetail> = emptyList(),
    val preselectedOrganization: OrganizationWithDetail? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class AddContractViewModel @Inject constructor(
    private val organizationDao: OrganizationDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val preselectedOrganizationId: String? =
        savedStateHandle["preselectedOrganizationId"]

    val uiState: StateFlow<AddContractUiState> =
        organizationDao.observeAll()
            .map { organizations ->

                val preselected = if (preselectedOrganizationId != null) {
                    organizations.find {
                        it.organization.organizationId == preselectedOrganizationId
                    }
                } else {
                    null
                }

                AddContractUiState(
                    organizations = organizations,
                    preselectedOrganization = preselected,
                    isLoading = false
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = AddContractUiState()
            )
}