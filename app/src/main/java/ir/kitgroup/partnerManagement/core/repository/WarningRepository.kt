package ir.kitgroup.partnerManagement.core.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.dao.WarningDao
import ir.kitgroup.partnerManagement.core.database.dao.WarningTypeDao
import ir.kitgroup.partnerManagement.core.database.entity.WarningEntity
import ir.kitgroup.partnerManagement.core.database.entity.WarningTypeEntity
import ir.kitgroup.partnerManagement.core.database.model.WarningWithDetail
import ir.kitgroup.partnerManagement.core.network.apiService.ApiService
import ir.kitgroup.partnerManagement.core.network.exception.ApiRequestExecutor
import ir.kitgroup.partnerManagement.core.ui.util.SyncStatus
import ir.kitgroup.partnerManagement.feature.dashboard.repository.SyncRepository
import ir.kitgroup.partnerManagement.feature.organization.model.CreateOrganizationWarningRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WarningRepository @Inject constructor(
    private val warningDao: WarningDao,
    private val warningTypeDao: WarningTypeDao,
    private val apiService: ApiService,
    private val syncRepository: SyncRepository,
    @ApplicationContext private val context: Context
) {
    fun observeByOrganization(organizationId: String): Flow<List<WarningWithDetail>> =
        warningDao.observeByOrganization(organizationId)

    fun observeWarningById(warningId: String): Flow<WarningWithDetail?> =
        warningDao.observeById(warningId)

    suspend fun saveLocal(contract: WarningEntity) {
        warningDao.upsert(contract)
    }

    suspend fun upsertAll(items: List<WarningEntity>) =
        warningDao.upsertAll(items)

    suspend fun deleteAll() = warningDao.deleteAll()

    suspend fun deleteById(warningId: String) = warningDao.deleteById(warningId)

    fun observeWarningTypes(): Flow<List<WarningTypeEntity>> =
        warningTypeDao.observeWarningTypes()

    suspend fun createOrganizationWarningAndSync(request: CreateOrganizationWarningRequest): Result<String?> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_warning_create)
        ) {
            apiService.createOrganizationWarning(request.copy(organizationWarningId = null))
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

    suspend fun updateOrganizationWarningAndSync(request: CreateOrganizationWarningRequest): Result<String?> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_warning_update)
        ) {
            apiService.updateOrganizationWarning(request)
        }.fold(
            onSuccess = { response ->
                if (!request.organizationWarningId.isNullOrBlank()) {
                    warningDao.updateSyncStatus(
                        warningId = request.warningId,
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

}
