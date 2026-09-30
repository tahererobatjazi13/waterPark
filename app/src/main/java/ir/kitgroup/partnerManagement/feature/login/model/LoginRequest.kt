package ir.kitgroup.partnerManagement.feature.login.model


data class LoginRequest(
    val username: String,
    val password: String,
    val deviceModel: String,
    val androidVersion: String,
    val imei: String,
    val appVersion: String
)
