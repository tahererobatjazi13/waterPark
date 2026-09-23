package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class VisitorDto(
    val visitorId: String,

    val name: String? = null,

    val code: String? = null,

    val description: String? = null,

    val mobile: String? = null,

    val recreationCenterId: String? = null,

    val workType: Int? = null,

    val systemUserId: String? = null,

    val appPassword: String? = null,

    val recreationCenterPartId: String? = null,

    val countActiveOrg: Int? = null,

    val deviceModel: String? = null,

    val androidVersion: String? = null,

    val appVersion: String? = null,

    val imei: String? = null,

    val lastSyncDate: String? = null
)