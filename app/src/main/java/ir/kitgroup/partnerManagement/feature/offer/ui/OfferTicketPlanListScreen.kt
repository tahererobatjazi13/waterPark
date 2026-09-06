package ir.kitgroup.partnerManagement.feature.offer.ui

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import ir.kitgroup.partnerManagement.core.ui.SessionViewModel
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.Status
import ir.kitgroup.partnerManagement.core.ui.util.UserRole
import ir.kitgroup.partnerManagement.feature.offer.model.OfferPlanUi


@Composable
fun OfferTicketPlanListScreen(
    onAddClick: () -> Unit,
    onPlanClick: (OfferPlanUi) -> Unit,
    plans: List<OfferPlanUi> = demoOfferPlans(),
    viewModel: SessionViewModel = hiltViewModel()
) {
    val appColors = LocalPartnerManagementColors.current
    val role by viewModel.userRole.collectAsState()
    val isSupervisor = role == UserRole.SUPERVISOR.name

    Scaffold(
        topBar = {
            CustomHeader(
                title = R.string.label_offer_ticket_plans,
                showBackButton = false
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
                if (isSupervisor) {
                    OfferPlansHeader(onAddClick = onAddClick)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = plans,
                        key = { plan -> plan.id }
                    ) { plan ->
                        OfferPlanListItem(
                            plan = plan,
                            onClick = { onPlanClick(plan) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OfferPlanListItem(
    plan: OfferPlanUi,
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
                    text = plan.planName,
                    style = typography.titleLarge,
                    color = appColors.textPrimary,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                StatusBadge(status = plan.status)

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
                    text = plan.planCode,
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
                startDate = plan.startDate,
                endDate = plan.endDate
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


@Composable
private fun OfferPlansHeader(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.label_create_plan_offer),
            style = typography.titleLarge,
            color = appColors.textPrimary
        )

        CustomButton(
            text = stringResource(R.string.label_new_plan),
            onClick = onAddClick,
            fillMaxWidth = false,
            height = 38.dp,
            textStyle = typography.titleLarge,
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            icon = Icons.Default.Add,
            colors = ButtonDefaults.buttonColors(
                containerColor = appColors.success,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}

fun demoOfferPlans(): List<OfferPlanUi> = listOf(
    OfferPlanUi(
        id = "1",
        planCode = "OFF-101",
        planName = "طرح تخفیف اقامت نوروزی",
        startDate = "1405/01/01",
        endDate = "1405/01/15",
        status = Status.ACTIVE
    ),
    OfferPlanUi(
        id = "2",
        planCode = "OFF-102",
        planName = "طرح بلیط تخفیف فصلی بهار",
        startDate = "1405/01/16",
        endDate = "1405/03/31",
        status = Status.ACTIVE
    ),
    OfferPlanUi(
        id = "3",
        planCode = "OFF-103",
        planName = "طرح آفر آخرهفته تابستانه",
        startDate = "1405/04/01",
        endDate = "1405/06/31",
        status = Status.CLOSED
    ),
    OfferPlanUi(
        id = "4",
        planCode = "OFF-104",
        planName = "طرح تشویقی وفاداری مشتریان",
        startDate = "1404/07/01",
        endDate = "1404/12/29",
        status = Status.DRAFT
    )
)

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun OfferTicketPlanListScreenPreview() {
    AppScreenPreview {
        OfferTicketPlanListScreen(
            onAddClick = {},
            onPlanClick = {}
        )
    }
}