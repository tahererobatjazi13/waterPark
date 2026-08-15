package ir.kitgroup.partnerManagement.feature.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.kitgroup.partnerManagement.core.ui.util.UserRole
import ir.kitgroup.partnerManagement.core.ui.util.datastore.MainPreferences
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val mainPreferences: MainPreferences
) : ViewModel() {

    fun login(
        username: String,
        role: UserRole,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            mainPreferences.saveLoginInfo(
                username = username,
                role = role.name
            )
            onSuccess()
        }
    }
}
