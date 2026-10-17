package ir.kitgroup.partnerManagement.core.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.dao.ContractDao
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.network.apiService.ApiService
import ir.kitgroup.partnerManagement.core.network.exception.ApiRequestExecutor
import ir.kitgroup.partnerManagement.feature.contract.model.ContractRequest
import ir.kitgroup.partnerManagement.feature.contract.model.TerminateActiveContractRequest
import ir.kitgroup.partnerManagement.feature.dashboard.repository.SyncRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContractRepository @Inject constructor(
    private val contractDao: ContractDao,
    private val apiService: ApiService,
    private val syncRepository: SyncRepository,
    @ApplicationContext private val context: Context
) {

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


    suspend fun createContractAndSync(
        request: ContractRequest
    ): Result<String?> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_contract_create)
        ) {
            apiService.createContract(request)
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

    suspend fun updateContractAndSync(
        request: ContractRequest
    ): Result<String?> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_contract_update)
        ) {
            apiService.updateContract(request)
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

}



