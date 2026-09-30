package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "organizations",
    indices = [
        Index(value = ["cityId"]),
        Index(value = ["regionId"]),
        Index(value = ["visitorId"])
    ]
)
data class OrganizationEntity(

    @PrimaryKey
    val organizationId: String,

    val name: String? = null,

    val ownerName: String? = null,

    val code: String? = null,

    val nationalId: String? = null,

    val organizationType: Int? = null,

    val organizationTypeName: String? = null,

    val grade: Int? = null,

    val gradeName: String? = null,

    val statusGetStand: Boolean? = null,

    val statusGetStandName: String? = null,

    val level: Int? = null,

    val levelName: String? = null,

    val status: Int? = null, // / 0 = ثبت اولیه، 1 = فعال، 2 = غیر فعال، 3 = معلق، 4 = بسته شده، 5 = زرد، 6 = ارجاع فوری / نارنجی

    val statusName: String? = null,

    val phone: String? = null,

    val landLine: String? = null,

    val mobile: String? = null,

    val address: String? = null,

    val cityId: String? = null,

    val cityName: String? = null,

    val regionId: String? = null,

    val regionName: String? = null,

    val latitude: String? = null,

    val longitude: String? = null,

    val visitorId: String? = null,

    val statusExternalCustomer: Boolean? = null,

    val englishName: String? = null,

    val document: String? = null,

    val customerCapacity: Int? = null,

    val email: String? = null,

    val ticketSaleCountHistory: Int? = null,

    val issuedSerialCount: Int? = null,

    val remainingSerialCount: Int? = null,

    val assignedSerialCount: Int? = null,

    val cancelledSerialCount: Int? = null,

    val usingSerialCount: Int? = null,

    val programingVisitCount: Int? = null,

    val visitCount: Int? = null,

    val countVisitorActive: Int? = null,

    val countStandAssign: Int? = null,

    val countWarning: Int? = null,

    val sumScore: Int? = null,

    val deactiveDate: String? = null,

    val reasonDeactive: String? = null,

    val stateCode: Int? = null
)