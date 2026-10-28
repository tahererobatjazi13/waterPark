package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class MeetingDto(

    val meetingId: String? = null,

    // این فیلد فقط مصرف لوکال دارد و در JSON ارسالی به سرور قرار نمی‌گیرد
    @Transient
    val serverId: String? = null,

    val organizationId: String?,

    val organizationName: String?,

    val visitorId: String?,

    val visitorName: String?,

    val subjectVisitId: String?,

    val subjectVisitName: String?,

    val organizationPersonId: String?,

    val organizationPersonName: String?,

    val description: String?,

    val visitDate: String?,

    val visitRealDate: String?,

    val visitTime: String?,

    val status: Int?,

    val statusName: String?,

    val type: Int?,

    val typeName: String?,

    val longitude: Double?,

    val latitude: Double?,

    val stateCode: Int?,

    val modifiedOn: String?

)