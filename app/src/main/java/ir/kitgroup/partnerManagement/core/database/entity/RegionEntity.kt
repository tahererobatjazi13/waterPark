package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "region")
data class RegionEntity(

    @PrimaryKey
    val regionId: String,

    val name: String? = null,

    val cityId: String? = null,

    val recreationCenterId: String? = null
)