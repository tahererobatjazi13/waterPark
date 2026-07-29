package ir.kitgroup.partnerManagement.feature.visits.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Schedule
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.LocationRow
import ir.kitgroup.partnerManagement.core.ui.components.Rating
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.components.StatusImageBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.extensions.style
import ir.kitgroup.partnerManagement.feature.dashboard.model.VisitStatus
import ir.kitgroup.partnerManagement.feature.visits.model.VisitDetailModel

@Composable
fun VisitDetailScreen(
    visitId: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    val visit = remember(visitId) {
        VisitDetailModel(
            id = visitId,
            title = "هتل اسپیناس پالاس",
            type = "بازدید حضوری",
            rating = 3,
            person = "محمد رضایی",
            date = "۱۴۰۳/۰۳/۲۴",
            time = "۱۰:۳۰",
            status = VisitStatus.DONE,
            icon = Icons.Default.DirectionsWalk,
            location = "تهران سعادت آباد",
            description = "جلسه با مسئول بازاریابی هتل برگزار شد"
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
                CollectionsInfoCard(visit = visit)

                Spacer(Modifier.height(12.dp))

                VisitorInfoCard(visit = visit)

                Spacer(Modifier.height(12.dp))

                SectionTitle(stringResource(R.string.label_location))

                Spacer(Modifier.height(8.dp))

                LocationCard()

                Spacer(Modifier.height(12.dp))

                SectionTitle(stringResource(R.string.label_description))

                Spacer(Modifier.height(8.dp))

                DescriptionCard(visit.description)

                Spacer(Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun CollectionsInfoCard(visit: VisitDetailModel) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(110.dp)
                    .clip(RoundedCornerShape(14.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = visit.title,
                    modifier = Modifier.fillMaxWidth(),
                    style = typography.titleLarge,
                    color = appColors.textPrimary
                )

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
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            InfoRow(
                label = stringResource(R.string.label_visitor_name),
                value = visit.person,
                icon = Icons.Default.Person
            )

            HorizontalDivider(color = appColors.border)

            InfoRow(
                label = stringResource(R.string.label_visit_date),
                value = visit.date,
                icon = Icons.Default.DateRange
            )

            HorizontalDivider(color = appColors.border)

            InfoRow(
                label = stringResource(R.string.label_visit_time),
                value = visit.time,
                icon = Icons.Default.Schedule
            )

            HorizontalDivider(color = appColors.border)

            InfoRow(
                label = stringResource(R.string.label_status),
                value = stringResource(visit.status.labelRes),
                icon = Icons.Default.CheckCircle,
                status = visit.status
            )
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    icon: ImageVector,
    status: VisitStatus? = null
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
            StatusImageBadge(
                text = value,
                style = status.style()
            )
        } else {
            Text(
                text = value,
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
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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

@Composable
private fun DescriptionCard(text: String) {
    val appColors = LocalPartnerManagementColors.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(appColors.cardBackground)
            .padding(16.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.fillMaxWidth(),
            style = typography.bodySmall,
            color = appColors.textPrimary,
            textAlign = TextAlign.Right
        )
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
