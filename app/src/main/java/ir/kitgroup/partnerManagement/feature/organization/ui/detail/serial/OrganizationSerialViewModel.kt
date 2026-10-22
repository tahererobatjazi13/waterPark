package ir.kitgroup.partnerManagement.feature.organization.ui.detail.serial

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.model.AssignedSerialWithDetail
import ir.kitgroup.partnerManagement.core.repository.AssignedSerialsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import javax.inject.Inject


@HiltViewModel
class OrganizationSerialViewModel @Inject constructor(
    private val assignedSerialsRepository: AssignedSerialsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val organizationId: String? = savedStateHandle["organizationId"]

    private val _assignedSerials = MutableStateFlow<List<AssignedSerialWithDetail>>(emptyList())
    val assignedSerials: StateFlow<List<AssignedSerialWithDetail>> =
        _assignedSerials.asStateFlow()

    fun observeOrganizationAssignedSerials(
        organizationId: String
    ) {
        viewModelScope.launch {
            assignedSerialsRepository
                .observeByOrganization(organizationId)
                .collect { assignedSerials ->
                    _assignedSerials.value = assignedSerials
                }
        }
    }
}