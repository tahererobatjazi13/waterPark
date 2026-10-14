package ir.kitgroup.partnerManagement.feature.offer.ui.serial_offer.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.database.model.AssignedSerialWithDetail
import ir.kitgroup.partnerManagement.core.repository.AssignedSerialsRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssignedSerialDetailViewModel @Inject constructor(
    private val assignedSerialsRepository: AssignedSerialsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // دریافت مستقیم serialId از آرگومان‌های ناوبری
    val serialId: String = checkNotNull(savedStateHandle.get<String>("serialId"))

    // مشاهده لحظه‌ای جزئیات سریال از دیتابیس لوکال
    val serialDetail: StateFlow<AssignedSerialWithDetail?> = assignedSerialsRepository
        .observeById(serialId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    // وضعیت در حال بارگذاری عملیات (مانند تغییر وضعیت)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // نگهداری آیتم انتخاب‌شده برای دیالوگ/باتم‌شیت تغییر وضعیت
    private val _statusDialogItem = MutableStateFlow<AssignedSerialWithDetail?>(null)
    val statusDialogItem: StateFlow<AssignedSerialWithDetail?> = _statusDialogItem.asStateFlow()

    // مدیریت پیام‌های یک‌بارمصرف (Toast/SnackBar)
    private val _message = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val message: SharedFlow<String> = _message.asSharedFlow()

    init {
        // همگام‌سازی تکی با سرور (در صورت نیاز به آخرین دیتا)
        fetchLatestDetail()
    }

    private fun fetchLatestDetail() {
        if (serialId.isBlank()) return
        viewModelScope.launch {
            try {
                assignedSerialsRepository.observeById(serialId)
            } catch (_: Exception) {
                // خطا در فچ شبکه مانع نمایش دیتای لوکال Cache شده نمی‌شود
            }
        }
    }

    /**
     * باز کردن مودال یا دیالوگ تغییر وضعیت
     */
    fun openStatusChangeDialog(item: AssignedSerialWithDetail) {
        _statusDialogItem.value = item
    }

    /**
     * بستن دیالوگ تغییر وضعیت
     */
    fun dismissStatusDialog() {
        _statusDialogItem.value = null
    }

    /**
     * متد ارسال درخواست تغییر وضعیت طرح سریال
     * @param newStatusId شناسه وضعیت جدید (از ContractOfferStatus)
     */
    fun updateStatus(newStatusId: String) {
        val currentItem = serialDetail.value ?: return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = assignedSerialsRepository.serialOfferChangeStatus(
                    id = currentItem.assignedSerial.serialAssignmentId,
                    status = newStatusId
                )

                if (result.isSuccess) {
                    _message.emit("وضعیت با موفقیت تغییر کرد")
                    dismissStatusDialog()
                } else {
                    _message.emit((result ?: "خطا در تغییر وضعیت").toString())
                }
            } catch (e: Exception) {
                _message.emit(e.localizedMessage ?: "خطای ناشناخته در برقراری ارتباط")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
