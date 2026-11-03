package ir.kitgroup.partnerManagement.feature.stand.ui.assigned_stands_organization

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.model.AssignedStandWithDetail
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.BouncingDotsIndicator
import ir.kitgroup.partnerManagement.core.ui.components.EmptyState
import ir.kitgroup.partnerManagement.core.ui.components.OfflinePendingFooterBanner
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.AssignedStandFilterTab
import ir.kitgroup.partnerManagement.core.ui.util.AssignmentMode
import ir.kitgroup.partnerManagement.core.ui.util.StandAssignmentStatus
import ir.kitgroup.partnerManagement.core.ui.util.SyncStatus
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate
import ir.kitgroup.partnerManagement.feature.organization.ui.detail.warning.MeetingBadge

@Composable
fun AssignedStandsOrganizationListScreen(
    onNewAssignmentOrganizationClick: () -> Unit,
    onViewItemDetailsClick: (AssignedStandWithDetail) -> Unit,
    modifier: Modifier = Modifier,
    onMeetingClick: (String) -> Unit,
    viewModel: AssignedStandsOrganizationViewModel = hiltViewModel(),
) {
    var selectedTab by rememberSaveable { mutableStateOf(AssignedStandFilterTab.All) }
    val searchQuery by rememberSaveable { mutableStateOf("") }
    val assignedStandList by viewModel.assignments.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }
    val appColors = LocalPartnerManagementColors.current

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowMessage -> snackBarHostState.showSnackbar(
                    event.message.asString(context)
                )

                is UiEvent.ShowError -> snackBarHostState.showSnackbar(
                    event.error.asString(context)
                )
            }
        }
    }

    val filteredAllocations = remember(selectedTab, assignedStandList, searchQuery) {
        val list = assignedStandList ?: return@remember emptyList()
        val query = searchQuery.trim()

        val tabFiltered = when (selectedTab) {
            AssignedStandFilterTab.All -> list
            AssignedStandFilterTab.Active -> list.filter { it.assignedStand.statusAssign == 1 }
            AssignedStandFilterTab.Draft -> list.filter { it.assignedStand.statusAssign == 0 }
            AssignedStandFilterTab.Returned -> list.filter { it.assignedStand.statusAssign == 2 }
            AssignedStandFilterTab.Cancelled -> list.filter { it.assignedStand.statusAssign == 3 }
        }

        if (query.isEmpty()) {
            tabFiltered
        } else {
            tabFiltered.filter { item ->
                item.organizationName.contains(query, ignoreCase = true) ||
                        item.standTitle.contains(query, ignoreCase = true) ||
                        item.assignedStand.description?.contains(query, ignoreCase = true) == true
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNewAssignmentOrganizationClick,
                containerColor = appColors.success,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp),
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = {
                    Text(
                        text = stringResource(R.string.label_new_allocation),
                        style = typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp) // کاهش فاصله کلی برای اتصال بهینه‌تر تب‌ها به لیست
        ) {
            // نوار فیلتر تب‌ها
            AssignedStandFilterChipsRow(
                selectedTab = selectedTab,
                allAssignments = assignedStandList.orEmpty(),
                onTabSelected = { selectedTab = it },
                modifier = Modifier.padding(top = 8.dp, bottom = 2.dp)
            )

            // محتوای فهرست / وضعیت خالی / لودینگ
            when {
                assignedStandList == null -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        BouncingDotsIndicator(
                            modifier = Modifier.size(36.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                filteredAllocations.isEmpty() -> {
                    EmptyState(
                        textRes = R.string.msg_no_item_found,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(top = 4.dp, bottom = 88.dp) // پدینگ بالای لیست به حداقل رسید
                    ) {
                        items(
                            items = filteredAllocations,
                            key = { it.assignedStand.assignedStandId }
                        ) { allocation ->
                            AssignedStandCard(
                                itemWithDetail = allocation,
                                onViewDetailsClick = { onViewItemDetailsClick(allocation) }
                                , onMeetingClick = onMeetingClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AssignedStandFilterChipsRow(
    selectedTab: AssignedStandFilterTab,
    allAssignments: List<AssignedStandWithDetail>,
    onTabSelected: (AssignedStandFilterTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        AssignedStandFilterTab.entries.forEach { tab ->
            val isSelected = selectedTab == tab
            val count = remember(tab, allAssignments) {
                when (tab) {
                    AssignedStandFilterTab.All -> allAssignments.size
                    AssignedStandFilterTab.Active -> allAssignments.count { it.assignedStand.statusAssign == 1 }
                    AssignedStandFilterTab.Draft -> allAssignments.count { it.assignedStand.statusAssign == 0 }
                    AssignedStandFilterTab.Returned -> allAssignments.count { it.assignedStand.statusAssign == 2 }
                    AssignedStandFilterTab.Cancelled -> allAssignments.count { it.assignedStand.statusAssign == 3 }
                }
            }

            FilterChip(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                shape = RoundedCornerShape(12.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                border = BorderStroke(
                    width = 0.8.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                ),
                label = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = stringResource(tab.titleRes),
                            style = typography.labelLarge,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.22f)
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = count.toString(),
                                style = typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun AssignedStandCard(
    itemWithDetail: AssignedStandWithDetail,
    onViewDetailsClick: () -> Unit,
    onMeetingClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme
    val assignment = itemWithDetail.assignedStand
    val isPendingSync = assignment.syncStatus != SyncStatus.SYNCED

    val assignmentModeTitle = AssignmentMode.fromId(assignment.assignmentMode)?.let {
        stringResource(it.titleRes)
    } ?: assignment.assignmentModeName ?: "-"

    val displayDeliveryDate = assignment.deliveryToOrgDate
        ?: assignment.assignmentDate
        ?: "-"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onViewDetailsClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(
            width = if (isPendingSync) 1.dp else 0.7.dp,
            color = if (isPendingSync) appColors.warning.copy(alpha = 0.75f) else appColors.border
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // ردیف هدر: نام سازمان و وضعیت تخصیص
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(colors.primaryContainer, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Apartment,
                            contentDescription = null,
                            tint = colors.onPrimaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = itemWithDetail.organizationName,
                            style = typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = appColors.textPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = itemWithDetail.standTitle,
                            style = typography.bodySmall,
                            color = colors.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                assignment.statusAssign?.let { statusId ->
                    StatusBadge(status = StandAssignmentStatus.fromId(statusId))
                }
            }

            HorizontalDivider(
                thickness = 0.6.dp,
                color = colors.outlineVariant.copy(alpha = 0.4f)
            )

            // جزئیات میانی در کانتینر ملایم و خوانا
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(colors.surfaceVariant.copy(alpha = 0.35f))
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StandCardField(
                        icon = Icons.Default.SwapHoriz,
                        label = stringResource(R.string.label_assignment_mode),
                        value = assignmentModeTitle,
                        modifier = Modifier.weight(1f)
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = colors.secondaryContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Inventory2,
                                contentDescription = null,
                                tint = colors.onSecondaryContainer,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "${assignment.count ?: 0} ${stringResource(R.string.label_unit_count)}",
                                style = typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = colors.onSecondaryContainer
                            )
                        }
                    }
                }

                assignment.description?.takeIf { it.isNotBlank() }?.let { desc ->
                    StandCardField(
                        icon = Icons.Default.Notes,
                        label = stringResource(R.string.label_description),
                        value = desc
                    )
                }
            }
            if (!assignment.meetingId.isNullOrBlank() && assignment.meetingId != "00000000-0000-0000-0000-000000000000") {
                MeetingBadge(
                    onClick = { onMeetingClick(assignment.meetingId!!) }
                )
            }
            // فوتر کارت
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = appColors.textSecondary
                    )
                    Text(
                        text = stringResource(R.string.label_delivery_organization_date) + ": " + formatJalaliDate(
                            displayDeliveryDate
                        ),
                        style = typography.labelSmall,
                        color = appColors.textSecondary
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = stringResource(R.string.label_show_details),
                        style = typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = colors.primary
                    )
                }
            }

            if (isPendingSync) {
                Spacer(modifier = Modifier.height(2.dp))
                OfflinePendingFooterBanner()
            }
        }
    }
}

@Composable
private fun StandCardField(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colors.primary,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "$label:",
            style = typography.labelSmall,
            color = appColors.textSecondary
        )
        Text(
            text = value,
            style = typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = appColors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


