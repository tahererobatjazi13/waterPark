package ir.kitgroup.hotel.feature.advertising.ui.item

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import ir.kitgroup.hotel.core.ui.util.AdvertisingFilterTab
import ir.kitgroup.hotel.feature.advertising.model.AdvertisingItem

private val demoAdvertisingItems = listOf(
    AdvertisingItem(
        id = "1",
        title = "استند رومیزی",
        type = "استند رومیزی",
        stock = 25,
        isActive = true,
        iconName = "stand"
    ),
    AdvertisingItem(
        id = "2",
        title = "بروشور معرفی",
        type = "بروشور",
        stock = 0,
        isActive = false,
        iconName = "brochure"
    ),
    AdvertisingItem(
        id = "3",
        title = "استند لابی",
        type = "استند دیواری",
        stock = 8,
        isActive = true,
        iconName = "location"
    )
)

@Composable
fun AdvertisingItemsScreen(
    onBackClick: () -> Unit,
    onAddItemClick: () -> Unit,
    onEditItemClick: (AdvertisingItem) -> Unit,
    modifier: Modifier = Modifier,
    items: List<AdvertisingItem> = demoAdvertisingItems
) {
    var selectedTab by rememberSaveable {
        mutableStateOf(AdvertisingFilterTab.All)
    }

    val filteredItems = remember(
        selectedTab,
        items
    ) {
        when (selectedTab) {
            AdvertisingFilterTab.All -> {
                items
            }

            AdvertisingFilterTab.Active -> {
                items.filter { item ->
                    item.isActive
                }
            }

            AdvertisingFilterTab.OutOfStock -> {
                items.filter { item ->
                    item.stock == 0
                }
            }
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
                .padding(
                    top = innerPadding.calculateTopPadding()
                ),
            shape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp
            ),
            color = MaterialTheme.colorScheme.background
        ) {
            AdvertisingItemsContent(
                items = filteredItems,
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    selectedTab = tab
                },
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
    Column(
        modifier = modifier.padding(12.dp)
    ) {

        Button(
            onClick = onAddItemClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 12.dp
            )
        ) {
            Text(
                text = stringResource(R.string.label_add_new_item),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

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
private fun AdvertisingItemsList(
    items: List<AdvertisingItem>,
    onEditItemClick: (AdvertisingItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(
            bottom = 16.dp
        )
    ) {
        items(
            items = items,
            key = { item ->
                item.id
            }
        ) { item ->
            AdvertisingItemCard(
                item = item,
                onEditClick = {
                    onEditItemClick(item)
                },
                modifier = Modifier.fillMaxWidth()
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
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = modifier
            .selectableGroup()
            .background(
                color = colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AdvertisingFilterTab.entries.forEach { tab ->
            val isSelected = selectedTab == tab

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .selectable(
                        selected = isSelected,
                        role = Role.Tab,
                        onClick = {
                            onTabSelected(tab)
                        }
                    ),
                shape = RoundedCornerShape(8.dp),
                color = if (isSelected) {
                    colorScheme.primary
                } else {
                    colorScheme.surfaceVariant
                },
                contentColor = if (isSelected) {
                    colorScheme.onPrimary
                } else {
                    colorScheme.onSurfaceVariant
                }
            ) {
                Box(
                    modifier = Modifier.padding(
                        horizontal = 8.dp,
                        vertical = 10.dp
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(tab.titleRes),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isSelected) {
                            FontWeight.SemiBold
                        } else {
                            FontWeight.Normal
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun AdvertisingItemCard(
    item: AdvertisingItem,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val itemIcon = advertisingItemIcon(item.iconName)

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
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            AdvertisingItemIcon(
                icon = itemIcon
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // تایتل اصلی
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // نوع آیتم
                InfoBlock(
                    label = "${stringResource(R.string.label_item_type)}: ",
                    value = item.type
                )

                // موجودی
                InfoBlock(
                    label = "${stringResource(R.string.label_stock)}: ",
                    value = item.stock.toString()
                )

                // وضعیت (بصورت کپسولی)
                AdvertisingItemStatus(
                    stock = item.stock,
                    isActive = item.isActive
                )
            }

            OutlinedButton(
                onClick = onEditClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = colorScheme.primary
                ),
                border = BorderStroke(1.dp, colorScheme.outlineVariant),
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
                    style = MaterialTheme.typography.labelMedium
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
    val colorScheme = MaterialTheme.colorScheme

    Surface(
        modifier = modifier.size(48.dp),
        shape = RoundedCornerShape(14.dp),
        color = colorScheme.primaryContainer,
        contentColor = colorScheme.onPrimaryContainer
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
private fun AdvertisingItemStatus(
    stock: Int,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    val status = when {
        stock == 0 -> AdvertisingStatusUi(
            titleRes = R.string.status_out_of_stock,
            icon = Icons.Filled.RemoveCircle,
            containerColor = Color(0xFFFFE0B2), // light orange
            contentColor = Color(0xFFE65100)    // dark orange
        )

        isActive -> AdvertisingStatusUi(
            titleRes = R.string.status_active,
            icon = Icons.Filled.CheckCircle,
            containerColor = Color(0xFFC8E6C9), // light green
            contentColor = Color(0xFF2E7D32)    // dark green
        )

        else -> AdvertisingStatusUi(
            titleRes = R.string.status_inactive,
            icon = Icons.Filled.Cancel,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(percent = 50),
        color = status.containerColor,
        contentColor = status.contentColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = status.icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )

            Text(
                text = stringResource(status.titleRes),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium
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
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
    }}

@Composable
private fun AdvertisingItemsEmptyState(
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

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
                color = colorScheme.surfaceVariant,
                contentColor = colorScheme.onSurfaceVariant
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
                text = stringResource(R.string.label_no_items_found),
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

private fun advertisingItemIcon(
    iconName: String
): ImageVector {
    return when (iconName) {
        "stand" -> Icons.Filled.Storefront
        "brochure" -> Icons.Filled.MenuBook
        "campaign" -> Icons.Filled.Campaign
        "location" -> Icons.Filled.LocationOn
        else -> Icons.Filled.Inventory2
    }
}

private data class AdvertisingStatusUi(
    val titleRes: Int,
    val icon: ImageVector,
    val containerColor: androidx.compose.ui.graphics.Color,
    val contentColor: androidx.compose.ui.graphics.Color
)
