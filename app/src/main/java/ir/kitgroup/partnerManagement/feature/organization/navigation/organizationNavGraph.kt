package ir.kitgroup.partnerManagement.feature.organization.navigation

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
import androidx.navigation.navigation
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.util.SYNC_AFTER_ORGANIZATION_SAVE
import ir.kitgroup.partnerManagement.feature.home.navigation.BottomNavItem
import ir.kitgroup.partnerManagement.feature.organization.ui.AssignVisitorScreen
import ir.kitgroup.partnerManagement.feature.organization.ui.add.AddOrganizationScreen
import ir.kitgroup.partnerManagement.feature.organization.ui.add.MapScreen
import ir.kitgroup.partnerManagement.feature.organization.ui.detail.OrganizationDetailScreen
import ir.kitgroup.partnerManagement.feature.organization.ui.list.OrganizationsListScreen
import ir.kitgroup.partnerManagement.navigation.Screen

fun NavGraphBuilder.organizationNavGraph(
    navController: NavController
) {
    navigation(
        startDestination = BottomNavItem.Organizations.route,
        route = "organization_graph"
    ) {

        // لیست سازمان‌ها
        composable(
            route = BottomNavItem.Organizations.route,
            enterTransition = { fadeIn(tween(250)) },
            exitTransition = { fadeOut(tween(200)) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {

                CustomHeader(
                    title = stringResource(R.string.nav_organizations),
                    showBackButton = false
                )

                OrganizationsListScreen(
                    navController = navController,
                    onOrganizationClick = { organizationId ->
                        navController.navigate(
                            Screen.OrganizationDetail.createRoute(organizationId)
                        )
                    },
                    onAddOrganizationClick = {
                        navController.navigate(
                            Screen.AddOrganization.route
                        )
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                )
            }
        }

        // افزودن سازمان
        composable(
            route = Screen.AddOrganization.route,
            enterTransition = { fadeIn(tween(250)) },
            exitTransition = { fadeOut(tween(200)) },
            popEnterTransition = { fadeIn(tween(250)) },
            popExitTransition = { fadeOut(tween(200)) }
        ) { backStackEntry ->

            val latitude = backStackEntry.savedStateHandle
                .get<Any>("selected_latitude")?.toString()?.toDoubleOrNull()

            val longitude = backStackEntry.savedStateHandle
                .get<Any>("selected_longitude")?.toString()?.toDoubleOrNull()
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                CustomHeader(
                    title = stringResource(
                        R.string.label_add_new_organization
                    ),

                    showBackButton = true,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AddOrganizationScreen(
                        organizationId = null,   onBack = {
                            navController.popBackStack()
                        },
                        onSuccessCreate = {
                            // تنظیم پرچم برای لیست سازمان‌ها
                            navController.getBackStackEntry(BottomNavItem.Organizations.route)
                                .savedStateHandle[SYNC_AFTER_ORGANIZATION_SAVE] = true

                            // بازگشت به لیست سازمان‌ها
                            navController.popBackStack(
                                BottomNavItem.Organizations.route,
                                inclusive = false
                            )
                        },
                        onSelectLocation = {
                            navController.navigate("map_screen")
                        },
                        selectedLatitude = latitude,
                        selectedLongitude = longitude
                    )
                }
            }


        }

        // ویرایش سازمان
        composable(
            route = Screen.EditOrganization.route,
            arguments = listOf(
                navArgument("organizationId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
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
        ) { backStackEntry ->

            val organizationId = backStackEntry.arguments?.getString("organizationId")

            val latitude = backStackEntry.savedStateHandle
                .get<Any>("selected_latitude")?.toString()?.toDoubleOrNull()

            val longitude = backStackEntry.savedStateHandle
                .get<Any>("selected_longitude")?.toString()?.toDoubleOrNull()
            val isEditMode = !organizationId.isNullOrBlank()


            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                CustomHeader(
                    title = if (isEditMode) {
                        R.string.label_edit_organization
                    } else {
                        R.string.label_add_new_organization
                    },
                    showBackButton = true,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AddOrganizationScreen(
                        organizationId = organizationId,   onBack = {
                            navController.popBackStack()
                        },
                        onSuccessCreate = {
                            // تنظیم پرچم برای لیست سازمان‌ها
                            navController.getBackStackEntry(BottomNavItem.Organizations.route)
                                .savedStateHandle[SYNC_AFTER_ORGANIZATION_SAVE] = true

                            // بازگشت به لیست سازمان‌ها
                            navController.popBackStack(
                                BottomNavItem.Organizations.route,
                                inclusive = false
                            )
                        },
                        onSelectLocation = {
                            navController.navigate("map_screen")
                        },
                        selectedLatitude = latitude,
                        selectedLongitude = longitude
                    )
                }
            }

        }

        // جزئیات سازمان
        composable(
            route = Screen.OrganizationDetail.route,
            arguments = listOf(
                navArgument("organizationId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
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
        ) { backStackEntry ->

            val organizationId = backStackEntry.arguments?.getString("organizationId")

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                CustomHeader(
                    title = stringResource(
                        R.string.label_details
                    ),
                    showBackButton = true,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    OrganizationDetailScreen(
                        organizationId = organizationId!!,

                        onStatusChangeSuccess = {
                            navController.getBackStackEntry(BottomNavItem.Organizations.route)
                                .savedStateHandle[SYNC_AFTER_ORGANIZATION_SAVE] = true

                            navController.popBackStack(
                                BottomNavItem.Organizations.route,
                                inclusive = false
                            )
                        },
                        onWarningSuccess = {
                            navController.getBackStackEntry(BottomNavItem.Organizations.route)
                            navController.getBackStackEntry(BottomNavItem.Organizations.route)
                                .savedStateHandle[SYNC_AFTER_ORGANIZATION_SAVE] = true

                            navController.popBackStack(
                                BottomNavItem.Organizations.route,
                                inclusive = false
                            )
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
                        onMeetingClick = { meetingId ->
                            navController.navigate(
                                Screen.MeetingDetail.createRoute(meetingId)
                            )
                        },
                        onAddMeetingClick = {
                            navController.navigate(
                                Screen.AddMeeting.createRoute(
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
                                Screen.AddAssignedStandsOrganization
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
                                Screen.AssignedStandsDetail
                                    .createRoute(
                                        assignedStandId = allocation.assignedStand.assignedStandId
                                    )
                            )
                        },
                        onContractClick = { contract ->
                            navController.navigate(
                                Screen.ContractDetail.createRoute(contract.contractId)
                            )
                        },
                        onAddTicketOfferClick = {
                            navController.navigate(
                                Screen.AddSerialOffer.createRoute(organizationId)
                            )
                        }, navController = navController
                    )
                }
            }


        }

        composable(
            route = Screen.AssignVisitor.route,
            arguments = listOf(
                navArgument("organizationId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
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
        ) { backStackEntry ->


            val organizationId = backStackEntry.arguments?.getString("organizationId")
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                CustomHeader(
                    title = stringResource(
                        R.string.label_assigning_visitor_organization
                    ),
                    showBackButton = true,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AssignVisitorScreen(
                        organizationId = organizationId,
                    )
                }
            }


        }
    }
    composable("map_screen",
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
        }) {
        MapScreen(
            navController = navController,
            onBackClick = { navController.popBackStack() },

            )
    }
}