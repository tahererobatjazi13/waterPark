package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import ir.kitgroup.partnerManagement.core.database.entity.AssignedSerialEntity
import ir.kitgroup.partnerManagement.core.database.model.AssignedSerialWithDetail
import ir.kitgroup.partnerManagement.core.database.model.OfferDetailWithProduct
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

    @Transaction
    @Query("SELECT * FROM assigned_serials WHERE serialAssignmentId = :serialAssignmentId LIMIT 1")
    fun observeById(serialAssignmentId: String): Flow<AssignedSerialWithDetail?>


    @Query("DELETE FROM assigned_serials")
    suspend fun deleteAll()
}