package ir.kitgroup.partnerManagement.feature.stand.ui.assigned_stands_organization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.model.AssignedStandWithDetail
import ir.kitgroup.partnerManagement.core.repository.AssignedStandsRepository
import ir.kitgroup.partnerManagement.core.ui.util.AppErrorMapper
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class AssignedStandsOrganizationViewModel @Inject constructor(
    assignedStandsRepository: AssignedStandsRepository,
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    val assignments: StateFlow<List<AssignedStandWithDetail>?> =
        assignedStandsRepository.getOrganizationAssignedStandsWithDetail()
            .catch { throwable ->
                if (throwable is CancellationException) throw throwable
                _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(throwable)))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

}
