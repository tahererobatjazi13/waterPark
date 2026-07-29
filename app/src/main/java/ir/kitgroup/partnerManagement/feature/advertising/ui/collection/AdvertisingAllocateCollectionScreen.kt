package ir.kitgroup.partnerManagement.feature.advertising.ui.collection

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
import ir.kitgroup.partnerManagement.core.ui.components.StatusImageBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.AllocationFilterTab
import ir.kitgroup.partnerManagement.core.ui.util.AllocationStatus
import ir.kitgroup.partnerManagement.core.ui.util.extensions.style
import ir.kitgroup.partnerManagement.feature.advertising.model.Allocation

private val demoAllocations = listOf(
    Allocation(
        "1",
        "محمد احمدی",
        "هتل پارسیان آزادی",
        "رومیزی",
        "stand",
        5,
        "۱۴۰۳/۰۳/۲۲",
        AllocationStatus.Delivered
    ),
    Allocation(
        "2",
        "سارا مرادی",
        "هتل اسپیناس پالاس",
        "دیواری",
        "wall",
        3,
        "۱۴۰۳/۰۳/۲۴",
        AllocationStatus.Pending
    ),
    Allocation(
        "3",
        "علی رضایی",
        "هتل هما",
        "کیوسک",
        "kiosk",
        2,
        "۱۴۰۳/۰۳/۲۰",
        AllocationStatus.Delivered
    ),
    Allocation(
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
fun AdvertisingAllocateCollectionScreen(
    onBackClick: () -> Unit,
    onNewAllocationClick: () -> Unit,
    onViewItemDetailsClick: (Allocation) -> Unit,
    modifier: Modifier = Modifier,
    allocations: List<Allocation> = demoAllocations
) {
    val appColors = LocalPartnerManagementColors.current
    var selectedTab by rememberSaveable { mutableStateOf(AllocationFilterTab.All) }

    val filteredAllocations = remember(selectedTab, allocations) {
        when (selectedTab) {
            AllocationFilterTab.All -> allocations
            AllocationFilterTab.Delivered -> allocations.filter { it.status == AllocationStatus.Delivered }
            AllocationFilterTab.Pending -> allocations.filter { it.status == AllocationStatus.Pending }
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
                AllocationHeader(onNewAllocationClick = onNewAllocationClick)

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
                        allocations = filteredAllocations,
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
    onNewAllocationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.label_allocate_stand_to_collection),
            style = MaterialTheme.typography.titleMedium,
            color = appColors.textPrimary,
        )
        Button(
            onClick = onNewAllocationClick,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = appColors.success,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.label_new_allocation),
                style = MaterialTheme.typography.labelLarge
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
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isSelected) colorScheme.onPrimary else appColors.textSecondary,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun AllocationsList(
    allocations: List<Allocation>,
    onViewItemDetailsClick: (Allocation) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(items = allocations, key = { it.id }) { allocation ->
            AllocationCard(
                allocation = allocation,
                onViewDetailsClick = { onViewItemDetailsClick(allocation) }
            )
        }
    }
}

@Composable
private fun AllocationCard(
    allocation: Allocation,
    onViewDetailsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    val itemIcon = allocationItemIcon(allocation.itemIconName)

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
                    label = stringResource(R.string.label_visitor_name),
                    value = allocation.visitorName,
                    icon = Icons.Default.PersonOutline
                )
                StatusImageBadge(
                    text = stringResource(allocation.status.labelRes),
                    style = allocation.status.style(),
                    icon = allocation.status.icon
                )
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
                        stringResource(R.string.label_name_collaborative),
                        allocation.collectionName,
                        Icons.Default.Business
                    )
                    DetailItem(
                        stringResource(R.string.label_allocated_date),
                        allocation.allocatedDate,
                        Icons.Default.CalendarMonth
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DetailItem(
                        stringResource(R.string.label_item_type),
                        allocation.itemType,
                        itemIcon
                    )
                    DetailItem(
                        stringResource(R.string.label_allocated_count),
                        "${allocation.count} ${stringResource(R.string.label_unit_count)}",
                        Icons.Default.Inventory2
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoItem(label: String, value: String, icon: ImageVector) {
    val appColors = LocalPartnerManagementColors.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Column {
            Text(
                label,
                style = MaterialTheme.typography.labelSmall,
                color = appColors.textSecondary
            )
            Text(
                value,
                style = MaterialTheme.typography.bodyLarge,
                color = appColors.textPrimary,
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
                style = MaterialTheme.typography.labelSmall,
                color = appColors.textSecondary
            )
            Text(
                value,
                style = MaterialTheme.typography.bodyMedium,
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
private fun AdvertisingAllocateCollectionScreenPreview() {
    AppScreenPreview {
        AdvertisingAllocateCollectionScreen(
            onBackClick = {},
            onNewAllocationClick = {},
            onViewItemDetailsClick = {}
        )
    }
}
