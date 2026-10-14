package ir.kitgroup.partnerManagement.core.repository

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.kitgroup.partnerManagement.core.database.dao.CityDao
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationDao
import ir.kitgroup.partnerManagement.core.database.dao.RegionDao
import ir.kitgroup.partnerManagement.core.database.entity.CityEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationEntity
import ir.kitgroup.partnerManagement.core.database.entity.RegionEntity
import ir.kitgroup.partnerManagement.core.database.entity.VisitorOrganizationEntity
import ir.kitgroup.partnerManagement.core.database.mapper.toEntity
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import ir.kitgroup.partnerManagement.core.network.apiService.ApiService
import ir.kitgroup.partnerManagement.core.network.model.ModelResponse
import ir.kitgroup.partnerManagement.core.network.model.TicketIssueLineDto
import ir.kitgroup.partnerManagement.feature.organization.model.ChangeOrganizationStatusRequest
import ir.kitgroup.partnerManagement.feature.organization.ui.add.model.AddOrganizationPersonRequest
import ir.kitgroup.partnerManagement.feature.organization.ui.add.model.CreateOrganizationRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrganizationRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val organizationDao: OrganizationDao,
    private val cityDao: CityDao,
    private val regionDao: RegionDao,
    private val apiService: ApiService

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

    fun observeAllCity(): Flow<List<CityEntity>> {
        return cityDao.observeAll()
    }

    fun observeRegionByCityId(
        cityId: String
    ): Flow<List<RegionEntity>> {
        return regionDao.observeByCity(cityId)
    }


    suspend fun createOrganization(request: CreateOrganizationRequest): Result<ModelResponse> {
        return try {

            val response = apiService.createOrganization(request)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.success) {
                    Result.success(body)
                } else {
                    Result.failure(Exception(body.message ?: "خطا در ثبت سازمان"))
                }
            } else {
                val errorMsg = response.errorBody()?.string() ?: "خطای سرور: ${response.code()}"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateOrganization(request: CreateOrganizationRequest): Result<ModelResponse> {
        return try {

            val response = apiService.updateOrganization(request)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                if (body.success) {
                    Result.success(body)
                } else {
                    Result.failure(Exception(body.message ?: "خطا در ثبت سازمان"))
                }
            } else {
                val errorMsg = response.errorBody()?.string() ?: "خطای سرور: ${response.code()}"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun changeOrganizationStatus(request: ChangeOrganizationStatusRequest): Result<ModelResponse> {
        return try {
            val response = apiService.changeOrganizationStatus(request)
            if (response.isSuccessful && response.body() != null) {
                Log.d("ChangeOrganizationStatusRequest5", "ok")

                val body = response.body()!!
                if (body.success) {
                    Result.success(body)
                } else {
                    Result.failure(Exception(body.message ?: "خطا در ثبت سازمان"))
                }
            } else {

                val errorMsg = response.errorBody()?.string() ?: "خطای سرور: ${response.code()}"
                Log.d("ChangeOrganizationStatusRequest6", errorMsg.toString())

                Result.failure(Exception(errorMsg))

            }
        } catch (e: Exception) {
            Log.d("ChangeOrganizationStatusRequest7", e.toString())

            Result.failure(e)

        }
    }

    suspend fun getOrganizationVisitors(
        organizationId: String
    ): Result<List<VisitorOrganizationEntity>> {
        return try {
            val response = apiService.getOrganizationVisitors(organizationId)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.map { it.toEntity() })
            } else {
                val errorMsg = response.errorBody()?.string()
                    ?: "خطا در دریافت لیست ویزیتورها. کد: ${response.code()}"
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


    suspend fun addOrganizationPerson(
        request: AddOrganizationPersonRequest
    ): Result<ModelResponse> {
        return try {

            val response = apiService.addOrganizationPerson(request)

            if (response.isSuccessful) {

                val body = response.body()

                if (body != null) {

                    Log.d(
                        "AddOrganizationPerson",
                        "response = $body"
                    )

                    if (body.success) {
                        Result.success(body)
                    } else {
                        Result.failure(
                            Exception(
                                body.message ?: "خطا در ثبت شخص"
                            )
                        )
                    }

                } else {
                    Result.failure(
                        Exception("پاسخ خالی از سرور دریافت شد")
                    )
                }

            } else {

                val errorMessage =
                    response.errorBody()?.string()
                        ?.takeIf { it.isNotBlank() }
                        ?: "خطای سرور: ${response.code()}"

                Log.e(
                    "AddOrganizationPerson",
                    "HTTP ${response.code()} - $errorMessage"
                )

                Result.failure(
                    Exception(errorMessage)
                )
            }

        } catch (e: Exception) {

            Log.e(
                "AddOrganizationPerson",
                "Exception",
                e
            )

            Result.failure(
                Exception(
                    e.localizedMessage
                        ?: "خطا در برقراری ارتباط با سرور",
                    e
                )
            )
        }
    }
}