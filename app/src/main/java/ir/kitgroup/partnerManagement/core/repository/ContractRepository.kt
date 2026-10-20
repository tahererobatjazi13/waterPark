package ir.kitgroup.partnerManagement.core.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.dao.ContractDao
import ir.kitgroup.partnerManagement.core.database.dao.ContractTerminationDao
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.database.entity.ContractTerminationEntity
import ir.kitgroup.partnerManagement.core.network.apiService.ApiService
import ir.kitgroup.partnerManagement.core.network.exception.ApiRequestExecutor
import ir.kitgroup.partnerManagement.core.ui.util.ContractStatus
import ir.kitgroup.partnerManagement.core.ui.util.SyncStatus
import ir.kitgroup.partnerManagement.feature.contract.model.ContractRequest
import ir.kitgroup.partnerManagement.feature.contract.model.TerminateActiveContractRequest
import ir.kitgroup.partnerManagement.feature.dashboard.repository.SyncRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

class ContractRepository @Inject constructor(
    private val contractDao: ContractDao,
    private val contractTerminationDao: ContractTerminationDao,
    private val apiService: ApiService,
    private val syncRepository: SyncRepository,
    @ApplicationContext private val context: Context
) {
    suspend fun saveLocal(contract: ContractEntity) {
        contractDao.upsert(contract)
    }

    fun observeByOrganization(
        organizationId: String
    ): Flow<List<ContractEntity>> {
        return contractDao.observeByOrganization(organizationId)
    }

    fun observeActiveByOrganization(
        organizationId: String
    ): Flow<List<ContractEntity>> {
        return contractDao.observeActiveByOrganization(organizationId)
    }

    fun observeAll() = contractDao.observeAll()

    suspend fun upsertAll(
        contracts: List<ContractEntity>
    ) {
        contractDao.upsertAll(contracts)
    }

    fun observeById(
        contractId: String
    ): Flow<ContractEntity?> {

        return contractDao.observeById(contractId)
    }

    suspend fun deleteAll() {
        contractDao.deleteAll()
    }

    /**
     * ایجاد قرارداد آنلاین:
     * ریکوئست با contractId = null فرستاده می‌شود.
     * پس از تایید سرور، فرآیند syncData اجرا می‌شود تا تمام داده‌های سرور بارگذاری گردند.
     */
    suspend fun createContractAndSync(request: ContractRequest): Result<String?> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_contract_create)
        ) {
            apiService.createContract(request.copy(contractId = null))
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

    /**
     * ویرایش قرارداد آنلاین:
     * با شناسه رسمی سرور ارسال شده و در صورت موفقیت وضعیت سینک بروزرسانی می‌شود.
     */
    suspend fun updateContractAndSync(request: ContractRequest): Result<String?> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_contract_update)
        ) {
            apiService.updateContract(request)
        }.fold(
            onSuccess = { response ->
                if (!request.contractId.isNullOrBlank()) {
                    contractDao.updateSyncStatus(
                        contractId = request.contractId,
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

    suspend fun terminateActiveContract(
        request: TerminateActiveContractRequest
    ): Result<String?> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_contract_terminate)
        ) {
            apiService.terminateActiveContract(request)
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

    suspend fun saveOfflineTermination(
        contractId: String,
        organizationId: String,
        terminationDate: String
    ) = withContext(Dispatchers.IO) {
        val currentTimestamp = System.currentTimeMillis()
        val isoTimestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date())

        // درج در جدول صف خاتمه‌های آفلاین جهت ارسال در سینک بعدی
        contractTerminationDao.insertOrUpdate(
            ContractTerminationEntity(
                contractId = contractId,
                organizationId = organizationId,
                terminationDate = terminationDate
            )
        )

        // به‌روزرسانی لوکال قرارداد در جدول contracts
        val existingContract = contractDao.getById(contractId)
        if (existingContract != null) {
            val nextSyncStatus = if (existingContract.syncStatus == SyncStatus.SYNCED) {
                SyncStatus.PENDING_UPDATE
            } else {
                existingContract.syncStatus
            }

            val updatedEntity = existingContract.copy(
                status = ContractStatus.INACTIVE.id,
                endDate = terminationDate,
                modifiedOn = isoTimestamp,
                syncStatus = nextSyncStatus,
                isLocallyModified = true,
                lastModifiedTimestamp = currentTimestamp
            )
            contractDao.insertOrUpdate(updatedEntity)
        }
    }
}



