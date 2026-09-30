package ir.kitgroup.partnerManagement.core.database.entity


import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "statistics")
data class StatisticEntity(
    @PrimaryKey
    val id: Int = 1,

    val totalVisits: Int?,
    val totalAssignedStands: Int?,
    val totalAssignedSerials: Int?,
    val activeContractsCount: Int?,
    val totalWarningsRegistered: Int?
)