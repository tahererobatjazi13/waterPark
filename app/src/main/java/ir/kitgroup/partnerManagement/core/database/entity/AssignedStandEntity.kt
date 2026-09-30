package ir.kitgroup.partnerManagement.core.database.entity


import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "assigned_stands",
    indices = [
        Index("organizationId"),
        Index("standTypeId"),
        Index("visitorId"),
        Index("meetingId")
    ]
)
data class AssignedStandEntity(

    @PrimaryKey
    val assignedStandId: String,

    val organizationId: String? = null,

    val organizationName: String? = null,

    val standTypeId: String? = null,

    val standTypeName: String? = null,

    val visitorId: String? = null,

    val visitorName: String? = null,

    val description: String? = null,

    val assignmentDate: String? = null,

    val plannedReturnDate: String? = null,

    val actualReturnDate: String? = null,

    val deliveryToOrgDate: String? = null,

    val deliveryToVisitorDate: String? = null,

    val count: Int? = null,

    // نوع اختصاص (AssignmentType): تخصیص به بازاریاب=1، تخصیص به سازمان=2، عودت=3، جمع آوری=4، خاتمه=5
    val assignmentType: Int? = null,

    val assignmentTypeName: String? = null,
    // ماهیت اختصاص (AssignmentMode): تبلیغاتی=0، امانی=1، اجاره‌ای=2

    val assignmentMode: Int? = null,

    val assignmentModeName: String? = null,

    val meetingId: String? = null,

    // وضعیت تخصیص (StandAssignmentStatus): پیش‌نویس=0، فعال تحویل داده شده=1، عودت شده=2، لغو شده=3
    val statusAssign: Int? = null,

    val statusAssignName: String? = null,

    val stateCode: Int? = null,

    /*   @Ignore
       val items: List<StandTypeEntity> = emptyList()*/
) {
    /* // Constructor مورد نیاز Room به دلیل استفاده از فیلد @Ignore
     constructor(
         standAssignmentId: String,
         name: String?,
         actualReturnDate: String?,
         advertisingStandId: String?,
         assignmentDate: String?,
         assignmentMode: Int?,
         assignmentType: Int?,
         deliveryToOrgDate: String?,
         deliveryToVisitorDate: String?,
         organizationId: String?,
         plannedReturnDate: String?,
         status: Int?,
         visitorId: String?,
         count: Int?,
         description: String?,
         meetingId: String?
     ) : this(
         standAssignmentId = standAssignmentId,
         name = name,
         actualReturnDate = actualReturnDate,
         advertisingStandId = advertisingStandId,
         assignmentDate = assignmentDate,
         assignmentMode = assignmentMode,
         assignmentType = assignmentType,
         deliveryToOrgDate = deliveryToOrgDate,
         deliveryToVisitorDate = deliveryToVisitorDate,
         organizationId = organizationId,
         plannedReturnDate = plannedReturnDate,
         status = status,
         visitorId = visitorId,
         count = count,
         description = description,
         meetingId = meetingId
     )*/
}
