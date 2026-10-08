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

    @Query(
        """
        SELECT * FROM regions
        WHERE cityId = :cityId
        ORDER BY name
    """
    )
    fun observeByCity(cityId: String): Flow<List<RegionEntity>>


    @Query("DELETE FROM regions")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM regions")
    suspend fun getCount(): Int

}