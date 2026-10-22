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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(items: List<AttachmentFileEntity>)
}
