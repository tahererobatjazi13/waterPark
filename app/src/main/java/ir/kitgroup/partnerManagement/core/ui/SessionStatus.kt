package ir.kitgroup.partnerManagement.core.ui

import ir.kitgroup.partnerManagement.feature.login.domain.UserSession

sealed interface SessionStatus {
    data object Checking : SessionStatus
    data object LoggedOut : SessionStatus
    data class LoggedIn(val session: UserSession) : SessionStatus
}
