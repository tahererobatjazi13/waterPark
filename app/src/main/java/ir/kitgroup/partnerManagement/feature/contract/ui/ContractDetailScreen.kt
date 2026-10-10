package ir.kitgroup.partnerManagement.feature.contract.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.CustomOutlinedButton
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.ContractStatus
import ir.kitgroup.partnerManagement.core.ui.util.CooperationModel
import ir.kitgroup.partnerManagement.core.ui.util.SettlementPeriodType
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog
import ir.kitgroup.partnerManagement.feature.organization.ui.detail.jalaliToMillis
import java.util.Locale

@Composable
fun ContractDetailScreen(
    contractId: String?,
    onEditClick: () -> Unit,
    onStatusChanged: () -> Unit,
    viewModel: ContractDetailViewModel = hiltViewModel()
) {
    val contract by viewModel.contract.collectAsStateWithLifecycle()
    val appColors = LocalPartnerManagementColors.current

    var showTerminationDatePicker by rememberSaveable {
        mutableStateOf(false)
    }

    val isUpdatingStatus by viewModel.isUpdatingStatus
        .collectAsStateWithLifecycle()

    LaunchedEffect(contractId) {
        if (!contractId.isNullOrBlank()) {
            viewModel.loadContract(contractId)
        }
    }

    Scaffold(

        bottomBar = {
            if (contract != null) {
                Surface(
                    color = appColors.screenBackground,
                    tonalElevation = 4.dp,
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp,
                                vertical = 10.dp
                            ),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (ContractStatus.fromId(contract!!.status) != ContractStatus.INACTIVE) {
                            CustomButton(
                                text = stringResource(
                                    R.string.label_edit_contract
                                ),
                                onClick = onEditClick,
                                fillMaxWidth = false,
                                modifier = Modifier.weight(1f),
                                height = 42.dp,
                                cornerRadius = 12.dp,
                                textStyle = typography.titleLarge,
                                contentPadding = PaddingValues(
                                    horizontal = 14.dp,
                                    vertical = 6.dp
                                )
                            )

                            CustomButton(
                                text = stringResource(
                                    R.string.label_termination_contract
                                ),
                                onClick = {
                                    if (!isUpdatingStatus) {
                                        showTerminationDatePicker = true
                                    }
                                },
                                fillMaxWidth = false,
                                modifier = Modifier.weight(1f),
                                height = 42.dp,
                                cornerRadius = 12.dp,
                                textStyle = typography.titleLarge,
                                contentPadding = PaddingValues(
                                    horizontal = 14.dp,
                                    vertical = 6.dp
                                ),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = appColors.error,
                                    contentColor = appColors.errorContainer
                                )
                            )
                        }
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding()
                ),
            shape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp
            ),
            color = MaterialTheme.colorScheme.background
        ) {

            val item = contract

            if (item == null) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }

            } else {

                ContractDetailContent(
                    contract = item
                )
            }
        }
    }

    /*
     * خاتمه قرارداد:
     * فقط تاریخ گرفته می‌شود.
     * هیچ Status از کاربر دریافت نمی‌شود.
     */
    if (showTerminationDatePicker && contract != null) {

        ir.kitgroup.partnerManagement.core.ui.components.DatePickerDialog(

            onDismiss = {
                if (!isUpdatingStatus) {
                    showTerminationDatePicker = false
                }
            },

            onDateSelected = { date ->

                val selectedJalaliDate =
                    "${date.year}/${
                        String.format(
                            Locale.US,
                            "%02d",
                            date.month
                        )
                    }/${
                        String.format(
                            Locale.US,
                            "%02d",
                            date.day
                        )
                    }"

                showTerminationDatePicker = false

                viewModel.terminateActiveContract(
                    contractId = contract!!.contractId,
                    organizationId = contract!!.organizationId.orEmpty(),
                    terminationDate = selectedJalaliDate
                ) {
                    onStatusChanged()
                }
            }
        )
    }
}

