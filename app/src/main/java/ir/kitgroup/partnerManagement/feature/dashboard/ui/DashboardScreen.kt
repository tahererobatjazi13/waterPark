package ir.kitgroup.partnerManagement.feature.dashboard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.theme.*
import ir.kitgroup.partnerManagement.core.ui.util.SideCurvedHeaderShape
import ir.kitgroup.partnerManagement.feature.dashboard.model.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import ir.kitgroup.partnerManagement.core.ui.components.LocationRow
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import ir.kitgroup.partnerManagement.core.ui.SessionViewModel
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.util.UserRole
import ir.kitgroup.partnerManagement.navigation.Screen
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.core.database.entity.MeetingEntity
import ir.kitgroup.partnerManagement.core.ui.util.DataState
import ir.kitgroup.partnerManagement.core.ui.util.MeetingType
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationStatus
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.demoMeetings
import ir.kitgroup.partnerManagement.feature.meeting.ui.MeetingTypeChip

@Composable
fun DashboardScreen(
    navController: NavController,
    sessionViewModel: SessionViewModel = hiltViewModel(),
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
    onSyncDataClick: () -> Unit = {},
    onUploadDataClick: () -> Unit = {}
) {

    val role by sessionViewModel.userRole.collectAsState()
    val isSupervisor = role == UserRole.SUPERVISOR.name

    val syncState by dashboardViewModel.syncState.collectAsStateWithLifecycle()
    val isSyncing = syncState is DataState.Loading
    var isUploading by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }


    val context = LocalContext.current

    LaunchedEffect(dashboardViewModel.uiEvent) {
        dashboardViewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowMessage -> {
                    val message = event.message.asString(context)
                    snackbarHostState.showSnackbar(message)
                }

                is UiEvent.ShowError -> {
                    val error = event.error.asString(context)
                    snackbarHostState.showSnackbar(error)
                }
            }
        }
    }


    val summaryItems = listOf(
        SummaryCardData(
            Icons.Filled.Domain,
            Orange,
            "34",
            stringResource(R.string.label_total_views_today),
            Orange
        ),
        SummaryCardData(
            Icons.Filled.Person,
            Green,
            "18",
            stringResource(R.string.label_in_person_visits),
            Green
        ),
        SummaryCardData(
            Icons.Filled.Call,
            Blue,
            "16",
            stringResource(R.string.label_phone_calls),
            Blue
        )
    )



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        DashboardHeader()
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // بخش عملیات همگام‌سازی و سینک داده‌ها
                item {
                    DataSyncSection(
                        isSyncing = isSyncing,
                        isUploading = isUploading,
                        onSyncClick = dashboardViewModel::onReceiveDataClick,
                        onUploadClick = dashboardViewModel::onSendDataClick
                    )
                }

                item { SummarySection(summaryItems) }
                item { Spacer(Modifier.width(6.dp)) }
                item {
                    QuickActionsSection(
                        onQuickContractClick = { navController.navigate(Screen.AddContract.route) },
                        onQuickOrganizationClick = { navController.navigate(Screen.AddOrganization.route) },
                        onQuickMeetingClick = { navController.navigate(Screen.AddMeeting.createRoute()) },
                        onQuickContractOfferClick = { navController.navigate(Screen.AddContractOffer.route) }
                    )
                }
                item { Spacer(Modifier.width(6.dp)) }
                item {
                    QuickAccessSection(
                        onContractClick = { navController.navigate(Screen.ContractsList.route) },
                        onAdvertisingClick = {
                            if (isSupervisor) {
                                navController.navigate(Screen.AdvertisingStandMenu.route)
                            } else {
                                navController.navigate(
                                    Screen.AdvertisingStandAssignmentOrganizationList.route
                                )
                            }
                        },
                        onReportClick = { navController.navigate(Screen.ReportMenu.route) },
                        onMapClick = {
                            navController.navigate(
                                Screen.Map.route
                            )
                        }
                    )
                }
                item { Spacer(Modifier.width(6.dp)) }
                item {
                    SectionTitle(
                        stringResource(R.string.label_schedule_title),
                        Icons.Default.Schedule
                    )
                }
                items(
                    items = demoMeetings,
                    key = { it.meetingId }
                ) { item ->
                    val isEditableScheduledPhysicalVisit =
                        item.status == 1

                    VisitCard(
                        item = item,
                        isSupervisor = isSupervisor,
                        onClick = {
                            if (isEditableScheduledPhysicalVisit) {
                                navController.navigate(
                                    Screen.AddMeeting.createRoute(
                                        meetingId = item.meetingId
                                    )
                                )
                            } else {
                                navController.navigate(
                                    Screen.MeetingDetail.createRoute(
                                        meetingId = item.meetingId
                                    )
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DashboardHeader(
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = SideCurvedHeaderShape(50f)
            )
            .windowInsetsPadding(WindowInsets.statusBars)
            .height(64.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.greeting_text),
            color = MaterialTheme.colorScheme.onPrimary,
            style = typography.titleLarge
        )
        //  NotificationIcon()
    }
}


@Composable
private fun DataSyncSection(
    isSyncing: Boolean,
    isUploading: Boolean,
    onSyncClick: () -> Unit,
    onUploadClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SyncActionCardButton(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_receive_data),
                subtitle = stringResource(R.string.label_update_data),
                icon = Icons.Default.Sync,
                accentColor = PartnerManagementTheme.colors.infoContainer,
                iconTint = PartnerManagementTheme.colors.onInfoContainer,
                isLoading = isSyncing,
                onClick = onSyncClick
            )

            SyncActionCardButton(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_send_data),
                subtitle = stringResource(R.string.label_transfer_changes),
                icon = Icons.Default.CloudUpload,
                accentColor = PartnerManagementTheme.colors.successContainer,
                iconTint = PartnerManagementTheme.colors.onSuccessContainer,
                isLoading = isUploading,
                onClick = onUploadClick
            )
        }
    }
}

