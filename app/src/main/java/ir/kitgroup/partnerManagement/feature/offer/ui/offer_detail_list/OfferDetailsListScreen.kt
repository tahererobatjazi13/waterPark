package ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail_list

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
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.OfferEntity
import ir.kitgroup.partnerManagement.core.database.model.OfferDetailWithProduct
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.EmptyState
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.CommissionType
import ir.kitgroup.partnerManagement.core.ui.util.DiscountType
import ir.kitgroup.partnerManagement.core.ui.util.OfferPlanLineStatus
import ir.kitgroup.partnerManagement.core.ui.util.OfferPlanStatus
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate

@Composable
fun OfferDetailsListScreen(
    onBackClick: () -> Unit,
    onAddClick: (planTitle: String) -> Unit,
    onOfferTicketPlanLineClick: (OfferDetailWithProduct) -> Unit,
    viewModel: OfferDetailsListViewModel = hiltViewModel()
) {
    val headerOffer by viewModel.headerOffer.collectAsStateWithLifecycle()
    val offerLines by viewModel.offerLines.collectAsStateWithLifecycle()
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
                        onClick = {
                            val title = headerOffer?.title?.takeIf { it.isNotBlank() } ?: ""
                            onAddClick(title)
                        },
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
            val currentHeader = headerOffer

            if (currentHeader == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                if (offerLines.isEmpty()) {
                    EmptyState(
                        textRes = R.string.msg_no_offers_details_found,
                        icon = Icons.Outlined.Inbox,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                } else {
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
                        item {
                            PlanHeaderSummarySection(plan = currentHeader)
                            Spacer(modifier = Modifier.height(10.dp))

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
                        itemsIndexed(
                            items = offerLines,
                            key = { _, item -> item.offerDetail.offerDetailId }
                        ) { index, item ->
                            OfferPlanLineListItem(
                                rowIndex = index + 1,
                                item = item,
                                onClick = { onOfferTicketPlanLineClick(item) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PlanHeaderSummarySection(
    plan: OfferEntity,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = plan.title.orEmpty(),
                style = typography.titleLarge,
                color = appColors.textPrimary,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            StatusBadge(status = OfferPlanStatus.fromId(plan.statusOffer))
        }

        plan.code?.takeIf { it.isNotBlank() }?.let { code ->
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
                    text = code,
                    style = typography.labelMedium,
                    color = appColors.textPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

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
                    text = formatJalaliDate(plan.fromDate),
                    style = typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "تا",
                    style = typography.labelLarge,
                    color = appColors.textTertiary
                )
                Text(
                    text = formatJalaliDate(plan.toDate),
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
    item: OfferDetailWithProduct,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val plan = item.offerDetail
    val appColors = LocalPartnerManagementColors.current

    // بهره‌گیری از پراپرتی کپسوله‌شده در مدل و کش کردن مقادیر Enum
    val displayName = item.resolvedProductName
    val commissionTitle = remember(plan.commissionType) {
        CommissionType.fromId(plan.commissionType)
    }
    val discountTitle = remember(plan.discountType) {
        DiscountType.fromId(plan.discountType)
    }
    val lineStatusEnum = remember(plan.status) {
        OfferPlanLineStatus.fromId(plan.status)
    }

    Card(
        modifier = modifier
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
            // هدر آیتم: شماره ردیف، عنوان کالا/خدمت و نشان وضعیت
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // شماره ردیف
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

                // نام اصلی کالا در هدر
                Text(
                    text = displayName,
                    style = typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                // وضعیت
                StatusBadge(status = lineStatusEnum)

                // آیکون هدایت
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

            // جزئیات سطر
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                /*   OfferLineDetailRow(
                       label = stringResource(R.string.label_related_product_or_service),
                       value = displayName,
                       valueColor = MaterialTheme.colorScheme.primary
                   )*/

                OfferLineDetailRow(
                    label = stringResource(R.string.label_commission_type),
                    value = commissionTitle?.let { stringResource(it.titleRes) } ?: "-",
                    valueColor = appColors.textPrimary
                )

                OfferLineDetailRow(
                    label = stringResource(R.string.label_discount_type),
                    value = discountTitle?.let { stringResource(it.titleRes) } ?: "-",
                    valueColor = appColors.textPrimary
                )
            }
        }
    }
}

/**
 * کامپوننت کمکی جهت جلوگیری از تکرار سطرهای برچسب/مقدار
 */
@Composable
private fun OfferLineDetailRow(
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label:",
            style = typography.bodySmall,
            color = appColors.textSecondary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            style = typography.labelLarge,
            color = valueColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
