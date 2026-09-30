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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class OfferDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    offerDetailDao: OfferDetailDao,
    offerDao: OfferDao
) : ViewModel() {

    private val offerDetailId: String = checkNotNull(savedStateHandle["offerDetailId"])

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
}
