package ir.kitgroup.partnerManagement.feature.report.ui.visitor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.VisitorEntity
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.EmptyState
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors

@Composable
fun VisitorListScreen(
    onBackClick: () -> Unit,
    onReportItemClick: () -> Unit,
    onViewItemDetailsClick: (VisitorEntity) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VisitorListViewModel = hiltViewModel()
) {
    val visitors by viewModel.visitors.collectAsState()
    val appColors = LocalPartnerManagementColors.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CustomHeader(
                title = stringResource(R.string.label_visitor_list),
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
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
          /*      CustomButton(
                    text = stringResource(R.string.label_visitor_performance_report),
                    onClick = onReportItemClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = appColors.success,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                )

                Spacer(modifier = Modifier.height(16.dp))
*/
                if (visitors.isEmpty()) {
                    EmptyState(
                        textRes = R.string.msg_no_item_found,
                        modifier = Modifier.fillMaxSize()
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
    visitors: List<VisitorEntity>,
    onViewPerformanceClick: (VisitorEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(
            items = visitors,
            key = { it.visitorId }
        ) { visitor ->
            VisitorCard(
                visitor = visitor,
                onViewPerformanceClick = onViewPerformanceClick
            )
        }
    }
}

@Composable
fun VisitorCard(
    visitor: VisitorEntity,
    onViewPerformanceClick: (VisitorEntity) -> Unit,
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
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ردیف نام و برچسب نقش/نوع فعالیت
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = visitor.name.orEmpty(),
                    style = typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = visitor.workTypeName.orEmpty(),
                        style = typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                thickness = 0.5.dp
            )

            // ردیف اطلاعات تکمیلی: کد و شماره موبایل
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                VisitorInfoItem(
                    icon = Icons.Default.Badge,
                    label = "کد: ${visitor.code}"
                )

                VisitorInfoItem(
                    icon = Icons.Default.Call,
                    label = visitor.mobile.orEmpty()
                )
            }
        }
    }
}

@Composable
fun VisitorInfoItem(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = label,
            style = typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
