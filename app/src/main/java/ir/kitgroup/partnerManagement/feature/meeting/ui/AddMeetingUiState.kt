package ir.kitgroup.partnerManagement.feature.meeting.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationDao
import ir.kitgroup.partnerManagement.core.database.model.MeetingWithDetail
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import ir.kitgroup.partnerManagement.core.repository.MeetingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class AddMeetingUiState(
    val organizations: List<OrganizationWithDetail> = emptyList(),
    val preloadedOrganization: OrganizationWithDetail? = null,
    val existingMeeting: MeetingWithDetail? = null,
    val isLoading: Boolean = true
)

@HiltViewModel
class AddMeetingViewModel @Inject constructor(
    private val organizationDao: OrganizationDao,
    private val meetingRepository: MeetingRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val meetingId: String? = savedStateHandle["meetingId"]
    val preselectedOrganizationId: String? = savedStateHandle["preselectedOrganizationId"]

    private val meetingFlow = if (meetingId != null) {
        meetingRepository.observeMeetingById(meetingId)
    } else {
        flowOf(null)
    }

    val uiState: StateFlow<AddMeetingUiState> = combine(
        organizationDao.observeAll(),
        meetingFlow
    ) { organizations, meeting ->
        val effectiveOrgId = meeting?.meeting?.organizationId ?: preselectedOrganizationId
        val preselected = organizations.find { it.organization.organizationId == effectiveOrgId }

        AddMeetingUiState(
            organizations = organizations,
            preloadedOrganization = preselected,
            existingMeeting = meeting,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AddMeetingUiState()
    )
}
