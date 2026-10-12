package ir.kitgroup.partnerManagement.core.repository

import android.util.Log
import ir.kitgroup.partnerManagement.core.database.dao.MeetingDao
import ir.kitgroup.partnerManagement.core.database.model.MeetingWithDetail
import ir.kitgroup.partnerManagement.core.network.apiService.ApiService
import ir.kitgroup.partnerManagement.core.network.model.ModelResponse
import ir.kitgroup.partnerManagement.core.ui.util.extractErrorMessage
import ir.kitgroup.partnerManagement.feature.contract.model.CreateContractRequest
import ir.kitgroup.partnerManagement.feature.contract.model.TerminateActiveContractRequest
import ir.kitgroup.partnerManagement.feature.meeting.model.ChangeStatusMeetingRequest
import ir.kitgroup.partnerManagement.feature.meeting.model.CreateMeetingRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MeetingRepository @Inject constructor(
    private val meetingDao: MeetingDao,
    private val apiService: ApiService,

    ) {

    fun observeMeetingsByOrganization(
        organizationId: String
    ): Flow<List<MeetingWithDetail>> {
        return meetingDao.observeByOrganization(organizationId)
    }

    fun observeAllMeetings(
    ): Flow<List<MeetingWithDetail>> {
        return meetingDao.observeAllMeetings()
    }

    fun observeMeetingsByDate(date: String): Flow<List<MeetingWithDetail>> {
        return meetingDao.observeMeetingsByDate(date)
    }

    suspend fun deleteMeeting(meetingId: String) {
        meetingDao.deleteById(meetingId)
    }

    fun observeMeetingById(
        meetingId: String
    ): Flow<MeetingWithDetail?> {
        return meetingDao.observeMeetingById(meetingId)
    }


    suspend fun createMeeting(
        request: CreateMeetingRequest
    ): Result<ModelResponse> {

        return try {

            val response =
                apiService.createMeeting(request)

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
                                ?: "خطا در ثبت  بازدید"
                        )
                    )
                }

            } else {
                val errorMessage = extractErrorMessage(
                    errorBody = response.errorBody()?.string(),
                    defaultMessage = "خطای سرور در ثبت بازدید: ${response.code()}"
                )

                Result.failure(
                    Exception(errorMessage)
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    suspend fun updateMeeting(
        request: CreateMeetingRequest
    ): Result<ModelResponse> {

        return try {

            val response =
                apiService.updateMeeting(request)

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
                                ?: "خطا در ویرایش قرارداد"
                        )
                    )
                }

            } else {
                val errorMessage = extractErrorMessage(
                    errorBody = response.errorBody()?.string(),
                    defaultMessage = "خطای سرور در ویرایش قرارداد: ${response.code()}"
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

    suspend fun changeStatusMeeting(
        request: ChangeStatusMeetingRequest
    ): Result<ModelResponse> {
        return try {
            val response = apiService.changeMeetingStatus(request)

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.success) {
                    Result.success(body)
                } else {
                    Result.failure(
                        Exception(body.message ?: "خطا در تغییر وضعیت بازدید")
                    )
                }
            } else {
                val errorMessage = extractErrorMessage(
                    errorBody = response.errorBody()?.string(),
                    defaultMessage = "خطای سرور در تغییر وضعیت بازدید: ${response.code()}"
                )
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}