package ir.kitgroup.partnerManagement.feature.offer.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.Status
import ir.kitgroup.partnerManagement.feature.offer.model.OfferPlanLineUi
import ir.kitgroup.partnerManagement.feature.offer.model.OfferPlanUi

@Composable
fun OfferTicketPlanLineListScreen(
    headerPlan: OfferPlanUi?,
    plans: List<OfferPlanLineUi> = emptyList(),
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
    onPlanClick: (OfferPlanLineUi) -> Unit
) {
    val appColors = LocalPartnerManagementColors.current

    Scaffold(
        topBar = {
            CustomHeader(
                title = R.string.label_offer_details,
                showBackButton = true,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            Surface(
                color = appColors.cardBackground,
                shadowElevation = 8.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    CustomButton(
                        text = stringResource(R.string.label_new_plan_detail),
                        onClick = onAddClick,
                        fillMaxWidth = true,
                        height = 48.dp,
                        textStyle = typography.titleLarge,
                        icon = Icons.Default.Add,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = appColors.success,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            }
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(
                    top = 16.dp,
                    bottom = innerPadding.calculateBottomPadding() + 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // هدر طرح انتخاب‌شده
                if (headerPlan != null) {
                    item {
                        PlanHeaderSummarySection(plan = headerPlan)
                        Spacer(modifier = Modifier.height(10.dp))

                        // بخش عنوان لیست همراه با شمارنده
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.label_offer_detail_list),
                                style = typography.labelLarge,
                                color = appColors.textPrimary
                            )
                        }
                    }
                }

                // استفاده از itemsIndexed برای محاسبه شماره ردیف
                itemsIndexed(
                    items = plans,
                    key = { _, plan -> plan.id }
                ) { index, plan ->
                    OfferPlanLineListItem(
                        rowIndex = index + 1,
                        plan = plan,
                        onClick = { onPlanClick(plan) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PlanHeaderSummarySection(
    plan: OfferPlanUi,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // نام طرح و وضعیت
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = plan.planName,
                style = typography.titleLarge,
                color = appColors.textPrimary,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            StatusBadge(status = plan.status)
        }

        // کد طرح
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

        Spacer(modifier = Modifier.height(2.dp))

        // بازه اعتبار طرح
        Row(
            modifier = Modifier.fillMaxWidth(),
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
                    text = stringResource(R.string.label_validity_period) + ":",
                    style = typography.labelMedium,
                    color = appColors.textSecondary
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = plan.startDate,
                    style = typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "تا",
                    style = typography.labelLarge,
                    color = appColors.textTertiary
                )
                Text(
                    text = plan.endDate,
                    style = typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = 6.dp),
            color = appColors.border.copy(alpha = 0.5f),
            thickness = 0.8.dp
        )
    }
}

@Composable
private fun OfferPlanLineListItem(
    rowIndex: Int,
    plan: OfferPlanLineUi,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // شماره ردیف و تایتل
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // بج دایره‌ای شماره ردیف
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$rowIndex",
                            style = typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = plan.planHeader,
                            style = typography.titleLarge,
                            color = appColors.textPrimary
                        )

                        Text(
                            text = plan.name,
                            style = typography.bodySmall,
                            color = appColors.textSecondary
                        )
                    }
                }

                StatusBadge(status = plan.status)

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = appColors.textTertiary,
                    modifier = Modifier.size(20.dp)
                )
            }

            HorizontalDivider(
                color = appColors.border.copy(alpha = 0.5f),
                thickness = 0.6.dp
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // کالا یا خدمت مرتبط
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = stringResource(R.string.label_related_product_or_service) + ":",
                        style = typography.bodySmall,
                        color = appColors.textSecondary
                    )
                    Text(
                        text = plan.relatedProductOrService,
                        style = typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // نوع پورسانت
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.label_commission_type) + ":",
                        style = typography.bodySmall,
                        color = appColors.textSecondary
                    )
                    Text(
                        text = plan.commissionType,
                        style = typography.labelLarge,
                        color = appColors.textPrimary
                    )
                }

                // نوع تخفیف
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.label_discount_type) + ":",
                        style = typography.bodySmall,
                        color = appColors.textSecondary
                    )
                    Text(
                        text = plan.discountType,
                        style = typography.labelLarge,
                        color = appColors.textPrimary
                    )
                }
            }

        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun OfferTicketPlanLineListScreenPreview() {
    AppScreenPreview {
        OfferTicketPlanLineListScreen(
            headerPlan = demoOfferPlans().first(),
            plans = demoOfferLinePlans(),
            onBackClick = {},
            onAddClick = {},
            onPlanClick = {}
        )
    }
}


fun demoOfferLinePlans(): List<OfferPlanLineUi> = listOf(
    OfferPlanLineUi(
        id = "1",
        planHeader = "فروش پاییزه",
        relatedProductOrService = "بلیط موج‌های آبی خردسال",
        ageCategory = "کودک",
        gender = "نیاز نیست",
        name = "آفر بلیط بزرگسال",
        commissionType = "درصدی",
        commissionPercent = 5.0,
        commissionAmount = null,
        discountType = "بدون تخفیف",
        discountPercent = null,
        discountAmount = null,
        status = Status.ACTIVE
    ),
    OfferPlanLineUi(
        id = "2",
        planHeader = "فروش زمستانه",
        relatedProductOrService = "بلیط پارک آبی بزرگسال",
        ageCategory = "بزرگسال",
        gender = "آقا و خانم",
        name = "آفر بلیط خردسال",
        commissionType = "مبلغ ثابت",
        commissionPercent = null,
        commissionAmount = 150_000,
        discountType = "درصدی",
        discountPercent = 10.0,
        discountAmount = null,
        status = Status.ACTIVE
    ),
    OfferPlanLineUi(
        id = "3",
        planHeader = "طرح ویژه خانواده",
        relatedProductOrService = "پکیج خانوادگی مجموعه تفریحی",
        ageCategory = "همه رده‌های سنی",
        gender = "نیاز نیست",
        name = "بلیط",
        commissionType = "بدون پورسانت",
        commissionPercent = null,
        commissionAmount = null,
        discountType = "مبلغ ثابت",
        discountPercent = null,
        discountAmount = 300_000,
        status = Status.CANCELLED
    )
)
