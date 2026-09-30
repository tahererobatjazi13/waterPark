package ir.kitgroup.partnerManagement.core.repository

import ir.kitgroup.partnerManagement.core.database.dao.WarningDao
import ir.kitgroup.partnerManagement.core.database.entity.WarningEntity
import ir.kitgroup.partnerManagement.core.database.model.WarningWithDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WarningRepository @Inject constructor(
    private val warningDao: WarningDao
) {
    fun observeByOrganization(organizationId: String): Flow<List<WarningWithDetail>> =
        warningDao.observeByOrganization(organizationId)

    suspend fun upsertAll(items: List<WarningEntity>) =
        warningDao.upsertAll(items)

    suspend fun deleteAll() = warningDao.deleteAll()

    suspend fun deleteById(warningId: String) = warningDao.deleteById(warningId)
}
