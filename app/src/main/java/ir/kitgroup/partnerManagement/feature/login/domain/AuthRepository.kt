package ir.kitgroup.partnerManagement.feature.login.domain



import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val session: Flow<UserSession?>
    suspend fun login(username: String, password: String): Result<UserSession>
    suspend fun logout()
}
