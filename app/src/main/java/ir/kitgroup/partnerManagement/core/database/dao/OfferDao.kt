package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ir.kitgroup.partnerManagement.core.database.entity.OfferEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface OfferDao {
    @Upsert
    suspend fun upsertAll(items: List<OfferEntity>)

    @Query("SELECT * FROM offers ORDER BY title")
    fun observeAll(): Flow<List<OfferEntity>>

    @Query("SELECT * FROM offers WHERE offerId = :offerId LIMIT 1")
    fun observeById(offerId: String): Flow<OfferEntity?>

    @Query("DELETE FROM offers")
    suspend fun deleteAll()
}