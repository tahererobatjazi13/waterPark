package ir.kitgroup.partnerManagement.feature.organization.ui.detail.meeting

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.model.MeetingWithDetail
import ir.kitgroup.partnerManagement.core.repository.MeetingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class OrganizationMeetingsViewModel @Inject constructor(
    private val meetingRepository: MeetingRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val organizationId: String? = savedStateHandle["organizationId"]

    private val _meetings = MutableStateFlow<List<MeetingWithDetail>>(emptyList())
    val meetings: StateFlow<List<MeetingWithDetail>> = _meetings.asStateFlow()

    fun observeOrganizationContracts(orgId: String) {
        meetingRepository.observeMeetingsByOrganization(orgId)
            .onEach { meetingList ->
                _meetings.value = meetingList
            }
            .launchIn(viewModelScope)
    }
}
