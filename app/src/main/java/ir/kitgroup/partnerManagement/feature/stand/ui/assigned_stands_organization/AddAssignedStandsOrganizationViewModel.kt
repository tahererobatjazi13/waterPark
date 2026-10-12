package ir.kitgroup.partnerManagement.feature.stand.ui.assigned_stands_organization

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.dao.AssignedStandDao
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationDao
import ir.kitgroup.partnerManagement.core.database.dao.StandTypeDao
import ir.kitgroup.partnerManagement.core.database.entity.AssignedStandEntity
import ir.kitgroup.partnerManagement.core.database.model.AssignedStandWithDetail
import ir.kitgroup.partnerManagement.core.repository.AssignedStandsRepository
import ir.kitgroup.partnerManagement.core.ui.util.datastore.MainPreferences
import ir.kitgroup.partnerManagement.core.ui.util.jalaliToGregorian
import ir.kitgroup.partnerManagement.feature.organization.model.CreateOrganizationWarningRequest
import ir.kitgroup.partnerManagement.feature.stand.model.ChangeAssignStandStatusRequest
import ir.kitgroup.partnerManagement.feature.stand.model.CreateAssignStandRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddAssignedStandsOrganizationViewModel @Inject constructor(
    private val organizationDao: OrganizationDao,
    private val standTypeDao: StandTypeDao,
    private val assignedStandDao: AssignedStandDao,
    private val assignedStandsRepository: AssignedStandsRepository,
    private val mainPreferences: MainPreferences,
    savedStateHandle: SavedStateHandle
) : ViewModel() {


    // خواندن پارامترها از SavedStateHandle (تزریق شده از Navigation Graph)
    val preselectedOrganizationId: String? = savedStateHandle["organizationId"]
        ?: savedStateHandle["preselectedOrganizationId"]
    val assignedStandId: String? = savedStateHandle["assignedStandId"]

    val isEditMode: Boolean = !assignedStandId.isNullOrBlank()

    val editStandDetailFlow = MutableStateFlow<AssignedStandWithDetail?>(null)

    init {
        // در صورت وجود شناسه، واکشی جزییات استند تخصیص‌یافته
        val standId = savedStateHandle.get<String>("assignedStandId")
        if (!standId.isNullOrBlank()) {
            loadAssignedStandForEdit(standId)
        }
    }

    private fun loadAssignedStandForEdit(id: String) {
        viewModelScope.launch {
            try {
                assignedStandsRepository.observeByIdWithDetail(id).collect { detail ->
                    editStandDetailFlow.value = detail
                }
            } catch (e: Exception) {
                Log.e("AddStandVM", "Error loading stand detail for edit", e)
            }
        }
    }


    private val organizationsFlow = organizationDao.observeAll()
    private val standsFlow = standTypeDao.observeAll()

    val uiState: StateFlow<AddAssignedStandsOrganizationUiState> =
        combine(
            organizationsFlow,
            standsFlow,
            editStandDetailFlow
        ) { organizations, stands, editDetail ->

            // در حالت ویرایش سازمان مربوط به تخصیص را ست می‌کنیم
            val targetOrgId = editDetail?.assignedStand?.organizationId ?: preselectedOrganizationId
            val preselected = targetOrgId?.let { id ->
                organizations.find { it.organization.organizationId == id }
            }

            AddAssignedStandsOrganizationUiState(
                organizations = organizations,
                preselectedOrganization = preselected,
                stands = stands,
                editStandDetail = editDetail,
                isLoading = false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AddAssignedStandsOrganizationUiState()
        )

    private val _isSaving = kotlinx.coroutines.flow.MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    private val _saveMessage =
        kotlinx.coroutines.flow.MutableSharedFlow<String>()

    val saveMessage = _saveMessage.asSharedFlow()

    fun saveAssignStand(
        standAssignmentId: String? = null,
        advertisingStandId: String,
        organizationId: String,
        meetingId: String? = null,
        count: Int,
        assignmentType: Int,
        assignmentMode: Int,
        assignmentDate: String,
        plannedReturnDate: String? = null,
        deliveryToVisitorDate: String? = null,
        deliveryToOrgDate: String? = null,
        description: String? = null,
        status: Int,
        isEdit: Boolean = false
    ) {
        if (_isSaving.value) return

        viewModelScope.launch {
            _isSaving.value = true

            try {
                // دریافت خودکار visitorId از DataStore/Preferences
                val actualVisitorId = mainPreferences.getVisitorId().ifBlank { null }
                val request = CreateAssignStandRequest(
                    standAssignmentId = standAssignmentId,
                    advertisingStandId = advertisingStandId,
                    visitorId = actualVisitorId,
                    organizationId = organizationId,
                    meetingId = meetingId,
                    count = count,
                    assignmentType = assignmentType,
                    assignmentMode = assignmentMode,
                    assignmentDate = jalaliToGregorian(assignmentDate).toString(),
                    plannedReturnDate = jalaliToGregorian(plannedReturnDate.orEmpty()),
                    deliveryToVisitorDate = jalaliToGregorian(deliveryToVisitorDate.orEmpty()),
                    deliveryToOrgDate = jalaliToGregorian(deliveryToOrgDate.orEmpty()),
                    description = description?.trim()?.ifEmpty { null },
                    status = status
                )

                Log.d("assignedStandsRepositoryRequest", request.toString())

                val isEditMode = isEdit || !standAssignmentId.isNullOrBlank()

                val result = if (isEditMode) {
                    assignedStandsRepository.updateAssignStand(request)
                } else {
                    assignedStandsRepository.createAssignStand(request)
                }

                Log.d("assignedStandsRepositoryResult", result.toString())

                result
                    .onSuccess { response ->
                        _saveMessage.emit(
                            response.message ?: if (isEditMode) "تغییرات با موفقیت ثبت شد." else "تخصیص استند با موفقیت ثبت شد."
                        )
                    }
                    .onFailure { error ->
                        _saveMessage.emit(
                            error.message ?: "عملیات انجام نشد."
                        )
                    }
            } catch (e: Exception) {
                Log.e("saveAssignStand", "Error occurred", e)
                _saveMessage.emit(e.localizedMessage ?: "خطای ناشناخته در ارتباط با سرور.")
            } finally {
                _isSaving.value = false
            }
        }
    }


    fun changeAssignStandStatus(
        request: ChangeAssignStandStatusRequest
    ) {
        viewModelScope.launch {
            val result =
                assignedStandsRepository.changeStatusAssignStand(request)

            result
                .onSuccess { response ->
                    _saveMessage.emit(
                        response.message ?: "وضعیت تخصیص استند تغییر کرد."
                    )
                }
                .onFailure { error ->
                    _saveMessage.emit(
                        error.message ?: "تغییر وضعیت انجام نشد."
                    )
                }
        }
    }

    suspend fun updateLocal(item: AssignedStandEntity) =
        assignedStandDao.update(item)

    suspend fun updateLocalStatus(
        assignedStandId: String,
        status: Int
    ) = assignedStandDao.updateStatus(
        assignedStandId = assignedStandId,
        status = status
    )



}

