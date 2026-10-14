package ir.kitgroup.partnerManagement.feature.offer.ui.serial_offer.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.PublishedWithChanges
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.ContractOfferStatus

@Composable
fun AssignedSerialDetailScreen(
    onEditClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AssignedSerialDetailViewModel = hiltViewModel()
) {
    val serialDetail by viewModel.serialDetail.collectAsStateWithLifecycle()
    val appColors = LocalPartnerManagementColors.current

    if (serialDetail == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val currentItem = serialDetail!!
    val serial = currentItem.assignedSerial

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    // دکمه ویرایش
                    Button(
                        onClick = { onEditClick(serial.serialAssignmentId) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.label_edit),
                            style = typography.titleLarge
                        )
                    }

                    // دکمه تغییر وضعیت
                    OutlinedButton(
                        onClick = { viewModel.openStatusChangeDialog(currentItem) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.PublishedWithChanges,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.label_determination_status),
                            style = typography.titleLarge
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
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
