package ir.kitgroup.partnerManagement.feature.stand.ui.assigned_stands_organization

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.core.database.model.AssignedStandWithDetail
import ir.kitgroup.partnerManagement.core.ui.components.BouncingDotsIndicator
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.EmptyState
import ir.kitgroup.partnerManagement.core.ui.components.OfflinePendingFooterBanner
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.util.AssignedStandFilterTab
import ir.kitgroup.partnerManagement.core.ui.util.AssignmentMode
import ir.kitgroup.partnerManagement.core.ui.util.AssignmentType
import ir.kitgroup.partnerManagement.core.ui.util.StandAssignmentStatus
import ir.kitgroup.partnerManagement.core.ui.util.SyncStatus
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate


@Composable
fun AssignedStandsOrganizationListScreen(
    onNewAssignmentOrganizationClick: () -> Unit,
    onViewItemDetailsClick: (AssignedStandWithDetail) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AssignedStandsOrganizationViewModel = hiltViewModel(),
) {

    var selectedTab by rememberSaveable { mutableStateOf(AssignedStandFilterTab.All) }
    val assignedStand by viewModel.assignments.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }


    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
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


    val filteredAllocations = remember(selectedTab, assignedStand) {
        when (selectedTab) {
            AssignedStandFilterTab.All -> assignedStand
            AssignedStandFilterTab.Active -> assignedStand!!.filter { it.assignedStand.statusAssign == 1 }
            AssignedStandFilterTab.Draft -> assignedStand!!.filter { it.assignedStand.statusAssign == 0 }
            AssignedStandFilterTab.Returned -> assignedStand!!.filter { it.assignedStand.statusAssign == 2 }
            AssignedStandFilterTab.Cancelled -> assignedStand!!.filter { it.assignedStand.statusAssign == 3 }
        }
    }


    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                AssignedStandHeader(onNewAssignmentOrganizationClick = onNewAssignmentOrganizationClick)

                Spacer(modifier = Modifier.height(16.dp))

                AssignedStandFilterTabs(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )

                Spacer(modifier = Modifier.height(16.dp))
                when {
                    assignedStand == null -> {
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

                    filteredAllocations?.isEmpty() == true -> {
                        EmptyState(
                            textRes = R.string.msg_no_item_found,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        )
                    }

                    else -> {
                        AssignedStandsList(
                            advertisingStandAssignments = filteredAllocations.orEmpty(),
                            onViewItemDetailsClick = onViewItemDetailsClick,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AssignedStandHeader(
    onNewAssignmentOrganizationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.label_assignment_stands_to_organizations),
            style = typography.titleMedium,
            color = appColors.textPrimary,
        )

        CustomButton(
            text = stringResource(R.string.label_new_allocation),
            onClick = onNewAssignmentOrganizationClick,
            fillMaxWidth = false,
            height = 38.dp,
            textStyle = typography.titleLarge,
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            icon = Icons.Default.Add,
            colors = ButtonDefaults.buttonColors(
                containerColor = appColors.success,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}

@Composable
private fun AssignedStandFilterTabs(
    selectedTab: AssignedStandFilterTab,
    onTabSelected: (AssignedStandFilterTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val appColors = LocalPartnerManagementColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup()
            .background(appColors.cardBackgroundAlt, RoundedCornerShape(12.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        AssignedStandFilterTab.entries.forEach { tab ->
            val isSelected = selectedTab == tab
            Box(
                modifier = Modifier
                    .weight(1f)
                    .selectable(
                        selected = isSelected,
                        role = Role.Tab,
                        onClick = { onTabSelected(tab) }
                    )
                    .background(
                        color = if (isSelected) colorScheme.primary else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(tab.titleRes),
                    style = typography.labelLarge,
                    color = if (isSelected) colorScheme.onPrimary else appColors.textSecondary,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun AssignedStandsList(
    advertisingStandAssignments: List<AssignedStandWithDetail>,
    onViewItemDetailsClick: (AssignedStandWithDetail) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(
            items = advertisingStandAssignments,
            key = { it.assignedStand.assignedStandId }
        ) { allocation ->
            AssignedStandCard(
                itemWithDetail = allocation,
                onViewDetailsClick = { onViewItemDetailsClick(allocation) }
            )
        }
    }
}

@Composable
private fun AssignedStandCard(
    itemWithDetail: AssignedStandWithDetail,
    onViewDetailsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isPendingSync = itemWithDetail.assignedStand.syncStatus != SyncStatus.SYNCED

    val appColors = LocalPartnerManagementColors.current
    val assignment = itemWithDetail.assignedStand

    val assignmentTypeTitle =
        AssignmentType.fromId(assignment.assignmentType)?.let {
            stringResource(it.titleRes)
        } ?: assignment.assignmentTypeName ?: "-"

    val assignmentModeTitle =
        AssignmentMode.fromId(assignment.assignmentMode)?.let {
            stringResource(it.titleRes)
        } ?: assignment.assignmentModeName ?: "-"

    val displayDeliveryDate = assignment.deliveryToOrgDate
        ?: assignment.assignmentDate
        ?: "-"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onViewDetailsClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(
            width = if (isPendingSync) 1.dp else 0.7.dp,
            color = if (isPendingSync) appColors.warning.copy(alpha = 0.65f) else appColors.border
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // نمایش نوع استند در بالای کارت
                InfoItem(
                    label = stringResource(R.string.label_stand_type),
                    value = itemWithDetail.standTitle,
                    icon = Icons.Default.Devices
                )
                assignment.statusAssign?.let { statusId ->
                    StatusBadge(status = StandAssignmentStatus.fromId(statusId))
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = 0.5.dp,
                color = appColors.border
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DetailItem(
                        label = stringResource(R.string.label_organization_name),
                        value = itemWithDetail.organizationName,
                        icon = Icons.Default.Category
                    )
                    DetailItem(
                        label = stringResource(R.string.label_delivery_organization_date),
                        value = formatJalaliDate(displayDeliveryDate),
                        icon = Icons.Default.CalendarMonth
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DetailItem(
                        label = stringResource(R.string.label_assignment_mode),
                        value = assignmentModeTitle,
                        icon = Icons.Default.AssignmentInd
                    )
                    DetailItem(
                        label = stringResource(R.string.label_allocated_count),
                        value = "${assignment.count ?: 0} ${stringResource(R.string.label_unit_count)}",
                        icon = Icons.Default.Inventory2
                    )
                }

            }

            if (isPendingSync) {
                Spacer(modifier = Modifier.height(10.dp))
                OfflinePendingFooterBanner()
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String, icon: ImageVector) {
    val appColors = LocalPartnerManagementColors.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Column {
            Text(
                text = label,
                style = typography.labelSmall,
                color = appColors.textSecondary
            )
            Text(
                text = value,
                style = typography.titleLarge,
                color = appColors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun DetailItem(label: String, value: String, icon: ImageVector) {
    val appColors = LocalPartnerManagementColors.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )
        Column {
            Text(
                text = label,
                style = typography.labelSmall,
                color = appColors.textSecondary
            )
            Text(
                text = value,
                style = typography.bodyMedium,
                color = appColors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun AssignedStandsOrganizationListScreenPreview() {
    AppScreenPreview {
        AssignedStandsOrganizationListScreen(
            onNewAssignmentOrganizationClick = {},
            onViewItemDetailsClick = {}
        )
    }
}
