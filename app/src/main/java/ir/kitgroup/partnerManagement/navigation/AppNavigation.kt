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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ir.kitgroup.partnerManagement.core.ui.theme.ThemeViewModel
import ir.kitgroup.partnerManagement.core.ui.util.VisitType
import ir.kitgroup.partnerManagement.feature.advertising.ui.item.AddAdvertisingItemScreen
import ir.kitgroup.partnerManagement.feature.advertising.ui.item.AdvertisingItemsScreen
import ir.kitgroup.partnerManagement.feature.advertising.ui.AdvertisingMenuScreen
import ir.kitgroup.partnerManagement.feature.advertising.ui.collection.AddAdvertisingAllocationCollectionScreen
import ir.kitgroup.partnerManagement.feature.advertising.ui.collection.AdvertisingAllocateCollectionScreen
import ir.kitgroup.partnerManagement.feature.advertising.ui.visitor.AddAdvertisingAllocationVisitorScreen
import ir.kitgroup.partnerManagement.feature.advertising.ui.visitor.AdvertisingAllocateVisitorScreen
import ir.kitgroup.partnerManagement.feature.card.ui.AddCardScreen
import ir.kitgroup.partnerManagement.feature.card.ui.CardsScreen
import ir.kitgroup.partnerManagement.feature.collaborative_collection.ui.AddCollectionScreen
import ir.kitgroup.partnerManagement.feature.collaborative_collection.ui.CollaborativeCollectionsScreen
import ir.kitgroup.partnerManagement.feature.collaborative_collection.ui.CollectionDetailScreen
import ir.kitgroup.partnerManagement.feature.dashboard.ui.DashboardScreen
import ir.kitgroup.partnerManagement.feature.home.navigation.BottomNavItem
import ir.kitgroup.partnerManagement.feature.home.navigation.BottomNavigationBar
import ir.kitgroup.partnerManagement.feature.login.ui.LoginScreen
import ir.kitgroup.partnerManagement.feature.profile.ui.ProfileScreen
import ir.kitgroup.partnerManagement.feature.profile.ui.SettingsScreen
import ir.kitgroup.partnerManagement.feature.profile.ui.ThemeModeScreen
import ir.kitgroup.partnerManagement.feature.visits.ui.RegisterVisitScreen
import ir.kitgroup.partnerManagement.feature.visits.ui.VisitDetailScreen
import ir.kitgroup.partnerManagement.feature.visits.ui.VisitsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val themeViewModel: ThemeViewModel = viewModel()

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

        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Dashboard.route,
                modifier = Modifier.padding(paddingValues)
            ) {

                composable("login") {
                    LoginScreen(navController)
                }

                composable(BottomNavItem.Dashboard.route) {
                    DashboardScreen(navController)
                }
                composable(BottomNavItem.Collections.route) {
                    CollaborativeCollectionsScreen(
                        onCollectionClick = { collectionId ->
                            navController.navigate(
                                Screen.CollectionDetail.createRoute(collectionId)
                            )
                        }
                    )
                }

                composable(BottomNavItem.Visits.route) {
                    VisitsScreen(
                        onVisitClick = { visitId ->
                            navController.navigate(Screen.VisitDetail.createRoute(visitId))
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
                    route = "register_visit/{visitType}",
                    arguments = listOf(
                        navArgument("visitType") { type = NavType.StringType }
                    )
                ) { backStack ->
                    val visitType = VisitType.fromRoute(
                        backStack.arguments?.getString("visitType")
                    )

                    RegisterVisitScreen(
                        onBackClick = { navController.popBackStack() },
                        visitType = visitType
                    )
                }

                composable(Screen.AddCollection.route) {
                    AddCollectionScreen(
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
                    route = Screen.CollectionDetail.route,
                    arguments = listOf(
                        navArgument("collectionId") { type = NavType.IntType }
                    )
                ) { backStack ->
                    val collectionId = backStack.arguments?.getInt("collectionId") ?: 0
                    CollectionDetailScreen(
                        collectionId = collectionId,
                        onBackClick = { navController.popBackStack() },
                        onEditClick = { /* عملیات ویرایش */ },
                        onDisableClick = { /* عملیات غیرفعال‌سازی */ }
                    )
                }

                composable(Screen.AdvertisingMenu.route) {
                    AdvertisingMenuScreen(
                        onBackClick = { navController.popBackStack() },
                        onManageItemsClick = {
                            navController.navigate(Screen.AdvertisingItems.route)
                        },
                        onAllocateVisitorClick = {
                            navController.navigate(Screen.AdvertisingAllocationVisitor.route)
                        },
                        onAllocateCollectionClick = {
                            navController.navigate(Screen.AdvertisingAllocationCollection.route)
                        }
                    )
                }

                composable(
                    route = Screen.AdvertisingItems.route
                ) {
                    AdvertisingItemsScreen(
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onAddItemClick = {
                            navController.navigate(
                                Screen.AdvertisingDetail.createRoute()
                            )
                        },
                        onEditItemClick = { item ->
                            navController.navigate(
                                Screen.AdvertisingDetail.createRoute(
                                    itemId = item.id
                                )
                            )
                        }
                    )
                }

                composable(Screen.AddAdvertisingAllocationVisitor.route) {
                    AddAdvertisingAllocationVisitorScreen(
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onSaveClick = {
                            navController.popBackStack()
                        }
                    )
                }



                composable(Screen.AdvertisingAllocationVisitor.route) {
                    AdvertisingAllocateVisitorScreen(
                        onBackClick = { navController.popBackStack() },
                        onNewAllocationClick = {
                            navController.navigate(Screen.AddAdvertisingAllocationVisitor.route)
                        },
                        onViewItemDetailsClick = { }
                    )
                }

                composable(Screen.AddAdvertisingAllocationCollection.route) {
                    AddAdvertisingAllocationCollectionScreen(
                        onBackClick = { navController.popBackStack() },
                        onSaveClick = { navController.popBackStack() }
                    )
                }


                composable(Screen.AdvertisingAllocationCollection.route) {
                    AdvertisingAllocateCollectionScreen(
                        onBackClick = { navController.popBackStack() },
                        onNewAllocationClick = {
                            navController.navigate(Screen.AddAdvertisingAllocationCollection.route)
                        },
                        onViewItemDetailsClick = {  }
                    )
                }


                composable(
                    route = Screen.AdvertisingDetail.route,
                    arguments = listOf(
                        navArgument(Screen.AdvertisingDetail.ITEM_ID_ARGUMENT) {
                            type = NavType.StringType
                        }
                    )
                ) { backStackEntry ->

                    val itemId = backStackEntry.arguments
                        ?.getString(Screen.AdvertisingDetail.ITEM_ID_ARGUMENT)
                        ?: Screen.AdvertisingDetail.NEW_ITEM_ID

                    AddAdvertisingItemScreen(
                        itemId = itemId,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onSaveClick = { savedItem ->
                            // TODO:
                            // اگر itemId == "new" بود، آیتم جدید ثبت شود.
                            // در غیر این صورت، آیتم موجود ویرایش شود.

                            navController.popBackStack()
                        }
                    )
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
                        onEditInfoClick = { /* ناوبری به ویرایش اطلاعات */ },
                        onChangePasswordClick = { /* ناوبری به تغییر رمز عبور */ },
                        onSettingsClick = { navController.navigate("settings") },
                        onLogoutConfirm = {
                            // منطق خروج از حساب کاربری مانند پاک کردن توکن‌ها و بازگشت به صفحه لاگین
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
