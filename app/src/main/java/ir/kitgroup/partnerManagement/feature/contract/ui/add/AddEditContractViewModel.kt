package ir.kitgroup.partnerManagement.feature.contract.ui.add

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import ir.kitgroup.partnerManagement.core.repository.ContractRepository
import ir.kitgroup.partnerManagement.core.repository.OrganizationRepository
import ir.kitgroup.partnerManagement.core.ui.util.AppErrorMapper
import ir.kitgroup.partnerManagement.core.ui.util.ContractStatus
import ir.kitgroup.partnerManagement.core.ui.util.CooperationModel
import ir.kitgroup.partnerManagement.core.ui.util.SettlementPeriodType
import ir.kitgroup.partnerManagement.core.ui.util.SyncStatus
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate
import ir.kitgroup.partnerManagement.core.ui.util.formatPercentForInput
import ir.kitgroup.partnerManagement.core.ui.util.jalaliToGregorian
import ir.kitgroup.partnerManagement.feature.contract.model.ContractRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import saman.zamani.persiandate.PersianDate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class AddEditContractViewModel @Inject constructor(
    private val organizationRepository: OrganizationRepository,
    private val contractRepository: ContractRepository,
    @ApplicationContext private val context: Context,
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

    private fun getCurrentIsoUtcTimestamp(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(Date())
    }

    fun submitContract(onSuccessCallback: () -> Unit) {
        val state = _uiState.value
        if (state.isLoading) return

        if (state.organizationId.isBlank()) {
            _uiEvent.tryEmit(UiEvent.ShowError(UiText.StringResource(R.string.error_select_organization)))
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val formattedStartDate = jalaliToGregorian(state.startDate).orEmpty()
                val discountVal = state.defaultDiscountPercent.replace(",", ".").toDoubleOrNull()
                val commissionVal =
                    state.defaultCommissionPercent.replace(",", ".").toDoubleOrNull()
                val currentTimestamp = System.currentTimeMillis()
                val isoTimestamp = getCurrentIsoUtcTimestamp()

                val cooperationTitle = context.getString(state.cooperationModel.titleRes)
                val contractTitle = "${state.organizationName} - $cooperationTitle"
                val settlementPeriodTitle = context.getString(state.settlementPeriodType.titleRes)

                if (isEditMode) {
                    // --- حالت ویرایش ---
                    val targetContractId = contractIdArg!!
                    val existingContract =
                        contractRepository.observeById(targetContractId).firstOrNull()

                    val updateRequest = ContractRequest(
                        contractId = targetContractId,
                        organizationId = state.organizationId,
                        startDate = formattedStartDate,
                        settlementPeriodType = state.settlementPeriodType.id,
                        cooperationModel = state.cooperationModel.id,
                        defaultDiscountPercent = discountVal,
                        defaultCommissionPercent = commissionVal,
                        status = state.status.id
                    )

                    val syncResult = contractRepository.updateContractAndSync(updateRequest)

                    syncResult.fold(
                        onSuccess = { serverMsg ->
                            val msg =
                                if (!serverMsg.isNullOrBlank()) UiText.DynamicString(serverMsg)
                                else UiText.StringResource(R.string.msg_contract_updated)
                            _uiEvent.emit(UiEvent.ShowMessage(msg))
                            delay(400)
                            onSuccessCallback()
                        },
                        onFailure = {
                            // تعیین وضعیت سینک
                            val nextSyncStatus =
                                if (existingContract?.syncStatus == SyncStatus.SYNCED) {
                                    SyncStatus.PENDING_UPDATE
                                } else {
                                    existingContract?.syncStatus ?: SyncStatus.PENDING_INSERT
                                }

                            // ذخیره تغییرات به عنوان ویرایش آفلاین در روم با پر کردن کامل تمام فیلدها
                            val offlineEntity = ContractEntity(
                                contractId = targetContractId,
                                title = contractTitle,
                                organizationId = state.organizationId,
                                contractNumber = existingContract?.contractNumber,
                                startDate = formattedStartDate,
                                endDate = existingContract?.endDate,
                                description = existingContract?.description,
                                settlementPeriodType = state.settlementPeriodType.id,
                                settlementPeriodTypeName = settlementPeriodTitle,
                                cooperationModel = state.cooperationModel.id,
                                cooperationModelName = cooperationTitle,
                                defaultDiscountPercent = discountVal,
                                defaultCommissionPercent = commissionVal,
                                status = state.status.id,
                                statusName = existingContract?.statusName,
                                stateCode = existingContract?.stateCode ?: 0,
                                modifiedOn = isoTimestamp,
                                syncStatus = nextSyncStatus,
                                isLocallyModified = true,
                                lastModifiedTimestamp = currentTimestamp
                            )
                            contractRepository.saveLocal(offlineEntity)
                            _uiEvent.emit(UiEvent.ShowMessage(UiText.StringResource(R.string.msg_changes_saved_offline_synchronized_connection)))
                            delay(400)
                            onSuccessCallback()
                        }
                    )
                } else {
                    // --- حالت ثبت جدید ---
                    val newRequest = ContractRequest(
                        contractId = null,
                        organizationId = state.organizationId,
                        startDate = formattedStartDate,
                        settlementPeriodType = state.settlementPeriodType.id,
                        cooperationModel = state.cooperationModel.id,
                        defaultDiscountPercent = discountVal,
                        defaultCommissionPercent = commissionVal,
                        status = state.status.id
                    )

                    val result = contractRepository.createContractAndSync(newRequest)

                    result.fold(
                        onSuccess = { serverMsg ->
                            val msg =
                                if (!serverMsg.isNullOrBlank()) UiText.DynamicString(serverMsg)
                                else UiText.StringResource(R.string.msg_contract_created)
                            _uiEvent.emit(UiEvent.ShowMessage(msg))
                            delay(400)
                            onSuccessCallback()
                        },
                        onFailure = {
                            // ایجاد شناسه محلی و ذخیره آفلاین کامل انتیتی
                            val localUUID = UUID.randomUUID().toString()
                            val offlineEntity = ContractEntity(
                                contractId = localUUID,
                                title = contractTitle,
                                organizationId = state.organizationId,
                                contractNumber = null,
                                startDate = formattedStartDate,
                                endDate = null,
                                description = null,
                                settlementPeriodType = state.settlementPeriodType.id,
                                settlementPeriodTypeName = settlementPeriodTitle,
                                cooperationModel = state.cooperationModel.id,
                                cooperationModelName = cooperationTitle,
                                defaultDiscountPercent = discountVal,
                                defaultCommissionPercent = commissionVal,
                                status = state.status.id,
                                statusName = null,
                                stateCode = 0,
                                modifiedOn = isoTimestamp,
                                syncStatus = SyncStatus.PENDING_INSERT,
                                isLocallyModified = true,
                                lastModifiedTimestamp = currentTimestamp
                            )
                            contractRepository.saveLocal(offlineEntity)
                            _uiEvent.emit(UiEvent.ShowMessage(UiText.StringResource(R.string.msg_saved_local_storage_awaiting_synchronization)))
                            delay(400)
                            onSuccessCallback()
                        }
                    )
                }

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

