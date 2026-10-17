package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import ir.kitgroup.partnerManagement.core.database.entity.OfferDetailEntity
import ir.kitgroup.partnerManagement.core.database.model.OfferDetailWithProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface OfferDetailDao {

    @Upsert
    suspend fun upsertAll(items: List<OfferDetailEntity>)

    @Query("SELECT * FROM offer_details  WHERE offerId = :offerId ORDER BY modifiedOn DESC")
    fun observeByOfferId(offerId: String): Flow<List<OfferDetailEntity>>

    @Transaction
    @Query("SELECT * FROM offer_details WHERE offerId = :offerId")
    fun observeOfferDetailWithProductByOfferId(offerId: String): Flow<List<OfferDetailWithProduct>>

    @Transaction
    @Query("SELECT * FROM offer_details WHERE offerDetailId = :offerDetailId LIMIT 1")
    fun observeOfferDetailById(offerDetailId: String): Flow<OfferDetailWithProduct?>

    @Query("DELETE FROM offer_details WHERE offerDetailId = :offerDetailId")
    suspend fun deleteByDetailId(offerDetailId: String)

}