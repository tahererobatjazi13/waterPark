package ir.kitgroup.partnerManagement.feature.offer.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ir.kitgroup.partnerManagement.core.ui.util.demoOfferTicketPlans
import ir.kitgroup.partnerManagement.core.ui.util.demoOfferTicketPlanLines
import ir.kitgroup.partnerManagement.feature.home.navigation.BottomNavItem
import ir.kitgroup.partnerManagement.feature.offer.ui.AddOfferTicketPlanLineDetailScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.AddOfferTicketPlanScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.OfferTicketPlanLineDetailScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.OfferTicketPlanLineListScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.OfferTicketPlanListScreen
import ir.kitgroup.partnerManagement.navigation.Screen


fun NavGraphBuilder.offerNavGraph(navController: NavController) {

    //  لیست کلی طرح‌ها
    composable(BottomNavItem.PlanOffer.route) {

        OfferTicketPlanListScreen(
            onAddClick = {
                navController.navigate(Screen.AddOfferTicketPlan.route)
            },
            onOfferTicketPlanClick = { offerTicketPlan ->
                navController.navigate(
                    Screen.OfferTicketPlanLineList.createRoute(offerTicketPlan.offerTicketPlanId)
                )
            },
            offerTicketPlans = demoOfferTicketPlans()
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
            navArgument("offerTicketPlanId") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val offerTicketPlanId = backStackEntry.arguments?.getString("offerTicketPlanId").orEmpty()
        val selectedOfferTicketPlan = demoOfferTicketPlans().firstOrNull { it.offerTicketPlanId == offerTicketPlanId }
        val offerTicketPlanLines = demoOfferTicketPlanLines()

        OfferTicketPlanLineListScreen(
            headerOfferTicketPlan = selectedOfferTicketPlan,
            offerTicketPlanLines = offerTicketPlanLines,
            onBackClick = { navController.popBackStack() },
            onAddClick = {
                navController.navigate(
                    Screen.AddOfferTicketPlanLineDetail.createRoute(
                        selectedOfferTicketPlan?.name ?: ""
                    )
                )
            },
            onOfferTicketPlanLineClick = { planLine ->
                navController.navigate(
                    Screen.OfferTicketPlanLineDetail.createRoute(planLine.offerTicketPlanLineId)
                )
            }
        )
    }

    // ثبت جزییات طرح جدید با دریافت نام طرح
    composable(
        route = Screen.AddOfferTicketPlanLineDetail.route,
        arguments = listOf(
            navArgument("offerTicketPlanLineName") {
                type = NavType.StringType
                defaultValue = ""
            }
        )
    ) { backStackEntry ->
        val offerTicketPlanLineName = backStackEntry.arguments?.getString("offerTicketPlanLineName").orEmpty()
        AddOfferTicketPlanLineDetailScreen(
            initialPlanHeader = offerTicketPlanLineName,
            onBackClick = { navController.popBackStack() },
            onSaveClick = { navController.popBackStack() }
        )
    }

    // صفحه جزئیات طرح
    composable(
        route = Screen.OfferTicketPlanLineDetail.route,
        arguments = listOf(
            navArgument("offerTicketPlanLineId") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val offerTicketPlanLineId = backStackEntry.arguments?.getString("offerTicketPlanLineId").orEmpty()
        val planLine = demoOfferTicketPlanLines().firstOrNull { it.offerTicketPlanLineId == offerTicketPlanLineId }

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

}
