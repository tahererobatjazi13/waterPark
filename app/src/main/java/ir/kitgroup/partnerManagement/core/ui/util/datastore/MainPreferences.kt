package ir.kitgroup.partnerManagement.core.ui.util.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.kitgroup.partnerManagement.core.ui.util.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("user_prefs")
val Context.filterConditionDataStore by preferencesDataStore(name = "filter_condition_prefs")

val Context.settingsDataStore by preferencesDataStore(name = "settings")

object ThemePreferences {
    val THEME_MODE = stringPreferencesKey("theme_mode")

    fun fromString(value: String?): ThemeMode {
        return when (value) {
            "LIGHT" -> ThemeMode.LIGHT
            "DARK" -> ThemeMode.DARK
            else -> ThemeMode.SYSTEM
        }
    }
}


@Singleton
class MainPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        val KEY_ID = intPreferencesKey("key_id")
        val KEY_FIRST_NAME = stringPreferencesKey("key_firstName")
        val KEY_LAST_NAME = stringPreferencesKey("key_lastName")
        val KEY_IS_LOGGED = booleanPreferencesKey("key_is_login")
        val KEY_BASE_URL = stringPreferencesKey("base_url")
        val KEY_USER_ROLE = stringPreferencesKey("key_user_role")
        val KEY_USERNAME = stringPreferencesKey("key_username")


    }

    val id: Flow<Int?> = context.dataStore.data
        .map { it[KEY_ID] }

    val firstName: Flow<String?> = context.dataStore.data
        .map { it[KEY_FIRST_NAME] }

    val lastName: Flow<String?> = context.dataStore.data
        .map { it[KEY_LAST_NAME] }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[KEY_IS_LOGGED] ?: false
        }


    val baseUrlFlow: Flow<String?> = context.settingsDataStore.data
        .map { it[KEY_BASE_URL] }

    val userRole: Flow<String?> = context.dataStore.data.map { it[KEY_USER_ROLE] }
    val username: Flow<String?> = context.dataStore.data.map { it[KEY_USERNAME] }


    suspend fun saveLoginInfo(username: String, role: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_USERNAME] = username
            prefs[KEY_USER_ROLE] = role
            prefs[KEY_IS_LOGGED] = true
        }
    }

    suspend fun clearUserInfo() {
        context.dataStore.edit { prefs ->
            prefs.remove(KEY_ID)
            prefs.remove(KEY_FIRST_NAME)
            prefs.remove(KEY_LAST_NAME)
            prefs[KEY_IS_LOGGED] = false
            prefs.remove(KEY_USERNAME)
            prefs.remove(KEY_USER_ROLE)
        }
    }

    suspend fun clearFilterConditionInfo() {
        context.filterConditionDataStore.edit { it.clear() }
    }

    suspend fun saveBaseUrl(baseUrl: String) {
        context.settingsDataStore.edit {
            it[KEY_BASE_URL] = baseUrl
        }
    }

    suspend fun getBaseUrl(): String {
        return baseUrlFlow.first() ?: "http://default/api/Android/"
    }

}
