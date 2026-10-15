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
import ir.kitgroup.partnerManagement.core.network.model.ModelResponse
import ir.kitgroup.partnerManagement.core.ui.util.extractErrorMessage
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

    fun observeOfferDetailById(offerId: String): Flow<List<OfferDetailWithProduct>> {
        return offerDetailDao.observeWithProductByOfferId(offerId)
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

    suspend fun deleteOfferDetailLine(
        offerDetailId: String
    ): Result<ModelResponse> {
        return try {
            val response = apiService.deleteOfferDetailPlan(offerDetailId)

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.success) {
                    // به‌روزرسانی رکورد در دیتابیس لوکال
                    offerDetailDao.deleteByDetailId(offerDetailId)
                    Result.success(body)
                } else {
                    Result.failure(Exception(body.message ?: "خطا در حذف"))
                }
            } else {
                val errorMessage = extractErrorMessage(
                    errorBody = response.errorBody()?.string(),
                    defaultMessage = "خطای سرور در حذف: ${response.code()}"
                )
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


