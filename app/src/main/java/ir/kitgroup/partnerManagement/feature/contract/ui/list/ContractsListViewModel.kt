package ir.kitgroup.partnerManagement.feature.contract.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.repository.ContractRepository
import ir.kitgroup.partnerManagement.core.ui.util.AppErrorMapper
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.usecase.CheckCanAddContractUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

@HiltViewModel
class ContractsListViewModel @Inject constructor(
    contractRepository: ContractRepository,
    private val checkCanAddContractUseCase: CheckCanAddContractUseCase
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    val contracts: StateFlow<List<ContractEntity>?> = contractRepository.observeAll()
        .catch { throwable ->
            if (throwable is CancellationException) throw throwable
            _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(throwable)))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    fun checkCanAddContractUseCase(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val error = checkCanAddContractUseCase()

                if (error != null) {
                    _uiEvent.emit(UiEvent.ShowError(error))
                } else {
                    onSuccess()
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiEvent.emit(
                    UiEvent.ShowError(
                        ir.kitgroup.partnerManagement.core.ui.util.UiText
                            .DynamicString("بررسی امکان ثبت قراداد ناموفق بود")
                    )
                )
            }
        }
    }
}
