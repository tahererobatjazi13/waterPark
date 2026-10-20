package ir.kitgroup.partnerManagement.feature.offer.ui.serial_offer.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.model.AssignedSerialWithDetail
import ir.kitgroup.partnerManagement.core.repository.AssignedSerialsRepository
import ir.kitgroup.partnerManagement.core.ui.util.AppErrorMapper
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_plan_list.add.AddEditOfferPlanUiState
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
class AssignedSerialDetailViewModel @Inject constructor(
    private val assignedSerialsRepository: AssignedSerialsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val serialId: String = checkNotNull(savedStateHandle["serialId"])

    // مشاهده لحظه‌ای جزئیات سریال از دیتابیس لوکال
    val serialDetail: StateFlow<AssignedSerialWithDetail?> =
        assignedSerialsRepository.observeById(serialId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )


    private val _uiState = MutableStateFlow(AddEditOfferPlanUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    fun changeStatusSerialOffer(
        onSuccessCallback: () -> Unit,
        newStatusId: String
    ) {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val result = assignedSerialsRepository.changeStatusSerialOfferC(
                    serialId,
                    newStatusId
                )
                result.fold(
                    onSuccess = { serverMessage ->
                        val uiMessage = if (!serverMessage.isNullOrBlank()) {
                            UiText.DynamicString(serverMessage)
                        } else {
                            UiText.StringResource(R.string.msg_change_status_success)
                        }
                        _uiEvent.emit(UiEvent.ShowMessage(uiMessage))
                        delay(600)
                        onSuccessCallback()
                    },
                    onFailure = { error ->
                        _uiEvent.emit(
                            UiEvent.ShowError(
                                AppErrorMapper.map(error)
                            )
                        )
                    }
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiEvent.emit(
                    UiEvent.ShowError(
                        AppErrorMapper.map(e)
                    )
                )
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
