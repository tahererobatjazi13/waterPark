package ir.kitgroup.partnerManagement.feature.profile.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.SessionViewModel
import ir.kitgroup.partnerManagement.core.ui.theme.ThemeViewModel
import ir.kitgroup.partnerManagement.core.ui.util.ThemeMode
import ir.kitgroup.partnerManagement.feature.home.navigation.BottomNavItem
import ir.kitgroup.partnerManagement.feature.profile.ui.CenterInfoScreen
import ir.kitgroup.partnerManagement.feature.profile.ui.ProfileScreen
import ir.kitgroup.partnerManagement.feature.profile.ui.SettingsScreen
import ir.kitgroup.partnerManagement.feature.profile.ui.ThemeModeScreen
import ir.kitgroup.partnerManagement.navigation.Screen
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader

fun NavGraphBuilder.profileNavGraph(
    navController: NavController,
    sessionViewModel: SessionViewModel,
    themeViewModel: ThemeViewModel
) {

    // ---------------------------------------------------------
    // Profile - Main Bottom Navigation
    // ---------------------------------------------------------
    composable(
        route = BottomNavItem.Profile.route,
        enterTransition = { fadeIn(tween(250)) },
        exitTransition = { fadeOut(tween(200)) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            CustomHeader(
                title = stringResource(R.string.nav_profile),
                showBackButton = false
            )

            ProfileScreen(
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                },
                onCenterClick = {
                    navController.navigate(Screen.CenterInfo.route)
                },
                onLogoutSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                viewModel = sessionViewModel,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    // --------------------------------------------------------
    // Settings
    // ---------------------------------------------------------
    composable(
        route = Screen.Settings.route,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        },
        popEnterTransition = {
            EnterTransition.None
        },
        popExitTransition = {
            ExitTransition.None
        }
    ) {
        val themeMode by themeViewModel.themeMode.collectAsState()

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomHeader(
                title = stringResource(
                    R.string.label_setting_theme
                ),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                SettingsScreen(
                    themeMode = themeMode ?: ThemeMode.SYSTEM,
                    onDisplayModeClick = {
                        navController.navigate(Screen.ThemeMode.route)
                    }
                )
            }
        }

    }

    // ---------------------------------------------------------
    // Theme Mode
    // ---------------------------------------------------------
    composable(
        route = Screen.ThemeMode.route,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        },
        popEnterTransition = {
            EnterTransition.None
        },
        popExitTransition = {
            ExitTransition.None
        }
    ) {
        val themeMode by themeViewModel.themeMode.collectAsState()

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomHeader(
                title = stringResource(
                    R.string.label_display_mode
                ),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                ThemeModeScreen(
                    themeMode = themeMode ?: ThemeMode.SYSTEM,
                    onThemeSelected = { mode ->
                        themeViewModel.setTheme(mode)
                    }
                )
            }
        }

    }

    // ---------------------------------------------------------
    // Center Info
    // ---------------------------------------------------------
    composable(
        route = Screen.CenterInfo.route,
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        },
        popEnterTransition = {
            EnterTransition.None
        },
        popExitTransition = {
            ExitTransition.None
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomHeader(
                title = stringResource(
                    R.string.label_center_information
                ),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                CenterInfoScreen(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}