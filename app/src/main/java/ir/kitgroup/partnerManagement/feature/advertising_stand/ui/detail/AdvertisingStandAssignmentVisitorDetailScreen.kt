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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Storefront
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
import ir.kitgroup.partnerManagement.core.database.entity.AdvertisingStandEntity
import ir.kitgroup.partnerManagement.core.database.entity.StandAssignmentEntity
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.AssignmentMode
import ir.kitgroup.partnerManagement.core.ui.util.AssignmentType
import ir.kitgroup.partnerManagement.core.ui.util.StandAssignmentStatus
import ir.kitgroup.partnerManagement.core.ui.util.StandType

@Composable
fun AdvertisingStandAssignmentVisitorDetailScreen(
    allocation: StandAssignmentEntity,
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
                        totalCount = allocation.count ?: allocation.items.size
                    )
                }

                if (allocation.items.isEmpty()) {
                    item {
                        EmptyAllocatedItemsCard()
                    }
                } else {
                    items(
                        items = allocation.items,
                        key = { item -> item.advertisingStandId }
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
    allocation: StandAssignmentEntity,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    val assignmentTypeTitle = AssignmentType.fromId(allocation.assignmentType)?.let {
        stringResource(it.titleRes)
    } ?: "-"

    val assignmentModeTitle = AssignmentMode.fromId(allocation.assignmentMode)?.let {
        stringResource(it.titleRes)
    } ?: "-"

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
                        text = assignmentTypeTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = appColors.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // خط دوم: ماهیت اختصاص
                    Text(
                        text = "${stringResource(R.string.label_assignment_mode)}: $assignmentModeTitle",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                StatusBadge(status = StandAssignmentStatus.fromId(allocation.status))
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
                    value = (allocation.count ?: 0).toString(),
                    modifier = Modifier.weight(1f)
                )

                AssignmentSummaryItem(
                    title = stringResource(R.string.label_total_allocated_count),
                    value = (allocation.count ?: 0).toString(),
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
    allocation: StandAssignmentEntity,
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
                value = allocation.visitorId ?: "-"
            )

            AssignmentDetailRow(
                icon = Icons.Default.CalendarMonth,
                label = stringResource(R.string.label_delivery_visitor_date),
                value = allocation.assignmentDate ?: "-"
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
    item: AdvertisingStandEntity,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    val standTypeTitle = StandType.fromId(item.standType)?.let {
        stringResource(it.titleRes)
    } ?: item.code.orEmpty()

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
                        imageVector = Icons.Default.Storefront,
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
                        text = item.name ?: "-",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = appColors.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = standTypeTitle,
                        style = MaterialTheme.typography.labelMedium,
                        color = appColors.textSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (!item.code.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = appColors.cardBackgroundAlt,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = item.code,
                            style = MaterialTheme.typography.labelSmall,
                            color = appColors.textSecondary
                        )
                    }
                }
            }
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

val demoAssignmentDetail = StandAssignmentEntity(
    standAssignmentId = "2",
    visitorId = "سارا مرادی",
    organizationId = "هتل اسپیناس پالاس",
    status = 2,
    advertisingStandId = "stand_2",
    count = 3,
    assignmentDate = "۱۴۰۳/۰۳/۲۴",
    assignmentType = 2,
    assignmentMode = 1,
    items = listOf(
        AdvertisingStandEntity(
            advertisingStandId = "1",
            name = "استند رومیزی",
            code = "TABLE_STAND",
            description = "",
            displayType = 0,
            installationType = 5,
            recreationCenterId = "1001",
            standType = 1
        ),
        AdvertisingStandEntity(
            advertisingStandId = "2",
            name = "بروشور معرفی",
            code = "BROCHURE",
            description = "توضیحات استند",
            displayType = 1,
            installationType = 2,
            recreationCenterId = "1001",
            standType = 2
        ),
        AdvertisingStandEntity(
            advertisingStandId = "3",
            name = "استند لابی",
            code = "LOBBY_STAND",
            description = "",
            displayType = 2,
            installationType = 1,
            recreationCenterId = "1002",
            standType = 3
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
