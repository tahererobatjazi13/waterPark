package ir.kitgroup.partnerManagement.feature.visits.ui

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
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Phone
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
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomDescriptionCard
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.LocationRow
import ir.kitgroup.partnerManagement.core.ui.components.Rating
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.Status
import ir.kitgroup.partnerManagement.feature.visits.model.VisitDetailModel
import ir.kitgroup.partnerManagement.feature.visits.model.VisitModel

val demoVisitItems = listOf(
    VisitModel(
        id = 1,
        organizationName = "هتل قصر طلایی",
        visitType = "بازدید حضوری برنامه‌ریزی شده",
        visitorName = "علی محمدی",
        date = "۱۴۰۳/۰۲/۱۵ , 11:30",
        city = "مشهد",
        district = "خیابان آزادی",
        icon = Icons.Default.DirectionsWalk,
        status = Status.PLANNED
    ),
    VisitModel(
        id = 2,
        organizationName = "هتل الماس",
        visitType = "بازدید تلفنی",
        visitorName = "علی رضایی",
        date = "۱۴۰۳/۰۲/۱۷ , 10:30",
        city = "مشهد",
        district = "خیابان آزادی",
        icon = Icons.Default.Phone,
        status = Status.DONE
    ),
    VisitModel(
        id = 3,
        organizationName = "هتل پارسیان",
        visitType = "بازدید حضوری غیربرنامه‌ریزی شده",
        visitorName = "مریم رضایی",
        date = "۱۴۰۳/۰۲/۱۶ , 02:30",
        city = "مشهد",
        district = "احمد آباد",
        icon = Icons.Default.DirectionsWalk,
        status = Status.CANCELLED
    ),
    VisitModel(
        id = 4,
        organizationName = "سازمان برق",
        visitType = "بازدید تلفنی",
        visitorName = "مریم مفرد",
        date = "۱۴۰۳/۰۲/۱۸ , 10:30",
        city = "مشهد",
        district = "پاسداران",
        icon = Icons.Default.Phone,
        status = Status.PLANNED
    )
)

@Composable
fun VisitDetailScreen(
    visitId: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    val visit = remember(visitId) {
        val selected = demoVisitItems.find { it.id == visitId } ?: demoVisitItems.first()
        val dateParts = selected.date.split(",")
        val pureDate = dateParts.getOrNull(0)?.trim() ?: selected.date
        val pureTime = dateParts.getOrNull(1)?.trim() ?: "۱۰:۰۰"

        VisitDetailModel(
            id = selected.id,
            title = selected.organizationName,
            type = selected.visitType,
            subject = if (selected.visitType.contains("تلفنی")) "پیگیری و مذاکره تلفنی" else "بررسی استند و عقد قرارداد",
            rating = 4,
            person = selected.visitorName,
            scheduledDate = pureDate,
            date = pureDate,
            time = pureTime,
            status = selected.status,
            icon = selected.icon,
            location = "${selected.city}، ${selected.district}",
            description = "گزارش ثبت شده برای ${selected.organizationName} توسط ${selected.visitorName}."
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        CustomHeader(
            title = R.string.label_visit_details,
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
                OrganizationInfoCard(visit = visit)

                Spacer(Modifier.height(12.dp))

                VisitorInfoCard(visit = visit)

                Spacer(Modifier.height(12.dp))

                SectionTitle(stringResource(R.string.label_location))

                Spacer(Modifier.height(8.dp))

                LocationCard()

                Spacer(Modifier.height(12.dp))

                SectionTitle(stringResource(R.string.label_description))

                Spacer(Modifier.height(8.dp))

                CustomDescriptionCard(visit.description)

                Spacer(Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun OrganizationInfoCard(visit: VisitDetailModel) {
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
                        text = visit.title,
                        style = typography.titleLarge,
                        color = appColors.textPrimary
                    )
                    StatusBadge(visit.status)
                }

                Spacer(Modifier.height(6.dp))
                LocationRow(
                    location = visit.location,
                )
                Spacer(Modifier.height(8.dp))
                Rating(visit.rating)
            }
        }
    }
}

@Composable
private fun VisitorInfoCard(visit: VisitDetailModel) {
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
            InfoRow(
                label = stringResource(R.string.label_visit_type),
                value = visit.type,
                icon = visit.icon
            )

            HorizontalDivider(color = appColors.border.copy(alpha = 0.5f))

            InfoRow(
                label = stringResource(R.string.label_visit_subject),
                value = visit.subject,
                icon = Icons.Default.Subject
            )

            HorizontalDivider(color = appColors.border.copy(alpha = 0.5f))

            InfoRow(
                label = stringResource(R.string.label_visitor_name),
                value = visit.person,
                icon = Icons.Default.Badge
            )

            HorizontalDivider(color = appColors.border.copy(alpha = 0.5f))

            InfoRow(
                label = stringResource(R.string.label_visit_Scheduled_date),
                value = visit.scheduledDate,
                icon = Icons.Default.Event
            )

            HorizontalDivider(color = appColors.border.copy(alpha = 0.5f))

            InfoRow(
                label = stringResource(R.string.label_visit_real_date),
                value = visit.date,
                icon = Icons.Default.EventAvailable
            )

            HorizontalDivider(color = appColors.border.copy(alpha = 0.5f))

            InfoRow(
                label = stringResource(R.string.label_visit_time),
                value = visit.time,
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
    status: Status? = null
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
            StatusBadge(status)
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
private fun VisitDetailScreenPreview() {
    AppScreenPreview {
        VisitDetailScreen(
            visitId = 1,
            onBackClick = {}
        )
    }
}
