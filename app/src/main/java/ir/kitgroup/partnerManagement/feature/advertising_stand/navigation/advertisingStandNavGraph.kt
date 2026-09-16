package ir.kitgroup.partnerManagement.feature.advertising_stand.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.AdvertisingStandMenuScreen
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.detail.AdvertisingStandAssignmentVisitorDetailScreen
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.detail.demoAssignmentDetail
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand.AddAdvertisingStandScreen
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand.AdvertisingStandsListScreen
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand_assignment_organization.AddAdvertisingStandAssignmentOrganizationScreen
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand_assignment_organization.AdvertisingStandAssignmentOrganizationListScreen
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand_assignment_visitor.AddAdvertisingStandAssignmentVisitorScreen
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand_assignment_visitor.AdvertisingStandAssignmentVisitorListScreen
import ir.kitgroup.partnerManagement.navigation.Screen


fun NavGraphBuilder.advertisingStandNavGraph(navController: NavController) {

    // منوی اصلی بخش استندهای تبلیغاتی
    composable(Screen.AdvertisingStandMenu.route) {
        AdvertisingStandMenuScreen(
            onBackClick = { navController.popBackStack() },
            onManageItemsClick = {
                navController.navigate(Screen.AdvertisingStandsList.route)
            },
            onStandAssignmentVisitorClick = {
                navController.navigate(Screen.AdvertisingStandAssignmentVisitorList.route)
            },
            onStandAssignmentOrganizationClick = {
                navController.navigate(Screen.AdvertisingStandAssignmentOrganizationList.route)
            }
        )
    }

    // لیست استندها
    composable(Screen.AdvertisingStandsList.route) {
        AdvertisingStandsListScreen(
            onBackClick = { navController.popBackStack() },
            onAddItemClick = {
                navController.navigate(
                    Screen.AdvertisingStandDetail.createRoute(Screen.AdvertisingStandDetail.NEW_ITEM_ID)
                )
            },
            onEditItemClick = { item ->
                navController.navigate(Screen.AdvertisingStandDetail.createRoute(item.id))
            },
            onDeleteItemClick = { /* لاجیک حذف در لیست یا ViewModel هندل می‌شود */ }
        )
    }

    //  افزودن یا ویرایش یک استند
    composable(
        route = Screen.AdvertisingStandDetail.route,
        arguments = listOf(
            navArgument(Screen.AdvertisingStandDetail.ITEM_ID_ARGUMENT) {
                type = NavType.StringType
            }
        )
    ) { backStackEntry ->
        val itemId = backStackEntry.arguments?.getString(Screen.AdvertisingStandDetail.ITEM_ID_ARGUMENT)
            ?: Screen.AdvertisingStandDetail.NEW_ITEM_ID

        AddAdvertisingStandScreen(
            itemId = itemId,
            onBackClick = { navController.popBackStack() },
            onSaveClick = { _ ->
                navController.popBackStack()
            }
        )
    }

    // لیست تخصیص‌های استند به ویزیتورها
    composable(Screen.AdvertisingStandAssignmentVisitorList.route) {
        AdvertisingStandAssignmentVisitorListScreen(
            onBackClick = { navController.popBackStack() },
            onNewAssignmentVisitorClick = {
                navController.navigate(Screen.AddAdvertisingStandAssignmentVisitor.route)
            },
            onViewItemDetailsClick = { assignment ->
                navController.navigate(
                    Screen.AdvertisingStandAssignmentVisitorDetail.createRoute(assignment.id)
                )
            }
        )
    }

    // ثبت تخصیص جدید به ویزیتور
    composable(Screen.AddAdvertisingStandAssignmentVisitor.route) {
        AddAdvertisingStandAssignmentVisitorScreen(
            onBackClick = { navController.popBackStack() },
            onSaveClick = { navController.popBackStack() }
        )
    }

    // لیست تخصیص‌های استند به سازمان‌ها
    composable(Screen.AdvertisingStandAssignmentOrganizationList.route) {
        AdvertisingStandAssignmentOrganizationListScreen(
            onBackClick = { navController.popBackStack() },
            onNewAssignmentOrganizationClick = {
                navController.navigate(Screen.AddAdvertisingStandAssignmentOrganization.createRoute())
            },
            onViewItemDetailsClick = { assignment ->
                navController.navigate(
                    Screen.AdvertisingStandAssignmentVisitorDetail.createRoute(assignment.id)
                )
            }
        )
    }

    //ثبت تخصیص به سازمان
    composable(
        route = Screen.AddAdvertisingStandAssignmentOrganization.route,
        arguments = listOf(
            navArgument("organizationId") {
                type = NavType.IntType
                defaultValue = -1
            }
        )
    ) { backStackEntry ->
        val orgId = backStackEntry.arguments?.getInt("organizationId")?.takeIf { it != -1 }

        AddAdvertisingStandAssignmentOrganizationScreen(
            preselectedOrganizationId = orgId,
            onBackClick = { navController.popBackStack() },
            onSaveClick = { navController.popBackStack() }
        )
    }

    // مشاهده جزئیات تخصیص (سازمانی یا ویزیتوری)
    composable(
        route = Screen.AdvertisingStandAssignmentVisitorDetail.route,
        arguments = listOf(
            navArgument("assignmentId") { type = NavType.StringType }
        )
    ) {
        AdvertisingStandAssignmentVisitorDetailScreen(
            allocation = demoAssignmentDetail,
            onBackClick = { navController.popBackStack() }
        )
    }
}
