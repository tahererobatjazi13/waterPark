package ir.kitgroup.partnerManagement.feature.offer.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ir.kitgroup.partnerManagement.feature.home.navigation.BottomNavItem
import ir.kitgroup.partnerManagement.feature.offer.ui.AddOfferTicketPlanLineDetailScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.AddOfferTicketPlanScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.OfferTicketPlanLineDetailScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.OfferTicketPlanLineListScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.OfferTicketPlanListScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.demoOfferLinePlans
import ir.kitgroup.partnerManagement.feature.offer.ui.demoOfferPlans
import ir.kitgroup.partnerManagement.navigation.Screen


fun NavGraphBuilder.offerNavGraph(navController: NavController) {

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

}
