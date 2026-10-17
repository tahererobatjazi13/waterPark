package ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail_plan_list.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.dao.OfferDetailDao
import ir.kitgroup.partnerManagement.core.database.dao.ProductDao
import ir.kitgroup.partnerManagement.core.database.entity.ProductEntity
import ir.kitgroup.partnerManagement.core.database.model.OfferDetailWithProduct
import ir.kitgroup.partnerManagement.core.network.apiService.ApiService
import ir.kitgroup.partnerManagement.core.network.exception.ApiRequestExecutor
import ir.kitgroup.partnerManagement.feature.dashboard.repository.SyncRepository
import ir.kitgroup.partnerManagement.feature.offer.model.OfferDetailPlanRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfferDetailPlanRepository @Inject constructor(
    private val apiService: ApiService,
    private val offerDetailDao: OfferDetailDao,
    private val productDao: ProductDao,
    private val syncRepository: SyncRepository,
    @ApplicationContext private val context: Context
) {
    fun getProducts(): Flow<List<ProductEntity>> = productDao.observeAll()

    fun observeOfferDetailWithProductByOfferId(offerId: String): Flow<List<OfferDetailWithProduct>> {
        return offerDetailDao.observeOfferDetailWithProductByOfferId(offerId)
    }

    fun observeOfferDetailById(offerDetailId: String?): Flow<OfferDetailWithProduct?> {
        return if (offerDetailId.isNullOrBlank()) {
            kotlinx.coroutines.flow.flowOf(null)
        } else {
            offerDetailDao.observeOfferDetailById(offerDetailId)
        }
    }

    suspend fun createOfferDetailPlanAndSync(
        request: OfferDetailPlanRequest
    ): Result<String?> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_offer_detail_plan_create)
        ) {
            apiService.createOfferDetailPlan(request)
        }.fold(
            onSuccess = { response ->
                syncRepository.syncData()
                Result.success(response.message)
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }


    suspend fun updateOfferDetailPlanAndSync(
        request: OfferDetailPlanRequest
    ): Result<String?> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_offer_detail_plan_update)
        ) {
            apiService.updateOfferDetailPlan(request)
        }.fold(
            onSuccess = { response ->
                syncRepository.syncData()
                Result.success(response.message)
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }


    suspend fun deleteOfferDetailPlan(
        offerDetailId: String
    ): Result<String?> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_offer_detail_plan_delete)
        ) {
            apiService.deleteOfferDetailPlan(offerDetailId)
        }.fold(
            onSuccess = { response ->
                offerDetailDao.deleteByDetailId(offerDetailId)
                Result.success(response.message)
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }

}


