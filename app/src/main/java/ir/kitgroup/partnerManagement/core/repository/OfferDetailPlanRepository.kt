package ir.kitgroup.partnerManagement.core.repository

import ir.kitgroup.partnerManagement.core.database.dao.OfferDetailDao
import ir.kitgroup.partnerManagement.core.database.dao.ProductDao
import ir.kitgroup.partnerManagement.core.database.entity.OfferDetailEntity
import ir.kitgroup.partnerManagement.core.database.entity.OfferEntity
import ir.kitgroup.partnerManagement.core.database.entity.ProductEntity
import ir.kitgroup.partnerManagement.core.database.model.OfferDetailWithProduct
import ir.kitgroup.partnerManagement.core.network.apiService.ApiService
import ir.kitgroup.partnerManagement.core.network.model.ModelResponse
import ir.kitgroup.partnerManagement.core.ui.util.extractErrorMessage
import ir.kitgroup.partnerManagement.feature.offer.model.CreateOfferDetailPlanRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// در فایل OfferDetailRepository.kt
class OfferDetailPlanRepository @Inject constructor(
    private val apiService: ApiService,
    private val offerDetailDao: OfferDetailDao,
    private val productDao: ProductDao
) {
    fun getProducts(): Flow<List<ProductEntity>> = productDao.observeAll() // بدون suspend


    fun getOfferById(offerId: String): Flow<List<OfferDetailWithProduct>> {
        return offerDetailDao.observeWithProductByOfferId(offerId)
    }


    // این متد باید suspend باشد تا بتواند متد suspend دیتابیس را صدا کند
//    suspend fun observeById(offerId: String): OfferDetailWithProduct? {
//        return offerDetailDao.observeById(offerId)
//    }
    suspend fun createOfferDetailPlan(
        request: CreateOfferDetailPlanRequest/*,
        entityToCache: OfferDetailEntity*/
    ): Result<ModelResponse> {
        return try {
            val response = apiService.createOfferDetailPlan(request)

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.success) {
                    // کش کردن رکورد جدید در دیتابیس لوکال
                   // offerDetailDao.insert(entityToCache)
                    Result.success(body)
                } else {
                    Result.failure(Exception(body.message ?: "خطا در ثبت طرح تخفیف"))
                }
            } else {
                val errorMessage = extractErrorMessage(
                    errorBody = response.errorBody()?.string(),
                    defaultMessage = "خطای سرور در ثبت طرح تخفیف: ${response.code()}"
                )
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateOfferDetailPlan(
        request: CreateOfferDetailPlanRequest/*,
        entityToUpdate: OfferDetailEntity*/
    ): Result<ModelResponse> {
        return try {
            val response = apiService.updateOfferDetailPlan(request)

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.success) {
                    // به‌روزرسانی رکورد در دیتابیس لوکال
                   // offerDao.update(entityToUpdate)
                    Result.success(body)
                } else {
                    Result.failure(Exception(body.message ?: "خطا در ویرایش طرح تخفیف"))
                }
            } else {
                val errorMessage = extractErrorMessage(
                    errorBody = response.errorBody()?.string(),
                    defaultMessage = "خطای سرور در ویرایش طرح تخفیف: ${response.code()}"
                )
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
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


