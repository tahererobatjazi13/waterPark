package ir.kitgroup.partnerManagement.core.repository

import ir.kitgroup.partnerManagement.core.database.dao.AssignedSerialDao
import ir.kitgroup.partnerManagement.core.database.entity.AssignedSerialEntity
import ir.kitgroup.partnerManagement.core.database.model.AssignedSerialWithDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssignedSerialsRepository @Inject constructor(
    private val assignedSerialDao: AssignedSerialDao
) {

    fun observeByOrganization(organizationId: String): Flow<List<AssignedSerialWithDetail>> =
        assignedSerialDao.observeByOrganization(organizationId)

    suspend fun upsertAll(items: List<AssignedSerialEntity>) =
        assignedSerialDao.upsertAll(items)

    suspend fun deleteAll() = assignedSerialDao.deleteAll()

}
