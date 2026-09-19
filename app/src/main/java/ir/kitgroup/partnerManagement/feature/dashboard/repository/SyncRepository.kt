package ir.kitgroup.partnerManagement.feature.dashboard.repository

import ir.kitgroup.partnerManagement.core.database.dao.BaseDataDao
import ir.kitgroup.partnerManagement.core.database.mapper.toEntity
import ir.kitgroup.partnerManagement.core.network.apiService.ApiService
import ir.kitgroup.partnerManagement.core.ui.util.datastore.MainPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncRepository @Inject constructor(
    private val apiService: ApiService,
    private val baseDataDao: BaseDataDao,
    private val preferences: MainPreferences
) {
    suspend fun syncBaseData(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // ۱. دریافت آخرین زمان سینک از DataStore
            val lastSync = preferences.getLastSyncTime() 

            // ۲. کال کردن سرور
            val response = apiService.getBaseData(lastSync)

            // ۳. ذخیره مستقیم در Room (تبدیل DTO به Entity)
            baseDataDao.insertCities(response.cities.map { it.toEntity() })
            baseDataDao.insertRegions(response.regions.map { it.toEntity() })
            baseDataDao.insertVisitTopics(response.visitTopics.map { it.toEntity() })
            baseDataDao.insertNoticeTypes(response.noticeTypes.map { it.toEntity() })
            baseDataDao.insertOrganizations(response.organizations.map { it.toEntity() })

            // ۴. بروزرسانی تایم استمپ آخرین همگام‌سازی
            preferences.saveLastSyncTime(System.currentTimeMillis())

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
