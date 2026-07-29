package ir.kitgroup.partnerManagement.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.kitgroup.partnerManagement.core.database.dao.PartnerManagementDao
import ir.kitgroup.partnerManagement.core.database.entity.PartnerManagementEntity

@Database(
    entities = [PartnerManagementEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun partnerManagementDao(): PartnerManagementDao
}
