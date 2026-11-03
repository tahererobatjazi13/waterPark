package ir.kitgroup.partnerManagement.feature.stand.ui.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.window.Dialog
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.StandTypeEntity
import ir.kitgroup.partnerManagement.core.database.model.AssignedStandWithDetail
import ir.kitgroup.partnerManagement.core.ui.components.*
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.*

@Composable
fun AssignedStandsDetailScreen(
    onEditClick: (String) -> Unit = {},
    onStatusChangeSuccess: () -> Unit,
    viewModel: AssignedStandsDetailViewModel = hiltViewModel()
) {
    val standDetail by viewModel.standDetail.collectAsStateWithLifecycle()
    val uiStateChange by viewModel.uiStateChange.collectAsStateWithLifecycle()

    var showStatusDialog by rememberSaveable { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowMessage -> snackBarHostState.showSnackbar(
                    event.message.asString(
                        context
                    )
                )

                is UiEvent.ShowError -> snackBarHostState.showSnackbar(event.error.asString(context))
            }
        }
    }

    val detail = standDetail

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (detail != null) {
                DualActionButtonBottomBar(
                    primaryText = stringResource(R.string.label_allocation_edit),
                    secondaryText = stringResource(R.string.label_determination_status),
                    primaryIcon = Icons.Outlined.Edit,
                    secondaryIcon = Icons.Outlined.Cancel,
                    secondaryVariant = SecondaryButtonVariant.FILLED,
                    buttonHeight = 50.dp,
                    onPrimaryClick = { onEditClick(detail.assignedStand.assignedStandId) },
                    onSecondaryClick = {
                        if (!uiStateChange.isChangeStatus) showStatusDialog = true
                    }
                )
            }
        }
    ) { innerPadding ->
        if (detail == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                BouncingDotsIndicator(
                    modifier = Modifier.size(48.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            val assignedStand = detail.assignedStand
            val standCount = assignedStand.count ?: 1

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(
                    top = innerPadding.calculateTopPadding() + 16.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item(key = "header") { AssignmentHeaderCard(detail) }
                item(key = "info") { AssignmentInformationCard(detail) }

                item(key = "assigned_stand_header") {
                    SectionTitle(title = stringResource(R.string.label_allocated_stands))
                }

                item(key = "assigned_stand_item") {
                    if (detail.standType != null) {
                        AssignedStandItemCard(detail.standType, standCount)
                    } else {
                        EmptyAllocatedItemsCard()
                    }
                }

                assignedStand.description?.takeIf { it.isNotBlank() }?.let { desc ->
                    item(key = "desc") { AssignmentDescriptionCard(desc) }
                }
            }

            if (showStatusDialog) {
                ChangeAssignedStandStatusDialog(
                    initialStatus = StandAssignmentStatus.fromId(assignedStand.statusAssign),
                    isSubmitting = uiStateChange.isChangeStatus,
                    onDismiss = { if (!uiStateChange.isChangeStatus) showStatusDialog = false },
                    onConfirm = { newStatus ->
                        viewModel.changeStatusAssignStand(
                            newStatusId = newStatus.id,
                            onErrorCallback = { showStatusDialog = false },
                            onSuccessCallback = {
                                showStatusDialog = false; onStatusChangeSuccess()
                            }
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun AssignmentHeaderCard(assignedStanDetail: AssignedStandWithDetail) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(0.7.dp, appColors.border)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(colors.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Inventory2,
                    null,
                    tint = colors.onPrimaryContainer,
                    modifier = Modifier.size(26.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    assignedStanDetail.standTitle,
                    style = typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = appColors.textPrimary
                )
                Text(
                    text = AssignmentMode.fromId(assignedStanDetail.assignedStand.assignmentMode)
                        ?.let { stringResource(it.titleRes) } ?: "-",
                    style = typography.bodySmall, color = colors.primary
                )
            }

            assignedStanDetail.assignedStand.statusAssign?.let { statusId ->
                StatusBadge(status = StandAssignmentStatus.fromId(statusId))
            }
        }
    }
}

@Composable
private fun AssignmentInformationCard(detail: AssignedStandWithDetail) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(0.7.dp, appColors.border)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                stringResource(R.string.label_assignment_information),
                style = typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = appColors.textPrimary
            )
            HorizontalDivider(color = appColors.border)

            detail.organizationName.takeIf { it.isNotBlank() }?.let {
                AssignmentDetailRow(
                    Icons.Default.Business,
                    stringResource(R.string.label_organization_name),
                    it
                )
            }
            AssignmentDetailRow(
                Icons.Default.CalendarMonth,
                stringResource(R.string.label_delivery_organization_date),
                formatJalaliDate(detail.assignedStand.deliveryToOrgDate ?: "-")
            )

            detail.visitorName.takeIf { it.isNotBlank() }?.let {
                AssignmentDetailRow(
                    Icons.Default.PersonOutline,
                    stringResource(R.string.label_choose_visitor_recipient),
                    it
                )
            }

            detail.assignedStand.plannedReturnDate?.takeIf { it.isNotBlank() }?.let { plannedDate ->
                AssignmentDetailRow(
                    Icons.Default.CalendarMonth,
                    stringResource(R.string.label_planned_return_date),
                    formatJalaliDate(plannedDate)
                )
            }

            detail.assignedStand.actualReturnDate?.takeIf { it.isNotBlank() }?.let { actualDate ->
                AssignmentDetailRow(
                    Icons.Default.CalendarMonth,
                    stringResource(R.string.label_actual_return_date),
                    formatJalaliDate(actualDate)
                )
            }
        }
    }
}

@Composable
private fun AssignmentDetailRow(icon: ImageVector, label: String, value: String) {
    val appColors = LocalPartnerManagementColors.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
        }
        Column {
            Text(label, style = typography.labelSmall, color = appColors.textSecondary)
            Text(
                value,
                style = typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = appColors.textPrimary
            )
        }
    }
}

