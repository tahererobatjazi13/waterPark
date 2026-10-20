package ir.kitgroup.partnerManagement.feature.offer.ui.serial_offer.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import ir.kitgroup.partnerManagement.core.ui.components.DualActionButtonBottomBar
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.ContractOfferStatus
import ir.kitgroup.partnerManagement.core.ui.util.SecondaryButtonVariant
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent

@Composable
fun AssignedSerialDetailScreen(
    onNavigateBack: () -> Unit,
    onEditClick: (serialId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AssignedSerialDetailViewModel = hiltViewModel()
) {
    val serialDetail by viewModel.serialDetail.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val appColors = LocalPartnerManagementColors.current
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    var showChangeStatusDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
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
        snackbarHost = { SnackbarHost(snackBarHostState) },
        bottomBar = {
            if (serialDetail != null) {
                DualActionButtonBottomBar(
                    primaryText = stringResource(R.string.label_edit),
                    secondaryText = stringResource(R.string.label_change_status),
                    onPrimaryClick = { onEditClick(viewModel.serialId) },
                    onSecondaryClick = { showChangeStatusDialog = true },
                    isSecondaryLoading = uiState.isLoading,
                    secondaryVariant = SecondaryButtonVariant.FILLED
                )
            }
        }
    ) { innerPadding ->
        val currentItem = serialDetail

        if (currentItem == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val serial = currentItem.assignedSerial

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // هدر بالای صفحه با نام و تگ وضعیت
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = appColors.cardBackground)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = serial.name.orEmpty(),
                                style = typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = currentItem.organizationName,
                                style = typography.bodyMedium,
                                color = appColors.textSecondary
                            )
                        }
                        StatusBadge(status = ContractOfferStatus.fromId(serial.contractOfferStatus))
                    }
                }

                // اطلاعات طرح و قرارداد
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = appColors.cardBackground)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DetailRowItem(
                            label = stringResource(R.string.label_offer_plan),
                            value = currentItem.offerName
                        )
                        HorizontalDivider(
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                        )
                        DetailRowItem(
                            label = stringResource(R.string.label_contract),
                            value = currentItem.contractName
                        )
                    }
                }

                // مشخصات سریال‌ها
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = appColors.cardBackground)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DetailRowItem(
                            label = stringResource(R.string.label_serial_prefix),
                            value = serial.serialPrefix.orEmpty().ifBlank { "---" }
                        )
                        DetailRowItem(
                            label = stringResource(R.string.label_serial_count),
                            value = "${serial.count} عدد"
                        )
                        DetailRowItem(
                            label = stringResource(R.string.label_start_serial),
                            value = serial.fromSerial.toString()
                        )
                        DetailRowItem(
                            label = stringResource(R.string.label_end_serial),
                            value = serial.toSerial.toString()
                        )
                        DetailRowItem(
                            label = stringResource(R.string.label_last_used_serial),
                            value = serial.lastSerialUsed?.toString() ?: "---"
                        )
                        DetailRowItem(
                            label = stringResource(R.string.label_remaining_serial_count),
                            value = serial.remainingSerialCount?.toString() ?: "---"
                        )
                    }
                }
            }

            // دیالوگ تغییر وضعیت
            if (showChangeStatusDialog) {
                ChangeStatusDialog(
                    currentStatusId = serial.contractOfferStatus,
                    onDismiss = { showChangeStatusDialog = false },
                    onConfirm = { selectedStatusId ->
                        showChangeStatusDialog = false
                        viewModel.changeStatusSerialOffer(
                            newStatusId = selectedStatusId.toString(),
                            onSuccessCallback = {
                                onNavigateBack
                            }
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun ChangeStatusDialog(
    currentStatusId: Int?,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    val statuses = remember {
        listOf(
            ContractOfferStatus.DRAFT,
            ContractOfferStatus.ACTIVE,
            ContractOfferStatus.SUSPENDED,
            ContractOfferStatus.EXHAUSTED
        )
    }

    var selectedStatus by remember {
        mutableStateOf(statuses.find { it.id == currentStatusId } ?: ContractOfferStatus.ACTIVE)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.label_change_status),
                style = typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                statuses.forEach { status ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedStatus = status }
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (selectedStatus == status),
                            onClick = { selectedStatus = status }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(status.titleRes),
                            style = typography.bodyLarge
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(selectedStatus.id)
                }
            ) {
                Text(text = stringResource(R.string.label_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.label_cancel))
            }
        }
    )
}


@Composable
private fun DetailRowItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = typography.bodyMedium,
            color = LocalPartnerManagementColors.current.textSecondary
        )
        Text(
            text = value,
            style = typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
