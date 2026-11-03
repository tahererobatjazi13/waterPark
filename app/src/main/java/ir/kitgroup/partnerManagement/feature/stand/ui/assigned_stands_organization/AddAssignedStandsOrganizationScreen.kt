package ir.kitgroup.partnerManagement.feature.stand.ui.assigned_stands_organization

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
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
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import ir.kitgroup.partnerManagement.core.ui.components.*
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.AssignmentMode
import ir.kitgroup.partnerManagement.core.ui.util.StandAssignmentStatus
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.feature.organization.ui.list.OrganizationListBottomSheet
import java.util.Locale

@Composable
fun AddAssignedStandsOrganizationScreen(
    modifier: Modifier = Modifier,
    viewModel: AddAssignedStandsOrganizationViewModel = hiltViewModel(),
    onSuccessSave: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val isOrganizationLocked = viewModel.isOrganizationLocked

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

    AddAssignedStandsOrganizationContent(
        uiState = uiState,
        isOrganizationLocked = isOrganizationLocked,
        snackBarHostState = snackBarHostState,
        onOrganizationSelected = { viewModel.onOrganizationChanged(it.organization.organizationId) },
        onStandSelected = viewModel::onStandChanged,
        onCountChange = viewModel::onCountChanged,
        onAssignmentModeChange = { viewModel.onAssignmentModeChanged(it.id) },
        onAssignmentDateChange = viewModel::onAssignmentDateChanged,
        onPlannedReturnDateChange = viewModel::onPlannedReturnDateChanged,
        onStatusChange = { viewModel.onStatusChanged(it.id) },
        onDescriptionChange = viewModel::onDescriptionChanged,
        onSubmit = { viewModel.submitAssignStand { onSuccessSave?.invoke() } },
        modifier = modifier
    )
}

@Composable
fun AddAssignedStandsOrganizationContent(
    uiState: AddAssignedStandsOrganizationUiState,
    isOrganizationLocked: Boolean,
    snackBarHostState: SnackbarHostState,
    onOrganizationSelected: (OrganizationWithDetail) -> Unit,
    onStandSelected: (String) -> Unit,
    onCountChange: (String) -> Unit,
    onAssignmentModeChange: (AssignmentMode) -> Unit,
    onAssignmentDateChange: (String) -> Unit,
    onPlannedReturnDateChange: (String) -> Unit,
    onStatusChange: (StandAssignmentStatus) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val appColors = LocalPartnerManagementColors.current
    var showOrganizationSheet by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showPlannedReturnDatePicker by remember { mutableStateOf(false) }

    val assignmentModeMap = remember(context) {
        AssignmentMode.entries.associateBy { context.getString(it.titleRes) }
    }
    val currentAssignmentMode =
        AssignmentMode.fromId(uiState.assignmentMode) ?: AssignmentMode.TABLIGHATI
    val isTrustMode = currentAssignmentMode == AssignmentMode.AMANI

    // فقط گزینه‌های پیش‌نویس (DRAFT) و فعال (ACTIVE_DELIVERED) در لیست قرار می‌گیرند
    val statusMap = remember(context) {
        listOf(
            StandAssignmentStatus.DRAFT,
            StandAssignmentStatus.ACTIVE_DELIVERED
        ).associateBy { context.getString(it.titleRes) }
    }

    val currentStatus =
        StandAssignmentStatus.fromId(uiState.status)

    val selectedOrgName = uiState.organizations
        .firstOrNull { it.organization.organizationId == uiState.organizationId }
        ?.organization?.name.orEmpty()

    val currentCount = uiState.count.toIntOrNull() ?: 1

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = appColors.cardBackground,
                tonalElevation = 6.dp,
                shadowElevation = 8.dp,
                border = BorderStroke(width = 0.5.dp, color = appColors.border)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    CustomButton(
                        text = if (uiState.isEditMode) {
                            stringResource(R.string.label_allocation_edit)
                        } else {
                            stringResource(R.string.label_allocation_record)
                        },
                        isLoading = uiState.isLoading,
                        enabled = uiState.isFormValid,
                        onClick = onSubmit,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // کارت اطلاعات پایه و سازمان
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
                border = BorderStroke(0.7.dp, appColors.border)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    DropdownSelectorField(
                        value = stringResource(currentAssignmentMode.titleRes),
                        label = stringResource(R.string.label_assignment_mode),
                        placeholder = stringResource(R.string.hint_choose_assignment_mode),
                        items = assignmentModeMap.keys.toList(),
                        isRequired = true,
                        onItemSelected = { selectedTitle ->
                            assignmentModeMap[selectedTitle]?.let { onAssignmentModeChange(it) }
                        }
                    )

                    if (!isOrganizationLocked) {
                        CustomSelectorField(
                            value = selectedOrgName,
                            label = stringResource(R.string.label_organization_receiving_name),
                            placeholder = stringResource(R.string.hint_choose_organization_name),
                            isExpanded = showOrganizationSheet,
                            isRequired = true,
                            onClick = { showOrganizationSheet = true }
                        )
                    }
                }
            }

            // بخش انتخاب استند تبلیغاتی
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SectionTitle(
                    stringResource(R.string.label_advertising_stands),
                    Icons.Filled.Checklist
                )

                if (uiState.stands.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = appColors.cardBackgroundAlt),
                        border = BorderStroke(0.7.dp, appColors.border)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "هیچ استندی در دیتابیس ثبت نشده است.",
                                color = MaterialTheme.colorScheme.error,
                                style = typography.bodyMedium
                            )
                        }
                    }
                } else {
                    uiState.stands.forEach { stand ->
                        val isSelected = uiState.advertisingStandId == stand.standTypeId

                        SelectableStandItemCard(
                            title = stand.title ?: "-",
                            subTitle = stand.standTypeName.orEmpty(),
                            isSelected = isSelected,
                            count = currentCount,
                            onToggle = {
                                if (!isSelected) {
                                    onStandSelected(stand.standTypeId)
                                    onCountChange("1")
                                } else {
                                    onStandSelected("")
                                }
                            },
                            onIncrement = {
                                if (currentCount < 999) {
                                    onCountChange((currentCount + 1).toString())
                                }
                            },
                            onDecrement = {
                                if (currentCount > 1) {
                                    onCountChange((currentCount - 1).toString())
                                }
                            }
                        )
                    }
                }
            }

            // کارت تاریخ‌ها و وضعیت
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
                border = BorderStroke(0.7.dp, appColors.border)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    CustomDateTimeFields(
                        label = stringResource(R.string.label_delivery_organization_date),
                        date = uiState.assignmentDate,
                        onDateClick = { showDatePicker = true },
                        showTime = false,
                        isRequired = true
                    )

                    AnimatedVisibility(
                        visible = isTrustMode,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        CustomDateTimeFields(
                            label = stringResource(R.string.label_planned_return_date),
                            date = uiState.plannedReturnDate,
                            onDateClick = { showPlannedReturnDatePicker = true },
                            showTime = false,
                            isRequired = true
                        )
                    }

                    if (!uiState.isEditMode) {
                        DropdownSelectorField(
                            value = stringResource(currentStatus.titleRes),
                            label = stringResource(R.string.label_status),
                            placeholder = "",
                            items = statusMap.keys.toList(),
                            isRequired = false,
                            onItemSelected = { selectedTitle ->
                                statusMap[selectedTitle]?.let { onStatusChange(it) }
                            }
                        )
                    }
                }
            }

            // کارت توضیحات
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
                border = BorderStroke(0.7.dp, appColors.border)
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    CustomDescriptionField(
                        label = stringResource(R.string.label_description),
                        value = uiState.description,
                        onValueChange = onDescriptionChange,
                        placeholder = stringResource(R.string.hint_description)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showOrganizationSheet) {
        OrganizationListBottomSheet(
            list = uiState.organizations,
            onDismiss = { showOrganizationSheet = false },
            onItemSelected = { item ->
                onOrganizationSelected(item)
                showOrganizationSheet = false
            }
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismiss = { showDatePicker = false },
            onDateSelected = { date ->
                val formatted = "${date.year}/${String.format(Locale.US, "%02d", date.month)}/${
                    String.format(Locale.US, "%02d", date.day)
                }"
                onAssignmentDateChange(formatted)
                showDatePicker = false
            }
        )
    }

    if (showPlannedReturnDatePicker) {
        DatePickerDialog(
            onDismiss = { showPlannedReturnDatePicker = false },
            onDateSelected = { date ->
                val formatted = "${date.year}/${String.format(Locale.US, "%02d", date.month)}/${
                    String.format(Locale.US, "%02d", date.day)
                }"
                onPlannedReturnDateChange(formatted)
                showPlannedReturnDatePicker = false
            }
        )
    }
}

