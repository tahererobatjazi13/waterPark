package ir.kitgroup.partnerManagement.core.repository

import ir.kitgroup.partnerManagement.core.database.dao.AssignedSerialDao
import ir.kitgroup.partnerManagement.core.database.entity.AssignedSerialEntity
import ir.kitgroup.partnerManagement.core.database.model.AssignedSerialWithDetail
import ir.kitgroup.partnerManagement.core.network.apiService.ApiService
import ir.kitgroup.partnerManagement.core.network.model.ModelResponse
import ir.kitgroup.partnerManagement.core.ui.util.extractErrorMessage
import ir.kitgroup.partnerManagement.feature.offer.model.CreateSerialOfferRequest
import ir.kitgroup.partnerManagement.feature.stand.model.ChangeAssignStandStatusRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssignedSerialsRepository @Inject constructor(
    private val assignedSerialDao: AssignedSerialDao,
    private val apiService: ApiService,
) {

    fun observeByOrganization(organizationId: String): Flow<List<AssignedSerialWithDetail>> =
        assignedSerialDao.observeByOrganization(organizationId)

    suspend fun upsertAll(items: List<AssignedSerialEntity>) =
        assignedSerialDao.upsertAll(items)

    fun observeById(id: String) = assignedSerialDao.observeById(id)

    suspend fun deleteAll() = assignedSerialDao.deleteAll()

    suspend fun createSerialOffer(
        request: CreateSerialOfferRequest
    ): Result<ModelResponse> {
        return try {
            val response = apiService.assignSerialOffer(request)

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.success) {
                    Result.success(body)
                } else {
                    Result.failure(Exception(body.message ?: "خطا در ثبت تخصیص سریال"))
                }
            } else {
                val errorMessage = extractErrorMessage(
                    errorBody = response.errorBody()?.string(),
                    defaultMessage = "خطای سرور در ثبت تخصیص سریال: ${response.code()}"
                )
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateSerialOffer(
        request: CreateSerialOfferRequest
    ): Result<ModelResponse> {
        return try {
            val response = apiService.updateSerialOffer(request)

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.success) {
                    // به‌روزرسانی رکورد در دیتابیس لوکال
                    Result.success(body)
                } else {
                    Result.failure(Exception(body.message ?: "خطا در ویرایش تخصیص سریال"))
                }
            } else {
                val errorMessage = extractErrorMessage(
                    errorBody = response.errorBody()?.string(),
                    defaultMessage = "خطای سرور در ویرایش تخصیص سریال: ${response.code()}"
                )
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun serialOfferChangeStatus(
        id: String,
        status: String
    ): Result<ModelResponse> {
        return try {
            val response = apiService.serialOfferChangeStatus(id,status)

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.success) {
                    // به‌روزرسانی رکورد در دیتابیس لوکال
                 //   offerDetailDao.deleteByDetailId(offerDetailId)
                    Result.success(body)
                } else {
                    Result.failure(Exception(body.message ?: "خطا  "))
                }
            } else {
                val errorMessage = extractErrorMessage(
                    errorBody = response.errorBody()?.string(),
                    defaultMessage = "خطای سرور  : ${response.code()}"
                )
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
