package ir.kitgroup.partnerManagement.core.repository

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.dao.MeetingDao
import ir.kitgroup.partnerManagement.core.database.entity.AssignStandChangeStatusEntity
import ir.kitgroup.partnerManagement.core.database.entity.MeetingChangeStatusEntity
import ir.kitgroup.partnerManagement.core.database.entity.MeetingEntity
import ir.kitgroup.partnerManagement.core.database.model.MeetingWithDetail
import ir.kitgroup.partnerManagement.core.network.apiService.ApiService
import ir.kitgroup.partnerManagement.core.network.exception.ApiException
import ir.kitgroup.partnerManagement.core.network.exception.ApiRequestExecutor
import ir.kitgroup.partnerManagement.core.network.model.ModelResponse
import ir.kitgroup.partnerManagement.core.ui.util.SyncStatus
import ir.kitgroup.partnerManagement.core.ui.util.extractErrorMessage
import ir.kitgroup.partnerManagement.feature.dashboard.repository.SyncRepository
import ir.kitgroup.partnerManagement.feature.meeting.model.ChangeStatusMeetingRequest
import ir.kitgroup.partnerManagement.feature.meeting.model.CreateMeetingRequest
import ir.kitgroup.partnerManagement.feature.stand.model.ChangeAssignStandStatusRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MeetingRepository @Inject constructor(
    private val meetingDao: MeetingDao,
    private val apiService: ApiService,
    private val syncRepository: SyncRepository,
    @ApplicationContext private val context: Context

) {
    suspend fun saveLocal(meeting: MeetingEntity) {
        meetingDao.upsert(meeting)
    }

    fun observeMeetingsByOrganization(
        organizationId: String
    ): Flow<List<MeetingWithDetail>> {
        return meetingDao.observeByOrganization(organizationId)
    }

    fun observeAll(
    ): Flow<List<MeetingWithDetail>> {
        return meetingDao.observeAll()
    }

    fun observeMeetingsByDate(date: String): Flow<List<MeetingWithDetail>> {
        return meetingDao.observeMeetingsByDate(date)
    }

    fun observeMeetingById(
        meetingId: String
    ): Flow<MeetingWithDetail?> {
        return meetingDao.observeMeetingById(meetingId)
    }

    suspend fun createMeetingAndSync(request: CreateMeetingRequest): Result<ModelResponse> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_meeting_create)
        ) {
            apiService.createMeeting(request.copy(meetingId = null))
        }.fold(
            onSuccess = { response ->
                if (response.success) {
                    syncRepository.syncData()
                    Result.success(response)

                } else {
                    Result.failure(ApiException(response.message))
                }
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }

    suspend fun updateMeetingAndSync(request: CreateMeetingRequest): Result<String?> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_meeting_update)
        ) {
            apiService.updateMeeting(request)
        }.fold(
            onSuccess = { response ->
                if (!request.meetingId.isNullOrBlank()) {
                    meetingDao.updateSyncStatus(
                        meetingId = request.meetingId,
                        status = SyncStatus.SYNCED,
                        isLocallyModified = false
                    )
                }
                syncRepository.syncData()
                Result.success(response.message)
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }


    suspend fun changeStatusMeeting(
        request: ChangeStatusMeetingRequest
    ): Result<String?> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_change_status)
        ) {
            apiService.changeMeetingStatus(request)
        }.fold(
            onSuccess = { response ->
                if (response.success) {
                    syncRepository.syncData()
                    Result.success(response.message)
                } else {
                    Result.failure(ApiException(response.message))
                }
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }

    suspend fun saveMeetingStatusChange(
        meetingId: String,
        status: Int,
        visitRealDate: String,
        latitude: Double,
        longitude: Double,
        description: String
    ) {

        meetingDao.insert(
            MeetingChangeStatusEntity(
                meetingId = meetingId,
                status = status,
                visitRealDate = visitRealDate,
                latitude = latitude,
                longitude = longitude,
                description = description)
        )
    }
}