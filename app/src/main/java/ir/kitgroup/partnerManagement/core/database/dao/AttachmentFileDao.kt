package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.kitgroup.partnerManagement.core.database.entity.AttachmentFileEntity

@Dao
interface AttachmentFileDao {

    @Query("""
        SELECT * FROM attachments_file 
        WHERE organizationId = :orgId
        ORDER BY 
            CASE WHEN isHeader = 1 THEN 0 ELSE 1 END,
            createdOn DESC
        LIMIT 1
    """)
    suspend fun getBestThumbnail(orgId: String): AttachmentFileEntity?

    @Query("SELECT * FROM attachments_file WHERE organizationId IN (:orgIds)")
    suspend fun getAttachmentsForOrganizations(orgIds: List<String>): List<AttachmentFileEntity>



    @Query("SELECT * FROM attachments_file WHERE attachmentFileId = :fileId")
    suspend fun getById(fileId: String): AttachmentFileEntity?

    @Query("UPDATE attachments_file SET localPath = :path WHERE attachmentFileId = :fileId")
    suspend fun updateLocalPath(fileId: String, path: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<AttachmentFileEntity>)


    // پیوست‌های سازمان (بدون بازدید)
    @Query("SELECT * FROM attachments_file WHERE organizationId = :orgId AND meetingId IS NULL")
    suspend fun getOrganizationAttachments(orgId: String): List<AttachmentFileEntity>

    // ب) پیوست‌های یک بازدید خاص برای آن سازمان
    @Query("SELECT * FROM attachments_file WHERE organizationId = :orgId AND meetingId = :meetingId")
    suspend fun getMeetingAttachments(orgId: String, meetingId: String): List<AttachmentFileEntity>

}
