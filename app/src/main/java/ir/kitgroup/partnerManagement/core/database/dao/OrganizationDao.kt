package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationPersonEntity
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import ir.kitgroup.partnerManagement.core.ui.util.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface OrganizationDao {

    @Upsert
    suspend fun upsertAll(items: List<OrganizationEntity>)

    @Upsert
    suspend fun upsert(item: OrganizationEntity)


    @Query(
        """
        SELECT * FROM organizations
        WHERE syncStatus != 'PENDING_DELETE'
        ORDER BY  modifiedOn DESC
    """
    )
    fun observeAll(): Flow<List<OrganizationWithDetail>>


    @Query(
        """
    SELECT * FROM organizations
    WHERE organizationId = :id
    LIMIT 1
"""
    )
    fun observeById(id: String): Flow<OrganizationWithDetail?>


    @Query(
        """
        DELETE FROM organizations
    """
    )
    suspend fun deleteAll()


    @Query("SELECT COUNT(*) FROM organizations")
    suspend fun getCount(): Int

    /**
     * دریافت لیست تمام قراردادهای ایجاد شده آفلاین جهت قرار دادن در pendingContracts
     */
    @Query(
        """
        SELECT * FROM organizations 
        WHERE syncStatus = :syncStatus
        ORDER BY lastModifiedTimestamp ASC
    """
    )
    suspend fun getOrganizationsBySyncStatus(syncStatus: SyncStatus): List<OrganizationEntity>



    @Query("DELETE FROM organizations WHERE organizationId = :organizationId")
    suspend fun deleteById(organizationId: String)

}