package ir.kitgroup.hotel.feature.dashboard.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ir.kitgroup.hotel.R
import ir.kitgroup.hotel.core.ui.theme.*
import ir.kitgroup.hotel.core.ui.util.SideCurvedHeaderShape
import ir.kitgroup.hotel.feature.dashboard.model.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import ir.kitgroup.hotel.core.ui.components.LocationRow
import ir.kitgroup.hotel.core.ui.components.Rating
import ir.kitgroup.hotel.core.ui.components.SectionTitle
import ir.kitgroup.hotel.core.ui.components.StatusBadge
import ir.kitgroup.hotel.core.ui.util.extensions.style
import androidx.compose.foundation.BorderStroke

@Composable
fun DashboardScreen(navController: NavController) {

    val summaryItems = listOf(
        SummaryCardData(
            Icons.Filled.Person,
            Green,
            "18",
            stringResource(R.string.label_in_person_visits),
            Green
        ),
        SummaryCardData(
            Icons.Filled.CreditCard,
            Purple,
            "12",
            stringResource(R.string.label_delivered_referrals),
            Purple
        ),
        SummaryCardData(
            Icons.Filled.Call,
            Blue,
            "27",
            stringResource(R.string.label_phone_calls),
            Blue
        ),
        SummaryCardData(
            Icons.Filled.Domain,
            Orange,
            "34",
            stringResource(R.string.label_received_visits),
            Orange
        )
    )

    val visits = listOf(
        VisitItem(
            "10:00",
            "هتل آزادی",
            "مشهد",
            "خیابان آزادی",
            4,
            VisitStatus.DONE
        ),
        VisitItem("11:30", "مجموعه پالاس", "مشهد", "احمد آباد", 4, VisitStatus.VISITING),
        VisitItem("14:00", "هتل پردیسان", "مشهد", "پاسداران", 3, VisitStatus.PLANNED),
        VisitItem("16:30", "سازمان بزرگ سیمرغ", "مشهد", "ولیعصر", 5, VisitStatus.PLANNED)
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        DashboardHeader()
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { SummarySection(summaryItems) }
                item { Spacer(Modifier.width(6.dp)) }
                item {
                    QuickActionsSection(
                        onQuickVisitClick = { navController.navigate("register_visit/physical") }, // حضوری
                        onQuickCallClick = { navController.navigate("register_visit/phone") }      // تلفنی
                    )
                }
                item { Spacer(Modifier.width(6.dp)) }
                item {
                    SectionTitle(
                        stringResource(R.string.label_schedule_title),
                        Icons.Default.Schedule
                    )
                }
                items(visits) {
                    VisitCard(it)
                }
            }
        }
    }
}

@Composable
private fun DashboardHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = SideCurvedHeaderShape(50f)
            )
            .windowInsetsPadding(WindowInsets.statusBars)
            .height(60.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        NotificationIcon()

        Text(
            text = stringResource(R.string.greeting_text),
            color = MaterialTheme.colorScheme.onPrimary,
            style = typography.titleLarge
        )
    }
}

@Composable
private fun NotificationIcon() {

    Box(
        modifier = Modifier.size(32.dp),
        contentAlignment = Alignment.Center
    ) {

        Icon(
            painter = painterResource(R.drawable.ic_bell),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(24.dp)
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(4.dp, (-4).dp)
                .size(16.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.error),
            contentAlignment = Alignment.Center
        ) {
            Text("9", color = MaterialTheme.colorScheme.onError, fontSize = 9.sp)
        }
    }
}


@Composable
private fun SummarySection(items: List<SummaryCardData>) {

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        SectionTitle(stringResource(R.string.label_summary_title))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            items.forEach {
                SummaryCard(it, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun SummaryCard(data: SummaryCardData, modifier: Modifier) {
    Card(
        modifier = modifier.height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = data.accent.copy(alpha = 0.08f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(data.iconColor.copy(.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(data.icon, null, tint = data.iconColor)
            }

            Text(
                data.value,
                color = MaterialTheme.colorScheme.onSurface,
                style = typography.titleLarge
            )

            Text(
                data.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = typography.labelSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(20))
                    .background(data.accent)
            )
        }
    }
}

@Composable
private fun QuickActionsSection(
    onQuickVisitClick: () -> Unit,
    onQuickCallClick: () -> Unit
) {

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        SectionTitle(stringResource(R.string.label_shortcut_title))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

            QuickActionCard(
                stringResource(R.string.label_register_visit_phone),
                Icons.Filled.Call,
                onQuickCallClick,
                Modifier.weight(1f)
            )
            QuickActionCard(
                stringResource(R.string.label_register_visit_physical),
                Icons.Filled.CalendarViewDay,
                onQuickVisitClick,
                Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier
) {

    Card(
        modifier = modifier
            .height(64.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color.Transparent)
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                        )
                    )
                )
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Icon(
                icon,
                null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(18.dp)
            )

            Spacer(Modifier.width(6.dp))

            Text(
                title, color = MaterialTheme.colorScheme.onPrimary, style = typography.labelMedium
            )
        }
    }
}

@Composable
private fun VisitCard(item: VisitItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 0.7.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.90f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            VisitTimeAndStatus(item)

            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    text = item.hotelName,
                    style = typography.titleMedium,
                    textAlign = TextAlign.End
                )

                Rating(item.rating)

                LocationRow(
                    location = "${item.city}، ${item.district}"
                )
            }

            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
        }
    }
}

@Composable
fun VisitTimeAndStatus(item: VisitItem) {
    val statusStyle = item.status.style()

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = item.time,
            style = typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        StatusBadge(
            text = stringResource(item.status.labelRes),
            icon = item.status.icon,
            style = statusStyle
        )
    }
}

