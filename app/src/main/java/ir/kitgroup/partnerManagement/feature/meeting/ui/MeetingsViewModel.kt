package ir.kitgroup.partnerManagement.feature.meeting.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.model.MeetingWithDetail
import ir.kitgroup.partnerManagement.core.repository.MeetingRepository
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.usecase.CheckCanAddMeetingUseCase
import ir.kitgroup.partnerManagement.core.usecase.CheckCanAddOrganizationUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeetingsViewModel @Inject constructor(
    private val meetingRepository: MeetingRepository,
    private val checkCanAddMeetingUseCase: CheckCanAddMeetingUseCase

) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()
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
    val allMeetings: StateFlow<List<MeetingWithDetail>?> =
        meetingRepository.observeAllMeetings()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )


    fun deleteMeeting(meetingId: String) {
        viewModelScope.launch {
            meetingRepository.deleteMeeting(meetingId)
        }
    }
}

