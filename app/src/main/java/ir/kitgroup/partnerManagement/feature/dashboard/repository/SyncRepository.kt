package ir.kitgroup.partnerManagement.feature.dashboard.repository

import androidx.room.withTransaction
import ir.kitgroup.partnerManagement.core.database.AppDatabase
import ir.kitgroup.partnerManagement.core.database.dao.AssignedSerialDao
import ir.kitgroup.partnerManagement.core.database.dao.AssignedStandDao
import ir.kitgroup.partnerManagement.core.database.dao.CityDao
import ir.kitgroup.partnerManagement.core.database.dao.ContractDao
import ir.kitgroup.partnerManagement.core.database.dao.MeetingDao
import ir.kitgroup.partnerManagement.core.database.dao.OfferDao
import ir.kitgroup.partnerManagement.core.database.dao.OfferDetailDao
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationDao
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationPersonDao
import ir.kitgroup.partnerManagement.core.database.dao.ProductDao
import ir.kitgroup.partnerManagement.core.database.dao.RegionDao
import ir.kitgroup.partnerManagement.core.database.dao.StandTypeDao
import ir.kitgroup.partnerManagement.core.database.dao.StatisticDao
import ir.kitgroup.partnerManagement.core.database.dao.VisitSubjectDao
import ir.kitgroup.partnerManagement.core.database.dao.VisitorDao
import ir.kitgroup.partnerManagement.core.database.dao.WarningDao
import ir.kitgroup.partnerManagement.core.database.dao.WarningTypeDao
import ir.kitgroup.partnerManagement.core.database.mapper.toEntity
import ir.kitgroup.partnerManagement.core.network.apiService.ApiService
import ir.kitgroup.partnerManagement.core.network.exception.ApiException
import ir.kitgroup.partnerManagement.core.ui.util.datastore.MainPreferences
import ir.kitgroup.partnerManagement.feature.dashboard.model.SyncDataRequest
import ir.kitgroup.partnerManagement.feature.dashboard.model.SyncDataResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncRepository @Inject constructor(
    private val apiService: ApiService,
    private val cityDao: CityDao,
    private val regionDao: RegionDao,
    private val visitSubjectDao: VisitSubjectDao,
    private val standTypeDao: StandTypeDao,
    private val warningTypeDao: WarningTypeDao,
    private val productDao: ProductDao,
    private val offerDao: OfferDao,
    private val offerDetailDao: OfferDetailDao,
    private val visitorDao: VisitorDao,
    private val organizationDao: OrganizationDao,
    private val organizationPersonDao: OrganizationPersonDao,
    private val meetingDao: MeetingDao,
    private val warningDao: WarningDao,
    private val assignedStandDao: AssignedStandDao,
    private val contractDao: ContractDao,
    private val assignedSerialDao: AssignedSerialDao,
    private val statisticDao: StatisticDao,
    private val preferences: MainPreferences,
    private val database: AppDatabase
) {
    suspend fun syncData(): Result<Unit> = withContext(Dispatchers.IO) {

        try {
            // خواندن مقادیر احراز هویت از DataStore
            val systemUserId = preferences.getSystemUserId()
            val visitorId = preferences.getVisitorId()
            val roleCode = preferences.getRoleCode()

            // اعتبار سنجی مقدماتی لاگین بودن کاربر
            if (
                systemUserId.isBlank() &&
                visitorId.isBlank()
            ) {
                return@withContext Result.failure(
                    IllegalStateException(
                        "اطلاعات احراز هویت کاربر یافت نشد. لطفاً مجدداً وارد شوید."
                    )
                )
            }

            val request = SyncDataRequest(
                systemUserId = systemUserId,
                visitorId = visitorId,
                roleCode = roleCode,
                pendingOrganizations = emptyList(),
                pendingPersons = emptyList(),
                pendingMeetings = emptyList(),
                pendingWarnings = emptyList(),
                pendingAssignedStands = emptyList()
            )
            //  دریافت داده از وب‌سرویس
            val response = apiService.syncData(request)


            println("success = ${response.success}")
            println("cities = ${response.cities.size}")
            println("regions = ${response.regions.size}")
            println("visitSubjects = ${response.visitSubjects.size}")
            println("standTypes = ${response.standTypes.size}")
            println("warningTypes = ${response.warningTypes.size}")
            println("products = ${response.products.size}")
            println("offers = ${response.offers.size}")
            println("offerDetails = ${response.offerDetails.size}")
            println("organizations = ${response.organizations.size}")
            println("organizationPersons = ${response.organizationPersons.size}")
            println("meetings = ${response.meetings.size}")
            println("warnings = ${response.warnings.size}")
            println("assignedStands = ${response.assignedStands.size}")
            println("contracts = ${response.contracts.size}")
            println("assignedSerials = ${response.assignedSerials.size}")

            if (!response.success) {

                return@withContext Result.failure(
                    ApiException(
                        response.message
                            ?: "خطا در همگام‌سازی از سمت سرور"
                    )
                )
            }

            database.withTransaction {
                saveResponse(response)
            }

            val confirmResponse = apiService.confirmLastSync(
                userId = visitorId
            )

            if (!confirmResponse.success) {
                throw ApiException(
                    confirmResponse.message
                        ?: "تأیید همگام‌سازی در سرور انجام نشد."
                )
            }
            // به‌روزرسانی زمان همگام‌سازی در صورت نیاز
            preferences.saveLastSyncTime(System.currentTimeMillis())

            Result.success(Unit)

        } catch (e: Exception) {
            println("message = ${e.message}")
            Result.failure(e)
        }
    }

    private suspend fun saveResponse(
        response: SyncDataResponse
    ) {

        cityDao.upsertAll(
            response.cities.map { it.toEntity() }
        )

        regionDao.upsertAll(
            response.regions.map { it.toEntity() }
        )

        visitSubjectDao.upsertAll(
            response.visitSubjects.map { it.toEntity() }
        )

        standTypeDao.upsertAll(
            response.standTypes.map { it.toEntity() }
        )

        warningTypeDao.upsertAll(
            response.warningTypes.map { it.toEntity() }
        )

        productDao.upsertAll(
            response.products.map { it.toEntity() }
        )

        offerDao.upsertAll(
            response.offers.map { it.toEntity() }
        )

        offerDetailDao.upsertAll(
            response.offerDetails.map { it.toEntity() }
        )

        visitorDao.upsertAll(
            response.visitors.map { it.toEntity() }
        )

        organizationDao.upsertAll(
            response.organizations.map { it.toEntity() }
        )

        organizationPersonDao.upsertAll(
            response.organizationPersons.map { it.toEntity() }
        )

        meetingDao.upsertAll(
            response.meetings.map { it.toEntity() }
        )

        warningDao.upsertAll(
            response.warnings.map { it.toEntity() }
        )

        assignedStandDao.upsertAll(
            response.assignedStands.map { it.toEntity() }
        )

        contractDao.upsertAll(
            response.contracts.map { it.toEntity() }
        )

        assignedSerialDao.upsertAll(
            response.assignedSerials.map { it.toEntity() }
        )

        response.statistics?.let { statistics ->

            statisticDao.upsert(
                statistics.toEntity()
            )
        }
    }
}
