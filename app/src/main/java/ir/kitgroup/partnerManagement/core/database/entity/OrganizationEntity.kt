package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "organization")
data class OrganizationEntity(

    @PrimaryKey
    val organizationId: String,

    val name: String? = null,

    val address: String? = null,

    val cityId: String? = null,

    val code: String? = null,

    val customerCapacity: Int? = null,

    val document: String? = null,

    val email: String? = null,

    val englishName: String? = null,

    val grade: Int? = null,

    val landLine: String? = null,

    val latitude: Double? = null,

    val longitude: Double? = null,

    val mobile: String? = null,

    val nationalId: String? = null,

    val organizationType: Int? = null,

    val ownerName: String? = null,

    val phone: String? = null,

    val regionId: String? = null,

    val status: Int? = null, // / 0 = ثبت اولیه، 1 = فعال، 2 = غیر فعال، 3 = معلق، 4 = بسته شده، 5 = زرد، 6 = ارجاع فوری / نارنجی

    val ticketSaleCountHistory: Int? = null,

    val statusExternalCustomer: Boolean? = null,

    val level: Int? = null,

    val statusGetStand: Boolean? = null,

    val reasonDeactive: String? = null,

    val countWarning: Int? = null,

    val sumScore: Int? = null,

    val recreationCenterId: String? = null,

    val sourceCreate: Int? = null,

    val recalculateStatistics: Boolean? = null,

    val countVisitorActive: Int? = null,

    val countStandAssign: Int? = null,

    val assignedSerialCount: Int? = null,

    val cancelledSerialCount: Int? = null,

    val remainingSerialCount: Int? = null,

    val issuedSerialCount: Int? = null,

    val visitCount: Int? = null,

    val visitorId: String? = null,

    val visitDate: String? = null,

    val programmingVisitCount: Int? = null,

    val usingSerialCount: Int? = null,

    val deactiveDate: String? = null,

    val subjectVisitId: String? = null
)