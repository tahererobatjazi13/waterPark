package ir.kitgroup.partnerManagement.feature.meeting.ui.add

import ir.kitgroup.partnerManagement.core.database.entity.VisitSubjectEntity
import ir.kitgroup.partnerManagement.core.database.model.MeetingWithDetail
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail

data class AddEditMeetingUiState(
    val organizations: List<OrganizationWithDetail> = emptyList(),
    val visitSubjects: List<VisitSubjectEntity> = emptyList(),
    val preloadedOrganization: OrganizationWithDetail? = null,
    val existingMeeting: MeetingWithDetail? = null,
    val isLoading: Boolean = false,
    val isSupervisor: Boolean = false,
    val allVisitors: List<VisitorItem> = emptyList(),
    val defaultVisitorId: String? = null,
    val defaultVisitorName: String? = null
)
data class VisitorItem(
    val id: String,
    val fullName: String
)