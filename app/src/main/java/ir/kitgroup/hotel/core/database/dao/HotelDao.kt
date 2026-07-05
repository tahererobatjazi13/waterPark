package ir.kitgroup.hotel.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ir.kitgroup.hotel.core.database.entity.HotelEntity

@Dao
interface HotelDao {

    @Insert
    suspend fun insertHotel(hotel: HotelEntity)

    @Query("SELECT * FROM hotels")
    suspend fun getHotels(): List<HotelEntity>
}