@Composable
private fun SyncActionCardButton(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    iconTint: Color,
    isLoading: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        enabled = !isLoading,
        modifier = modifier.height(64.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(accentColor),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = iconTint
                    )
                } else {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    style = typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun NotificationIcon() {
    Box(
        modifier = Modifier.size(32.dp),
        contentAlignment = Alignment.Center
    ) {

        Icon(
            painter = painterResource(R.drawable.ic_bell),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(24.dp)
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(4.dp, (-4).dp)
                .size(16.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.error),
            contentAlignment = Alignment.Center
        ) {
            Text("9", color = MaterialTheme.colorScheme.onError, fontSize = 9.sp)
        }
    }
}


@Composable
private fun SummarySection(items: List<SummaryCardData>) {

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        SectionTitle(stringResource(R.string.label_summary_title))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            items.forEach {
                SummaryCard(it, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun SummaryCard(data: SummaryCardData, modifier: Modifier) {
    Card(
        modifier = modifier.height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = data.accent.copy(alpha = 0.08f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(data.iconColor.copy(.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(data.icon, null, tint = data.iconColor)
            }

            Text(
                data.value,
                color = MaterialTheme.colorScheme.onSurface,
                style = typography.displayMedium
            )

            Text(
                data.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = typography.labelMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(20))
                    .background(data.accent)
            )
        }
    }
}


@Composable
private fun QuickActionsSection(
    onQuickContractClick: () -> Unit,
    onQuickOrganizationClick: () -> Unit,
    onQuickMeetingClick: () -> Unit,
    onQuickContractOfferClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SectionTitle(stringResource(R.string.label_shortcut_title))

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickActionCard(
                title = stringResource(R.string.label_contract),
                icon = Icons.Filled.AssignmentTurnedIn,
                onClick = onQuickContractClick,
                modifier = Modifier.weight(1f)
            )

            QuickActionCard(
                title = stringResource(R.string.label_add_new_organization),
                icon = Icons.Filled.AddBusiness,
                onClick = onQuickOrganizationClick,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickActionCard(
                title = stringResource(R.string.label_register_visit),
                icon = Icons.Filled.FactCheck,
                onClick = onQuickMeetingClick,
                modifier = Modifier.weight(1f)
            )
            QuickActionCard(
                title = stringResource(R.string.label_contract_offer),
                icon = Icons.Filled.FactCheck,
                onClick = onQuickContractOfferClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier
) {

    Card(
        modifier = modifier
            .height(64.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color.Transparent)
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                        )
                    )
                )
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Icon(
                icon,
                null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(18.dp)
            )

            Spacer(Modifier.width(6.dp))

            Text(
                title, color = MaterialTheme.colorScheme.onPrimary, style = typography.labelMedium
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
    Column {

        SectionTitle(stringResource(R.string.label_quick_access))

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickAccessCard(
                title = stringResource(R.string.label_contract_organization),
                icon = Icons.Filled.Inventory2,
                backgroundColor = PartnerManagementTheme.colors.successContainer,
                iconTint = PartnerManagementTheme.colors.onSuccessContainer,
                onClick = onContractClick,
                modifier = Modifier.weight(1f)
            )
            QuickAccessCard(
                title = stringResource(R.string.label_advertising_stands),
                icon = Icons.Filled.Inventory2,
                backgroundColor = PartnerManagementTheme.colors.warningContainer,
                iconTint = PartnerManagementTheme.colors.onWarningContainer,
                onClick = onAdvertisingClick,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickAccessCard(
                title = stringResource(R.string.label_reports),
                icon = Icons.Filled.Assessment,
                backgroundColor = PartnerManagementTheme.colors.purpleContainer,
                iconTint = PartnerManagementTheme.colors.onPurpleContainer,
                onClick = onReportClick,
                modifier = Modifier.weight(1f)
            )
            QuickAccessCard(
                title = stringResource(R.string.label_map),
                icon = Icons.Filled.Map,
                backgroundColor = PartnerManagementTheme.colors.infoContainer,
                iconTint = PartnerManagementTheme.colors.onInfoContainer,
                onClick = onMapClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun QuickAccessCard(
    title: String,
    icon: ImageVector,
    backgroundColor: Color,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(104.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = BorderStroke(
            width = 1.dp,
            color = iconTint.copy(alpha = 0.10f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 12.dp,
                    vertical = 10.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = title,
                style = typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun VisitCard(
    item: MeetingEntity,
    isSupervisor: Boolean,
    onClick: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColors.cardBackground
        ),
        border = BorderStroke(
            width = 0.7.dp,
            color = appColors.border
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = item.organizationId!!,
                    style = typography.titleLarge
                )
                Spacer(modifier = Modifier.height(6.dp))

                val visitType = MeetingType.fromValue(item.type)

                MeetingTypeChip(
                    text = stringResource(id = visitType.titleRes),
                    icon = visitType.icon
                )
                Spacer(modifier = Modifier.height(6.dp))

                // نمایش نام بازاریاب صرفاً در صورتی که نقش سرپرست باشد و مقدار داشته باشد
                if (isSupervisor && item.visitorId!!.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = LocalPartnerManagementColors.current.textSecondary
                        )
                        Text(
                            text = item.visitorId,
                            style = typography.labelMedium,
                            color = LocalPartnerManagementColors.current.textSecondary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))

                LocationRow(
                    location = "${item.organizationId}، ${item.organizationId}"
                )
            }

            VisitTimeAndStatus(item)
        }
    }
}


@Composable
fun VisitTimeAndStatus(item: MeetingEntity) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = item.visitDate!!,
            style = typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        StatusBadge(status = OrganizationStatus.fromId(item.status))

    }
}

