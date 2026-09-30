package ir.kitgroup.partnerManagement.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.ui.util.datastore.MainPreferences
import ir.kitgroup.partnerManagement.feature.login.domain.UserSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val mainPreferences: MainPreferences
) : ViewModel() {

    // =========================================================
    // Session
    // =========================================================

    val session: StateFlow<UserSession?> =
        mainPreferences.session.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    // =========================================================
    // Session Status
    // =========================================================

    private val _sessionStatus =
        MutableStateFlow<SessionStatus>(
            SessionStatus.Checking
        )

    val sessionStatus: StateFlow<SessionStatus> =
        _sessionStatus.asStateFlow()

    // =========================================================
    // Profile
    // =========================================================

    val fullName: StateFlow<String?> =
        mainPreferences.fullName.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val userName: StateFlow<String?> =
        mainPreferences.userName.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val userMobile: StateFlow<String?> =
        mainPreferences.userMobile.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val roleCode: StateFlow<Int?> =
        mainPreferences.roleCode.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val roleName: StateFlow<String?> =
        mainPreferences.roleName.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )
    val centerName: StateFlow<String?> =
        mainPreferences.centerName
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )


    val centerBrandName: StateFlow<String?> =
        mainPreferences.centerBrandName
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    val centerPhone: StateFlow<String?> =
        mainPreferences.centerPhone
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    val centerSupportPhone: StateFlow<String?> =
        mainPreferences.centerSupportPhone
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    val centerAddress: StateFlow<String?> =
        mainPreferences.centerAddress
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    val centerWebsite: StateFlow<String?> =
        mainPreferences.centerWebsite
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    val centerLatitude: StateFlow<String?> =
        mainPreferences.centerLatitude
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    val centerLongitude: StateFlow<String?> =
        mainPreferences.centerLongitude
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )

    val isLoggedIn: StateFlow<Boolean> =
        mainPreferences.isLoggedIn.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    init {
        observeSession()
    }

    private fun observeSession() {

        viewModelScope.launch {

            mainPreferences.session.collect { userSession ->

                _sessionStatus.value =
                    if (userSession != null) {
                        SessionStatus.LoggedIn(userSession)
                    } else {
                        SessionStatus.LoggedOut
                    }
            }
        }
    }

    // =========================================================
    // Logout
    // =========================================================

    fun logout(
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            mainPreferences.logout()
            onComplete()
        }
    }
}
