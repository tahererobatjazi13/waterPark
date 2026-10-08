package ir.kitgroup.partnerManagement.feature.offer.ui.offer_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.dao.OfferDao
import ir.kitgroup.partnerManagement.core.database.entity.OfferEntity
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.usecase.CheckCanAddMeetingUseCase
import ir.kitgroup.partnerManagement.core.usecase.CheckCanAddOfferUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfferListViewModel @Inject constructor(
    offerDao: OfferDao,
    private val checkCanAddOfferUseCase: CheckCanAddOfferUseCase

) : ViewModel() {


    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    fun checkCanNavigateToAddOffer(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val error = checkCanAddOfferUseCase()
            if (error != null) {
                _uiEvent.emit(UiEvent.ShowError(error))
            } else {
                onSuccess()
            }
        }
    }

    val offers: StateFlow<List<OfferEntity>> = offerDao.observeAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}
