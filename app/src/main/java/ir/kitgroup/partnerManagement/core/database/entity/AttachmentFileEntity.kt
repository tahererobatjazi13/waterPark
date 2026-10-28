package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "attachments_file",
    indices = [
        Index(value = ["organizationId"]),
        Index(value = ["organizationId", "fileType"])
    ]
)
data class AttachmentFileEntity(
    @PrimaryKey
    val attachmentFileId: String,

    val organizationId: String,
    val meetingId: String?,

    val fileType: Int = 0,
    val description: String? = null,

    // اطلاعات سرور
    val fileUrl: String? = null,
    val relativePath: String? = null,
    val mimeType: String? = null,
    val fileSize: Long = 0L,
    val createdOn: String? = null,

    // مسیر کش محلی (برای آفلاین واقعی)
    val localPath: String? = null,

    val isHeader: Boolean = false, // اگر در اپ شما "سردر" یک نوع خاص نیست، این فیلد کمک می‌کند
    val lastSyncTimestamp: Long = System.currentTimeMillis()
)
