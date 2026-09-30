package ir.kitgroup.partnerManagement.feature.login.domain


data class UserSession(
    val userName: String,
    val fullName: String,
    val mobile: String,
    val roleCode: Int,
    val roleName: String
)