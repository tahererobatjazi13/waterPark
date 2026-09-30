package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ir.kitgroup.partnerManagement.core.database.entity.StatisticEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StatisticDao {

    @Upsert
    suspend fun upsert(statistic: StatisticEntity)

    @Query("SELECT * FROM statistics WHERE id = 1 LIMIT 1")
    suspend fun get(): StatisticEntity?

    @Query("SELECT * FROM statistics WHERE id = 1 LIMIT 1")
    fun observe(): Flow<StatisticEntity?>

    @Query("DELETE FROM statistics")
    suspend fun deleteAll()
}