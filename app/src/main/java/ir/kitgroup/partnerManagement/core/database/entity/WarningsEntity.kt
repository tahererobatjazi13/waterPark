package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "warnings")
data class WarningsEntity(
    @PrimaryKey val warningsId: Int,
    val name: String,
    val score: String,
    val receationCenterId: Int,
    val code: String
)