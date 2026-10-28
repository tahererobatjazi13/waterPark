package ir.kitgroup.partnerManagement.feature.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.CardMenuItem
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.ThemeMode

@Composable
fun SettingsScreen(
    themeMode: ThemeMode,
    onDisplayModeClick: () -> Unit,
  /*  onClearDataConfirm: () -> Unit,*/
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    var showClearDataDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(appColors.screenBackground)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = appColors.screenBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // آیتم حالت نمایش (تم)
                CardMenuItem(
                    icon = R.drawable.ic_pallete,
                    title = R.string.label_display_mode,
                    subtitle = when (themeMode) {
                        ThemeMode.LIGHT -> stringResource(R.string.label_light_theme)
                        ThemeMode.DARK -> stringResource(R.string.label_dark_theme)
                        ThemeMode.SYSTEM -> stringResource(R.string.label_system_theme)
                    },
                    containerColor = appColors.cardBackground,
                    contentColor = appColors.textPrimary,
                    onClick = onDisplayModeClick
                )

              /*  // آیتم پاک‌کردن داده‌ها
                CardMenuItem(
                    icon = android.R.drawable.ic_menu_delete, // آیکون سطل زباله پروژه (مثلاً R.drawable.ic_delete)
                    title = R.string.label_clear_cache_title, // یا متن مستقیم: "پاک کردن داده‌ها"
                    subtitle = "حذف تمامی اطلاعات ذخیره‌شده آفلاین",
                    containerColor = appColors.cardBackground,
                    contentColor = MaterialTheme.colorScheme.error,
                    onClick = { showClearDataDialog = true }
                )*/
            }
        }
    }
/*
    // دیالوگ هشدار تأیید حذف داده‌ها
    if (showClearDataDialog) {
        AlertDialog(
            onDismissRequest = { showClearDataDialog = false },
            title = {
                Text(
                    text = "هشدار حذف اطلاعات",
                    style = MaterialTheme.typography.titleMedium,
                    color = appColors.textPrimary
                )
            },
            text = {
                Text(
                    text = "با انجام این عملیات، تمامی اطلاعات ذخیره‌شده محلی، تغییرات سینک‌نشده و حافظه موقت برنامه حذف خواهند شد. آیا از انجام این کار اطمینان دارید؟",
                    style = MaterialTheme.typography.bodyMedium,
                    color = appColors.textSecondary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showClearDataDialog = false
                        onClearDataConfirm()
                    }
                ) {
                    Text(
                        text = "تأیید و حذف",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDataDialog = false }) {
                    Text(
                        text = "انصراف",
                        color = appColors.textPrimary,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            },
            containerColor = appColors.cardBackground,
            shape = RoundedCornerShape(16.dp)
        )
    }*/
}
/*


class SettingsRepository @Inject constructor(
    private val database: AppDatabase,
    private val mainPreferences: MainPreferences
) {
    suspend fun clearAllData() = withContext(Dispatchers.IO) {
        // ۱. پاکسازی کامل تمام جداول روم
        database.clearAllTables()

        // ۲. ریست کردن فلگ‌های سینک در Preferences (اختیاری اما توصیه شده)
        // mainPreferences.clearSyncStatus()
    }
}
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val database: AppDatabase, // یا SettingsRepository
    // ...
) : ViewModel() {

    fun clearAllData(onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            database.clearAllTables()
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        }
    }
}*/

