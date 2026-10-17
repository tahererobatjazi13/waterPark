package ir.kitgroup.partnerManagement.feature.contract.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.util.SYNC_AFTER_SERIAL_OFFER_SAVE
import ir.kitgroup.partnerManagement.feature.contract.ui.add.AddEditContractScreen
import ir.kitgroup.partnerManagement.feature.contract.ui.detail.ContractDetailScreen
import ir.kitgroup.partnerManagement.feature.contract.ui.list.ContractsListScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.serial_offer.detail.AssignedSerialDetailScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.serial_offer.add.AddContractOfferScreen
import ir.kitgroup.partnerManagement.navigation.Screen

fun NavGraphBuilder.contractNavGraph(
    navController: NavController
) {

    // ------------------------------------------------
    // Contracts List
    // ------------------------------------------------
    composable(
        route =
        Screen.ContractsList.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomHeader(
                title = stringResource(
                    R.string.label_contract_organization
                ),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                ContractsListScreen(
                    onAddContractClick = {
                        navController.navigate(Screen.AddContract.route)
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
        }
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
        Column(modifier = Modifier.fillMaxSize()) {

            CustomHeader(
                title = stringResource(
                    R.string.label_details
                ),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )

            Box(modifier = Modifier.fillMaxSize()) {

                ContractDetailScreen(
                    onEditClick = { contractId ->
                        navController.navigate(Screen.EditContract.createRoute(contractId))
                    },
                    onTerminateActiveContract = {
                        navController.popBackStack()
                    }
                )
            }
        }

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
            navArgument(Screen.AddContract.ORGANIZATION_ID_ARGUMENT) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        ),
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomHeader(
                title = stringResource(
                    R.string.label_contract_organization
                ),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                AddEditContractScreen(
                    onSuccessSave = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }


    // ------------------------------------------------
    // Edit Contract
    // ------------------------------------------------
    composable(
        route = Screen.EditContract.route,
        arguments = listOf(navArgument("contractId") { type = NavType.StringType }),
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomHeader(
                title = stringResource(
                    R.string.label_contract_organization
                ),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                AddEditContractScreen(
                    onSuccessSave = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }


    // ------------------------------------------------
    // Contract Offer
    // ------------------------------------------------
    composable(
        route = Screen.AddContractOffer.route,
        arguments = listOf(navArgument("organizationId") {
            type = NavType.StringType
            nullable = true
        }
        ),
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
                AddContractOfferScreen(
                    organizationId = organizationId,
                    onSaveSuccess = {
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set(SYNC_AFTER_SERIAL_OFFER_SAVE, true)
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
                title = stringResource(R.string.label_ticket_offer_detail),
                showBackButton = true,
                onBackClick = { navController.popBackStack() }
            )

            AssignedSerialDetailScreen(
                onEditClick = { id ->
                    navController.navigate(Screen.AddContractOffer.createRoute(organizationId = id))
                }
            )
        }
    }
}