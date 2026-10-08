package ir.kitgroup.partnerManagement.feature.meeting.ui

import ir.kitgroup.partnerManagement.core.database.entity.VisitSubjectEntity
import ir.kitgroup.partnerManagement.core.database.model.MeetingWithDetail
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail

data class AddMeetingUiState(
    val organizations: List<OrganizationWithDetail> = emptyList(),
    val visitSubjects: List<VisitSubjectEntity> = emptyList(),
    val preloadedOrganization: OrganizationWithDetail? = null,
    val existingMeeting: MeetingWithDetail? = null,
    val isLoading: Boolean = true
)