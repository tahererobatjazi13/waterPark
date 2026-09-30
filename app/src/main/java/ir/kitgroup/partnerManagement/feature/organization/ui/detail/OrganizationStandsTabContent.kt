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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
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
import ir.kitgroup.partnerManagement.core.database.model.AssignedStandWithDetail
import ir.kitgroup.partnerManagement.core.ui.components.EmptyState
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.AssignmentMode
import ir.kitgroup.partnerManagement.core.ui.util.AssignmentType
import ir.kitgroup.partnerManagement.core.ui.util.StandAssignmentStatus
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate
import ir.kitgroup.partnerManagement.feature.stand.ui.assigned_stands_organization.DetailItem
import ir.kitgroup.partnerManagement.feature.stand.ui.assigned_stands_organization.InfoItem

@Composable
fun OrganizationStandsTabContent(
    assignments: List<AssignedStandWithDetail>,
    onAssignStandsClick: () -> Unit = {},
    onViewItemDetailsClick: (AssignedStandWithDetail) -> Unit,
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
            EmptyState(
                textRes = R.string.msg_no_stand_found,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(
                    items = assignments,
                    key = { _, item -> item.assignedStand.assignedStandId }
                ) { _, item ->
                    AdvertisingStandAssignmentCard(
                        itemWithDetail = item,
                        onViewDetailsClick = { onViewItemDetailsClick(item) }
                    )
                }
            }
        }
    }
}

@Composable
private fun AdvertisingStandAssignmentCard(
    itemWithDetail: AssignedStandWithDetail,
    onViewDetailsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    val assignment = itemWithDetail.assignedStand

    val assignmentTypeTitle =
        AssignmentType.fromId(assignment.assignmentType)?.let {
            stringResource(it.titleRes)
        } ?: assignment.assignmentTypeName ?: "-"

    val assignmentModeTitle =
        AssignmentMode.fromId(assignment.assignmentMode)?.let {
            stringResource(it.titleRes)
        } ?: assignment.assignmentModeName ?: "-"

    // تاریخ تحویل به سازمان یا تاریخ تخصیص
    val displayDate = assignment.deliveryToOrgDate
        ?: assignment.assignmentDate
        ?: "-"

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
                    label = stringResource(R.string.label_stand_type),
                    value = itemWithDetail.standTitle,
                    icon = Icons.Default.Devices
                )
                assignment.statusAssign?.let { statusId ->
                    StatusBadge(status = StandAssignmentStatus.fromId(statusId))
                }
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
                        value = assignmentTypeTitle,
                        icon = Icons.Default.Category
                    )
                    DetailItem(
                        label = stringResource(R.string.label_delivery_organization_date),
                        value = formatJalaliDate(displayDate),
                        icon = Icons.Default.CalendarMonth
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DetailItem(
                        label = stringResource(R.string.label_assignment_mode),
                        value = assignmentModeTitle,
                        icon = Icons.Default.AssignmentInd
                    )
                    DetailItem(
                        label = stringResource(R.string.label_allocated_count),
                        value = "${assignment.count ?: 0} ${stringResource(R.string.label_unit_count)}",
                        icon = Icons.Default.Inventory2
                    )
                }
            }
        }
    }
}
