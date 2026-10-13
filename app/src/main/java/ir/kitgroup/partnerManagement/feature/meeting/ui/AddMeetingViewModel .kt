package ir.kitgroup.partnerManagement.feature.meeting.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationDao
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationPersonDao
import ir.kitgroup.partnerManagement.core.database.dao.VisitSubjectDao
import ir.kitgroup.partnerManagement.core.database.dao.VisitorDao
import ir.kitgroup.partnerManagement.core.database.entity.MeetingEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationPersonEntity
import ir.kitgroup.partnerManagement.core.repository.MeetingRepository
import ir.kitgroup.partnerManagement.core.ui.util.datastore.MainPreferences
import ir.kitgroup.partnerManagement.core.ui.util.jalaliToGregorian
import ir.kitgroup.partnerManagement.feature.meeting.model.CreateMeetingRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMeetingViewModel @Inject constructor(
    private val organizationDao: OrganizationDao,
    private val visitSubjectDao: VisitSubjectDao,
    private val organizationPersonDao: OrganizationPersonDao,
    private val meetingRepository: MeetingRepository,
    private val mainPreferences: MainPreferences,
    private val visitorDao: VisitorDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val meetingId: String? =
        savedStateHandle["meetingId"]

    val preselectedOrganizationId: String? = savedStateHandle.get<String>("organizationId")
        ?.takeIf { it.isNotBlank() && it != "null" && it != "{organizationId}" }

    val isEditMode: Boolean =
        !meetingId.isNullOrBlank()

    private val _isSaving =
        MutableStateFlow(false)

    val isSaving: StateFlow<Boolean> =
        _isSaving.asStateFlow()

    private val _currentVisitorId = MutableStateFlow("")
    private val _currentVisitorName = MutableStateFlow("")
    private val _allVisitors = MutableStateFlow<List<VisitorItem>>(emptyList())

    init {
        loadUserSessionInfo()
    }

    private fun loadUserSessionInfo() {
        viewModelScope.launch {
            _currentVisitorId.value = mainPreferences.getVisitorId()
        }
    }

    // دریافت لیست واقعی بازاریاب‌ها از دیتابیس و تبدیل به VisitorItem
    private val allVisitorsFlow = visitorDao.observeAll().map { entities ->
        entities.map { entity ->
            VisitorItem(
                id = entity.visitorId,
                fullName = entity.name ?: entity.name.orEmpty()
            )
        }
    }

    // تعریف errorMessage به صورت SharedFlow
    private val _errorMessage = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val errorMessage: SharedFlow<String> = _errorMessage.asSharedFlow()

    private val _saveSuccess =
        MutableSharedFlow<String>(
            extraBufferCapacity = 1
        )

    val saveSuccess =
        _saveSuccess.asSharedFlow()

    private val meetingFlow =
        if (!meetingId.isNullOrBlank()) {
            meetingRepository.observeMeetingById(
                meetingId
            )
        } else {
            flowOf(null)
        }


    val uiState: StateFlow<AddMeetingUiState> =
        combine(
            organizationDao.observeAll(),
            visitSubjectDao.observeAll(),
            meetingFlow, allVisitorsFlow,

            _currentVisitorId
        ) { organizations, visitSubjects, meeting, allVisitors, sessionVisitorId ->

            val effectiveOrganizationId =
                meeting?.meeting?.organizationId ?: preselectedOrganizationId

            val preselectedOrg = organizations.firstOrNull {
                it.organization.organizationId == effectiveOrganizationId
            }

            val defaultVisitorId = preselectedOrg?.organization?.activeVisitorId
                ?.takeIf { it.isNotBlank() && it != "00000000-0000-0000-0000-000000000000" }
                ?: preselectedOrg?.organization?.visitorId
                    ?.takeIf { it.isNotBlank() && it != "00000000-0000-0000-0000-000000000000" }
                ?: sessionVisitorId
            val defaultVisitorName = allVisitors
                .firstOrNull { it.id == defaultVisitorId }
                ?.fullName
                .orEmpty()

            AddMeetingUiState(
                organizations = organizations,
                visitSubjects = visitSubjects,
                preloadedOrganization = preselectedOrg,
                existingMeeting = meeting,
                allVisitors = allVisitors,
                defaultVisitorId = defaultVisitorId,
                defaultVisitorName = defaultVisitorName,
                isLoading = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AddMeetingUiState()
        )


    fun createMeeting(meeting: MeetingEntity) {
        if (_isSaving.value) return

        // اعتبارسنجی سازمان
        if (meeting.organizationId!!.isBlank()) {
            _errorMessage.tryEmit("لطفاً سازمان را انتخاب کنید.")
            return
        }

        // اعتبارسنجی موضوع بازدید
        if (meeting.subjectVisitId!!.isBlank()) {
            _errorMessage.tryEmit("لطفاً موضوع بازدید را انتخاب کنید.")
            return
        }

        viewModelScope.launch {

            val sessionVisitorId = mainPreferences.getVisitorId()
            val effectiveVisitorId =
                meeting.visitorId.takeIf { !it.isNullOrBlank() } ?: sessionVisitorId
            _isSaving.value = true
            try {
                val request = CreateMeetingRequest(
                    meetingId = if (isEditMode) meetingId else null,
                    organizationId = meeting.organizationId,
                    organizationPersonId = meeting.organizationPersonId,
                    visitorId = effectiveVisitorId,
                    subjectVisitId = meeting.subjectVisitId,
                    description = meeting.description,
                    visitDate = jalaliToGregorian(meeting.visitDate.orEmpty()),
                    visitRealDate = jalaliToGregorian(meeting.visitRealDate.orEmpty()),
                    visitTime = meeting.visitTime,
                    status = meeting.status ?: 0,
                    type = meeting.type ?: 0,
                    latitude = meeting.latitude?.replace(",", ".")?.toDoubleOrNull(),
                    longitude = meeting.longitude?.replace(",", ".")?.toDoubleOrNull()
                )

                Log.d(
                    "MeetingRequest",
                    request.toString()
                )
                val result = if (isEditMode) {
                    meetingRepository.updateMeeting(request)
                } else {
                    meetingRepository.createMeeting(request)
                }

                result.onSuccess { response ->
                    _saveSuccess.emit(
                        response.message
                            ?: if (isEditMode) "بازدید با موفقیت ویرایش شد." else "بازدید با موفقیت ثبت شد."
                    )
                }.onFailure { error ->
                    _errorMessage.emit(
                        error.message
                            ?: if (isEditMode) "خطا در ویرایش بازدید" else "خطا در ثبت بازدید"
                    )
                }
            } catch (e: Exception) {
                _errorMessage.emit(e.message ?: "خطای نامشخص در ذخیره بازدید")
            } finally {
                _isSaving.value = false
            }
        }
    }


    private val _organizationPersons =
        MutableStateFlow<List<OrganizationPersonEntity>>(emptyList())

    val organizationPersons: StateFlow<List<OrganizationPersonEntity>> =
        _organizationPersons.asStateFlow()

    fun loadOrganizationPersons(
        organizationId: String?
    ) {

        if (organizationId.isNullOrBlank()) {
            _organizationPersons.value = emptyList()
            return
        }

        viewModelScope.launch {
            organizationPersonDao
                .observeByOrganizationId(organizationId)
                .collect { persons ->
                    _organizationPersons.value = persons
                }
        }
    }

}