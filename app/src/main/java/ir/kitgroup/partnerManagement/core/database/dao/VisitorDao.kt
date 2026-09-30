package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ir.kitgroup.partnerManagement.core.database.entity.VisitorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitorDao {
    @Upsert
    suspend fun upsertAll(items: List<VisitorEntity>)

    @Query("SELECT * FROM visitors ORDER BY name")
    fun observeAll(): Flow<List<VisitorEntity>>

    @Query("DELETE FROM visitors")
    suspend fun deleteAll()
}