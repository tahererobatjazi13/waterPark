package ir.kitgroup.partnerManagement.feature.offer.ui.offer_plan_list.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.entity.OfferEntity
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_plan_list.repository.OfferPlanRepository
import ir.kitgroup.partnerManagement.core.ui.util.AppErrorMapper
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.usecase.CheckCanAddOfferUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfferPlanListViewModel @Inject constructor(
    offerPlanRepository: OfferPlanRepository,
    private val checkCanAddOfferUseCase: CheckCanAddOfferUseCase
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    val offers: StateFlow<List<OfferEntity>?> = offerPlanRepository.observeAll()
        .catch { throwable ->
            if (throwable is CancellationException) throw throwable
            _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(throwable)))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    fun checkCanNavigateToAddOffer(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val error = checkCanAddOfferUseCase()

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
                            .DynamicString("بررسی امکان ثبت آفر ناموفق بود")
                    )
                )
            }
        }
    }
}
