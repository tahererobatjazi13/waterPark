package ir.kitgroup.partnerManagement.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import ir.kitgroup.partnerManagement.core.database.entity.AssignedStandEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationEntity
import ir.kitgroup.partnerManagement.core.database.entity.StandTypeEntity
import ir.kitgroup.partnerManagement.core.database.entity.VisitorEntity

data class AssignedStandWithDetail(
    @Embedded
    val assignedStand: AssignedStandEntity,

    @Relation(
        parentColumn = "standTypeId",
        entityColumn = "standTypeId"
    )
    val standType: StandTypeEntity?,
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
    val standTitle: String
        get() = standType?.title
            ?: standType?.standTypeName
            ?: assignedStand.standTypeName
            ?: ""

    val visitorName: String
        get() = visitor?.name ?: ""

    val organizationName: String
        get() = organization?.name
            ?: ""
}
