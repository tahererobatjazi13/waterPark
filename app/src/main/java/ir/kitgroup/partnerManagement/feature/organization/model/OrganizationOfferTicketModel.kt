package ir.kitgroup.partnerManagement.feature.organization.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import ir.kitgroup.partnerManagement.R

data class OrganizationOfferTicketModel(
    val id: Int,
    val title: String,                           // عنوان کامل آفر
    val offerPlanName: String,                    // طرح / آفر (مثال: فروش بلیط تابستانه)
    val contractName: String,                     // قرارداد (مثال: قرارداد فروش مهمانپذیر شبنم)
    val organizationName: String,                 // نام سازمان (مثال: مهمانپذیر شبنم)
    val cooperationModel: String,                 // مدل همکاری (مثال: بلیط تخفیف دار)
    val serialPrefix: String,                     // کد مخفف سریال (مثال: DC)
    val serialCount: Int,                         // تعداد سریال (مثال: 7)
    val startSerial: Long,                        // شروع سریال (مثال: 100)
    val endSerial: Long,                          // پایان سریال (مثال: 106)
    val status: TicketSerialStatus = TicketSerialStatus.DRAFT, // وضعیت سریالها
    val lastUsedSerial: Long? = null,             // آخرین سریال استفاده شده
    val ownerName: String = ""                    // مالک
)


enum class TicketSerialStatus(
    @StringRes val labelRes: Int,
    val containerColor: Color,
    val contentColor: Color
) {
    DRAFT(R.string.status_draft, Color(0xFFE0E0E0), Color(0xFF424242)),
    ACTIVE(R.string.status_active, Color(0xFFE8F5E9), Color(0xFF2E7D32)),
    COMPLETED(R.string.status_completed, Color(0xFFE3F2FD), Color(0xFF1565C0)),
    EXPIRED(R.string.status_expired, Color(0xFFFFEBEE), Color(0xFFC62828))
}