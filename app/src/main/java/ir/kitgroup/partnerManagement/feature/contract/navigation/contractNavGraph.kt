package ir.kitgroup.partnerManagement.feature.contract.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ir.kitgroup.partnerManagement.core.ui.util.demoContracts
import ir.kitgroup.partnerManagement.feature.contract.ui.AddContractScreen
import ir.kitgroup.partnerManagement.feature.contract.ui.ContractDetailScreen
import ir.kitgroup.partnerManagement.feature.contract.ui.ContractsListScreen
import ir.kitgroup.partnerManagement.feature.offer.ui.AddContractOfferScreen
import ir.kitgroup.partnerManagement.navigation.Screen


fun NavGraphBuilder.contractNavGraph(navController: NavController) {

    composable(Screen.ContractsList.route) {
        val contracts = demoContracts()

        ContractsListScreen(
            onBackClick = { navController.popBackStack() },
            onAddClick = {
                navController.navigate(Screen.AddContract.route)
            },
            onContractClick = { contract ->
                navController.navigate(
                    Screen.ContractDetail.createRoute(contract.contractId)
                )
            },
            contracts = contracts
        )
    }

    composable(
        route = Screen.ContractDetail.route,
        arguments = listOf(
            navArgument("contractId") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val contractId = backStackEntry.arguments?.getString("contractId") ?: ""
        val contract = demoContracts().firstOrNull { it.contractId == contractId }

        if (contract != null) {
            ContractDetailScreen(
                contract = contract,
                onBackClick = { navController.popBackStack() }
            )
        }
    }

    composable(
        route = Screen.AddContract.route,
        arguments = listOf(
            navArgument("organizationId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) { backStackEntry ->
        val organizationId = backStackEntry.arguments?.getString("organizationId")
        AddContractScreen(
            preselectedOrganizationId = organizationId,
            onBackClick = { navController.popBackStack() },
            onSaveClick = {
                // عملیات ذخیره‌سازی
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
