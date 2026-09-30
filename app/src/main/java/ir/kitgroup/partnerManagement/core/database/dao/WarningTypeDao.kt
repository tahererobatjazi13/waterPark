package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ir.kitgroup.partnerManagement.core.database.entity.WarningTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WarningTypeDao {
    @Upsert
    suspend fun upsertAll(items: List<WarningTypeEntity>)

    @Query("SELECT * FROM warning_types ORDER BY title")
    fun observeAll(): Flow<List<WarningTypeEntity>>

    @Query("DELETE FROM warning_types")
    suspend fun deleteAll()
}