package ir.kitgroup.partnerManagement.feature.offer.ui.offer_plan_list.add

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.util.AppErrorMapper
import ir.kitgroup.partnerManagement.core.ui.util.OfferPlanStatus
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import ir.kitgroup.partnerManagement.core.ui.util.formatJalali
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate
import ir.kitgroup.partnerManagement.core.ui.util.jalaliToGregorian
import ir.kitgroup.partnerManagement.feature.offer.model.OfferPlanRequest
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_plan_list.repository.OfferPlanRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import saman.zamani.persiandate.PersianDate
import javax.inject.Inject

@HiltViewModel
class AddEditOfferPlanViewModel @Inject constructor(
    private val offerPlanRepository: OfferPlanRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val offerId: String? = savedStateHandle["offerId"]

    private val _uiState = MutableStateFlow(AddEditOfferPlanUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        if (!offerId.isNullOrBlank()) {
            loadOffer(offerId)
        } else {
            initDefaultDates()
        }
    }

    private fun initDefaultDates() {
        val today = PersianDate()
        val defaultDate = formatJalali(today.shYear, today.shMonth, today.shDay)

        _uiState.update {
            it.copy(
                fromDate = defaultDate,
                toDate = ""
            )
        }
    }

    private fun loadOffer(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isInitialLoading = true) }
            try {
                val offer = offerPlanRepository.observeOfferById(id).firstOrNull()
                if (offer != null) {
                    _uiState.update {
                        it.copy(
                            offerId = offerId,
                            offerName = offer.title.orEmpty(),
                            fromDate = formatJalaliDate(offer.fromDate),
                            toDate = formatJalaliDate(offer.toDate),
                            status = OfferPlanStatus.fromId(offer.statusOffer),
                            isEditMode = true,
                            isInitialLoading = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(isInitialLoading = false) }
                    _uiEvent.emit(
                        UiEvent.ShowError(UiText.DynamicString("طرح موردنظر پیدا نشد."))
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(isInitialLoading = false) }
                _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(e)))
            }
        }
    }

    fun onNameChange(value: String) {
        _uiState.update { it.copy(offerName = value) }
    }

    fun onFromDateChange(year: Int, month: Int, day: Int) {
        _uiState.update { it.copy(fromDate = formatJalali(year, month, day)) }
    }

    fun onToDateChange(year: Int, month: Int, day: Int) {
        _uiState.update { it.copy(toDate = formatJalali(year, month, day)) }
    }

    fun onStatusChange(status: OfferPlanStatus) {
        _uiState.update { it.copy(status = status) }
    }

    fun submitOfferPlan(onSuccessCallback: () -> Unit) {
        val state = _uiState.value
        if (state.isLoading) return

        if (state.offerName.trim().isBlank()) {
            _uiEvent.tryEmit(
                UiEvent.ShowError(UiText.StringResource(R.string.error_offer_name_required))
            )
            return
        }

        // اعتبارسنجی ترتیبی تاریخ‌ها فقط در صورت پر بودن هر دو تاریخ
        if (state.fromDate.isNotBlank() && state.toDate.isNotBlank() && state.fromDate > state.toDate) {
            _uiEvent.tryEmit(
                UiEvent.ShowError(UiText.StringResource(R.string.error_end_date_before_start))
            )
            return
        }

        val request = OfferPlanRequest(
            offerTicketPlanId = if (state.isEditMode) state.offerId else null,
            title = state.offerName.trim(),
            validFromDate = jalaliToGregorian(state.fromDate),
            validToDate = jalaliToGregorian(state.toDate),
            status = state.status.id
        )
        Log.e("OfferPlanRequest", request.toString())

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val result = if (state.isEditMode) {
                    offerPlanRepository.updateOfferPlanAndSync(request)
                } else {
                    offerPlanRepository.createOfferPlanAndSync(request)
                }

                result.fold(
                    onSuccess = { serverMessage ->
                        val uiMessage = if (!serverMessage.isNullOrBlank()) {
                            UiText.DynamicString(serverMessage)
                        } else {
                            UiText.StringResource(
                                if (state.isEditMode) R.string.msg_offer_plan_updated else R.string.msg_offer_plan_created
                            )
                        }
                        _uiEvent.emit(UiEvent.ShowMessage(uiMessage))
                        kotlinx.coroutines.delay(500)
                        onSuccessCallback()

                    },
                    onFailure = { error ->
                        _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(error)))
                    }
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(e)))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
