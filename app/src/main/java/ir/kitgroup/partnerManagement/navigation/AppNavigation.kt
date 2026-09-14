package ir.kitgroup.partnerManagement.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ir.kitgroup.partnerManagement.core.ui.SessionViewModel
import ir.kitgroup.partnerManagement.core.ui.theme.ThemeViewModel
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand.AdvertisingStandsListScreen
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.AdvertisingStandMenuScreen
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand.AddAdvertisingStandScreen
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand_assignment_organization.AddAdvertisingStandAssignmentOrganizationScreen
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand_assignment_organization.AdvertisingStandAssignmentOrganizationListScreen
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand_assignment_visitor.AddAdvertisingStandAssignmentVisitorScreen
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand_assignment_visitor.AdvertisingStandAssignmentVisitorListScreen
import ir.kitgroup.partnerManagement.feature.report.ui.visitor.VisitorListScreen
import ir.kitgroup.partnerManagement.feature.card.ui.AddCardScreen
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
import ir.kitgroup.partnerManagement.feature.report.ui.organization.CollectionPerformanceScreen
import ir.kitgroup.partnerManagement.feature.report.ui.organization.ReportContractScreen
import ir.kitgroup.partnerManagement.feature.report.ui.visitor.VisitorAnalysisScreen
import ir.kitgroup.partnerManagement.feature.report.ui.visitor.VisitorsPerformanceDashboardScreen
import ir.kitgroup.partnerManagement.feature.visits.ui.RegisterVisitScreen
import ir.kitgroup.partnerManagement.feature.visits.ui.VisitDetailScreen
import ir.kitgroup.partnerManagement.feature.visits.ui.VisitsScreen
import ir.kitgroup.partnerManagement.core.ui.SessionStatus
import ir.kitgroup.partnerManagement.core.ui.util.ThemeMode
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.detail.AdvertisingStandAssignmentVisitorDetailScreen
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.detail.demoAssignmentDetail
import ir.kitgroup.partnerManagement.feature.contract.ui.ContractsListScreen
import ir.kitgroup.partnerManagement.feature.login.ui.SplashScreen
import ir.kitgroup.partnerManagement.feature.contract.ui.AddContractScreen
import ir.kitgroup.partnerManagement.feature.contract.ui.ContractDetailScreen
import ir.kitgroup.partnerManagement.feature.contract.ui.demoContracts
import ir.kitgroup.partnerManagement.feature.dashboard.ui.MapScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.AddContractOfferScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.AddOfferTicketPlanLineDetailScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.AddOfferTicketPlanScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.OfferTicketPlanLineDetailScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.OfferTicketPlanLineListScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.OfferTicketPlanListScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.demoOfferLinePlans
import ir.kitgroup.partnerManagement.feature.offer.ui.demoOfferPlans
import ir.kitgroup.partnerManagement.feature.organization.ui.demoOrganizations

