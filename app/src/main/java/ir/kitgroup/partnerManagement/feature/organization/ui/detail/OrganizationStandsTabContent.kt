package ir.kitgroup.partnerManagement.feature.organization.ui.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.feature.advertising_stand.model.AdvertisingStandAssignment
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand_assignment_organization.DetailItem
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand_assignment_organization.InfoItem

@Composable
 fun OrganizationStandsTabContent(
    assignments: List<AdvertisingStandAssignment>,
    onAssignStandsClick: () -> Unit = {},
    onViewItemDetailsClick: (AdvertisingStandAssignment) -> Unit,
) {
    val appColors = LocalPartnerManagementColors.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                SectionTitle(title = stringResource(R.string.label_assignment_stands_to_organization_list))
            }

            Spacer(modifier = Modifier.width(8.dp))
            FilledTonalButton(
                onClick = onAssignStandsClick,
                shape = RoundedCornerShape(12.dp),
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
                    text = stringResource(R.string.label_new_allocation),
                    style = typography.titleLarge
                )
            }
        }
        if (assignments.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.WorkspacePremium,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.msg_no_stand_found),
                        style = typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(
                    items = assignments,
                    key = { _, item -> item.id }
                ) { index, item ->
                    AdvertisingStandAssignmentCard(
                        index = index,
                        advertisingStandAssignment = item,
                        onViewDetailsClick = { onViewItemDetailsClick(item) }
                    )
                }
            }
        }

    }
}

@Composable
private fun AdvertisingStandAssignmentCard(
    index: Int,
    advertisingStandAssignment: AdvertisingStandAssignment,
    onViewDetailsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onViewDetailsClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(0.5.dp, appColors.border)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                InfoItem(
                    label = stringResource(R.string.label_organization_receiving_name),
                    value = advertisingStandAssignment.organizationName,
                    icon = Icons.Default.Business
                )
                StatusBadge(advertisingStandAssignment.status)

            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = 0.5.dp,
                color = appColors.border
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DetailItem(
                        label = stringResource(R.string.label_assignment_type),
                        value = advertisingStandAssignment.assignmentType,
                        icon = Icons.Default.Category
                    )
                    DetailItem(
                        stringResource(R.string.label_delivery_organization_date),
                        advertisingStandAssignment.allocatedDate,
                        Icons.Default.CalendarMonth
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DetailItem(
                        label = stringResource(R.string.label_assignment_mode),
                        value = advertisingStandAssignment.assignmentMode,
                        icon = Icons.Default.AssignmentInd
                    )
                    DetailItem(
                        stringResource(R.string.label_allocated_count),
                        "${advertisingStandAssignment.count} ${stringResource(R.string.label_unit_count)}",
                        Icons.Default.Inventory2
                    )
                }
            }
        }
    }
}