package ir.kitgroup.partnerManagement.feature.stand.ui.assigned_stands_organization

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomDateTimeFields
import ir.kitgroup.partnerManagement.core.ui.components.CustomDescriptionField
import ir.kitgroup.partnerManagement.core.ui.components.CustomSelectorField
import ir.kitgroup.partnerManagement.core.ui.components.DatePickerDialog
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
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
    onOrganizationSelected: (ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail) -> Unit,
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
    var showOrganizationSheet by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showPlannedReturnDatePicker by remember { mutableStateOf(false) }

    val assignmentModeMap = remember(context) {
        AssignmentMode.entries.associateBy { context.getString(it.titleRes) }
    }
    val currentAssignmentMode =
        AssignmentMode.fromId(uiState.assignmentMode) ?: AssignmentMode.TABLIGHATI
    val isTrustMode = currentAssignmentMode == AssignmentMode.AMANI

    val statusMap = remember(context) {
        StandAssignmentStatus.entries.associateBy { context.getString(it.titleRes) }
    }
    val currentStatus =
        StandAssignmentStatus.fromId(uiState.status) ?: StandAssignmentStatus.ACTIVE_DELIVERED

    val selectedOrgName = uiState.organizations
        .firstOrNull { it.organization.organizationId == uiState.organizationId }
        ?.organization?.name.orEmpty()

    val currentCount = uiState.count.toIntOrNull() ?: 1

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
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
                        // فیلد انتخاب سازمان در صورت خالی بودن اولیه
                        CustomSelectorField(
                            value = selectedOrgName,
                            label = stringResource(R.string.label_organization_receiving_name),
                            placeholder = stringResource(R.string.hint_choose_organization_name),
                            isExpanded = showOrganizationSheet,
                            isRequired = true,
                            onClick = { showOrganizationSheet = true }
                        )
                    }

                    // لیست استندها
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        SectionTitle(
                            stringResource(R.string.label_advertising_stands),
                            Icons.Filled.Checklist
                        )

                        if (uiState.stands.isEmpty()) {
                            Text(
                                text = "هیچ استندی در دیتابیس ثبت نشده است.",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        } else {
                            uiState.stands.forEach { stand ->
                                val isSelected = uiState.advertisingStandId == stand.standTypeId

                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (!isSelected) {
                                                onStandSelected(stand.standTypeId)
                                                onCountChange("1")
                                            } else {
                                                onStandSelected("")
                                            }
                                        },
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (isSelected) {
                                        LocalPartnerManagementColors.current.cardBackgroundAlt
                                    } else {
                                        LocalPartnerManagementColors.current.cardBackground
                                    },
                                    border = BorderStroke(
                                        1.dp,
                                        if (isSelected) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            LocalPartnerManagementColors.current.border
                                        }
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = isSelected,
                                            onCheckedChange = { checked ->
                                                if (checked) {
                                                    onStandSelected(stand.standTypeId)
                                                    onCountChange("1")
                                                } else {
                                                    onStandSelected("")
                                                }
                                            }
                                        )

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = stand.title ?: "-",
                                                style = MaterialTheme.typography.titleMedium
                                            )
                                            Text(
                                                text = stand.standTypeName.orEmpty(),
                                                style = MaterialTheme.typography.bodySmall,
                                                color = LocalPartnerManagementColors.current.textSecondary
                                            )
                                        }

                                        if (isSelected) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                CounterButton(
                                                    text = "+",
                                                    onClick = {
                                                        if (currentCount < 999) {
                                                            onCountChange((currentCount + 1).toString())
                                                        }
                                                    }
                                                )

                                                CounterValue(value = currentCount)

                                                CounterButton(
                                                    text = "−",
                                                    onClick = {
                                                        if (currentCount > 1) {
                                                            onCountChange((currentCount - 1).toString())
                                                        }
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    CustomDateTimeFields(
                        label = stringResource(R.string.label_delivery_organization_date),
                        date = uiState.assignmentDate,
                        onDateClick = { showDatePicker = true },
                        showTime = false,
                        isRequired = true
                    )

                    if (isTrustMode) {
                        CustomDateTimeFields(
                            label = stringResource(R.string.label_planned_return_date),
                            date = uiState.plannedReturnDate,
                            onDateClick = { showPlannedReturnDatePicker = true },
                            showTime = false
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

                    CustomDescriptionField(
                        label = stringResource(R.string.label_description),
                        value = uiState.description,
                        onValueChange = onDescriptionChange,
                        placeholder = stringResource(R.string.hint_description)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                CustomButton(
                    text = if (uiState.isEditMode) {
                        stringResource(R.string.label_allocation_edit)
                    } else {
                        stringResource(R.string.label_allocation_record)
                    },
                    isLoading = uiState.isLoading,
                    enabled = uiState.isFormValid,
                    onClick = onSubmit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )
            }

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
private fun CounterButton(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 36.dp, height = 36.dp)
            .border(1.dp, LocalPartnerManagementColors.current.border, RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun CounterValue(
    value: Int
) {
    Box(
        modifier = Modifier
            .width(48.dp)
            .height(36.dp)
            .border(1.dp, LocalPartnerManagementColors.current.border, RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = LocalPartnerManagementColors.current.textPrimary,
        )
    }
}
