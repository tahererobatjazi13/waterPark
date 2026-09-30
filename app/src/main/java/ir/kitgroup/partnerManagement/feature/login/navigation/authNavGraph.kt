package ir.kitgroup.partnerManagement.feature.login.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ir.kitgroup.partnerManagement.core.ui.SessionStatus
import ir.kitgroup.partnerManagement.core.ui.SessionViewModel
import ir.kitgroup.partnerManagement.feature.home.navigation.BottomNavItem
import ir.kitgroup.partnerManagement.feature.login.ui.LoginScreen
import ir.kitgroup.partnerManagement.feature.login.ui.SplashScreen
import ir.kitgroup.partnerManagement.navigation.Screen
import kotlinx.coroutines.delay

fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    sessionViewModel: SessionViewModel
) {
    composable(Screen.Splash.route) {

        val sessionStatus by
        sessionViewModel.sessionStatus.collectAsState()

        LaunchedEffect(sessionStatus) {

            when (val status = sessionStatus) {

                SessionStatus.Checking -> {
                    // هنوز وضعیت Session مشخص نشده
                }

                is SessionStatus.LoggedIn -> {

                    delay(2000)

                    navController.navigate(
                        BottomNavItem.Dashboard.route
                    ) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }

                SessionStatus.LoggedOut -> {

                    delay(2000)

                    navController.navigate(
                        Screen.Login.route
                    ) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            }
        }

        SplashScreen()
    }
    composable(Screen.Login.route) {
        LoginScreen(
            navController = navController
        )
    }
}