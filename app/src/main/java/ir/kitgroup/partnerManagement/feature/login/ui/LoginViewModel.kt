package ir.kitgroup.partnerManagement.feature.login.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.network.BaseUrlProvider
import ir.kitgroup.partnerManagement.core.network.BaseUrlValidator
import ir.kitgroup.partnerManagement.core.ui.util.convertNumbersToEnglish
import ir.kitgroup.partnerManagement.core.ui.util.datastore.MainPreferences
import ir.kitgroup.partnerManagement.core.ui.util.fixPersianChars
import ir.kitgroup.partnerManagement.feature.login.domain.AuthRepository
import ir.kitgroup.partnerManagement.feature.login.domain.InvalidCredentialsException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    @Inject
    lateinit var mainPreferences: MainPreferences

    @Inject
    lateinit var baseUrlProvider: BaseUrlProvider

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


    fun onOpenServerDialog() {
        viewModelScope.launch {
            val savedBaseUrl = mainPreferences.baseUrlFlow.firstOrNull().orEmpty()
            val displayAddress = extractHostAndPort(savedBaseUrl)
            _uiState.update {
                it.copy(
                    isServerDialogVisible = true,
                    currentServerAddress = displayAddress,
                    serverAddressError = null
                )
            }
        }
    }

    fun onDismissServerDialog() {
        _uiState.update { it.copy(isServerDialogVisible = false, serverAddressError = null) }
    }

    fun onSaveServerAddress(inputAddress: String) {
        val cleanAddress = convertNumbersToEnglish(fixPersianChars(inputAddress)).trim()

        if (cleanAddress.isEmpty()) {
            _uiState.update { it.copy(serverAddressError = R.string.error_enter_address_server) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isTestingServer = true, serverAddressError = null) }

            val validBaseUrl = BaseUrlValidator.buildBaseUrl(cleanAddress)
            if (validBaseUrl == null) {
                _uiState.update {
                    it.copy(
                        isTestingServer = false,
                        serverAddressError = R.string.error_unable_connect_server
                    )
                }
                return@launch
            }

            // ذخیره و به‌روزرسانی زنده در Retrofit/BaseUrlProvider
            mainPreferences.saveBaseUrl(validBaseUrl)
            baseUrlProvider.updateBaseUrl(validBaseUrl)

            _uiState.update {
                it.copy(
                    isTestingServer = false,
                    isServerDialogVisible = false,
                    serverAddressError = null
                )
            }
        }
    }

    private fun extractHostAndPort(fullUrl: String): String {
        return try {
            val uri = fullUrl.toHttpUrl()
            if (uri.port == 80 || uri.port == 443) uri.host else "${uri.host}:${uri.port}"
        } catch (e: Exception) {
            ""
        }
    }
}
