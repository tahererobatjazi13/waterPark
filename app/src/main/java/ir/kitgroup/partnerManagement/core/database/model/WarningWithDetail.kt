package ir.kitgroup.partnerManagement.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import ir.kitgroup.partnerManagement.core.database.entity.VisitorEntity
import ir.kitgroup.partnerManagement.core.database.entity.WarningEntity
import ir.kitgroup.partnerManagement.core.database.entity.WarningTypeEntity

data class WarningWithDetail(
    @Embedded
    val warning: WarningEntity,

    @Relation(
        parentColumn = "warningTypeId",
        entityColumn = "warningTypeId"
    )
    val warningType: WarningTypeEntity?,
    @Relation(
        parentColumn = "visitorId",
        entityColumn = "visitorId"
    )
    val visitor: VisitorEntity?
) {
    val warningTypeName: String
        get() = warningType?.title ?: ""

    val visitorName: String
        get() = visitor?.name ?: ""

}
