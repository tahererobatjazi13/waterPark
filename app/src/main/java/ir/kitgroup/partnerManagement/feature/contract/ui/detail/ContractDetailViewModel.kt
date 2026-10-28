package ir.kitgroup.partnerManagement.feature.contract.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.repository.ContractRepository
import ir.kitgroup.partnerManagement.core.ui.util.AppErrorMapper
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import ir.kitgroup.partnerManagement.core.ui.util.isNetworkError
import ir.kitgroup.partnerManagement.core.ui.util.jalaliToGregorian
import ir.kitgroup.partnerManagement.feature.contract.model.TerminateActiveContractRequest
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException


@HiltViewModel
class ContractDetailViewModel @Inject constructor(
    private val contractRepository: ContractRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val contractId: String = checkNotNull(savedStateHandle["contractId"])

    val contract: StateFlow<ContractEntity?> =
        contractRepository.observeById(contractId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    private val _uiState = MutableStateFlow(ContractDetailUiState())
    val uiState: StateFlow<ContractDetailUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    fun terminateActiveContract(
        contractId: String,
        organizationId: String,
        terminationJalaliDate: String,
        onSuccessCallback: () -> Unit
    ) {
        if (_uiState.value.isTerminating) return

        viewModelScope.launch {
            _uiState.update { it.copy(isTerminating = true) }

            try {
                val gregorianTerminationDate = jalaliToGregorian(terminationJalaliDate).orEmpty()

                val request = TerminateActiveContractRequest(
                    contractId = contractId,
                    organizationId = organizationId,
                    terminationDate = gregorianTerminationDate
                )

                val result = contractRepository.terminateActiveContract(request)

                result.fold(
                    onSuccess = { serverMessage ->
                        val uiMessage = if (!serverMessage.isNullOrBlank()) {
                            UiText.DynamicString(serverMessage)
                        } else {
                            UiText.StringResource(R.string.msg_contract_terminated)
                        }

                        _uiEvent.emit(UiEvent.ShowMessage(uiMessage))
                        delay(500)
                        onSuccessCallback()
                    },
                    onFailure = { error ->
                        if (error.isNetworkError()) {
                            try {
                                contractRepository.saveOfflineTermination(
                                    contractId = contractId,
                                    organizationId = organizationId,
                                    terminationDate = gregorianTerminationDate
                                )

                                _uiEvent.emit(
                                    UiEvent.ShowMessage(
                                        UiText.StringResource(R.string.msg_changes_saved_offline_synchronized_connection)
                                    )
                                )

                                delay(500)
                                onSuccessCallback()
                            } catch (dbError: Exception) {
                                _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(dbError)))
                            }
                        } else {
                            _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(error)))
                        }
                    }
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(e)))
            } finally {
                _uiState.update { it.copy(isTerminating = false) }
            }
        }
    }


}
