package ir.kitgroup.partnerManagement.core.ui.util.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.kitgroup.partnerManagement.core.ui.util.ThemeMode
import ir.kitgroup.partnerManagement.feature.login.domain.UserSession
import ir.kitgroup.partnerManagement.feature.login.model.RecreationCenterDto
import ir.kitgroup.partnerManagement.feature.login.model.UserDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.userPreferencesDataStore by preferencesDataStore(name = "user_prefs")
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
    private val userPrefs = context.userPreferencesDataStore
    private val settingsPrefs = context.settingsDataStore

    companion object {
        // User / Session
        val KEY_IS_LOGGED = booleanPreferencesKey("key_is_logged")
        val KEY_TOKEN = stringPreferencesKey("key_token")
        val KEY_VISITOR_ID = stringPreferencesKey("key_visitor_id")
        val KEY_SYSTEM_USER_ID = stringPreferencesKey("key_system_user_id")
        val KEY_FULL_NAME = stringPreferencesKey("key_full_name")
        val KEY_USERNAME = stringPreferencesKey("key_username")
        val KEY_USER_MOBILE = stringPreferencesKey("key_user_mobile")
        val KEY_ROLE_CODE = intPreferencesKey("key_role_code")
        val KEY_ROLE_NAME = stringPreferencesKey("key_role_name")
        val KEY_IS_ACTIVE = booleanPreferencesKey("key_is_active")

        // Recreation Center
        val KEY_CENTER_ID = stringPreferencesKey("key_center_id")
        val KEY_CENTER_NAME = stringPreferencesKey("key_center_name")
        val KEY_CENTER_BRAND = stringPreferencesKey("key_center_brand")
        val KEY_CENTER_PHONE = stringPreferencesKey("key_center_phone")
        val KEY_CENTER_SUPPORT_PHONE = stringPreferencesKey("key_center_support_phone")
        val KEY_CENTER_ADDRESS = stringPreferencesKey("key_center_address")
        val KEY_CENTER_WEBSITE = stringPreferencesKey("key_center_webSite")
        val KEY_CENTER_LATITUDE = stringPreferencesKey("key_center_latitude")
        val KEY_CENTER_LONGITUDE = stringPreferencesKey("key_center_longitude")

        // Settings
        val KEY_BASE_URL = stringPreferencesKey("key_base_url")
        val KEY_LAST_SYNC_TIME = longPreferencesKey("key_last_sync_time")
    }

    // Session Flows
    val isLoggedIn: Flow<Boolean> = userPrefs.data.map { it[KEY_IS_LOGGED] ?: false }
    val fullName: Flow<String?> = userPrefs.data.map { it[KEY_FULL_NAME] }
    val userName: Flow<String?> = userPrefs.data.map { it[KEY_USERNAME] }
    val userMobile: Flow<String?> = userPrefs.data.map { it[KEY_USER_MOBILE] }
    val roleCode: Flow<Int?> = userPrefs.data.map { it[KEY_ROLE_CODE] }
    val roleName: Flow<String?> = userPrefs.data.map { it[KEY_ROLE_NAME] }
    val token: Flow<String?> = userPrefs.data.map { it[KEY_TOKEN] }

    val session: Flow<UserSession?> =
        userPrefs.data.map { preferences ->

            val loggedIn =
                preferences[KEY_IS_LOGGED] ?: false

            if (!loggedIn) {
                null
            } else {

                val userName =
                    preferences[KEY_USERNAME]

                if (userName.isNullOrBlank()) {
                    null
                } else {
                    UserSession(
                        userName = userName,
                        fullName = preferences[KEY_FULL_NAME].orEmpty(),
                        mobile = preferences[KEY_USER_MOBILE].orEmpty(),
                        roleCode = preferences[KEY_ROLE_CODE] ?: 0,
                        roleName = preferences[KEY_ROLE_NAME].orEmpty()
                    )
                }
            }
        }


    // اطلاعات مرکز
    val centerId: Flow<String?> = userPrefs.data.map { it[KEY_CENTER_ID] }
    val centerName: Flow<String?> = userPrefs.data.map { it[KEY_CENTER_NAME] }
    val centerBrandName: Flow<String?> = userPrefs.data.map { it[KEY_CENTER_BRAND] }
    val centerPhone: Flow<String?> = userPrefs.data.map { it[KEY_CENTER_PHONE] }
    val centerSupportPhone: Flow<String?> = userPrefs.data.map { it[KEY_CENTER_SUPPORT_PHONE] }
    val centerAddress: Flow<String?> = userPrefs.data.map { it[KEY_CENTER_ADDRESS] }
    val centerWebsite: Flow<String?> = userPrefs.data.map { it[KEY_CENTER_WEBSITE] }
    val centerLatitude: Flow<String?> = userPrefs.data.map { it[KEY_CENTER_LATITUDE] }
    val centerLongitude: Flow<String?> = userPrefs.data.map { it[KEY_CENTER_LONGITUDE] }


    // Base URL
    val baseUrlFlow: Flow<String?> = settingsPrefs.data.map { it[KEY_BASE_URL] }

    suspend fun saveBaseUrl(baseUrl: String) {
        context.settingsDataStore.edit {
            it[KEY_BASE_URL] = baseUrl
        }
    }

    suspend fun getBaseUrl(): String {
        return baseUrlFlow.first() ?: "http://default/api/Android/"
    }

    // Save Full Session
    suspend fun saveFullSession(
        token: String,
        user: UserDto,
        center: RecreationCenterDto?
    ) {
        userPrefs.edit { prefs ->
            prefs[KEY_IS_LOGGED] = true
            prefs[KEY_TOKEN] = token
            prefs[KEY_VISITOR_ID] = user.visitorId
            prefs[KEY_SYSTEM_USER_ID] = user.systemUserId.orEmpty()
            prefs[KEY_FULL_NAME] = user.fullName.orEmpty()
            prefs[KEY_USERNAME] = user.username.orEmpty()
            prefs[KEY_USER_MOBILE] = user.mobile.orEmpty()
            prefs[KEY_ROLE_CODE] = user.roleCode!!.toInt()
            prefs[KEY_ROLE_NAME] = user.roleName.orEmpty()
            prefs[KEY_IS_ACTIVE] = user.isActive

            center?.let {
                prefs[KEY_CENTER_ID] = it.centerId
                prefs[KEY_CENTER_NAME] = it.name.orEmpty()
                prefs[KEY_CENTER_BRAND] = it.brandName.orEmpty()
                prefs[KEY_CENTER_PHONE] = it.phone.orEmpty()
                prefs[KEY_CENTER_SUPPORT_PHONE] = it.supportPhone.orEmpty()
                prefs[KEY_CENTER_ADDRESS] = it.address.orEmpty()
                prefs[KEY_CENTER_WEBSITE] = it.website.orEmpty()
                prefs[KEY_CENTER_LATITUDE] = it.latitude.orEmpty()
                prefs[KEY_CENTER_LONGITUDE] = it.longitude.orEmpty()
            }
        }
    }

    // Other Session Data
    suspend fun getVisitorId(): String {
        return userPrefs.data.map { it[KEY_VISITOR_ID].orEmpty() }.first()
    }

    suspend fun getSystemUserId(): String {
        return userPrefs.data.map { it[KEY_SYSTEM_USER_ID].orEmpty() }.first()
    }

    suspend fun getRoleCode(): Int {
        return userPrefs.data.map { it[KEY_ROLE_CODE] ?: 0 }.first()
    }

    // Sync
    suspend fun saveLastSyncTime(timestamp: Long) {
        settingsPrefs.edit { it[KEY_LAST_SYNC_TIME] = timestamp }
    }

    suspend fun getLastSyncTime(): Long? {
        return context.userPreferencesDataStore.data.map { it[KEY_LAST_SYNC_TIME] }.first()
    }

    // Logout
    suspend fun logout() {
        userPrefs.edit { preferences ->
            preferences.remove(KEY_IS_LOGGED)
            preferences.remove(KEY_TOKEN)
            preferences.remove(KEY_VISITOR_ID)
            preferences.remove(KEY_SYSTEM_USER_ID)
            preferences.remove(KEY_FULL_NAME)
            preferences.remove(KEY_USERNAME)
            preferences.remove(KEY_USER_MOBILE)
            preferences.remove(KEY_ROLE_CODE)
            preferences.remove(KEY_ROLE_NAME)
            preferences.remove(KEY_IS_ACTIVE)
            preferences.remove(KEY_CENTER_ID)
            preferences.remove(KEY_CENTER_NAME)
            preferences.remove(KEY_CENTER_BRAND)
            preferences.remove(KEY_CENTER_PHONE)
            preferences.remove(KEY_CENTER_SUPPORT_PHONE)
            preferences.remove(KEY_CENTER_ADDRESS)
            preferences.remove(KEY_CENTER_WEBSITE)
            preferences.remove(KEY_CENTER_LATITUDE)
            preferences.remove(KEY_CENTER_LONGITUDE)
        }
    }

    suspend fun clearUserInfo() {
        userPrefs.edit { it.clear() }
    }

}
