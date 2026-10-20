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
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_plan_list.add.AddEditOfferScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail.OfferDetailScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail_plan_list.add.AddEditOfferDetailPlanScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail_plan_list.list.OfferDetailPlanListScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_plan_list.list.OfferPlanListRoute
import ir.kitgroup.partnerManagement.feature.offer.ui.serial_offer.add.AddEditSerialOfferScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.serial_offer.detail.AssignedSerialDetailScreen
import ir.kitgroup.partnerManagement.navigation.Screen

fun NavGraphBuilder.offerNavGraph(navController: NavController) {

    // لیست کلی آفرها
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

            OfferPlanListRoute(
                onOfferClick = { offer ->
                    navController.navigate(
                        Screen.OfferDetailList.createRoute(offer.offerId)
                    )
                },
                onAddOfferClick = {
                    navController.navigate(Screen.AddOffer.route)
                },
                onEditOfferClick = { offerId ->
                    navController.navigate(
                        Screen.EditOffer.createRoute(offerId)
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
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomHeader(
                title = stringResource(
                    R.string.label_create_plan_offer
                ),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                AddEditOfferScreen(
                    onSuccessSave = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
    // ویرایش طرح موجود
    composable(
        route = Screen.EditOffer.route,
        arguments = listOf(
            navArgument("offerId") {
                type = NavType.StringType
            }
        ),
        enterTransition = { fadeIn(tween(250)) },
        exitTransition = { fadeOut(tween(200)) },
        popEnterTransition = { fadeIn(tween(250)) },
        popExitTransition = { fadeOut(tween(200)) }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomHeader(
                title = stringResource(R.string.label_edit_plan_offer),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                AddEditOfferScreen(
                    onSuccessSave = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }

    // لیست جزییات طرح انتخاب‌شده
    composable(
        route = Screen.OfferDetailList.route,
        arguments = listOf(
            navArgument("offerId") { type = NavType.StringType }),
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {

        Column(modifier = Modifier.fillMaxSize()) {

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
                OfferDetailPlanListScreen(
                    onAddOfferDetailPlanClick = { planId, planTitle ->
                        navController.navigate(
                            Screen.AddOfferDetail.createRoute(planId, planTitle)
                        )
                    },
                    onOfferDetailPlanClick = { item ->
                        navController.navigate(
                            Screen.OfferDetail.createRoute(item.offerDetail.offerDetailId)
                        )
                    }
                )
            }
        }
    }


    // ثبت جزییات طرح جدید
    composable(
        route = Screen.AddOfferDetail.route,
        arguments = listOf(
            navArgument("offerTicketPlanId") {
                type = NavType.StringType
            },
            navArgument("offerTicketPlanName") {
                type = NavType.StringType
                defaultValue = ""
                nullable = true
            }
        ),
        enterTransition = { fadeIn(tween(250)) },
        exitTransition = { fadeOut(tween(200)) },
        popEnterTransition = { fadeIn(tween(250)) },
        popExitTransition = { fadeOut(tween(200)) }
    ) { backStackEntry ->
        val offerTicketPlanId = backStackEntry.arguments?.getString("offerTicketPlanId").orEmpty()
        val rawName = backStackEntry.arguments?.getString("offerTicketPlanName").orEmpty()
        val offerTicketPlanName = runCatching {
            java.net.URLDecoder.decode(rawName, "UTF-8").trim()
        }.getOrDefault(rawName)

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomHeader(
                title = stringResource(R.string.label_offer_product_service),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                AddEditOfferDetailPlanScreen(
                    offerTicketPlanId = offerTicketPlanId,
                    offerTicketPlanLineId = null,
                    offerTicketPlanName = offerTicketPlanName,
                    onSuccessSave = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }

    // ویرایش جزئیات طرح موجود
    composable(
        route = Screen.EditOfferDetail.route,
        arguments = listOf(
            navArgument("offerDetailId") {
                type = NavType.StringType
            }
        ),
        enterTransition = { fadeIn(tween(250)) },
        exitTransition = { fadeOut(tween(200)) },
        popEnterTransition = { fadeIn(tween(250)) },
        popExitTransition = { fadeOut(tween(200)) }
    ) { backStackEntry ->
        val offerDetailId = backStackEntry.arguments?.getString("offerDetailId").orEmpty()

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomHeader(
                title = stringResource(R.string.label_offer_product_service),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                AddEditOfferDetailPlanScreen(
                    offerTicketPlanId = null,
                    offerTicketPlanLineId = offerDetailId,
                    offerTicketPlanName = "",
                    onSuccessSave = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }


    // صفحه جزئیات کالا/خدمت لاین طرح
    composable(
        route = Screen.OfferDetail.route,
        arguments = listOf(
            navArgument("offerDetailId") { type = NavType.StringType }
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomHeader(
                title = stringResource(
                    R.string.label_offer_product_service
                ),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                OfferDetailScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onEditClick = { offerDetailId ->
                        navController.navigate(Screen.EditOfferDetail.createRoute(offerDetailId))
                    }
                )
            }
        }
    }


    // ------------------------------------------------
    // Serial Offer
    // ------------------------------------------------
    composable(
        route = Screen.AddSerialOffer.route,
        arguments = listOf(navArgument("organizationId") {
            type = NavType.StringType
            nullable = true
        }),
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) { backStackEntry ->
        val organizationId = backStackEntry.arguments?.getString("organizationId")

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomHeader(
                title = stringResource(
                    R.string.label_contract_offer
                ),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                AddEditSerialOfferScreen(
                    organizationId = organizationId,
                    onSaveSuccess = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }


    composable(
        route = Screen.EditSerialOffer.route,
        arguments = listOf(
            navArgument("serialId") {
                type = NavType.StringType
            }
        ),
        enterTransition = { fadeIn(tween(250)) },
        exitTransition = { fadeOut(tween(200)) },
        popEnterTransition = { fadeIn(tween(250)) },
        popExitTransition = { fadeOut(tween(200)) }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomHeader(
                title = stringResource(R.string.label_contract_offer),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                AddEditSerialOfferScreen(
                    onSaveSuccess = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }


    composable(
        route = Screen.AssignedSerialDetail.route,
        arguments = listOf(
            navArgument("serialId") { type = NavType.StringType }
        )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            CustomHeader(
                title = stringResource(R.string.label_serial_detail),
                showBackButton = true,
                onBackClick = { navController.popBackStack() }
            )

            AssignedSerialDetailScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onEditClick = { serialId ->
                    navController.navigate(Screen.EditSerialOffer.createRoute(serialId = serialId))
                }
            )
        }
    }
}
