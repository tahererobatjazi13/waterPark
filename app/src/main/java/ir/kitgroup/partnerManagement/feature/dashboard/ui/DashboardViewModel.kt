package ir.kitgroup.partnerManagement.feature.dashboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.dao.AssignStandChangeStatusDao
import ir.kitgroup.partnerManagement.core.database.dao.AssignedStandDao
import ir.kitgroup.partnerManagement.core.database.dao.ContractDao
import ir.kitgroup.partnerManagement.core.database.dao.ContractTerminationDao
import ir.kitgroup.partnerManagement.core.database.dao.MeetingChangeStatusDao
import ir.kitgroup.partnerManagement.core.database.dao.MeetingDao
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationChangeStatusDao
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationDao
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationPersonDao
import ir.kitgroup.partnerManagement.core.database.dao.StatisticDao
import ir.kitgroup.partnerManagement.core.database.dao.WarningDao
import ir.kitgroup.partnerManagement.core.database.entity.StatisticEntity
import ir.kitgroup.partnerManagement.core.database.model.MeetingWithDetail
import ir.kitgroup.partnerManagement.core.repository.MeetingRepository
import ir.kitgroup.partnerManagement.core.ui.util.DataState
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.execute
import ir.kitgroup.partnerManagement.core.usecase.CheckCanAddContractUseCase
import ir.kitgroup.partnerManagement.core.usecase.CheckCanAddMeetingUseCase
import ir.kitgroup.partnerManagement.core.usecase.CheckCanAddOfferUseCase
import ir.kitgroup.partnerManagement.core.usecase.CheckCanAddOrganizationUseCase
import ir.kitgroup.partnerManagement.feature.dashboard.repository.SyncRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    organizationDao: OrganizationDao,
    organizationPersonDao: OrganizationPersonDao,
    contractDao: ContractDao,
    warningDao: WarningDao,
    meetingDao: MeetingDao,
    assignedStandDao: AssignedStandDao,
    organizationChangeStatusDao: OrganizationChangeStatusDao,
    contractTerminationDao: ContractTerminationDao,
    assignStandChangeStatusDao: AssignStandChangeStatusDao,
    meetingChangeStatusDao: MeetingChangeStatusDao,
    private val syncRepository: SyncRepository,
    meetingRepository: MeetingRepository,
    statisticDao: StatisticDao,
    private val checkCanAddOrganizationUseCase: CheckCanAddOrganizationUseCase,
    private val checkCanAddContractUseCase: CheckCanAddContractUseCase,
    private val checkCanAddMeetingUseCase: CheckCanAddMeetingUseCase,
    private val checkCanAddOfferUseCase: CheckCanAddOfferUseCase,
) : ViewModel() {

    val syncState = MutableStateFlow<DataState<Unit>>(DataState.Idle)
    val uploadState = MutableStateFlow<DataState<Unit>>(DataState.Idle)

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    val pendingUploadCount: StateFlow<Int> = combine(
        listOf(
            organizationDao.observePendingCount(),
            organizationPersonDao.observePendingCount(),
            contractDao.observePendingCount(),
            warningDao.observePendingCount(),
            meetingDao.observePendingCount(),
            assignedStandDao.observePendingCount(),
            organizationChangeStatusDao.observePendingCount(),
            contractTerminationDao.observePendingCount(),
            assignStandChangeStatusDao.observePendingCount(),
            meetingChangeStatusDao.observePendingCount()
        )
    ) { counts ->
        counts.sum()
    }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    fun onReceiveDataClick() {
        execute(
            stateFlow = syncState,
            eventFlow = _uiEvent,
            successMessageRes = R.string.msg_data_synced_successfully,
            action = {
                // مپ کردن خروجی به Result<Unit> جهت سازگاری کامل با اکستنشن execute
                syncRepository.syncData().map { }
            }
        )
    }

    fun onSendDataClick() {
        // همان منطق همگام‌سازی یا فراخوانی اکشن اختصاصی ارسال
        execute(
            stateFlow = uploadState,
            eventFlow = _uiEvent,
            successMessageRes = R.string.msg_data_synced_successfully,
            action = {
                syncRepository.syncData().map { }
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

    fun checkCanNavigateToAddContract(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val error = checkCanAddContractUseCase()
            if (error != null) {
                _uiEvent.emit(UiEvent.ShowError(error))
            } else {
                onSuccess()
            }
        }
    }

    fun checkCanNavigateToAddMeeting(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val error = checkCanAddMeetingUseCase()
            if (error != null) {
                _uiEvent.emit(UiEvent.ShowError(error))
            } else {
                onSuccess()
            }
        }
    }

    fun checkCanNavigateToAddOffer(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val error = checkCanAddOfferUseCase()
            if (error != null) {
                _uiEvent.emit(UiEvent.ShowError(error))
            } else {
                onSuccess()
            }
        }
    }
}
