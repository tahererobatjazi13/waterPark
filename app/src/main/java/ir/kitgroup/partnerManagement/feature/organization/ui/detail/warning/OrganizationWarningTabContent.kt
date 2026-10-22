package ir.kitgroup.partnerManagement.feature.organization.ui.detail.warning

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.model.WarningWithDetail
import ir.kitgroup.partnerManagement.core.ui.components.ActionIconButton
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
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SectionTitle(title = stringResource(R.string.label_warnings_organization))

            CustomButton(
                text = stringResource(R.string.label_register_organization_warning),
                onClick = {
                    organizationWarningViewModel.resetForm()
                    showDialog = true
                    onAddWarningClick()
                },
                fillMaxWidth = true,
                height = 46.dp,
                cornerRadius = 12.dp,
                textStyle = typography.titleLarge,
                icon = Icons.Filled.RateReview,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            )

            if (warnings.isEmpty()) {
                EmptyState(
                    textRes = R.string.label_no_warning_added,
                    icon = Icons.Filled.RateReview,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
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
                            onDeleteClick = { onDeleteWarningClick(item) }
                        )
                    }
                }
            }
        }
    }

    // دیالوگ ثبت و ویرایش تذکر (نمایش بر اساس متغیر showDialog)
    if (showDialog) {
        // استفاده از key جهت ریست شدن کامل فیلدهای دیالوگ متناسب با آیتم انتخابی
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
                organizationWarningViewModel.deleteWarning(
                    item.warning.warningId
                )
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
    val isPendingSync = warning.warning.syncStatus != SyncStatus.SYNCED


    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(
            width = if (isPendingSync) 1.dp else 0.7.dp,
            color = if (isPendingSync) appColors.warning.copy(alpha = 0.65f) else appColors.border
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(MaterialTheme.colorScheme.tertiaryContainer, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.RateReview,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = "${index + 1}_ ${warning.warningTypeName}",
                        style = typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    WarningScoreChip(score = warning.warning.score ?: 0)

                    ActionIconButton(
                        icon = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.label_edit),
                        onClick = onEditClick,
                        tint = MaterialTheme.colorScheme.primary,
                        backgroundColor = MaterialTheme.colorScheme.primaryContainer
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            warning.warning.description?.takeIf { it.isNotBlank() }?.let { description ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = stringResource(R.string.label_description),
                        style = typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = description,
                        style = typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = warning.visitorName,
                        style = typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = formatJalaliDate(warning.warning.warningDate),
                        style = typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (isPendingSync) {
                Spacer(modifier = Modifier.height(10.dp))
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

    val chipColor = if (isPositive) {
        PartnerManagementTheme.colors.successContainer
    } else {
        PartnerManagementTheme.colors.errorContainer
    }

    val contentColor = if (isPositive) {
        PartnerManagementTheme.colors.success
    } else {
        PartnerManagementTheme.colors.error
    }

    val scoreText = when {
        score > 0 -> "+$score امتیاز"
        score < 0 -> "$score امتیاز"
        else -> "0 امتیاز"
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = chipColor
    ) {
        Box(
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 4.dp
            ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = scoreText,
                style = typography.labelMedium,
                color = contentColor
            )
        }
    }
}