@Composable
private fun AssignedStandItemCard(item: StandTypeEntity, count: Int) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(0.7.dp, colors.outlineVariant)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(colors.secondaryContainer, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Storefront,
                    null,
                    tint = colors.onSecondaryContainer,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    item.title ?: "-",
                    style = typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = appColors.textPrimary
                )
                Text(StandType.fromId(item.standType)?.let { stringResource(it.titleRes) }
                    ?: item.code.orEmpty(),
                    style = typography.labelMedium,
                    color = appColors.textSecondary)
            }
            Surface(shape = RoundedCornerShape(8.dp), color = colors.primaryContainer) {
                Text(
                    count.toString(),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colors.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun AssignmentDescriptionCard(desc: String) {
    val appColors = LocalPartnerManagementColors.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(0.7.dp, appColors.border)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Description,
                    null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    stringResource(R.string.label_description),
                    style = typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = appColors.textPrimary
                )
            }
            Text(desc, style = typography.bodyMedium, color = appColors.textSecondary)
        }
    }
}

@Composable
private fun EmptyAllocatedItemsCard() {
    val appColors = LocalPartnerManagementColors.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, appColors.border),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackgroundAlt)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Inventory2,
                null,
                tint = appColors.textTertiary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                stringResource(R.string.msg_no_allocated_stand_found),
                style = typography.bodyMedium,
                color = appColors.textSecondary
            )
        }
    }
}

@Composable
private fun ChangeAssignedStandStatusDialog(
    initialStatus: StandAssignmentStatus,
    isSubmitting: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (StandAssignmentStatus) -> Unit
) {
    var selectedStatus by rememberSaveable { mutableStateOf(initialStatus) }
    val statusEntries = remember { StandAssignmentStatus.entries }
    val statusTitles = statusEntries.map { stringResource(it.titleRes) }

    Dialog(onDismissRequest = onDismiss) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "تعیین وضعیت استند",
                        style = typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    DropdownSelectorField(
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

                    if (isSubmitting) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CustomButton(
                            text = stringResource(R.string.label_registration),
                            enabled = !isSubmitting,
                            onClick = { onConfirm(selectedStatus) },
                            modifier = Modifier.weight(1f)
                        )
                        CustomOutlinedButton(
                            text = stringResource(R.string.label_cancel),
                            enabled = !isSubmitting,
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
