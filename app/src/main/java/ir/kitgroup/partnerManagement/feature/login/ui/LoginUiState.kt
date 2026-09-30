package ir.kitgroup.partnerManagement.feature.login.ui

data class LoginUiState(
    val username: String = "",
    val password: String = "",

    val isLoading: Boolean = false,

    val usernameError: String? = null,
    val passwordError: String? = null,
    val loginError: String? = null,

    val isServerDialogVisible: Boolean = false,
    val currentServerAddress: String = "",
    val serverAddressError: String? = null,
    val isTestingServer: Boolean = false
)