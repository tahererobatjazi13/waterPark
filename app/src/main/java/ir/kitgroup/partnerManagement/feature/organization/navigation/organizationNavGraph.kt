package ir.kitgroup.partnerManagement.feature.organization.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.navArgument
import ir.kitgroup.partnerManagement.feature.home.navigation.BottomNavItem
import ir.kitgroup.partnerManagement.feature.organization.ui.AddOrganizationScreen
import ir.kitgroup.partnerManagement.feature.organization.ui.AssignVisitorScreen
import ir.kitgroup.partnerManagement.feature.organization.ui.OrganizationDetailScreen
import ir.kitgroup.partnerManagement.feature.organization.ui.OrganizationsListScreen
import ir.kitgroup.partnerManagement.navigation.Screen

fun NavGraphBuilder.organizationNavGraph(
    navController: NavController
) {
    navigation(
        startDestination = BottomNavItem.Organizations.route,
        route = "organization_graph"
    ) {

        // لیست سازمان‌ها
        composable(BottomNavItem.Organizations.route) {
            OrganizationsListScreen(
                onNavigateToDetail = { organizationId ->
                    navController.navigate(
                        Screen.OrganizationDetail.createRoute(organizationId)
                    )
                },
                onNavigateToAdd = {
                    navController.navigate(
                        Screen.AddOrganization.route
                    )
                }
            )
        }

        // افزودن سازمان
        composable(
            route = Screen.AddOrganization.route
        ) {
            AddOrganizationScreen(
                organizationId = 0,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // ویرایش سازمان
        composable(
            route = Screen.EditOrganization.route,
            arguments = listOf(
                navArgument("organizationId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val organizationId =
                backStackEntry.arguments?.getInt("organizationId") ?: 0

            AddOrganizationScreen(
                organizationId = organizationId,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // جزئیات سازمان
        composable(
            route = Screen.OrganizationDetail.route,
            arguments = listOf(
                navArgument("organizationId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val organizationId =
                backStackEntry.arguments?.getInt("organizationId") ?: 0

            OrganizationDetailScreen(
                organizationId = organizationId,

                onBack = {
                    navController.popBackStack()
                },

                onEditClick = {
                    navController.navigate(
                        Screen.EditOrganization.createRoute(
                            organizationId
                        )
                    )
                },

                onDisableClick = {
                    // عملیات غیرفعال‌سازی
                },

                onVisitClick = { visitId ->
                    navController.navigate(
                        Screen.VisitDetail.createRoute(visitId)
                    )
                },

                onAddVisitClick = {
                    navController.navigate(
                        Screen.RegisterVisit.createRoute(
                            organizationId = organizationId
                        )
                    )
                },

                onAssignVisitorClick = {
                    navController.navigate(
                        "assign_visitor/$organizationId"
                    )
                },

                onEditVisitorClick = {
                    // TODO
                },

                onDeleteVisitorClick = {
                    // TODO
                },

                onAssignStandsClick = {
                    navController.navigate(
                        Screen.AddAdvertisingStandAssignmentOrganization
                            .createRoute(organizationId)
                    )
                },

                onAssignContractClick = {
                    navController.navigate(
                        Screen.AddContract.createRoute(
                            organizationId = organizationId
                        )
                    )
                },

                onViewItemDetailsClick = { allocation ->
                    navController.navigate(
                        Screen.AdvertisingStandAssignmentVisitorDetail
                            .createRoute(
                                assignmentId = allocation.id
                            )
                    )
                },

                onContractClick = { contract ->
                    navController.navigate(
                        Screen.ContractDetail.createRoute(contract.id)
                    )
                },

                onAddTicketOfferClick = {
                    navController.navigate(
                        Screen.AddContractOffer.route
                    )
                }
            )
        }

        composable(
            route = Screen.
            AssignVisitor.route,
            arguments = listOf(
                navArgument("organizationId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val organizationId =
                backStackEntry.arguments?.getInt("organizationId") ?: 0

            AssignVisitorScreen(
                organizationId = organizationId,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}