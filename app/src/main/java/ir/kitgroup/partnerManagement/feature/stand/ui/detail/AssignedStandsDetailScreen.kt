package ir.kitgroup.partnerManagement.feature.stand.ui.detail

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.StandTypeEntity
import ir.kitgroup.partnerManagement.core.database.model.AssignedStandWithDetail
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomOutlinedButton
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.AssignmentMode
import ir.kitgroup.partnerManagement.core.ui.util.AssignmentType
import ir.kitgroup.partnerManagement.core.ui.util.MeetingStatus
import ir.kitgroup.partnerManagement.core.ui.util.StandAssignmentStatus
import ir.kitgroup.partnerManagement.core.ui.util.StandType
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate


@Composable
fun AssignedStandsDetailRoute(
    onEditClick: (String) -> Unit = {},
    onStatusChanged: (Int) -> Unit = {},
    onNavigateBack: () -> Unit = {}, // اضافه شدن کال‌بک بازگشت
    modifier: Modifier = Modifier,
    viewModel: AssignedStandsDetailViewModel = hiltViewModel()
) {
    val allocationDetail by viewModel.standDetail.collectAsStateWithLifecycle()
    val isUpdatingStatus by viewModel.isUpdatingStatus.collectAsStateWithLifecycle()

    allocationDetail?.let { detail ->
        AssignedStandsDetailScreen(
            allocationWithDetail = detail,
            onEditClick = onEditClick,
            onNavigateBack = onNavigateBack,
            onStatusChanged = { newStatusId, onDismissDialog ->
                viewModel.changeStatusAssignStand(
                    newStatusId = newStatusId,
                    onSuccess = {
                        onDismissDialog()
                    }
                )
            }, isUpdatingStatus = isUpdatingStatus,
            modifier = modifier
        )
    } ?: Box(
        modifier = modifier
            .fillMaxSize()
            .background(LocalPartnerManagementColors.current.screenBackground),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun AssignedStandsDetailScreen(
    allocationWithDetail: AssignedStandWithDetail,
    onEditClick: (String) -> Unit,
    onNavigateBack: () -> Unit,
    onStatusChanged: (Int, () -> Unit) -> Unit,
    isUpdatingStatus: Boolean = false,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    val allocation = allocationWithDetail.assignedStand
    val standType = allocationWithDetail.standType
    val standCount = allocationWithDetail.assignedStand.count
    var showStatusDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(

        bottomBar = {
            if (allocationWithDetail != null) {
                Surface(
                    color = appColors.screenBackground,
                    tonalElevation = 4.dp,
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // دکمه ویرایش بازدید
                        CustomButton(
                            text = stringResource(R.string.label_edit_stand),
                            onClick = { onEditClick(allocationWithDetail.assignedStand.assignedStandId) },
                            fillMaxWidth = false,
                            modifier = Modifier.weight(1f),
                            height = 42.dp,
                            cornerRadius = 12.dp,
                            textStyle = typography.titleLarge,
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )

                        // دکمه تعیین وضعیت بازدید
                        CustomButton(
                            text = stringResource(R.string.label_determination_status),
                            onClick = {
                                if (!isUpdatingStatus) {
                                    showStatusDialog = true
                                }
                            },
                            fillMaxWidth = false,
                            modifier = Modifier.weight(1f),
                            height = 42.dp,
                            cornerRadius = 12.dp,
                            textStyle = typography.titleLarge,
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = appColors.error,
                                contentColor = appColors.errorContainer
                            )
                        )
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            shape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp
            ),
            color = MaterialTheme.colorScheme.background
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
                // ۱. هدر اصلی (شامل عنوان استند، ماهیت تخصیص و وضعیت)
                item {
                    AssignmentHeaderCard(
                        allocationWithDetail = allocationWithDetail
                    )
                }

                // ۲. اطلاعات طرفین و تاریخ تحویل (سازمان/بازاریاب)
                item {
                    AssignmentInformationCard(
                        assignedStandDetail = allocationWithDetail
                    )
                }

                // ۳. هدر لیست آیتم‌های استند تخصیص‌یافته
                item {
                    AllocatedItemsSectionHeader(
                        itemTypesCount = if (standType != null) 1 else 0,
                        totalCount = allocation.count ?: 1
                    )
                }

                // ۴. جزئیات کامل نوع استند واکشی‌شده از دیتابیس (Relation)
                item {
                    if (standType != null) {
                        AllocatedStandItemCard(item = standType, standCount!!)
                    } else {
                        EmptyAllocatedItemsCard()
                    }
                }

                // ۵. کارت توضیحات تکمیلی (در صورت وجود)
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

// دیالوگ تغییر وضعیت
    if (showStatusDialog) {
        val initialStatus = StandAssignmentStatus.fromId(allocation.statusAssign)
        ChangeAssignedStandStatusDialog(
            initialStatus = initialStatus,
            isSaving = isUpdatingStatus,
            onDismiss = { showStatusDialog = false },
            onConfirm = { selectedStatus ->
                showStatusDialog = false
                onStatusChanged(selectedStatus.id) {}
                onNavigateBack()
            }
        )
    }
}

/**
 * دیالوگ انتخاب وضعیت جدید بازدید
 */
@Composable
fun ChangeAssignedStandStatusDialog(
    initialStatus: StandAssignmentStatus,
    isSaving: Boolean = false,
    onDismiss: () -> Unit,
    onConfirm: (StandAssignmentStatus) -> Unit
) {
    var selectedStatus by rememberSaveable { mutableStateOf(initialStatus) }
    val statusEntries = remember { StandAssignmentStatus.entries }
    val statusTitles = statusEntries.map { stringResource(it.titleRes) }

    Dialog(
        onDismissRequest = {
            if (!isSaving) onDismiss()
        }
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "تعیین وضعیت استند",
                    style = typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                DropdownSelectorField(
                    modifier = Modifier.fillMaxWidth(),
                    value = stringResource(selectedStatus.titleRes),
                    label = stringResource(R.string.label_status),
                    placeholder = "",
                    items = statusTitles,
                    isRequired = true,
                    onItemSelected = { selectedTitle ->
                        val index = statusTitles.indexOf(selectedTitle)
                        if (index >= 0) {
                            selectedStatus = statusEntries[index]
                        }
                    }
                )

                if (isSaving) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CustomButton(
                        text = stringResource(R.string.label_registration),
                        enabled = !isSaving,
                        onClick = { onConfirm(selectedStatus) },
                        modifier = Modifier.weight(1f)
                    )

                    CustomOutlinedButton(
                        text = stringResource(R.string.label_cancel),
                        enabled = !isSaving,
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun AssignmentHeaderCard(
    allocationWithDetail: AssignedStandWithDetail,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    val allocation = allocationWithDetail.assignedStand

    val assignmentTypeTitle = AssignmentType.fromId(allocation.assignmentType)?.let {
        stringResource(it.titleRes)
    } ?: allocation.assignmentTypeName ?: "-"

    val assignmentModeTitle = AssignmentMode.fromId(allocation.assignmentMode)?.let {
        stringResource(it.titleRes)
    } ?: allocation.assignmentModeName ?: "-"

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
                    Text(
                        text = allocationWithDetail.standTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = appColors.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    /*
                                        text = "$assignmentTypeTitle | $assignmentModeTitle",
                    */

                    Text(
                        text = "$assignmentModeTitle",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    allocation.statusAssign?.let { statusId ->
                        StatusBadge(status = StandAssignmentStatus.fromId(statusId))
                    }
                }

            }

            /*       HorizontalDivider(
                       thickness = 0.7.dp,
                       color = appColors.border
                   )

                   Row(
                       modifier = Modifier.fillMaxWidth(),
                       horizontalArrangement = Arrangement.spacedBy(12.dp)
                   ) {
                       AssignmentSummaryItem(
                           title = stringResource(R.string.label_item_types_count),
                           value = if (allocationWithDetail.standType != null) "1" else "0",
                           modifier = Modifier.weight(1f)
                       )

                       AssignmentSummaryItem(
                           title = stringResource(R.string.label_total_allocated_count),
                           value = (allocation.count ?: 0).toString(),
                           modifier = Modifier.weight(1f)
                       )
                   }*/
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
    assignedStandDetail: AssignedStandWithDetail,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    val displayOrgName = assignedStandDetail.organizationName.takeIf { it.isNotBlank() }

    val displayVisitorName = assignedStandDetail.visitorName?.takeIf { it.isNotBlank() }

    val deliveryToOrgDate = assignedStandDetail.assignedStand.deliveryToOrgDate ?: "-"

    val deliveryToVisitorDate = assignedStandDetail.assignedStand.deliveryToVisitorDate ?: "-"


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

            // نمایش نام سازمان در صورت تخصیص به سازمان
            displayOrgName?.let { org ->
                AssignmentDetailRow(
                    icon = Icons.Default.Business,
                    label = stringResource(R.string.label_organization_name),
                    value = org
                )
            }
            // تاریخ تحویل/تخصیص
            AssignmentDetailRow(
                icon = Icons.Default.CalendarMonth,
                label = stringResource(R.string.label_delivery_organization_date),
                value = formatJalaliDate(deliveryToOrgDate)
            )
            // نمایش نام بازاریاب در صورت وجود
            displayVisitorName?.let { visitor ->
                AssignmentDetailRow(
                    icon = Icons.Default.PersonOutline,
                    label = stringResource(R.string.label_choose_visitor_recipient),
                    value = visitor
                )
            }
            // تاریخ تحویل/تخصیص
            AssignmentDetailRow(
                icon = Icons.Default.CalendarMonth,
                label = stringResource(R.string.label_delivery_visitor_date),
                value = formatJalaliDate(deliveryToVisitorDate)
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
                style = MaterialTheme.typography.labelSmall,
                color = appColors.textSecondary
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
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
            imageVector = Icons.Default.Devices,
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

        /* Text(
             text = stringResource(
                 R.string.label_items_count_summary,
                 itemTypesCount,
                 totalCount
             ),
             style = MaterialTheme.typography.labelMedium,
             color = appColors.textSecondary
         )*/
    }
}

@Composable
private fun AllocatedStandItemCard(
    item: StandTypeEntity,
    standCount: Int,
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
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
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
                        text = item.title ?: "-",
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

                //     if (!item.code.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .background(
                            color = appColors.cardBackgroundAlt,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = standCount.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        color = appColors.textSecondary
                    )
                }
                //  }
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
/*

@Preview(
    showBackground = true,
    widthDp = 412,
    heightDp = 915
)
@Composable
private fun AssignedStandsDetailScreenPreview() {
    AppScreenPreview {
        AssignedStandsDetailScreen(
            allocationWithDetail = AssignedStandWithDetail(
                assignedStand = AssignedStandEntity(
                    assignedStandId = "1",
                    organizationId = "org_101",
                    organizationName = "هتل بین‌المللی آزادی",
                    visitorId = "vis_202",
                    visitorName = "علی محمدی",
                    deliveryToOrgDate = "۱۴۰۳/۰۳/۲۵",
                    count = 4,
                    assignmentType = 2,
                    assignmentMode = 1,
                    statusAssign = 1,
                    description = "استندها تحویل مسئول تشریفات هتل گردید."
                ),
                standType = StandTypeEntity(
                    standTypeId = "st_01",
                    title = "استند ایستاده A4",
                    code = "STAND-FL-A4",
                    standType = 2
                )
            ),
            onBackClick = {}
        )
    }
}
*/
