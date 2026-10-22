package ir.kitgroup.partnerManagement.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.kitgroup.partnerManagement.core.database.dao.AssignStandChangeStatusDao
import ir.kitgroup.partnerManagement.core.database.dao.AssignedSerialDao
import ir.kitgroup.partnerManagement.core.database.dao.AssignedStandDao
import ir.kitgroup.partnerManagement.core.database.dao.AttachmentFileDao
import ir.kitgroup.partnerManagement.core.database.dao.CityDao
import ir.kitgroup.partnerManagement.core.database.dao.ContractDao
import ir.kitgroup.partnerManagement.core.database.dao.ContractTerminationDao
import ir.kitgroup.partnerManagement.core.database.dao.MeetingChangeStatusDao
import ir.kitgroup.partnerManagement.core.database.dao.MeetingDao
import ir.kitgroup.partnerManagement.core.database.dao.OfferDao
import ir.kitgroup.partnerManagement.core.database.dao.OfferDetailDao
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationDao
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationPersonDao
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationChangeStatusDao
import ir.kitgroup.partnerManagement.core.database.dao.ProductDao
import ir.kitgroup.partnerManagement.core.database.dao.RegionDao
import ir.kitgroup.partnerManagement.core.database.dao.StandTypeDao
import ir.kitgroup.partnerManagement.core.database.dao.StatisticDao
import ir.kitgroup.partnerManagement.core.database.dao.VisitSubjectDao
import ir.kitgroup.partnerManagement.core.database.dao.VisitorDao
import ir.kitgroup.partnerManagement.core.database.dao.WarningDao
import ir.kitgroup.partnerManagement.core.database.dao.WarningTypeDao
import ir.kitgroup.partnerManagement.core.database.entity.AssignStandChangeStatusEntity
import ir.kitgroup.partnerManagement.core.database.entity.AssignedStandEntity
import ir.kitgroup.partnerManagement.core.database.entity.CityEntity
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.database.entity.MeetingEntity
import ir.kitgroup.partnerManagement.core.database.entity.OfferDetailEntity
import ir.kitgroup.partnerManagement.core.database.entity.OfferEntity
import ir.kitgroup.partnerManagement.core.database.entity.WarningTypeEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationPersonEntity
import ir.kitgroup.partnerManagement.core.database.entity.ProductEntity
import ir.kitgroup.partnerManagement.core.database.entity.RegionEntity
import ir.kitgroup.partnerManagement.core.database.entity.AssignedSerialEntity
import ir.kitgroup.partnerManagement.core.database.entity.AttachmentFileEntity
import ir.kitgroup.partnerManagement.core.database.entity.ContractTerminationEntity
import ir.kitgroup.partnerManagement.core.database.entity.MeetingChangeStatusEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationChangeStatusEntity
import ir.kitgroup.partnerManagement.core.database.entity.StandTypeEntity
import ir.kitgroup.partnerManagement.core.database.entity.StatisticEntity
import ir.kitgroup.partnerManagement.core.database.entity.VisitSubjectEntity
import ir.kitgroup.partnerManagement.core.database.entity.VisitorEntity
import ir.kitgroup.partnerManagement.core.database.entity.WarningEntity


@Database(
    entities = [
        CityEntity::class,
        RegionEntity::class,
        VisitSubjectEntity::class,
        StandTypeEntity::class,
        WarningTypeEntity::class,
        ProductEntity::class,
        OfferEntity::class,
        OfferDetailEntity::class,
        VisitorEntity::class,
        OrganizationEntity::class,
        OrganizationChangeStatusEntity::class,
        OrganizationPersonEntity::class,
        AttachmentFileEntity::class,
        MeetingEntity::class,
        MeetingChangeStatusEntity::class,
        WarningEntity::class,
        AssignedStandEntity::class,
        AssignStandChangeStatusEntity::class,
        ContractEntity::class,
        ContractTerminationEntity::class,
        AssignedSerialEntity::class,
        StatisticEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
    abstract fun regionDao(): RegionDao
    abstract fun visitSubjectDao(): VisitSubjectDao
    abstract fun standTypeDao(): StandTypeDao
    abstract fun warningTypeDao(): WarningTypeDao
    abstract fun productDao(): ProductDao
    abstract fun offerDao(): OfferDao
    abstract fun offerDetailDao(): OfferDetailDao
    abstract fun visitorDao(): VisitorDao
    abstract fun organizationDao(): OrganizationDao
    abstract fun organizationPersonDao(): OrganizationPersonDao
    abstract fun meetingDao(): MeetingDao
    abstract fun meetingChangeStatusDao(): MeetingChangeStatusDao
    abstract fun warningDao(): WarningDao
    abstract fun assignedStandDao(): AssignedStandDao
    abstract fun assignStandChangeStatusDao(): AssignStandChangeStatusDao
    abstract fun contractDao(): ContractDao
    abstract fun assignedSerialDao(): AssignedSerialDao
    abstract fun statisticDao(): StatisticDao
    abstract fun contractTerminationDao(): ContractTerminationDao
    abstract fun organizationChangeStatusDao(): OrganizationChangeStatusDao
    abstract fun attachmentFileDao(): AttachmentFileDao
}
