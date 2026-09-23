package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class OrganizationPersonDto(
    val organizationPersonId: String,

    val name: String? = null,

    val description: String? = null,

    val isCommissionEligible: Boolean? = null,

    val organizationId: String? = null,

    val personId: String? = null,

    val role: Int? = null,

    val beforeDate: String? = null,

    val beforeRemain: String? = null,

    val remainBalance: String? = null,

    val appPassword: String? = null,

    val updateRemainPriceWallet: Boolean? = null,

    val finalDate: String? = null,

    val statusRelation: Int? = null
)