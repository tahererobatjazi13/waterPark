package ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand_assignment_organization

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
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.AllocationFilterTab
import androidx.compose.material3.MaterialTheme.typography
import ir.kitgroup.partnerManagement.core.database.entity.StandAssignmentEntity
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.util.AssignmentMode
import ir.kitgroup.partnerManagement.core.ui.util.AssignmentType
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationStatus
import ir.kitgroup.partnerManagement.core.ui.util.StandAssignmentStatus
import ir.kitgroup.partnerManagement.core.ui.util.demoStandAssignments


@Composable
fun AdvertisingStandAssignmentOrganizationListScreen(
    onBackClick: () -> Unit,
    onNewAssignmentOrganizationClick: () -> Unit,
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
            AllocationFilterTab.Draft -> advertisingStandAssignments.filter { it.status == 1 }
            AllocationFilterTab.Returned -> advertisingStandAssignments.filter { it.status == 2 }
            AllocationFilterTab.Cancelled -> advertisingStandAssignments.filter { it.status == 2 }
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


                AllocationHeader(onNewAssignmentOrganizationClick = onNewAssignmentOrganizationClick)

                Spacer(modifier = Modifier.height(16.dp))

                AllocationFilterTabs(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (filteredAllocations.isEmpty()) {
                    AllocationEmptyState(modifier = Modifier.weight(1f))
                } else {
                    AllocationsList(
                        advertisingStandAssignments = filteredAllocations,
                        onViewItemDetailsClick = onViewItemDetailsClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun AllocationHeader(
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
            .background(appColors.cardBackgroundAlt, RoundedCornerShape(12.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        AllocationFilterTab.entries.forEach { tab ->
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
        items(items = advertisingStandAssignments, key = { it.standAssignmentId }) { allocation ->
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
    val assignmentTypeTitle =
        AssignmentType.fromId(advertisingStandAssignment.assignmentType)?.let {
            stringResource(it.titleRes)
        } ?: "-"

    val assignmentModeTitle =
        AssignmentMode.fromId(advertisingStandAssignment.assignmentMode)?.let {
            stringResource(it.titleRes)
        } ?: "-"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onViewDetailsClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(0.5.dp, appColors.border)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                InfoItem(
                    label = stringResource(R.string.label_organization_receiving_name),
                    value = advertisingStandAssignment.organizationId!!,
                    icon = Icons.Default.Business
                )
                StatusBadge(status = StandAssignmentStatus.fromId(advertisingStandAssignment.status))

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
                        label = stringResource(R.string.label_assignment_type),
                        value = assignmentTypeTitle,
                        icon = Icons.Default.Category
                    )
                    DetailItem(
                        stringResource(R.string.label_delivery_organization_date),
                        advertisingStandAssignment.assignmentDate!!,
                        Icons.Default.CalendarMonth
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
                        stringResource(R.string.label_allocated_count),
                        "${advertisingStandAssignment.count} ${stringResource(R.string.label_unit_count)}",
                        Icons.Default.Inventory2
                    )
                }
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
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Column {
            Text(
                label,
                style = typography.labelSmall,
                color = appColors.textSecondary
            )
            Text(
                value,
                style = typography.titleLarge,
                color = appColors.textPrimary,
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
private fun AllocationEmptyState(modifier: Modifier = Modifier) {
    val appColors = LocalPartnerManagementColors.current
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.Inbox,
                null,
                modifier = Modifier.size(64.dp),
                tint = appColors.border
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(stringResource(R.string.msg_no_item_found), color = appColors.textSecondary)
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
private fun AdvertisingStandAssignmentOrganizationListScreenPreview() {
    AppScreenPreview {
        AdvertisingStandAssignmentOrganizationListScreen(
            onBackClick = {},
            onNewAssignmentOrganizationClick = {},
            onViewItemDetailsClick = {}
        )
    }
}
