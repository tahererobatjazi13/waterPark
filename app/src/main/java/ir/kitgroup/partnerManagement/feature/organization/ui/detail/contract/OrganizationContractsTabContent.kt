package ir.kitgroup.partnerManagement.feature.organization.ui.detail.contract

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.ui.components.EmptyState
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.feature.contract.ui.list.ContractListItem

@Composable
fun OrganizationContractsTabContent(
    organizationId: String,
    onAssignContractClick: () -> Unit = {},
    onContractClick: (ContractEntity) -> Unit,
    viewModel: OrganizationContractsViewModel = hiltViewModel()
) {
    val appColors = LocalPartnerManagementColors.current
    val contracts by viewModel.contracts.collectAsStateWithLifecycle()

    LaunchedEffect(organizationId) {
        viewModel.observeOrganizationContracts(organizationId)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            // هدر تب
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionTitle(title = stringResource(R.string.label_contract_organization_list))
            }

            if (contracts.isEmpty()) {
                EmptyState(
                    textRes = R.string.msg_no_contract_found,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 84.dp)
                ) {
                    itemsIndexed(
                        items = contracts,
                        key = { _, item -> item.contractId }
                    ) { _, contract ->
                        ContractListItem(
                            contract = contract,
                            onClick = { onContractClick(contract) }
                        )
                    }
                }
            }
        }

        // دکمه شناور ثبت قرارداد در پایین صفحه
        ExtendedFloatingActionButton(
            onClick = onAssignContractClick,
            containerColor = appColors.success,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = RoundedCornerShape(16.dp),
            icon = { Icon(Icons.Default.Add, contentDescription = null) },
            text = {
                Text(
                    text = stringResource(R.string.label_new_contract),
                    style = typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        )
    }
}
