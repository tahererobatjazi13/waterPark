package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductServiceDto(
    val productServiceId: String,

    val name: String? = null,

    val basePrice: String? = null,

    val code: String? = null,

    val isCommissionable: Boolean? = null,

    val itemType: Int? = null,

    val recreationCenterId: String? = null,

    val unit: Int? = null,

    val basePriceExternal: String? = null
)