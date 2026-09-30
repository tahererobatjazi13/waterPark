package ir.kitgroup.partnerManagement.feature.stand.ui.assigned_stands_organization

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

data class AddAssignedStandsOrganizationUiState(
    val organizations: List<OrganizationWithDetail> = emptyList(),
    val preselectedOrganization: OrganizationWithDetail? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class AddAssignedStandsOrganizationViewModel @Inject constructor(
    private val organizationDao: OrganizationDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val preselectedOrganizationId: String? = savedStateHandle["preselectedOrganizationId"]

    val uiState: StateFlow<AddAssignedStandsOrganizationUiState> =
        organizationDao.observeAll()
            .map { organizations ->
                val preselected = if (preselectedOrganizationId != null) {
                    organizations.find {
                        it.organization.organizationId == preselectedOrganizationId
                    }
                } else {
                    null
                }

                AddAssignedStandsOrganizationUiState(
                    organizations = organizations,
                    preselectedOrganization = preselected,
                    isLoading = false
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = AddAssignedStandsOrganizationUiState()
            )
}
