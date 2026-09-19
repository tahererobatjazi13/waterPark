package ir.kitgroup.partnerManagement.core.network.apiService

import ir.kitgroup.partnerManagement.feature.dashboard.model.BaseDataSyncResponse
import retrofit2.http.GET
import retrofit2.http.Query



interface ApiService {
    @GET("sync/base-data")
    suspend fun getBaseData(
        @Query("lastSyncTime") lastSyncTimestamp: Long? = null
    ): BaseDataSyncResponse

}