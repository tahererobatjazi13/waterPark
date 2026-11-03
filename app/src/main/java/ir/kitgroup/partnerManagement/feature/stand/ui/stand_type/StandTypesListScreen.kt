package ir.kitgroup.partnerManagement.feature.stand.ui.stand_type

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.StandTypeEntity
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.DeleteConfirmationDialog
import ir.kitgroup.partnerManagement.core.ui.components.EmptyState
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.DisplayType
import ir.kitgroup.partnerManagement.core.ui.util.InstallationType

@Composable
fun StandTypesListScreen(
    onAddItemClick: () -> Unit,
    onEditItemClick: (StandTypeEntity) -> Unit,
    onDeleteItemClick: (StandTypeEntity) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StandTypesListViewModel = hiltViewModel()
) {
    val items by viewModel.stands.collectAsState()
    var standToDelete by remember { mutableStateOf<StandTypeEntity?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            AdvertisingItemsContent(
                items = items,
                onAddItemClick = onAddItemClick,
                onEditItemClick = onEditItemClick,
                onDeleteItemClick = { item -> standToDelete = item },
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    standToDelete?.let { item ->
        DeleteConfirmationDialog(
            itemType = stringResource(R.string.label_stand),
            itemName = item.title ?: "",
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
    items: List<StandTypeEntity>,
    onAddItemClick: () -> Unit,
    onEditItemClick: (StandTypeEntity) -> Unit,
    onDeleteItemClick: (StandTypeEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        if (items.isEmpty()) {
            EmptyState(
                textRes = R.string.msg_no_item_found,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // نوار خلاصه تعداد انواع استند ثبت شده
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp, start = 4.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                    Text(
                        text = stringResource(R.string.label_type_assigned_stands),
                        style = typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = appColors.textPrimary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    border = BorderStroke(
                        0.5.dp,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    )
                ) {
                    Text(
                        text = "${items.size} مورد",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

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
    items: List<StandTypeEntity>,
    onEditItemClick: (StandTypeEntity) -> Unit,
    onDeleteItemClick: (StandTypeEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(
            items = items,
            key = { it.standTypeId }
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
    item: StandTypeEntity,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme

    val installationTitle = InstallationType.fromId(item.installationType)?.let {
        stringResource(it.titleRes)
    } ?: "-"

    val displayTitle = DisplayType.fromId(item.displayType)?.let {
        stringResource(it.titleRes)
    } ?: "-"

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(0.7.dp, appColors.border)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ردیف هدر: آیکون، نام و دکمه‌های عملیات
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(
                            color = colors.primaryContainer.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Storefront,
                        contentDescription = null,
                        tint = colors.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.title.orEmpty().ifBlank { "-" },
                        style = typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = appColors.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (!item.standTypeName.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.standTypeName,
                            style = typography.bodySmall,
                            color = appColors.textSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                /*
                                // دکمه‌های عملیاتی
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    ActionIconButton(
                                        icon = Icons.Default.Edit,
                                        contentDescription = stringResource(R.string.label_edit),
                                        onClick = onEditClick,
                                        tint = colors.primary,
                                        backgroundColor = appColors.cardBackgroundAlt
                                    )

                                    ActionIconButton(
                                        icon = Icons.Default.DeleteOutline,
                                        contentDescription = stringResource(R.string.label_delete),
                                        onClick = onDeleteClick,
                                        tint = colors.error,
                                        backgroundColor = colors.errorContainer.copy(alpha = 0.4f)
                                    )
                                }*/
            }

            HorizontalDivider(
                thickness = 0.6.dp,
                color = appColors.border.copy(alpha = 0.6f)
            )

            // باکس‌های مشخصات فنی (نوع نصب و نوع نمایش)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AttributeTile(
                    label = stringResource(R.string.label_installation_type),
                    value = installationTitle,
                    icon = Icons.Default.Build,
                    modifier = Modifier.weight(1f)
                )

                AttributeTile(
                    label = stringResource(R.string.label_display_type),
                    value = displayTitle,
                    icon = Icons.Default.Visibility,
                    modifier = Modifier.weight(1f)
                )
            }

            // باکس توضیحات
            if (!item.description.isNullOrBlank()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = appColors.cardBackgroundAlt,
                    border = BorderStroke(0.5.dp, appColors.border)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notes,
                            contentDescription = null,
                            modifier = Modifier
                                .size(16.dp)
                                .padding(top = 2.dp),
                            tint = appColors.textTertiary
                        )
                        Text(
                            text = item.description,
                            style = typography.bodySmall,
                            color = appColors.textSecondary,
                            lineHeight = typography.bodySmall.lineHeight
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AttributeTile(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = appColors.cardBackgroundAlt,
        border = BorderStroke(0.5.dp, appColors.border)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(colors.surface, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = colors.primary
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = typography.labelSmall,
                    color = appColors.textTertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = value,
                    style = typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = appColors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun StandTypesListScreenPreview() {
    AppScreenPreview {
        StandTypesListScreen(
            onAddItemClick = {},
            onEditItemClick = {},
            onDeleteItemClick = {}
        )
    }
}
