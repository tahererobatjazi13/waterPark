package ir.kitgroup.partnerManagement.feature.contract.ui.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.ui.components.BouncingDotsIndicator
import ir.kitgroup.partnerManagement.core.ui.components.CustomDialog
import ir.kitgroup.partnerManagement.core.ui.components.DatePickerDialog
import ir.kitgroup.partnerManagement.core.ui.components.DualActionButtonBottomBar
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.ContractStatus
import ir.kitgroup.partnerManagement.core.ui.util.CooperationModel
import ir.kitgroup.partnerManagement.core.ui.util.SecondaryButtonVariant
import ir.kitgroup.partnerManagement.core.ui.util.SettlementPeriodType
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate
import java.util.Locale

@Composable
fun ContractDetailScreen(
    onEditClick: (contractId: String) -> Unit,
    onTerminateActiveContract: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ContractDetailViewModel = hiltViewModel()
) {
    val contract by viewModel.contract.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    // کنترل وضعیت باز بودن دیالوگ‌ها
    var showTerminationDatePicker by rememberSaveable { mutableStateOf(false) }
    var showConfirmationDialog by rememberSaveable { mutableStateOf(false) }
    var selectedTerminationDate by rememberSaveable { mutableStateOf("") }

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

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        bottomBar = {
            if (contract != null && ContractStatus.fromId(contract!!.status) != ContractStatus.INACTIVE) {
                DualActionButtonBottomBar(
                    primaryText = stringResource(R.string.label_edit_contract),
                    secondaryText = stringResource(R.string.label_termination_contract),
                    primaryIcon = Icons.Outlined.Edit,
                    secondaryIcon = Icons.Outlined.Cancel,
                    secondaryVariant = SecondaryButtonVariant.FILLED,
                    isSecondaryLoading = uiState.isTerminating,
                    buttonHeight = 44.dp,
                    onPrimaryClick = { onEditClick(viewModel.contractId) },
                    onSecondaryClick = {
                        if (!uiState.isTerminating) {
                            showTerminationDatePicker = true
                        }
                    }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            val currentItem = contract
            if (currentItem == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    BouncingDotsIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                ContractDetailContent(contract = currentItem)
            }
        }
    }
    // ۱. نمایش دیت‌پیکر برای انتخاب تاریخ خاتمه
    if (showTerminationDatePicker && contract != null) {
        DatePickerDialog(
            onDismiss = {
                if (!uiState.isTerminating) {
                    showTerminationDatePicker = false
                }
            },
            onDateSelected = { date ->
                selectedTerminationDate =
                    "${date.year}/${String.format(Locale.US, "%02d", date.month)}/${
                        String.format(
                            Locale.US,
                            "%02d",
                            date.day
                        )
                    }"
                showTerminationDatePicker = false
                showConfirmationDialog = true
            }
        )
    }

    // ۲. نمایش دیالوگ تأییدیه خاتمه همراه با تاریخ انتخاب‌شده (باگ isVisible اصلاح شد)
    if (showConfirmationDialog && contract != null) {
        CustomDialog(
            title = stringResource(R.string.label_termination_contract),
            message = "آیا از خاتمه دادن به این قرارداد در تاریخ $selectedTerminationDate اطمینان دارید؟",
            confirmText = stringResource(R.string.label_confirm_termination),
            dismissText = stringResource(R.string.label_cancel),
            isVisible = showConfirmationDialog,
            onDismiss = {
                if (!uiState.isTerminating) {
                    showConfirmationDialog = false
                }
            },
            onConfirm = {
                showConfirmationDialog = false
                viewModel.terminateActiveContract(
                    contractId = contract!!.contractId,
                    organizationId = contract!!.organizationId.orEmpty(),
                    terminationJalaliDate = selectedTerminationDate
                ) {
                    onTerminateActiveContract()
                }
            }
        )
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
        // عنوان + وضعیت + شماره قرارداد
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
            border = BorderStroke(width = 0.7.dp, color = appColors.border)
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

                contract.contractNumber?.takeIf { it.isNotBlank() }?.let { number ->
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

        // کارت مشخصات و جزئیات قرارداد
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
            border = BorderStroke(width = 0.7.dp, color = appColors.border)
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
                /*  HorizontalDivider(color = appColors.border.copy(alpha = 0.4f))

                         DetailRow(
                             label = stringResource(R.string.label_default_commission_percent),
                             value = formatPercentForInput(contract.defaultCommissionPercent)
                         )
                         HorizontalDivider(color = appColors.border.copy(alpha = 0.4f))

                         DetailRow(
                             label = stringResource(R.string.label_default_discount_percent),
                             value = formatPercentForInput(contract.defaultDiscountPercent)
                         ) */

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

