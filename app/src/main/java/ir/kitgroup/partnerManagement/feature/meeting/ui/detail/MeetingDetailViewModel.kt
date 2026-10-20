package ir.kitgroup.partnerManagement.feature.meeting.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.model.MeetingWithDetail
import ir.kitgroup.partnerManagement.core.repository.MeetingRepository
import ir.kitgroup.partnerManagement.core.ui.util.DataState
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import ir.kitgroup.partnerManagement.feature.meeting.model.ChangeStatusMeetingRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeetingDetailViewModel @Inject constructor(
    private val meetingRepository: MeetingRepository
) : ViewModel() {

    private val _meeting = MutableStateFlow<MeetingWithDetail?>(null)
    val meeting: StateFlow<MeetingWithDetail?> = _meeting.asStateFlow()

    private val _isUpdatingStatus = MutableStateFlow(false)
    val isUpdatingStatus: StateFlow<Boolean> = _isUpdatingStatus.asStateFlow()

    private val _statusUpdateState = MutableStateFlow<DataState<Unit>?>(null)
    val statusUpdateState: StateFlow<DataState<Unit>?> = _statusUpdateState.asStateFlow()

    fun observeMeetingById(meetingId: String) {
        viewModelScope.launch {
            meetingRepository
                .observeMeetingById(meetingId)
                .collect { meeting ->
                    _meeting.value = meeting
                }
        }
    }


    /**
     * تغییر وضعیت جلسه/بازدید با دریافت بدنه کامل درخواست
     */

    /**
     * تغییر وضعیت جلسه/بازدید با استفاده از مقادیر پایه
     */
    fun changeStatusMeeting(
        meetingId: String,
        newStatus: Int,
        onSuccess: () -> Unit = {}
    ) {
        val currentMeeting = _meeting.value?.meeting
        val request = ChangeStatusMeetingRequest(
            meetingId = meetingId,
            status = newStatus,
            visitRealDate = currentMeeting?.visitRealDate,
            latitude = currentMeeting?.latitude?.replace(",", ".")?.toDoubleOrNull(),
            longitude = currentMeeting?.longitude?.replace(",", ".")?.toDoubleOrNull(),
            description = currentMeeting?.description
        )
        updateMeetingStatus(request, onSuccess)
    }

    /**
     * تغییر وضعیت جلسه/بازدید با دریافت بدنه کامل درخواست
     */
    fun updateMeetingStatus(
        request: ChangeStatusMeetingRequest,
        onSuccess: () -> Unit
    ) {
        if (_isUpdatingStatus.value) return

        viewModelScope.launch {
            _isUpdatingStatus.value = true
            _statusUpdateState.value = DataState.Loading

            try {
                val result = meetingRepository.changeStatusMeeting(request)

                result.onSuccess {
                    _statusUpdateState.value = DataState.Success(Unit)
                    request.meetingId?.let { id -> observeMeetingById(id) }
                    onSuccess()
                }.onFailure { error ->
                    _statusUpdateState.value = DataState.Error(
                        UiText.DynamicString(error.localizedMessage ?: "خطا در تغییر وضعیت")
                    )
                }
            } catch (e: Exception) {
                _statusUpdateState.value = DataState.Error(
                    UiText.DynamicString(e.localizedMessage ?: "خطای ناشناخته")
                )
            } finally {
                _isUpdatingStatus.value = false
            }
        }
    }


    fun resetStatusState() {
        _statusUpdateState.value = null
    }
}