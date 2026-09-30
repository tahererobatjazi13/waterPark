package ir.kitgroup.partnerManagement.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import ir.kitgroup.partnerManagement.core.database.entity.MeetingEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationEntity
import ir.kitgroup.partnerManagement.core.database.entity.VisitSubjectEntity
import ir.kitgroup.partnerManagement.core.database.entity.VisitorEntity

data class MeetingWithDetail(

    @Embedded
    val meeting: MeetingEntity,

    @Relation(
        parentColumn = "subjectVisitId",
        entityColumn = "subjectVisitId"
    )
    val visitSubject: VisitSubjectEntity?,
    @Relation(
        parentColumn = "visitorId",
        entityColumn = "visitorId"
    )
    val visitor: VisitorEntity?,

    @Relation(
        parentColumn = "organizationId",
        entityColumn = "organizationId"
    )
    val organization: OrganizationEntity?
) {

    val subjectVisitName: String
        get() = visitSubject?.title ?: ""

    val visitorName: String
        get() = visitor?.name ?: ""

    val organizationName: String
        get() = organization?.name ?: ""

    val organizationAddress: String
        get() = organization?.address ?: ""

    val organizationGrade: Int
        get() = organization?.grade ?: 0

    val cityName: String
        get() = organization?.cityName.orEmpty()

    val regionName: String
        get() = organization?.regionName.orEmpty()
}