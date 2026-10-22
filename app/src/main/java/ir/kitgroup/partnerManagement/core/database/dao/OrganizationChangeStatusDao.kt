package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationChangeStatusEntity

@Dao
interface OrganizationChangeStatusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(item: OrganizationChangeStatusEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: OrganizationChangeStatusEntity)

    @Query("SELECT * FROM organization_change_status")
    suspend fun getAll(): List<OrganizationChangeStatusEntity>

    @Query("DELETE FROM organization_change_status WHERE organizationId = :organizationId")
    suspend fun deleteById(organizationId: String)

    @Query("DELETE FROM  organization_change_status")
    suspend fun deleteAll()

}
