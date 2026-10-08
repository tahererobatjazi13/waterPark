package ir.kitgroup.partnerManagement.core.repository

import ir.kitgroup.partnerManagement.core.database.dao.VisitorDao
import ir.kitgroup.partnerManagement.core.database.entity.VisitorEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VisitorRepository @Inject constructor(
    private val visitorDao: VisitorDao,
) {

    fun getAllVisitors(): Flow<List<VisitorEntity>> =
        visitorDao.observeAll()

}
