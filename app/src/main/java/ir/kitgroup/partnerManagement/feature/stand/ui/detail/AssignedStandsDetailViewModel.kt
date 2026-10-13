package ir.kitgroup.partnerManagement.feature.stand.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.model.AssignedStandWithDetail
import ir.kitgroup.partnerManagement.core.database.model.MeetingWithDetail
import ir.kitgroup.partnerManagement.core.repository.AssignedStandsRepository
import ir.kitgroup.partnerManagement.core.ui.util.DataState
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import ir.kitgroup.partnerManagement.feature.stand.model.ChangeAssignStandStatusRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

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


    private val _meeting = MutableStateFlow<MeetingWithDetail?>(null)
    val meeting: StateFlow<MeetingWithDetail?> = _meeting.asStateFlow()

    private val _isUpdatingStatus = MutableStateFlow(false)
    val isUpdatingStatus: StateFlow<Boolean> = _isUpdatingStatus.asStateFlow()

    private val _statusUpdateState = MutableStateFlow<DataState<Unit>?>(null)
    val statusUpdateState: StateFlow<DataState<Unit>?> = _statusUpdateState.asStateFlow()


  fun changeStatusAssignStand(
      newStatusId: Int,
      actualReturnDate: String? = null,
      onSuccess: () -> Unit = {}
    ) {
        if (_isUpdatingStatus.value) return

        viewModelScope.launch {
            _isUpdatingStatus.value = true
            _statusUpdateState.value = DataState.Loading

            try {
                val currentDetail = standDetail.value?.assignedStand

                val request = ChangeAssignStandStatusRequest(
                    standAssignmentId = currentDetail!!.assignedStandId,
                    organizationId = currentDetail.organizationId!!,
                    status = newStatusId,
                    actualReturnDate = actualReturnDate,
                    deliveryToOrgDate = currentDetail.deliveryToOrgDate
                )
                val result = assignedStandsRepository.changeStatusAssignStand(request)

                result.onSuccess {
                    _statusUpdateState.value = DataState.Success(Unit)
                    onSuccess()
                }.onFailure { error ->
                    _statusUpdateState.value = DataState.Error(
                        UiText.DynamicString(error.localizedMessage ?: "خطا در تغییر وضعیت استند")
                    )
                }
            } catch (e: Exception) {
                _statusUpdateState.value = DataState.Error(
                    UiText.DynamicString(e.localizedMessage ?: "خطای ناشناخته رخ داده است")
                )
            } finally {
                _isUpdatingStatus.value = false
            }
        }
    }









    fun resetStatusState() {
        _statusUpdateState.value = null
    }
}
