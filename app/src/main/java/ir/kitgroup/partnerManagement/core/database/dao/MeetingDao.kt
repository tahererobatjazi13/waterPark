package ir.kitgroup.partnerManagement.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import ir.kitgroup.partnerManagement.core.database.entity.MeetingEntity
import ir.kitgroup.partnerManagement.core.database.model.MeetingWithDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface MeetingDao {
    @Upsert
    suspend fun upsertAll(items: List<MeetingEntity>)

    @Query("SELECT * FROM meetings ORDER BY visitDate DESC, visitTime DESC")
    fun observeAllMeetings(): Flow<List<MeetingWithDetail>>

    @Query("SELECT * FROM meetings WHERE status = :status ORDER BY visitDate DESC, visitTime DESC")
    fun observeMeetingsByStatus(status: Int): Flow<List<MeetingEntity>>


    @Query(
        """
        SELECT * FROM meetings
        WHERE organizationId = :organizationId
        ORDER BY visitDate DESC
    """
    )
    fun observeByOrganization(
        organizationId: String
    ): Flow<List<MeetingWithDetail>>

    @Query(
        """
    SELECT * FROM meetings
    WHERE substr(visitDate, 1, 10) = :date
    ORDER BY visitTime ASC
    """
    )
    fun observeMeetingsByDate(
        date: String
    ): Flow<List<MeetingWithDetail>>

    @Query("SELECT * FROM meetings WHERE meetingId = :id LIMIT 1")
    fun observeMeetingById(id: String): Flow<MeetingWithDetail?>

    @Query("DELETE FROM meetings WHERE meetingId = :meetingId")
    suspend fun deleteById(meetingId: String)

    @Query("DELETE FROM meetings")
    suspend fun deleteAll()
}