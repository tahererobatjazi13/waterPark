package ir.kitgroup.partnerManagement.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.kitgroup.partnerManagement.core.database.AppDatabase
import ir.kitgroup.partnerManagement.core.database.dao.PartnerManagementDao
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
            "partnerManagement_db"
        ).build()
    }


    @Provides
    fun providePartnerManagementDao(
        db: AppDatabase
    ): PartnerManagementDao {
        return db.partnerManagementDao()
    }
}
