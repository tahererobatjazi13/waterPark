package ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail_plan_list.add

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.ProductEntity
import ir.kitgroup.partnerManagement.core.ui.util.AppErrorMapper
import ir.kitgroup.partnerManagement.core.ui.util.CommissionType
import ir.kitgroup.partnerManagement.core.ui.util.DiscountType
import ir.kitgroup.partnerManagement.core.ui.util.GenderType
import ir.kitgroup.partnerManagement.core.ui.util.OfferDetailPlanStatus
import ir.kitgroup.partnerManagement.core.ui.util.PersonCategory
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import ir.kitgroup.partnerManagement.core.ui.util.formatPercentForInput
import ir.kitgroup.partnerManagement.feature.offer.model.OfferDetailPlanRequest
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail_plan_list.repository.OfferDetailPlanRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditOfferDetailPlanViewModel @Inject constructor(
    private val offerDetailPlanRepository: OfferDetailPlanRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val offerTicketPlanIdArg: String? = savedStateHandle["offerTicketPlanId"]
    private val offerTicketPlanLineIdArg: String? = savedStateHandle["offerTicketPlanLineId"]
    private val offerTicketPlanNameArg: String? = savedStateHandle["offerTicketPlanName"]

    private val _uiState = MutableStateFlow(
        AddEditOfferDetailPlanUiState(
            offerTicketPlanId = offerTicketPlanIdArg.orEmpty(),
            offerTicketPlanName = offerTicketPlanNameArg.orEmpty()
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    private var isInitialized = false

    init {
        observeProducts()
        if (!offerTicketPlanLineIdArg.isNullOrBlank()) {
            loadOfferDetailForEdit(offerTicketPlanLineIdArg)
        }
    }

    fun initialize(
        offerTicketPlanId: String?,
        offerTicketPlanLineId: String?,
        offerTicketPlanName: String
    ) {
        if (isInitialized) return
        isInitialized = true

        if (!offerTicketPlanLineId.isNullOrBlank()) {
            loadOfferDetailForEdit(offerTicketPlanLineId)
        } else {
            _uiState.update { current ->
                current.copy(
                    offerTicketPlanId = offerTicketPlanId ?: current.offerTicketPlanId,
                    offerTicketPlanName = offerTicketPlanName.ifBlank { current.offerTicketPlanName },
                    isEditMode = false
                )
            }
        }
    }

    private fun observeProducts() {
        offerDetailPlanRepository.getProducts()
            .onEach { productList ->
                _uiState.update { it.copy(availableProducts = productList) }
            }
            .launchIn(viewModelScope)
    }

    private fun loadOfferDetailForEdit(offerDetailId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isInitialLoading = true) }
            try {
                val offerDetailWithProduct =
                    offerDetailPlanRepository.observeOfferDetailById(offerDetailId).firstOrNull()

                if (offerDetailWithProduct != null) {
                    val detail = offerDetailWithProduct.offerDetail
                    _uiState.update { current ->
                        current.copy(
                            offerTicketPlanName = offerDetailWithProduct.offerName,
                            offerTicketPlanLineId = detail.offerDetailId,
                            offerTicketPlanId = detail.offerId,
                            productServiceId = detail.productId.orEmpty(),
                            productServiceName = offerDetailWithProduct.resolvedProductName,
                            personCategory = PersonCategory.fromId(detail.personCategory),
                            gender = GenderType.fromId(detail.gender),
                            commissionType = CommissionType.fromId(detail.commissionType),
                            commissionPercent = formatPercentForInput(detail.commissionPercent),
                            commissionAmount = formatPercentForInput(detail.commissionAmount),
                            discountType = DiscountType.fromId(detail.discountType),
                            discountPercent = formatPercentForInput(detail.discountPercent),
                            discountAmount = formatPercentForInput(detail.discountAmount),
                            status = OfferDetailPlanStatus.fromId(detail.status),
                            isEditMode = true,
                            isInitialLoading = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(isInitialLoading = false) }
                    _uiEvent.emit(UiEvent.ShowError(UiText.DynamicString("جزئیات طرح مورد نظر یافت نشد.")))
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(isInitialLoading = false) }
                _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(e)))
            }
        }
    }

    fun onProductSelected(product: ProductEntity) {
        _uiState.update {
            it.copy(
                productServiceId = product.productId,
                productServiceName = product.name.orEmpty()
            )
        }
    }

    fun onPersonCategoryChange(category: PersonCategory) {
        _uiState.update { it.copy(personCategory = category) }
    }

    fun onGenderChange(gender: GenderType) {
        _uiState.update { it.copy(gender = gender) }
    }

    fun onCommissionTypeChange(type: CommissionType) {
        _uiState.update {
            it.copy(
                commissionType = type,
                commissionPercent = if (type == CommissionType.PERCENTAGE) it.commissionPercent else "",
                commissionAmount = if (type == CommissionType.FIXED_AMOUNT) it.commissionAmount else ""
            )
        }
    }

    fun onCommissionPercentChange(value: String) {
        _uiState.update { it.copy(commissionPercent = value) }
    }

    fun onCommissionAmountChange(value: String) {
        _uiState.update { it.copy(commissionAmount = value) }
    }

    fun onDiscountTypeChange(type: DiscountType) {
        _uiState.update {
            it.copy(
                discountType = type,
                discountPercent = if (type == DiscountType.PERCENTAGE) it.discountPercent else "",
                discountAmount = if (type == DiscountType.FIXED_AMOUNT) it.discountAmount else ""
            )
        }
    }

    fun onDiscountPercentChange(value: String) {
        _uiState.update { it.copy(discountPercent = value) }
    }

    fun onDiscountAmountChange(value: String) {
        _uiState.update { it.copy(discountAmount = value) }
    }

    fun onStatusChange(status: OfferDetailPlanStatus) {
        _uiState.update { it.copy(status = status) }
    }

    fun submitOfferDetailPlan(onSuccessCallback: () -> Unit) {
        val state = _uiState.value
        if (state.isLoading) return

        if (state.productServiceId.isBlank()) {
            _uiEvent.tryEmit(UiEvent.ShowError(UiText.StringResource(R.string.error_product_service_required)))
            return
        }

        val discountPercentInt = state.discountPercent.toIntOrNull() ?: 0
        val discountAmountInt = state.discountAmount.toIntOrNull() ?: 0
        val commissionPercentInt = state.commissionPercent.toIntOrNull() ?: 0
        val commissionAmountInt = state.commissionAmount.toIntOrNull() ?: 0

        val request = OfferDetailPlanRequest(
            offerTicketPlanLineId = if (state.isEditMode) state.offerTicketPlanLineId else null,
            offerTicketPlanId = state.offerTicketPlanId,
            productServiceId = state.productServiceId,
            discountType = state.discountType.id,
            discountPercent = discountPercentInt,
            discountAmount = discountAmountInt,
            commissionType = state.commissionType.id,
            commissionPercent = commissionPercentInt,
            commissionAmount = commissionAmountInt,
            personCategory = state.personCategory.id,
            gender = state.gender.id,
            status = state.status.id
        )

        Log.d("AddEditOfferDetailVM", "Request: $request")

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val result = if (state.isEditMode) {
                    offerDetailPlanRepository.updateOfferDetailPlanAndSync(request)
                } else {
                    offerDetailPlanRepository.createOfferDetailPlanAndSync(request)
                }

                result.fold(
                    onSuccess = { serverMessage ->
                        val uiMessage = if (!serverMessage.isNullOrBlank()) {
                            UiText.DynamicString(serverMessage)
                        } else {
                            UiText.StringResource(
                                if (state.isEditMode) R.string.msg_offer_detail_plan_updated else R.string.msg_offer_detail_plan_created
                            )
                        }
                        _uiEvent.emit(UiEvent.ShowMessage(uiMessage))
                        delay(500)
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
