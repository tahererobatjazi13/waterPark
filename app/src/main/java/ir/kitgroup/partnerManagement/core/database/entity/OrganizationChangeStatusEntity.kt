package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "organization_change_status")
data class OrganizationChangeStatusEntity(
    @PrimaryKey
    val organizationId: String,
    val statusOrg: Int,
    val deactiveDate: String,
    val reason: String,
    val visitDate: String,
    val subjectVisitId: String,
    val createdAt: Long = System.currentTimeMillis()
)


