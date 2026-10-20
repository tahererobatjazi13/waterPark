package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assign_stand_change_status")
data class AssignStandChangeStatusEntity(
    @PrimaryKey
    val standAssignmentId: String,
    val organizationId: String,
    val status: Int,
    val actualReturnDate: String,
    val deliveryToOrgDate: String,
    val createdAt: Long = System.currentTimeMillis()
)