@Composable
fun AppNavigation(
    themeViewModel: ThemeViewModel = hiltViewModel()

) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val sessionViewModel: SessionViewModel = hiltViewModel()


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
                startDestination = "splash",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("splash") {

                    val sessionStatus by sessionViewModel.sessionStatus.collectAsState()

                    LaunchedEffect(sessionStatus) {
                        if (sessionStatus == SessionStatus.Checking) {
                            return@LaunchedEffect
                        }

                        kotlinx.coroutines.delay(2000)

                        when (sessionStatus) {

                            SessionStatus.Checking -> {
                                // هنوز DataStore در حال خواندن است
                            }

                            is SessionStatus.LoggedIn -> {

                                navController.navigate(
                                    BottomNavItem.Dashboard.route
                                ) {
                                    popUpTo("splash") {
                                        inclusive = true
                                    }

                                    launchSingleTop = true
                                }
                            }

                            SessionStatus.LoggedOut -> {

                                navController.navigate("login") {

                                    popUpTo("splash") {
                                        inclusive = true
                                    }

                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                    SplashScreen()
                }

                composable("login") {
                    LoginScreen(navController)
                }
                composable(BottomNavItem.Dashboard.route) {
                    DashboardScreen(navController)
                }

                composable(
                    route = "map",
                    arguments = listOf()
                ) { backStackEntry ->
                    MapScreen(
                        organizations = demoOrganizations,   // ← از همان منبع داده مشترک لیست
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable(BottomNavItem.Organizations.route) {
                    OrganizationsListScreen(
                        onOrganizationClick = { organizationId ->
                            navController.navigate(
                                Screen.OrganizationDetail.createRoute(organizationId)
                            )
                        }, onAddOrganizationClick = {
                            navController.navigate(Screen.AddOrganization.route)
                        }
                    )
                }
                composable(
                    route = "assign_visitor/{organizationId}",
                    arguments = listOf(
                        navArgument("organizationId") {
                            type = NavType.IntType
                        }
                    )
                ) { backStackEntry ->
                    val organizationId = backStackEntry.arguments?.getInt("organizationId") ?: 0
                    AssignVisitorScreen(
                        organizationId = organizationId,
                        onBackClick = { navController.popBackStack() },
                        onSaveClick = {
                            // عملیات ثبت
                        }
                    )
                }


                composable(BottomNavItem.Visits.route) {
                    VisitsScreen(
                        onVisitClick = { visitId ->
                            navController.navigate(Screen.VisitDetail.createRoute(visitId))
                        },
                        onAddVisitClick = {
                            navController.navigate(Screen.RegisterVisit.createRoute())
                        },
                        onEditVisitClick = { visitId ->
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




                composable(Screen.RegisterCard.route) {
                    AddCardScreen(onBackClick = { navController.popBackStack() })
                }

                composable(Screen.AddOrganization.route) {
                    AddOrganizationScreen(
                        organizationId = 0,
                        onBackClick = { navController.popBackStack() },
                        onCancel = { navController.popBackStack() },
                        onSaveClick = { navController.popBackStack() },
                        onSelectLocation = {},
                        onAddImage = {}
                    )
                }

                composable(
                    route = Screen.EditOrganization.route,
                    arguments = listOf(navArgument("organizationId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val organizationId = backStackEntry.arguments?.getInt("organizationId") ?: 0
                    AddOrganizationScreen(
                        organizationId = organizationId,
                        onBackClick = { navController.popBackStack() },
                        onCancel = { navController.popBackStack() },
                        onSaveClick = { navController.popBackStack() },
                        onSelectLocation = {},
                        onAddImage = {}
                    )
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
                        onEditClick = {
                            navController.navigate(
                                Screen.EditOrganization.createRoute(
                                    organizationId
                                )
                            )
                        },

                        onAssignVisitorClick = {
                            navController.navigate("assign_visitor/$id")
                        },

                        onEditVisitorClick = {
                        },
                        onDeleteVisitorClick = {
                        },
                        onAssignStandsClick = {
                            navController.navigate(Screen.AddAdvertisingStandAssignmentOrganization.route)
                        },
                        onAssignContractClick = {
                            navController.navigate(Screen.AddContract.route)
                        },
                        onViewItemDetailsClick = { allocation ->
                            navController.navigate(
                                Screen.AdvertisingStandAssignmentVisitorDetail.createRoute(
                                    assignmentId = allocation.id
                                )
                            )
                        }, onContractClick = { contract ->
                            navController.navigate(
                                Screen.ContractDetail.createRoute(contract.id)
                            )
                        }, onAddTicketOfferClick = {
                            navController.navigate(Screen.AddContractOffer.route)
                        },
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
                        onViewItemDetailsClick = { allocation ->
                            navController.navigate(
                                Screen.AdvertisingStandAssignmentVisitorDetail.createRoute(
                                    assignmentId = allocation.id
                                )
                            )
                        })
                }
                composable(
                    route = Screen.AdvertisingStandAssignmentVisitorDetail.route,
                    arguments = listOf(
                        navArgument("assignmentId") {
                            type = NavType.StringType
                        }
                    )
                ) { backStackEntry ->

                    val assignmentId = backStackEntry.arguments
                        ?.getString("assignmentId")
                        .orEmpty()

                    AdvertisingStandAssignmentVisitorDetailScreen(
                        allocation = demoAssignmentDetail,
                        onBackClick = {
                            navController.popBackStack()
                        }
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
                        onViewItemDetailsClick = { allocation ->
                            navController.navigate(
                                Screen.AdvertisingStandAssignmentVisitorDetail.createRoute(
                                    assignmentId = allocation.id
                                )
                            )
                        }
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
                            // لانچ کردن Intent تماس سیستمی با بازاریاب
                        }
                    )
                }

                composable(
                    route = Screen.ReportContract.route
                ) {
                    ReportContractScreen(
                        onBackClick = { navController.popBackStack() })
                }
                composable(Screen.ContractsList.route) {
                    val contracts = demoContracts()

                    ContractsListScreen(
                        onBackClick = { navController.popBackStack() },
                        onAddClick = {
                            navController.navigate(Screen.AddContract.route)
                        },
                        onContractClick = { contract ->
                            navController.navigate(
                                Screen.ContractDetail.createRoute(contract.id)
                            )
                        },
                        contracts = contracts
                    )
                }

                composable(
                    route = Screen.ContractDetail.route,
                    arguments = listOf(
                        navArgument("contractId") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val contractId = backStackEntry.arguments?.getString("contractId") ?: ""
                    val contract = demoContracts().firstOrNull { it.id == contractId }

                    if (contract != null) {
                        ContractDetailScreen(
                            contract = contract,
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }


                composable(Screen.AddContract.route) {
                    AddContractScreen(
                        onBackClick = { navController.popBackStack() },
                        onSaveClick = {
                        }
                    )
                }


                composable(Screen.AddContractOffer.route) {
                    AddContractOfferScreen(
                        onBackClick = { navController.popBackStack() },
                        onSaveClick = {
                        }
                    )
                }
                //  لیست کلی طرح‌ها
                composable(BottomNavItem.PlanOffer.route) {

                    val plans = demoOfferPlans()

                    OfferTicketPlanListScreen(
                        onAddClick = {
                            navController.navigate(Screen.AddOfferTicketPlan.route)
                        },
                        onPlanClick = { plan ->
                            navController.navigate(
                                Screen.OfferTicketPlanLineList.createRoute(plan.id)
                            )
                        },
                        plans = plans
                    )
                }

                // افزودن  طرح جدید
                composable(Screen.AddOfferTicketPlan.route) {
                    AddOfferTicketPlanScreen(
                        onBackClick = { navController.popBackStack() },
                        onSaveClick = {
                        }
                    )
                }

                // لیست جزییات و لاین‌های طرح انتخاب‌شده
                composable(
                    route = Screen.OfferTicketPlanLineList.route,
                    arguments = listOf(
                        navArgument("offerId") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val offerId = backStackEntry.arguments?.getString("offerId").orEmpty()
                    val selectedPlan = demoOfferPlans().firstOrNull { it.id == offerId }
                    val planLines = demoOfferLinePlans()

                    OfferTicketPlanLineListScreen(
                        headerPlan = selectedPlan,
                        plans = planLines,
                        onBackClick = { navController.popBackStack() },
                        onAddClick = {
                            navController.navigate(
                                Screen.AddOfferTicketPlanLineDetail.createRoute(
                                    selectedPlan?.planName ?: ""
                                )
                            )
                        },
                        onPlanClick = { planLine ->
                            navController.navigate(
                                Screen.OfferTicketPlanLineDetail.createRoute(planLine.id)
                            )
                        }
                    )
                }

                // ثبت جزییات طرح جدید با دریافت نام طرح
                composable(
                    route = Screen.AddOfferTicketPlanLineDetail.route,
                    arguments = listOf(
                        navArgument("planName") {
                            type = NavType.StringType
                            defaultValue = ""
                        }
                    )
                ) { backStackEntry ->
                    val planName = backStackEntry.arguments?.getString("planName").orEmpty()
                    AddOfferTicketPlanLineDetailScreen(
                        initialPlanHeader = planName,
                        onBackClick = { navController.popBackStack() },
                        onSaveClick = { navController.popBackStack() }
                    )
                }

                // صفحه جزئیات طرح
                composable(
                    route = Screen.OfferTicketPlanLineDetail.route,
                    arguments = listOf(
                        navArgument("lineId") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val lineId = backStackEntry.arguments?.getString("lineId").orEmpty()
                    val planLine = demoOfferLinePlans().firstOrNull { it.id == lineId }

                    if (planLine != null) {
                        OfferTicketPlanLineDetailScreen(
                            plan = planLine,
                            onBackClick = { navController.popBackStack() }
                        )
                    } else {
                        LaunchedEffect(Unit) {
                            navController.popBackStack()
                        }
                    }
                }

                composable(
                    route = Screen.CollectionPerformance.route
                ) {
                    CollectionPerformanceScreen(
                        onBackClick = { navController.popBackStack() })
                }

                composable(BottomNavItem.Profile.route) {
                    ProfileScreen(
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
                        },
                        viewModel = sessionViewModel
                    )
                }



                composable("settings") {
                    val themeMode by themeViewModel.themeMode.collectAsState()


                    SettingsScreen(
                        themeMode = themeMode ?: ThemeMode.SYSTEM,
                        onBackClick = { navController.popBackStack() },
                        onDisplayModeClick = { navController.navigate("theme_mode") }
                    )
                }


                composable("theme_mode") {
                    val themeMode by themeViewModel.themeMode.collectAsState()
                    ThemeModeScreen(
                        themeMode = themeMode ?: ThemeMode.SYSTEM,
                        onThemeSelected = { mode -> themeViewModel.setTheme(mode) },
                        onBackClick = { navController.popBackStack() }
                    )
                }

            }
        }
    }
}
