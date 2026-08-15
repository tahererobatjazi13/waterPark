package ir.kitgroup.partnerManagement.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ir.kitgroup.partnerManagement.core.ui.SessionViewModel
import ir.kitgroup.partnerManagement.core.ui.theme.ThemeViewModel
import ir.kitgroup.partnerManagement.core.ui.util.VisitType
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand.AdvertisingStandsListScreen
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.AdvertisingStandMenuScreen
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand.AddAdvertisingStandScreen
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand_assignment_organization.AddAdvertisingStandAssignmentOrganizationScreen
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand_assignment_organization.AdvertisingStandAssignmentOrganizationListScreen
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand_assignment_visitor.AddAdvertisingStandAssignmentVisitorScreen
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand_assignment_visitor.AdvertisingStandAssignmentVisitorListScreen
import ir.kitgroup.partnerManagement.feature.report.ui.visitor.VisitorListScreen
import ir.kitgroup.partnerManagement.feature.card.ui.AddCardScreen
import ir.kitgroup.partnerManagement.feature.card.ui.CardsScreen
import ir.kitgroup.partnerManagement.feature.dashboard.ui.DashboardScreen
import ir.kitgroup.partnerManagement.feature.home.navigation.BottomNavItem
import ir.kitgroup.partnerManagement.feature.home.navigation.BottomNavigationBar
import ir.kitgroup.partnerManagement.feature.login.ui.LoginScreen
import ir.kitgroup.partnerManagement.feature.organization.ui.AddOrganizationScreen
import ir.kitgroup.partnerManagement.feature.organization.ui.AssignVisitorScreen
import ir.kitgroup.partnerManagement.feature.organization.ui.OrganizationDetailScreen
import ir.kitgroup.partnerManagement.feature.organization.ui.OrganizationsListScreen
import ir.kitgroup.partnerManagement.feature.profile.ui.ProfileScreen
import ir.kitgroup.partnerManagement.feature.profile.ui.SettingsScreen
import ir.kitgroup.partnerManagement.feature.profile.ui.ThemeModeScreen
import ir.kitgroup.partnerManagement.feature.report.ui.ReportMenuScreen
import ir.kitgroup.partnerManagement.feature.report.ui.collection.CollectionPerformanceScreen
import ir.kitgroup.partnerManagement.feature.report.ui.collection.ReportContractScreen
import ir.kitgroup.partnerManagement.feature.report.ui.visitor.VisitorAnalysisScreen
import ir.kitgroup.partnerManagement.feature.report.ui.visitor.VisitorsPerformanceDashboardScreen
import ir.kitgroup.partnerManagement.feature.visits.ui.RegisterVisitScreen
import ir.kitgroup.partnerManagement.feature.visits.ui.VisitDetailScreen
import ir.kitgroup.partnerManagement.feature.visits.ui.VisitsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val themeViewModel: ThemeViewModel = viewModel()
    val sessionViewModel: SessionViewModel = hiltViewModel()


    val bottomBarRoutes = setOf(
        BottomNavItem.Dashboard.route,
        BottomNavItem.Collections.route,
        BottomNavItem.Visits.route,
        BottomNavItem.Cards.route,
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
        val isLoggedIn by sessionViewModel.isLoggedIn.collectAsState()

        val startDestination = if (isLoggedIn) {
            BottomNavItem.Dashboard.route
        } else {
            "login"
        }
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            NavHost(
                navController = navController,
                startDestination = startDestination,
                modifier = Modifier.padding(paddingValues)
            ) {

                composable("login") {
                    LoginScreen(navController)
                }

                composable(BottomNavItem.Dashboard.route) {
                    DashboardScreen(navController)
                }
                composable(BottomNavItem.Collections.route) {
                    OrganizationsListScreen(
                        onOrganizationClick = { organizationId ->
                            navController.navigate(
                                Screen.OrganizationDetail.createRoute(organizationId)
                            )
                        }, onAssignVisitorClick = {
                            navController.navigate(Screen.AssignVisitor.route)
                        }
                    )
                }


                composable(Screen.AssignVisitor.route) {
                    AssignVisitorScreen(
                        onBackClick = { navController.popBackStack() },
                        onSaveClick = {
                        }
                    )
                }

                composable(BottomNavItem.Visits.route) {
                    VisitsScreen(
                        onVisitClick = { visitId ->
                            navController.navigate(Screen.VisitDetail.createRoute(visitId))
                        }, onEditVisitClick = { visitId ->
                            navController.navigate(Screen.RegisterVisit.createRoute(visitId))
                        }
                    )
                }

                composable(
                    route = Screen.VisitDetail.route,
                    arguments = listOf(
                        navArgument("visitId") { type = NavType.IntType }
                    )
                ) { backStack ->
                    val visitId = backStack.arguments?.getInt("visitId") ?: 0
                    VisitDetailScreen(
                        visitId = visitId,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable(
                    route = Screen.RegisterVisit.route,
                    arguments = listOf(
                        navArgument("visitId") {
                            type = NavType.IntType
                            defaultValue = -1
                        }
                    )
                ) { backStackEntry ->

                    val visitId = backStackEntry.arguments?.getInt("visitId") ?: -1

                    RegisterVisitScreen(
                        visitId = visitId,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onSubmitClick = {
                            navController.popBackStack()
                        }
                    )
                }


                composable(Screen.AddCollection.route) {
                    AddOrganizationScreen(
                        onBackClick = { navController.popBackStack() },
                        onCancel = { navController.popBackStack() },
                        onSaveClick = { navController.popBackStack() },
                        onSelectLocation = {},
                        onAddImage = {}
                    )
                }

                composable(Screen.RegisterCard.route) {
                    AddCardScreen(onBackClick = { navController.popBackStack() })
                }

                composable(
                    route = Screen.OrganizationDetail.route,
                    arguments = listOf(
                        navArgument("organizationId") { type = NavType.IntType }
                    )
                ) { backStack ->
                    val organizationId = backStack.arguments?.getInt("organizationId") ?: 0
                    OrganizationDetailScreen(
                        organizationId = organizationId,
                        onBackClick = { navController.popBackStack() },
                        onEditClick = { /* عملیات ویرایش */ },
                        onDisableClick = { /* عملیات غیرفعال‌سازی */ }
                    )
                }

                // Advertising Stand
                composable(Screen.AdvertisingStandMenu.route) {
                    AdvertisingStandMenuScreen(
                        onBackClick = { navController.popBackStack() },
                        onManageItemsClick = {
                            navController.navigate(Screen.AdvertisingStandsList.route)
                        },
                        onStandAssignmentVisitorClick = {
                            navController.navigate(Screen.AdvertisingStandAssignmentVisitorList.route)
                        },
                        onStandAssignmentOrganizationClick = {
                            navController.navigate(Screen.AdvertisingStandAssignmentOrganizationList.route)
                        }
                    )
                }

                composable(
                    route = Screen.AdvertisingStandsList.route
                ) {
                    AdvertisingStandsListScreen(
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onAddItemClick = {
                            navController.navigate(
                                Screen.AdvertisingStandDetail.createRoute(
                                    Screen.AdvertisingStandDetail.NEW_ITEM_ID
                                )
                            )
                        },
                        onEditItemClick = { item ->
                            navController.navigate(
                                Screen.AdvertisingStandDetail.createRoute(
                                    itemId = item.id
                                )
                            )
                        },
                        onDeleteItemClick = { item ->
                            //    standToDelete = item
                        }
                    )
                }

                composable(
                    route = Screen.AdvertisingStandDetail.route,
                    arguments = listOf(
                        navArgument(Screen.AdvertisingStandDetail.ITEM_ID_ARGUMENT) {
                            type = NavType.StringType
                            nullable = false
                        }
                    )
                ) { backStackEntry ->

                    val itemId = backStackEntry.arguments?.getString(
                        Screen.AdvertisingStandDetail.ITEM_ID_ARGUMENT
                    ) ?: Screen.AdvertisingStandDetail.NEW_ITEM_ID

                    AddAdvertisingStandScreen(
                        itemId = itemId,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onSaveClick = { savedItem ->
                            /*
                             * اینجا باید در ViewModel:
                             * - اگر itemId == "new" بود -> ثبت آیتم جدید
                             * - در غیر این صورت -> ویرایش آیتم با همان id
                             */

                            navController.popBackStack()
                        }
                    )
                }

                composable(Screen.AdvertisingStandAssignmentVisitorList.route) {
                    AdvertisingStandAssignmentVisitorListScreen(
                        onBackClick = { navController.popBackStack() },
                        onNewAssignmentVisitorClick = {
                            navController.navigate(Screen.AddAdvertisingStandAssignmentVisitor.route)
                        },
                        onViewItemDetailsClick = { }
                    )
                }

                composable(Screen.AddAdvertisingStandAssignmentVisitor.route) {
                    AddAdvertisingStandAssignmentVisitorScreen(
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onSaveClick = {
                            navController.popBackStack()
                        }
                    )
                }

                composable(Screen.AdvertisingStandAssignmentOrganizationList.route) {
                    AdvertisingStandAssignmentOrganizationListScreen(
                        onBackClick = { navController.popBackStack() },
                        onNewAssignmentOrganizationClick = {
                            navController.navigate(Screen.AddAdvertisingStandAssignmentOrganization.route)
                        },
                        onViewItemDetailsClick = { }
                    )
                }

                composable(Screen.AddAdvertisingStandAssignmentOrganization.route) {
                    AddAdvertisingStandAssignmentOrganizationScreen(
                        onBackClick = { navController.popBackStack() },
                        onSaveClick = { navController.popBackStack() }
                    )
                }

                // Report
                composable(Screen.ReportMenu.route) {
                    ReportMenuScreen(
                        onBackClick = { navController.popBackStack() },
                        onVisitorClick = {
                            navController.navigate(Screen.VisitorList.route)
                        },
                        onReportContractClick = {
                            navController.navigate(Screen.ReportContract.route)
                        },
                        onCollectionPerformanceClick = {
                            navController.navigate(Screen.CollectionPerformance.route)
                        }
                    )
                }
                composable(
                    route = Screen.VisitorList.route
                ) {
                    VisitorListScreen(
                        onBackClick = { navController.popBackStack() },
                        onReportItemClick = {
                            navController.navigate(Screen.VisitorsPerformance.route)
                        },
                        onViewItemDetailsClick = { visitor ->
                            navController.navigate("visitor_analysis/${visitor.id}")
                        })
                }


                composable(route = Screen.VisitorsPerformance.route) {
                    VisitorsPerformanceDashboardScreen(
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable(
                    route = "visitor_analysis/{visitorId}",
                    arguments = listOf(navArgument("visitorId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val visitorId = backStackEntry.arguments?.getString("visitorId") ?: ""
                    VisitorAnalysisScreen(
                        visitorId = visitorId,
                        onBackClick = { navController.popBackStack() },
                        onCallClick = { phone ->
                            // لانچ کردن Intent تماس سیستمی با ویزیتور
                        }
                    )
                }

                composable(
                    route = Screen.ReportContract.route
                ) {
                    ReportContractScreen(
                        onBackClick = { navController.popBackStack() })
                }

                composable(
                    route = Screen.CollectionPerformance.route
                ) {
                    CollectionPerformanceScreen(
                        onBackClick = { navController.popBackStack() })
                }
                composable(BottomNavItem.Cards.route) {
                    CardsScreen(
                        onRegisterCardClick = {
                            navController.navigate(Screen.RegisterCard.route)
                        }
                    )
                }


                composable(BottomNavItem.Profile.route) {
                    ProfileScreen(
                        onEditInfoClick = { },
                        onChangePasswordClick = { },
                        onSettingsClick = {
                            navController.navigate("settings")
                        },
                        onLogoutSuccess = {
                            navController.navigate("login") {
                                popUpTo(0) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        }
                    )
                }


                composable("settings") {
                    val themeMode by themeViewModel.themeMode.collectAsState()

                    SettingsScreen(
                        themeMode = themeMode,
                        onBackClick = { navController.popBackStack() },
                        onDisplayModeClick = { navController.navigate("theme_mode") }
                    )
                }


                composable("theme_mode") {
                    val themeMode by themeViewModel.themeMode.collectAsState()
                    ThemeModeScreen(
                        themeMode = themeMode,
                        onThemeSelected = { mode -> themeViewModel.setTheme(mode) },
                        onBackClick = { navController.popBackStack() }
                    )
                }

            }
        }
    }
}
