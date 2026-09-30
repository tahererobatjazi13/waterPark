package ir.kitgroup.partnerManagement.feature.offer.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ir.kitgroup.partnerManagement.feature.home.navigation.BottomNavItem
import ir.kitgroup.partnerManagement.feature.offer.ui.AddOfferDetailScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.AddOfferScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail.OfferDetailScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail_list.OfferDetailsListScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_list.OfferListScreen
import ir.kitgroup.partnerManagement.navigation.Screen


fun NavGraphBuilder.offerNavGraph(navController: NavController) {

    //  لیست کلی آفرها
    composable(BottomNavItem.Offers.route) {
        OfferListScreen(
            onAddClick = {
                navController.navigate(Screen.AddOffer.route)
            },
            onOfferClick = { offer ->
                navController.navigate(
                    Screen.OfferDetailList.createRoute(offer.offerId)
                )
            }
        )
    }

    // افزودن  طرح جدید
    composable(Screen.AddOffer.route) {
        AddOfferScreen(
            onBackClick = { navController.popBackStack() },
            onSaveClick = {
            }
        )
    }

    // لیست جزییات و لاین‌های طرح انتخاب‌شده
    composable(
        route = Screen.OfferDetailList.route,
        arguments = listOf(
            navArgument("offerId") { type = NavType.StringType }
        )
    ) {
        OfferDetailsListScreen(
            onBackClick = { navController.popBackStack() },
            onAddClick = { planTitle ->
                navController.navigate(
                    Screen.AddOfferDetail.createRoute(planTitle)
                )
            },
            onOfferTicketPlanLineClick = { item  ->
                navController.navigate(
                         Screen.OfferDetail.createRoute(item.offerDetail.offerDetailId)
                )
            }
        )
    }


// ثبت جزییات طرح جدید با دریافت نام طرح
    composable(
        route = Screen.AddOfferDetail.route,
        arguments = listOf(
            navArgument("offerTicketPlanLineName") {
                type = NavType.StringType
                defaultValue = ""
            }
        )
    ) { backStackEntry ->
        val rawName = backStackEntry.arguments?.getString("offerTicketPlanLineName").orEmpty()
        val decodedPlanName = runCatching {
            java.net.URLDecoder.decode(rawName, "UTF-8").trim()
        }.getOrDefault(rawName)

        AddOfferDetailScreen(
            initialPlanHeader = decodedPlanName,
            onBackClick = { navController.popBackStack() },
            onSaveClick = { navController.popBackStack() }
        )
    }


    // صفحه جزئیات کالا/خدمت لاین طرح
    composable(
        route = Screen.OfferDetail.route,
        arguments = listOf(
            navArgument("offerDetailId") { type = NavType.StringType }
        )
    ) {
        OfferDetailScreen(
            onBackClick = { navController.popBackStack() }
        )
    }

}
