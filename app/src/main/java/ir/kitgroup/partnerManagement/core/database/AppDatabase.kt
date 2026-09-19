package ir.kitgroup.partnerManagement.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.kitgroup.partnerManagement.core.database.dao.BaseDataDao
import ir.kitgroup.partnerManagement.core.database.entity.CityEntity
import ir.kitgroup.partnerManagement.core.database.entity.WarningsEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationEntity
import ir.kitgroup.partnerManagement.core.database.entity.RegionEntity
import ir.kitgroup.partnerManagement.core.database.entity.SubjectVisitEntity


@Database(
    entities = [
        CityEntity::class,
        RegionEntity::class,
        SubjectVisitEntity::class,
        WarningsEntity::class,
        OrganizationEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun baseDataDao(): BaseDataDao
}
