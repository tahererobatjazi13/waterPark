package ir.kitgroup.partnerManagement.feature.offer.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.material3.HorizontalDivider
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.OfferTicketPlanEntity
import ir.kitgroup.partnerManagement.core.ui.SessionViewModel
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.OfferPlanStatus
import ir.kitgroup.partnerManagement.core.ui.util.UserRole
import ir.kitgroup.partnerManagement.core.ui.util.demoOfferTicketPlans


@Composable
fun OfferTicketPlanListScreen(
    onAddClick: () -> Unit,
    onOfferTicketPlanClick: (OfferTicketPlanEntity) -> Unit,
    offerTicketPlans: List<OfferTicketPlanEntity> = demoOfferTicketPlans(),
    viewModel: SessionViewModel = hiltViewModel()
) {
    val appColors = LocalPartnerManagementColors.current
    val role by viewModel.userRole.collectAsState()
    val isSupervisor = role == UserRole.SUPERVISOR.name

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background, // رنگ زمینه منطبق با هدر
        floatingActionButton = {
            if (isSupervisor) {
                ExtendedFloatingActionButton(
                    onClick = onAddClick,
                    containerColor = LocalPartnerManagementColors.current.success,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(52.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.label_new_plan),
                            style = typography.titleLarge
                        )
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            // هدر بالای صفحه بدون پدینگ ناخواسته
            CustomHeader(
                title = R.string.label_offer_ticket_plans,
                showBackButton = false
            )

            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = appColors.screenBackground
            ) {
                // تمام پدینگ‌ها و فاصله FAB مستقیماً درون LazyColumn مدیریت می‌شوند
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = if (isSupervisor) {
                            paddingValues.calculateBottomPadding() + 16.dp
                        } else {
                            16.dp
                        }
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = offerTicketPlans,
                        key = { plan -> plan.offerTicketPlanId }
                    ) { plan ->
                        OfferPlanListItem(
                            plan = plan,
                            onClick = { onOfferTicketPlanClick(plan) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(60.dp))
                    }
                }
            }
        }
    }
}


@Composable
private fun OfferPlanListItem(
    plan: OfferTicketPlanEntity,
    onClick: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColors.cardBackground
        ),
        border = BorderStroke(
            width = 0.8.dp,
            color = appColors.border.copy(alpha = 0.8f)
        )
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // ردیف هدر: نام طرح + برچسب وضعیت + فلش ناوبری
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = plan.name!!,
                    style = typography.titleLarge,
                    color = appColors.textPrimary,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                StatusBadge(status = OfferPlanStatus.fromId(plan.status))

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = appColors.textTertiary,
                    modifier = Modifier.size(20.dp)
                )
            }

            // ردیف اطلاعاتی: کد طرح
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = stringResource(R.string.label_offer_code) + ":",
                    style = typography.bodySmall,
                    color = appColors.textSecondary
                )
                Text(
                    text = plan.code!!,
                    style = typography.labelMedium,
                    color = appColors.textPrimary
                )
            }

            HorizontalDivider(
                color = appColors.border.copy(alpha = 0.5f),
                thickness = 0.6.dp
            )

            // ردیف بازه زمانی
            PlanDateRangeDisplay(
                startDate = plan.validFromDate!!,
                endDate = plan.validToDate!!
            )
        }
    }
}

@Composable
private fun PlanDateRangeDisplay(
    startDate: String,
    endDate: String,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.DateRange,
                contentDescription = null,
                tint = appColors.textSecondary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = stringResource(R.string.label_validity_period),
                style = typography.labelMedium,
                color = appColors.textSecondary
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = startDate,
                style = typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "تا",
                style = typography.labelLarge,
                color = appColors.textTertiary
            )
            Text(
                text = endDate,
                style = typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun OfferTicketPlanListScreenPreview() {
    AppScreenPreview {
        OfferTicketPlanListScreen(
            onAddClick = {},
            onOfferTicketPlanClick = {}
        )
    }
}