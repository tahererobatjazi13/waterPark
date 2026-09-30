package ir.kitgroup.partnerManagement.feature.contract.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.ContractStatus
import ir.kitgroup.partnerManagement.core.ui.util.CooperationModel
import ir.kitgroup.partnerManagement.core.ui.util.SettlementPeriodType
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate

@Composable
fun ContractDetailScreen(
    onBackClick: () -> Unit,
    viewModel: ContractDetailViewModel = hiltViewModel()
) {
    val contract by viewModel.contract.collectAsStateWithLifecycle()
    val appColors = LocalPartnerManagementColors.current

    Scaffold(
        topBar = {
            CustomHeader(
                title = R.string.label_details,
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
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = appColors.screenBackground
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
                    label = stringResource(R.string.label_default_discount_percent),
                    value = formatPercent(contract.defaultDiscountPercent)
                )
                HorizontalDivider(color = appColors.border.copy(alpha = 0.4f))

                DetailRow(
                    label = stringResource(R.string.label_default_commission_percent),
                    value = formatPercent(contract.defaultCommissionPercent)
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
