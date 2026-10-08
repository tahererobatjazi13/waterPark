package ir.kitgroup.partnerManagement.feature.contract.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ir.kitgroup.partnerManagement.core.ui.util.SYNC_AFTER_CONTRACT_SAVE
import ir.kitgroup.partnerManagement.feature.contract.ui.add.AddContractRoute
import ir.kitgroup.partnerManagement.feature.contract.ui.ContractDetailScreen
import ir.kitgroup.partnerManagement.feature.contract.ui.ContractsListScreen
import ir.kitgroup.partnerManagement.feature.dashboard.ui.DashboardViewModel
import ir.kitgroup.partnerManagement.feature.home.navigation.BottomNavItem
import ir.kitgroup.partnerManagement.feature.offer.ui.AddContractOfferScreen
import ir.kitgroup.partnerManagement.navigation.Screen
/*
fun NavGraphBuilder.contractNavGraph(
    navController: NavController
) {

    // لیست قراردادها
    composable(
        route = Screen.ContractsList.route
    ) {
        ContractsListScreen(
            onBackClick = {
                navController.popBackStack()
            },

            onAddClick = {
                navController.navigate(
                    Screen.AddContract.createRoute()
                )
            },

            onContractClick = { contract ->
                navController.navigate(
                    Screen.ContractDetail.createRoute(
                        contract.contractId
                    )
                )
            }
        )
    }

    // جزئیات قرارداد
    composable(
        route = Screen.ContractDetail.route,
        arguments = listOf(
            navArgument("contractId") {
                type = NavType.StringType
            }
        )
    ) {
        ContractDetailScreen(
            onBackClick = {
                navController.popBackStack()
            }
        )
    }

    // افزودن قرارداد
    composable(
        route = "${Screen.AddContract.route}?${Screen.AddContract.ORGANIZATION_ID_ARGUMENT}={${Screen.AddContract.ORGANIZATION_ID_ARGUMENT}}",
        arguments = listOf(
            navArgument(Screen.AddContract.ORGANIZATION_ID_ARGUMENT) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) {
        AddContractRoute(
            onBackClick = {
                navController.popBackStack()
            },

            onSaved = {

                navController.navigate(
                    BottomNavItem.Dashboard.route
                ) {
                    popUpTo(Screen.AddContract.route) {
                        inclusive = true
                    }

                    launchSingleTop = true
                }
            }
        )
    }

    // افزودن پیشنهاد قرارداد
    composable(
        route = Screen.AddContractOffer.route
    ) {
        AddContractOfferScreen(
            onBackClick = {
                navController.popBackStack()
            },

            onSaveClick = {
                // ذخیره
            }
        )
    }
}*/

fun NavGraphBuilder.contractNavGraph(
    navController: NavController
) {

    // ------------------------------------------------
    // Contracts List
    // ------------------------------------------------

    composable(
        route =
        Screen.ContractsList.route
    ) {

        ContractsListScreen(

            onBackClick = {
                navController.popBackStack()
            },

            onAddClick = {
              //  dashboardViewModel.checkCanNavigateToAddContract {
                    navController.navigate(Screen.AddContract.route)
              //  }
            },

            onContractClick = { contract ->

                navController.navigate(
                    Screen.ContractDetail.createRoute(
                        contract.contractId
                    )
                )
            }
        )
    }


    // ------------------------------------------------
    // Contract Detail
    // ------------------------------------------------

    composable(
        route = Screen.ContractDetail.route,
        arguments = listOf(
            navArgument("contractId") {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->

        val contractId =
            backStackEntry.arguments
                ?.getString("contractId")

        ContractDetailScreen(
            contractId = contractId,

            onBackClick = {
                navController.popBackStack()
            },

            onEditClick = {
                navController.navigate(
                    Screen.EditContract.createRoute(
                        contractId!!
                    )
                )
            },

            onStatusChanged = {
                navController
                    .getBackStackEntry(
                        BottomNavItem.Dashboard.route
                    )
                    .savedStateHandle[
                    SYNC_AFTER_CONTRACT_SAVE
                ] = true

                navController.popBackStack(
                    BottomNavItem.Dashboard.route,
                    inclusive = false
                )
            }
        )
    }

    // ------------------------------------------------
    // Add Contract
    // ------------------------------------------------

    composable(

        route =
        "${Screen.AddContract.route}?" +
                "${Screen.AddContract.ORGANIZATION_ID_ARGUMENT}=" +
                "{${Screen.AddContract.ORGANIZATION_ID_ARGUMENT}}",

        arguments = listOf(

            navArgument(
                Screen.AddContract
                    .ORGANIZATION_ID_ARGUMENT
            ) {

                type =
                    NavType.StringType

                nullable = true

                defaultValue = null
            }
        )
    ) {

        AddContractRoute(

            onBackClick = {
                navController.popBackStack()
            },

            onSaved = {

                navController
                    .getBackStackEntry(
                        BottomNavItem.Dashboard.route
                    )
                    .savedStateHandle[
                    SYNC_AFTER_CONTRACT_SAVE
                ] = true

                navController.popBackStack(
                    BottomNavItem.Dashboard.route,
                    inclusive = false
                )
            }
        )
    }


    // ------------------------------------------------
    // Edit Contract
    // ------------------------------------------------

    composable(
        route = Screen.EditContract.route,
        arguments = listOf(
            navArgument("contractId") {
                type = NavType.StringType
            }
        )
    ) {

        AddContractRoute(
            onBackClick = {
                navController.popBackStack()
            },

            onSaved = {

                navController
                    .getBackStackEntry(
                        BottomNavItem.Dashboard.route
                    )
                    .savedStateHandle[
                    SYNC_AFTER_CONTRACT_SAVE
                ] = true

                navController.popBackStack(
                    BottomNavItem.Dashboard.route,
                    inclusive = false
                )
            }
        )
    }


    // ------------------------------------------------
    // Contract Offer
    // ------------------------------------------------

    composable(
        route =
        Screen.AddContractOffer.route
    ) {

        AddContractOfferScreen(

            onBackClick = {
                navController.popBackStack()
            },

            onSaveClick = {
                // ...
            }
        )
    }
}