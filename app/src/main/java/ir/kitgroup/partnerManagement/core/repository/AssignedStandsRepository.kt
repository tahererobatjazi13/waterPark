package ir.kitgroup.partnerManagement.core.repository

import android.util.Log
import ir.kitgroup.partnerManagement.core.database.dao.AssignedStandDao
import ir.kitgroup.partnerManagement.core.database.entity.AssignedStandEntity
import ir.kitgroup.partnerManagement.core.database.model.AssignedStandWithDetail
import ir.kitgroup.partnerManagement.core.network.apiService.ApiService
import ir.kitgroup.partnerManagement.core.network.model.ModelResponse
import ir.kitgroup.partnerManagement.core.ui.util.extractErrorMessage
import ir.kitgroup.partnerManagement.feature.stand.model.ChangeAssignStandStatusRequest
import ir.kitgroup.partnerManagement.feature.stand.model.CreateAssignStandRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssignedStandsRepository @Inject constructor(
    private val assignedStandDao: AssignedStandDao,
    private val apiService: ApiService
) {

    fun observeByOrganizationWithDetail(organizationId: String): Flow<List<AssignedStandWithDetail>> =
        assignedStandDao.observeByOrganizationWithDetail(organizationId)

    fun getOrganizationAssignedStandsWithDetail(): Flow<List<AssignedStandWithDetail>> =
        assignedStandDao.getOrganizationAssignedStandsWithDetail()

    fun getVisitorAssignedStandsWithDetail(): Flow<List<AssignedStandWithDetail>> =
        assignedStandDao.getVisitorAssignedStandsWithDetail()

    fun observeByIdWithDetail(assignedStandId: String): Flow<AssignedStandWithDetail> =
        assignedStandDao.observeByIdWithDetail(assignedStandId)

    suspend fun upsertAll(items: List<AssignedStandEntity>) =
        assignedStandDao.upsertAll(items)

    suspend fun deleteAll() = assignedStandDao.deleteAll()


    suspend fun createAssignStand(
        request: CreateAssignStandRequest
    ): Result<ModelResponse> {

        return try {

            val response =
                apiService.createAssignStand(request)

            if (
                response.isSuccessful &&
                response.body() != null
            ) {

                val body =
                    response.body()!!

                if (body.success) {

                    Result.success(body)

                } else {

                    Result.failure(
                        Exception(
                            body.message
                                ?: "خطا در ثبت انتساب استندها"
                        )
                    )
                }

            } else {
                val errorMessage = extractErrorMessage(
                    errorBody = response.errorBody()?.string(),
                    defaultMessage = "خطای سرور درانتساب استندها: ${response.code()}"
                )

                Result.failure(
                    Exception(errorMessage)
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun updateAssignStand(
        request: CreateAssignStandRequest
    ): Result<ModelResponse> {

        return try {

            val response =
                apiService.updateAssignStand(request)

            if (
                response.isSuccessful &&
                response.body() != null
            ) {

                val body =
                    response.body()!!

                if (body.success) {

                    Result.success(body)

                } else {

                    Result.failure(
                        Exception(
                            body.message
                                ?: "خطا در ویرایش انتساب استندها"
                        )
                    )
                }

            } else {
                val errorMessage = extractErrorMessage(
                    errorBody = response.errorBody()?.string(),
                    defaultMessage = "خطای سرور در انتساب استندها: ${response.code()}"
                )

                Log.d(
                    "MeetingRequestErrorMessage",
                    errorMessage
                )
                Result.failure(
                    Exception(errorMessage)
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun changeStatusAssignStand(
        request: ChangeAssignStandStatusRequest
    ): Result<ModelResponse> {

        return try {

            val response =
                apiService.changeAssignStandStatus(request)

            if (
                response.isSuccessful &&
                response.body() != null
            ) {

                val body =
                    response.body()!!

                if (body.success) {

                    Result.success(body)

                } else {

                    Result.failure(
                        Exception(
                            body.message
                                ?: "خطا در تغییر وضعیت"
                        )
                    )
                }

            } else {

                val errorMessage = extractErrorMessage(
                    errorBody = response.errorBody()?.string(),
                    defaultMessage = "خطای سرور در تغییر وضعیت: ${response.code()}"
                )

                Result.failure(
                    Exception(errorMessage)
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }


}
