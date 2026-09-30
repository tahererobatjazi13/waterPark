package ir.kitgroup.partnerManagement.feature.stand.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.model.AssignedStandWithDetail
import ir.kitgroup.partnerManagement.core.repository.AssignedStandsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AssignedStandsDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    assignedStandsRepository: AssignedStandsRepository
) : ViewModel() {

    private val assignedStandId: String = checkNotNull(savedStateHandle["assignedStandId"])

    val standDetail: StateFlow<AssignedStandWithDetail?> = assignedStandsRepository
        .observeByIdWithDetail(assignedStandId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}
