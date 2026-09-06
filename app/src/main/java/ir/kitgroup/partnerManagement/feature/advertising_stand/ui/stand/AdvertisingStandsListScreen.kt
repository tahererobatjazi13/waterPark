package ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.ActionIconButton
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.DeleteConfirmationDialog
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.feature.advertising_stand.model.AdvertisingStandItem

private val advertisingStandList = listOf(
    AdvertisingStandItem(
        "1", "استند رومیزی",
        "استند رومیزی", "پرتال", "دیجیتال", ""
    ),
    AdvertisingStandItem(
        "2",
        "بروشور معرفی",
        "استند بروشور",
        "ایستاده", "چاپی", "توضیحات استند"
    ),
    AdvertisingStandItem("3", "استند لابی", "کیوسک", "ثابت", "دیجیتال", "")
)

@Composable
fun AdvertisingStandsListScreen(
    onBackClick: () -> Unit,
    onAddItemClick: () -> Unit,
    onEditItemClick: (AdvertisingStandItem) -> Unit,
    onDeleteItemClick: (AdvertisingStandItem) -> Unit,
    modifier: Modifier = Modifier,
    items: List<AdvertisingStandItem> = advertisingStandList
) {
    var standToDelete by remember { mutableStateOf<AdvertisingStandItem?>(null) }

    val appColors = LocalPartnerManagementColors.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CustomHeader(
                title = R.string.label_managing_stands_items,
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
                items = items,
                onAddItemClick = onAddItemClick,
                onEditItemClick = onEditItemClick,
                onDeleteItemClick = { item ->
                    standToDelete = item
                },
                modifier = Modifier.fillMaxSize()
            )

        }
    }
    standToDelete?.let { item ->
        DeleteConfirmationDialog(
            itemType = stringResource(R.string.label_stand),
            itemName = item.name,
            onConfirm = {
                onDeleteItemClick(item)
                standToDelete = null
            },
            onDismiss = {
                standToDelete = null
            }
        )
    }
}


@Composable
private fun AdvertisingItemsContent(
    items: List<AdvertisingStandItem>,
    onAddItemClick: () -> Unit,
    onEditItemClick: (AdvertisingStandItem) -> Unit,
    onDeleteItemClick: (AdvertisingStandItem) -> Unit,
    modifier: Modifier = Modifier
) {

    val appColors = LocalPartnerManagementColors.current

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        CustomButton(
            text = stringResource(R.string.label_add_new_stand),
            onClick = onAddItemClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = appColors.success,
                contentColor = MaterialTheme.colorScheme.onPrimary

            ),
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
                onDeleteItemClick = onDeleteItemClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}

@Composable
private fun AdvertisingItemsList(
    items: List<AdvertisingStandItem>,
    onEditItemClick: (AdvertisingStandItem) -> Unit,
    onDeleteItemClick: (AdvertisingStandItem) -> Unit,
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
                onDeleteClick = { onDeleteItemClick(item) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun AdvertisingItemCard(
    item: AdvertisingStandItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = modifier.fillMaxWidth(),
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
                .padding(16.dp)
        ) {
            Text(
                text = item.name,
                style = typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                InfoBlockWithIcon(
                    label = "${stringResource(R.string.label_stand_type)}: ",
                    value = item.standType,
                    icon = Icons.Default.Inventory2,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                ActionIconButton(
                    icon = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.label_edit),
                    onClick = onEditClick,
                    tint = MaterialTheme.colorScheme.primary,
                    backgroundColor = appColors.cardBackgroundAlt
                )

                Spacer(modifier = Modifier.width(8.dp))

                ActionIconButton(
                    icon = Icons.Default.DeleteOutline,
                    contentDescription = stringResource(R.string.label_delete),
                    onClick = onDeleteClick,
                    tint = MaterialTheme.colorScheme.error,
                    backgroundColor = MaterialTheme.colorScheme.errorContainer
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = 0.5.dp,
                color = appColors.border
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                InfoBlockWithIcon(
                    label = "${stringResource(R.string.label_installation_type)}: ",
                    value = item.installationType,
                    icon = Icons.Default.Build,
                    modifier = Modifier.weight(1f)
                )

                InfoBlockWithIcon(
                    label = "${stringResource(R.string.label_display_type)}: ",
                    value = item.displayType,
                    icon = Icons.Default.Visibility,
                    modifier = Modifier.weight(1f)
                )
            }

            if (item.description.isNotBlank()) {

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.label_description_item),
                        style = typography.labelMedium,
                        color = appColors.textSecondary
                    )

                    Text(
                        text = item.description,
                        style = typography.bodyMedium,
                        color = appColors.textPrimary,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}



@Composable
private fun InfoBlockWithIcon(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(6.dp))

        Column {
            Text(
                text = label,
                style = typography.labelMedium,
                color = appColors.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = value,
                style = typography.bodyMedium,
                color = appColors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
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

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun AdvertisingStandsListScreenPreview() {
    AppScreenPreview {
        AdvertisingStandsListScreen(
            onBackClick = {},
            onAddItemClick = {},
            onEditItemClick = {},
            onDeleteItemClick = {}
        )
    }
}
