package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "visitor_organization")
data class VisitorOrganizationEntity(
    @PrimaryKey
    val visitorOrganizationId: String,

    val name: String? = null,

    val dateEnd: String? = null,

    val dateStart: String? = null,

    val organizationId: String? = null,

    val visitorId: String? = null,

    val statusRelation: Int? = null   // ثبت اولیه=0 ،فعال=1 ، غیر فعال=2
)