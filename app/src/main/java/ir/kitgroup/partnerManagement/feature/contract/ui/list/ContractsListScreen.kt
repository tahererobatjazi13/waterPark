package ir.kitgroup.partnerManagement.feature.contract.ui.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.EmptyState
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.ContractStatus
import ir.kitgroup.partnerManagement.core.ui.util.CooperationModel
import ir.kitgroup.partnerManagement.core.ui.util.SettlementPeriodType
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate

@Composable
fun ContractsListScreen(
    onAddContractClick: () -> Unit,
    onContractClick: (ContractEntity) -> Unit,
    modifier: Modifier = Modifier,
    contractsListViewModel: ContractsListViewModel = hiltViewModel()
) {
    val contracts by contractsListViewModel.contracts.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(contractsListViewModel.uiEvent) {
        contractsListViewModel.uiEvent.collect { event ->
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
        containerColor = MaterialTheme.colorScheme.background
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
                    .padding(16.dp)
            ) {
                ContractsHeader(
                    onAddContractClick = {
                        contractsListViewModel.checkCanAddContractUseCase(onAddContractClick)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                when {
                    contracts == null -> {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(36.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    contracts?.isEmpty() == true -> {
                        EmptyState(
                            textRes = R.string.msg_no_item_found,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        )
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(
                                items = contracts.orEmpty(),
                                key = { contract -> contract.contractId }
                            ) { contract ->
                                ContractListItem(
                                    contract = contract,
                                    onClick = { onContractClick(contract) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContractListItem(
    contract: ContractEntity,
    onClick: () -> Unit,
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

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColors.cardBackground
        ),
        border = BorderStroke(
            width = 0.7.dp,
            color = appColors.border
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            // عنوان قرارداد و وضعیت
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

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = appColors.textSecondary
                )
            }

            contract.contractNumber?.let { number ->
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.label_contract_number),
                        style = typography.labelMedium,
                        color = appColors.textTertiary
                    )
                    Text(
                        text = number,
                        style = typography.titleMedium,
                        color = appColors.textPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(
                thickness = 0.6.dp,
                color = appColors.border
            )
            Spacer(modifier = Modifier.height(8.dp))

            // مدل همکاری
            ContractInfoRow(
                label = stringResource(R.string.label_cooperation_model),
                value = cooperationModelText
            )

            Spacer(modifier = Modifier.height(4.dp))

            // نوع دوره تسویه
            ContractInfoRow(
                label = stringResource(R.string.label_settlement_period_type),
                value = settlementPeriodTypeText
            )

            Spacer(modifier = Modifier.height(8.dp))

            // دوره اعتبار و تاریخ‌ها
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.label_validity_period),
                    style = typography.labelMedium,
                    color = appColors.textSecondary
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = formatJalaliDate(contract.startDate),
                        style = typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = stringResource(R.string.label_to),
                        style = typography.labelLarge,
                        color = appColors.textTertiary
                    )
                    Text(
                        text = formatJalaliDate(contract.endDate),
                        style = typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun ContractInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = typography.labelMedium,
            color = appColors.textSecondary
        )
        Text(
            text = value,
            style = typography.bodyMedium,
            color = appColors.textPrimary
        )
    }
}

@Composable
fun ContractsHeader(
    onAddContractClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.label_contract_organization),
            style = typography.titleMedium,
            color = appColors.textPrimary
        )

        CustomButton(
            text = stringResource(R.string.label_new_contract),
            onClick = onAddContractClick,
            fillMaxWidth = false,
            height = 38.dp,
            textStyle = typography.titleLarge,
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            icon = Icons.Default.Add,
            colors = ButtonDefaults.buttonColors(
                containerColor = appColors.success,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}
