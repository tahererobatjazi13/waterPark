package ir.kitgroup.partnerManagement.feature.stand.ui.assigned_stands_organization

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomDateTimeFields
import ir.kitgroup.partnerManagement.core.ui.components.CustomDescriptionField
import ir.kitgroup.partnerManagement.core.ui.components.CustomSelectorField
import ir.kitgroup.partnerManagement.core.ui.components.DatePickerDialog
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.AssignmentMode
import ir.kitgroup.partnerManagement.core.ui.util.AssignmentType
import ir.kitgroup.partnerManagement.core.ui.util.StandAssignmentStatus
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate
import ir.kitgroup.partnerManagement.feature.organization.ui.list.OrganizationListBottomSheet
import saman.zamani.persiandate.PersianDate
import java.util.Locale

@Composable
fun AddAssignedStandsOrganizationScreen(
    modifier: Modifier = Modifier,
    assignedStandId: String? = null,
    organizationId: String? = null,
    viewModel: AddAssignedStandsOrganizationViewModel = hiltViewModel(),
    onSuccessSave: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()
    val targetOrganizationId = organizationId ?: viewModel.preselectedOrganizationId

    // شنیدن پیام‌های نتیجه عملیات ثبت
    LaunchedEffect(Unit) {
        viewModel.saveMessage.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            if (message.contains("موفقیت")) {
                onSuccessSave?.invoke()
            }
        }
    }

    var selectedStandId by rememberSaveable {
        mutableStateOf<String?>(null)
    }
    val isEditMode = !assignedStandId.isNullOrBlank()

    var standCount by rememberSaveable {
        mutableIntStateOf(1)
    }

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

    var selectedOrganization by remember { mutableStateOf<OrganizationWithDetail?>(null) }
    var showOrganizationSheet by remember { mutableStateOf(false) }
    val editDetail by viewModel.editStandDetailFlow.collectAsStateWithLifecycle()

    // ۱. پُر کردن خودکار فیلدها هنگام دریافت اطلاعات ویرایش
    LaunchedEffect(uiState.editStandDetail) {
        uiState.editStandDetail?.let { detail ->
            val stand = detail.assignedStand
            selectedStandId = stand.standTypeId
            standCount = stand.count ?: 1

            AssignmentMode.fromId(stand.assignmentMode)?.let {
                selectedAssignmentMode = it
            }
            stand.statusAssign?.let { statusId ->
                StandAssignmentStatus.fromId(statusId)?.let {
                    selectedStatus = it
                }
            }
            if (!stand.deliveryToOrgDate.isNullOrBlank()) {
                deliveryOrganizationDate = formatJalaliDate(stand.deliveryToOrgDate)
            }
            if (!stand.plannedReturnDate.isNullOrBlank()) {
                plannedReturnDate = formatJalaliDate(stand.plannedReturnDate)
            }
            if (!stand.actualReturnDate.isNullOrBlank()) {
                actualReturnDate = formatJalaliDate(stand.actualReturnDate)
            }
            description = stand.description ?: ""
        }
    }
    // ست کردن سازمان پیش‌فرض انتخاب شده
    LaunchedEffect(uiState.preselectedOrganization) {
        if (uiState.preselectedOrganization != null && selectedOrganization == null) {
            selectedOrganization = uiState.preselectedOrganization
        }
    }

    val finalOrganizationId = viewModel.preselectedOrganizationId
        ?: selectedOrganization?.organization?.organizationId

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
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
                    /*           DropdownSelectorField(
                                   value = stringResource(selectedAssignmentType.titleRes),
                                   label = stringResource(R.string.label_assignment_type),
                                   placeholder = stringResource(R.string.hint_choose_assignment_type),
                                   items = assignmentTypeMap.keys.toList(),
                                   isRequired = true,
                                   onItemSelected = { selectedTitle ->
                                       assignmentTypeMap[selectedTitle]?.let { selectedAssignmentType = it }
                                   }
                               )*/

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

                    // نمایش انتخاب سازمان در صورتی که از قبل مشخص نشده باشد
                    if (viewModel.preselectedOrganizationId == null) {
                        CustomSelectorField(
                            value = selectedOrganization?.organization?.name ?: "",
                            label = stringResource(R.string.label_organization_receiving_name),
                            placeholder = stringResource(R.string.hint_choose_organization_name),
                            isExpanded = showOrganizationSheet,
                            onClick = {
                                showOrganizationSheet = true
                            }
                        )
                    }

                    // لیست استندها مستقیماً از دیتابیس Room
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
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
                                val isSelected = selectedStandId == stand.standTypeId

                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (!isSelected) {
                                                selectedStandId = stand.standTypeId
                                                standCount = 1
                                            } else {
                                                selectedStandId = null
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
                                                    selectedStandId = stand.standTypeId
                                                    standCount = 1
                                                } else {
                                                    selectedStandId = null
                                                    standCount = 1
                                                }
                                            }
                                        )

                                        Column(
                                            modifier = Modifier.weight(1f)
                                        ) {
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
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                CounterButton(
                                                    text = "+",
                                                    onClick = {
                                                        if (standCount < 999) standCount++
                                                    }
                                                )

                                                CounterValue(value = standCount)

                                                CounterButton(
                                                    text = "−",
                                                    onClick = {
                                                        if (standCount > 1) standCount--
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
                    if (!isEditMode) {

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
                    }

                    CustomDescriptionField(
                        label = stringResource(R.string.label_description),
                        value = description,
                        onValueChange = { description = it },
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
                        onClick = {
                            val standId = selectedStandId ?: return@CustomButton
                            val orgId = finalOrganizationId ?: return@CustomButton

                            viewModel.saveAssignStand(
                                standAssignmentId = null, // در حالت ایجاد مقدار null است
                                advertisingStandId = standId,
                                organizationId = orgId,
                                meetingId = null,
                                count = standCount,
                                assignmentType = selectedAssignmentType.id,
                                assignmentMode = selectedAssignmentMode.id,
                                assignmentDate = deliveryOrganizationDate,
                                plannedReturnDate = if (isTrustMode) plannedReturnDate else null,
                                deliveryToVisitorDate = null,
                                deliveryToOrgDate = deliveryOrganizationDate,
                                description = description,
                                status = selectedStatus.id,
                                isEdit = false
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        isLoading = isSaving,
                        enabled = !isSaving &&
                                selectedStandId != null &&
                                standCount > 0 &&
                                finalOrganizationId != null
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

    if (showOrganizationSheet && viewModel.preselectedOrganizationId == null) {
        OrganizationListBottomSheet(
            list = uiState.organizations,
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