@Composable
private fun SelectableStandItemCard(
    title: String,
    subTitle: String,
    isSelected: Boolean,
    count: Int,
    onToggle: () -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme

    val containerColor = if (isSelected) {
        colors.primaryContainer.copy(alpha = 0.12f)
    } else {
        appColors.cardBackground
    }

    val borderColor = if (isSelected) {
        colors.primary
    } else {
        appColors.border
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onToggle),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(if (isSelected) 1.5.dp else 0.7.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = colors.primary,
                    uncheckedColor = appColors.textTertiary
                )
            )

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        color = if (isSelected) colors.primaryContainer else appColors.infoContainer,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Storefront,
                    contentDescription = null,
                    tint = if (isSelected) colors.onPrimaryContainer else appColors.onInfoContainer,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = appColors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (subTitle.isNotBlank()) {
                    Text(
                        text = subTitle,
                        style = typography.bodySmall,
                        color = appColors.textSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            AnimatedVisibility(
                visible = isSelected,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                StandQuantityStepper(
                    count = count,
                    onIncrement = onIncrement,
                    onDecrement = onDecrement
                )
            }
        }
    }
}

@Composable
private fun StandQuantityStepper(
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .background(appColors.cardBackground, RoundedCornerShape(12.dp))
            .border(1.dp, appColors.border, RoundedCornerShape(12.dp))
            .padding(4.dp)
    ) {
        FilledTonalIconButton(
            onClick = onIncrement,
            modifier = Modifier.size(32.dp),
            shape = RoundedCornerShape(8.dp),
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = colors.primaryContainer,
                contentColor = colors.onPrimaryContainer
            )
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "افزایش",
                modifier = Modifier.size(16.dp)
            )
        }

        Box(
            modifier = Modifier
                .widthIn(min = 32.dp)
                .padding(horizontal = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = count.toString(),
                style = typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = appColors.textPrimary
            )
        }

        FilledTonalIconButton(
            onClick = onDecrement,
            enabled = count > 1,
            modifier = Modifier.size(32.dp),
            shape = RoundedCornerShape(8.dp),
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = colors.surfaceVariant,
                contentColor = colors.onSurfaceVariant,
                disabledContainerColor = colors.surfaceVariant.copy(alpha = 0.4f),
                disabledContentColor = colors.onSurfaceVariant.copy(alpha = 0.4f)
            )
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "کاهش",
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
