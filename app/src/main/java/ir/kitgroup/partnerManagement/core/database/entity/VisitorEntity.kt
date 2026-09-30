package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "visitors")
data class VisitorEntity(
    @PrimaryKey
    val visitorId: String,

    val name: String? = null,

    val code: String? = null,

    val mobile: String? = null,

    val workType: Int? = null,

    val workTypeName: String? = null,

    val stateCode: Int? = null

)