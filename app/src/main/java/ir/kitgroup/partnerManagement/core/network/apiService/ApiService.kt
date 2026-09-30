package ir.kitgroup.partnerManagement.core.network.apiService


import ir.kitgroup.partnerManagement.feature.dashboard.model.ConfirmLastSyncResponse
import ir.kitgroup.partnerManagement.feature.dashboard.model.SyncDataRequest
import ir.kitgroup.partnerManagement.feature.dashboard.model.SyncDataResponse
import ir.kitgroup.partnerManagement.feature.login.model.LoginRequest
import ir.kitgroup.partnerManagement.feature.login.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("api/Auth/Login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("api/Sync/syncData")
    suspend fun syncData(
        @Body request: SyncDataRequest
    ): SyncDataResponse

    @POST("api/Sync/confirmLastSync")
    suspend fun confirmLastSync(
        @Query("userId") userId: String
    ): ConfirmLastSyncResponse

}