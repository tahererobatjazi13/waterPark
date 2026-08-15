package ir.kitgroup.partnerManagement.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.ui.util.UserRole
import ir.kitgroup.partnerManagement.core.ui.util.datastore.MainPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val mainPreferences: MainPreferences
) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean> = mainPreferences.isLoggedIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )
    val userName = mainPreferences.username

    val userRole: StateFlow<String?> = mainPreferences.userRole
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val isSupervisorFlow: Flow<Boolean> = mainPreferences.userRole.map { role ->
        role == UserRole.SUPERVISOR.name
    }

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            mainPreferences.clearUserInfo()
            onComplete()
        }
    }
}
