package ir.kitgroup.partnerManagement.feature.login.ui

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val usernameErrorRes: Int? = null,
    val passwordErrorRes: Int? = null,
    val loginErrorRes: Int? = null,
    val isLoading: Boolean = false,

    // فیلدهای مربوط به دیالوگ سرور
    val isServerDialogVisible: Boolean = false,
    val currentServerAddress: String = "",
    val serverAddressError: Int? = null,
    val isTestingServer: Boolean = false
)

sealed interface LoginEffect {
    data object NavigateToDashboard : LoginEffect
}
