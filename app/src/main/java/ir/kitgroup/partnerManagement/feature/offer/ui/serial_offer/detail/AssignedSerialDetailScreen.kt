package ir.kitgroup.partnerManagement.feature.offer.ui.serial_offer.detail

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
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.BouncingDotsIndicator
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomOutlinedButton
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField
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
                BouncingDotsIndicator()
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

    val statusTitles = statuses.map {
        stringResource(it.titleRes)
    }

    var selectedStatus by rememberSaveable(currentStatusId) {
        mutableIntStateOf(
            statuses.find { it.id == currentStatusId }?.id
                ?: ContractOfferStatus.ACTIVE.id
        )
    }

    val selectedStatusTitle = statuses
        .find { it.id == selectedStatus }
        ?.let { stringResource(it.titleRes) }
        .orEmpty()

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.label_change_status),
                    style = typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                DropdownSelectorField(
                    modifier = Modifier.fillMaxWidth(),
                    value = selectedStatusTitle,
                    label = stringResource(R.string.label_status),
                    placeholder = "",
                    items = statusTitles,
                    isRequired = true,
                    onItemSelected = { selectedTitle ->
                        val index = statusTitles.indexOf(selectedTitle)

                        if (index != -1) {
                            selectedStatus = statuses[index].id
                        }
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CustomButton(
                        text = stringResource(R.string.label_confirm),
                        onClick = {
                            onConfirm(selectedStatus)
                        },
                        modifier = Modifier.weight(1f)
                    )

                    CustomOutlinedButton(
                        text = stringResource(R.string.label_cancel),
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
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
