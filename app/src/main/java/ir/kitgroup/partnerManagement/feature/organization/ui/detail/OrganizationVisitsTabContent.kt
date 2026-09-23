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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
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
import ir.kitgroup.partnerManagement.core.database.entity.MeetingEntity
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.util.MeetingType
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationStatus
import ir.kitgroup.partnerManagement.feature.meeting.ui.DetailRow
import ir.kitgroup.partnerManagement.feature.meeting.ui.MeetingTypeChip

@Composable
fun OrganizationVisitsTabContent(
    visits: List<MeetingEntity>,
    onAddMeetingClick: () -> Unit,
    onMeetingClick: ((String) -> Unit),
    modifier: Modifier = Modifier
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
                SectionTitle(title = stringResource(R.string.label_visits_list))
            }
            FilledTonalButton(
                onClick = onAddMeetingClick,
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
                    text = stringResource(R.string.label_register_visit),
                    style = typography.titleLarge
                )
            }
        }


        if (visits.isEmpty()) {
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
                        text = stringResource(R.string.msg_no_visit_found),
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
                itemsIndexed(visits) { index, visit ->
                    VisitCard(
                        item = visit,
                        onClick = { onMeetingClick(visit.meetingId) },
                        onEditClick = { /*onEditVisitClick(item.id)*/ },
                        onDeleteClick = { /*visitPendingDelete = item */ }
                    )
                }

            }
        }
    }
}

@Composable
fun VisitCard(
    item: MeetingEntity,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColors.cardBackground
        ),
        border = BorderStroke(
            width = 0.7.dp,
            color = appColors.border
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // ردیف اول: نوع بازدید (راست/شروع) و بج وضعیت (چپ/پایان)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val visitType = MeetingType.fromValue(item.type)

                MeetingTypeChip(
                    text = stringResource(id = visitType.titleRes),
                    icon = visitType.icon
                )

                StatusBadge(status = OrganizationStatus.fromId(item.status))

            }

            Spacer(modifier = Modifier.height(10.dp))

            // ردیف نام بازدیدکننده
            DetailRow(
                icon = Icons.Default.Person,
                text = item.personId
            )

            Spacer(modifier = Modifier.height(6.dp))

            // ردیف تاریخ و دکمه‌های اکشن در یک راستا برای کاهش ارتفاع کارت
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DetailRow(
                    icon = Icons.Default.DateRange,
                    text = item.visitDate
                )
                /*
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
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
}
