package ir.kitgroup.partnerManagement.feature.login.model


data class LoginResponse(
    val success: Boolean,
    val message: String?,
    val user: UserDto?,
    val center: RecreationCenterDto?,
    val token: String?
)

data class UserDto(
    val visitorId: String,
    val systemUserId: String?,
    val fullName: String?,
    val mobile: String?,
    val username: String?,
    val roleCode: Int?,
    val roleName: String?,
    val isActive: Boolean
)

data class RecreationCenterDto(
    val centerId: String,
    val name: String?,
    val brandName: String?,
    val phone: String?,
    val supportPhone: String?,
    val address: String?,
    val website: String?,
    val latitude: String?,
    val longitude: String?,
    val imageBase64: String?
)