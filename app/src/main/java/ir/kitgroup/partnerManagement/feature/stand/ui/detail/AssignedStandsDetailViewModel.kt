package ir.kitgroup.partnerManagement.feature.stand.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.model.AssignedStandWithDetail
import ir.kitgroup.partnerManagement.core.database.model.MeetingWithDetail
import ir.kitgroup.partnerManagement.core.repository.AssignedStandsRepository
import ir.kitgroup.partnerManagement.core.ui.util.AppErrorMapper
import ir.kitgroup.partnerManagement.core.ui.util.DataState
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationStatus
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import ir.kitgroup.partnerManagement.core.ui.util.isNetworkError
import ir.kitgroup.partnerManagement.feature.organization.model.ChangeStatusOrganizationRequest
import ir.kitgroup.partnerManagement.feature.organization.ui.detail.OrganizationDetailUiState
import ir.kitgroup.partnerManagement.feature.stand.model.ChangeAssignStandStatusRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class AssignedStandsDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val assignedStandsRepository: AssignedStandsRepository
) : ViewModel() {

    private val assignedStandId: String = checkNotNull(savedStateHandle["assignedStandId"])

    val standDetail: StateFlow<AssignedStandWithDetail?> = assignedStandsRepository
        .observeByIdWithDetail(assignedStandId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )



    private val _uiStateChange = MutableStateFlow(AssignedStandDetailUiState())
    val uiStateChange: StateFlow<AssignedStandDetailUiState> = _uiStateChange.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    fun changeStatusAssignStand(
     /*   standAssignmentId: String,
        organizationId: String,*/
        newStatusId: Int,
      /*  actualReturnDate: String? = null,
        deliveryToOrgDate: String? = null,*/
        onErrorCallback: (() -> Unit)? = null,
        onSuccessCallback: () -> Unit
    ) {
        if (_uiStateChange.value.isChangeStatus) return

        viewModelScope.launch {
            _uiStateChange.update { it.copy(isChangeStatus = true) }

            try {
                val currentDetail = standDetail.value?.assignedStand

                val request = ChangeAssignStandStatusRequest(
                    standAssignmentId = currentDetail!!.assignedStandId,
                    organizationId = currentDetail.organizationId!!,
                    status = newStatusId,
                    actualReturnDate = currentDetail.actualReturnDate,
                    deliveryToOrgDate = currentDetail.deliveryToOrgDate
                )

                val result = assignedStandsRepository.changeStatusAssignStand(request)

                result.fold(
                    onSuccess = { serverMessage ->
                        val uiMessage = if (!serverMessage.isNullOrBlank()) {
                            UiText.DynamicString(serverMessage)
                        } else {
                            UiText.StringResource(R.string.msg_change_status_success)
                        }

                        _uiEvent.emit(UiEvent.ShowMessage(uiMessage))
                        delay(500)
                        onSuccessCallback()
                    },
                    onFailure = { error ->
                        if (error.isNetworkError()) {

                            // تنها در صورت قطعی واقعی اینترنت در دیتابیس لوکال کش می‌شود
                            try {
                                assignedStandsRepository.saveOfflineAssignStandStatusChange(
                                    standAssignmentId = currentDetail.assignedStandId,
                                    organizationId = currentDetail.organizationId,
                                    status = newStatusId,
                                    actualReturnDate =  currentDetail.actualReturnDate!!,
                                    deliveryToOrgDate = currentDetail.deliveryToOrgDate!!
                                )

                                _uiEvent.emit(
                                    UiEvent.ShowMessage(
                                        UiText.StringResource(R.string.msg_changes_saved_offline_synchronized_connection)
                                    )
                                )
                                delay(500)
                                onSuccessCallback()
                            } catch (dbError: Exception) {
                               onErrorCallback?.invoke()
                                _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(dbError)))
                            }
                        } else {
                           onErrorCallback?.invoke()
                            _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(error)))
                        }
                    }
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(e)))
            } finally {
                _uiStateChange.update { it.copy(isChangeStatus = false) }
            }
        }
    }

}
