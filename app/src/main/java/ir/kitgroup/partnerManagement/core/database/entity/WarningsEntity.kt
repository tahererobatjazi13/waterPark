package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "warnings")
data class WarningsEntity(

    @PrimaryKey
    val warningsId: String,

    val name: String? = null,

    val score: Int? = null,

    val recreationCenterId: String? = null,

    val code: String? = null
)