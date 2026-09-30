package ir.kitgroup.partnerManagement.core.ui.util

import ir.kitgroup.partnerManagement.feature.login.domain.UserSession

sealed interface SessionStatus {

    data object Checking : SessionStatus

    data class LoggedIn(
        val session: UserSession
    ) : SessionStatus

    data object LoggedOut : SessionStatus
}