package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class WarningDto(

    val warningId: String? = null,
    // این فیلد فقط مصرف لوکال دارد و در JSON ارسالی به سرور قرار نمی‌گیرد
    @Transient
    val serverId: String? = null,

    val organizationId: String?,

    val organizationName: String?,

    val meetingId: String?,

    val visitorId: String?,

    val visitorName: String?,

    val warningTypeId: String?,

    val warningTypeName: String?,

    val score: Int?,

    val description: String?,

    val warningDate: String?,

    val stateCode: Int?,

    val modifiedOn: String?

)