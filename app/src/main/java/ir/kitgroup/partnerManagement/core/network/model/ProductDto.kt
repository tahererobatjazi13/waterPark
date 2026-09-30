package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val productId: String,

    val name: String?,

    val code: String?,

    val price: Double?,

    val priceExternal: Double?,

    val itemType: Int?,

    val itemTypeName: String?,

    val isCommissionable: Boolean?,

    val stateCode: Int?

)