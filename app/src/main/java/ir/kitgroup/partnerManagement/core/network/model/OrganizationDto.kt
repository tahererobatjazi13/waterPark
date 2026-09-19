package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class OrganizationDto(
    val id: Long,
    val name: String,
    val cityId: Long,
    val regionId: Long?,
    val address: String?,
    val phone: String?,
    val latitude: Double?,
    val longitude: Double?,
    val grade: String?,
    val status: String?
)