@Composable
fun ChangeContractStatusDialog(
    initialStatus: ContractStatus,
    isSaving: Boolean = false,
    onDismiss: () -> Unit,
    onConfirm: (ContractStatus) -> Unit
) {

    var selectedStatus by rememberSaveable {
        mutableStateOf(initialStatus)
    }

    val statusEntries = remember {
        ContractStatus.entries
    }

    val statusTitles = statusEntries.map {
        stringResource(it.titleRes)
    }

    Dialog(
        onDismissRequest = {
            if (!isSaving) {
                onDismiss()
            }
        }
    ) {

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
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
                    text = stringResource(
                        R.string.label_determination_organization_status
                    ),
                    style = typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                DropdownSelectorField(
                    modifier = Modifier.fillMaxWidth(),
                    value = stringResource(
                        selectedStatus.titleRes
                    ),
                    label = stringResource(
                        R.string.label_status
                    ),
                    placeholder = "",
                    items = statusTitles,
                    isRequired = true,
                    onItemSelected = { selectedTitle ->

                        val index =
                            statusTitles.indexOf(selectedTitle)

                        if (index >= 0) {
                            selectedStatus =
                                statusEntries[index]
                        }
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    CustomButton(
                        text = if (isSaving) {
                            stringResource(
                                R.string.label_save
                            )
                        } else {
                            stringResource(
                                R.string.label_registration
                            )
                        },
                        onClick = {
                            if (!isSaving) {
                                onConfirm(selectedStatus)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )

                    CustomOutlinedButton(
                        text = stringResource(
                            R.string.label_cancel
                        ),
                        onClick = {
                            if (!isSaving) {
                                onDismiss()
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun ContractDetailContent(
    contract: ContractEntity,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    val cooperationModelText = contract.cooperationModel?.let { id ->
        CooperationModel.fromId(id)?.let { stringResource(it.titleRes) }
    } ?: "-"

    val settlementPeriodTypeText = contract.settlementPeriodType?.let { id ->
        SettlementPeriodType.fromId(id)?.let { stringResource(it.titleRes) }
    } ?: "-"

    val status = ContractStatus.fromId(contract.status)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // ===== کارت سربرگ: عنوان قرارداد + شماره + وضعیت =====
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = appColors.cardBackground
            ),
            border = BorderStroke(
                width = 0.7.dp,
                color = appColors.border
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = contract.title ?: "-",
                        style = typography.titleLarge,
                        color = appColors.textPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    StatusBadge(status = status)
                }

                contract.contractNumber?.let { number ->
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "شماره قرارداد: $number",
                        style = typography.bodyMedium,
                        color = appColors.textSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ===== کارت جزئیات =====
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = appColors.cardBackground
            ),
            border = BorderStroke(
                width = 0.7.dp,
                color = appColors.border
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                DetailRow(
                    label = stringResource(R.string.label_contract_title),
                    value = contract.title ?: "-"
                )
                HorizontalDivider(color = appColors.border.copy(alpha = 0.4f))

                DetailRow(
                    label = stringResource(R.string.label_cooperation_model),
                    value = cooperationModelText
                )
                HorizontalDivider(color = appColors.border.copy(alpha = 0.4f))

                DetailRow(
                    label = stringResource(R.string.label_settlement_period_type),
                    value = settlementPeriodTypeText
                )
                HorizontalDivider(color = appColors.border.copy(alpha = 0.4f))

                DetailRow(
                    label = stringResource(R.string.label_start_date),
                    value = formatJalaliDate(contract.startDate)
                )
                HorizontalDivider(color = appColors.border.copy(alpha = 0.4f))

                DetailRow(
                    label = stringResource(R.string.label_end_date),
                    value = formatJalaliDate(contract.endDate)
                )
                HorizontalDivider(color = appColors.border.copy(alpha = 0.4f))
                DetailRow(
                    label = stringResource(R.string.label_default_commission_percent),
                    value = formatPercent(contract.defaultCommissionPercent)
                )

                HorizontalDivider(color = appColors.border.copy(alpha = 0.4f))

                DetailRow(
                    label = stringResource(R.string.label_default_discount_percent),
                    value = formatPercent(contract.defaultDiscountPercent)
                )

                contract.description?.takeIf { it.isNotBlank() }?.let { description ->
                    HorizontalDivider(color = appColors.border.copy(alpha = 0.4f))
                    DetailRow(
                        label = stringResource(R.string.label_description),
                        value = description
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = typography.bodyMedium,
            color = appColors.textSecondary,
            modifier = Modifier.weight(0.45f)
        )
        Text(
            text = value,
            style = typography.bodyMedium,
            color = appColors.textPrimary,
            modifier = Modifier.weight(0.55f)
        )
    }
}

private fun formatPercent(value: Double?): String {
    if (value == null) return "-"
    val trimmed = if (value % 1.0 == 0.0) value.toInt().toString() else value.toString()
    return "$trimmed٪"
}
