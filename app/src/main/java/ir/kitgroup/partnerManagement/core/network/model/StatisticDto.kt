package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable


@Serializable
data class StatisticDto(

    val totalVisits: Int?,

    val totalAssignedStands: Int?,

    val totalAssignedSerials: Int?,

    val activeContractsCount: Int?,

    val totalWarningsRegistered: Int?
)