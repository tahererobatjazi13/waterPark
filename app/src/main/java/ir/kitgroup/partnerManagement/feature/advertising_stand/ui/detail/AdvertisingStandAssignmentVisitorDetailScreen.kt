package ir.kitgroup.partnerManagement.feature.advertising_stand.ui.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.Status
import ir.kitgroup.partnerManagement.feature.advertising_stand.model.AdvertisingStandAssignmentDetail
import ir.kitgroup.partnerManagement.feature.advertising_stand.model.AllocatedStandItem
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand_assignment_visitor.allocationItemIcon

@Composable
fun AdvertisingStandAssignmentVisitorDetailScreen(
    allocation: AdvertisingStandAssignmentDetail,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            CustomHeader(
                title = R.string.label_assignment_details,
                showBackButton = true,
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            shape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp
            ),
            color = appColors.screenBackground
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.navigationBars),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    AssignmentHeaderCard(allocation = allocation)
                }

                item {
                    AssignmentInformationCard(allocation = allocation)
                }

                item {
                    AllocatedItemsSectionHeader(
                        itemTypesCount = allocation.items.size,
                        totalCount = allocation.totalCount
                    )
                }

                if (allocation.items.isEmpty()) {
                    item {
                        EmptyAllocatedItemsCard()
                    }
                } else {
                    items(
                        items = allocation.items,
                        key = { item -> item.id }
                    ) { item ->
                        AllocatedStandItemCard(item = item)
                    }
                }

                allocation.description
                    ?.takeIf { it.isNotBlank() }
                    ?.let { description ->
                        item {
                            AssignmentDescriptionCard(
                                description = description
                            )
                        }
                    }
            }
        }
    }
}

@Composable
private fun AssignmentHeaderCard(
    allocation: AdvertisingStandAssignmentDetail,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColors.cardBackground
        ),
        border = BorderStroke(
            width = 1.dp,
            color = appColors.border
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(
                            color = appColors.infoContainer,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Inventory2,
                        contentDescription = null,
                        tint = appColors.onInfoContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // خط اول: نوع اختصاص (عنوان اصلی)
                    Text(
                        text = allocation.assignmentType,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = appColors.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // خط دوم: ماهیت اختصاص
                    Text(
                        text = "${stringResource(R.string.label_assignment_mode)}: ${allocation.assignmentMode}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                StatusBadge(status = allocation.status)
            }

            HorizontalDivider(
                thickness = 0.7.dp,
                color = appColors.border
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AssignmentSummaryItem(
                    title = stringResource(R.string.label_item_types_count),
                    value = allocation.items.size.toString(),
                    modifier = Modifier.weight(1f)
                )

                AssignmentSummaryItem(
                    title = stringResource(R.string.label_total_allocated_count),
                    value = allocation.totalCount.toString(),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun AssignmentSummaryItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Column(
        modifier = modifier
            .background(
                color = appColors.cardBackgroundAlt,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(
                horizontal = 12.dp,
                vertical = 10.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = appColors.textSecondary
        )
    }
}

@Composable
private fun AssignmentInformationCard(
    allocation: AdvertisingStandAssignmentDetail,
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
            width = 1.dp,
            color = appColors.border
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = stringResource(R.string.label_assignment_information),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = appColors.textPrimary
            )

            HorizontalDivider(
                thickness = 0.7.dp,
                color = appColors.border
            )

            AssignmentDetailRow(
                icon = Icons.Default.PersonOutline,
                label = stringResource(R.string.label_choose_visitor_recipient),
                value = allocation.visitorName
            )

            AssignmentDetailRow(
                icon = Icons.Default.CalendarMonth,
                label = stringResource(R.string.label_delivery_visitor_date),
                value = allocation.allocatedDate
            )
        }
    }
}

@Composable
private fun AssignmentDetailRow(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = appColors.textSecondary
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = appColors.textPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun AllocatedItemsSectionHeader(
    itemTypesCount: Int,
    totalCount: Int,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Inventory2,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp)
        )

        Text(
            text = stringResource(R.string.label_allocated_stands),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = appColors.textPrimary,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = stringResource(
                R.string.label_items_count_summary,
                itemTypesCount,
                totalCount
            ),
            style = MaterialTheme.typography.labelMedium,
            color = appColors.textSecondary
        )
    }
}

