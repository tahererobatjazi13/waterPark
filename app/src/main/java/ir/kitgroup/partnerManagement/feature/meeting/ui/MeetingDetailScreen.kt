package ir.kitgroup.partnerManagement.feature.meeting.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Subject
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.MeetingEntity
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomDescriptionCard
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.LocationRow
import ir.kitgroup.partnerManagement.core.ui.components.Rating
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.MeetingType
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationStatus
import ir.kitgroup.partnerManagement.core.ui.util.demoMeetings



@Composable
fun MeetingDetailScreen(
    meetingId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    val meeting = remember(meetingId) {
        val selected = demoMeetings.find { it.meetingId == meetingId } ?: demoMeetings.first()
        val dateParts = selected.visitDate!!.split(",")
        val pureDate = dateParts.getOrNull(0)?.trim() ?: selected.visitDate
        val pureTime = dateParts.getOrNull(1)?.trim() ?: "۱۰:۰۰"

        MeetingEntity(
            meetingId = "meet-001",
            name = "بازدید حضوری برنامه‌ریزی شده - هتل قصر طلایی",
            description = "مشهد، خیابان آزادی",
            organizationId = "org-ghasr-talaee",
            status = 2,
            type = 1,
            visitDate = "1403/02/15",
            visitTime = "11:30",
            visitorId = "visitor-ali-mohammadi",
            visitRealDate = "1403/02/15",
            subjectVisitId = "subj-regular-inspection",
            personId = "person-ali-mohammadi",
            latitude = 36.2972,
            longitude = 59.6067
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        CustomHeader(
            title = R.string.label_meeting_details,
            showBackButton = true,
            onBackClick = onBackClick
        )
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = appColors.screenBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                OrganizationInfoCard(meeting = meeting)

                Spacer(Modifier.height(12.dp))

                VisitorInfoCard(meeting = meeting)

                Spacer(Modifier.height(12.dp))

                SectionTitle(stringResource(R.string.label_location))

                Spacer(Modifier.height(8.dp))

                LocationCard()

                Spacer(Modifier.height(12.dp))

                SectionTitle(stringResource(R.string.label_description))

                Spacer(Modifier.height(8.dp))

                CustomDescriptionCard(meeting.description!!)

                Spacer(Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun OrganizationInfoCard(meeting: MeetingEntity) {
    val appColors = LocalPartnerManagementColors.current

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
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = meeting.name.orEmpty(),
                        style = typography.titleLarge,
                        color = appColors.textPrimary
                    )
                    StatusBadge(
                        status = OrganizationStatus.fromId(meeting.status)
                    )
                }

                Spacer(Modifier.height(6.dp))

                LocationRow(
                    location = meeting.description ?: "-"
                )

                Spacer(Modifier.height(8.dp))

                Rating( 4)
            }
        }
    }
}

@Composable
private fun VisitorInfoCard(meeting: MeetingEntity) {
    val appColors = LocalPartnerManagementColors.current

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
        Column {
            val meetingType = MeetingType.fromValue(meeting.type)

            InfoRow(
                label = stringResource(R.string.label_visit_type),
                value = stringResource(id = meetingType.titleRes),
                icon = meetingType.icon
            )


            HorizontalDivider(color = appColors.border.copy(alpha = 0.5f))

            InfoRow(
                label = stringResource(R.string.label_visit_subject),
                value = meeting.subjectVisitId,
                icon = Icons.Default.Subject
            )

            HorizontalDivider(color = appColors.border.copy(alpha = 0.5f))

            InfoRow(
                label = stringResource(R.string.label_visitor_name),
                value = meeting.visitorId,
                icon = Icons.Default.Badge
            )

            HorizontalDivider(color = appColors.border.copy(alpha = 0.5f))

            InfoRow(
                label = stringResource(R.string.label_visit_Scheduled_date),
                value = meeting.visitDate,
                icon = Icons.Default.Event
            )

            HorizontalDivider(color = appColors.border.copy(alpha = 0.5f))

            InfoRow(
                label = stringResource(R.string.label_visit_real_date),
                value = meeting.visitRealDate,
                icon = Icons.Default.EventAvailable
            )

            HorizontalDivider(color = appColors.border.copy(alpha = 0.5f))

            InfoRow(
                label = stringResource(R.string.label_visit_time),
                value = meeting.visitTime,
                icon = Icons.Default.Schedule
            )
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String? = null,
    icon: ImageVector,
    status: Int? = null
) {
    val appColors = LocalPartnerManagementColors.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "$label :",
                style = typography.labelMedium,
                color = appColors.textSecondary
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        if (status != null) {
            StatusBadge(status = OrganizationStatus.fromId(status))

        } else {
            Text(
                text = value ?: "-",
                style = typography.labelMedium,
                color = appColors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
private fun LocationCard() {
    val appColors = LocalPartnerManagementColors.current

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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(12.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(44.dp)
                    .align(Alignment.Center)
            )

            Icon(
                imageVector = Icons.Default.Fullscreen,
                contentDescription = null,
                tint = appColors.textPrimary,
                modifier = Modifier
                    .size(22.dp)
                    .align(Alignment.TopEnd)
            )
        }
    }
}


@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun MeetingDetailScreenPreview() {
    AppScreenPreview {
        MeetingDetailScreen(
            meetingId = "",
            onBackClick = {}
        )
    }
}
