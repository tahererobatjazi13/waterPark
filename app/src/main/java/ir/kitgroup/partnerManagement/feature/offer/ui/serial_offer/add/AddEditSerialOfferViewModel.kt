package ir.kitgroup.partnerManagement.feature.offer.ui.serial_offer.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.database.entity.OfferEntity
import ir.kitgroup.partnerManagement.core.repository.AssignedSerialsRepository
import ir.kitgroup.partnerManagement.core.repository.ContractRepository
import ir.kitgroup.partnerManagement.core.ui.util.AppErrorMapper
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import ir.kitgroup.partnerManagement.core.ui.util.toEnglishDigits
import ir.kitgroup.partnerManagement.feature.offer.model.SerialOfferRequest
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_plan_list.repository.OfferPlanRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class AddEditSerialOfferViewModel @Inject constructor(
    private val assignedSerialsRepository: AssignedSerialsRepository,
    private val offerPlanRepository: OfferPlanRepository,
    private val contractRepository: ContractRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navOrganizationId: String? = savedStateHandle.get<String>("organizationId")
    private val navMeetingId: String? = savedStateHandle.get<String>("meetingId")
    private val serialId: String? = savedStateHandle.get<String>("serialId")

    private val _uiState = MutableStateFlow(AddEditSerialOfferUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        observeOfferPlans()
        observeContracts(navOrganizationId.orEmpty())
        if (!serialId.isNullOrBlank()) {
            loadForEdit(serialId)
        }
    }

    private fun observeOfferPlans() {
        offerPlanRepository.observeAll()
            .onEach { plans ->
                val offerPlansItems = plans.map {
                    OfferEntity(offerId = it.offerId, title = it.title.orEmpty())
                }
                _uiState.update { state ->
                    if (!state.isEditMode && state.selectedOfferPlanId.isNullOrBlank()) {
                        val defaultOfferPlan = offerPlansItems.firstOrNull()
                        state.copy(
                            offerPlans = offerPlansItems,
                            selectedOfferPlanId = defaultOfferPlan?.offerId,
                            selectedOfferPlanTitle = defaultOfferPlan?.title.orEmpty()
                        )
                    } else {
                        state.copy(offerPlans = offerPlansItems)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeContracts(orgId: String) {
        if (orgId.isBlank()) return

        contractRepository.observeActiveByOrganization(orgId)
            .onEach { contracts ->
                val contractItems = contracts.map {
                    ContractEntity(contractId = it.contractId, title = it.title.orEmpty())
                }
                _uiState.update { state ->
                    if (!state.isEditMode && state.selectedContractId.isNullOrBlank()) {
                        val defaultContract = contractItems.firstOrNull()
                        state.copy(
                            contracts = contractItems,
                            selectedContractId = defaultContract?.contractId,
                            selectedContractTitle = defaultContract?.title.orEmpty()
                        )
                    } else {
                        state.copy(contracts = contractItems)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadForEdit(serialId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isInitialLoading = true) }
            try {
                assignedSerialsRepository.observeById(serialId).collect { detail ->
                    detail?.let { item ->
                        val serial = item.assignedSerial

                        // لود کردن قراردادهای سازمان این رکورد
                        serial.organizationId?.let { orgId ->
                            observeContracts(orgId)
                        }

                        _uiState.update { state ->
                            state.copy(
                                isEditMode = true,
                                id = serial.serialAssignmentId,
                                organizationId = serial.organizationId,
                                selectedOfferPlanId = serial.offerId,
                                selectedOfferPlanTitle = item.offerName,
                                selectedContractId = serial.contractId,
                                selectedContractTitle = item.contractName,
                                serialPrefix = serial.serialPrefix.orEmpty(),
                                countSerialText = (serial.count ?: 0).toString(),
                                serialFromText = (serial.fromSerial ?: 0).toString(),
                                selectedStatusId = serial.contractOfferStatus ?: 0,
                                isInitialLoading = false
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isInitialLoading = false) }
                _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(e)))
            }
        }
    }


    fun onSelectOfferPlan(item: OfferEntity) {
        _uiState.update {
            it.copy(
                selectedOfferPlanId = item.offerId,
                selectedOfferPlanTitle = item.title.orEmpty()
            )
        }
    }

    fun onSelectContract(item: ContractEntity) {
        _uiState.update {
            it.copy(
                selectedContractId = item.contractId,
                selectedContractTitle = item.title.orEmpty()
            )
        }
    }

    fun onSerialPrefixChange(v: String) = _uiState.update { it.copy(serialPrefix = v) }

    fun onCountSerialChange(v: String) =
        _uiState.update { it.copy(countSerialText = v.toEnglishDigits().filter(Char::isDigit)) }

    fun onSerialFromChange(v: String) =
        _uiState.update { it.copy(serialFromText = v.toEnglishDigits().filter(Char::isDigit)) }

    fun onStatusChange(statusId: Int) = _uiState.update { it.copy(selectedStatusId = statusId) }

    fun submitSerialOffer(
        organizationId: String?,
        meetingId: String?,
        onSuccessCallback: () -> Unit
    ) {
        val state = _uiState.value
        if (state.isLoading) return

        // بررسی و اعتبارسنجی فیلدهای اجباری
        if (state.selectedOfferPlanId.isNullOrBlank()) {
            _uiEvent.tryEmit(UiEvent.ShowError(UiText.StringResource(R.string.hint_choose_offer_plan)))
            return
        }

        if (state.selectedContractId.isNullOrBlank()) {
            _uiEvent.tryEmit(UiEvent.ShowError(UiText.StringResource(R.string.hint_choose_contract)))
            return
        }

        val countSerial = state.countSerialText.toEnglishDigits().toIntOrNull() ?: 0
        if (countSerial <= 0) {
            _uiEvent.tryEmit(UiEvent.ShowError(UiText.StringResource(R.string.hint_enter_count_serial)))
            return
        }

        val serialFrom = state.serialFromText.toEnglishDigits().toIntOrNull() ?: 0
        if (serialFrom <= 0) {
            _uiEvent.tryEmit(UiEvent.ShowError(UiText.StringResource(R.string.hint_enter_serial_from)))
            return
        }

        val targetOrgId = organizationId
            ?: state.organizationId
            ?: navOrganizationId
        val targetMeetingId = meetingId ?: navMeetingId

        val request = SerialOfferRequest(
            id = state.id,
            contractId = state.selectedContractId,
            organizationId = targetOrgId,
            offerTicketPlanId = state.selectedOfferPlanId,
            meetingId = targetMeetingId,
            contractOfferStatus = state.selectedStatusId,
            countSerial = countSerial,
            serialFrom = serialFrom,
            serialPrefix = state.serialPrefix.trim().ifBlank { null }
        )


        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val result = if (state.isEditMode) {
                    assignedSerialsRepository.updateSerialOfferAndSync(request)
                } else {
                    assignedSerialsRepository.createSerialOfferAndSync(request)
                }

                result.fold(
                    onSuccess = { serverMessage ->
                        val uiMessage = if (!serverMessage.isNullOrBlank()) {
                            UiText.DynamicString(serverMessage)
                        } else {
                            UiText.StringResource(
                                if (state.isEditMode) R.string.msg_assign_serial_offer else R.string.msg_update_serial_offer
                            )
                        }
                        _uiEvent.emit(UiEvent.ShowMessage(uiMessage))
                        delay(400)
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
