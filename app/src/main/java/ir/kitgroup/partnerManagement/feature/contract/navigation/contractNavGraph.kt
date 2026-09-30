package ir.kitgroup.partnerManagement.feature.contract.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ir.kitgroup.partnerManagement.feature.contract.ui.AddContractRoute
import ir.kitgroup.partnerManagement.feature.contract.ui.ContractDetailScreen
import ir.kitgroup.partnerManagement.feature.contract.ui.ContractsListScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.AddContractOfferScreen
import ir.kitgroup.partnerManagement.navigation.Screen


fun NavGraphBuilder.contractNavGraph(navController: NavController) {

    composable(Screen.ContractsList.route) {

        ContractsListScreen(
            onBackClick = { navController.popBackStack() },
            onAddClick = {
                navController.navigate(Screen.AddContract.route)
            },
            onContractClick = { contract ->
                navController.navigate(
                    Screen.ContractDetail.createRoute(contract.contractId)
                )
            })
    }

    composable(
        route = Screen.ContractDetail.route,
        arguments = listOf(
            navArgument("contractId") { type = NavType.StringType }
        )
    ) {
        ContractDetailScreen(
            onBackClick = { navController.popBackStack() }
        )
    }

    composable(
        route = "${Screen.AddContract.route}?preselectedOrganizationId={preselectedOrganizationId}",
        arguments = listOf(
            navArgument("preselectedOrganizationId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) {
        AddContractRoute(
            onBackClick = { navController.popBackStack() },
            onSaveClick = { contract ->
                // عملیات ذخیره‌سازی یا فراخوانی متد ذخیره
                navController.popBackStack()
            }
        )
    }


    composable(Screen.AddContractOffer.route) {
        AddContractOfferScreen(
            onBackClick = { navController.popBackStack() },
            onSaveClick = {
            }
        )
    }
}
