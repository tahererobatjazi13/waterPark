package ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail_plan_list.list

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.dao.OfferDao
import ir.kitgroup.partnerManagement.core.database.dao.OfferDetailDao
import ir.kitgroup.partnerManagement.core.database.entity.OfferEntity
import ir.kitgroup.partnerManagement.core.database.model.OfferDetailWithProduct
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail_plan_list.repository.OfferDetailPlanRepository
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_plan_list.repository.OfferPlanRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfferDetailPlanListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val offerDetailPlanRepository: OfferDetailPlanRepository,
    offerPlanRepository: OfferPlanRepository,
) : ViewModel() {

    val offerId: String = checkNotNull(savedStateHandle["offerId"])

    val headerOffer: StateFlow<OfferEntity?> = offerPlanRepository.observeOfferById(offerId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )


    /*   val headerOffer: StateFlow<OfferEntity?> = offerPlanRepository.getOfferById(offerId)
           .stateIn(
               scope = viewModelScope,
               started = SharingStarted.WhileSubscribed(5_000),
               initialValue = null
           )*/

    private val _offerDetails = MutableStateFlow<List<OfferDetailWithProduct>>(emptyList())
    val offerDetails: StateFlow<List<OfferDetailWithProduct>> = _offerDetails.asStateFlow()


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadOfferDetails()
    }

    fun loadOfferDetails() {
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            offerDetailPlanRepository.observeOfferDetailById(offerId) // ارسال شناسه آفر
                .catch { throwable ->
                    Log.e("OfferDetailsVM", "Error collecting offer details", throwable)
                    _errorMessage.value = throwable.localizedMessage ?: "خطا در دریافت لیست آفرها"
                    _isLoading.value = false
                }
                .collect { lines ->
                    _offerDetails.value = lines
                    _isLoading.value = false
                }
        }
    }


}
