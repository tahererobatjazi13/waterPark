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
import ir.kitgroup.partnerManagement.core.database.entity.OfferTicketPlanEntity
import ir.kitgroup.partnerManagement.core.database.entity.OfferTicketPlanLineEntity
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.CommissionType
import ir.kitgroup.partnerManagement.core.ui.util.DiscountType
import ir.kitgroup.partnerManagement.core.ui.util.OfferPlanLineStatus
import ir.kitgroup.partnerManagement.core.ui.util.OfferPlanStatus
import ir.kitgroup.partnerManagement.core.ui.util.demoOfferTicketPlanLines
import ir.kitgroup.partnerManagement.core.ui.util.demoOfferTicketPlans

@Composable
fun OfferTicketPlanLineListScreen(
    headerOfferTicketPlan: OfferTicketPlanEntity?,
    offerTicketPlanLines: List<OfferTicketPlanLineEntity> = emptyList(),
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
    onOfferTicketPlanLineClick: (OfferTicketPlanLineEntity) -> Unit
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
                if (headerOfferTicketPlan != null) {
                    item {
                        PlanHeaderSummarySection(plan = headerOfferTicketPlan)
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
                    items = offerTicketPlanLines,
                    key = { _, plan -> plan.offerTicketPlanLineId }
                ) { index, plan ->
                    OfferPlanLineListItem(
                        rowIndex = index + 1,
                        plan = plan,
                        onClick = { onOfferTicketPlanLineClick(plan) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PlanHeaderSummarySection(
    plan: OfferTicketPlanEntity,
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
                text = plan.name.orEmpty(),
                style = typography.titleLarge,
                color = appColors.textPrimary,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            StatusBadge(status = OfferPlanStatus.fromId(plan.status))
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
                text = plan.code.orEmpty(),
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
                    text = plan.validFromDate.orEmpty(),
                    style = typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "تا",
                    style = typography.labelLarge,
                    color = appColors.textTertiary
                )
                Text(
                    text = plan.validToDate.orEmpty(),
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
    plan: OfferTicketPlanLineEntity,
    onClick: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current

    // نگاشت مقادیر عددی Entity به Enumهای مربوطه
    val commissionTypeEnum = CommissionType.fromId(plan.commissionType)
    val discountTypeEnum = DiscountType.fromId(plan.discountType)
    val lineStatusEnum = OfferPlanLineStatus.fromId(plan.status)

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
                            text = plan.name.takeIf { !it.isNull_or_blank() } ?: plan.offerTicketPlanLineId,
                            style = typography.titleLarge,
                            color = appColors.textPrimary
                        )
                    }
                }

                // بج وضعیت خط آفر بر اساس Enum درست
                StatusBadge(status = lineStatusEnum)

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
                        text = plan.productServiceId.orEmpty(),
                        style = typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // نوع پورسانت (نمایش عنوان از stringResource بر اساس Enum)
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
                        text = stringResource(commissionTypeEnum.titleRes),
                        style = typography.labelLarge,
                        color = appColors.textPrimary
                    )
                }

                // نوع تخفیف (نمایش عنوان از stringResource بر اساس Enum)
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
                        text = stringResource(discountTypeEnum.titleRes),
                        style = typography.labelLarge,
                        color = appColors.textPrimary
                    )
                }
            }
        }
    }
}

// تابع کمکی کوچک برای بررسی Null یا خالی بودن رشته‌ها
private fun String?.isNull_or_blank(): Boolean = this == null || this.isBlank()

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun OfferTicketPlanLineListScreenPreview() {
    AppScreenPreview {
        OfferTicketPlanLineListScreen(
            headerOfferTicketPlan = demoOfferTicketPlans().firstOrNull(),
            offerTicketPlanLines = demoOfferTicketPlanLines(),
            onBackClick = {},
            onAddClick = {},
            onOfferTicketPlanLineClick = {}
        )
    }
}
