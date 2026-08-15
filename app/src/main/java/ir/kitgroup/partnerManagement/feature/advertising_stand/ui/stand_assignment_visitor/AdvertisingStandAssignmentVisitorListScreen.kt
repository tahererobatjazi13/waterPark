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
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Storefront
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
import ir.kitgroup.partnerManagement.core.ui.components.StatusImageBadge
import ir.kitgroup.partnerManagement.core.ui.theme.PartnerManagementTheme
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.AllocationFilterTab
import ir.kitgroup.partnerManagement.core.ui.util.AllocationStatus
import ir.kitgroup.partnerManagement.core.ui.util.extensions.style
import ir.kitgroup.partnerManagement.feature.advertising_stand.model.AdvertisingStandAssignment

private val demoAdvertisingStandAssignments = listOf(
    AdvertisingStandAssignment(
        "1",
        "محمد احمدی",
        "هتل پارسیان آزادی",
        "رومیزی",
        "stand",
        5,
        "۱۴۰۳/۰۳/۲۲",
        AllocationStatus.Delivered
    ),
    AdvertisingStandAssignment(
        "2",
        "سارا مرادی",
        "هتل اسپیناس پالاس",
        "دیواری",
        "wall",
        3,
        "۱۴۰۳/۰۳/۲۴",
        AllocationStatus.Pending
    ),
    AdvertisingStandAssignment(
        "3",
        "علی رضایی",
        "هتل هما",
        "کیوسک",
        "kiosk",
        2,
        "۱۴۰۳/۰۳/۲۰",
        AllocationStatus.Delivered
    ),
    AdvertisingStandAssignment(
        "4",
        "نازنین کریمی",
        "هتل بزرگ تهران",
        "رومیزی",
        "stand",
        4,
        "۱۴۰۳/۰۳/۲۵",
        AllocationStatus.Pending
    )
)

@Composable
fun AdvertisingStandAssignmentVisitorListScreen(
    onBackClick: () -> Unit,
    onNewAssignmentVisitorClick: () -> Unit,
    onViewItemDetailsClick: (AdvertisingStandAssignment) -> Unit,
    modifier: Modifier = Modifier,
    advertisingStandAssignments: List<AdvertisingStandAssignment> = demoAdvertisingStandAssignments
) {
    val appColors = LocalPartnerManagementColors.current
    var selectedTab by rememberSaveable { mutableStateOf(AllocationFilterTab.All) }

    val filteredAllocations = remember(selectedTab, advertisingStandAssignments) {
        when (selectedTab) {
            AllocationFilterTab.All -> advertisingStandAssignments
            AllocationFilterTab.Delivered -> advertisingStandAssignments.filter { it.status == AllocationStatus.Delivered }
            AllocationFilterTab.Pending -> advertisingStandAssignments.filter { it.status == AllocationStatus.Pending }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            CustomHeader(
                title = R.string.label_advertising_allocation,
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
            style = MaterialTheme.typography.titleMedium,
            color = appColors.textPrimary,
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = onNewAssignmentVisitorClick,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PartnerManagementTheme.colors.success,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(R.string.label_new_allocation),
                style = MaterialTheme.typography.labelLarge,
            )
        }
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
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                    color = if (selected) colorScheme.onPrimary else appColors.textSecondary
                )
            }
        }
    }
}

@Composable
private fun AllocationsList(
    advertisingStandAssignments: List<AdvertisingStandAssignment>,
    onViewItemDetailsClick: (AdvertisingStandAssignment) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(
            items = advertisingStandAssignments,
            key = { it.id }
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
    advertisingStandAssignment: AdvertisingStandAssignment,
    onViewDetailsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    val itemIcon = allocationItemIcon(advertisingStandAssignment.itemIconName)

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
                    label = stringResource(R.string.label_visitor_name),
                    value = advertisingStandAssignment.visitorName,
                    leadingIcon = Icons.Default.PersonOutline,
                    modifier = Modifier.weight(1f)
                )

                StatusImageBadge(
                    text = stringResource(advertisingStandAssignment.status.labelRes),
                    style = advertisingStandAssignment.status.style(),
                    icon = advertisingStandAssignment.status.icon
                )
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
                    DetailLine(
                        label = stringResource(R.string.label_organization_name),
                        value = advertisingStandAssignment.organizationName,
                        icon = Icons.Default.Business
                    )

                    DetailLine(
                        label = stringResource(R.string.label_allocated_date),
                        value = advertisingStandAssignment.allocatedDate,
                        icon = Icons.Default.CalendarMonth
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DetailLine(
                        label = stringResource(R.string.label_item_type),
                        value = advertisingStandAssignment.itemType,
                        icon = itemIcon
                    )

                    DetailLine(
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
private fun InfoLine(
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
                style = MaterialTheme.typography.labelMedium,
                color = appColors.textSecondary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = appColors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun DetailLine(
    label: String,
    value: String,
    icon: ImageVector
) {
    val appColors = LocalPartnerManagementColors.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = appColors.textSecondary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
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
                style = MaterialTheme.typography.titleMedium,
                color = appColors.textSecondary
            )
        }
    }
}

private fun allocationItemIcon(name: String): ImageVector {
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
