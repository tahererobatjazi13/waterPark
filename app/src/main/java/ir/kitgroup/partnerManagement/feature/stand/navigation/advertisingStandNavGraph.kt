package ir.kitgroup.partnerManagement.feature.stand.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
    composable(Screen.AdvertisingStandMenu.route) {
        AdvertisingStandMenuScreen(
            onBackClick = { navController.popBackStack() },
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

    // لیست استندها
    composable(Screen.StandTypesList.route) {
        StandTypesListScreen(
            onBackClick = { navController.popBackStack() },
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

    //  افزودن یا ویرایش یک استند
    composable(
        route = Screen.AddStandTypes.route,
        arguments = listOf(
            navArgument(Screen.AddStandTypes.ITEM_ID_ARGUMENT) {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->
        val itemId =
            backStackEntry.arguments?.getString(Screen.AddStandTypes.ITEM_ID_ARGUMENT)
                ?: Screen.AddStandTypes.NEW_ITEM_ID

        AddStandTypeScreen(
            itemId = itemId,
            onBackClick = { navController.popBackStack() },
            onSaveClick = { _ ->
                navController.popBackStack()
            }
        )
    }

    // لیست تخصیص‌های استند به ویزیتورها
    composable(Screen.AssignedStandsVisitorList.route) {
        AssignedStandsVisitorListScreen(
            onBackClick = { navController.popBackStack() },
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

    // ثبت تخصیص جدید به ویزیتور
    composable(Screen.AddAssignedStandsVisitorVisitor.route) {
        AddAssignedStandsVisitorScreen(
            onBackClick = { navController.popBackStack() },
            onSaveClick = { navController.popBackStack() }
        )
    }

    // لیست تخصیص‌های استند به سازمان‌ها
    composable(Screen.AssignedStandsOrganizationList.route) {
        AssignedStandsOrganizationListScreen(
            onBackClick = { navController.popBackStack() },
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

    //ثبت تخصیص به سازمان
    composable(
        route = Screen.AddAssignedStandsOrganization.route,
        arguments = listOf(
            navArgument("organizationId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) { backStackEntry ->
        val organizationId = backStackEntry.arguments?.getString("organizationId")

        AddAssignedStandsOrganizationScreen(
            preselectedOrganizationId = organizationId,
            onBackClick = { navController.popBackStack() },
            onSaveClick = { navController.popBackStack() }
        )
    }

    // مشاهده جزئیات تخصیص (سازمانی یا ویزیتوری)
    composable(
        route = Screen.AssignedStandsDetail.route,
        arguments = listOf(
            navArgument("assignedStandId") { type = NavType.StringType }
        )
    ) {
        AssignedStandsDetailRoute(
            onBackClick = { navController.popBackStack() }
        )
    }
}
