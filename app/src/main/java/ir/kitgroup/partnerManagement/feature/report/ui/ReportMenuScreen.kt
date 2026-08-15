package ir.kitgroup.partnerManagement.feature.report.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Assessment       // بهینه‌سازی شده برای گزارش عملکرد
import androidx.compose.material.icons.filled.Description      // بهینه‌سازی شده برای قراردادها
import androidx.compose.material.icons.filled.People           // بهینه‌سازی شده برای ویزیتورها/افراد
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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

@Composable
fun ReportMenuScreen(
    onBackClick: () -> Unit,
    onVisitorClick: () -> Unit,
    onReportContractClick: () -> Unit,
    onCollectionPerformanceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CustomHeader(
                title = R.string.label_reports,
                showBackButton = true,
                onBackClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.primary
    ) { innerPadding ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding()
                ),
            shape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp
            ),
            color = MaterialTheme.colorScheme.background
        ) {
            AdvertisingMenuContent(
                onVisitorClick = onVisitorClick,
                onReportContractClick = onReportContractClick,
                onCollectionPerformanceClick = onCollectionPerformanceClick,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun AdvertisingMenuContent(
    onVisitorClick: () -> Unit,
    onReportContractClick: () -> Unit,
    onCollectionPerformanceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = stringResource(R.string.msg_select_one_of_options),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        item {
            AdvertisingMenuCard(
                titleRes = R.string.label_visitor_list,
                subtitleRes = R.string.label_report_view_visitor,
                icon = Icons.Filled.People, // آیکون افراد به جای جعبه انبار
                iconContainerColor = MaterialTheme.colorScheme.primaryContainer,
                iconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                onClick = onVisitorClick
            )
        }

        item {
            AdvertisingMenuCard(
                titleRes = R.string.label_cooperation_agreement,
                subtitleRes = R.string.label_report_cooperation_agreement,
                icon = Icons.Filled.Description, // آیکون سند/توضیحات به جای ثبت‌نام
                iconContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                iconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                onClick = onReportContractClick
            )
        }

        item {
            AdvertisingMenuCard(
                titleRes = R.string.label_collaborative_collections_report,
                subtitleRes = R.string.label_collections_report_low_high_selling,
                icon = Icons.Filled.Assessment, // آیکون آنالیز و نمودار عملکرد به جای ساختمان
                iconContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                iconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                onClick = onCollectionPerformanceClick
            )
        }
    }
}

@Composable
private fun AdvertisingMenuCard(
    @StringRes titleRes: Int,
    @StringRes subtitleRes: Int,
    icon: ImageVector,
    iconContainerColor: Color,
    iconColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface
        ),
        border = BorderStroke(
            width = 0.7.dp,
            color = colorScheme.outline.copy(alpha = 0.15f) // اصلاح شفافیت بوردر برای ظاهر تمیزتر
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(52.dp),
                shape = RoundedCornerShape(14.dp),
                color = iconContainerColor,
                contentColor = iconColor
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(26.dp) // سایز بهینه آیکون داخلی
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(titleRes),
                    style = MaterialTheme.typography.titleMedium,
                    color = colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(subtitleRes),
                    style = MaterialTheme.typography.bodySmall,
                    color = colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun ReportMenuScreenPreview() {
    AppScreenPreview {
        ReportMenuScreen(
            onBackClick = {},
            onVisitorClick = {},
            onReportContractClick = {},
            onCollectionPerformanceClick = {}
        )
    }
}
