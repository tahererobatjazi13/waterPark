package ir.kitgroup.hotel.feature.collaborative_collection.ui

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ir.kitgroup.hotel.R
import ir.kitgroup.hotel.core.ui.components.CollectionStatusBadge
import ir.kitgroup.hotel.core.ui.components.CustomHeader
import ir.kitgroup.hotel.core.ui.components.FilterChip
import ir.kitgroup.hotel.core.ui.components.FilterSection
import ir.kitgroup.hotel.core.ui.components.LocationRow
import ir.kitgroup.hotel.core.ui.components.Rating
import ir.kitgroup.hotel.core.ui.model.FilterItem
import ir.kitgroup.hotel.core.ui.theme.HotelTheme
import ir.kitgroup.hotel.core.ui.util.CollectionStatus
import ir.kitgroup.hotel.feature.collaborative_collection.model.CollectionModel

@Composable
fun CollaborativeCollectionsScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(true) }

    val collections = listOf(

        CollectionModel(
            1,
            "هتل پارسیان آزادی", "مشهد،یوسفی", CollectionStatus.ACTIVE,
            5
        ),
        CollectionModel(
            2,
            " مجموعه پالاس", "مشهد،قاسم آباد", CollectionStatus.ACTIVE, 4
        ),
        CollectionModel(3, "سازمان نوید", "مشهد،پیروزی", CollectionStatus.ACTIVE, 3),
        CollectionModel(4, "هتل مرکزی", "مشهد، امام رضا", CollectionStatus.ACTIVE, 5),
        CollectionModel(5, "کیوسک اطلس", "مشهد، کوهسنگی", CollectionStatus.ACTIVE, 3),
        CollectionModel(6, "هتل الماس", "مشهد، پاستور", CollectionStatus.ACTIVE, 2),
        CollectionModel(7, "هتل وفا", "مشهد، وکیال آباد", CollectionStatus.ACTIVE, 3),
        CollectionModel(8, "سازمان مهندسی", "مشهد، فاطمی", CollectionStatus.ACTIVE, 4),
        CollectionModel(9, "هتل امیر", "مشهد، رضاییه", CollectionStatus.ACTIVE, 2),
        CollectionModel(10, "آپارتمان ملل", "مشهد، ستاری", CollectionStatus.ACTIVE, 3),
        CollectionModel(11, "مهمانسرا اسپیناس", "مشهد، مرکزی", CollectionStatus.INACTIVE, 5),
        CollectionModel(12, "چالیدره", "مشهد، طرقبه", CollectionStatus.INACTIVE, 2)
    )

    val filteredList =
        if (selectedTab)
            collections.filter { it.status == CollectionStatus.ACTIVE }
        else
            collections.filter { it.status == CollectionStatus.INACTIVE }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        CustomHeader(
            title = R.string.label_collaborative_collections_list,
            showBackButton = false
        )
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp)
            ) {
                FilterSection()

                Spacer(Modifier.height(8.dp))

                CollectionsFiltersRow()

                Spacer(Modifier.height(8.dp))

                CollectionsTabSwitcher(
                    activeCount = collections.count { it.status == CollectionStatus.ACTIVE },
                    inactiveCount = collections.count { it.status == CollectionStatus.INACTIVE },
                    selectedTab = selectedTab,
                    onTabChange = { selectedTab = it }
                )

                Spacer(Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                ) {
                    items(filteredList) { collection ->
                        CollectionsCard(collection)
                    }

                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
fun CollectionsFiltersRow() {
    val filters = listOf(
        FilterItem(R.string.label_rating, Icons.Default.Star),
        FilterItem(R.string.label_visitor, Icons.Default.Person),
        FilterItem(R.string.label_region, Icons.Default.LocationCity)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
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

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(
                containerColor = HotelTheme.colors.infoContainer,
                contentColor = HotelTheme.colors.onInfoContainer
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
                tint = HotelTheme.colors.info,
                modifier = Modifier.size(14.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = stringResource(R.string.label_filters),
                color = HotelTheme.colors.info,
                style = typography.labelSmall
            )
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
            title = R.string.label_inactive_collections,
            count = inactiveCount.toString(),
            isSelected = !selectedTab,
            modifier = Modifier.weight(1f),
            onClick = { onTabChange(false) }
        )

        TabItem(
            title = R.string.label_active_collections,
            count = activeCount.toString(),
            isSelected = selectedTab,
            modifier = Modifier.weight(1f),
            onClick = { onTabChange(true) }
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
        if (isSelected) HotelTheme.colors.infoContainer
        else HotelTheme.colors.ratingInactive

    val badgeContentColor =
        if (isSelected) HotelTheme.colors.info
        else MaterialTheme.colorScheme.onSurfaceVariant

    val titleColor =
        if (isSelected) MaterialTheme.colorScheme.primary
        else HotelTheme.colors.textSecondary

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

        Spacer(Modifier.width(8.dp))

        Text(
            text = stringResource(title),
            color = titleColor,
            style = typography.bodySmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        )
    }
}

@Composable
fun CollectionsCard(collection: CollectionModel) {

    Card(
        modifier = Modifier.fillMaxWidth(),
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

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp),
                horizontalAlignment = Alignment.End
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    CollectionStatusBadge(collection.status)
                    Text(
                        text = collection.name,
                        style = typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(Modifier.height(4.dp))
                Rating(collection.rating)
                Spacer(Modifier.height(6.dp))
                LocationRow(location = collection.location)
            }

            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}