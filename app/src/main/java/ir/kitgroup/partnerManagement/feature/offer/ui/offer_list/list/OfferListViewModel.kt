package ir.kitgroup.partnerManagement.feature.offer.ui.offer_list.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.entity.OfferEntity
import ir.kitgroup.partnerManagement.core.repository.OfferPlanRepository
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.usecase.CheckCanAddOfferUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfferListViewModel @Inject constructor(
    private val  repository: OfferPlanRepository,
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

    private val _offers = MutableStateFlow<List<OfferEntity>?>(null)
    val offers = _offers.asStateFlow()

/*    // اتصال مستقیم به Flow دیتابیس با stateIn
    val offers: StateFlow<List<OfferEntity>> = repository.observeAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )*/

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadOffers()
    }

    fun loadOffers() {
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            repository.observeAll()
                .catch { throwable ->
                    Log.e("AssignedStandsVM", "Error collecting assigned stands", throwable)
                    _errorMessage.value = throwable.localizedMessage ?: "خطا در دریافت لیست آفرها"
                    _isLoading.value = false
                }
                .collect { standList ->
                    _offers.value = standList
                    _isLoading.value = false
                }
        }
    }

}
