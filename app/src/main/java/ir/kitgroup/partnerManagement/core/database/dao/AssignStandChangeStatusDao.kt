package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.kitgroup.partnerManagement.core.database.entity.AssignStandChangeStatusEntity

@Dao
interface AssignStandChangeStatusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(item: AssignStandChangeStatusEntity)

    @Query("SELECT * FROM assign_stand_change_status")
    suspend fun getAll(): List<AssignStandChangeStatusEntity>

    @Query("DELETE FROM assign_stand_change_status WHERE standAssignmentId = :standAssignmentId")
    suspend fun deleteById(standAssignmentId: String)

    @Query("DELETE FROM  assign_stand_change_status")
    suspend fun deleteAll()
}
