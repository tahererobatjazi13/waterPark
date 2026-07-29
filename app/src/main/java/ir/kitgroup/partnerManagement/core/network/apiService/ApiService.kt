package ir.kitgroup.partnerManagement.core.network.apiService

import ir.kitgroup.partnerManagement.core.database.entity.PartnerManagementEntity
import retrofit2.http.GET

// ApiService.kt
interface ApiService {
    @GET("partnerManagements")
    suspend fun getPartnerManagements(): List<PartnerManagementEntity>
}