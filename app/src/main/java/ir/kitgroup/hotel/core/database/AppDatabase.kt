package ir.kitgroup.hotel.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.kitgroup.hotel.core.database.dao.HotelDao
import ir.kitgroup.hotel.core.database.entity.HotelEntity

@Database(
    entities = [HotelEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun hotelDao(): HotelDao
}
