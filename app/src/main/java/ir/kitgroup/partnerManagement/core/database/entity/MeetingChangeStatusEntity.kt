package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meeting_change_status")
data class MeetingChangeStatusEntity(
    @PrimaryKey
    val meetingId: String,
    val status: Int,
    val visitRealDate: String,
    val latitude: Double,
    val longitude: Double,
    val description: String,
    val createdAt: Long = System.currentTimeMillis()
)

