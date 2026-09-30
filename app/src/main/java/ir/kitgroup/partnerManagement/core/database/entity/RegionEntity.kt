package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "regions",
    indices = [
        Index("cityId")
    ]
)
data class RegionEntity(

    @PrimaryKey
    val regionId: String,

    val name: String? = null,

    val cityId: String? = null,

    val stateCode: Int? = null
)