package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import ir.kitgroup.partnerManagement.core.database.entity.OfferEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OfferDao {
    @Upsert
    suspend fun upsertAll(items: List<OfferEntity>)

    @Upsert
    suspend fun upsert(offer: OfferEntity)

    @Query("SELECT * FROM offers ORDER BY modifiedOn DESC")
    fun observeAll(): Flow<List<OfferEntity>>

    @Query("DELETE FROM offers")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(offer: OfferEntity): Long

    @Update
    suspend fun update(offer: OfferEntity): Int

    @Query("SELECT * FROM offers WHERE offerId = :offerId LIMIT 1")
    fun observeOfferById(offerId: String): Flow<OfferEntity?>

    @Query("SELECT COUNT(*) FROM offers")
    suspend fun getCount(): Int
}