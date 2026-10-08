package ir.kitgroup.partnerManagement.feature.organization.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import ir.kitgroup.partnerManagement.core.repository.OrganizationRepository
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.usecase.CheckCanAddOrganizationUseCase
import ir.kitgroup.partnerManagement.feature.dashboard.repository.SyncRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrganizationsViewModel @Inject constructor(
    private val repository: OrganizationRepository,
    private val syncRepository: SyncRepository,
    private val checkCanAddOrganizationUseCase: CheckCanAddOrganizationUseCase

) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()
    fun checkCanNavigateToAddOrganization(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val error = checkCanAddOrganizationUseCase()
            if (error != null) {
                _uiEvent.emit(UiEvent.ShowError(error))
            } else {
                onSuccess()
            }
        }
    }
    val allOrganizations: StateFlow<List<OrganizationWithDetail>> =
        repository.observeAll()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun syncOrganizationsData() {
        viewModelScope.launch {
            try {
                syncRepository.syncData()
            } catch (e: Exception) {
            }
        }
    }
}

