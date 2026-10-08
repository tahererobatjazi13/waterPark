package ir.kitgroup.partnerManagement.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.SessionViewModel
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.theme.ThemeViewModel
import ir.kitgroup.partnerManagement.feature.contract.navigation.contractNavGraph
import ir.kitgroup.partnerManagement.feature.dashboard.ui.DashboardScreen
import ir.kitgroup.partnerManagement.feature.dashboard.ui.MapRoute
import ir.kitgroup.partnerManagement.feature.home.navigation.BottomNavItem
import ir.kitgroup.partnerManagement.feature.home.navigation.BottomNavigationBar
import ir.kitgroup.partnerManagement.feature.login.navigation.authNavGraph
import ir.kitgroup.partnerManagement.feature.meeting.navigation.meetingNavGraph
import ir.kitgroup.partnerManagement.feature.offer.navigation.offerNavGraph
import ir.kitgroup.partnerManagement.feature.organization.navigation.organizationNavGraph
import ir.kitgroup.partnerManagement.feature.profile.navigation.profileNavGraph
import ir.kitgroup.partnerManagement.feature.report.navigation.reportNavGraph
import ir.kitgroup.partnerManagement.feature.stand.navigation.advertisingStandNavGraph

@Composable
fun AppNavigation(
    themeViewModel: ThemeViewModel = hiltViewModel(),
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val appColors = LocalPartnerManagementColors.current

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val userFullName by sessionViewModel.fullName.collectAsState()

    val topBarTitle: String? = when (currentRoute) {
       BottomNavItem.Dashboard.route -> {
            if (!userFullName.isNullOrBlank()) {
                stringResource(R.string.label_welcome_user, userFullName!!.trim())
            } else {
                stringResource(R.string.nav_dashboard)
            }
        }
        BottomNavItem.Dashboard.route -> stringResource(R.string.nav_dashboard)
        BottomNavItem.Organizations.route -> stringResource(R.string.nav_organizations)
        BottomNavItem.Meetings.route -> stringResource(R.string.nav_meetings)
        BottomNavItem.Offers.route -> stringResource(R.string.nav_plan_offer)
        BottomNavItem.Profile.route -> stringResource(R.string.nav_profile)
        else -> null
    }

    val mainTabRoutes = remember {
        setOf(
            BottomNavItem.Dashboard.route,
            BottomNavItem.Organizations.route,
            BottomNavItem.Meetings.route,
            BottomNavItem.Offers.route,
            BottomNavItem.Profile.route
        )
    }

    val showBottomBar = currentRoute in mainTabRoutes

    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Rtl
    ) {
        Scaffold(
            containerColor = appColors.screenBackground,
            topBar = {
                if (topBarTitle != null) {
                    CustomHeader(
                        title = topBarTitle,
                        showBackButton = false
                    )
                }
            },
            bottomBar = {
                AnimatedVisibility(
                    visible = showBottomBar,
                    enter = fadeIn(animationSpec = tween(durationMillis = 150)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 100))
                ) {
                    BottomNavigationBar(
                        navController = navController
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(appColors.screenBackground)
                    .padding(bottom = paddingValues.calculateBottomPadding())
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Splash.route,
                    modifier = Modifier.fillMaxSize()
                ) {
                    authNavGraph(
                        navController = navController,
                        sessionViewModel = sessionViewModel
                    )

                    composable(BottomNavItem.Dashboard.route) {
                        DashboardScreen(navController)
                    }

                    composable(Screen.Map.route) {
                        MapRoute(
                            onBackClick = navController::popBackStack
                        )
                    }

                    organizationNavGraph(navController)
                    meetingNavGraph(navController)
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
}
