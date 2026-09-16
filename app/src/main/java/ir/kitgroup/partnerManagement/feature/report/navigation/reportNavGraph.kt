package ir.kitgroup.partnerManagement.feature.report.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ir.kitgroup.partnerManagement.feature.report.ui.ReportMenuScreen
import ir.kitgroup.partnerManagement.feature.report.ui.organization.OrganizationPerformanceScreen
import ir.kitgroup.partnerManagement.feature.report.ui.organization.ReportContractScreen
import ir.kitgroup.partnerManagement.feature.report.ui.visitor.VisitorAnalysisScreen
import ir.kitgroup.partnerManagement.feature.report.ui.visitor.VisitorListScreen
import ir.kitgroup.partnerManagement.feature.report.ui.visitor.VisitorsPerformanceDashboardScreen
import ir.kitgroup.partnerManagement.navigation.Screen


fun NavGraphBuilder.reportNavGraph(navController: NavController) {

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
                navController.navigate(Screen.OrganizationPerformance.route)
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
        route = Screen.ReportContract.route
    ) {
        ReportContractScreen(
            onBackClick = { navController.popBackStack() })
    }

    composable(
        route = Screen.OrganizationPerformance.route
    ) {
        OrganizationPerformanceScreen(
            onBackClick = { navController.popBackStack() })
    }

    composable(
        route = "visitor_analysis/{visitorId}",
        arguments = listOf(navArgument("visitorId") { type = NavType.StringType })
    ) { backStackEntry ->
        val visitorId = backStackEntry.arguments?.getString("visitorId") ?: ""
        VisitorAnalysisScreen(
            visitorId = visitorId,
            onBackClick = { navController.popBackStack() },
            onCallClick = {
                // لانچ کردن Intent تماس سیستمی با بازاریاب
            }
        )
    }

}
