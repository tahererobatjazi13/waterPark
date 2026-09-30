package ir.kitgroup.partnerManagement.feature.dashboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.dao.StatisticDao
import ir.kitgroup.partnerManagement.core.database.entity.StatisticEntity
import ir.kitgroup.partnerManagement.core.database.model.MeetingWithDetail
import ir.kitgroup.partnerManagement.core.repository.MeetingRepository
import ir.kitgroup.partnerManagement.core.ui.util.DataState
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.execute
import ir.kitgroup.partnerManagement.feature.dashboard.repository.SyncRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val syncRepository: SyncRepository,
    private val meetingRepository: MeetingRepository,
    private val statisticDao: StatisticDao,
) : ViewModel() {

    val syncState =
        MutableStateFlow<DataState<Unit>>(DataState.Idle)

    val uploadState =
        MutableStateFlow<DataState<Unit>>(DataState.Idle)

    private val _uiEvent =
        MutableSharedFlow<UiEvent>()

    val uiEvent =
        _uiEvent.asSharedFlow()

    fun onReceiveDataClick() {

        execute(
            stateFlow = syncState,
            eventFlow = _uiEvent,
            successMessageRes =
            R.string.msg_data_synced_successfully,
            action = {
                syncRepository.syncData()
            }
        )
    }

    fun onSendDataClick() {

        execute(
            stateFlow = uploadState,
            eventFlow = _uiEvent,
            successMessageRes =
            R.string.msg_data_sent_successfully,
            action = {
                Result.success(Unit)
            }
        )
    }

    val statistics: StateFlow<StatisticEntity?> = statisticDao.observe()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    private val todayGregorianDate: String = getTodayGregorianDate()

    private fun getTodayGregorianDate(): String {
        val today = Calendar.getInstance()

        return String.format(
            Locale.US,
            "%04d-%02d-%02d",
            today.get(Calendar.YEAR),
            today.get(Calendar.MONTH) + 1,
            today.get(Calendar.DAY_OF_MONTH)
        )
    }

    val todayMeetings: StateFlow<List<MeetingWithDetail>> =
        meetingRepository
            .observeMeetingsByDate(todayGregorianDate)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
}
