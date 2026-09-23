package ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand_assignment_visitor

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
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
import ir.kitgroup.partnerManagement.core.database.entity.StandAssignmentEntity
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.AllocationFilterTab
import ir.kitgroup.partnerManagement.core.ui.util.AssignmentMode
import ir.kitgroup.partnerManagement.core.ui.util.AssignmentType
import ir.kitgroup.partnerManagement.core.ui.util.StandAssignmentStatus
import ir.kitgroup.partnerManagement.core.ui.util.demoStandAssignments


@Composable
fun AdvertisingStandAssignmentVisitorListScreen(
    onBackClick: () -> Unit,
    onNewAssignmentVisitorClick: () -> Unit,
    onViewItemDetailsClick: (StandAssignmentEntity) -> Unit,
    modifier: Modifier = Modifier,
    advertisingStandAssignments: List<StandAssignmentEntity> = demoStandAssignments
) {
    val appColors = LocalPartnerManagementColors.current
    var selectedTab by rememberSaveable { mutableStateOf(AllocationFilterTab.All) }

    val filteredAllocations = remember(selectedTab, advertisingStandAssignments) {
        when (selectedTab) {
            AllocationFilterTab.All -> advertisingStandAssignments
            AllocationFilterTab.Active -> advertisingStandAssignments.filter { it.status == 1 }
            AllocationFilterTab.Draft -> advertisingStandAssignments.filter { it.status == 2 }
            AllocationFilterTab.Returned -> advertisingStandAssignments.filter { it.status == 3 }
            AllocationFilterTab.Cancelled -> advertisingStandAssignments.filter { it.status == 4 }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            CustomHeader(
                title = R.string.label_assignment_advertising_stand,
                showBackButton = true,
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = appColors.screenBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                AllocationHeader(onNewAssignmentVisitorClick = onNewAssignmentVisitorClick)

                Spacer(modifier = Modifier.height(16.dp))

                AllocationFilterTabs(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (filteredAllocations.isEmpty()) {
                    AllocationEmptyState(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                } else {
                    AllocationsList(
                        advertisingStandAssignments = filteredAllocations,
                        onViewItemDetailsClick = onViewItemDetailsClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun AllocationHeader(
    onNewAssignmentVisitorClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.label_assignment_stands_to_visitor),
            style = typography.titleMedium,
            color = appColors.textPrimary,
            modifier = Modifier.weight(1f)
        )
        CustomButton(
            text = stringResource(R.string.label_new_allocation),
            onClick = onNewAssignmentVisitorClick,
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
private fun AllocationFilterTabs(
    selectedTab: AllocationFilterTab,
    onTabSelected: (AllocationFilterTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val appColors = LocalPartnerManagementColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup()
            .background(appColors.cardBackgroundAlt, RoundedCornerShape(14.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AllocationFilterTab.entries.forEach { tab ->
            val selected = selectedTab == tab

            Box(
                modifier = Modifier
                    .weight(1f)
                    .selectable(
                        selected = selected,
                        role = Role.Tab,
                        onClick = { onTabSelected(tab) }
                    )
                    .background(
                        color = if (selected) colorScheme.primary else Color.Transparent,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(tab.titleRes),
                    style = typography.labelLarge,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    color = if (selected) colorScheme.onPrimary else appColors.textSecondary
                )
            }
        }
    }
}

@Composable
private fun AllocationsList(
    advertisingStandAssignments: List<StandAssignmentEntity>,
    onViewItemDetailsClick: (StandAssignmentEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(
            items = advertisingStandAssignments,
            key = { it.standAssignmentId }
        ) { allocation ->
            AllocationCard(
                advertisingStandAssignment = allocation,
                onViewDetailsClick = { onViewItemDetailsClick(allocation) }
            )
        }
    }
}

@Composable
private fun AllocationCard(
    advertisingStandAssignment: StandAssignmentEntity,
    onViewDetailsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    val assignmentTypeTitle = AssignmentType.fromId(advertisingStandAssignment.assignmentType)?.let {
        stringResource(it.titleRes)
    } ?: "-"

    val assignmentModeTitle = AssignmentMode.fromId(advertisingStandAssignment.assignmentMode)?.let {
        stringResource(it.titleRes)
    } ?: "-"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onViewDetailsClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColors.cardBackground
        ),
        border = BorderStroke(
            width = 0.7.dp,
            color = appColors.border
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoLine(
                    label = stringResource(R.string.label_choose_visitor_recipient),
                    value = advertisingStandAssignment.visitorId!!,
                    leadingIcon = Icons.Default.PersonOutline,
                    modifier = Modifier.weight(1f)
                )
                StatusBadge(status = StandAssignmentStatus.fromId(advertisingStandAssignment.status))

            }

            HorizontalDivider(
                thickness = 0.6.dp,
                color = appColors.border
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DetailItem(
                        label = stringResource(R.string.label_assignment_type),
                        value = assignmentTypeTitle,
                        icon = Icons.Default.Category
                    )

                    DetailItem(
                        label = stringResource(R.string.label_delivery_visitor_date),
                        value = advertisingStandAssignment.assignmentDate!!,
                        icon = Icons.Default.CalendarMonth
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DetailItem(
                        label = stringResource(R.string.label_assignment_mode),
                        value = assignmentModeTitle,
                        icon = Icons.Default.AssignmentInd
                    )

                    DetailItem(
                        label = stringResource(R.string.label_allocated_count),
                        value = "${advertisingStandAssignment.count} ${stringResource(R.string.label_unit_count)}",
                        icon = Icons.Default.Inventory2
                    )
                }
            }
        }
    }
}

@Composable
fun InfoLine(
    label: String,
    value: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )

        Column {
            Text(
                text = label,
                style = typography.labelMedium,
                color = appColors.textSecondary
            )
            Text(
                text = value,
                style = typography.titleMedium,
                color = appColors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun DetailItem(label: String, value: String, icon: ImageVector) {
    val appColors = LocalPartnerManagementColors.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
        Column {
            Text(
                label,
                style = typography.labelSmall,
                color = appColors.textSecondary
            )
            Text(
                value,
                style = typography.bodyMedium,
                color = appColors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun AllocationEmptyState(
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Inventory2,
                contentDescription = null,
                tint = appColors.border,
                modifier = Modifier.size(56.dp)
            )

            Text(
                text = stringResource(R.string.msg_no_item_found),
                style = typography.titleMedium,
                color = appColors.textSecondary
            )
        }
    }
}

fun allocationItemIcon(name: String): ImageVector {
    return when (name.lowercase()) {
        "stand" -> Icons.Default.Devices
        "wall" -> Icons.Default.Home
        "kiosk" -> Icons.Default.Campaign
        "brochure" -> Icons.Default.FolderOpen
        "location" -> Icons.Default.Place
        "storefront" -> Icons.Default.Storefront
        else -> Icons.Default.Inventory2
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun AdvertisingStandAssignmentVisitorListScreenPreview() {
    AppScreenPreview {
        AdvertisingStandAssignmentVisitorListScreen(
            onBackClick = {},
            onNewAssignmentVisitorClick = {},
            onViewItemDetailsClick = {}
        )
    }
}
