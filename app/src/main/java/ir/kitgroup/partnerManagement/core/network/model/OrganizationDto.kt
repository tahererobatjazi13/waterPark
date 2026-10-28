package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class OrganizationDto(

    val organizationId: String? = null,

    // این فیلد فقط مصرف لوکال دارد و در JSON ارسالی به سرور قرار نمی‌گیرد
    @Transient
    val serverId: String? = null,

    val activeVisitorId: String?,

    val activeContractId: String?,

    val name: String?,

    val ownerName: String?,

    val code: String?,

    val nationalId: String?,

    val organizationType: Int?,

    val organizationTypeName: String?,

    val grade: Int?,

    val gradeName: String?,

    val statusGetStand: Boolean?,

    val statusGetStandName: String?,

    val level: Int?,

    val levelName: String?,

    val status: Int?,

    val statusName: String?,

    val phone: String?,

    val landLine: String?,

    val mobile: String?,

    val address: String?,

    val cityId: String?,

    val cityName: String?,

    val regionId: String?,

    val regionName: String?,

    val latitude: Double?,

    val longitude: Double?,

    val visitorId: String?,

    val statusExternalCustomer: Boolean?,

    val englishName: String?,

    val description: String?,

    val customerCapacity: Int?,

    val email: String?,

    val ticketSaleCountHistory: Int?,

    val issuedSerialCount: Int?,

    val remainingSerialCount: Int?,

    val assignedSerialCount: Int?,

    val cancelledSerialCount: Int?,

    val usingSerialCount: Int?,

    val programingVisitCount: Int?,

    val visitCount: Int?,

    val countVisitorActive: Int?,

    val countStandAssign: Int?,

    val countWarning: Int?,

    val sumScore: Int?,

    val deactiveDate: String?,

    val reasonDeactive: String?,

    val stateCode: Int?,

    val modifiedOn: String?

)