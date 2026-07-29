package ir.kitgroup.partnerManagement.feature.collaborative_collection.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.FilterChip
import ir.kitgroup.partnerManagement.core.ui.components.FilterSection
import ir.kitgroup.partnerManagement.core.ui.components.LocationRow
import ir.kitgroup.partnerManagement.core.ui.components.Rating
import ir.kitgroup.partnerManagement.core.ui.model.FilterItem
import ir.kitgroup.partnerManagement.core.ui.theme.PartnerManagementTheme
import ir.kitgroup.partnerManagement.core.ui.util.Status
import ir.kitgroup.partnerManagement.feature.collaborative_collection.model.CollectionModel

@Composable
fun CollaborativeCollectionsScreen(
    onCollectionClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }

    val collections = listOf(

        CollectionModel(
            1,
            "هتل پارسیان آزادی", "مشهد،یوسفی", Status.ACTIVE,
            5
        ),
        CollectionModel(
            2,
            " مجموعه پالاس", "مشهد،قاسم آباد", Status.ACTIVE, 4
        ),
        CollectionModel(3, "سازمان نوید", "مشهد،پیروزی", Status.ACTIVE, 3),
        CollectionModel(4, "هتل مرکزی", "مشهد، امام رضا", Status.ACTIVE, 5),
        CollectionModel(5, "کیوسک اطلس", "مشهد، کوهسنگی", Status.ACTIVE, 3),
        CollectionModel(6, "هتل الماس", "مشهد، پاستور", Status.ACTIVE, 2),
        CollectionModel(7, "هتل وفا", "مشهد، وکیال آباد", Status.ACTIVE, 3),
        CollectionModel(8, "سازمان مهندسی", "مشهد، فاطمی", Status.ACTIVE, 4),
        CollectionModel(9, "هتل امیر", "مشهد، رضاییه", Status.ACTIVE, 2),
        CollectionModel(10, "آپارتمان ملل", "مشهد، ستاری", Status.ACTIVE, 3),
        CollectionModel(11, "مهمانسرا اسپیناس", "مشهد، مرکزی", Status.INACTIVE, 5),
        CollectionModel(12, "چالیدره", "مشهد، طرقبه", Status.INACTIVE, 2)
    )

    val filteredList =
        if (selectedTab)
            collections.filter { it.status == Status.ACTIVE }
        else
            collections.filter { it.status == Status.INACTIVE }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        CustomHeader(
            title = R.string.label_collaborative_collections_list,
            showBackButton = false
        )
        Surface(
            modifier = modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp)
            ) {
                FilterSection(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    hint = stringResource(R.string.label_search)
                )

                Spacer(modifier.height(8.dp))

                CollectionsFiltersRow()

                Spacer(modifier.height(8.dp))

                CollectionsTabSwitcher(
                    activeCount = collections.count { it.status == Status.ACTIVE },
                    inactiveCount = collections.count { it.status == Status.INACTIVE },
                    selectedTab = selectedTab,
                    onTabChange = { selectedTab = it }
                )

                Spacer(modifier.height(8.dp))

                LazyColumn(
                    modifier = modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                ) {
                    items(
                        items = filteredList,
                        key = { it.id }
                    ) { collection ->
                        CollectionsCard(
                            collection = collection,
                            onClick = { onCollectionClick(collection.id) }
                        )
                    }
                    item { Spacer(modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
fun CollectionsFiltersRow() {
    val filters = listOf(
        FilterItem(R.string.label_region, Icons.Default.LocationCity),
        FilterItem(R.string.label_visitor, Icons.Default.Person),
        FilterItem(R.string.label_rating, Icons.Default.Star)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(
                containerColor = PartnerManagementTheme.colors.infoContainer,
                contentColor = PartnerManagementTheme.colors.onInfoContainer
            ),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
            modifier = Modifier
                .height(36.dp)
                .wrapContentWidth()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_filter),
                contentDescription = null,
                tint = PartnerManagementTheme.colors.info,
                modifier = Modifier.size(14.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = stringResource(R.string.label_filters),
                color = PartnerManagementTheme.colors.info,
                style = typography.labelSmall
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(filters) { filter ->
                FilterChip(
                    text = stringResource(filter.titleRes),
                    icon = filter.icon
                )
            }
        }
    }
}

@Composable
fun CollectionsTabSwitcher(
    activeCount: Int,
    inactiveCount: Int,
    selectedTab: Boolean,
    onTabChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(12.dp)
            )
            .padding(4.dp)
    ) {
        TabItem(
            title = R.string.label_active_collections,
            count = activeCount.toString(),
            isSelected = selectedTab,
            modifier = Modifier.weight(1f),
            onClick = { onTabChange(true) }
        )
        TabItem(
            title = R.string.label_inactive_collections,
            count = inactiveCount.toString(),
            isSelected = !selectedTab,
            modifier = Modifier.weight(1f),
            onClick = { onTabChange(false) }
        )
    }
}

@Composable
fun TabItem(
    @StringRes title: Int,
    count: String,
    isSelected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    val containerColor =
        if (isSelected) MaterialTheme.colorScheme.surface
        else Color.Transparent

    val badgeContainerColor =
        if (isSelected) PartnerManagementTheme.colors.infoContainer
        else PartnerManagementTheme.colors.ratingInactive

    val badgeContentColor =
        if (isSelected) PartnerManagementTheme.colors.info
        else MaterialTheme.colorScheme.onSurfaceVariant

    val titleColor =
        if (isSelected) MaterialTheme.colorScheme.primary
        else PartnerManagementTheme.colors.textSecondary

    Row(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .background(containerColor)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(title),
            color = titleColor,
            style = typography.bodySmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        )

        Spacer(Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .background(
                    badgeContainerColor,
                    CircleShape
                )
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(
                text = count,
                color = badgeContentColor,
                style = typography.titleSmall
            )
        }
    }
}

@Composable
fun CollectionsCard(
    collection: CollectionModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = collection.name,
                        style = typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    StatusBadge(collection.status)
                }

                Spacer(Modifier.height(4.dp))
                Rating(collection.rating)
                Spacer(Modifier.height(6.dp))
                LocationRow(location = collection.location)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun CollaborativeCollectionsScreenPreview() {
    AppScreenPreview {
        CollaborativeCollectionsScreen(
            onCollectionClick = {}
        )
    }
}
