package ir.kitgroup.partnerManagement.feature.contract.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.repository.ContractRepository
import ir.kitgroup.partnerManagement.core.ui.util.jalaliToGregorian
import ir.kitgroup.partnerManagement.feature.contract.model.TerminateActiveContractRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContractDetailViewModel @Inject constructor(
    private val contractRepository: ContractRepository
) : ViewModel() {

    private val _contract =
        MutableStateFlow<ContractEntity?>(null)

    val contract: StateFlow<ContractEntity?> =
        _contract.asStateFlow()

    private val _isUpdatingStatus =
        MutableStateFlow(false)

    val isUpdatingStatus: StateFlow<Boolean> =
        _isUpdatingStatus.asStateFlow()

    fun loadContract(contractId: String) {

        viewModelScope.launch {

            contractRepository
                .observeById(contractId)
                .collect { item ->

                    _contract.value = item
                }
        }
    }

    fun terminateActiveContract(
        contractId: String,
        organizationId: String,
        terminationDate: String,
        onSuccess: () -> Unit
    ) {
        if (_isUpdatingStatus.value) return

        viewModelScope.launch {

            _isUpdatingStatus.value = true

            try {

                // تاریخ انتخاب‌شده در UI جلالی است
                // API تاریخ میلادی می‌خواهد
                val gregorianTerminationDate =
                    jalaliToGregorian(
                        terminationDate
                    ).orEmpty()

                val request =
                    TerminateActiveContractRequest(
                        contractId = contractId,
                        organizationId = organizationId,
                        terminationDate = gregorianTerminationDate
                    )

                Log.d(
                    "ContractTermination",
                    "Request = $request"
                )

                val result =
                    contractRepository
                        .terminateActiveContract(request)

                result
                    .onSuccess {

                        // اطلاعات قرارداد را مجدداً از دیتابیس بخوان
                        loadContract(contractId)

                        onSuccess()
                    }
                    .onFailure { error ->

                        // این قسمت را مطابق State خطای فعلی
                        // ContractDetailViewModel خودت مدیریت کن

                        Log.e(
                            "ContractTermination",
                            "Termination failed",
                            error
                        )
                    }

            } catch (e: Exception) {

                Log.e(
                    "ContractTermination",
                    "Termination exception",
                    e
                )

            } finally {

                _isUpdatingStatus.value = false
            }
        }
    }
}