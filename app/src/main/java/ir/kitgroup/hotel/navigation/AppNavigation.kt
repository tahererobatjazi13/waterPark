package ir.kitgroup.hotel.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import ir.kitgroup.hotel.feature.collaborative_collection.ui.CollaborativeCollectionsScreen
import ir.kitgroup.hotel.feature.dashboard.ui.DashboardScreen
import ir.kitgroup.hotel.feature.home.navigation.BottomNavItem
import ir.kitgroup.hotel.feature.home.navigation.BottomNavigationBar
import ir.kitgroup.hotel.feature.home.ui.*
import ir.kitgroup.hotel.feature.login.ui.LoginScreen
import ir.kitgroup.hotel.feature.profile.ui.ProfileScreen
import ir.kitgroup.hotel.feature.profile.ui.SettingsScreen
import ir.kitgroup.hotel.feature.visits.ui.RegisterVisitScreen
import ir.kitgroup.hotel.feature.visits.ui.VisitDetailScreen
import ir.kitgroup.hotel.feature.visits.ui.VisitsScreen
import ir.kitgroup.hotel.core.ui.theme.ThemeViewModel
import ir.kitgroup.hotel.core.ui.util.VisitType
import ir.kitgroup.hotel.feature.profile.ui.ThemeModeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    val showBottomBar = currentRoute != "login"
    val themeViewModel: ThemeViewModel = viewModel()

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController)
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Dashboard.route,
            modifier = Modifier.padding(padding)
        ) {
            composable("login") {
                LoginScreen(navController)
            }
            composable(BottomNavItem.Dashboard.route) {
                DashboardScreen(navController)
            }
            composable(BottomNavItem.Collections.route) {
                CollaborativeCollectionsScreen(navController)
            }
            composable(BottomNavItem.Visits.route) {
                VisitsScreen(navController)
            }
            composable(
                route = Screen.VisitDetail.route,
                arguments = listOf(navArgument("visitId") { type = NavType.IntType })
            ) { backStackEntry ->
                val visitId = backStackEntry.arguments?.getInt("visitId") ?: 0
                VisitDetailScreen(navController, visitId)
            }

            composable(
                route = "register_visit/{visitType}",
                arguments = listOf(navArgument("visitType") { type = NavType.StringType })
            ) { backStackEntry ->

                val visitType = VisitType.fromRoute(
                    backStackEntry.arguments?.getString("visitType")
                )

                RegisterVisitScreen(
                    navController = navController,
                    visitType = visitType
                )
            }

            composable(BottomNavItem.Cards.route) {
                CardsScreen(navController)
            }
            composable(BottomNavItem.Profile.route) {
                ProfileScreen(navController)
            }
            composable("settings") {
                SettingsScreen(viewModel = themeViewModel, navController)
            }

            composable("theme_mode") {
                ThemeModeScreen(
                    viewModel = themeViewModel,
                    navController = navController
                )
            }
        }
    }
}
