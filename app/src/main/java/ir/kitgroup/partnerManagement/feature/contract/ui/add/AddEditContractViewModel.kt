package ir.kitgroup.partnerManagement.feature.contract.ui.add

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import ir.kitgroup.partnerManagement.core.repository.ContractRepository
import ir.kitgroup.partnerManagement.core.repository.OrganizationRepository
import ir.kitgroup.partnerManagement.core.ui.util.AppErrorMapper
import ir.kitgroup.partnerManagement.core.ui.util.ContractStatus
import ir.kitgroup.partnerManagement.core.ui.util.CooperationModel
import ir.kitgroup.partnerManagement.core.ui.util.SettlementPeriodType
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate
import ir.kitgroup.partnerManagement.core.ui.util.jalaliToGregorian
import ir.kitgroup.partnerManagement.feature.contract.model.ContractRequest
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import saman.zamani.persiandate.PersianDate
import javax.inject.Inject
import ir.kitgroup.partnerManagement.core.ui.util.formatPercentForInput
import java.util.Locale
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class AddEditContractViewModel @Inject constructor(
    private val organizationRepository: OrganizationRepository,
    private val contractRepository: ContractRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val organizationIdArg: String? = savedStateHandle["organizationId"]
    private val contractIdArg: String? = savedStateHandle["contractId"]
    val isEditMode: Boolean = !contractIdArg.isNullOrBlank()


    private val _uiState = MutableStateFlow(AddEditContractUiState(isEditMode = isEditMode))
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        initializeDefaultDate()
        loadInitialData()
    }

    private fun initializeDefaultDate() {
        val today = PersianDate()
        val formattedDate = "${today.shYear}/${String.format(Locale.US, "%02d", today.shMonth)}/${
            String.format(
                Locale.US,
                "%02d",
                today.shDay
            )
        }"
        _uiState.update { it.copy(startDate = formattedDate) }
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isInitialLoading = true) }

            val organizations = organizationRepository.observeAll().firstOrNull().orEmpty()

            val selectedOrg = if (!organizationIdArg.isNullOrBlank()) {
                organizations.firstOrNull { it.organization.organizationId == organizationIdArg }
            } else null

            _uiState.update { current ->
                current.copy(
                    organizations = organizations,
                    preselectedOrganization = selectedOrg,
                    organizationId = selectedOrg?.organization?.organizationId
                        ?: current.organizationId,
                    organizationName = selectedOrg?.organization?.name ?: current.organizationName
                )
            }

            // در صورتی که حالت ویرایش باشد، لود کردن جزئیات قرارداد
            if (isEditMode && !contractIdArg.isNullOrBlank()) {
                val contract = contractRepository.observeById(contractIdArg).firstOrNull()
                if (contract != null) {
                    val orgForContract =
                        organizations.firstOrNull { it.organization.organizationId == contract.organizationId }
                    _uiState.update { current ->
                        current.copy(
                            organizationId = contract.organizationId.orEmpty(),
                            organizationName = orgForContract?.organization?.name.orEmpty(),
                            startDate = contract.startDate?.takeIf { it.isNotBlank() }
                                ?.let { formatJalaliDate(it) }.orEmpty(),
                            settlementPeriodType = SettlementPeriodType.fromId(contract.settlementPeriodType)
                                ?: SettlementPeriodType.MONTHLY,
                            cooperationModel = CooperationModel.fromId(contract.cooperationModel)
                                ?: CooperationModel.REFERRAL_VOUCHER,
                            defaultDiscountPercent = formatPercentForInput(contract.defaultDiscountPercent),
                            defaultCommissionPercent = formatPercentForInput(contract.defaultCommissionPercent),
                            status = ContractStatus.fromId(contract.status)
                        )
                    }
                }
            }

            _uiState.update { it.copy(isInitialLoading = false) }
        }
    }


    fun onOrganizationSelected(organization: OrganizationWithDetail) {
        _uiState.update {
            it.copy(
                organizationId = organization.organization.organizationId,
                organizationName = organization.organization.name.orEmpty(),
                preselectedOrganization = organization
            )
        }
    }

    fun onStartDateChange(year: Int, month: Int, day: Int) {
        val formattedDate = "$year/${String.format(Locale.US, "%02d", month)}/${
            String.format(
                Locale.US,
                "%02d",
                day
            )
        }"
        _uiState.update { it.copy(startDate = formattedDate) }
    }

    fun onSettlementPeriodTypeChange(type: SettlementPeriodType) {
        _uiState.update { it.copy(settlementPeriodType = type) }
    }

    fun onCooperationModelChange(model: CooperationModel) {
        _uiState.update { it.copy(cooperationModel = model) }
    }

    fun onDefaultDiscountPercentChange(percent: String) {
        _uiState.update { it.copy(defaultDiscountPercent = percent) }
    }

    fun onDefaultCommissionPercentChange(percent: String) {
        _uiState.update { it.copy(defaultCommissionPercent = percent) }
    }

    fun onStatusChange(status: ContractStatus) {
        _uiState.update { it.copy(status = status) }
    }


    fun submitContract(onSuccessCallback: () -> Unit) {
        val state = _uiState.value
        if (state.isLoading) return

        if (state.organizationId.isBlank()) {
            _uiEvent.tryEmit(UiEvent.ShowError(UiText.StringResource(R.string.error_select_organization)))
            return
        }
        val request = ContractRequest(
            contractId = if (isEditMode) contractIdArg else null,
            organizationId = state.organizationId,
            startDate = jalaliToGregorian(state.startDate).orEmpty(),
            settlementPeriodType = state.settlementPeriodType.id,
            cooperationModel = state.cooperationModel.id,
            defaultDiscountPercent = state.defaultDiscountPercent.replace(",", ".")
                .toDoubleOrNull(),
            defaultCommissionPercent = state.defaultCommissionPercent.replace(",", ".")
                .toDoubleOrNull(),
            status = state.status.id
        )
        Log.e("ContractRequest", request.toString())

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {

                val result = if (isEditMode) {
                    contractRepository.updateContractAndSync(request)
                } else {
                    contractRepository.createContractAndSync(request)
                }

                result.fold(
                    onSuccess = { serverMessage ->
                        val uiMessage = if (!serverMessage.isNullOrBlank()) {
                            UiText.DynamicString(serverMessage)
                        } else {
                            UiText.StringResource(
                                if (state.isEditMode) R.string.msg_contract_updated else R.string.msg_contract_created
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