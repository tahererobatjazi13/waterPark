package ir.kitgroup.partnerManagement.feature.offer.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.OfferTicketPlanLineEntity
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.CommissionType
import ir.kitgroup.partnerManagement.core.ui.util.DiscountType
import ir.kitgroup.partnerManagement.core.ui.util.GenderType
import ir.kitgroup.partnerManagement.core.ui.util.OfferPlanLineStatus
import ir.kitgroup.partnerManagement.core.ui.util.PersonCategory
import java.text.NumberFormat
import java.util.Locale

@Composable
fun OfferTicketPlanLineDetailScreen(
    plan: OfferTicketPlanLineEntity,
    onBackClick: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current

    Scaffold(
        topBar = {
            CustomHeader(
                title = R.string.label_offer_product_service,
                showBackButton = true,
                onBackClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.primary
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            shape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp
            ),
            color = appColors.screenBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // بخش اطلاعات اصلی طرح
                OfferDetailSection(
                    title = stringResource(R.string.label_offer_main_information)
                ) {
                    OfferDetailRow(
                        label = stringResource(R.string.label_name),
                        value = plan.name.toDisplayValue()
                    )

                    OfferDetailRow(
                        label = stringResource(R.string.label_related_product_or_service),
                        value = plan.productServiceId.toDisplayValue()
                    )

                    OfferDetailRow(
                        label = stringResource(R.string.label_age_category),
                        value = stringResource(PersonCategory.fromId(plan.personCategory).titleRes)
                    )

                    OfferDetailRow(
                        label = stringResource(R.string.label_gender),
                        value = stringResource(GenderType.fromId(plan.gender).titleRes),
                        showDivider = false
                    )

                    HorizontalDivider(
                        color = appColors.border.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    // نمایش وضعیت
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.label_status),
                            style = typography.bodyMedium,
                            color = appColors.textSecondary
                        )
                        StatusBadge(status = OfferPlanLineStatus.fromId(plan.status))
                    }
                }

                // بخش اطلاعات کمیسیون
                OfferDetailSection(
                    title = stringResource(R.string.label_offer_commission_information)
                ) {
                    OfferDetailRow(
                        label = stringResource(R.string.label_commission_type),
                        value = stringResource(CommissionType.fromId(plan.commissionType).titleRes)
                    )

                    OfferDetailRow(
                        label = stringResource(R.string.label_commission_percent),
                        value = plan.commissionPercent.toPercentDisplay()
                    )

                    OfferDetailRow(
                        label = stringResource(R.string.label_commission_amount),
                        value = plan.commissionAmount.toAmountDisplay(),
                        showDivider = false
                    )
                }

                // بخش اطلاعات تخفیف
                OfferDetailSection(
                    title = stringResource(R.string.label_offer_discount_information)
                ) {
                    OfferDetailRow(
                        label = stringResource(R.string.label_discount_type),
                        value = stringResource(DiscountType.fromId(plan.discountType).titleRes)
                    )

                    OfferDetailRow(
                        label = stringResource(R.string.label_discount_percent),
                        value = plan.discountPercent.toPercentDisplay()
                    )

                    OfferDetailRow(
                        label = stringResource(R.string.label_discount_amount),
                        value = plan.discountAmount.toAmountDisplay(),
                        showDivider = false
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun OfferDetailSection(
    title: String,
    content: @Composable () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current

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
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            content()
        }
    }
}

@Composable
private fun OfferDetailRow(
    label: String,
    value: String,
    showDivider: Boolean = true
) {
    val appColors = LocalPartnerManagementColors.current

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = typography.bodyMedium,
                color = appColors.textSecondary,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = value,
                style = typography.bodyMedium,
                color = appColors.textPrimary,
                modifier = Modifier.weight(1f)
            )
        }

        if (showDivider) {
            HorizontalDivider(
                color = appColors.border.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun String?.toDisplayValue(): String {
    return if (isNullOrBlank()) {
        stringResource(R.string.value_not_available)
    } else {
        this
    }
}

@Composable
private fun String?.toPercentDisplay(): String {
    if (isNullOrBlank()) {
        return stringResource(R.string.value_not_available)
    }

    val doubleValue = this.toDoubleOrNull()
    val formattedPercent = if (doubleValue != null) {
        if (doubleValue % 1.0 == 0.0) {
            doubleValue.toInt().toString()
        } else {
            doubleValue.toString()
        }
    } else {
        this
    }

    return stringResource(
        R.string.value_percent,
        formattedPercent
    )
}

@Composable
private fun String?.toAmountDisplay(): String {
    if (isNullOrBlank()) {
        return stringResource(R.string.value_not_available)
    }

    val longValue = this.replace(",", "").toDoubleOrNull()?.toLong()
    val formattedAmount = if (longValue != null) {
        NumberFormat.getNumberInstance(Locale.US).format(longValue)
    } else {
        this
    }

    return stringResource(
        R.string.value_amount_rial,
        formattedAmount
    )
}
