package ir.kitgroup.partnerManagement.core.database.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.kitgroup.partnerManagement.core.database.AppDatabase
import ir.kitgroup.partnerManagement.core.database.dao.BaseDataDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "partnerManagement_db")
            .build()

    @Provides
    fun provideBaseDataDaoDao(db: AppDatabase): BaseDataDao =
        db.baseDataDao()

}
