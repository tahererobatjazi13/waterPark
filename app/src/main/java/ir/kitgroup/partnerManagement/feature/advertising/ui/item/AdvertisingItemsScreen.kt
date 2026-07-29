package ir.kitgroup.partnerManagement.feature.advertising.ui.item

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
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocationOn
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
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.StatusImageBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.AdvertisingFilterTab
import ir.kitgroup.partnerManagement.core.ui.util.AllocationStatus
import ir.kitgroup.partnerManagement.core.ui.util.extensions.style
import ir.kitgroup.partnerManagement.feature.advertising.model.AdvertisingItem

private val demoAdvertisingItems = listOf(
    AdvertisingItem(
        "1", "استند رومیزی", "استند رومیزی", 25, true, "stand", AllocationStatus.Active
    ),
    AdvertisingItem(
        "2",
        "بروشور معرفی",
        "بروشور",
        0,
        false,
        "brochure",
        AllocationStatus.OutOfStock
    ),
    AdvertisingItem("3", "استند لابی", "استند دیواری", 8, true, "location", AllocationStatus.Active)
)

@Composable
fun AdvertisingItemsScreen(
    onBackClick: () -> Unit,
    onAddItemClick: () -> Unit,
    onEditItemClick: (AdvertisingItem) -> Unit,
    modifier: Modifier = Modifier,
    items: List<AdvertisingItem> = demoAdvertisingItems
) {
    val appColors = LocalPartnerManagementColors.current
    var selectedTab by rememberSaveable { mutableStateOf(AdvertisingFilterTab.All) }

    val filteredItems = remember(selectedTab, items) {
        when (selectedTab) {
            AdvertisingFilterTab.All -> items
            AdvertisingFilterTab.Active -> items.filter { it.isActive }
            AdvertisingFilterTab.OutOfStock -> items.filter { it.stock == 0 }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CustomHeader(
                title = R.string.label_managing_promotional_items,
                showBackButton = true,
                onBackClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.primary
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = appColors.screenBackground
        ) {
            AdvertisingItemsContent(
                items = filteredItems,
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it },
                onAddItemClick = onAddItemClick,
                onEditItemClick = onEditItemClick,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun AdvertisingItemsContent(
    items: List<AdvertisingItem>,
    selectedTab: AdvertisingFilterTab,
    onTabSelected: (AdvertisingFilterTab) -> Unit,
    onAddItemClick: () -> Unit,
    onEditItemClick: (AdvertisingItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Button(
            onClick = onAddItemClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = appColors.success,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = stringResource(R.string.label_add_new_item),
                style = typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        AdvertisingFilterTabs(
            selectedTab = selectedTab,
            onTabSelected = onTabSelected,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (items.isEmpty()) {
            AdvertisingItemsEmptyState(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        } else {
            AdvertisingItemsList(
                items = items,
                onEditItemClick = onEditItemClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}

@Composable
private fun AdvertisingFilterTabs(
    selectedTab: AdvertisingFilterTab,
    onTabSelected: (AdvertisingFilterTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Row(
        modifier = modifier
            .selectableGroup()
            .background(appColors.cardBackgroundAlt, RoundedCornerShape(12.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AdvertisingFilterTab.entries.forEach { tab ->
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
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(tab.titleRes),
                    style = typography.labelMedium,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else appColors.textSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun AdvertisingItemsList(
    items: List<AdvertisingItem>,
    onEditItemClick: (AdvertisingItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(
            items = items,
            key = { it.id }
        ) { item ->
            AdvertisingItemCard(
                item = item,
                onEditClick = { onEditItemClick(item) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun AdvertisingItemCard(
    item: AdvertisingItem,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    val itemIcon = advertisingItemIcon(item.iconName)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onEditClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(width = 0.7.dp, color = appColors.border)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            AdvertisingItemIcon(icon = itemIcon)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = item.title,
                    style = typography.titleMedium,
                    color = appColors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                InfoBlock(
                    label = "${stringResource(R.string.label_item_type)}: ",
                    value = item.type
                )

                InfoBlock(
                    label = "${stringResource(R.string.label_stock)}: ",
                    value = item.stock.toString()
                )

                StatusImageBadge(
                    text = stringResource(item.status.labelRes),
                    style = item.status.style(),
                    icon = item.status.icon
                )
            }

            OutlinedButton(
                onClick = onEditClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                border = BorderStroke(1.dp, appColors.border),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.label_edit),
                    style = typography.labelMedium
                )
            }
        }
    }
}

@Composable
private fun AdvertisingItemIcon(
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.size(48.dp),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(25.dp)
            )
        }
    }
}

@Composable
private fun InfoBlock(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = typography.labelMedium,
            color = appColors.textSecondary
        )
        Text(
            text = value,
            style = typography.bodyMedium,
            color = appColors.textPrimary
        )
    }
}

@Composable
private fun AdvertisingItemsEmptyState(
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                modifier = Modifier.size(64.dp),
                shape = RoundedCornerShape(20.dp),
                color = appColors.cardBackgroundAlt,
                contentColor = appColors.textSecondary
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Inventory2,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Text(
                text = stringResource(R.string.msg_no_item_found),
                style = typography.bodyMedium,
                color = appColors.textSecondary
            )
        }
    }
}

private fun advertisingItemIcon(iconName: String): ImageVector {
    return when (iconName) {
        "stand" -> Icons.Filled.Storefront
        "brochure" -> Icons.AutoMirrored.Filled.MenuBook
        "campaign" -> Icons.Filled.Campaign
        "location" -> Icons.Filled.LocationOn
        else -> Icons.Filled.Inventory2
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun AdvertisingItemsScreenPreview() {
    AppScreenPreview {
        AdvertisingItemsScreen(
            onBackClick = {},
            onAddItemClick = {},
            onEditItemClick = {}
        )
    }
}