@Composable
private fun AllocatedStandItemCard(
    item: AllocatedStandItem,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    val itemIcon = allocationItemIcon(item.iconName)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColors.cardBackground
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .background(
                            color = appColors.infoContainer,
                            shape = RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = itemIcon,
                        contentDescription = null,
                        tint = appColors.onInfoContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = appColors.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = item.type,
                        style = MaterialTheme.typography.labelMedium,
                        color = appColors.textSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                ItemCountBadge(count = item.count)
            }
        }
    }
}

@Composable
private fun ItemCountBadge(
    count: Int,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Column(
        modifier = modifier
            .background(
                color = appColors.successContainer,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(
                horizontal = 12.dp,
                vertical = 7.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = appColors.onSuccessContainer
        )

        Text(
            text = stringResource(R.string.label_unit_count),
            style = MaterialTheme.typography.labelSmall,
            color = appColors.onSuccessContainer
        )
    }
}

@Composable
private fun ItemAdditionalInformationRow(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = appColors.textSecondary
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = appColors.textPrimary
            )
        }
    }
}

@Composable
private fun AssignmentDescriptionCard(
    description: String,
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
            width = 1.dp,
            color = appColors.border
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )

                Text(
                    text = stringResource(R.string.label_description),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = appColors.textPrimary
                )
            }

            HorizontalDivider(
                thickness = 0.7.dp,
                color = appColors.border
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = appColors.textSecondary
            )
        }
    }
}

@Composable
private fun EmptyAllocatedItemsCard(
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
            width = 1.dp,
            color = appColors.border
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Inventory2,
                contentDescription = null,
                tint = appColors.textTertiary,
                modifier = Modifier.size(42.dp)
            )

            Text(
                text = stringResource(R.string.msg_no_allocated_stand_found),
                style = MaterialTheme.typography.bodyMedium,
                color = appColors.textSecondary
            )
        }
    }
}

val demoAssignmentDetail = AdvertisingStandAssignmentDetail(
    id = "AS-1403-0012",
    visitorName = "محمد احمدی",
    organizationName = "هتل پارسیان آزادی",
    assignmentType = "تخصیص به بازاریاب",
    assignmentMode = "تبلیغاتی",
    allocatedDate = "۱۴۰۳/۰۳/۲۲",
    status = Status.ACTIVE,
    items = listOf(
        AllocatedStandItem(
            id = "1",
            title = "استند رومیزی معرفی خدمات",
            type = "رومیزی",
            iconName = "stand",
            count = 5,
            code = "ST-TBL-101",
            description = "قابل استفاده در میز پذیرش و لابی هتل"
        ),
        AllocatedStandItem(
            id = "2",
            title = "استند دیواری راهنمای گردشگری",
            type = "دیواری",
            iconName = "wall",
            count = 3,
            code = "ST-WAL-205"
        ),
        AllocatedStandItem(
            id = "3",
            title = "کیوسک تبلیغاتی",
            type = "کیوسک",
            iconName = "kiosk",
            count = 2,
            code = "ST-KSK-307",
            description = "جهت نصب در ورودی اصلی مجموعه"
        )
    ),
    description = "تحویل استندها با هماهنگی مدیر داخلی هتل انجام شده است."
)

@Preview(
    showBackground = true,
    widthDp = 412,
    heightDp = 915
)
@Composable
private fun AdvertisingStandAssignmentVisitorDetailScreenPreview() {
    AppScreenPreview {
        AdvertisingStandAssignmentVisitorDetailScreen(
            allocation = demoAssignmentDetail,
            onBackClick = {}
        )
    }
}
