package ir.kitgroup.hotel.feature.advertising.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.kitgroup.hotel.R
import ir.kitgroup.hotel.core.ui.components.CustomHeader

@Composable
fun AdvertisingMenuScreen(
    onBackClick: () -> Unit,
    onManageItemsClick: () -> Unit,
    onAllocateVisitorClick: () -> Unit,
    onAllocateCollectionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CustomHeader(
                title = R.string.label_advertising_stands,
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
            AdvertisingMenuContent(
                onManageItemsClick = onManageItemsClick,
                onAllocateVisitorClick = onAllocateVisitorClick,
                onAllocateCollectionClick = onAllocateCollectionClick,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

}

@Composable
private fun AdvertisingMenuContent(
    onManageItemsClick: () -> Unit,
    onAllocateVisitorClick: () -> Unit,
    onAllocateCollectionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.msg_select_one_of_options),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            AdvertisingMenuCard(
                titleRes = R.string.label_managing_promotional_items,
                subtitleRes = R.string.label_add_edit_view_advertising_items,
                icon = Icons.Filled.Campaign,
                onClick = onManageItemsClick
            )
        }

        item {
            AdvertisingMenuCard(
                titleRes = R.string.label_allocate_advertising_items,
                subtitleRes = R.string.label_allocate_items_to_visitor,
                icon = Icons.Filled.PlaylistAdd,
                onClick = onAllocateVisitorClick
            )
        }


        item {
            AdvertisingMenuCard(
                titleRes = R.string.label_allocate_advertising_items,
                subtitleRes = R.string.label_allocate_items_to_collection,
                icon = Icons.Filled.PlaylistAdd,
                onClick = onAllocateCollectionClick
            )
        }
    }
}

@Composable
private fun AdvertisingMenuCard(
    @StringRes titleRes: Int,
    @StringRes subtitleRes: Int,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    Card(
        onClick = onClick,

        modifier = Modifier
            .fillMaxWidth()
       , shape = RoundedCornerShape(16.dp),
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(52.dp),
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
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(titleRes),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(subtitleRes),
                    style = MaterialTheme.typography.bodySmall,
                    color = colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = null,
                tint = colorScheme.onSurfaceVariant
            )
        }
    }
}
