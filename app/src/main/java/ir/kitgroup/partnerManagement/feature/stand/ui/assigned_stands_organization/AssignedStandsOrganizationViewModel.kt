package ir.kitgroup.partnerManagement.feature.stand.ui.assigned_stands_organization

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.model.AssignedStandWithDetail
import ir.kitgroup.partnerManagement.core.repository.AssignedStandsRepository
import ir.kitgroup.partnerManagement.feature.dashboard.repository.SyncRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssignedStandsOrganizationViewModel @Inject constructor(
    private val assignedStandsRepository: AssignedStandsRepository, // افزودن private val برای تبدیل به property کلاس
    private val syncRepository: SyncRepository
) : ViewModel() {

    private val _assignments = MutableStateFlow<List<AssignedStandWithDetail>>(emptyList())
    val assignments: StateFlow<List<AssignedStandWithDetail>> = _assignments.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadAssignedStands()
    }

    /**
     * واکشی و اشتراک در داده‌های استندهای تخصیص‌یافته به سازمان‌ها
     */
    fun loadAssignedStands() {
        loadJob?.cancel()

        loadJob = viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            assignedStandsRepository.getOrganizationAssignedStandsWithDetail()
                .catch { throwable ->
                    Log.e("AssignedStandsVM", "Error collecting assigned stands", throwable)
                    _errorMessage.value = throwable.localizedMessage ?: "خطا در دریافت لیست استندها"
                    _isLoading.value = false
                }
                .collect { standList ->
                    _assignments.value = standList
                    _isLoading.value = false
                }
        }
    }
}
