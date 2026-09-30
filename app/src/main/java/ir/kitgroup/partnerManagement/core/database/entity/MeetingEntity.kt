package ir.kitgroup.partnerManagement.core.database.entity


import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "meetings",
    indices = [
        Index("organizationId"),
        Index("visitorId"),
        Index("subjectVisitId")
    ]
)
data class MeetingEntity(

    @PrimaryKey
    val meetingId: String,

    val organizationId: String? = null,

    val organizationName: String? = null,

    val visitorId: String? = null,

    val visitorName: String? = null,

    val subjectVisitId: String? = null,

    val subjectVisitName: String? = null,

    val organizationPersonId: String? = null,

    val organizationPersonName: String? = null,

    val description: String? = null,

    val visitDate: String? = null,

    val visitRealDate: String? = null,

    val visitTime: String? = null,

    val status: Int? = null,//  برنامه ریزی شده=0، انجام شده=1، لغو شده=2

    val statusName: String? = null,

    val type: Int? = null,  //   تلفنی=0، حضوری برنامه ریزی شده=1، حضوری غیر برنامه ریزی شده=2، حضوری فوری= 3

    val typeName: String? = null,

    val longitude: String? = null,

    val latitude: String? = null,

    val stateCode: Int? = null

)