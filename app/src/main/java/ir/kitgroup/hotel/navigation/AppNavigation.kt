package ir.kitgroup.hotel.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import ir.kitgroup.hotel.core.ui.theme.ThemeViewModel
import ir.kitgroup.hotel.core.ui.util.VisitType
import ir.kitgroup.hotel.feature.advertising.ui.item.AddAdvertisingItemScreen
import ir.kitgroup.hotel.feature.advertising.ui.visitor.AddAdvertisingVisitorAllocationScreen
import ir.kitgroup.hotel.feature.advertising.ui.item.AdvertisingItemsScreen
import ir.kitgroup.hotel.feature.advertising.ui.AdvertisingMenuScreen
import ir.kitgroup.hotel.feature.advertising.ui.collection.AddAdvertisingAllocationCollectionScreen
import ir.kitgroup.hotel.feature.advertising.ui.collection.AdvertisingAllocateCollectionScreen
import ir.kitgroup.hotel.feature.advertising.ui.visitor.AdvertisingAllocateVisitorScreen
import ir.kitgroup.hotel.feature.card.ui.CardRegistrationScreen
import ir.kitgroup.hotel.feature.card.ui.CardsScreen
import ir.kitgroup.hotel.feature.collaborative_collection.ui.CollaborativeCollectionsScreen
import ir.kitgroup.hotel.feature.collaborative_collection.ui.CollectionDetailScreen
import ir.kitgroup.hotel.feature.collaborative_collection.ui.RegisterCollectionScreen
import ir.kitgroup.hotel.feature.dashboard.ui.DashboardScreen
import ir.kitgroup.hotel.feature.home.navigation.BottomNavItem
import ir.kitgroup.hotel.feature.home.navigation.BottomNavigationBar
import ir.kitgroup.hotel.feature.login.ui.LoginScreen
import ir.kitgroup.hotel.feature.profile.ui.ProfileScreen
import ir.kitgroup.hotel.feature.profile.ui.SettingsScreen
import ir.kitgroup.hotel.feature.profile.ui.ThemeModeScreen
import ir.kitgroup.hotel.feature.visits.ui.RegisterVisitScreen
import ir.kitgroup.hotel.feature.visits.ui.VisitDetailScreen
import ir.kitgroup.hotel.feature.visits.ui.VisitsScreen

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
                    CollaborativeCollectionsScreen(navController)
                }

                composable(BottomNavItem.Visits.route) {
                    VisitsScreen(navController)
                }

                composable(
                    route = Screen.VisitDetail.route,
                    arguments = listOf(
                        navArgument("visitId") { type = NavType.IntType }
                    )
                ) { backStack ->
                    val visitId = backStack.arguments?.getInt("visitId") ?: 0
                    VisitDetailScreen(navController, visitId)
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
                        navController = navController,
                        visitType = visitType
                    )
                }

                composable(Screen.RegisterCollection.route) {
                    RegisterCollectionScreen(
                        navController = navController,
                        onCancel = { navController.popBackStack() },
                        onSave = { navController.popBackStack() },
                        onSelectLocation = {},
                        onAddImage = {}
                    )
                }

                composable(Screen.RegisterCard.route) {
                    CardRegistrationScreen(navController = navController)
                }

                composable(
                    route = Screen.CollectionDetail.route,
                    arguments = listOf(
                        navArgument("collectionId") { type = NavType.IntType }
                    )
                ) { backStack ->
                    val collectionId = backStack.arguments?.getInt("collectionId") ?: 0
                    CollectionDetailScreen(navController, collectionId)
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
                    AddAdvertisingVisitorAllocationScreen(
                        onBackClick = { navController.popBackStack() },
                        onSaveClick = { navController.popBackStack() }
                    )
                }
                composable(Screen.AdvertisingAllocationVisitor.route) {
                    AdvertisingAllocateVisitorScreen(
                        onBackClick = { navController.popBackStack() },
                        onNewAllocationClick = {
                            navController.navigate(Screen.AddAdvertisingAllocationVisitor.route)
                        },
                        onViewItemDetailsClick = { navController.popBackStack() }
                    )
                }

                composable(Screen.AddAdvertisingAllocationCollection.route) {
                    AddAdvertisingAllocationCollectionScreen (
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
                        onViewItemDetailsClick = { navController.popBackStack() }
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
                    CardsScreen(navController)
                }

                composable(BottomNavItem.Profile.route) {
                    ProfileScreen(navController)
                }

                composable("settings") {
                    SettingsScreen(
                        viewModel = themeViewModel,
                        navController = navController
                    )
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
}
