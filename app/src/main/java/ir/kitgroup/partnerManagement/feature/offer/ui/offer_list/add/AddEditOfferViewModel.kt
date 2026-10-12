package ir.kitgroup.partnerManagement.feature.offer.ui.offer_list.add

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.entity.OfferEntity
import ir.kitgroup.partnerManagement.core.repository.OfferPlanRepository
import ir.kitgroup.partnerManagement.core.ui.util.OfferPlanStatus
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import ir.kitgroup.partnerManagement.core.ui.util.jalaliToGregorian
import ir.kitgroup.partnerManagement.feature.offer.model.CreateOfferPlanRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class AddEditOfferViewModel @Inject constructor(
    private val repository: OfferPlanRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditOfferUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    fun loadOffer(offerId: String) {
        viewModelScope.launch {
            val offer = repository.getOfferById(offerId)

            if (offer != null) {
                initData(offer)
            } else {
                _uiEvent.emit(
                    UiEvent.ShowError(
                        UiText.DynamicString("طرح موردنظر پیدا نشد.")
                    )
                )
            }
        }
    }


    fun initData(offer: OfferEntity) {
        if (offer != null) {
            _uiState.value = AddEditOfferUiState(
                offerId = offer.offerId,
                offerName = offer.title.orEmpty(),
                fromDate = offer.fromDate.orEmpty(),
                toDate = offer.toDate.orEmpty(),
                status = OfferPlanStatus.fromId(offer.statusOffer),
                isEditMode = true
            )
        }
    }


    fun onNameChange(value: String) {
        _uiState.value = _uiState.value.copy(offerName = value)
    }

    fun onFromDateChange(value: String) {
        _uiState.value = _uiState.value.copy(fromDate = value)
    }

    fun onToDateChange(value: String) {
        _uiState.value = _uiState.value.copy(toDate = value)
    }

    fun onStatusChange(status: OfferPlanStatus) {
        _uiState.value = _uiState.value.copy(status = status)
    }

    fun submitPlan(onSuccess: () -> Unit) {
        val state = _uiState.value

        if (state.offerName.isBlank()) {
            _uiEvent.tryEmit(UiEvent.ShowError(UiText.DynamicString("لطفاً تمامی فیلدهای الزامی را تکمیل کنید.")))
            return
        }

        // تابع کمکی برای اطمینان از خروجی میلادی معتبر
        fun normalizeToGregorian(dateStr: String?): String? {
            if (dateStr.isNullOrBlank()) return null
            val cleanDate = dateStr.trim().take(10) // در صورت وجود زمان T00:00:00

            // اگر تاریخ هم‌اکنون میلادی است (مثلاً 2026-10-12 یا 2026/10/12)
            if (cleanDate.startsWith("20") || cleanDate.startsWith("19")) {
                return cleanDate.replace('/', '-')
            }

            // در غیر این صورت تاریخ شمسی است و نیاز به تبدیل دارد
            return try {
                jalaliToGregorian(cleanDate)
            } catch (e: Exception) {
                cleanDate
            }
        }

        val fromDateGregorian = normalizeToGregorian(state.fromDate)
        val toDateGregorian = normalizeToGregorian(state.toDate)

        val targetOfferId = if (state.isEditMode) state.offerId else null

        val request = CreateOfferPlanRequest(
            offerTicketPlanId = targetOfferId,
            title = state.offerName.trim(),
            validFromDate = fromDateGregorian,
            validToDate = toDateGregorian,
            status = state.status.id
        )

        Log.d("CreateOfferPlanRequest", request.toString())

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = if (state.isEditMode) {
                repository.updateOfferPlan(request)
            } else {
                repository.createOfferPlan(request)
            }

            _uiState.value = _uiState.value.copy(isLoading = false)

            result.onSuccess {
                _uiEvent.emit(
                    UiEvent.ShowMessage(
                        UiText.DynamicString(if (state.isEditMode) "طرح با موفقیت ویرایش شد" else "طرح با موفقیت ثبت شد")
                    )
                )
                onSuccess()
            }.onFailure { ex ->
                _uiEvent.emit(
                    UiEvent.ShowError(
                        UiText.DynamicString(ex.message ?: "خطای ناشناخته رخ داد")
                    )
                )
            }
        }
    }

}
