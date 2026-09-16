package ir.kitgroup.partnerManagement.feature.profile.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ir.kitgroup.partnerManagement.core.ui.SessionViewModel
import ir.kitgroup.partnerManagement.core.ui.theme.ThemeViewModel
import ir.kitgroup.partnerManagement.core.ui.util.ThemeMode
import ir.kitgroup.partnerManagement.feature.home.navigation.BottomNavItem
import ir.kitgroup.partnerManagement.feature.profile.ui.ProfileScreen
import ir.kitgroup.partnerManagement.feature.profile.ui.SettingsScreen
import ir.kitgroup.partnerManagement.feature.profile.ui.ThemeModeScreen
import ir.kitgroup.partnerManagement.navigation.Screen

fun NavGraphBuilder.profileNavGraph(
    navController: NavController,
    sessionViewModel: SessionViewModel,
    themeViewModel: ThemeViewModel
) {
    // صفحه اصلی پروفایل
    composable(BottomNavItem.Profile.route) {
        ProfileScreen(
            onSettingsClick = {
                navController.navigate(Screen.Settings.route)
            },
            onLogoutSuccess = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(0) { inclusive = true }
                    launchSingleTop = true
                }
            },
            viewModel = sessionViewModel
        )
    }

    // صفحه تنظیمات
    composable(Screen.Settings.route) {
        val themeMode by themeViewModel.themeMode.collectAsState()

        SettingsScreen(
            themeMode = themeMode ?: ThemeMode.SYSTEM,
            onBackClick = { navController.popBackStack() },
            onDisplayModeClick = { navController.navigate(Screen.ThemeMode.route) }
        )
    }

    // صفحه انتخاب تم
    composable(Screen.ThemeMode.route) {
        val themeMode by themeViewModel.themeMode.collectAsState()

        ThemeModeScreen(
            themeMode = themeMode ?: ThemeMode.SYSTEM,
            onThemeSelected = { mode -> themeViewModel.setTheme(mode) },
            onBackClick = { navController.popBackStack() }
        )
    }
}
