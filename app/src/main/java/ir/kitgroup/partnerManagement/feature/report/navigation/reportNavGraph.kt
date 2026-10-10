package ir.kitgroup.partnerManagement.feature.report.navigation

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
import ir.kitgroup.partnerManagement.feature.report.ui.ReportMenuScreen
import ir.kitgroup.partnerManagement.feature.report.ui.organization.OrganizationPerformanceScreen
import ir.kitgroup.partnerManagement.feature.report.ui.organization.ReportContractScreen
import ir.kitgroup.partnerManagement.feature.report.ui.visitor.VisitorAnalysisScreen
import ir.kitgroup.partnerManagement.feature.report.ui.visitor.VisitorListScreen
import ir.kitgroup.partnerManagement.feature.report.ui.visitor.VisitorsPerformanceDashboardScreen
import ir.kitgroup.partnerManagement.navigation.Screen


fun NavGraphBuilder.reportNavGraph(navController: NavController) {

    composable(Screen.ReportMenu.route,
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
                    R.string.label_reports
                ),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                ReportMenuScreen(
                    onVisitorClick = {
                        navController.navigate(Screen.VisitorList.route)
                    },
                    onReportContractClick = {
                        navController.navigate(Screen.ReportContract.route)
                    },
                    onCollectionPerformanceClick = {
                        navController.navigate(Screen.OrganizationPerformance.route)
                    }
                )
            }
        }

    }

    composable(
        route = Screen.VisitorList.route,
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
                    R.string.label_visitor_list
                ),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                VisitorListScreen(
                    onReportItemClick = {
                        navController.navigate(Screen.VisitorsPerformance.route)
                    },
                    onViewItemDetailsClick = { visitor ->
                        navController.navigate("visitor_analysis/${visitor.visitorId}")
                    })
            }
        }


    }

    composable(route = Screen.VisitorsPerformance.route,
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
                    R.string.label_visitors_performance_analysis
                ),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                VisitorsPerformanceDashboardScreen(
                )
            }
        }


    }

    composable(
        route = Screen.ReportContract.route,
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
                    R.string.label_report_cooperation_agreement
                ),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                ReportContractScreen(
                )
            }
        }

    }

    composable(
        route = Screen.OrganizationPerformance.route,
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
                    R.string.label_organizations_report
                ),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                OrganizationPerformanceScreen()
            }
        }

    }

    composable(
        route = "visitor_analysis/{visitorId}",
        arguments = listOf(navArgument("visitorId") { type = NavType.StringType })
    ) { backStackEntry ->
        val visitorId = backStackEntry.arguments?.getString("visitorId") ?: ""

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomHeader(
                title = stringResource(
                    R.string.label_visitor_performance_analysis
                ),
                showBackButton = true,
                onBackClick = {
                    navController.popBackStack()
                }
            )

            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                VisitorAnalysisScreen(
                    visitorId = visitorId,
                    onCallClick = {
                        // لانچ کردن Intent تماس سیستمی با بازاریاب
                    }
                )
            }
        }
    }
}
