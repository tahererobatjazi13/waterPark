package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "visitor_organizations")
data class VisitorOrganizationEntity(

    @PrimaryKey
    val visitorOrganizationId: String,

    val visitorId: String?,

    val organizationId: String?,

    val statusRelation: Int?,

    val statusRelationName: String?,

    val dateStart: String?,

    val dateEnd: String?,

    val name: String?,

    val status: Int?// ثبت اولیه=0 ،فعال=1 ، غیر فعال=2
)