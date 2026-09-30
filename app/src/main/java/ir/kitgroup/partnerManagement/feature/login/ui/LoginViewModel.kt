package ir.kitgroup.partnerManagement.feature.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.network.BaseUrlProvider
import ir.kitgroup.partnerManagement.core.network.NetworkResult
import ir.kitgroup.partnerManagement.core.ui.util.datastore.MainPreferences
import ir.kitgroup.partnerManagement.core.ui.util.fixPersianChars
import ir.kitgroup.partnerManagement.core.ui.util.toEnglishDigits
import ir.kitgroup.partnerManagement.feature.login.model.LoginResponse
import ir.kitgroup.partnerManagement.feature.login.repository.LoginRepository
import ir.kitgroup.partnerManagement.feature.login.validator.ServerAddressValidator
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val mainPreferences: MainPreferences,
    private val baseUrlProvider: BaseUrlProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _effects = MutableSharedFlow<LoginEffect>()
    val effects = _effects.asSharedFlow()

    // =========================================================
    // Username
    // =========================================================

    fun onUsernameChange(value: String) {
        _uiState.update {
            it.copy(
                username = value,
                usernameError = null,
                loginError = null
            )
        }
    }

    // =========================================================
    // Password
    // =========================================================

    fun onPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                password = value,
                passwordError = null,
                loginError = null
            )
        }
    }

    // =========================================================
    // Login
    // =========================================================
    fun onLoginClick() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            val baseUrl = mainPreferences.baseUrlFlow.first().orEmpty()
            if (baseUrl.isBlank()) {
                _uiState.update {
                    it.copy(
                        loginError = "ابتدا تنظیمات سرور را انجام دهید.",
                        isServerDialogVisible = true,
                        currentServerAddress = "",
                        serverAddressError = null
                    )
                }
                return@launch
            }

            proceedLoginValidationAndCall()
        }
    }


    fun proceedLoginValidationAndCall() {

        val state = _uiState.value

        val username = state.username
            .trim()
            .toEnglishDigits()
            .fixPersianChars()

        val password = state.password
            .trim()
            .toEnglishDigits()

        _uiState.update {
            it.copy(
                usernameError = null,
                passwordError = null,
                loginError = null
            )
        }

        when {
            username.isBlank() -> {
                _uiState.update {
                    it.copy(
                        usernameError = "نام کاربری را وارد کنید"
                    )
                }
            }

            password.isBlank() -> {
                _uiState.update {
                    it.copy(
                        passwordError = "رمز عبور را وارد کنید"
                    )
                }
            }

            else -> {
                login(username, password)
            }
        }
    }

    private fun login(
        username: String,
        password: String
    ) {

        if (_uiState.value.isLoading) {
            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    loginError = null
                )
            }

            when (
                val result = loginRepository.loginUser(
                    username = username,
                    password = password
                )
            ) {

                is NetworkResult.Loading -> Unit

                is NetworkResult.Error -> {

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            loginError = result.message
                        )
                    }
                }

                is NetworkResult.Success -> {

                    handleLoginSuccess(result.data)
                }
            }
        }
    }

    private suspend fun handleLoginSuccess(
        response: LoginResponse
    ) {

        if (!response.success) {

            _uiState.update {
                it.copy(
                    isLoading = false,
                    loginError = response.message
                        ?: "ورود ناموفق بود"
                )
            }

            return
        }

        val user = response.user

        if (user == null) {

            _uiState.update {
                it.copy(
                    isLoading = false,
                    loginError =
                    "اطلاعات کاربر از سرور دریافت نشد"
                )
            }

            return
        }

        val token = response.token

        if (token.isNullOrBlank()) {

            _uiState.update {
                it.copy(
                    isLoading = false,
                    loginError =
                    "توکن ورود از سرور دریافت نشد"
                )
            }

            return
        }

        mainPreferences.saveFullSession(
            token = token,
            user = user,
            center = response.center
        )

        _uiState.update {
            it.copy(
                isLoading = false,
                loginError = null
            )
        }

        _effects.emit(
            LoginEffect.NavigateToMain
        )
    }

    // =========================================================
    // Server
    // =========================================================

    fun onOpenServerDialog() {

        viewModelScope.launch {

            val baseUrl =
                mainPreferences.baseUrlFlow.first().orEmpty()

            _uiState.update {
                it.copy(
                    isServerDialogVisible = true,
                    currentServerAddress =
                    ServerAddressValidator.extractHostAndPort(
                        baseUrl
                    ),
                    serverAddressError = null
                )
            }
        }
    }

    fun onDismissServerDialog() {

        if (_uiState.value.isTestingServer) {
            return
        }

        _uiState.update {
            it.copy(
                isServerDialogVisible = false,
                serverAddressError = null
            )
        }
    }

    fun onSaveServerAddress(address: String) {

        val normalizedAddress = address
            .trim()
            .toEnglishDigits()
            .fixPersianChars()

        if (normalizedAddress.isBlank()) {

            _uiState.update {
                it.copy(
                    serverAddressError =
                    "آدرس سرور را وارد کنید"
                )
            }

            return
        }

        val baseUrl =
            ServerAddressValidator.normalize(
                normalizedAddress
            )

        if (baseUrl == null) {

            _uiState.update {
                it.copy(
                    serverAddressError =
                    "آدرس سرور نامعتبر است"
                )
            }

            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isTestingServer = true,
                    serverAddressError = null
                )
            }

            try {

                // ذخیره دائمی
                mainPreferences.saveBaseUrl(baseUrl)

                // اعمال فوری روی Retrofit/OkHttp
                baseUrlProvider.updateBaseUrl(baseUrl)

                _uiState.update {
                    it.copy(
                        isTestingServer = false,
                        isServerDialogVisible = false,
                        currentServerAddress =
                        ServerAddressValidator
                            .extractHostAndPort(baseUrl),
                        serverAddressError = null
                    )
                }

            } catch (e: Exception) {

                _uiState.update {
                    it.copy(
                        isTestingServer = false,
                        serverAddressError =
                        e.message
                            ?: "ذخیره آدرس سرور انجام نشد"
                    )
                }
            }
        }
    }
}

sealed interface LoginEffect {

    data object NavigateToMain : LoginEffect
}