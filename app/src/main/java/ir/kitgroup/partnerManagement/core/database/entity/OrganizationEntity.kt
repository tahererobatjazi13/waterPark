package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "organizations")
data class OrganizationEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val cityId: Long,
    val regionId: Long?,
    val address: String?,
    val phone: String?,
    val latitude: Double?,
    val longitude: Double?,
    val grade: String?,
    val status: String?
)