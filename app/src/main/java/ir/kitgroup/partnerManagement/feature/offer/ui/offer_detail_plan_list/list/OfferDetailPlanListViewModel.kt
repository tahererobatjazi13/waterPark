package ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail_plan_list.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.entity.OfferEntity
import ir.kitgroup.partnerManagement.core.database.model.OfferDetailWithProduct
import ir.kitgroup.partnerManagement.core.ui.util.AppErrorMapper
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail_plan_list.repository.OfferDetailPlanRepository
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_plan_list.repository.OfferPlanRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class OfferDetailPlanListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    offerDetailPlanRepository: OfferDetailPlanRepository,
    offerPlanRepository: OfferPlanRepository,
) : ViewModel() {

    val offerId: String = checkNotNull(savedStateHandle["offerId"])

    val headerOffer: StateFlow<OfferEntity?> = offerPlanRepository.observeOfferById(offerId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    val offerDetails: StateFlow<List<OfferDetailWithProduct>?> =
        offerDetailPlanRepository.observeOfferDetailWithProductByOfferId(offerId)
            .catch { throwable ->
                if (throwable is CancellationException) throw throwable
                _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(throwable)))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )
}
