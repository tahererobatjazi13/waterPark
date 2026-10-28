package ir.kitgroup.partnerManagement.core.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.dao.AssignedStandDao
import ir.kitgroup.partnerManagement.core.database.entity.AssignStandChangeStatusEntity
import ir.kitgroup.partnerManagement.core.database.entity.AssignedStandEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationChangeStatusEntity
import ir.kitgroup.partnerManagement.core.database.model.AssignedStandWithDetail
import ir.kitgroup.partnerManagement.core.network.apiService.ApiService
import ir.kitgroup.partnerManagement.core.network.exception.ApiException
import ir.kitgroup.partnerManagement.core.network.exception.ApiRequestExecutor
import ir.kitgroup.partnerManagement.core.network.model.ModelResponse
import ir.kitgroup.partnerManagement.core.ui.util.SyncStatus
import ir.kitgroup.partnerManagement.core.ui.util.extractErrorMessage
import ir.kitgroup.partnerManagement.feature.contract.model.ContractRequest
import ir.kitgroup.partnerManagement.feature.dashboard.repository.SyncRepository
import ir.kitgroup.partnerManagement.feature.organization.model.ChangeStatusOrganizationRequest
import ir.kitgroup.partnerManagement.feature.stand.model.ChangeAssignStandStatusRequest
import ir.kitgroup.partnerManagement.feature.stand.model.CreateAssignStandRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssignedStandsRepository @Inject constructor(
    private val assignedStandDao: AssignedStandDao,
    private val apiService: ApiService,
    private val syncRepository: SyncRepository,
    @ApplicationContext private val context: Context
) {

    suspend fun saveLocal(assignedStand: AssignedStandEntity) {
        assignedStandDao.upsert(assignedStand)
    }

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

    suspend fun createAssignStandAndSync(request: CreateAssignStandRequest): Result<String?> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_assign_stand_create)
        ) {
            apiService.createAssignStand(request.copy(standAssignmentId = null))
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

    suspend fun updateAssignStandAndSync(request: CreateAssignStandRequest): Result<String?> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_assign_stand_update)
        ) {
            apiService.updateAssignStand(request)
        }.fold(
            onSuccess = { response ->
                if (!request.standAssignmentId.isNullOrBlank()) {
                    assignedStandDao.updateSyncStatus(
                        assignedStandId = request.standAssignmentId,
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

    suspend fun changeStatusAssignStand(
        request: ChangeAssignStandStatusRequest
    ): Result<String?> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_change_status)
        ) {
            apiService.changeAssignStandStatus(request)
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


    suspend fun saveOfflineAssignStandStatusChange(
        standAssignmentId: String,
        organizationId: String,
        status: Int,
        actualReturnDate: String,
        deliveryToOrgDate: String
    ) {

        assignedStandDao.insert(
            AssignStandChangeStatusEntity(
                standAssignmentId = standAssignmentId,
                organizationId = organizationId,
                status = status,
                actualReturnDate = actualReturnDate,
                deliveryToOrgDate = deliveryToOrgDate,

                )
        )
    }
}
