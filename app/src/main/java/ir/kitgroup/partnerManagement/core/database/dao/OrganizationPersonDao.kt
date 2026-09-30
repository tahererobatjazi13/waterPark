package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationPersonEntity
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

    @Query("DELETE FROM organization_persons")
    suspend fun deleteAll()
}