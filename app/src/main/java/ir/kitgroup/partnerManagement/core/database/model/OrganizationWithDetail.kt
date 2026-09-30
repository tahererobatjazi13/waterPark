package ir.kitgroup.partnerManagement.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import ir.kitgroup.partnerManagement.core.database.entity.CityEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationEntity
import ir.kitgroup.partnerManagement.core.database.entity.RegionEntity

data class OrganizationWithDetail(

    @Embedded
    val organization: OrganizationEntity,

    @Relation(
        parentColumn = "cityId",
        entityColumn = "cityId"
    )
    val city: CityEntity?,

    @Relation(
        parentColumn = "regionId",
        entityColumn = "regionId"
    )
    val region: RegionEntity?

) {
    val cityName: String
        get() = city?.name ?: ""

    val regionName: String
        get() = region?.name ?: ""
}