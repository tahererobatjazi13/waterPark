package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "regions")
data class RegionEntity(
    @PrimaryKey val regionId: Int,
    val name: String,
    val cityId: Int,
    val receationCenterId: Int
)