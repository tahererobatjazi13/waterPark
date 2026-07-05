package ir.kitgroup.hotel.core.ui.theme

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.kitgroup.hotel.core.ui.util.ThemeMode
import ir.kitgroup.hotel.core.ui.util.datastore.ThemePreferences
import ir.kitgroup.hotel.core.ui.util.datastore.settingsDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode

    init {
        viewModelScope.launch {
            context.settingsDataStore.data.collect { pref ->
                val mode = ThemePreferences.fromString(
                    pref[ThemePreferences.THEME_MODE]
                )
                _themeMode.value = mode
            }
        }
    }

    fun setTheme(mode: ThemeMode) {
        viewModelScope.launch {
            context.settingsDataStore.edit {
                it[ThemePreferences.THEME_MODE] = mode.name
            }
        }
    }
}
