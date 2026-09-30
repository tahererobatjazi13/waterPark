package ir.kitgroup.partnerManagement.feature.stand.ui.assigned_stands_visitor

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
class AssignedStandsVisitorViewModel @Inject constructor(
    assignedStandsRepository: AssignedStandsRepository
) : ViewModel() {

    val assignments: StateFlow<List<AssignedStandWithDetail>> = assignedStandsRepository
        .getVisitorAssignedStandsWithDetail()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}
