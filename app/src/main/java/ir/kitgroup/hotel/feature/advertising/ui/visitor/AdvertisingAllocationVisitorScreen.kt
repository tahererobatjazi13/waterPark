package ir.kitgroup.hotel.feature.advertising.ui.visitor

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.FiberManualRecord
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
import androidx.compose.ui.unit.dp
import ir.kitgroup.hotel.R
import ir.kitgroup.hotel.core.ui.components.CustomHeader
import ir.kitgroup.hotel.core.ui.util.AllocationFilterTab
import ir.kitgroup.hotel.core.ui.util.AllocationStatus
import ir.kitgroup.hotel.feature.advertising.model.Allocation

private val demoAllocations = listOf(
    Allocation(
        id = "1",
        visitorName = "محمد احمدی",
        hotelName = "هتل پارسیان آزادی",
        itemType = "رومیزی",
        itemIconName = "stand",
        count = 5,
        allocatedDate = "۱۴۰۳/۰۳/۲۲",
        status = AllocationStatus.Delivered
    ),
    Allocation(
        id = "2",
        visitorName = "سارا مرادی",
        hotelName = "هتل اسپیناس پالاس",
        itemType = "دیواری",
        itemIconName = "wall",
        count = 3,
        allocatedDate = "۱۴۰۳/۰۳/۲۴",
        status = AllocationStatus.Pending
    ),
    Allocation(
        id = "3",
        visitorName = "علی رضایی",
        hotelName = "هتل هما",
        itemType = "کیوسک",
        itemIconName = "kiosk",
        count = 2,
        allocatedDate = "۱۴۰۳/۰۳/۲۰",
        status = AllocationStatus.Delivered
    ),
    Allocation(
        id = "4",
        visitorName = "نازنین کریمی",
        hotelName = "هتل بزرگ تهران",
        itemType = "رومیزی",
        itemIconName = "stand",
        count = 4,
        allocatedDate = "۱۴۰۳/۰۳/۲۵",
        status = AllocationStatus.Pending
    )
)

@Composable
fun AdvertisingAllocateVisitorScreen(
    onBackClick: () -> Unit,
    onNewAllocationClick: () -> Unit,
    onViewItemDetailsClick: (Allocation) -> Unit,
    modifier: Modifier = Modifier,
    allocations: List<Allocation> = demoAllocations
) {
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
                .padding(
                    top = innerPadding.calculateTopPadding()
                ),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                AllocationHeader(
                    onNewAllocationClick = onNewAllocationClick,
                    modifier = Modifier.padding(
                        start = 8.dp,
                        top = 16.dp,
                        end = 8.dp
                    )
                )


                Spacer(modifier = Modifier.height(16.dp))

                AllocationFilterTabs(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                    modifier = Modifier.padding(horizontal = 8.dp)
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
                        allocations = filteredAllocations,
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
    onNewAllocationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = stringResource(R.string.label_allocate_stand_to_visitor),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = onNewAllocationClick,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.btn_new_allocation),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
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

    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup()
            .background(colorScheme.surfaceVariant, RoundedCornerShape(14.dp))
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
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                    color = if (selected) colorScheme.onPrimary else colorScheme.onSurfaceVariant
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
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp
        )
    ) {
        items(
            items = allocations,
            key = { it.id }
        ) { allocation ->
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
    val icon = allocationItemIcon(allocation.itemIconName)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /*onClick()*/ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 0.7.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.90f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            // بخش اطلاعات اصلی (سمت راست در RTL)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InfoLine(
                    label = stringResource(R.string.label_visitor_name),
                    value = allocation.visitorName,
                    leadingIcon = Icons.Default.PersonOutline
                )

                InfoLine(
                    label = stringResource(R.string.label_hotel_name),
                    value = allocation.hotelName,
                    leadingIcon = Icons.Default.Business
                )
            }

            // بخش نوع/تعداد/تاریخ (سمت چپ در RTL)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AllocationStatusChip(status = allocation.status)

                DetailLine(
                    label = stringResource(R.string.label_item_type),
                    value = allocation.itemType,
                    icon = icon
                )

                DetailLine(
                    label = stringResource(R.string.label_allocated_count),
                    value = "${allocation.count} ${stringResource(R.string.label_unit_count)}",
                    icon = Icons.Default.Inventory2
                )

                DetailLine(
                    label = stringResource(R.string.label_allocated_date),
                    value = allocation.allocatedDate,
                    icon = Icons.Default.CalendarMonth
                )
            }
        }
    }
}

@Composable
private fun AllocationStatusChip(
    status: AllocationStatus,
    modifier: Modifier = Modifier
) {
    val (text, bg, fg, icon) = when (status) {
        AllocationStatus.Delivered -> Quad(
            text = stringResource(R.string.status_delivered),
            bg = Color(0xFFE8F7EC),
            fg = Color(0xFF2E9E4D),
            icon = Icons.Default.Check
        )

        AllocationStatus.Pending -> Quad(
            text = stringResource(R.string.status_pending),
            bg = Color(0xFFFFF2DD),
            fg = Color(0xFFE59A13),
            icon = Icons.Default.FiberManualRecord
        )
    }

    Surface(
        modifier = modifier.wrapContentWidth(),
        shape = RoundedCornerShape(999.dp),
        color = bg
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = fg,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = text,
                color = fg,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private data class Quad(
    val text: String,
    val bg: Color,
    val fg: Color,
    val icon: ImageVector
)

@Composable
private fun InfoLine(
    label: String,
    value: String,
    leadingIcon: ImageVector
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = leadingIcon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp)
        )

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
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
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp)
        )

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
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
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Inventory2,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(56.dp)
            )
            Text(
                text = stringResource(R.string.label_no_items_found),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
