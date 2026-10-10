package ir.kitgroup.partnerManagement.feature.offer.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.feature.home.navigation.BottomNavItem
import ir.kitgroup.partnerManagement.feature.offer.ui.AddOfferDetailScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.AddOfferScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail.OfferDetailScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail_list.OfferDetailsListScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_list.OfferListScreen
import ir.kitgroup.partnerManagement.navigation.Screen


fun NavGraphBuilder.offerNavGraph(navController: NavController) {

    //  لیست کلی آفرها
    composable(
        route = BottomNavItem.Offers.route,
        enterTransition = { fadeIn(tween(250)) },
        exitTransition = { fadeOut(tween(200)) },
        popEnterTransition = { fadeIn(tween(250)) },
        popExitTransition = { fadeOut(tween(200)) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            CustomHeader(
                title = stringResource(R.string.nav_plan_offer),
                showBackButton = false
            )

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
    }

    // افزودن  طرح جدید
    composable(
        route = Screen.AddOffer.route,
        enterTransition = { fadeIn(tween(250)) },
        exitTransition = { fadeOut(tween(200)) },
        popEnterTransition = { fadeIn(tween(250)) },
        popExitTransition = { fadeOut(tween(200)) }
    ) {
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
        ),
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        },
        popEnterTransition = {
            EnterTransition.None
        },
        popExitTransition = {
            ExitTransition.None
        }
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomHeader(
                title = stringResource(
                    R.string.label_offer_details
                ),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                OfferDetailsListScreen(
                    onAddClick = { planTitle ->
                        navController.navigate(
                            Screen.AddOfferDetail.createRoute(planTitle)
                        )
                    },
                    onOfferTicketPlanLineClick = { item ->
                        navController.navigate(
                            Screen.OfferDetail.createRoute(item.offerDetail.offerDetailId)
                        )
                    }
                )
            }
        }
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
