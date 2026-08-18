package ir.kitgroup.partnerManagement.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.feature.login.domain.AuthRepository
import ir.kitgroup.partnerManagement.feature.login.domain.UserSession
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    /**
     * Session واقعی کاربر
     */
    val session: StateFlow<UserSession?> =
        authRepository.session
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = null
            )

    /**
     * وضعیت Session
     *
     * نکته مهم:
     * مستقیماً از authRepository.session ساخته می‌شود
     * تا مقدار اولیه null در session باعث LoggedOut شدن
     * زودهنگام نشود.
     */
    val sessionStatus: StateFlow<SessionStatus> =
        authRepository.session
            .map { userSession ->
                if (userSession != null) {
                    SessionStatus.LoggedIn(userSession)
                } else {
                    SessionStatus.LoggedOut
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = SessionStatus.Checking
            )

    /**
     * وضعیت ورود
     */
    val isLoggedIn: StateFlow<Boolean> =
        session
            .map { it != null }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = false
            )

    /**
     * نام کاربری
     */
    val userName: StateFlow<String> =
        session
            .map { it?.username.orEmpty() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = ""
            )

    /**
     * نقش کاربر
     */
    val userRole: StateFlow<String> =
        session
            .map { it?.role?.name.orEmpty() }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = ""
            )

    /**
     * Logout
     */
    fun logout(
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {

            authRepository.logout()

            onComplete()
        }
    }
}