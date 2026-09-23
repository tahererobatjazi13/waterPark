package ir.kitgroup.partnerManagement.feature.report.ui.organization

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.components.JalaliRangeCalendar
import ir.kitgroup.partnerManagement.feature.report.ui.organization.model.ReportDashboardUi
import ir.kitgroup.partnerManagement.feature.report.ui.organization.model.ReportRowUi
import ir.kitgroup.partnerManagement.core.ui.components.DateRangeChip
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationStatus
import ir.kitgroup.partnerManagement.core.ui.util.Status

@Composable
fun ReportContractScreen(
    onBackClick: () -> Unit,
    ui: ReportDashboardUi = demoReportDashboardUi()
) {
    val appColors = LocalPartnerManagementColors.current
    var showDateRangeDialog by remember {
        mutableStateOf(false)
    }

    var startDate by remember {
        mutableStateOf("1403/02/01")
    }

    var endDate by remember {
        mutableStateOf("1403/02/31")
    }

    Scaffold(
        topBar = {
            CustomHeader(
                title = R.string.label_report_cooperation_agreement,
                showBackButton = true,
                onBackClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.primary
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

                ReportTable(
                    rows = ui.rows,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(30.dp))

                TotalCardsRow(ui = ui)
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
                        color = appColors.textPrimary
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
private fun ReportTable(
    rows: List<ReportRowUi>,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(1.dp, appColors.border)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(appColors.tableHeaderBackground)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TableCellHeader(
                    text = stringResource(R.string.label_organization_name),
                    modifier = Modifier.weight(2.5f)
                )

                TableCellHeader(
                    text = stringResource(R.string.label_start_date),
                    modifier = Modifier.weight(1.6f),
                    center = true
                )

                TableCellHeader(
                    text = stringResource(R.string.label_cooperation_model),
                    modifier = Modifier.weight(2.4f),
                    center = true
                )

                TableCellHeader(
                    text = stringResource(R.string.label_status),
                    modifier = Modifier.weight(1.5f),
                    center = true
                )

            }
            HorizontalDivider(color = appColors.border)

            // ردیف‌های جدول با همان نسبت‌های هدر جهت تراز دقیق ستون‌ها
            rows.forEachIndexed { index, row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (index % 2 == 0) appColors.tableRowEven else appColors.tableRowOdd)
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TableCell(
                        text = row.collectionName,
                        modifier = Modifier.weight(2.5f)
                    )

                    TableCell(
                        text = row.contractDate,
                        modifier = Modifier.weight(1.6f),
                        center = true
                    )

                    TableCell(
                        text = row.cooperate,
                        modifier = Modifier.weight(2.4f),
                        center = true
                    )

                    Column(
                        modifier = Modifier.weight(1.5f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        StatusBadge(status = OrganizationStatus.fromId(row.status))

                    }

                }
                HorizontalDivider(color = appColors.border.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
private fun TotalCardsRow(ui: ReportDashboardUi) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TotalItem(
            title = stringResource(R.string.label_total_contracts_count),
            value = ui.totalManual.toString(),
            modifier = Modifier.weight(1f)
        )
        TotalItem(
            title = stringResource(R.string.label_total_active_contracts_count),
            value = ui.totalSerial.toString(),
            modifier = Modifier.weight(1f)
        )
        TotalItem(
            title = stringResource(R.string.label_total_inactive_contracts_count),
            value = ui.totalIntro.toString(),
            modifier = Modifier.weight(1f)
        )
    }

}

@Composable
private fun TotalItem(title: String, value: String, modifier: Modifier = Modifier) {
    val appColors = LocalPartnerManagementColors.current
    Column(
        modifier = modifier
            .background(appColors.cardBackground, RoundedCornerShape(12.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(title, style = MaterialTheme.typography.labelSmall, color = appColors.textSecondary)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun TableCellHeader(
    text: String,
    modifier: Modifier = Modifier,
    center: Boolean = false
) {
    val appColors = LocalPartnerManagementColors.current
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodySmall,
        color = appColors.textPrimary,
        textAlign = if (center) TextAlign.Center else TextAlign.Start,
        maxLines = 2,
        softWrap = true
    )
}

@Composable
fun TableCell(
    text: String,
    modifier: Modifier = Modifier,
    center: Boolean = false
) {
    val appColors = LocalPartnerManagementColors.current
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodySmall,
        color = appColors.textPrimary,
        textAlign = if (center) TextAlign.Center else TextAlign.Start,
        maxLines = 1,
        softWrap = false
    )
}


fun demoReportDashboardUi(): ReportDashboardUi {
    val rows = listOf(
        ReportRowUi(
            id = "1",
            collectionName = "هتل اسپیناس پالاس",
            contractDate = "1405/02/25",
            cooperate = "پورسانت و تخفیف",
            status = 1
        ),
        ReportRowUi(
            id = "2",
            collectionName = "هتل پارسیان آزادی",
            contractDate = "1405/02/25",
            cooperate = "پورسانتی",
            status = 1
        ),
        ReportRowUi(
            id = "3",
            collectionName = "هتل هما شیراز",
            contractDate = "1405/02/25",
            cooperate = "پورسانت و تخفیف",
            status =1
        ),
        ReportRowUi(
            id = "4",
            collectionName = "هتل بزرگ تهران",
            contractDate = "1405/02/25",
            cooperate = "بلیط تخفیف دار",
            status = 2
        ),
        ReportRowUi(
            id = "5",
            collectionName = "هتل پردیس کیش",
            contractDate = "1405/02/25",
            cooperate = "پورسانتی",
            status = 2
        ),
        ReportRowUi(
            id = "6",
            collectionName = "هتل آسمان اصفهان",
            contractDate = "1405/02/25",
            cooperate = "نقدی",
            status =2
        ),
        ReportRowUi(
            id = "7",
            collectionName = "هتل المپیک تهران",
            contractDate = "1405/02/25",
            cooperate = "بلیط تخفیف دار",
            status =1
        ),
        ReportRowUi(
            id = "8",
            collectionName = "هتل آرامیس مشهد",
            contractDate = "1405/02/25",
            cooperate = "بلیط تخفیف دار",
            status = 2
        )
    )

    return ReportDashboardUi(
        rows = rows,
        totalIntro = 532,
        totalSerial = 1064,
        totalManual = 374
    )
}
