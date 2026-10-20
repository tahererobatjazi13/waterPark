package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.ui.util.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ContractDao {

    // --- ذخیره و به‌روزرسانی محلی ---

    @Upsert
    suspend fun upsert(item: ContractEntity)

    @Upsert
    suspend fun upsertAll(items: List<ContractEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(item: ContractEntity)


    // --- کوئری‌های عمومی UI (فیلتر کردن رکوردهای در حال حذف آفلاین) ---
    @Query(
        """
        SELECT * FROM contracts
        WHERE syncStatus != 'PENDING_DELETE'
        ORDER BY  startDate DESC
    """
    )
    fun observeAll(): Flow<List<ContractEntity>>

    @Query(
        """
        SELECT * FROM contracts
        WHERE organizationId = :organizationId
          AND syncStatus != 'PENDING_DELETE'
        ORDER BY startDate DESC
    """
    )

    fun observeByOrganization(
        organizationId: String
    ): Flow<List<ContractEntity>>

    @Query(
        """
        SELECT * FROM contracts
        WHERE organizationId = :organizationId
          AND status = 1
          AND syncStatus != 'PENDING_DELETE'
        ORDER BY startDate DESC
    """
    )
    fun observeActiveByOrganization(
        organizationId: String
    ): Flow<List<ContractEntity>>

    @Query("SELECT * FROM contracts WHERE contractId = :contractId LIMIT 1")
    fun observeById(contractId: String): Flow<ContractEntity?>

    @Query("SELECT * FROM contracts WHERE contractId = :contractId LIMIT 1")
    suspend fun getById(contractId: String): ContractEntity?

    /**
     * دریافت لیست تمام قراردادهای ایجاد شده آفلاین جهت قرار دادن در pendingContracts
     */
    @Query(
        """
        SELECT * FROM contracts 
        WHERE syncStatus = :syncStatus
        ORDER BY lastModifiedTimestamp ASC
    """
    )
    suspend fun getContractsBySyncStatus(syncStatus: SyncStatus): List<ContractEntity>

    /**
     * شمارش کل موارد پندینگ مربوط به قراردادها (جهت نمایش بج یا شمارنده در داشبورد)
     */
    @Query(
        """
        SELECT COUNT(*) FROM contracts 
        WHERE syncStatus != 'SYNCED'
    """
    )
    fun observePendingCount(): Flow<Int>

    // --- پاک‌سازی ---
    @Query("DELETE FROM contracts WHERE contractId = :contractId")
    suspend fun deleteById(contractId: String)

    @Query("DELETE FROM contracts")
    suspend fun deleteAll()

    @Query("UPDATE contracts SET syncStatus = :status, isLocallyModified = :isLocallyModified WHERE contractId = :contractId")
    suspend fun updateSyncStatus(
        contractId: String,
        status: SyncStatus,
        isLocallyModified: Boolean
    )
}
