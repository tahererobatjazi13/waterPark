package ir.kitgroup.partnerManagement.feature.meeting.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.model.MeetingWithDetail
import ir.kitgroup.partnerManagement.core.repository.MeetingRepository
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

    fun observeMeetingById(meetingId: String) {
        viewModelScope.launch {
            meetingRepository
                .observeMeetingById(meetingId)
                .collect { meeting ->
                    _meeting.value = meeting
                }
        }
    }
}