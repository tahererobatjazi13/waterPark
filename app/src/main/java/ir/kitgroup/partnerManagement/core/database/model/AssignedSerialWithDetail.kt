package ir.kitgroup.partnerManagement.core.database.model

import androidx.room.Embedded
import androidx.room.Relation
import ir.kitgroup.partnerManagement.core.database.entity.AssignedSerialEntity
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.database.entity.OfferEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationEntity

data class AssignedSerialWithDetail(
    @Embedded
    val assignedSerial: AssignedSerialEntity,

    @Relation(
        parentColumn = "offerId",
        entityColumn = "offerId"
    )
    val offer: OfferEntity?,

    @Relation(
        parentColumn = "contractId",
        entityColumn = "contractId"
    )
    val contract: ContractEntity?,

    @Relation(
        parentColumn = "organizationId",
        entityColumn = "organizationId"
    )
    val organization: OrganizationEntity?
) {

    val offerName: String
        get() = offer?.title
            ?: ""
    val contractName: String
        get() = contract?.title
            ?: ""

    val organizationName: String
        get() = organization?.name
            ?: ""
}
