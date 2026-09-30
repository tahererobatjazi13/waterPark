package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ir.kitgroup.partnerManagement.core.database.entity.RegionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RegionDao {

    @Upsert
    suspend fun upsertAll(items: List<RegionEntity>)

    @Query("""
        SELECT * FROM regions
        WHERE cityId = :cityId
        ORDER BY name
    """)
    fun observeByCity(cityId: String): Flow<List<RegionEntity>>

    @Query("""
        SELECT * 
        FROM regions
        WHERE regionId = :regionId
        LIMIT 1
    """)
    fun observeById(regionId: String): Flow<RegionEntity?>

    @Query("DELETE FROM regions")
    suspend fun deleteAll()
}