package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.database.entity.WarningEntity
import ir.kitgroup.partnerManagement.core.database.model.WarningWithDetail
import ir.kitgroup.partnerManagement.core.ui.util.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface WarningDao {
    @Upsert
    suspend fun upsertAll(items: List<WarningEntity>)

    @Upsert
    suspend fun upsert(item: WarningEntity)


    @Query("""
        SELECT * FROM warnings
        WHERE organizationId = :organizationId
        ORDER BY warningDate DESC
    """)
    fun observeByOrganization(
        organizationId: String
    ): Flow<List<WarningWithDetail>>

    @Transaction
    @Query("""
        SELECT * FROM warnings
        WHERE warningId = :warningId
        LIMIT 1
    """)
    fun observeById(warningId: String): Flow<WarningWithDetail?>


    @Query("DELETE FROM warnings")
    suspend fun deleteAll()

    @Query("DELETE FROM warnings WHERE warningId = :warningId")
    suspend fun deleteById(warningId: String)

    @Query(
        """
        SELECT * FROM warnings 
        WHERE syncStatus = :syncStatus
        ORDER BY lastModifiedTimestamp ASC
    """
    )
    suspend fun getWarningsBySyncStatus(syncStatus: SyncStatus): List<WarningEntity>


    @Query("UPDATE warnings SET syncStatus = :status, isLocallyModified = :isLocallyModified WHERE warningId = :warningId")
    suspend fun updateSyncStatus(
        warningId: String,
        status: SyncStatus,
        isLocallyModified: Boolean
    )

    @Query("SELECT COUNT(*) FROM warnings WHERE syncStatus IN ('PENDING_INSERT', 'PENDING_UPDATE')")
    fun observePendingCount(): Flow<Int>
}