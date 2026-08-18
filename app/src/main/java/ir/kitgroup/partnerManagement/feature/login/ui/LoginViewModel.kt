package ir.kitgroup.partnerManagement.feature.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.feature.login.domain.AuthRepository
import ir.kitgroup.partnerManagement.feature.login.domain.InvalidCredentialsException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _effects = Channel<LoginEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onUsernameChange(value: String) {
        _uiState.update {
            it.copy(username = value, usernameErrorRes = null, loginErrorRes = null)
        }
    }

    fun onPasswordChange(value: String) {
        _uiState.update {
            it.copy(password = value, passwordErrorRes = null, loginErrorRes = null)
        }
    }

    fun onLoginClick() {
        val current = _uiState.value
        if (current.isLoading) return

        val usernameError = if (current.username.isBlank()) {
            R.string.error_username_required
        } else {
            null
        }
        val passwordError = if (current.password.isBlank()) {
            R.string.error_password_required
        } else {
            null
        }

        if (usernameError != null || passwordError != null) {
            _uiState.update {
                it.copy(
                    usernameErrorRes = usernameError,
                    passwordErrorRes = passwordError,
                    loginErrorRes = null
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    usernameErrorRes = null,
                    passwordErrorRes = null,
                    loginErrorRes = null
                )
            }

            val result = authRepository.login(
                username = current.username,
                password = current.password
            )

            result.fold(
                onSuccess = { session ->
                    _uiState.update { state -> state.copy(isLoading = false) }
                    _effects.send(LoginEffect.NavigateToDashboard)
                },
                onFailure = { error ->
                    val errorRes = if (error is InvalidCredentialsException) {
                        R.string.error_invalid_login
                    } else {
                        R.string.error_invalid_login
                    }
                    _uiState.update {
                        it.copy(isLoading = false, loginErrorRes = errorRes)
                    }
                }
            )
        }
    }
}
