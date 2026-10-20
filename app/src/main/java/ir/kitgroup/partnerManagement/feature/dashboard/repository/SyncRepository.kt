package ir.kitgroup.partnerManagement.feature.dashboard.repository

import android.util.Log
import androidx.room.withTransaction
import ir.kitgroup.partnerManagement.core.database.AppDatabase
import ir.kitgroup.partnerManagement.core.database.dao.AssignStandChangeStatusDao
import ir.kitgroup.partnerManagement.core.database.dao.AssignedSerialDao
import ir.kitgroup.partnerManagement.core.database.dao.AssignedStandDao
import ir.kitgroup.partnerManagement.core.database.dao.CityDao
import ir.kitgroup.partnerManagement.core.database.dao.ContractDao
import ir.kitgroup.partnerManagement.core.database.dao.ContractTerminationDao
import ir.kitgroup.partnerManagement.core.database.dao.MeetingChangeStatusDao
import ir.kitgroup.partnerManagement.core.database.dao.MeetingDao
import ir.kitgroup.partnerManagement.core.database.dao.OfferDao
import ir.kitgroup.partnerManagement.core.database.dao.OfferDetailDao
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationDao
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationPersonDao
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationChangeStatusDao
import ir.kitgroup.partnerManagement.core.database.dao.ProductDao
import ir.kitgroup.partnerManagement.core.database.dao.RegionDao
import ir.kitgroup.partnerManagement.core.database.dao.StandTypeDao
import ir.kitgroup.partnerManagement.core.database.dao.StatisticDao
import ir.kitgroup.partnerManagement.core.database.dao.VisitSubjectDao
import ir.kitgroup.partnerManagement.core.database.dao.VisitorDao
import ir.kitgroup.partnerManagement.core.database.dao.WarningDao
import ir.kitgroup.partnerManagement.core.database.dao.WarningTypeDao
import ir.kitgroup.partnerManagement.core.database.mapper.toEntity
import ir.kitgroup.partnerManagement.core.database.mapper.toPendingDto
import ir.kitgroup.partnerManagement.core.network.apiService.ApiService
import ir.kitgroup.partnerManagement.core.network.exception.ApiException
import ir.kitgroup.partnerManagement.core.ui.util.SyncStatus
import ir.kitgroup.partnerManagement.core.ui.util.datastore.MainPreferences
import ir.kitgroup.partnerManagement.feature.dashboard.model.SyncDataResponse
import ir.kitgroup.partnerManagement.feature.dashboard.model.SyncRequestDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
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
    private val organizationChangeStatusDao: OrganizationChangeStatusDao,
    private val organizationPersonDao: OrganizationPersonDao,
    private val meetingDao: MeetingDao,
    private val meetingChangeStatusDao: MeetingChangeStatusDao,
    private val warningDao: WarningDao,
    private val assignedStandDao: AssignedStandDao,
    private val assignStandChangeStatusDao: AssignStandChangeStatusDao,
    private val contractDao: ContractDao,
    private val contractTerminationDao: ContractTerminationDao,
    private val assignedSerialDao: AssignedSerialDao,
    private val statisticDao: StatisticDao,
    private val preferences: MainPreferences,
    private val database: AppDatabase,
    private val mainPreferences: MainPreferences,
) {
    suspend fun syncData(): Result<Unit> = withContext(Dispatchers.IO) {

        try {
            // خواندن مقادیر احراز هویت از DataStore
            val systemUserId = preferences.getSystemUserId()
            val visitorId = preferences.getVisitorId()
            val roleCode = preferences.getRoleCode()

            // اعتبارسنجی مقدماتی لاگین بودن کاربر
            if (systemUserId.isBlank() && visitorId.isBlank()) {
                return@withContext Result.failure(
                    IllegalStateException(
                        "اطلاعات احراز هویت کاربر یافت نشد. لطفاً مجدداً وارد شوید."
                    )
                )
            }

            val isFirstSync = mainPreferences.isFirstSync.first()

            //  واکشی قراردادهای جدید/ویرایشی
            val pendingContractsInserts =
                contractDao.getContractsBySyncStatus(SyncStatus.PENDING_INSERT)
            val pendingContractsUpdates =
                contractDao.getContractsBySyncStatus(SyncStatus.PENDING_UPDATE)
            val pendingContracts = pendingContractsInserts + pendingContractsUpdates
            val pendingContractsDto = pendingContracts.map { entity -> entity.toPendingDto() }


            // واکشی خاتمه‌های قرارداد معلق (آفلاین)
            val pendingTerminations = contractTerminationDao.getAll()
            val pendingTerminationsDto = pendingTerminations.map { it.toPendingDto() }

            //  واکشی تذکرهای جدید/ویرایشی
            val pendingWarningsInserts =
                warningDao.getWarningsBySyncStatus(SyncStatus.PENDING_INSERT)
            val pendingWarningsUpdates =
                warningDao.getWarningsBySyncStatus(SyncStatus.PENDING_UPDATE)
            val pendingWarnings = pendingWarningsInserts + pendingWarningsUpdates
            val pendingWarningsDto = pendingWarnings.map { entity -> entity.toPendingDto() }


            //  واکشی سازمان ها جدید/ویرایشی
            val pendingOrganizationsInserts =
                organizationDao.getOrganizationsBySyncStatus(SyncStatus.PENDING_INSERT)
            val pendingOrganizationsUpdates =
                organizationDao.getOrganizationsBySyncStatus(SyncStatus.PENDING_UPDATE)
            val pendingOrganizations =
                pendingOrganizationsInserts + pendingOrganizationsUpdates
            val pendingOrganizationsDto =
                pendingOrganizations.map { entity -> entity.toPendingDto() }


            //  واکشی اشخاص سازمان ها جدید/ویرایشی
            val pendingOrganizationPersonsInserts =
                organizationPersonDao.getOrganizationPersonsBySyncStatus(SyncStatus.PENDING_INSERT)
            val pendingOrganizationPersonsUpdates =
                organizationPersonDao.getOrganizationPersonsBySyncStatus(SyncStatus.PENDING_UPDATE)
            val pendingOrganizationPersons =
                pendingOrganizationPersonsInserts + pendingOrganizationPersonsUpdates
            val pendingOrganizationPersonsDto =
                pendingOrganizationPersons.map { entity -> entity.toPendingDto() }


            // واکشی تغییر وضعیت سازمان ها (آفلاین)
            val pendingOrganizationStatusChange = organizationChangeStatusDao.getAll()
            val organizationStatusChangeDto =
                pendingOrganizationStatusChange.map { it.toPendingDto() }


            //  واکشی سازمان ها جدید/ویرایشی
            val pendingMeetingsInserts =
                meetingDao.getMeetingsBySyncStatus(SyncStatus.PENDING_INSERT)
            val pendingMeetingsUpdates =
                meetingDao.getMeetingsBySyncStatus(SyncStatus.PENDING_UPDATE)
            val pendingMeetings =
                pendingMeetingsInserts + pendingMeetingsUpdates
            val pendingMeetingsDto =
                pendingMeetings.map { entity -> entity.toPendingDto() }

            // واکشی تغییر وضعیت بازدیدها (آفلاین)
            val pendingMeetingChangeStatus = meetingChangeStatusDao.getAll()
            val meetingChangeStatusDto =
                pendingMeetingChangeStatus.map { it.toPendingDto() }


            //  واکشی تخصیص استند ها جدید/ویرایشی
            val pendingAssignedStandsInserts =
                assignedStandDao.getAssignedStandBySyncStatus(SyncStatus.PENDING_INSERT)
            val pendingAssignedStandsUpdates =
                assignedStandDao.getAssignedStandBySyncStatus(SyncStatus.PENDING_UPDATE)
            val pendingAssignedStands =
                pendingAssignedStandsInserts + pendingAssignedStandsUpdates
            val pendingAssignedStandsDto =
                pendingAssignedStands.map { entity -> entity.toPendingDto() }

            // واکشی تغییر وضعیت تخصیص استندها (آفلاین)
            val pendingAssignedStandChangeStatus = assignStandChangeStatusDao.getAll()
            val assignedStandChangeStatusDto =
                pendingAssignedStandChangeStatus.map { it.toPendingDto() }

            val request = SyncRequestDto(
                statusSync = isFirstSync,
                systemUserId = systemUserId,
                visitorId = visitorId,
                roleCode = roleCode,
                pendingOrganizations = pendingOrganizationsDto,
                pendingPersons = pendingOrganizationPersonsDto,
                pendingMeetings = pendingMeetingsDto,
                pendingWarnings = pendingWarningsDto,
                pendingOrgStatusChanges = organizationStatusChangeDto,
                pendingContracts = pendingContractsDto,
                pendingContractTerminations = pendingTerminationsDto,
                pendingMeetingStatusChanges = meetingChangeStatusDto,
                pendingUpdateStands = pendingAssignedStandsDto,
                pendingStandStatusChanges = assignedStandChangeStatusDto
            )

            Log.e("requestttttt22", request.toString())

            // ارسال به وب‌سرویس و دریافت داده‌ها
            val response = apiService.syncData(request)

            println("success = ${response.success}")
            println("contracts from server = ${response.contracts.size}")

            if (!response.success) {
                return@withContext Result.failure(
                    ApiException(
                        response.message ?: "خطا در همگام‌سازی از سمت سرور"
                    )
                )
            }

            // حذف رکوردهای موقت محلی و ذخیره داده‌های رسمی سرور در یک ترنزکشن امن
            database.withTransaction {
                pendingOrganizations.forEach { offlineItem ->
                    organizationDao.deleteById(offlineItem.organizationId)
                }
                pendingOrganizationPersons.forEach { offlineItem ->
                    organizationPersonDao.deleteById(offlineItem.orgPersonId)
                }
                pendingAssignedStands.forEach { offlineItem ->
                    assignedStandDao.deleteById(offlineItem.assignedStandId)
                }
                pendingContracts.forEach { offlineItem ->
                    contractDao.deleteById(offlineItem.contractId)
                }
                pendingWarnings.forEach { offlineItem ->
                    warningDao.deleteById(offlineItem.warningId)
                }
                pendingMeetings.forEach { offlineItem ->
                    meetingDao.deleteById(offlineItem.meetingId)
                }
                // سپس تمام داده‌های جدید سرور ذخیره می‌شوند
                saveResponse(response)
            }

            val confirmResponse = apiService.confirmLastSync(
                userId = visitorId
            )

            if (response.success && isFirstSync) {
                mainPreferences.markFirstSyncCompleted()
            }

            if (!confirmResponse.success) {
                throw ApiException(
                    confirmResponse.message ?: "تأیید همگام‌سازی در سرور انجام نشد."
                )
            }

            // به‌روزرسانی زمان همگام‌سازی
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
        cityDao.upsertAll(response.cities.map { it.toEntity() })
        regionDao.upsertAll(response.regions.map { it.toEntity() })
        visitSubjectDao.upsertAll(response.visitSubjects.map { it.toEntity() })
        standTypeDao.upsertAll(response.standTypes.map { it.toEntity() })
        warningTypeDao.upsertAll(response.warningTypes.map { it.toEntity() })
        productDao.upsertAll(response.products.map { it.toEntity() })
        offerDao.upsertAll(response.offers.map { it.toEntity() })
        offerDetailDao.upsertAll(response.offerDetails.map { it.toEntity() })
        visitorDao.upsertAll(response.visitors.map { it.toEntity() })
        organizationDao.upsertAll(response.organizations.map { it.toEntity() })
        organizationPersonDao.upsertAll(response.organizationPersons.map { it.toEntity() })
        meetingDao.upsertAll(response.meetings.map { it.toEntity() })
        warningDao.upsertAll(response.warnings.map { it.toEntity() })
        assignedStandDao.upsertAll(response.assignedStands.map { it.toEntity() })
        contractDao.upsertAll(response.contracts.map { it.toEntity() })
        assignedSerialDao.upsertAll(response.assignedSerials.map { it.toEntity() })

        response.statistics?.let { statistics ->
            statisticDao.upsert(statistics.toEntity())
        }
    }
}
