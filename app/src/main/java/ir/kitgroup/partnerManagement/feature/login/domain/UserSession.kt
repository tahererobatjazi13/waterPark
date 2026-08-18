package ir.kitgroup.partnerManagement.feature.login.domain

import ir.kitgroup.partnerManagement.core.ui.util.UserRole

data class UserSession(
    val username: String,
    val role: UserRole
)
