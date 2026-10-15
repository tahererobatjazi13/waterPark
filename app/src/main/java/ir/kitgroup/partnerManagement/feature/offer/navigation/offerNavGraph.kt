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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.util.SYNC_AFTER_OFFER_DETAIL_SAVE
import ir.kitgroup.partnerManagement.feature.home.navigation.BottomNavItem
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_plan_list.add.AddEditOfferScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail_plan_list.add.AddOfferDetailScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail.OfferDetailScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail_plan_list.list.OfferDetailPlanListScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.offer_plan_list.list.OfferPlanListRoute
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
    ) { backStackEntry ->

        // شنود سیگنال سینک و رفرش از SavedStateHandle همین Entry
        val shouldRefreshAndSync by backStackEntry.savedStateHandle
            .getStateFlow(SYNC_AFTER_OFFER_DETAIL_SAVE, false)
            .collectAsStateWithLifecycle()

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
                OfferDetailPlanListScreen(
                    shouldRefreshAndSync = shouldRefreshAndSync,
                    onRefreshHandled = {
                        backStackEntry.savedStateHandle[SYNC_AFTER_OFFER_DETAIL_SAVE] = false
                    },
                    onAddClick = { planId, planTitle ->
                        navController.navigate(
                            Screen.AddOfferDetail.createRoute(planId, planTitle)
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
            }
        )
    ) { backStackEntry ->
        val offerPlanId = backStackEntry.arguments?.getString("offerTicketPlanId").orEmpty()
        val rawName = backStackEntry.arguments?.getString("offerTicketPlanName").orEmpty()
        val decodedPlanName = runCatching {
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
                AddOfferDetailScreen(
                    offerDetailId = offerPlanId,
                    initialPlanHeader = decodedPlanName,
                    onSaveSuccess = {
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(SYNC_AFTER_OFFER_DETAIL_SAVE, true)

                        navController.popBackStack()
                    }
                )
            }
        }
    }
    // ویرایش جزئیات سطر طرح
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
                // صفحه فرم ثبت/ویرایش (مثلاً با ارسال offerDetailId برای لود اطلاعات قبلی سطر)
                AddOfferDetailScreen(
                    offerDetailId = offerDetailId, // یا کامپوننت مخصوص ویرایش شما
                    onSaveSuccess = {
                        // ارسال فلگ سینک و آپدیت به پشته قبلی
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(SYNC_AFTER_OFFER_DETAIL_SAVE, true)
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
                    onNavigateToEdit = { offerDetailId ->
                        navController.navigate(Screen.EditOfferDetail.createRoute(offerDetailId))
                    }
                )
            }
        }
    }


}
