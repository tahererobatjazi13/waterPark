package ir.kitgroup.partnerManagement.core.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.dao.AssignedSerialDao
import ir.kitgroup.partnerManagement.core.database.entity.AssignedSerialEntity
import ir.kitgroup.partnerManagement.core.database.model.AssignedSerialWithDetail
import ir.kitgroup.partnerManagement.core.network.apiService.ApiService
import ir.kitgroup.partnerManagement.core.network.exception.ApiRequestExecutor
import ir.kitgroup.partnerManagement.feature.dashboard.repository.SyncRepository
import ir.kitgroup.partnerManagement.feature.offer.model.SerialOfferRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssignedSerialsRepository @Inject constructor(
    private val assignedSerialDao: AssignedSerialDao,
    private val apiService: ApiService,
    private val syncRepository: SyncRepository,
    @ApplicationContext private val context: Context
) {

    fun observeByOrganization(organizationId: String): Flow<List<AssignedSerialWithDetail>> =
        assignedSerialDao.observeByOrganization(organizationId)

    suspend fun upsertAll(items: List<AssignedSerialEntity>) =
        assignedSerialDao.upsertAll(items)

    fun observeById(id: String) = assignedSerialDao.observeById(id)

    suspend fun deleteAll() = assignedSerialDao.deleteAll()


    suspend fun createSerialOfferAndSync(
        request: SerialOfferRequest
    ): Result<String?> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_assign_serial_offer)
        ) {
            apiService.assignSerialOffer(request)
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

    suspend fun updateSerialOfferAndSync(
        request: SerialOfferRequest
    ): Result<String?> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_update_serial_offer)
        ) {
            apiService.updateSerialOffer(request)
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

    suspend fun changeStatusSerialOfferC(
        id: String,
        status: String
    ): Result<String?> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_change_status_serial_offer)
        ) {
            apiService.serialOfferChangeStatus(id, status)
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

}
