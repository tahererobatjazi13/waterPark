package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meeting")
data class MeetingEntity(

    @PrimaryKey
    val meetingId: String,

    val name: String? = null,

    val description: String? = null,

    val organizationId: String? = null,

    val status: Int? = null,//  برنامه ریزی شده=0، انجام شده=1، لغو شده=2

    val type: Int? = null,  //   تلفنی=0، حضوری برنامه ریزی شده=1، حضوری غیر برنامه ریزی شده=2، حضوری فوری= 3

    val visitDate: String? = null,

    val visitorId: String? = null,

    val visitRealDate: String? = null,

    val subjectVisitId: String? = null,

    val visitTime: String? = null,

    val personId: String? = null,

    val longitude: Double? = null,

    val latitude: Double? = null
)