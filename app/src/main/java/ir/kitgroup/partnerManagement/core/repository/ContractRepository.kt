package ir.kitgroup.partnerManagement.core.repository

import ir.kitgroup.partnerManagement.core.database.dao.ContractDao
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContractRepository @Inject constructor(
    private val contractDao: ContractDao
) {

    fun observeByOrganization(
        organizationId: String
    ): Flow<List<ContractEntity>> {
        return contractDao.observeByOrganization(organizationId)
    }

    fun observeAllContracts(): Flow<List<ContractEntity>> =
        contractDao.observeAll()

    suspend fun upsertAll(
        contracts: List<ContractEntity>
    ) {
        contractDao.upsertAll(contracts)
    }

    suspend fun deleteAll() {
        contractDao.deleteAll()
    }
}