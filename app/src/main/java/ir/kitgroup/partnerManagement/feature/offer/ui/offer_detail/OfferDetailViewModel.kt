package ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.dao.OfferDao
import ir.kitgroup.partnerManagement.core.database.dao.OfferDetailDao
import ir.kitgroup.partnerManagement.core.database.entity.OfferDetailEntity
import ir.kitgroup.partnerManagement.core.database.entity.OfferEntity
import ir.kitgroup.partnerManagement.core.database.model.OfferDetailWithProduct
import ir.kitgroup.partnerManagement.core.repository.OfferDetailPlanRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfferDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    offerDetailDao: OfferDetailDao,
    offerDao: OfferDao,
    private val repository: OfferDetailPlanRepository,

    ) : ViewModel() {

    val offerDetailId: String = checkNotNull(savedStateHandle["offerDetailId"])

    val offerDetail: StateFlow<OfferDetailWithProduct?> = offerDetailDao.observeById(offerDetailId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )
    val parentOffer: StateFlow<OfferEntity?> = offerDetail
        .flatMapLatest { detailWithProduct ->
            val offerId = detailWithProduct?.offerDetail?.offerId
            if (offerId != null) {
                offerDao.observeById(offerId)
            } else {
                flowOf(null)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )


    // وضعیت در حال حذف
    private val _isDeleting = MutableStateFlow(false)
    val isDeleting: StateFlow<Boolean> = _isDeleting.asStateFlow()

    // ارسال رویدادهای یک‌باره (پیام خطا یا موفقیت)
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    sealed interface UiEvent {
        data object DeleteSuccess : UiEvent
        data class ShowMessage(val message: String) : UiEvent
    }

    fun deleteOfferDetail() {
        if (_isDeleting.value) return

        viewModelScope.launch {
            _isDeleting.value = true
            repository.deleteOfferDetailLine(offerDetailId)
                .onSuccess {
                    _isDeleting.value = false
                    _eventFlow.emit(UiEvent.DeleteSuccess)
                }
                .onFailure { throwable ->
                    _isDeleting.value = false
                    _eventFlow.emit(
                        UiEvent.ShowMessage(throwable.localizedMessage ?: "خطا در عملیات حذف")
                    )
                }
        }
    }

}
