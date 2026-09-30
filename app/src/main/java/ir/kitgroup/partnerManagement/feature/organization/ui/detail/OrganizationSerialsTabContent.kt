package ir.kitgroup.partnerManagement.feature.organization.ui.detail

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import ir.kitgroup.partnerManagement.core.database.model.AssignedSerialWithDetail
import ir.kitgroup.partnerManagement.core.ui.components.EmptyState
import ir.kitgroup.partnerManagement.core.ui.util.ContractOfferStatus


@Composable
fun OrganizationSerialsTabContent(
    serials: List<AssignedSerialWithDetail>,
    onAddTicketClick: () -> Unit,
    onTicketClick: (AssignedSerialWithDetail) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTicketForDetail by remember { mutableStateOf<AssignedSerialWithDetail?>(null) }
    val appColors = LocalPartnerManagementColors.current

    Column(modifier = modifier.fillMaxSize()) {
        // هدر تب شامل عنوان و دکمه افزودن
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Box(modifier = Modifier.weight(1f)) {
                SectionTitle(title = stringResource(R.string.label_organization_serial_list))
            }

            Spacer(modifier = Modifier.width(8.dp))

            FilledTonalButton(
                onClick = onAddTicketClick,
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = appColors.success,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.label_contract_offer),
                    style = typography.titleLarge
                )
            }
        }

        if (serials.isEmpty()) {
            EmptyState(
                textRes = R.string.msg_no_tickets_offers_found,
                icon = Icons.Default.ConfirmationNumber,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(serials, key = { it.assignedSerial.serialAssignmentId }) { ticket ->
                    TicketOfferCard(
                        item = ticket,
                        onClick = {
                            selectedTicketForDetail = ticket
                            onTicketClick(ticket)
                        }
                    )
                }
            }
        }
    }

    // دیالوگ نمایش جزئیات کامل آفر با کلیک روی کارت
    selectedTicketForDetail?.let { ticket ->
        TicketOfferDetailDialog(
            item = ticket,
            onDismiss = { selectedTicketForDetail = null }
        )
    }
}

@Composable
fun TicketOfferCard(
    item: AssignedSerialWithDetail,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        onClick = onClick,

        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(0.5.dp, appColors.border)
    )
    {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.assignedSerial.name.orEmpty(),
                    style = typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                StatusBadge(status = ContractOfferStatus.fromId(item.assignedSerial.contractOfferStatus))

            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.offerName,
                style = typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                thickness = 0.6.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.label_serial_range),
                        style = typography.labelSmall,
                        color = appColors.textSecondary
                    )
                    Text(
                        text = "${item.assignedSerial.serialPrefix} : ${item.assignedSerial.fromSerial} - ${item.assignedSerial.toSerial}",
                        style = typography.titleMedium
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(R.string.label_serial_count),
                        style = typography.labelSmall,
                        color = appColors.textSecondary
                    )
                    Text(
                        text = "${item.assignedSerial.count} عدد",
                        style = typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
fun TicketOfferDetailDialog(
    item: AssignedSerialWithDetail,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.action_close))
            }
        },
        title = {
            Text(
                text = stringResource(R.string.label_ticket_offer_detail),
                style = typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DetailItemRow(
                    label = stringResource(R.string.label_offer_plan),
                    value = item.offerName
                )
                DetailItemRow(
                    label = stringResource(R.string.label_contract),
                    value = item.contractName
                )
                DetailItemRow(
                    label = stringResource(R.string.label_org_name),
                    value = item.organizationName
                )
                /*   DetailItemRow(
                       label = stringResource(R.string.label_cooperation_model),
                       value = ticket.cooperationModel
                   )*/
                DetailItemRow(
                    label = stringResource(R.string.label_serial_prefix),
                    value = item.assignedSerial.serialPrefix.orEmpty()
                )
                DetailItemRow(
                    label = stringResource(R.string.label_serial_count),
                    value = item.assignedSerial.count.toString()
                )
                DetailItemRow(
                    label = stringResource(R.string.label_start_serial),
                    value = item.assignedSerial.fromSerial.toString()
                )
                DetailItemRow(
                    label = stringResource(R.string.label_end_serial),
                    value = item.assignedSerial.toSerial.toString()
                )
                DetailItemRow(
                    label = stringResource(R.string.label_serial_status),
                    value = item.assignedSerial.contractOfferStatusName.orEmpty()
                )
                DetailItemRow(
                    label = stringResource(R.string.label_last_used_serial),
                    value = item.assignedSerial.lastSerialUsed?.toString() ?: "---"
                )
                DetailItemRow(
                    label = stringResource(R.string.label_remaining_serial_count),
                    value = item.assignedSerial.remainingSerialCount?.toString() ?: "---"
                )
            }
        }
    )
}


@Composable
private fun DetailItemRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "$label:",
            style = typography.bodySmall,
            color = LocalPartnerManagementColors.current.textSecondary,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            style = typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(0.6f)
        )
    }
}
