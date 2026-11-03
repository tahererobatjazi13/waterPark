package ir.kitgroup.partnerManagement.feature.organization.ui.detail.stand

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.model.AssignedStandWithDetail
import ir.kitgroup.partnerManagement.core.ui.components.EmptyState
import ir.kitgroup.partnerManagement.core.ui.components.OfflinePendingFooterBanner
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.AssignmentMode
import ir.kitgroup.partnerManagement.core.ui.util.AssignmentType
import ir.kitgroup.partnerManagement.core.ui.util.StandAssignmentStatus
import ir.kitgroup.partnerManagement.core.ui.util.SyncStatus
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate

@Composable
fun OrganizationStandsTabContent(
    organizationId: String,
    onAssignStandsClick: () -> Unit = {},
    onViewItemDetailsClick: (AssignedStandWithDetail) -> Unit,
    viewModel: OrganizationStandsViewModel = hiltViewModel()
) {
    val appColors = LocalPartnerManagementColors.current
    val assignments by viewModel.assignedStands.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = organizationId) {
        viewModel.observeOrganizationContracts(organizationId)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // هدر تب: عنوان و شمارنده تعداد استندهای تخصیص‌یافته
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SectionTitle(title = stringResource(R.string.label_assignment_stands_to_organization_list))
                    if (assignments.isNotEmpty()) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = assignments.size.toString(),
                                style = typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            if (assignments.isEmpty()) {
                EmptyState(
                    textRes = R.string.msg_no_stand_found,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues( bottom = 84.dp)
                ) {
                    items(
                        items = assignments,
                        key = { it.assignedStand.assignedStandId }
                    ) { item ->
                        AdvertisingStandAssignmentCard(
                            itemWithDetail = item,
                            onViewDetailsClick = { onViewItemDetailsClick(item) }
                        )
                    }
                }
            }
        }

        // دکمه شناور افزودن تخصیص در پایین صفحه
        ExtendedFloatingActionButton(
            onClick = onAssignStandsClick,
            containerColor = appColors.success,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = RoundedCornerShape(16.dp),
            icon = { Icon(Icons.Default.Add, contentDescription = null) },
            text = {
                Text(
                    text = stringResource(R.string.label_new_allocation),
                    style = typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}

@Composable
private fun AdvertisingStandAssignmentCard(
    itemWithDetail: AssignedStandWithDetail,
    onViewDetailsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme
    val assignment = itemWithDetail.assignedStand
    val isPendingSync = assignment.syncStatus != SyncStatus.SYNCED

    val assignmentTypeTitle = AssignmentType.fromId(assignment.assignmentType)?.let {
        stringResource(it.titleRes)
    } ?: assignment.assignmentTypeName ?: "-"

    val assignmentModeTitle = AssignmentMode.fromId(assignment.assignmentMode)?.let {
        stringResource(it.titleRes)
    } ?: assignment.assignmentModeName ?: "-"

    val displayDate = assignment.deliveryToOrgDate
        ?: assignment.assignmentDate
        ?: "-"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onViewDetailsClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(
            width = if (isPendingSync) 1.dp else 0.7.dp,
            color = if (isPendingSync) appColors.warning.copy(alpha = 0.75f) else appColors.border
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // ۱. هدر کارت: عنوان استند و وضعیت تخصیص
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(colors.primaryContainer, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Devices,
                            contentDescription = null,
                            tint = colors.onPrimaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = itemWithDetail.standTitle,
                            style = typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = appColors.textPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = assignmentTypeTitle,
                            style = typography.bodySmall,
                            color = colors.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                assignment.statusAssign?.let { statusId ->
                    StatusBadge(status = StandAssignmentStatus.fromId(statusId))
                }
            }

            HorizontalDivider(
                thickness = 0.6.dp,
                color = colors.outlineVariant.copy(alpha = 0.4f)
            )

            // ۲. کانتینر اطلاعات میانی کارت
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(colors.surfaceVariant.copy(alpha = 0.35f))
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StandCardField(
                        icon = Icons.Default.SwapHoriz,
                        label = stringResource(R.string.label_assignment_mode),
                        value = assignmentModeTitle,
                        modifier = Modifier.weight(1f)
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = colors.secondaryContainer
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Inventory2,
                                contentDescription = null,
                                tint = colors.onSecondaryContainer,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "${assignment.count ?: 0} ${stringResource(R.string.label_unit_count)}",
                                style = typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = colors.onSecondaryContainer
                            )
                        }
                    }
                }

                assignment.description?.takeIf { it.isNotBlank() }?.let { desc ->
                    StandCardField(
                        icon = Icons.Default.Notes,
                        label = stringResource(R.string.label_description),
                        value = desc
                    )
                }
            }

            // ۳. فوتر کارت: تاریخ تحویل و دکمه راهنما جهت مشاهده جزئیات
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = appColors.textSecondary
                    )
                    Text(
                        text = stringResource(R.string.label_delivery_organization_date) + ": " + formatJalaliDate(displayDate),
                        style = typography.labelSmall,
                        color = appColors.textSecondary
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = stringResource(R.string.label_show_details),
                        style = typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = colors.primary
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = colors.primary
                    )
                }
            }

            if (isPendingSync) {
                Spacer(modifier = Modifier.height(2.dp))
                OfflinePendingFooterBanner()
            }
        }
    }
}

@Composable
private fun StandCardField(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colors.primary,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = "$label:",
            style = typography.labelSmall,
            color = appColors.textSecondary
        )
        Text(
            text = value,
            style = typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = appColors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
