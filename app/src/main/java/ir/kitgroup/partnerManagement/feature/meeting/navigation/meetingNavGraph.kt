package ir.kitgroup.partnerManagement.feature.meeting.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.navigation.navigation
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.feature.home.navigation.BottomNavItem
import ir.kitgroup.partnerManagement.feature.meeting.ui.add.AddEditMeetingScreen
import ir.kitgroup.partnerManagement.feature.meeting.ui.detail.MeetingDetailScreen
import ir.kitgroup.partnerManagement.feature.meeting.ui.list.MeetingsListScreen
import ir.kitgroup.partnerManagement.navigation.Screen

fun NavGraphBuilder.meetingNavGraph(
    navController: NavController
) {
    navigation(
        startDestination = BottomNavItem.Meetings.route,
        route = "meeting_graph" // هماهنگ با isMainSection در AppNavigation
    ) {
        // =====================================================
        // Meetings - Main Tab
        // =====================================================
        composable(
            route = BottomNavItem.Meetings.route,
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
            val appColors = LocalPartnerManagementColors.current
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(appColors.screenBackground)
            ) {
                CustomHeader(
                    title = stringResource(R.string.nav_meetings),
                    showBackButton = false
                )

                MeetingsListScreen(
                    onMeetingClick = { meetingId ->
                        navController.navigate(
                            Screen.MeetingDetail.createRoute(meetingId)
                        )
                    },
                    onAddMeetingClick = {
                        navController.navigate(
                            Screen.AddMeeting.createRoute()
                        )
                    },
                    onEditMeetingClick = { meetingId ->
                        navController.navigate(
                            Screen.AddMeeting.createRoute(meetingId)
                        )
                    }
                )
            }
        }

        // =====================================================
        // Meeting Detail
        // =====================================================
        composable(
            route = Screen.MeetingDetail.route,
            arguments = listOf(
                navArgument("meetingId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            ),
            enterTransition = { fadeIn(tween(250)) },
            exitTransition = { fadeOut(tween(200)) },
            popEnterTransition = { fadeIn(tween(250)) },
            popExitTransition = { fadeOut(tween(200)) }
        ) { backStackEntry ->
            val meetingId = backStackEntry.arguments?.getString("meetingId")

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                CustomHeader(
                    title = stringResource(
                        R.string.label_meeting_details
                    ),
                    showBackButton = true,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MeetingDetailScreen(
                        meetingId = meetingId.orEmpty(),
                        onEditClick = { id ->
                            // اصلاح مسیر به AddMeeting که از قبل در همین گراف تعریف شده است
                            navController.navigate(
                                Screen.AddMeeting.createRoute(id)
                            )
                        },
                        onStatusChanged = {
                            navController.popBackStack(
                                BottomNavItem.Dashboard.route,
                                inclusive = false
                            )
                        },
                    )
                }
            }

        }

        // =====================================================
        // Add / Edit Meeting
        // =====================================================
        composable(
            route = Screen.AddMeeting.route,
            arguments = listOf(
                navArgument("meetingId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("preselectedOrganizationId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            ),
            enterTransition = { fadeIn(tween(250)) },
            exitTransition = { fadeOut(tween(200)) },
            popEnterTransition = { fadeIn(tween(250)) },
            popExitTransition = { fadeOut(tween(200)) }
        ) {
            AddEditMeetingScreen(
                onBackClick = {
                    navController.popBackStack()
                }, onSaved = {
                    navController.popBackStack(
                        BottomNavItem.Dashboard.route,
                        inclusive = false
                    )
                }
            )
        }
    }
}
