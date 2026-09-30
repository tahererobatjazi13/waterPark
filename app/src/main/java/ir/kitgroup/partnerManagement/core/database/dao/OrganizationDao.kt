package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationEntity
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
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
        ORDER BY name
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
}