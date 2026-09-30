package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "warning_types")
data class WarningTypeEntity(

    @PrimaryKey
    val warningTypeId: String,

    val title: String? = null,

    val code: String? = null,

    val score: Int? = null,

    val stateCode: Int? = null
)