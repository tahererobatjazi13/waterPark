package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val productId: String,

    val name: String? = null,

    val code: String? = null,

    val price: Double? = null,

    val priceExternal: Double? = null,

    val itemType: Int? = null,

    val itemTypeName: String? = null,

    val isCommissionable: Boolean? = null,

    val stateCode: Int? = null

)