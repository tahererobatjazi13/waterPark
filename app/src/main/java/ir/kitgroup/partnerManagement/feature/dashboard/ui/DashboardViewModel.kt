package ir.kitgroup.partnerManagement.feature.dashboard.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.util.DataState
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.execute
import ir.kitgroup.partnerManagement.feature.dashboard.repository.SyncRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val syncRepository: SyncRepository
) : ViewModel() {

    val syncState = MutableStateFlow<DataState<Unit>>(DataState.Idle)
    val uploadState = MutableStateFlow<DataState<Unit>>(DataState.Idle)

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()


    fun onReceiveDataClick() {
        execute(
            stateFlow = syncState,
            eventFlow = _uiEvent,
            successMessageRes = R.string.msg_data_synced_successfully,
            action = { syncRepository.syncBaseData() }
        )
    }

    fun onSendDataClick() {
        execute(
            stateFlow = uploadState,
            eventFlow = _uiEvent,
            successMessageRes = R.string.msg_data_sent_successfully,
            action = { Result.success(Unit) }
        )
    }
}
