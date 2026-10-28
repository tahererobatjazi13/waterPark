package ir.kitgroup.partnerManagement.feature.meeting.ui.add

import ir.kitgroup.partnerManagement.core.database.entity.OrganizationPersonEntity
import ir.kitgroup.partnerManagement.core.database.entity.VisitSubjectEntity
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import ir.kitgroup.partnerManagement.core.ui.model.OrganizationImageItem
import ir.kitgroup.partnerManagement.core.ui.util.MeetingStatus
import ir.kitgroup.partnerManagement.core.ui.util.MeetingType

data class VisitorItem(
    val id: String,
    val fullName: String
)

data class AddEditMeetingUiState(
    // فیلدهای فرم
    val meetingId: String? = null,
    val organizationId: String = "",
    val organizationName: String = "",
    val subjectVisitId: String = "",
    val organizationPersonId: String? = null,
    val organizationPersonName: String = "",
    val visitorId: String? = null,
    val visitorName: String = "",
    val visitType: MeetingType = MeetingType.PHONE,
    val status: MeetingStatus = MeetingStatus.PLANNED,
    val visitScheduledDate: String = "",
    val visitScheduledTime: String = "",
    val visitRealDate: String = "",
    val visitRealTime: String = "",
    val description: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val images: List<OrganizationImageItem> = emptyList(),

    // داده‌های Dropdown و کمکی
    val organizations: List<OrganizationWithDetail> = emptyList(),
    val visitSubjects: List<VisitSubjectEntity> = emptyList(),
    val organizationPersons: List<OrganizationPersonEntity> = emptyList(),
    val allVisitors: List<VisitorItem> = emptyList(),

    // وضعیت‌های صفحه و نقش
    val isSupervisor: Boolean = false,
    val isEditMode: Boolean = false,
    val isLoading: Boolean = false,
    val isInitialLoading: Boolean = false
) {
    val isScheduledInPersonVisit: Boolean
        get() = visitType == MeetingType.PLANNED_IN_PERSON

    val isUnscheduledInPersonVisit: Boolean
        get() = visitType == MeetingType.UNPLANNED_IN_PERSON

    val isFormValid: Boolean
        get() {
            val isInPerson = visitType == MeetingType.PLANNED_IN_PERSON ||
                    visitType == MeetingType.UNPLANNED_IN_PERSON

            val isLocationValid = !isInPerson || (!latitude!!.isNaN()&& !longitude!!.isNaN())

            return organizationId.isNotBlank() &&
                    subjectVisitId.isNotBlank() &&
                    (!isSupervisor || !visitorId.isNullOrBlank()) &&
                    isLocationValid
        }

}
