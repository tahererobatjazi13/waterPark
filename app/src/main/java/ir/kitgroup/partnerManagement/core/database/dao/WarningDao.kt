package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ir.kitgroup.partnerManagement.core.database.entity.WarningEntity
import ir.kitgroup.partnerManagement.core.database.model.WarningWithDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface WarningDao {
    @Upsert
    suspend fun upsertAll(items: List<WarningEntity>)

    @Query("""
        SELECT * FROM warnings
        WHERE organizationId = :organizationId
        ORDER BY warningDate DESC
    """)
    fun observeByOrganization(
        organizationId: String
    ): Flow<List<WarningWithDetail>>

    @Query("DELETE FROM warnings")
    suspend fun deleteAll()

    @Query("DELETE FROM warnings WHERE warningId = :warningId")
    suspend fun deleteById(warningId: String)

}