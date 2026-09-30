package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "assigned_serials",
    indices = [
        Index("organizationId"),
        Index("offerId"),
        Index("contractId"),
        Index("meetingId")
    ]
)
data class AssignedSerialEntity(
    @PrimaryKey
    val serialAssignmentId: String,

    val name: String? = null,

    val organizationId: String? = null,

    val organizationName: String? = null,

    val offerId: String? = null,

    val offerName: String? = null,

    val contractId: String? = null,

    val serialPrefix: String? = null,

    val fromSerial: Int? = null,

    val toSerial: Int? = null,

    val count: Int? = null,

    val assignmentDate: String? = null,

    val contractOfferStatus: Int? = null,

    val contractOfferStatusName: String? = null,

    val meetingId: String? = null,

    val lastSerialUsed: Int? = null,

    val remainingSerialCount: Int? = null,

    val stateCode: Int? = null,

    ) /*{
    */
/**
 * وضعیت فعال بودن رکورد تخصیص سریال در CRM
 *//*
    val isActive: Boolean
        get() = stateCode == 0

    */
/**
 * بررسی اتمام سریال‌های تخصیص‌یافته
 *//*
    val isExhausted: Boolean
        get() = remainingSerialCount <= 0

    */
/**
 * شماره سریال بعدی قابل استفاده برای تخصیص
 *//*
    val nextAvailableSerial: Int?
        get() = if (lastSerialUsed < toSerial) lastSerialUsed + 1 else null

    */
/**
 * درصد پیشروی مصرف سریال‌ها برای نمایش در UI (Progress)
 *//*
    val usagePercent: Int
        get() = if (count > 0) (((count - remainingSerialCount).coerceAtLeast(0) * 100) / count) else 0

    */
/**
 * نمایش قالب بازه سریال مانند: ABC-1000 تا ABC-1019
 *//*
    val formattedRange: String
        get() = "${serialPrefix ?: ""}-$fromSerial تا ${serialPrefix ?: ""}-$toSerial"
}
*/