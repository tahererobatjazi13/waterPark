package ir.kitgroup.partnerManagement.feature.login.ui

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val usernameErrorRes: Int? = null,
    val passwordErrorRes: Int? = null,
    val loginErrorRes: Int? = null
)

sealed interface LoginEffect {
    data object NavigateToDashboard : LoginEffect
}
