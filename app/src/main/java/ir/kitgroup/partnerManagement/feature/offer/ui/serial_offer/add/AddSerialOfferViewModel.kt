package ir.kitgroup.partnerManagement.feature.offer.ui.serial_offer.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.dao.AssignedSerialDao
import ir.kitgroup.partnerManagement.core.repository.AssignedSerialsRepository
import ir.kitgroup.partnerManagement.core.repository.ContractRepository
import ir.kitgroup.partnerManagement.core.repository.OfferPlanRepository
import ir.kitgroup.partnerManagement.core.repository.OrganizationRepository
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.feature.offer.model.CreateSerialOfferRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import ir.kitgroup.partnerManagement.core.ui.util.toEnglishDigits


@HiltViewModel
class AddSerialOfferViewModel @Inject constructor(
    private val assignedSerialsRepository: AssignedSerialsRepository,
    private val assignedSerialDao: AssignedSerialDao,
    private val offerPlanRepository: OfferPlanRepository,
    private val contractRepository: ContractRepository,
    private val organizationRepository: OrganizationRepository,

    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val organizationId: String? = savedStateHandle.get<String>("organizationId")

    private val _uiState = MutableStateFlow(AddContractOfferUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving = _isSaving.asStateFlow()

    private val _saveMessage = MutableSharedFlow<String>()
    val saveMessage = _saveMessage.asSharedFlow()

    init {
        observeOfferPlans()
        observeContracts(organizationId ?: "")

        savedStateHandle.get<String>("id")?.let { id ->
            if (id.isNotBlank() && id != "null") {
                loadForEdit(id)
            }
        }
    }


    private fun observeOfferPlans() {
        offerPlanRepository.observeAll()
            .onEach { plans ->
                val dropDownItems = plans.map { DropDownItem(id = it.offerId, title = it.title ?: "") }
                _uiState.update { s ->
                    if (!s.isEditMode && s.selectedOfferPlanId.isNullOrBlank()) {
                        val defaultPlan = dropDownItems.firstOrNull()
                        s.copy(
                            offerPlans = dropDownItems,
                            selectedOfferPlanId = defaultPlan?.id,
                            selectedOfferPlanTitle = defaultPlan?.title ?: ""
                        )
                    } else {
                        s.copy(offerPlans = dropDownItems)
                    }
                }
            }
            .launchIn(viewModelScope)
    }



    private fun observeContracts(orgId: String) {
        contractRepository.observeActiveByOrganization(orgId)
            .onEach { contracts ->
                val dropDownItems = contracts.map {
                    DropDownItem(
                        id = it.contractId,
                        title = it.title ?: ""
                    )
                }

                _uiState.update { s ->
                    // اگر کاربر در حالت ویرایش نباشد و هنوز قراردادی انتخاب نشده باشد:
                    if (!s.isEditMode && s.selectedContractId.isNullOrBlank()) {

                        // روش اول: اگر آبجکت قرارداد خودش فیلد isActive یا وضعیت فعال دارد
                        // val active = contracts.firstOrNull { it.isActive || it.contractStatus == 1 }

                        // روش دوم: انتخاب اولین قرارداد یا تطبیق با activeContractId سازمان
                        val defaultContract = dropDownItems.firstOrNull()

                        s.copy(
                            contracts = dropDownItems,
                            selectedContractId = defaultContract?.id,
                            selectedContractTitle = defaultContract?.title ?: ""
                        )
                    } else {
                        s.copy(contracts = dropDownItems)
                    }
                }
            }
            .launchIn(viewModelScope)
    }


    private fun loadForEdit(id: String) {
        viewModelScope.launch {
            assignedSerialsRepository.observeById(id).collect { detail ->
                detail?.let { item ->
                    val serial = item.assignedSerial
                    _uiState.update { s ->
                        s.copy(
                            isEditMode = true,
                            id = serial.serialAssignmentId,
                            selectedOfferPlanId = serial.offerId,
                            selectedOfferPlanTitle = item.offerName ?: "",
                            selectedContractId = serial.contractId,
                            selectedContractTitle = item.contractName ?: "",
                            serialPrefix = serial.serialPrefix ?: "",
                            countSerialText = (serial.count ?: 0).toString(),
                            serialFromText = (serial.fromSerial ?: 0).toString(),
                            selectedStatusId = serial.contractOfferStatus ?: 0
                        )
                    }
                }
            }
        }
    }

    fun onSelectOfferPlan(item: DropDownItem) {
        _uiState.update {
            it.copy(
                selectedOfferPlanId = item.id,
                selectedOfferPlanTitle = item.title
            )
        }
    }

    fun onSelectContract(item: DropDownItem) {
        _uiState.update {
            it.copy(
                selectedContractId = item.id,
                selectedContractTitle = item.title
            )
        }
    }
    // یک Event اختصاصی برای اتمام موفقیت‌آمیز
    private val _saveSuccessEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val saveSuccessEvent = _saveSuccessEvent.asSharedFlow()

// یا استفاده از UiEvent موجود در پروژه‌تان

    fun onSerialPrefixChange(v: String) = _uiState.update { it.copy(serialPrefix = v) }

    fun onCountSerialChange(v: String) =
        _uiState.update { it.copy(countSerialText = v.toEnglishDigits().filter(Char::isDigit)) }

    fun onSerialFromChange(v: String) =
        _uiState.update { it.copy(serialFromText = v.toEnglishDigits().filter(Char::isDigit)) }

    fun onStatusChange(statusId: Int) = _uiState.update { it.copy(selectedStatusId = statusId) }

    fun save(
        organizationId: String? = null,
        meetingId: String? = null
    ) {
        if (_isSaving.value) return

        val s = _uiState.value
        val countSerial = s.countSerialText.toEnglishDigits().toIntOrNull() ?: 0
        val serialFrom = s.serialFromText.toEnglishDigits().toIntOrNull() ?: 0

        viewModelScope.launch {
            // اعتبارسنجی فیلدها با emit ایمن
            if (s.selectedOfferPlanId.isNullOrBlank()) {
                _saveMessage.emit("طرح را انتخاب کنید.")
                return@launch
            }
            if (s.selectedContractId.isNullOrBlank()) {
                _saveMessage.emit("قرارداد را انتخاب کنید.")
                return@launch
            }
            if (countSerial <= 0) {
                _saveMessage.emit("تعداد سریال را وراد کنید.")
                return@launch
            }
            if (serialFrom <= 0) {
                _saveMessage.emit("سریال از معتبر نیست.")
                return@launch
            }

            _isSaving.value = true
            try {
                val request = CreateSerialOfferRequest(
                    id = s.id,
                    contractId = s.selectedContractId,
                    organizationId = organizationId,
                    offerTicketPlanId = s.selectedOfferPlanId,
                    meetingId = meetingId,
                    contractOfferStatus = s.selectedStatusId,
                    countSerial = countSerial,
                    serialFrom = serialFrom,
                    serialPrefix = s.serialPrefix.ifBlank { null }
                )

                val result = if (s.isEditMode) {
                    assignedSerialsRepository.updateSerialOffer(request)
                } else {
                    assignedSerialsRepository.createSerialOffer(request)
                }

                result.onSuccess { r ->
                    _saveMessage.emit(r.message ?: "عملیات با موفقیت انجام شد.")
                    _saveSuccessEvent.emit(Unit) // فقط اینجا صفحه بسته شود
                }.onFailure { e ->
                    _saveMessage.emit(e.message ?: "عملیات انجام نشد.")
                    // صفحه بسته نمی‌شود
                }

            } finally {
                _isSaving.value = false
            }
        }
    }

}
