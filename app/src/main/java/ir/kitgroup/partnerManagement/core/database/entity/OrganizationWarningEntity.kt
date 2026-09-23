package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "organization_warning")
data class OrganizationWarningEntity(
    @PrimaryKey
    val organizationWarningId: String,

    val name: String? = null,

    val organizationId: String? = null,

    val warningId: String? = null,

    val visitorId: String? = null,

    val dateWarning: String? = null,

    val description: String? = null,

    val score: Int? = null,

    val meetingId: String? = null
)