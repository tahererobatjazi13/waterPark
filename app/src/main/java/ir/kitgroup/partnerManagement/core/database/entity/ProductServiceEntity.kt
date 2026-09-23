package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_service")
data class ProductServiceEntity(
    @PrimaryKey
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