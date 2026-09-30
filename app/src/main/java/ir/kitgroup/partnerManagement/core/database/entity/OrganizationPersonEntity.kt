package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "organization_persons",
    indices = [
        Index("organizationId")
    ]
)
data class OrganizationPersonEntity(

    @PrimaryKey
    val orgPersonId: String,

    val organizationId: String? = null,

    val mainPersonId: String? = null,

    val fullName: String? = null,

    val role: Int? = null,

    val roleName: String? = null,

    val mobile: String? = null,

    val phone: String? = null,

    val gender: Int? = null,

    val genderName: String? = null,

    val description: String? = null,

    val isCommissionEligible: Boolean? = null,

    val statusRelation: Int? = null, //      فعال=1 ، قطع همکاری با سازمان=2

    val statusRelationName: String? = null,

    val remainBalance: Double? = null,

    val beforeRemain: Double? = null,

    val beforeDate: String? = null,

    val stateCode: Int? = null

)