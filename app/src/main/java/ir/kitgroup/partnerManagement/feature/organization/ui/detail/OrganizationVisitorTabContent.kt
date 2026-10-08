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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.VisitorOrganizationEntity
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.EmptyState
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.DataState
import ir.kitgroup.partnerManagement.core.ui.util.VisitorOrganizationStatus
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate

@Composable
fun OrganizationVisitorTabContent(
    visitorsState: DataState<List<VisitorOrganizationEntity>>,
    onRetry: () -> Unit,
    onAssignVisitorClick: () -> Unit = {},
    onEditVisitorClick: (String) -> Unit = {},
    onDeleteVisitorClick: (String) -> Unit = {},
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

        when (visitorsState) {
            is DataState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            is DataState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "خطا در دریافت اطلاعات از سرور",
                        style = typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomButton(
                        text = "تلاش مجدد",
                        onClick = onRetry
                    )
                }
            }

            is DataState.Success -> {
                val visitors = visitorsState.data
                if (visitors.isEmpty()) {
                    EmptyState(
                        textRes = R.string.msg_no_visitor_found,
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
                            items = visitors,
                            key = { _, item -> item.visitorOrganizationId }
                        ) { index, visitor ->
                            VisitorOrganizationCard(
                                index = index,
                                visitor = visitor,
                                onEditClick = { onEditVisitorClick(visitor.visitorOrganizationId) },
                                onDeleteClick = { onDeleteVisitorClick(visitor.visitorOrganizationId) }
                            )
                        }
                    }
                }
            }

            is DataState.Idle -> {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun VisitorOrganizationCard(
    index: Int,
    visitor: VisitorOrganizationEntity,
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
                    text = "${index + 1}. ${visitor.name ?: ""}".trim(),
                    style = typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                StatusBadge(status = VisitorOrganizationStatus.fromId(visitor.statusRelation ?: 0))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                VisitorStatItem(
                    label = stringResource(R.string.label_start_date),
                    date = formatJalaliDate(visitor.dateStart),
                    icon = Icons.Default.CalendarMonth
                )

                VisitorStatItem(
                    label = stringResource(R.string.label_end_date),
                    date = formatJalaliDate(visitor.dateEnd),
                    icon = Icons.Default.CalendarMonth
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            /*  Row(
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
              }*/
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
        Text(
            text = "$label:",
            style = typography.labelMedium,
            color = colors.onSurfaceVariant
        )
        Text(
            text = date.ifBlank { "—" },
            style = typography.labelLarge,
            color = colors.onSurface
        )
    }
}
