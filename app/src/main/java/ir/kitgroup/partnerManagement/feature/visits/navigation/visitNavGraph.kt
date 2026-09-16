package ir.kitgroup.partnerManagement.feature.visits.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.navArgument
import ir.kitgroup.partnerManagement.feature.home.navigation.BottomNavItem
import ir.kitgroup.partnerManagement.feature.visits.ui.RegisterVisitScreen
import ir.kitgroup.partnerManagement.feature.visits.ui.VisitDetailScreen
import ir.kitgroup.partnerManagement.feature.visits.ui.VisitsScreen
import ir.kitgroup.partnerManagement.navigation.Screen

fun NavGraphBuilder.visitsNavGraph(navController: NavController) {

    navigation(
        startDestination = BottomNavItem.Visits.route,
        route = "Visit_graph"
    ) {
        // لیست بازدید ها
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


        // صفحه جزئیات بازدید
        composable(
            route = Screen.VisitDetail.route,
            arguments = listOf(
                navArgument("visitId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val visitId = backStackEntry.arguments?.getInt("visitId") ?: 0
            VisitDetailScreen(
                visitId = visitId,
                onBackClick = { navController.popBackStack() }
            )
        }
        // صفحه ثبت / ویرایش بازدید
        composable(
            route = Screen.RegisterVisit.route,
            arguments = listOf(
                navArgument("visitId") {
                    type = NavType.IntType
                    defaultValue = -1
                },
                navArgument("organizationId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val visitId = backStackEntry.arguments?.getInt("visitId")?.takeIf { it != -1 }
            val organizationId =
                backStackEntry.arguments?.getInt("organizationId")?.takeIf { it != -1 }

            RegisterVisitScreen(
                visitId = visitId,
                preselectedOrganizationId = organizationId,
                onBackClick = { navController.popBackStack() },
                onSubmitClick = { navController.popBackStack() }
            )
        }
    }
}