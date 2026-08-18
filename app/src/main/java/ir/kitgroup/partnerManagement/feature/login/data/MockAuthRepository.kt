package ir.kitgroup.partnerManagement.feature.login.data

import ir.kitgroup.partnerManagement.core.ui.util.UserRole
import ir.kitgroup.partnerManagement.core.ui.util.datastore.MainPreferences
import ir.kitgroup.partnerManagement.feature.login.domain.AuthRepository
import ir.kitgroup.partnerManagement.feature.login.domain.InvalidCredentialsException
import ir.kitgroup.partnerManagement.feature.login.domain.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockAuthRepository @Inject constructor(
    private val mainPreferences: MainPreferences
) : AuthRepository {

    override val session: Flow<UserSession?> = combine(
        mainPreferences.isLoggedIn,
        mainPreferences.username,
        mainPreferences.userRole
    ) { isLoggedIn, username, roleName ->
        if (!isLoggedIn || username.isNullOrBlank() || roleName.isNullOrBlank()) {
            return@combine null
        }
        val role = runCatching { UserRole.valueOf(roleName) }.getOrNull()
            ?: return@combine null
        UserSession(username = username, role = role)
    }

    override suspend fun login(username: String, password: String): Result<UserSession> {
        val role = when {
            username == "s" && password == "123" -> UserRole.SUPERVISOR
            username == "v" && password == "123" -> UserRole.VISITOR
            else -> return Result.failure(InvalidCredentialsException())
        }

        val userSession = UserSession(username = username, role = role)
        mainPreferences.saveLoginInfo(
            username = userSession.username,
            role = userSession.role.name
        )
        return Result.success(userSession)
    }

    override suspend fun logout() {
        mainPreferences.clearUserInfo()
    }
}
