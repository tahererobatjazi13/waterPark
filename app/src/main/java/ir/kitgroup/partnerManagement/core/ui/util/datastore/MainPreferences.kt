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
        val KEY_PERSONNEL_ID = intPreferencesKey("key_personnelId")
        val KEY_IS_LOGGED = booleanPreferencesKey("key_is_login")
        val KEY_SALE_CENTER_ID = intPreferencesKey("key_sale_center_id")
        val KEY_DEFAULT_ANBAR_ID = intPreferencesKey("key_default_anbar_Id")
        val KEY_CONTROL_VISIT_SCHEDULE = booleanPreferencesKey("key_control_visit_schedule")
        val KEY_BASE_URL = stringPreferencesKey("base_url")


        val KEY_FILTER_CONDITION = stringPreferencesKey("filter_condition")
        val KEY_FROM_DATE = stringPreferencesKey("from_date")
        val KEY_TO_DATE = stringPreferencesKey("to_date")
        val KEY_CUSTOMER_ID = intPreferencesKey("customer_id")
        val KEY_CUSTOMER_NAME = stringPreferencesKey("customer_name")
        val KEY_DIRECTION_CODES = stringPreferencesKey("direction_codes")
        val KEY_DIRECTION_NAMES = stringPreferencesKey("direction_names")
        val KEY_DIRECTION_CLEARED = booleanPreferencesKey("direction_cleared")
    }

    val id: Flow<Int?> = context.dataStore.data
        .map { it[KEY_ID] }

    val firstName: Flow<String?> = context.dataStore.data
        .map { it[KEY_FIRST_NAME] }

    val lastName: Flow<String?> = context.dataStore.data
        .map { it[KEY_LAST_NAME] }

    val personnelId: Flow<Int?> = context.dataStore.data
        .map { it[KEY_PERSONNEL_ID] }

    val isLoggedIn: Flow<Boolean?> = context.dataStore.data
        .map { it[KEY_IS_LOGGED] }

    val saleCenterId: Flow<Int?> = context.dataStore.data
        .map { it[KEY_SALE_CENTER_ID] }

    val defaultAnbarId: Flow<Int?> = context.dataStore.data
        .map { it[KEY_DEFAULT_ANBAR_ID] }

    val controlVisitSchedule: Flow<Boolean?> = context.dataStore.data
        .map { it[KEY_CONTROL_VISIT_SCHEDULE] }

    val filterCondition: Flow<String?> = context.filterConditionDataStore.data
        .map { it[KEY_FILTER_CONDITION] }

    val fromDate: Flow<String?> = context.filterConditionDataStore.data
        .map { it[KEY_FROM_DATE] }

    val toDate: Flow<String?> = context.filterConditionDataStore.data
        .map { it[KEY_TO_DATE] }

    val customerName: Flow<String?> = context.filterConditionDataStore.data
        .map { it[KEY_CUSTOMER_NAME] }

    val customerId: Flow<Int?> = context.filterConditionDataStore.data
        .map { it[KEY_CUSTOMER_ID] }

    val directionCodes: Flow<String?> = context.filterConditionDataStore.data
        .map { it[KEY_DIRECTION_CODES] }

    val directionNames: Flow<String?> = context.filterConditionDataStore.data
        .map { it[KEY_DIRECTION_NAMES] }

    val baseUrlFlow: Flow<String?> = context.settingsDataStore.data
        .map { it[KEY_BASE_URL] }


    suspend fun saveUserInfo(
        id: Int,
        firstName: String, lastName: String, personnelId: Int
    ) {
        context.dataStore.edit { prefs ->
            prefs[KEY_ID] = id
            prefs[KEY_FIRST_NAME] = firstName
            prefs[KEY_LAST_NAME] = lastName
            prefs[KEY_PERSONNEL_ID] = personnelId
            prefs[KEY_IS_LOGGED] = true
        }
    }

    suspend fun clearUserInfo() {
        context.dataStore.edit { it.clear() }
    }

    suspend fun saveVisitorInfo(
        id: Int,
        saleCenterId: Int
    ) {
        context.dataStore.edit { prefs ->
            prefs[KEY_SALE_CENTER_ID] = saleCenterId
        }
    }

    suspend fun saveDefaultAnbarId(
        defaultAnbarId: Int
    ) {
        context.dataStore.edit { prefs ->
            prefs[KEY_DEFAULT_ANBAR_ID] = defaultAnbarId
        }
    }

    suspend fun saveControlVisitSchedule(controlVisitSchedule: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_CONTROL_VISIT_SCHEDULE] = controlVisitSchedule
        }
    }

    suspend fun saveFilterConditionInfo(
        filterCondition: String,
        fromDate: String, toDate: String, customerId: Int, customerName: String
    ) {
        context.filterConditionDataStore.edit { prefs ->
            prefs[KEY_FILTER_CONDITION] = filterCondition
            prefs[KEY_FROM_DATE] = fromDate
            prefs[KEY_TO_DATE] = toDate
            prefs[KEY_CUSTOMER_ID] = customerId
            prefs[KEY_CUSTOMER_NAME] = customerName
        }
    }

    suspend fun clearFilterConditionInfo() {
        context.filterConditionDataStore.edit { it.clear() }
    }

    suspend fun saveDirectionFilter(directionCodes: String, directionNames: String) {
        context.filterConditionDataStore.edit { prefs ->
            prefs[KEY_DIRECTION_CODES] = directionCodes
            prefs[KEY_DIRECTION_NAMES] = directionNames
        }
    }

    suspend fun clearDirectionFilter() {
        context.filterConditionDataStore.edit { prefs ->
            prefs.remove(KEY_DIRECTION_CODES)
            prefs.remove(KEY_DIRECTION_NAMES)
        }
    }

    suspend fun setDirectionCleared(value: Boolean) {
        context.filterConditionDataStore.edit {
            it[KEY_DIRECTION_CLEARED] = value
        }
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
