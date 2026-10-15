package ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail_plan_list.add

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.dao.OfferDetailDao
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail_plan_list.repository.OfferDetailPlanRepository
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.formatPercentForInput
import ir.kitgroup.partnerManagement.feature.offer.model.OfferDetailPlanRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddOfferDetailViewModel @Inject constructor(
    private val offerDetailPlanRepository: OfferDetailPlanRepository,
    private val offerDetailDao: OfferDetailDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditOfferDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    private val _saveMessage = MutableSharedFlow<String>()
    val saveMessage = _saveMessage.asSharedFlow()


    init {
        observeProducts()
        // دریافت شناسه سطر برای حالت ویرایش
        savedStateHandle.get<String>("offerDetailId")?.let { id ->
            if (id.isNotBlank() && id != "null") {
                loadOfferDetailForEdit(id)
            }
        }

    }

    private fun observeProducts() {
        offerDetailPlanRepository.getProducts().onEach { productList ->
            _uiState.update { it.copy(availableProducts = productList) }
        }.launchIn(viewModelScope)
    }

    private fun loadOfferDetailForEdit(detailId: String) {
        viewModelScope.launch {
            offerDetailDao.observeById(detailId).collect { detailWithProduct ->
                detailWithProduct?.let { item ->
                    val plan = item.offerDetail

                    _uiState.update { currentState ->
                        currentState.copy(
                            planHeaderName = item.offerName,
                            offerTicketPlanLineId = plan.offerDetailId,
                            offerTicketPlanId = plan.offerId,
                            productServiceId = plan.productId ?: "",
                            productServiceName = item.resolvedProductName,
                            personCategory = plan.personCategory ?: 0,
                            gender = plan.gender ?: 0,
                            commissionType = plan.commissionType ?: 0,
                            commissionPercent = formatPercentForInput(plan.commissionPercent),
                            commissionAmount = formatPercentForInput(plan.commissionAmount),
                            discountType = plan.discountType ?: 0,
                            discountPercent = formatPercentForInput(plan.discountPercent),
                            discountAmount = formatPercentForInput(plan.discountAmount),
                            status = plan.status ?: 0,
                            isEditMode = true
                        )
                    }
                }
            }
        }
    }

    fun saveOfferDetail(
        offerTicketPlanLineId: String? = null,
        offerTicketPlanId: String,
        productServiceId: String,
        discountType: Int,
        discountPercent: Int,
        discountAmount: Int,
        commissionType: Int,
        commissionPercent: Int,
        commissionAmount: Int,
        personCategory: Int,
        gender: Int,
        status: Int,
        isEdit: Boolean = false
    ) {
        if (_isSaving.value) return

        viewModelScope.launch {
            _isSaving.value = true
            try {
                val request = OfferDetailPlanRequest(
                    offerTicketPlanLineId = offerTicketPlanLineId,
                    offerTicketPlanId = offerTicketPlanId,
                    productServiceId = productServiceId,
                    discountType = discountType,
                    discountPercent = discountPercent,
                    discountAmount = discountAmount,
                    commissionType = commissionType,
                    commissionPercent = commissionPercent,
                    commissionAmount = commissionAmount,
                    personCategory = personCategory,
                    gender = gender,
                    status = status
                )

                Log.d("CreateOfferDetailPlanRequest", request.toString())

                val isEditMode = isEdit || !offerTicketPlanLineId.isNullOrBlank()

                val result = if (isEditMode) {
                    offerDetailPlanRepository.updateOfferDetailPlanAndSync(request)
                } else {
                    offerDetailPlanRepository.createOfferDetailPlanAndSync(request)
                }

                Log.d("assignedStandsRepositoryResult", result.toString())

             /*   result
                    .onSuccess { response ->
                        _saveMessage.emit(
                            response.message
                                ?: if (isEditMode) "تغییرات با موفقیت ثبت شد." else "تخصیص استند با موفقیت ثبت شد."
                        )
                    }
                    .onFailure { error ->
                        _saveMessage.emit(
                            error.message ?: "عملیات انجام نشد."
                        )
                    }*/
            } catch (e: Exception) {
                Log.e("saveAssignStand", "Error occurred", e)
                _saveMessage.emit(e.localizedMessage ?: "خطای ناشناخته در ارتباط با سرور.")
            } finally {
                _isSaving.value = false
            }
        }
    }
}
