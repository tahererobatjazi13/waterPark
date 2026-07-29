package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ir.kitgroup.partnerManagement.core.database.entity.PartnerManagementEntity

@Dao
interface PartnerManagementDao {

    @Insert
    suspend fun insertPartnerManagement(partnerManagements: PartnerManagementEntity)

    @Query("SELECT * FROM partnerManagements")
    suspend fun getPartnerManagements(): List<PartnerManagementEntity>
}

