package ir.kitgroup.partnerManagement.core.repository

import ir.kitgroup.partnerManagement.core.database.dao.StandTypeDao
import ir.kitgroup.partnerManagement.core.database.entity.StandTypeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StandTypesRepository @Inject constructor(
    private val standTypeDao: StandTypeDao
) {
    fun observeAll(): Flow<List<StandTypeEntity>> =
        standTypeDao.observeAll()
}
