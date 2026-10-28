package ir.kitgroup.partnerManagement.feature.dashboard.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.model.MeetingWithDetail
import ir.kitgroup.partnerManagement.core.ui.SessionViewModel
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.EmptyState
import ir.kitgroup.partnerManagement.core.ui.components.OfflinePendingFooterBanner
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.*
import ir.kitgroup.partnerManagement.core.ui.util.*
import ir.kitgroup.partnerManagement.feature.dashboard.model.SummaryCardData
import ir.kitgroup.partnerManagement.feature.home.navigation.BottomNavItem
import ir.kitgroup.partnerManagement.feature.meeting.ui.list.MeetingCard
import ir.kitgroup.partnerManagement.feature.meeting.ui.list.MeetingTypeChip
import ir.kitgroup.partnerManagement.navigation.Screen

@Composable
fun DashboardScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    sessionViewModel: SessionViewModel = hiltViewModel(),
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
    onSyncDataClick: () -> Unit = {},
    onUploadDataClick: () -> Unit = {}
) {
    val todayMeetings by dashboardViewModel.todayMeetings.collectAsStateWithLifecycle()
    val statistics by dashboardViewModel.statistics.collectAsStateWithLifecycle()

    val roleCode by sessionViewModel.roleCode.collectAsState()
    val currentRole = remember(roleCode) { UserRole.fromCode(roleCode.toString()) }
    val isSupervisor = currentRole == UserRole.SUPERVISOR

    val syncState by dashboardViewModel.syncState.collectAsStateWithLifecycle()
    val uploadState by dashboardViewModel.uploadState.collectAsStateWithLifecycle()

    val isSyncing = syncState is DataState.Loading
    val isUploading = uploadState is DataState.Loading
    val isBusy = isSyncing || isUploading

    val pendingUploadCount by dashboardViewModel.pendingUploadCount.collectAsStateWithLifecycle()
    val hasPendingUpload = pendingUploadCount > 0

    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    val currentBackStackEntry = remember(navController) {
        navController.getBackStackEntry(BottomNavItem.Dashboard.route)
    }


    LaunchedEffect(dashboardViewModel.uiEvent) {
        dashboardViewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowMessage -> snackBarHostState.showSnackbar(
                    event.message.asString(
                        context
                    )
                )

                is UiEvent.ShowError -> snackBarHostState.showSnackbar(event.error.asString(context))
            }
        }
    }

    val fullName by sessionViewModel.fullName.collectAsState()
    val userFullName = remember(fullName) {
        if (!fullName.isNullOrBlank()) {
            context.getString(R.string.label_welcome_user, fullName!!.trim())
        } else {
            context.getString(R.string.nav_dashboard)
        }
    }

    val summaryItems = listOf(
        SummaryCardData(
            icon = Icons.Filled.Domain,
            iconColor = Orange,
            value = (statistics?.totalVisits ?: 0).toString(),
            title = stringResource(R.string.label_total_meetings),
            accent = Orange
        ),
        SummaryCardData(
            icon = Icons.Filled.Inventory2,
            iconColor = Blue,
            value = (statistics?.totalAssignedStands ?: 0).toString(),
            title = stringResource(R.string.label_total_assigned_stands),
            accent = Blue
        ),
        SummaryCardData(
            icon = Icons.Filled.ConfirmationNumber,
            iconColor = Purple,
            value = (statistics?.totalAssignedSerials ?: 0).toString(),
            title = stringResource(R.string.label_total_assigned_serials),
            accent = Purple
        ),
        SummaryCardData(
            icon = Icons.Filled.AssignmentTurnedIn,
            iconColor = Green,
            value = (statistics?.activeContractsCount ?: 0).toString(),
            title = stringResource(R.string.label_total_active_contracts_count),
            accent = Green
        ),
        SummaryCardData(
            icon = Icons.Filled.Warning,
            iconColor = Red,
            value = (statistics?.totalWarningsRegistered ?: 0).toString(),
            title = stringResource(R.string.label_total_warnings_registered),
            accent = Red
        )
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            CustomHeader(
                title = userFullName,
                showBackButton = false
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // کارت سینک
                item {
                    UnifiedSyncCard(
                        hasPendingUpload = hasPendingUpload,
                        pendingCount = pendingUploadCount,
                        isLoading = isBusy,
                        onClick = {
                            if (hasPendingUpload) {
                                onUploadDataClick()
                                dashboardViewModel.onSendDataClick()
                            } else {
                                onSyncDataClick()
                                dashboardViewModel.onReceiveDataClick()
                            }
                        }
                    )
                }

                // دسترسی سریع اصلی
                item {
                    QuickActionsSection(
                        onQuickMeetingClick = {
                            dashboardViewModel.checkCanNavigateToAddMeeting {
                                navController.navigate(Screen.AddMeeting.createRoute())
                            }
                        },
                        onQuickOrganizationClick = {
                            dashboardViewModel.checkCanNavigateToAddOrganization {
                                navController.navigate(Screen.AddOrganization.route)
                            }
                        }
                    )
                }

                // خلاصه وضعیت آماری
                item {
                    //  SummaryGridSection(summaryItems)
                    SummaryRowSection(summaryItems)
                }

                // ماژول‌های سیستم
                item {
                    QuickAccessSection(
                        onContractClick = { navController.navigate(Screen.ContractsList.route) },
                        onAdvertisingClick = {
                            if (isSupervisor) {
                                navController.navigate(Screen.AdvertisingStandMenu.route)
                            } else {
                                navController.navigate(Screen.AssignedStandsOrganizationList.route)
                            }
                        },
                        onReportClick = { navController.navigate(Screen.ReportMenu.route) },
                        onMapClick = { navController.navigate(Screen.Map.route) }
                    )
                }

                item {

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        SectionTitle(
                            stringResource(R.string.label_schedule_title),
                            Icons.Default.Analytics,
                            titleColor = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        if (todayMeetings.isEmpty()) {
                            EmptyState(
                                textRes = R.string.msg_no_meeting_register_today,
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                todayMeetings.forEach { meeting ->
                                    MeetingCard(
                                        item = meeting,
                                        isSupervisor = isSupervisor,
                                        onClick = {
                                            navController.navigate(
                                                Screen.MeetingDetail.createRoute(
                                                    meetingId = meeting.meeting.meetingId
                                                )
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }

                }

            }
        }
    }
}

@Composable
private fun UnifiedSyncCard(
    hasPendingUpload: Boolean,
    pendingCount: Int,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "syncRotation")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val containerGradient = if (hasPendingUpload) {
        listOf(
            PartnerManagementTheme.colors.warningContainer.copy(alpha = 0.95f),
            PartnerManagementTheme.colors.warningContainer.copy(alpha = 0.70f)
        )
    } else {
        listOf(
            MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
            MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
        )
    }

    val iconContainerColor = if (hasPendingUpload) {
        PartnerManagementTheme.colors.onWarningContainer.copy(alpha = 0.15f)
    } else {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
    }

    val accentContentColor = if (hasPendingUpload) {
        PartnerManagementTheme.colors.onWarningContainer
    } else {
        MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = !isLoading,
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ), shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            1.2.dp,
            if (hasPendingUpload) PartnerManagementTheme.colors.onWarningContainer.copy(alpha = 0.4f)
            else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (hasPendingUpload) 4.dp else 1.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(containerGradient))
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(iconContainerColor),
                    contentAlignment = Alignment.Center
                ) {
                    val iconModifier = if (isLoading) Modifier.rotate(rotationAngle) else Modifier
                    Icon(
                        imageVector = if (hasPendingUpload) Icons.Default.CloudUpload else Icons.Default.Sync,
                        contentDescription = null,
                        tint = accentContentColor,
                        modifier = iconModifier.size(26.dp)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (hasPendingUpload) {
                                stringResource(R.string.msg_sync_changes_to_server)
                            } else {
                                stringResource(R.string.label_receive_data)
                            },
                            style = typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        AnimatedVisibility(
                            visible = hasPendingUpload && pendingCount > 0,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Red,
                                shadowElevation = 2.dp
                            ) {
                                Text(
                                    text = stringResource(
                                        R.string.format_record_count,
                                        pendingCount
                                    ),
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (hasPendingUpload) {
                            stringResource(R.string.desc_offline_data_exists)
                        } else {
                            stringResource(R.string.label_update_data)
                        },
                        style = typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun QuickActionsSection(
    onQuickMeetingClick: () -> Unit,
    onQuickOrganizationClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickActionCard(
            title = stringResource(R.string.label_add_new_organization),
            icon = Icons.Filled.AddBusiness,
            gradient = listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
            ),
            onClick = onQuickOrganizationClick,
            modifier = Modifier.weight(1f)
        )
        QuickActionCard(
            title = stringResource(R.string.label_register_visit),
            icon = Icons.Filled.AddLocationAlt,
            gradient = listOf(Color(0xFF2E7D32), Color(0xFF43A047)),
            onClick = onQuickMeetingClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    icon: ImageVector,
    gradient: List<Color>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(76.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.horizontalGradient(gradient))
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    style = typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * بخش خلاصه وضعیت آماری در قالب یک ردیف افقی با اسکرول نرم
 */
@Composable
private fun SummaryRowSection(
    summaryItems: List<SummaryCardData>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SectionTitle(
            stringResource(R.string.label_summary_title), Icons.Default.Analytics,
            titleColor = MaterialTheme.colorScheme.primary
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(
                items = summaryItems,
                key = { it.title }
            ) { item ->
                CompactSummaryCard(
                    item = item,
                    modifier = Modifier.width(145.dp)
                )
            }
        }
    }
}

/**
 * کارت آماری کامپکت با جلوه بصری مدرن و حداقل اشغال فضا
 */
@Composable
private fun CompactSummaryCard(
    item: SummaryCardData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(96.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, item.accent.copy(alpha = 0.22f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            item.accent.copy(alpha = 0.06f),
                            Color.Transparent
                        )
                    )
                )
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(item.accent.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = item.accent,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = item.value,
                    style = typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = item.title,
                style = typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
fun QuickAccessSection(
    onContractClick: () -> Unit,
    onAdvertisingClick: () -> Unit,
    onReportClick: () -> Unit,
    onMapClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SectionTitle(
            stringResource(R.string.label_quick_access), Icons.Default.DashboardCustomize,
            titleColor = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickAccessTile(
                title = stringResource(R.string.label_contract_organization),
                icon = Icons.Filled.Inventory2,
                tint = PartnerManagementTheme.colors.successContainer,
                onTint = PartnerManagementTheme.colors.onSuccessContainer,
                onClick = onContractClick,
                modifier = Modifier.weight(1f)
            )
            QuickAccessTile(
                title = stringResource(R.string.label_advertising_stands),
                icon = Icons.Filled.Storefront,
                tint = PartnerManagementTheme.colors.warningContainer,
                onTint = PartnerManagementTheme.colors.onWarningContainer,
                onClick = onAdvertisingClick,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickAccessTile(
                title = stringResource(R.string.label_reports),
                icon = Icons.Filled.Assessment,
                tint = PartnerManagementTheme.colors.purpleContainer,
                onTint = PartnerManagementTheme.colors.onPurpleContainer,
                onClick = onReportClick,
                modifier = Modifier.weight(1f)
            )
            QuickAccessTile(
                title = stringResource(R.string.label_map),
                icon = Icons.Filled.Map,
                tint = PartnerManagementTheme.colors.infoContainer,
                onTint = PartnerManagementTheme.colors.onInfoContainer,
                onClick = onMapClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun QuickAccessTile(
    title: String,
    icon: ImageVector,
    tint: Color,
    onTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(86.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = tint.copy(alpha = 0.65f)),
        border = BorderStroke(1.dp, onTint.copy(alpha = 0.15f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = onTint,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                style = typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
