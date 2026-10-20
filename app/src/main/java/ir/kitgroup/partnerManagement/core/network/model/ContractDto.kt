package ir.kitgroup.partnerManagement.core.network.model


import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ContractDto(

    val contractId: String? = null,

    // این فیلد فقط مصرف لوکال دارد و در JSON ارسالی به سرور قرار نمی‌گیرد
    @Transient
    val serverId: String? = null,

    val title: String? = null,

    val organizationId: String? = null,

    val contractNumber: String? = null,

    val startDate: String? = null,

    val endDate: String? = null,

    val description: String? = null,

    val settlementPeriodType: Int? = null,

    val settlementPeriodTypeName: String? = null,

    val cooperationModel: Int? = null,

    val cooperationModelName: String? = null,

    val defaultDiscountPercent: Double? = null,

    val defaultCommissionPercent: Double? = null,

    val status: Int? = null,

    val statusName: String? = null,

    val stateCode: Int? = null,

    val modifiedOn: String? = null
)
