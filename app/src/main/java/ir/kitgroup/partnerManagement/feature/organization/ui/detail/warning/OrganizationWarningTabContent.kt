package ir.kitgroup.partnerManagement.feature.organization.ui.detail.warning

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.TrendingFlat
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.model.WarningWithDetail
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.DeleteConfirmationDialog
import ir.kitgroup.partnerManagement.core.ui.components.EmptyState
import ir.kitgroup.partnerManagement.core.ui.components.OfflinePendingFooterBanner
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.theme.PartnerManagementTheme
import ir.kitgroup.partnerManagement.core.ui.util.SyncStatus
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate

@Composable
fun OrganizationWarningTabContent(
    organizationId: String,
    isSupervisor: Boolean,
    organizationWarningViewModel: OrganizationWarningViewModel = hiltViewModel(),
    onAddWarningClick: () -> Unit = {},
    onEditWarningClick: (WarningWithDetail) -> Unit = {},
    onWarningSuccess: () -> Unit = {},
    onDeleteWarningClick: (WarningWithDetail) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val appColors = LocalPartnerManagementColors.current
    var showDialog by remember { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }

    val warnings by organizationWarningViewModel.warnings.collectAsStateWithLifecycle()
    val uiState by organizationWarningViewModel.uiState.collectAsStateWithLifecycle()
    var warningPendingDelete by remember { mutableStateOf<WarningWithDetail?>(null) }

    LaunchedEffect(organizationId) {
        organizationWarningViewModel.observeOrganizationWarnings(organizationId)
    }

    LaunchedEffect(Unit) {
        organizationWarningViewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowMessage -> {
                    snackBarHostState.showSnackbar(event.message.asString(context))
                }

                is UiEvent.ShowError -> {
                    snackBarHostState.showSnackbar(event.error.asString(context))
                }
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // هدر تمیز همراه با عنوان و دکمه استاندارد ثبت تذکر
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    SectionTitle(title = stringResource(R.string.label_warnings_organization))
                }

                FilledTonalButton(
                    onClick = {
                        organizationWarningViewModel.resetForm()
                        showDialog = true
                        onAddWarningClick()
                    },
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.label_register_organization_warning),
                        style = typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (warnings.isEmpty()) {
                EmptyState(
                    textRes = R.string.label_no_warning_added,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    itemsIndexed(
                        items = warnings,
                        key = { _, item -> item.warning.warningId }
                    ) { index, item ->
                        OrganizationWarningCard(
                            index = index,
                            warning = item,
                            isSupervisor = isSupervisor,
                            onEditClick = {
                                organizationWarningViewModel.setupForEdit(item)
                                showDialog = true
                                onEditWarningClick(item)
                            },
                            onDeleteClick = {
                                warningPendingDelete = item
                                onDeleteWarningClick(item)
                            }
                        )
                    }
                }
            }
        }
    }

    // دیالوگ ثبت و ویرایش تذکر
    if (showDialog) {
        key(uiState.warningRecordId ?: "new_warning") {
            AddEditOrganizationWarningDialog(
                warningTypes = uiState.warningTypes,
                isSubmitting = uiState.isLoading,
                initialWarningId = uiState.warningTypeId.takeIf { it.isNotBlank() },
                initialDescription = uiState.description,
                onDismiss = { showDialog = false },
                onConfirm = { warningId, description ->
                    organizationWarningViewModel.submitWarning(
                        warningTypeId = warningId,
                        description = description,
                        onSuccessCallback = {
                            showDialog = false
                            onWarningSuccess()
                        }
                    )
                }
            )
        }
    }

    // دیالوگ تأیید حذف
    warningPendingDelete?.let { item ->
        DeleteConfirmationDialog(
            itemType = stringResource(R.string.label_warnings),
            itemName = item.warningTypeName,
            onConfirm = {
                organizationWarningViewModel.deleteWarning(item.warning.warningId)
                warningPendingDelete = null
            },
            onDismiss = {
                warningPendingDelete = null
            }
        )
    }
}

@Composable
private fun OrganizationWarningCard(
    index: Int,
    warning: WarningWithDetail,
    isSupervisor: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme
    val isPendingSync = warning.warning.syncStatus != SyncStatus.SYNCED

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(
            width = if (isPendingSync) 1.dp else 0.7.dp,
            color = if (isPendingSync) appColors.warning.copy(alpha = 0.65f) else appColors.border
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // ردیف عنوان تذکر، امتیاز و اکشن‌ها
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(colors.tertiaryContainer, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.RateReview,
                            contentDescription = null,
                            tint = colors.onTertiaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${index + 1}. ${warning.warningTypeName}".trim(),
                            style = typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = appColors.textPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // بج امتیاز تذکر
                WarningScoreChip(score = warning.warning.score ?: 0)
            }

            // باکس متن توضیحات (در صورت وجود)
            warning.warning.description?.takeIf { it.isNotBlank() }?.let { description ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(colors.surfaceVariant.copy(alpha = 0.35f))
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Notes,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(top = 2.dp)
                            .size(16.dp),
                        tint = colors.primary
                    )
                    Text(
                        text = description,
                        style = typography.bodyMedium,
                        color = appColors.textPrimary,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // اطلاعات پایین کارت: ثبت‌کننده، تاریخ و دکمه‌های عملیاتی
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ثبت‌کننده و تاریخ
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (warning.visitorName.isNotBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = appColors.textSecondary
                            )
                            Text(
                                text = warning.visitorName,
                                style = typography.labelSmall,
                                color = appColors.textSecondary
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = appColors.textSecondary
                        )
                        Text(
                            text = formatJalaliDate(warning.warning.warningDate),
                            style = typography.labelSmall,
                            color = appColors.textSecondary
                        )
                    }
                }

                // دکمه‌های اکشن ویرایش و حذف
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = colors.primary.copy(alpha = 0.1f),
                        modifier = Modifier.size(34.dp)
                    ) {
                        IconButton(onClick = onEditClick) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = stringResource(R.string.label_edit),
                                tint = colors.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                 /*   if (isSupervisor) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = colors.error.copy(alpha = 0.1f),
                            modifier = Modifier.size(34.dp)
                        ) {
                            IconButton(onClick = onDeleteClick) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = stringResource(R.string.label_delete),
                                    tint = colors.error,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }*/
                }
            }

            // بنر وضعیت سینک در حالت آفلاین
            if (isPendingSync) {
                Spacer(modifier = Modifier.height(2.dp))
                OfflinePendingFooterBanner()
            }
        }
    }
}

@Composable
private fun WarningScoreChip(
    score: Int
) {
    val isPositive = score > 0
    val isZero = score == 0

    val chipColor = when {
        isPositive -> PartnerManagementTheme.colors.successContainer
        isZero -> MaterialTheme.colorScheme.surfaceVariant
        else -> PartnerManagementTheme.colors.errorContainer
    }

    val contentColor = when {
        isPositive -> PartnerManagementTheme.colors.success
        isZero -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> PartnerManagementTheme.colors.error
    }

    val icon = when {
        isPositive -> Icons.Default.ArrowUpward
        isZero -> Icons.Default.TrendingFlat
        else -> Icons.Default.ArrowDownward
    }

    val scoreText = when {
        score > 0 -> "+$score"
        else -> "$score"
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = chipColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = "$scoreText امتیاز",
                style = typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        }
    }
}
