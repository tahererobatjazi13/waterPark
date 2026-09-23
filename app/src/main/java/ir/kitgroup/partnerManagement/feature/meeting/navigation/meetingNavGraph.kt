package ir.kitgroup.partnerManagement.feature.meeting.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.navArgument
import ir.kitgroup.partnerManagement.feature.home.navigation.BottomNavItem
import ir.kitgroup.partnerManagement.feature.meeting.ui.AddMeetingScreen
import ir.kitgroup.partnerManagement.feature.meeting.ui.MeetingDetailScreen
import ir.kitgroup.partnerManagement.feature.meeting.ui.MeetingsListScreen
import ir.kitgroup.partnerManagement.navigation.Screen

fun NavGraphBuilder.meetingNavGraph(navController: NavController) {

    navigation(
        startDestination = BottomNavItem.Meetings.route,
        route = "Visit_graph"
    ) {
        // لیست بازدید ها
        composable(BottomNavItem.Meetings.route) {
            MeetingsListScreen(
                onMeetingClick = { meetingId ->
                    navController.navigate(Screen.MeetingDetail.createRoute(meetingId))
                },
                onAddMeetingClick = {
                    navController.navigate(Screen.AddMeeting.createRoute())
                },
                onEditMeetingClick = { meetingId ->
                    navController.navigate(Screen.AddMeeting.createRoute(meetingId))
                }
            )
        }


        // صفحه جزئیات بازدید
        composable(
            route = Screen.MeetingDetail.route,
            arguments = listOf(
                navArgument("meetingId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val meetingId = backStackEntry.arguments?.getString("meetingId")

            MeetingDetailScreen(
                meetingId = meetingId!!,
                onBackClick = { navController.popBackStack() }
            )
        }
        // صفحه ثبت / ویرایش بازدید
        composable(
            route = Screen.AddMeeting.route,
            arguments = listOf(
                navArgument("meetingId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("organizationId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val meetingId = backStackEntry.arguments?.getString("meetingId")
            val organizationId = backStackEntry.arguments?.getString("organizationId")

            AddMeetingScreen(
                meetingId = meetingId,
                preselectedOrganizationId = organizationId,
                onBackClick = { navController.popBackStack() },
                onSubmitClick = { navController.popBackStack() }
            )
        }
    }
}