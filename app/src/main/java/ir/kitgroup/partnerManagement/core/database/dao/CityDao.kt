package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ir.kitgroup.partnerManagement.core.database.entity.CityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {

    @Upsert
    suspend fun upsertAll(items: List<CityEntity>)

    @Query("SELECT * FROM cities ORDER BY name")
    fun observeAll(): Flow<List<CityEntity>>

    @Query("""
        SELECT * 
        FROM cities
        WHERE cityId = :cityId
        LIMIT 1
    """)
    fun observeById(cityId: String): Flow<CityEntity?>

    @Query("DELETE FROM cities")
    suspend fun deleteAll()
}