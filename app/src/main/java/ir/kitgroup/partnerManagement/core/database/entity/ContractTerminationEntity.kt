package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contract_terminations")
data class ContractTerminationEntity(
    @PrimaryKey
    val contractId: String,
    val organizationId: String,
    val terminationDate: String,
    val createdAt: Long = System.currentTimeMillis()
)
