package ir.kitgroup.partnerManagement.core.database.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.kitgroup.partnerManagement.core.database.AppDatabase
import ir.kitgroup.partnerManagement.core.database.dao.AssignedSerialDao
import ir.kitgroup.partnerManagement.core.database.dao.AssignedStandDao
import ir.kitgroup.partnerManagement.core.database.dao.CityDao
import ir.kitgroup.partnerManagement.core.database.dao.ContractDao
import ir.kitgroup.partnerManagement.core.database.dao.MeetingDao
import ir.kitgroup.partnerManagement.core.database.dao.OfferDao
import ir.kitgroup.partnerManagement.core.database.dao.OfferDetailDao
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationDao
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationPersonDao
import ir.kitgroup.partnerManagement.core.database.dao.ProductDao
import ir.kitgroup.partnerManagement.core.database.dao.RegionDao
import ir.kitgroup.partnerManagement.core.database.dao.StandTypeDao
import ir.kitgroup.partnerManagement.core.database.dao.StatisticDao
import ir.kitgroup.partnerManagement.core.database.dao.VisitSubjectDao
import ir.kitgroup.partnerManagement.core.database.dao.VisitorDao
import ir.kitgroup.partnerManagement.core.database.dao.WarningDao
import ir.kitgroup.partnerManagement.core.database.dao.WarningTypeDao
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
            "partner_management.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCityDao(
        db: AppDatabase
    ): CityDao = db.cityDao()

    @Provides
    fun provideRegionDao(
        db: AppDatabase
    ): RegionDao = db.regionDao()

    @Provides
    fun provideVisitSubjectDao(
        db: AppDatabase
    ): VisitSubjectDao = db.visitSubjectDao()

    @Provides
    fun provideStandTypeDao(
        db: AppDatabase
    ): StandTypeDao = db.standTypeDao()

    @Provides
    fun provideWarningTypeDao(
        db: AppDatabase
    ): WarningTypeDao = db.warningTypeDao()

    @Provides
    fun provideProductDao(
        db: AppDatabase
    ): ProductDao = db.productDao()

    @Provides
    fun provideOfferDao(
        db: AppDatabase
    ): OfferDao = db.offerDao()

    @Provides
    fun provideOfferDetailDao(
        db: AppDatabase
    ): OfferDetailDao = db.offerDetailDao()

   @Provides
    fun provideVisitorDao(
        db: AppDatabase
    ): VisitorDao = db.visitorDao()

    @Provides
    fun provideOrganizationDao(
        db: AppDatabase
    ): OrganizationDao = db.organizationDao()

    @Provides
    fun provideOrganizationPersonDao(
        db: AppDatabase
    ): OrganizationPersonDao = db.organizationPersonDao()

    @Provides
    fun provideMeetingDao(
        db: AppDatabase
    ): MeetingDao = db.meetingDao()

    @Provides
    fun provideWarningDao(
        db: AppDatabase
    ): WarningDao = db.warningDao()

    @Provides
    fun provideAssignedStandDao(
        db: AppDatabase
    ): AssignedStandDao = db.assignedStandDao()

    @Provides
    fun provideContractDao(
        db: AppDatabase
    ): ContractDao = db.contractDao()

    @Provides
    fun provideAssignedSerialDao(
        db: AppDatabase
    ): AssignedSerialDao = db.assignedSerialDao()

    @Provides
    fun provideStatisticDao(
        db: AppDatabase
    ): StatisticDao {
        return db.statisticDao()
    }
}
