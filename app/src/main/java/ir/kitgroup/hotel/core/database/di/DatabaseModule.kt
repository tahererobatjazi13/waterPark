package ir.kitgroup.hotel.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.kitgroup.hotel.core.database.AppDatabase
import ir.kitgroup.hotel.core.database.dao.HotelDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {

        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "hotel_db"
        ).build()
    }

    @Provides
    fun provideHotelDao(
        db: AppDatabase
    ): HotelDao {
        return db.hotelDao()
    }
}
