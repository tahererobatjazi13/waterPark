// src/main/java/ir/kitgroup/partnerManagement/feature/report/ui/visitor/VisitorAnalysisScreen.kt
package ir.kitgroup.partnerManagement.feature.report.ui.visitor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.feature.report.model.VisitorAnalysis

@Composable
fun VisitorAnalysisScreen(
    visitorId: String,
    onBackClick: () -> Unit,
    onCallClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    analysisData: VisitorAnalysis = getDemoAnalysis(visitorId)
) {
    val appColors = LocalPartnerManagementColors.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.primary,
        topBar = {
            CustomHeader(
                title = R.string.label_visitor_performance_analysis,
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = analysisData.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = appColors.textPrimary
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            Button(
                                onClick = { onCallClick(analysisData.phoneNumber) },
                                shape = RoundedCornerShape(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = appColors.infoContainer,
                                    contentColor = appColors.onInfoContainer
                                ),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = stringResource(R.string.label_call),
                                    style = MaterialTheme.typography.labelLarge,
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${stringResource(R.string.label_visitor_code)} ${analysisData.code}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = appColors.textSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "${stringResource(R.string.label_region)}: ${analysisData.region}",
                                style = MaterialTheme.typography.labelMedium,
                                color = appColors.onInfoContainer,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // گرید دو در دو کارت‌های تحلیل عملکرد با رنگ‌بندی متکی بر تم
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AnalysisCard(
                            title = stringResource(R.string.label_total_visits),
                            value = analysisData.totalVisits.toString(),
                            icon = Icons.Default.PieChart,
                            modifier = Modifier.weight(1f),
                            iconBgColor = appColors.iconBlueContainer,
                            iconTintColor = appColors.onInfoContainer
                        )
                        AnalysisCard(
                            title = stringResource(R.string.label_average_score),
                            value = analysisData.averageScore.toString(),
                            icon = Icons.Default.StarOutline,
                            subtitle = stringResource(R.string.label_out_of_five),
                            modifier = Modifier.weight(1f),
                            iconBgColor = appColors.iconBlueContainer,
                            iconTintColor = appColors.onInfoContainer
                        )
                        AnalysisCard(
                            title = stringResource(R.string.label_assigned_cards),
                            value = analysisData.assignedCards.toString(),
                            icon = Icons.Default.Business,
                            modifier = Modifier.weight(1f),
                            iconBgColor = appColors.iconBlueContainer,
                            iconTintColor = appColors.onInfoContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnalysisCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    iconBgColor: Color,
    iconTintColor: Color
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColors.cardBackground
        ),
        border = BorderStroke(
            width = 0.8.dp,
            color = appColors.border
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(iconBgColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTintColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = appColors.textSecondary,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    color = appColors.onInfoContainer
                )

                if (subtitle != null) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelMedium,
                        color = appColors.textTertiary
                    )
                }
            }
        }
    }
}

private fun getDemoAnalysis(visitorId: String): VisitorAnalysis {
    return when (visitorId) {
        "2" -> VisitorAnalysis(
            id = "2",
            name = "سارا محمدی",
            code = "۱۲۴۴۵",
            region = "تهران - شمال",
            phoneNumber = "09120000000",
            totalVisits = 148,
            successConversionCount = 52,
            successConversionPercent = 37.1,
            averageScore = 4.3,
            assignedCards = 96
        )

        "1" -> VisitorAnalysis(
            id = "1",
            name = "امیر حسین رضایی",
            code = "۱۲۴۴۱",
            region = "تهران - مرکز",
            phoneNumber = "09121111111",
            totalVisits = 180,
            successConversionCount = 75,
            successConversionPercent = 41.6,
            averageScore = 4.5,
            assignedCards = 110
        )

        else -> VisitorAnalysis(
            id = visitorId,
            name = "علی جعفری",
            code = "۱۲۴۴۲",
            region = "تهران - غرب",
            phoneNumber = "09122222222",
            totalVisits = 210,
            successConversionCount = 90,
            successConversionPercent = 42.8,
            averageScore = 4.2,
            assignedCards = 120
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun VisitorAnalysisScreenPreview() {
    AppScreenPreview {
        VisitorAnalysisScreen(
            visitorId = "2",
            onBackClick = {},
            onCallClick = {}
        )
    }
}
