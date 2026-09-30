package ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail_list

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
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class OfferDetailsListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    offerDao: OfferDao,
    offerDetailDao: OfferDetailDao
) : ViewModel() {

    val offerId: String = checkNotNull(savedStateHandle["offerId"])

    val headerOffer: StateFlow<OfferEntity?> = offerDao.observeById(offerId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val offerLines: StateFlow<List<OfferDetailWithProduct>> = offerDetailDao.observeWithProductByOfferId(offerId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}
