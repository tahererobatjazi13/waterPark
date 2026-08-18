package ir.kitgroup.partnerManagement.feature.report.ui.visitor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.feature.report.model.Visitor


private val demoVisitors = listOf(
    Visitor(
        id = "1",
        name = "امیر حسین رضایی",
        collectionVisited = 5,
        cardsGiven = 20
    ),
    Visitor(
        id = "2",
        name = "سارا محمدی",
        collectionVisited = 3,
        cardsGiven = 15
    ),
    Visitor(
        id = "3",
        name = "علی جعفری",
        collectionVisited = 7,
        cardsGiven = 25
    ),
    Visitor(
        id = "4",
        name = "نگین حیدری",
        collectionVisited = 4,
        cardsGiven = 18
    )
)


@Composable
fun VisitorListScreen(
    onBackClick: () -> Unit,
    onReportItemClick: () -> Unit,
    onViewItemDetailsClick: (Visitor) -> Unit,
    modifier: Modifier = Modifier,
    visitors: List<Visitor> = demoVisitors
) {
    val appColors = LocalPartnerManagementColors.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            CustomHeader(
                title = R.string.label_visitor_list,
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
                    .padding(16.dp)
            ) {
                CustomButton(
                    text = stringResource(R.string.label_visitor_performance_report),
                    onClick = onReportItemClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = appColors.success,
                        contentColor =MaterialTheme.colorScheme.onPrimary
                    ),
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (visitors.isEmpty()) {
                    VisitorEmptyState(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                } else {
                    VisitorsList(
                        visitors = visitors,
                        onViewPerformanceClick = onViewItemDetailsClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun VisitorsList(
    visitors: List<Visitor>,
    onViewPerformanceClick: (Visitor) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(
            items = visitors,
            key = { it.id }
        ) { visitors ->
            VisitorCard(
                visitor = visitors,
                onViewPerformanceClick = onViewPerformanceClick
            )

        }
    }
}

@Composable
fun VisitorCard(
    visitor: Visitor,
    onViewPerformanceClick: (Visitor) -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onViewPerformanceClick(visitor) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = LocalPartnerManagementColors.current.cardBackground
        ),
        border = BorderStroke(
            width = 0.7.dp,
            color = LocalPartnerManagementColors.current.border
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = visitor.name,
                    style = typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.label_today_performance),
                    style = typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )

                VisitorStatItem(
                    count = visitor.collectionVisited,
                    label = stringResource(R.string.label_visited_organization),
                    icon = Icons.Default.Business,
                    modifier = Modifier.weight(1f)
                )

                VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)

                VisitorStatItem(
                    count = visitor.cardsGiven,
                    label = stringResource(R.string.label_card_given),
                    icon = Icons.Default.CreditCard,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun VerticalDivider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.outlineVariant,
    thickness: Dp = 1.dp
) {
    Spacer(
        modifier = modifier
            .fillMaxHeight()
            .width(thickness)
            .background(color)
    )
}

@Composable
fun VisitorStatItem(
    count: Int,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.height(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = count.toString(),
                style = typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
        }
        Text(
            text = label,
            style = typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Composable
private fun VisitorEmptyState(
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Inventory2,
                contentDescription = null,
                tint = appColors.border,
                modifier = Modifier.size(56.dp)
            )
            Text(
                text = stringResource(R.string.msg_no_item_found),
                style = typography.titleMedium,
                color = appColors.textSecondary
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun VisitorListScreenPreview() {
    AppScreenPreview {
        VisitorListScreen(
            onBackClick = {},
            onReportItemClick = {},
            onViewItemDetailsClick = {}
        )
    }
}
