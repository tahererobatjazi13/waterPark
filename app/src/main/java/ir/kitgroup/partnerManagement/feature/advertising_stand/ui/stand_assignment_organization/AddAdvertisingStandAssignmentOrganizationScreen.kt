package ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand_assignment_organization

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.AdvertisingStandEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationEntity
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomDateTimeFields
import ir.kitgroup.partnerManagement.core.ui.components.CustomDescriptionField
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.CustomSelectorField
import ir.kitgroup.partnerManagement.core.ui.components.DatePickerDialog
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.AssignmentMode
import ir.kitgroup.partnerManagement.core.ui.util.AssignmentType
import ir.kitgroup.partnerManagement.core.ui.util.StandAssignmentStatus
import ir.kitgroup.partnerManagement.core.ui.util.StandType
import ir.kitgroup.partnerManagement.core.ui.util.demoOrganizations
import ir.kitgroup.partnerManagement.feature.advertising_stand.model.StandSelectionUiModel
import ir.kitgroup.partnerManagement.feature.organization.ui.OrganizationBottomSheet
import saman.zamani.persiandate.PersianDate
import java.util.Locale

@Composable
fun AddAdvertisingStandAssignmentOrganizationScreen(
    preselectedOrganizationId: String? = null,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // نقشه‌های نگاشت عنوان به Enum
    val assignmentTypeMap = remember(context) {
        AssignmentType.entries.associateBy { context.getString(it.titleRes) }
    }
    var selectedAssignmentType by rememberSaveable { mutableStateOf(AssignmentType.ASSIGN_TO_ORGANIZATION) }

    val assignmentModeMap = remember(context) {
        AssignmentMode.entries.associateBy { context.getString(it.titleRes) }
    }
    var selectedAssignmentMode by rememberSaveable { mutableStateOf(AssignmentMode.TABLIGHATI) }

    val statusMap = remember(context) {
        StandAssignmentStatus.entries.associateBy { context.getString(it.titleRes) }
    }
    var selectedStatus by rememberSaveable { mutableStateOf(StandAssignmentStatus.ACTIVE_DELIVERED) }

    var description by rememberSaveable { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var showPlannedReturnDatePicker by remember { mutableStateOf(false) }
    var showActualReturnDatePicker by remember { mutableStateOf(false) }

    val today = remember { PersianDate() }
    val defaultDate = remember {
        "${today.shYear}/${String.format(Locale.US, "%02d", today.shMonth)}/${
            String.format(Locale.US, "%02d", today.shDay)
        }"
    }

    var deliveryOrganizationDate by rememberSaveable { mutableStateOf(defaultDate) }
    var plannedReturnDate by rememberSaveable { mutableStateOf(defaultDate) }
    var actualReturnDate by rememberSaveable { mutableStateOf(defaultDate) }

    val isTrustMode = selectedAssignmentMode == AssignmentMode.AMANI

    var selectedOrganization by remember { mutableStateOf<OrganizationEntity?>(null) }
    var showOrganizationSheet by remember { mutableStateOf(false) }

    LaunchedEffect(preselectedOrganizationId) {
        if (preselectedOrganizationId != null) {
            selectedOrganization = demoOrganizations.find {
                it.organizationId == preselectedOrganizationId
            }
        }
    }

    val standItems = remember {
        mutableStateListOf(
            StandSelectionUiModel(
                entity = AdvertisingStandEntity(
                    advertisingStandId = "1",
                    name = "استند رومیزی",
                    code = "STAND-01",
                    standType = StandType.DESKTOP_STAND.id
                ),
                count = 0,
                isSelected = false
            ),
            StandSelectionUiModel(
                entity = AdvertisingStandEntity(
                    advertisingStandId = "2",
                    name = "بنر پرتابل",
                    code = "STAND-02",
                    standType = StandType.BANNER.id
                ),
                count = 0,
                isSelected = false
            ),
            StandSelectionUiModel(
                entity = AdvertisingStandEntity(
                    advertisingStandId = "3",
                    name = "کیوسک لمسی",
                    code = "STAND-03",
                    standType = StandType.KIOSK.id
                ),
                count = 0,
                isSelected = false
            )
        )
    }

    val appColors = LocalPartnerManagementColors.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CustomHeader(
                title = R.string.label_allocation_record,
                showBackButton = true,
                onBackClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.primary
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
                        value = stringResource(selectedAssignmentType.titleRes),
                        label = stringResource(R.string.label_assignment_type),
                        placeholder = stringResource(R.string.hint_choose_assignment_type),
                        items = assignmentTypeMap.keys.toList(),
                        isRequired = true,
                        onItemSelected = { selectedTitle ->
                            assignmentTypeMap[selectedTitle]?.let { selectedAssignmentType = it }
                        }
                    )

                    DropdownSelectorField(
                        value = stringResource(selectedAssignmentMode.titleRes),
                        label = stringResource(R.string.label_assignment_mode),
                        placeholder = stringResource(R.string.hint_choose_assignment_mode),
                        items = assignmentModeMap.keys.toList(),
                        isRequired = true,
                        onItemSelected = { selectedTitle ->
                            assignmentModeMap[selectedTitle]?.let { selectedAssignmentMode = it }
                        }
                    )

                    if (preselectedOrganizationId == null) {
                        CustomSelectorField(
                            value = selectedOrganization?.name ?: "",
                            label = stringResource(R.string.label_organization_receiving_name),
                            placeholder = stringResource(R.string.hint_choose_organization_name),
                            isExpanded = showOrganizationSheet,
                            onClick = {
                                showOrganizationSheet = true
                            }
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SectionTitle(
                            stringResource(R.string.label_advertising_stands),
                            Icons.Filled.Checklist
                        )

                        standItems.forEachIndexed { index, item ->
                            AdvertisingItemCard(
                                item = item,
                                onToggleSelected = {
                                    val newSelected = !item.isSelected
                                    standItems[index] = item.copy(
                                        isSelected = newSelected,
                                        count = if (newSelected && item.count == 0) 1 else item.count
                                    )
                                },
                                onIncrease = {
                                    standItems[index] = item.copy(
                                        count = item.count + 1,
                                        isSelected = true
                                    )
                                },
                                onDecrease = {
                                    if (item.count > 0) {
                                        val newCount = item.count - 1
                                        standItems[index] = item.copy(
                                            count = newCount,
                                            isSelected = newCount > 0
                                        )
                                    }
                                }
                            )
                        }
                    }

                    CustomDateTimeFields(
                        label = stringResource(R.string.label_delivery_organization_date),
                        date = deliveryOrganizationDate,
                        onDateClick = { showDatePicker = true },
                        showTime = false
                    )

                    if (isTrustMode) {
                        CustomDateTimeFields(
                            label = stringResource(R.string.label_planned_return_date),
                            date = plannedReturnDate,
                            onDateClick = { showPlannedReturnDatePicker = true },
                            showTime = false
                        )

                        CustomDateTimeFields(
                            label = stringResource(R.string.label_actual_return_date),
                            date = actualReturnDate,
                            onDateClick = { showActualReturnDatePicker = true },
                            showTime = false
                        )
                    }

                    DropdownSelectorField(
                        value = stringResource(selectedStatus.titleRes),
                        label = stringResource(R.string.label_status),
                        placeholder = "",
                        items = statusMap.keys.toList(),
                        isRequired = false,
                        onItemSelected = { selectedTitle ->
                            statusMap[selectedTitle]?.let { selectedStatus = it }
                        }
                    )

                    CustomDescriptionField(
                        label = stringResource(R.string.label_description),
                        value = description,
                        onValueChange = { newValue ->
                            description = newValue
                        },
                        placeholder = stringResource(R.string.hint_description)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    CustomButton(
                        text = stringResource(R.string.label_allocation_record),
                        onClick = onSaveClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismiss = { showDatePicker = false },
            onDateSelected = { date ->
                deliveryOrganizationDate =
                    "${date.year}/${String.format(Locale.US, "%02d", date.month)}/${
                        String.format(Locale.US, "%02d", date.day)
                    }"
                showDatePicker = false
            }
        )
    }

    if (showOrganizationSheet && preselectedOrganizationId == null) {
        OrganizationBottomSheet(
            list = demoOrganizations,
            onDismiss = { showOrganizationSheet = false },
            onItemSelected = { item ->
                selectedOrganization = item
                showOrganizationSheet = false
            }
        )
    }

    if (showPlannedReturnDatePicker) {
        DatePickerDialog(
            onDismiss = { showPlannedReturnDatePicker = false },
            onDateSelected = { date ->
                plannedReturnDate =
                    "${date.year}/${String.format(Locale.US, "%02d", date.month)}/${
                        String.format(Locale.US, "%02d", date.day)
                    }"
                showPlannedReturnDatePicker = false
            }
        )
    }

    if (showActualReturnDatePicker) {
        DatePickerDialog(
            onDismiss = { showActualReturnDatePicker = false },
            onDateSelected = { date ->
                actualReturnDate =
                    "${date.year}/${String.format(Locale.US, "%02d", date.month)}/${
                        String.format(Locale.US, "%02d", date.day)
                    }"
                showActualReturnDatePicker = false
            }
        )
    }
}

