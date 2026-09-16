package ir.kitgroup.partnerManagement.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ir.kitgroup.partnerManagement.core.ui.SessionViewModel
import ir.kitgroup.partnerManagement.core.ui.theme.ThemeViewModel
import ir.kitgroup.partnerManagement.feature.dashboard.ui.DashboardScreen
import ir.kitgroup.partnerManagement.feature.home.navigation.BottomNavItem
import ir.kitgroup.partnerManagement.feature.home.navigation.BottomNavigationBar
import ir.kitgroup.partnerManagement.feature.advertising_stand.navigation.advertisingStandNavGraph
import ir.kitgroup.partnerManagement.feature.contract.navigation.contractNavGraph
import ir.kitgroup.partnerManagement.feature.dashboard.ui.MapScreen
import ir.kitgroup.partnerManagement.feature.login.navigation.authNavGraph
import ir.kitgroup.partnerManagement.feature.offer.navigation.offerNavGraph
import ir.kitgroup.partnerManagement.feature.organization.navigation.organizationNavGraph
import ir.kitgroup.partnerManagement.feature.visits.navigation.visitsNavGraph
import ir.kitgroup.partnerManagement.feature.organization.ui.demoOrganizations
import ir.kitgroup.partnerManagement.feature.profile.navigation.profileNavGraph
import ir.kitgroup.partnerManagement.feature.report.navigation.reportNavGraph

@Composable
fun AppNavigation(
    themeViewModel: ThemeViewModel = hiltViewModel(),
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val bottomBarRoutes = setOf(
        BottomNavItem.Dashboard.route,
        BottomNavItem.Organizations.route,
        BottomNavItem.Visits.route,
        BottomNavItem.PlanOffer.route,
        BottomNavItem.Profile.route
    )

    val showBottomBar = currentRoute in bottomBarRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { paddingValues ->
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            NavHost(
                navController = navController,
                startDestination = Screen.Splash.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                authNavGraph(
                    navController = navController,
                    sessionViewModel = sessionViewModel
                )
                composable(BottomNavItem.Dashboard.route) {
                    DashboardScreen(navController)
                }

                composable(Screen.Map.route) {
                    MapScreen(
                        organizations = demoOrganizations,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                organizationNavGraph(navController)

                visitsNavGraph(navController)

                offerNavGraph(navController)

                contractNavGraph(navController)

                advertisingStandNavGraph(navController)

                reportNavGraph(navController)

                profileNavGraph(
                    navController = navController,
                    sessionViewModel = sessionViewModel,
                    themeViewModel = themeViewModel
                )
            }
        }
    }
}
