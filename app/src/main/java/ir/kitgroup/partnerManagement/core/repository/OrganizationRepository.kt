package ir.kitgroup.partnerManagement.core.repository

import ir.kitgroup.partnerManagement.core.database.dao.CityDao
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationDao
import ir.kitgroup.partnerManagement.core.database.dao.RegionDao
import ir.kitgroup.partnerManagement.core.database.entity.CityEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationEntity
import ir.kitgroup.partnerManagement.core.database.entity.RegionEntity
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrganizationRepository @Inject constructor(
    private val organizationDao: OrganizationDao,
    private val cityDao: CityDao,
    private val regionDao: RegionDao
) {
    fun observeAll(): Flow<List<OrganizationWithDetail>> {
        return organizationDao.observeAll()
    }

    fun observeById(
        organizationId: String
    ): Flow<OrganizationWithDetail?> {
        return organizationDao.observeById(organizationId)
    }

    suspend fun upsertAll(
        items: List<OrganizationEntity>
    ) {
        organizationDao.upsertAll(items)
    }

    suspend fun upsert(
        item: OrganizationEntity
    ) {
        organizationDao.upsert(item)
    }

    suspend fun deleteAll() {
        organizationDao.deleteAll()
    }

    fun observeCity(
        cityId: String
    ): Flow<CityEntity?> {
        return cityDao.observeById(cityId)
    }

    fun observeRegion(
        regionId: String
    ): Flow<RegionEntity?> {
        return regionDao.observeById(regionId)
    }

}