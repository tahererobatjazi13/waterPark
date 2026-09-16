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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.graphics.vector.ImageVector
import ir.kitgroup.partnerManagement.core.ui.components.ActionIconButton
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.feature.organization.model.VisitorOrganization


@Composable
fun OrganizationVisitorTabContent(
    visitors: List<VisitorOrganization> = emptyList(),
    onAssignVisitorClick: () -> Unit = {},
    onEditVisitorClick: (Int) -> Unit = {},
    onDeleteVisitorClick: (Int) -> Unit = {},
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
                SectionTitle(title = stringResource(R.string.label_assignment_visitor_to_organization_list))
            }
            FilledTonalButton(
                onClick = onAssignVisitorClick,
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
                    text = stringResource(R.string.label_new_assigning),
                    style = typography.titleLarge
                )
            }
        }
        if (visitors.isEmpty()) {
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
                        text = stringResource(R.string.msg_no_visitor_found),
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
                    items = visitors,
                    key = { _, item -> item.id }
                ) { index, visitor ->
                    RelatedVisitorCard(
                        index = index,
                        visitor = visitor,
                        onEditClick = {
                            onEditVisitorClick(visitor.id)
                        },
                        onDeleteClick = {
                            onDeleteVisitorClick(visitor.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RelatedVisitorCard(
    index: Int,
    visitor: VisitorOrganization,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(0.5.dp, appColors.border)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${index + 1}. ${visitor.name}".trim(),
                    style = typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                StatusBadge(visitor.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                // تاریخ شروع
                VisitorStatItem(
                    label = stringResource(R.string.label_start_date),
                    date = visitor.startDate,
                    icon = Icons.Default.CalendarMonth
                )

                // تاریخ خاتمه
                VisitorStatItem(
                    label = stringResource(R.string.label_end_date),
                    date = visitor.endDate,
                    icon = Icons.Default.CalendarMonth
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    space = 8.dp,
                    alignment = Alignment.End
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActionIconButton(
                    icon = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.label_edit),
                    onClick = onEditClick,
                    tint = MaterialTheme.colorScheme.primary,
                    backgroundColor = appColors.cardBackgroundAlt
                )

                ActionIconButton(
                    icon = Icons.Default.DeleteOutline,
                    contentDescription = stringResource(R.string.label_delete),
                    onClick = onDeleteClick,
                    tint = MaterialTheme.colorScheme.error,
                    backgroundColor = MaterialTheme.colorScheme.errorContainer
                )
            }
        }
    }
}

@Composable
private fun VisitorStatItem(
    label: String,
    date: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = colors.onSurfaceVariant
        )

        // عنوان (مثلاً: تاریخ شروع:)
        Text(
            text = "$label:",
            style = typography.labelMedium,
            color = colors.onSurfaceVariant
        )

        // مقدار تاریخ
        Text(
            text = date.ifBlank { "—" },
            style = typography.labelLarge,
            color = colors.onSurface
        )
    }
}