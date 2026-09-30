package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ir.kitgroup.partnerManagement.core.database.entity.StandTypeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StandTypeDao {
    @Upsert
    suspend fun upsertAll(items: List<StandTypeEntity>)

    @Query("SELECT * FROM stand_types ORDER BY title")
    fun observeAll(): Flow<List<StandTypeEntity>>

    @Query("DELETE FROM stand_types")
    suspend fun deleteAll()

    @Query("DELETE FROM stand_types WHERE standTypeId = :id")
    suspend fun deleteById(id: String)

}