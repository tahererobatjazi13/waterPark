package ir.kitgroup.partnerManagement.feature.report.ui.organization

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.DateRangeChip
import ir.kitgroup.partnerManagement.core.ui.components.JalaliRangeCalendar
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.theme.PartnerManagementTheme
import ir.kitgroup.partnerManagement.feature.report.ui.organization.model.CollectionPerformanceUi

@Composable
fun CollectionPerformanceScreen(
    onBackClick: () -> Unit
) {
    val collections = listOf(
        CollectionPerformanceUi("1", "هتل هما ۱", 24, 5),
        CollectionPerformanceUi("2", "هتل الماس", 16, 4),
        CollectionPerformanceUi("3", "هتل قصر", 9, 4),
        CollectionPerformanceUi("4", "هتل تارا", 6, 3),
    )
    val appColors = LocalPartnerManagementColors.current

    var selectedTab by remember { mutableIntStateOf(0) }
    var showDateRangeDialog by remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf("1403/02/01") }
    var endDate by remember { mutableStateOf("1403/02/31") }

    Scaffold(
        topBar = {
            CustomHeader(
                title = R.string.label_organizations_report,
                showBackButton = true,
                onBackClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            val tabs = listOf(
                stringResource(R.string.label_all),
                stringResource(R.string.label_best_selling),
                stringResource(R.string.label_low_selling)
            )
            CustomSegmentedTabs(
                items = tabs,
                selectedIndex = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            DateRangeChip(
                startDate = startDate,
                endDate = endDate,
                onClick = {
                    showDateRangeDialog = true
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            CollectionListTable(collections)

            Spacer(modifier = Modifier.height(24.dp))

            CollectionComparisonChart(collections)
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
                        style = typography.titleMedium,
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
fun CustomSegmentedTabs(items: List<String>, selectedIndex: Int, onTabSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(4.dp)
    ) {
        items.forEachIndexed { index, text ->
            val isSelected = selectedIndex == index
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        color = if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent
                    )
                    .clickable { onTabSelected(index) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    style = typography.labelMedium.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                )
            }
        }
    }
}

@Composable
fun CollectionListTable(collections: List<CollectionPerformanceUi>) {
    val appColors = LocalPartnerManagementColors.current

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp)
        ) {
            Text(
                text = stringResource(R.string.label_name),
                modifier = Modifier.weight(1f),
                style = typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = stringResource(R.string.label_consumed_offer_cards_count),
                style = typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        HorizontalDivider(color = appColors.border.copy(alpha = 0.4f))

        collections.forEach { collection ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .background(
                                color = PartnerManagementTheme.colors.infoContainer,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "★",
                            color = PartnerManagementTheme.colors.ratingActive,
                            style = typography.bodySmall
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = collection.starRating.toString(),
                            color = PartnerManagementTheme.colors.ratingActive,
                            style = typography.labelSmall,
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = collection.name,
                        style = typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Text(
                    text = collection.usageCount.toString(),
                    style = typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            HorizontalDivider(color = appColors.border.copy(alpha = 0.2f))
        }
    }
}

@Composable
fun CollectionComparisonChart(collections: List<CollectionPerformanceUi>) {
    val maxUsage = collections.maxOfOrNull { it.usageCount }?.toFloat() ?: 1f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.label_consumption_comparison),
                style = typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            collections.forEach { collection ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 6.dp)
                ) {
                    Text(
                        text = collection.name,
                        modifier = Modifier.width(90.dp),
                        style = typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    LinearProgressIndicator(
                        progress = { collection.usageCount / maxUsage },
                        modifier = Modifier
                            .weight(1f)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = collection.usageCount.toString(),
                        style = typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
