package ir.kitgroup.partnerManagement.feature.report.ui.visitor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.components.JalaliRangeCalendar

// Chart (Vico)
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf
import ir.kitgroup.partnerManagement.feature.report.model.KpiUi
import ir.kitgroup.partnerManagement.feature.report.model.VisitorRowUi
import ir.kitgroup.partnerManagement.feature.report.model.VisitorsDashboardUi
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import ir.kitgroup.partnerManagement.core.ui.components.DateRangeChip

@Composable
fun VisitorsPerformanceDashboardScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    ui: VisitorsDashboardUi = demoVisitorsDashboardUi(),
) {
    val appColors = LocalPartnerManagementColors.current
    var showDateRangeDialog by remember {
        mutableStateOf(false)
    }

    var startDate by remember {
        mutableStateOf(ui.startDatePersian)
    }

    var endDate by remember {
        mutableStateOf(ui.endDatePersian)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            CustomHeader(
                title = R.string.label_visitors_performance_analysis,
                showBackButton = true,
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = appColors.screenBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                DateRangeChip(
                    startDate = startDate,
                    endDate = endDate,
                    onClick = {
                        showDateRangeDialog = true
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                KpiGrid(
                    items = ui.kpis,
                    columns = 3
                )

                Spacer(modifier = Modifier.height(16.dp))

                VisitorsTable(
                    rows = ui.rows
                )

                Spacer(modifier = Modifier.height(16.dp))

                VisitorsComparisonChart(
                    rows = ui.rows
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    if (showDateRangeDialog) {
        AlertDialog(
            onDismissRequest = {
                showDateRangeDialog = false
            },
            confirmButton = {},
            dismissButton = {},
            text = {
                Column {
                    Text(
                        text = stringResource(R.string.label_date_range_selection),
                        style = MaterialTheme.typography.titleMedium,
                        color = LocalPartnerManagementColors.current.textPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    JalaliRangeCalendar(
                        onRangeSelected = { start, end ->
                            startDate = "${start.year}/${
                                start.month.toString().padStart(2, '0')
                            }/${start.day.toString().padStart(2, '0')}"

                            endDate = "${end.year}/${
                                end.month.toString().padStart(2, '0')
                            }/${end.day.toString().padStart(2, '0')}"

                            showDateRangeDialog = false
                        }
                    )
                }
            }
        )
    }
}

@Composable
private fun KpiGrid(
    items: List<KpiUi>,
    columns: Int
) {
    val chunkedItems = items.chunked(columns)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        chunkedItems.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                rowItems.forEach { item ->
                    KpiCard(
                        title = item.title,
                        value = item.value,
                        icon = item.icon,
                        modifier = Modifier.weight(1f)
                    )
                }

                val remaining = columns - rowItems.size
                if (remaining > 0) {
                    repeat(remaining) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun KpiCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = modifier
            .wrapContentHeight()
            .defaultMinSize(minHeight = 110.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(0.7.dp, appColors.border)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 6.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(appColors.iconBlueContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = appColors.onInfoContainer,
                    modifier = Modifier.size(15.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                    lineHeight = 13.sp
                ),
                color = appColors.textSecondary,
                textAlign = TextAlign.Center,
                maxLines = 3,
                minLines = 3,
                softWrap = true
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                color = appColors.onInfoContainer,
                maxLines = 1
            )
        }
    }
}
@Composable
private fun VisitorsTable(
    rows: List<VisitorRowUi>,
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(0.8.dp, appColors.border)
    ) {
        val scroll = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scroll)
        ) {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(horizontal = 10.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TableCell(
                    text = stringResource(R.string.label_visitor_name),
                    width = 130.dp,
                    bold = true
                )
                TableCell(
                    text = stringResource(R.string.label_count_visit),
                    width = 90.dp,
                    bold = true,
                    center = true
                )
                TableCell(
                    text = stringResource(R.string.label_count_calls),
                    width = 90.dp,
                    bold = true,
                    center = true
                )
                TableCell(
                    text = stringResource(R.string.label_count_cooperation_requests),
                    width = 130.dp,
                    bold = true,
                    center = true
                )
                TableCell(
                    text = stringResource(R.string.label_count_discount_cards_organizations),
                    width = 140.dp,
                    bold = true,
                    center = true
                )
                TableCell(
                    text = stringResource(R.string.label_count_stands_organizations),
                    width = 130.dp,
                    bold = true,
                    center = true
                )
            }

            // استفاده از HorizontalDivider جدید به جای Divider قدیمی منسوخ شده
            HorizontalDivider(
                thickness = 1.dp,
                color = appColors.border
            )

            rows.forEachIndexed { i, row ->
                Row(
                    modifier = Modifier
                        .background(if (i % 2 == 0) appColors.tableRowEven else appColors.tableRowOdd)
                        .padding(horizontal = 10.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TableCell(text = row.name, width = 130.dp)
                    TableCell(text = row.visits.toString(), width = 90.dp, center = true)
                    TableCell(text = row.calls.toString(), width = 90.dp, center = true)
                    TableCell(text = row.introLetters.toString(), width = 130.dp, center = true)
                    TableCell(text = row.tutorials.toString(), width = 140.dp, center = true)
                    TableCell(text = row.receivedCards.toString(), width = 130.dp, center = true)
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = appColors.border.copy(alpha = 0.6f)
                )
            }
        }
    }
}


@Composable
private fun TableCell(
    text: String,
    width: androidx.compose.ui.unit.Dp,
    bold: Boolean = false,
    center: Boolean = false,
    maxLines: Int = 2
) {
    val appColors = LocalPartnerManagementColors.current
    Text(
        text = text,
        modifier = Modifier.width(width),
        style = MaterialTheme.typography.bodySmall,
        fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
        color = appColors.textPrimary,
        textAlign = if (center) TextAlign.Center else TextAlign.Start,
        maxLines = maxLines,
        softWrap = true
    )
}

@Composable
private fun VisitorsComparisonChart(
    rows: List<VisitorRowUi>,
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(0.8.dp, appColors.border)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.BarChart,
                    contentDescription = null,
                    tint = appColors.info
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.label_visitors_activity_comparison),
                    style = MaterialTheme.typography.titleMedium,
                    color = appColors.textPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LegendRow(
                items = listOf(
                    LegendItem(stringResource(R.string.label_count_visit), appColors.chartBlue),
                    LegendItem(stringResource(R.string.label_count_calls), appColors.chartTeal),
                    LegendItem(
                        stringResource(R.string.label_count_cooperation_requests),
                        appColors.chartOrange
                    )
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            val modelProducer = remember(rows) {
                ChartEntryModelProducer(buildGroupedChartEntries(rows))
            }

            val columnChart = columnChart(
                columns = listOf(
                    LineComponent(
                        color = appColors.chartBlue.toArgb(),
                        thicknessDp = 8f,
                        shape = Shapes.roundedCornerShape(25)
                    ),
                    LineComponent(
                        color = appColors.chartTeal.toArgb(),
                        thicknessDp = 8f,
                        shape = Shapes.roundedCornerShape(25)
                    ),
                    LineComponent(
                        color = appColors.chartOrange.toArgb(),
                        thicknessDp = 8f,
                        shape = Shapes.roundedCornerShape(25)
                    ),
                    LineComponent(
                        color = appColors.chartPurple.toArgb(),
                        thicknessDp = 8f,
                        shape = Shapes.roundedCornerShape(25)
                    )
                ),
                spacing = 16.dp
            )

            Chart(
                chart = columnChart,
                chartModelProducer = modelProducer,
                startAxis = rememberStartAxis(),
                bottomAxis = rememberBottomAxis(
                    valueFormatter = { value, _ ->
                        rows.getOrNull(value.toInt())?.name ?: ""
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            )
        }
    }
}

private data class LegendItem(val title: String, val color: Color)

@Composable
private fun LegendRow(items: List<LegendItem>) {
    val appColors = LocalPartnerManagementColors.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items.forEach { item ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(item.color, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.labelSmall,
                    color = appColors.textSecondary,
                    maxLines = 1
                )
            }
        }
    }
}

private fun buildGroupedChartEntries(rows: List<VisitorRowUi>): List<List<ChartEntry>> {
    val visits = mutableListOf<ChartEntry>()
    val calls = mutableListOf<ChartEntry>()
    val intro = mutableListOf<ChartEntry>()

    rows.forEachIndexed { index, row ->
        val x = index.toFloat()
        visits += entryOf(x, row.visits.toFloat())
        calls += entryOf(x, row.calls.toFloat())
        intro += entryOf(x, row.introLetters.toFloat())
    }
    return listOf(visits, calls, intro)
}

@Composable
private fun demoVisitorsDashboardUi(): VisitorsDashboardUi {
    val rows = listOf(
        VisitorRowUi(
            "1",
            "علی محمدی",
            visits = 148,
            calls = 43,
            introLetters = 32,
            tutorials = 28,
            receivedCards = 22
        ),
        VisitorRowUi(
            "2",
            "سارا احمدی",
            visits = 123,
            calls = 38,
            introLetters = 26,
            tutorials = 21,
            receivedCards = 20
        ),
        VisitorRowUi(
            "3",
            "مهدی رضایی",
            visits = 102,
            calls = 31,
            introLetters = 23,
            tutorials = 18,
            receivedCards = 16
        ),
        VisitorRowUi(
            "4",
            "نگین حسینی",
            visits = 92,
            calls = 27,
            introLetters = 20,
            tutorials = 15,
            receivedCards = 14
        ),
        VisitorRowUi(
            "5",
            "رضا کریمی",
            visits = 78,
            calls = 22,
            introLetters = 18,
            tutorials = 16,
            receivedCards = 13
        ),
        VisitorRowUi(
            "6",
            "ندا کرمی",
            visits = 55,
            calls = 44,
            introLetters = 18,
            tutorials = 16,
            receivedCards = 15
        ),
    )

    return VisitorsDashboardUi(
        startDatePersian = "۱۴۰۳/۰۲/۰۱",
        endDatePersian = "۱۴۰۳/۰۲/۳۱",
        kpis = listOf(
            KpiUi(stringResource(R.string.label_count_visitor), "۱۹۸", Icons.Default.People),
            KpiUi(stringResource(R.string.label_count_visit), "۲۸۹", Icons.Default.LocationOn),
            KpiUi(stringResource(R.string.label_count_calls), "۳۶۷", Icons.Default.Call),
            KpiUi(
                stringResource(R.string.label_count_cooperation_requests),
                "۱,۲۴۵",
                Icons.Default.Handshake
            ),
            KpiUi(
                stringResource(R.string.label_count_discount_cards_visitors),
                "۵۲",
                Icons.Default.CardGiftcard
            ),
            KpiUi(
                stringResource(R.string.label_count_stands_visitors),
                "۵۲",
                Icons.Default.CoPresent
            ),
            KpiUi(
                stringResource(R.string.label_count_discount_cards_organizations),
                "۸.۶",
                Icons.Default.Business
            ),
        ),
        rows = rows
    )
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun VisitorsPerformanceDashboardScreenPreview() {
    AppScreenPreview {
        VisitorsPerformanceDashboardScreen(
            onBackClick = {})
    }
}
