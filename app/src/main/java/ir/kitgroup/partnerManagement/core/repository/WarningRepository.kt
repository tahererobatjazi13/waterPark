package ir.kitgroup.partnerManagement.core.repository

import android.util.Log
import ir.kitgroup.partnerManagement.core.database.dao.WarningDao
import ir.kitgroup.partnerManagement.core.database.dao.WarningTypeDao
import ir.kitgroup.partnerManagement.core.database.entity.WarningEntity
import ir.kitgroup.partnerManagement.core.database.entity.WarningTypeEntity
import ir.kitgroup.partnerManagement.core.database.model.WarningWithDetail
import ir.kitgroup.partnerManagement.core.network.apiService.ApiService
import ir.kitgroup.partnerManagement.core.network.model.ModelResponse
import ir.kitgroup.partnerManagement.core.ui.util.extractErrorMessage
import ir.kitgroup.partnerManagement.feature.meeting.model.CreateMeetingRequest
import ir.kitgroup.partnerManagement.feature.organization.model.CreateOrganizationWarningRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WarningRepository @Inject constructor(
    private val warningDao: WarningDao,
    private val warningTypeDao: WarningTypeDao,
    private val apiService: ApiService
) {
    fun observeByOrganization(organizationId: String): Flow<List<WarningWithDetail>> =
        warningDao.observeByOrganization(organizationId)

    suspend fun upsertAll(items: List<WarningEntity>) =
        warningDao.upsertAll(items)

    suspend fun deleteAll() = warningDao.deleteAll()

    suspend fun deleteById(warningId: String) = warningDao.deleteById(warningId)

    fun observeWarningTypes(): Flow<List<WarningTypeEntity>> =
        warningTypeDao.observeWarningTypes()


    suspend fun createOrganizationWarning(
        request: CreateOrganizationWarningRequest
    ): Result<ModelResponse> {

        return try {

            val response =
                apiService.createOrganizationWarning(request)

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
                                ?: "خطا در ثبت تذکر"
                        )
                    )
                }

            } else {
                val errorMessage = extractErrorMessage(
                    errorBody = response.errorBody()?.string(),
                    defaultMessage = "خطای سرور در ثبت تذکر: ${response.code()}"
                )

                Result.failure(
                    Exception(errorMessage)
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun updateOrganizationWarning(
        request: CreateOrganizationWarningRequest
    ): Result<ModelResponse> {

        return try {

            val response =
                apiService.updateOrganizationWarning(request)

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
                                ?: "خطا در ویرایش تذکر"
                        )
                    )
                }

            } else {
                val errorMessage = extractErrorMessage(
                    errorBody = response.errorBody()?.string(),
                    defaultMessage = "خطای سرور در ویرایش تذکر: ${response.code()}"
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
}
