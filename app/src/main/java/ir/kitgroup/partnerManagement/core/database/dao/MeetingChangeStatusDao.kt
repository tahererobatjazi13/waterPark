package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.kitgroup.partnerManagement.core.database.entity.MeetingChangeStatusEntity

@Dao
interface MeetingChangeStatusDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(item: MeetingChangeStatusEntity)

    @Query("SELECT * FROM meeting_change_status")
    suspend fun getAll(): List<MeetingChangeStatusEntity>

    @Query("DELETE FROM meeting_change_status WHERE meetingId = :meetingId")
    suspend fun deleteById(meetingId: String)

    @Query("DELETE FROM  meeting_change_status")
    suspend fun deleteAll()
}
