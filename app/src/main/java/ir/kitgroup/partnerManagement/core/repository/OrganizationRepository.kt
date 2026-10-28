package ir.kitgroup.partnerManagement.core.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.dao.CityDao
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationChangeStatusDao
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationDao
import ir.kitgroup.partnerManagement.core.database.dao.RegionDao
import ir.kitgroup.partnerManagement.core.database.entity.CityEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationChangeStatusEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationEntity
import ir.kitgroup.partnerManagement.core.database.entity.RegionEntity
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import ir.kitgroup.partnerManagement.core.network.apiService.ApiService
import ir.kitgroup.partnerManagement.core.network.exception.ApiException
import ir.kitgroup.partnerManagement.core.network.exception.ApiRequestExecutor
import ir.kitgroup.partnerManagement.core.network.model.ModelResponse
import ir.kitgroup.partnerManagement.core.network.model.TicketIssueLineDto
import ir.kitgroup.partnerManagement.core.network.model.VisitorOrganizationDto
import ir.kitgroup.partnerManagement.core.ui.util.SyncStatus
import ir.kitgroup.partnerManagement.feature.dashboard.repository.SyncRepository
import ir.kitgroup.partnerManagement.feature.organization.model.ChangeStatusOrganizationRequest
import ir.kitgroup.partnerManagement.feature.organization.ui.add.model.AddOrganizationPersonRequest
import ir.kitgroup.partnerManagement.feature.organization.ui.add.model.CreateOrganizationRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrganizationRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val organizationDao: OrganizationDao,
    private val cityDao: CityDao,
    private val regionDao: RegionDao,
    private val organizationChangeStatusDao: OrganizationChangeStatusDao,
    private val apiService: ApiService,
    private val syncRepository: SyncRepository
) {

    fun observeAll() = organizationDao.observeAll()

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

    fun observeAllCity(): Flow<List<CityEntity>> {
        return cityDao.observeAll()
    }

    fun observeRegionByCityId(
        cityId: String
    ): Flow<List<RegionEntity>> {
        return regionDao.observeByCity(cityId)
    }

    suspend fun saveLocal(organization: OrganizationEntity) {
        organizationDao.upsert(organization)
    }

    suspend fun createOrganizationAndSync(request: CreateOrganizationRequest): Result<ModelResponse> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_organization_create)
        ) {
            apiService.createOrganization(request.copy(organizationId = null))
        }.fold(
            onSuccess = { response ->
                if (response.success) {
                    Result.success(response)
                } else {
                    Result.failure(ApiException(response.message))
                }
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }

    suspend fun updateOrganizationAndSync(request: CreateOrganizationRequest): Result<String?> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_organization_update)
        ) {
            apiService.updateOrganization(request)
        }.fold(
            onSuccess = { response ->
                if (response.success) {
                    if (!request.organizationId.isNullOrBlank()) {
                        organizationDao.updateSyncStatus(
                            organizationId = request.organizationId,
                            status = SyncStatus.SYNCED,
                            isLocallyModified = false
                        )
                    }
                    syncRepository.syncData()
                    Result.success(response.message)
                } else {
                    Result.failure(ApiException(response.message))
                }
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }

    suspend fun changeStatusOrganization(
        request: ChangeStatusOrganizationRequest
    ): Result<String?> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_organization_change_status)
        ) {
            apiService.changeStatusOrganization(request)
        }.fold(
            onSuccess = { response ->
                if (response.success) {
                    syncRepository.syncData()
                    Result.success(response.message)
                } else {
                    Result.failure(ApiException(response.message))
                }
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }


    suspend fun saveOfflineOrganizationStatusChange(
        organizationId: String,
        statusOrg: Int,
        deactiveDate: String?,
        reason: String?,
        visitDate: String?,
        subjectVisitId: String?
    ) {
        organizationChangeStatusDao.insert(
            OrganizationChangeStatusEntity(
                organizationId = organizationId,
                statusOrg = statusOrg,
                deactiveDate = deactiveDate!!,
                reason = reason!!,
                visitDate = visitDate!!,
                subjectVisitId = subjectVisitId!!
            )
        )

    }


    suspend fun getOrganizationVisitors(
        organizationId: String
    ): Result<List<VisitorOrganizationDto>> {
        return try {
            val response = apiService.getOrganizationVisitors(organizationId)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string()
                    ?: "خطا در دریافت اطلاعات. کد: ${response.code()}"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getOrganizationTicketIssue(
        organizationId: String
    ): Result<List<TicketIssueLineDto>> {
        return try {
            val response = apiService.getOrganizationTicketIssue(organizationId)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string()
                    ?: "خطا در دریافت اطلاعات. کد: ${response.code()}"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addOrganizationPerson(request: AddOrganizationPersonRequest): Result<String?> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_person_create)
        ) {
            apiService.addOrganizationPerson(request)
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

