package ir.kitgroup.partnerManagement.core.repository

import ir.kitgroup.partnerManagement.core.database.dao.AssignedStandDao
import ir.kitgroup.partnerManagement.core.database.entity.AssignedStandEntity
import ir.kitgroup.partnerManagement.core.database.model.AssignedStandWithDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssignedStandsRepository @Inject constructor(
    private val assignedStandDao: AssignedStandDao
) {

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

}
