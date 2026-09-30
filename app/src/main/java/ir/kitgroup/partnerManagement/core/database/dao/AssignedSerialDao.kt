package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ir.kitgroup.partnerManagement.core.database.entity.AssignedSerialEntity
import ir.kitgroup.partnerManagement.core.database.model.AssignedSerialWithDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface AssignedSerialDao {
    @Upsert
    suspend fun upsertAll(items: List<AssignedSerialEntity>)

    @Query("""
        SELECT * FROM assigned_serials
        WHERE organizationId = :organizationId
    """)
    fun observeByOrganization(
        organizationId: String
    ): Flow<List<AssignedSerialWithDetail>>

    @Query("DELETE FROM assigned_serials")
    suspend fun deleteAll()
}