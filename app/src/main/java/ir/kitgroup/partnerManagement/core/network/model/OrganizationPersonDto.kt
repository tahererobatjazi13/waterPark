package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class OrganizationPersonDto(

    val orgPersonId: String,

    val organizationId: String,

    val mainPersonId: String?,

    val fullName: String?,

    val role: Int?,

    val roleName: String?,

    val mobile: String?,

    val phone: String?,

    val gender: Int?,

    val genderName: String?,

    val description: String?,

    val isCommissionEligible: Boolean?,

    val statusRelation: Int?,

    val statusRelationName: String?,

    val remainBalance: Double?,

    val beforeRemain: Double?,

    val beforeDate: String?,

    val stateCode: Int?

)