package ir.kitgroup.partnerManagement.feature.meeting.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationDao
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationPersonDao
import ir.kitgroup.partnerManagement.core.database.dao.VisitSubjectDao
import ir.kitgroup.partnerManagement.core.database.entity.MeetingEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationPersonEntity
import ir.kitgroup.partnerManagement.core.repository.MeetingRepository
import ir.kitgroup.partnerManagement.core.ui.util.jalaliToGregorian
import ir.kitgroup.partnerManagement.feature.meeting.model.CreateMeetingRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMeetingViewModel @Inject constructor(
    private val organizationDao: OrganizationDao,
    private val visitSubjectDao: VisitSubjectDao,
    private val organizationPersonDao: OrganizationPersonDao,
    private val meetingRepository: MeetingRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val meetingId: String? =
        savedStateHandle["meetingId"]

    val preselectedOrganizationId: String? =
        savedStateHandle["preselectedOrganizationId"]

    val isEditMode: Boolean =
        !meetingId.isNullOrBlank()

    private val _isSaving =
        MutableStateFlow(false)

    val isSaving: StateFlow<Boolean> =
        _isSaving.asStateFlow()

    private val _errorMessage =
        MutableStateFlow<String?>(null)

    val errorMessage: StateFlow<String?> =
        _errorMessage.asStateFlow()

    private val _saveSuccess =
        MutableSharedFlow<String>(
            extraBufferCapacity = 1
        )

    val saveSuccess =
        _saveSuccess.asSharedFlow()

    private val meetingFlow =
        if (!meetingId.isNullOrBlank()) {
            meetingRepository.observeMeetingById(
                meetingId!!
            )
        } else {
            flowOf(null)
        }

    val uiState: StateFlow<AddMeetingUiState> =
        combine(
            organizationDao.observeAll(),
            visitSubjectDao.observeAll(),
            meetingFlow
        ) { organizations, visitSubjects, meeting ->

            val effectiveOrganizationId =
                meeting?.meeting?.organizationId
                    ?: preselectedOrganizationId

            val preselectedOrganization =
                organizations.firstOrNull {
                    it.organization.organizationId ==
                            effectiveOrganizationId
                }

            AddMeetingUiState(
                organizations = organizations,
                visitSubjects = visitSubjects,
                preloadedOrganization =
                preselectedOrganization,
                existingMeeting =
                meeting,
                isLoading = false
            )

        }.stateIn(
            scope = viewModelScope,
            started =
            SharingStarted.WhileSubscribed(5_000),
            initialValue =
            AddMeetingUiState()
        )

    fun createMeeting(
        meeting: MeetingEntity
    ) {

        if (_isSaving.value) {
            return
        }

        // -----------------------------
        // Validation
        // -----------------------------

        if (meeting.organizationId.isNullOrBlank()) {

            _errorMessage.value =
                "لطفاً سازمان را انتخاب کنید."

            return
        }

        if (meeting.subjectVisitId.isNullOrBlank()) {

            _errorMessage.value =
                "لطفاً موضوع بازدید را انتخاب کنید."
            return
        }

        viewModelScope.launch {

            _isSaving.value = true
            _errorMessage.value = null

            try {

                val request =
                    CreateMeetingRequest(

                        meetingId =
                        if (isEditMode) {
                            meetingId
                        } else {
                            null
                        },

                        organizationId =
                        meeting.organizationId,
                        organizationPersonId =
                        meeting.organizationPersonId,

                        visitorId =
                        meeting.visitorId,

                        subjectVisitId =
                        meeting.subjectVisitId,

                        description =
                        meeting.description,

                        visitDate =
                        jalaliToGregorian(
                            meeting.visitDate.orEmpty()
                        ),

                        status =
                        meeting.status!!,

                        type =
                        meeting.type!!,

                        latitude =
                        meeting.latitude
                            ?.replace(",", ".")
                            ?.toDoubleOrNull(),

                        longitude =
                        meeting.longitude
                            ?.replace(",", ".")
                            ?.toDoubleOrNull()
                    )

                Log.d(
                    "MeetingRequest",
                    request.toString()
                )

                val result =
                    if (isEditMode) {

                        // ویرایش
                        meetingRepository.updateMeeting(
                            request
                        )

                    } else {

                        // افزودن
                        meetingRepository.createMeeting(
                            request
                        )
                    }

                result
                    .onSuccess { response ->

                        Log.d(
                            "MeetingResponse",
                            response.toString()
                        )

                        _saveSuccess.emit(
                            response.message
                                ?: if (isEditMode) {
                                    "بازدید با موفقیت ویرایش شد."
                                } else {
                                    "بازدید با موفقیت ثبت شد."
                                }
                        )
                    }
                    .onFailure { error ->

                        Log.e(
                            "MeetingSave",
                            "خطا در ذخیره بازدید",
                            error
                        )

                        _errorMessage.value =
                            error.message
                                ?: if (isEditMode) {
                                    "خطا در ویرایش بازدید"
                                } else {
                                    "خطا در ثبت بازدید"
                                }
                    }

            } catch (e: Exception) {

                Log.e(
                    "MeetingSave",
                    "Exception",
                    e
                )

                _errorMessage.value =
                    e.message
                        ?: "خطای نامشخص در ذخیره بازدید"

            } finally {

                _isSaving.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
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