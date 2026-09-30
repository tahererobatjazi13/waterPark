package ir.kitgroup.partnerManagement.core.database.entity


import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "warnings",
    indices = [
        Index("organizationId"),
        Index("meetingId"),
        Index("visitorId"),
        Index("warningTypeId")
    ]
)

data class WarningEntity(

    @PrimaryKey
    val warningId: String,

    val organizationId: String? = null,

    val organizationName: String? = null,

    val meetingId: String? = null,

    val visitorId: String? = null,

    val visitorName: String? = null,

    val warningTypeId: String? = null,

    val warningTypeName: String? = null,

    val score: Int? = null,

    val description: String? = null,

    val warningDate: String? = null,

    val stateCode: Int? = null

)