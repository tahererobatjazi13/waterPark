package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationPersonEntity
import ir.kitgroup.partnerManagement.core.ui.util.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface OrganizationPersonDao {
    @Upsert
    suspend fun upsertAll(items: List<OrganizationPersonEntity>)

    @Query("""
        SELECT * FROM organization_persons
        WHERE organizationId = :organizationId
    """)
    fun observeByOrganization(
        organizationId: String
    ): Flow<List<OrganizationPersonEntity>>

    @Query(
        """
        SELECT *
        FROM organization_persons
        WHERE organizationId = :organizationId
        ORDER BY fullName
        """
    )
    fun observeByOrganizationId(
        organizationId: String
    ): Flow<List<OrganizationPersonEntity>>


    @Query("DELETE FROM organization_persons")
    suspend fun deleteAll()


    /**
     * دریافت لیست تمام قراردادهای ایجاد شده آفلاین جهت قرار دادن در pendingContracts
     */
    @Query(
        """
        SELECT * FROM organization_persons 
        WHERE syncStatus = :syncStatus
        ORDER BY lastModifiedTimestamp ASC
    """
    )
    suspend fun getOrganizationPersonsBySyncStatus(syncStatus: SyncStatus): List<OrganizationPersonEntity>


    @Query("DELETE FROM organization_persons WHERE orgPersonId = :orgPersonId")
    suspend fun deleteById(orgPersonId: String)

}