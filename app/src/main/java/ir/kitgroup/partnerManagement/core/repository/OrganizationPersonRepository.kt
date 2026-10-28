package ir.kitgroup.partnerManagement.core.repository

import ir.kitgroup.partnerManagement.core.database.dao.OrganizationPersonDao
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationPersonEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrganizationPersonRepository @Inject constructor(
    private val organizationPersonDao: OrganizationPersonDao
) {
    suspend fun saveLocal(person: OrganizationPersonEntity) {
        organizationPersonDao.upsert(person)
    }
    fun observeByOrganization(
        organizationId: String
    ): Flow<List<OrganizationPersonEntity>> {
        return organizationPersonDao.observeByOrganization(organizationId)
    }
}