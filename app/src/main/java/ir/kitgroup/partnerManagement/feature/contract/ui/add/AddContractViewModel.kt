package ir.kitgroup.partnerManagement.feature.contract.ui.add

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationDao
import ir.kitgroup.partnerManagement.core.repository.ContractRepository
import ir.kitgroup.partnerManagement.core.ui.util.jalaliToGregorian
import ir.kitgroup.partnerManagement.feature.contract.model.CreateContractRequest
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddContractViewModel @Inject constructor(

    private val organizationDao: OrganizationDao,

    private val contractRepository: ContractRepository,

    savedStateHandle: SavedStateHandle

) : ViewModel() {

    val organizationId: String? =
        savedStateHandle["organizationId"]

    val contractId: String? =
        savedStateHandle["contractId"]

    val isEditMode: Boolean =
        !contractId.isNullOrBlank()


    private val _uiState =
        MutableStateFlow(
            AddContractUiState()
        )

    val uiState =
        _uiState.asStateFlow()


    init {

        loadData()
    }


    private fun loadData() {

        viewModelScope.launch {

            organizationDao
                .observeAll()
                .collect { organizations ->

                    val selectedOrganization =
                        if (
                            !organizationId.isNullOrBlank()
                        ) {

                            organizations.firstOrNull {
                                it.organization.organizationId ==
                                        organizationId
                            }

                        } else {
                            null
                        }

                    _uiState.update {

                        it.copy(
                            organizations =
                            organizations,

                            preselectedOrganization =
                            selectedOrganization,

                            isLoading = false
                        )
                    }
                }
        }


        if (isEditMode) {

            viewModelScope.launch {

                contractRepository
                    .observeById(contractId!!)
                    .collect { contract ->

                        _uiState.update {

                            it.copy(
                                contract = contract
                            )
                        }
                    }
            }
        }
    }


    fun saveContract(
        form: AddContractFormState
    ) {

        if (_uiState.value.isSaving) {
            return
        }


        // ----------------------------
        // Validation
        // ----------------------------

        if (
            form.organizationId.isBlank()
        ) {
            setMessage(
                "لطفاً سازمان را انتخاب کنید."
            )
            return
        }


        viewModelScope.launch {

            _uiState.update {

                it.copy(
                    isSaving = true,
                    message = null,
                    saveSuccess = false
                )
            }


            try {

                val request =
                    CreateContractRequest(
                        contractId =
                        if (isEditMode) {
                            contractId
                        } else {
                            null
                        },
                        organizationId =
                        form.organizationId,

                        startDate = jalaliToGregorian(form.startDate).orEmpty(),

                        settlementPeriodType =
                        form.settlementPeriodType,

                        cooperationModel =
                        form.cooperationModel,

                        defaultDiscountPercent =
                        form.defaultDiscountPercent
                            .replace(",", ".")
                            .toDoubleOrNull(),

                        defaultCommissionPercent =
                        form.defaultCommissionPercent
                            .replace(",", ".")
                            .toDoubleOrNull(),


                        status =
                        form.status
                    )


                val result =

                    if (isEditMode) {

                        contractRepository
                            .updateContract(request)

                    } else {

                        contractRepository
                            .createContract(request)
                    }

                Log.d("contract11111", request.toString())

                result
                    .onSuccess { response ->

                        val serverContractId =
                            response.data
                                ?.takeIf {
                                    it.isNotBlank() &&
                                            it !=
                                          null

                                }
                                ?: contractId


                        _uiState.update {

                            it.copy(
                                isSaving = false,

                                saveSuccess = true,

                                message =
                                response.message,

                                createdContractId =
                                serverContractId
                            )
                        }
                    }

                    .onFailure { error ->

                        _uiState.update {

                            it.copy(
                                isSaving = false,

                                saveSuccess = false,

                                message =
                                error.message
                                    ?: "خطا در ثبت قرارداد"
                            )
                        }
                    }

            } catch (e: Exception) {

                _uiState.update {

                    it.copy(
                        isSaving = false,

                        saveSuccess = false,

                        message =
                        e.message
                            ?: "خطای نامشخص"
                    )
                }
            }
        }
    }


    private fun setMessage(
        message: String
    ) {

        _uiState.update {

            it.copy(
                message = message
            )
        }
    }


    fun clearMessage() {

        _uiState.update {

            it.copy(
                message = null
            )
        }
    }
}