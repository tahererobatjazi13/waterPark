package ir.kitgroup.partnerManagement.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import ir.kitgroup.partnerManagement.core.database.entity.OfferDetailEntity
import ir.kitgroup.partnerManagement.core.database.entity.ProductEntity // یا نام کلاس انتیتی محصول شما

data class OfferDetailWithProduct(
    @Embedded
    val offerDetail: OfferDetailEntity,

    @Relation(
        parentColumn = "productId",
        entityColumn = "productId"
    )
    val product: ProductEntity?
) {

    val resolvedProductName: String
        get() = product?.name?.takeIf { it.isNotBlank() }
            ?: offerDetail.productName?.takeIf { it.isNotBlank() }
            ?: offerDetail.offerDetailId
}
