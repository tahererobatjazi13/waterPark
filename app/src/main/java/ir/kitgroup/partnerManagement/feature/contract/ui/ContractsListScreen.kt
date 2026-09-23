package ir.kitgroup.partnerManagement.feature.contract.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.ContractStatus
import ir.kitgroup.partnerManagement.core.ui.util.CooperationModel
import ir.kitgroup.partnerManagement.core.ui.util.SettlementPeriodType
import ir.kitgroup.partnerManagement.core.ui.util.demoContracts

@Composable
fun ContractsListScreen(
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
    onContractClick: (ContractEntity) -> Unit,
    contracts: List<ContractEntity> = demoContracts()
) {
    val appColors = LocalPartnerManagementColors.current

    Scaffold(
        topBar = {
            CustomHeader(
                title = R.string.label_contract_organization,
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                ContractsHeader(onAddClick = onAddClick)

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = contracts,
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

@Composable
fun ContractListItem(
    contract: ContractEntity,
    onClick: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current

    val cooperationModelText = contract.cooperationModel?.let { id ->
        CooperationModel.fromId(id)?.let { stringResource(it.titleRes) }
    } ?: "-"

    val settlementPeriodTypeText = contract.settlementPeriodType?.let { id ->
        SettlementPeriodType.fromId(id)?.let { stringResource(it.titleRes) }
    } ?: "-"

    val status = ContractStatus.fromId(contract.contractStatus)

    Card(
        modifier = Modifier
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = contract.name ?: "-",
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
                Text(
                    text = number,
                    style = typography.labelMedium,
                    color = appColors.textSecondary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(
                thickness = 0.6.dp,
                color = appColors.border
            )
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                ContractInfoItem(
                    label = stringResource(R.string.label_cooperation_model),
                    value = cooperationModelText,
                    modifier = Modifier.weight(1f)
                )

                ContractInfoItem(
                    label = stringResource(R.string.label_settlement_period_type),
                    value = settlementPeriodTypeText,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ContractInfoItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Column(
        modifier = modifier
    ) {
        Text(
            text = label,
            style = typography.labelSmall,
            color = appColors.textSecondary
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = value,
            style = typography.bodyMedium,
            color = appColors.textPrimary
        )
    }
}

@Composable
private fun ContractsHeader(
    onAddClick: () -> Unit,
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
            onClick = onAddClick,
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
