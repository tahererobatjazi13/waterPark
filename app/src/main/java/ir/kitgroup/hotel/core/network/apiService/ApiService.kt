package ir.kitgroup.hotel.core.network.apiService

import ir.kitgroup.hotel.core.database.entity.HotelEntity
import retrofit2.http.GET

// ApiService.kt
interface ApiService {
    @GET("hotels")
    suspend fun getHotels(): List<HotelEntity>
}