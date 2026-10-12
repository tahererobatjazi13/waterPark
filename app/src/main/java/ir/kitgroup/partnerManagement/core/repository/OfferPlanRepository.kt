package ir.kitgroup.partnerManagement.core.repository

import ir.kitgroup.partnerManagement.core.database.dao.OfferDao
import ir.kitgroup.partnerManagement.core.database.entity.OfferEntity
import ir.kitgroup.partnerManagement.core.database.model.MeetingWithDetail
import ir.kitgroup.partnerManagement.core.network.apiService.ApiService
import ir.kitgroup.partnerManagement.core.network.model.ModelResponse
import ir.kitgroup.partnerManagement.core.ui.util.extractErrorMessage
import ir.kitgroup.partnerManagement.feature.offer.model.CreateOfferPlanRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OfferPlanRepository @Inject constructor(
    private val offerDao: OfferDao,
    private val apiService: ApiService
) {
    // این متد باید suspend باشد تا بتواند متد suspend دیتابیس را صدا کند
    suspend fun getOfferById(offerId: String): OfferEntity? {
        return offerDao.getOfferById(offerId)
    }

    fun observeAll(
    ): Flow<List<OfferEntity>> {
        return offerDao.observeAll()
    }
    suspend fun createOfferPlan(
        request: CreateOfferPlanRequest/*,
        entityToCache: OfferEntity*/
    ): Result<ModelResponse> {
        return try {
            val response = apiService.createOfferPlan(request)

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.success) {
                    // کش کردن رکورد جدید در دیتابیس لوکال
                //    offerDao.insert(entityToCache)
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

    suspend fun updateOfferPlan(
        request: CreateOfferPlanRequest/*,
        entityToUpdate: OfferEntity*/
    ): Result<ModelResponse> {
        return try {
            val response = apiService.updateOfferPlan(request)

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.success) {
                    // به‌روزرسانی رکورد در دیتابیس لوکال
                    //offerDao.update(entityToUpdate)
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
}
