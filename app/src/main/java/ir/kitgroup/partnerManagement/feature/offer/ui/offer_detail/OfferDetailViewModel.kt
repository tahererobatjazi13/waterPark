package ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.dao.OfferDao
import ir.kitgroup.partnerManagement.core.database.dao.OfferDetailDao
import ir.kitgroup.partnerManagement.core.database.entity.OfferEntity
import ir.kitgroup.partnerManagement.core.database.model.OfferDetailWithProduct
import ir.kitgroup.partnerManagement.core.ui.util.AppErrorMapper
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail_plan_list.repository.OfferDetailPlanRepository
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_plan_list.add.AddEditOfferPlanUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class OfferDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    offerDetailDao: OfferDetailDao,
    offerDao: OfferDao,
    private val offerDetailPlanRepository: OfferDetailPlanRepository,
) : ViewModel() {

    val offerDetailId: String = checkNotNull(savedStateHandle["offerDetailId"])

    val offerDetail: StateFlow<OfferDetailWithProduct?> =
        offerDetailDao.observeOfferDetailById(offerDetailId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )


    val offer: StateFlow<OfferEntity?> = offerDetail
        .flatMapLatest { detailWithProduct ->
            val offerId = detailWithProduct?.offerDetail?.offerId
            if (offerId != null) {
                offerDao.observeOfferById(offerId)
            } else {
                flowOf(null)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )


    private val _uiState = MutableStateFlow(AddEditOfferPlanUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    fun deleteOfferDetail(onSuccessCallback: () -> Unit) {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val result = offerDetailPlanRepository.deleteOfferDetailPlan(offerDetailId)

                result.fold(
                    onSuccess = { serverMessage ->
                        val uiMessage = if (!serverMessage.isNullOrBlank()) {
                            UiText.DynamicString(serverMessage)
                        } else {
                            UiText.StringResource(R.string.msg_offer_detail_plan_deleted)
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