@Composable
private fun AdvertisingItemCard(
    item: StandSelectionUiModel,
    onToggleSelected: () -> Unit,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    val cardBackground =
        if (item.isSelected) LocalPartnerManagementColors.current.cardBackgroundAlt
        else LocalPartnerManagementColors.current.cardBackground

    val standTypeTitle = StandType.fromId(item.entity.standType)?.let {
        stringResource(it.titleRes)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(cardBackground, RoundedCornerShape(16.dp))
            .border(1.dp, LocalPartnerManagementColors.current.border, RoundedCornerShape(16.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.isSelected,
            onCheckedChange = { onToggleSelected() },
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = LocalPartnerManagementColors.current.textSecondary
            )
        )

        Spacer(modifier = Modifier.width(6.dp))

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = item.entity.name ?: "-",
                color = MaterialTheme.colorScheme.primary,
                style = typography.titleMedium,
                textAlign = TextAlign.Start,
            )

            if (!standTypeTitle.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = standTypeTitle,
                    color = LocalPartnerManagementColors.current.textSecondary,
                    style = typography.bodySmall,
                    textAlign = TextAlign.Start,
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.label_delivery_quantity),
                color = LocalPartnerManagementColors.current.textPrimary,
                style = typography.labelSmall,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                CounterButton(
                    text = "+",
                    onClick = onIncrease
                )

                CounterValue(
                    value = item.count
                )

                CounterButton(
                    text = "−",
                    onClick = onDecrease
                )
            }
        }
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
            style = typography.titleMedium,
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
            style = typography.titleMedium,
            color = LocalPartnerManagementColors.current.textPrimary,
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun AddAdvertisingStandAssignmentOrganizationScreenPreview() {
    AppScreenPreview {
        AddAdvertisingStandAssignmentOrganizationScreen(
            onBackClick = {},
            onSaveClick = {}
        )
    }
}
