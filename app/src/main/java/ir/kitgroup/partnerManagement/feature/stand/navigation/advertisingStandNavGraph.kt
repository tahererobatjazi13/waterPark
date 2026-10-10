package ir.kitgroup.partnerManagement.feature.stand.navigation

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
import ir.kitgroup.partnerManagement.feature.stand.ui.AdvertisingStandMenuScreen
import ir.kitgroup.partnerManagement.feature.stand.ui.assigned_stands_organization.AddAssignedStandsOrganizationScreen
import ir.kitgroup.partnerManagement.feature.stand.ui.assigned_stands_organization.AssignedStandsOrganizationListScreen
import ir.kitgroup.partnerManagement.feature.stand.ui.assigned_stands_visitor.AddAssignedStandsVisitorScreen
import ir.kitgroup.partnerManagement.feature.stand.ui.assigned_stands_visitor.AssignedStandsVisitorListScreen
import ir.kitgroup.partnerManagement.feature.stand.ui.detail.AssignedStandsDetailRoute
import ir.kitgroup.partnerManagement.feature.stand.ui.stand_type.AddStandTypeScreen
import ir.kitgroup.partnerManagement.feature.stand.ui.stand_type.StandTypesListScreen
import ir.kitgroup.partnerManagement.navigation.Screen


fun NavGraphBuilder.advertisingStandNavGraph(navController: NavController) {

    // منوی اصلی بخش استندهای تبلیغاتی
    composable(Screen.AdvertisingStandMenu.route,
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

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomHeader(
                title = stringResource(
                    R.string.label_advertising_stands
                ),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )
            Box(
                modifier = Modifier.fillMaxSize()
            ) {

                AdvertisingStandMenuScreen(
                    onManageItemsClick = {
                        navController.navigate(Screen.StandTypesList.route)
                    },
                    onStandAssignmentVisitorClick = {
                        navController.navigate(Screen.AssignedStandsVisitorList.route)
                    },
                    onStandAssignmentOrganizationClick = {
                        navController.navigate(Screen.AssignedStandsOrganizationList.route)
                    }
                )
            }
        }

    }

    // لیست استندها
    composable(Screen.StandTypesList.route,
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


        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomHeader(
                title = stringResource(
                    R.string.label_managing_stands_items
                ),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )
            Box(
                modifier = Modifier.fillMaxSize()
            ) {

                StandTypesListScreen(
                    onAddItemClick = {
                        navController.navigate(
                            Screen.AddStandTypes.createRoute(Screen.AddStandTypes.NEW_ITEM_ID)
                        )
                    },
                    onEditItemClick = { item ->
                        navController.navigate(Screen.AddStandTypes.createRoute(item.standTypeId))
                    },
                    onDeleteItemClick = { /* لاجیک حذف در لیست یا ViewModel هندل می‌شود */ }
                )
            }
        }


    }

    //  افزودن یا ویرایش یک استند
    composable(
        route = Screen.AddStandTypes.route,
        arguments = listOf(
            navArgument(Screen.AddStandTypes.ITEM_ID_ARGUMENT) {
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
    ) { backStackEntry ->
        val itemId =
            backStackEntry.arguments?.getString(Screen.AddStandTypes.ITEM_ID_ARGUMENT)
                ?: Screen.AddStandTypes.NEW_ITEM_ID
        val isEditMode = itemId != Screen.AddStandTypes.NEW_ITEM_ID

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomHeader(
                title = if (isEditMode) R.string.label_edit_stand else R.string.label_add_new_stand,

                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )
            Box(
                modifier = Modifier.fillMaxSize()
            ) {

                AddStandTypeScreen(
                    itemId = itemId, onSaveClick = { _ ->
                        navController.popBackStack()
                    }
                )
            }
        }

    }

    // لیست تخصیص‌های استند به ویزیتورها
    composable(Screen.AssignedStandsVisitorList.route, enterTransition = {
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

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomHeader(
                title = stringResource(
                    R.string.label_assignment_advertising_stand
                ),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                AssignedStandsVisitorListScreen(
                    onNewAssignmentVisitorClick = {
                        navController.navigate(Screen.AddAssignedStandsVisitorVisitor.route)
                    },
                    onViewItemDetailsClick = { assignment ->
                        navController.navigate(
                            Screen.AssignedStandsDetail.createRoute(assignment.assignedStand.assignedStandId)
                        )
                    }
                )
            }
        }
    }

    // ثبت تخصیص جدید به ویزیتور
    composable(Screen.AddAssignedStandsVisitorVisitor.route, enterTransition = {
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

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomHeader(
                title = stringResource(
                    R.string.label_allocation_record
                ),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                AddAssignedStandsVisitorScreen(
                    onSaveClick = { navController.popBackStack() }
                )
            }
        }


    }

    // لیست تخصیص‌های استند به سازمان‌ها
    composable(Screen.AssignedStandsOrganizationList.route, enterTransition = {
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


        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomHeader(
                title = stringResource(
                    R.string.label_assignment_advertising_stand
                ),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                AssignedStandsOrganizationListScreen(
                    onNewAssignmentOrganizationClick = {
                        navController.navigate(Screen.AddAssignedStandsOrganization.createRoute())
                    },
                    onViewItemDetailsClick = { assignment ->
                        navController.navigate(
                            Screen.AssignedStandsDetail.createRoute(assignment.assignedStand.assignedStandId)
                        )
                    }
                )
            }
        }

    }

    //ثبت تخصیص به سازمان
    composable(
        route = Screen.AddAssignedStandsOrganization.route,
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
                    R.string.label_allocation_record
                ),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )
            Box(
                modifier = Modifier.fillMaxSize()
            ) {

                AddAssignedStandsOrganizationScreen(
                    preselectedOrganizationId = organizationId,
                    onSaveClick = { navController.popBackStack() }
                )
            }
        }


    }

    // مشاهده جزئیات تخصیص (سازمانی یا ویزیتوری)
    composable(
        route = Screen.AssignedStandsDetail.route,
        arguments = listOf(
            navArgument("assignedStandId") { type = NavType.StringType }
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
                    R.string.label_assignment_details
                ),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )
            Box(
                modifier = Modifier.fillMaxSize()
            ) {

                AssignedStandsDetailRoute()
            }
        }



    }
}
