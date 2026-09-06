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
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.feature.offer.model.OfferPlanLineUi
import java.text.NumberFormat
import java.util.Locale

@Composable
fun OfferTicketPlanLineDetailScreen(
    plan: OfferPlanLineUi,
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
                    title = stringResource(
                        R.string.label_offer_main_information
                    )
                ) {

                    OfferDetailRow(
                        label = stringResource(
                            R.string.label_offer_plan_header
                        ),
                        value = plan.planHeader
                    )

                    OfferDetailRow(
                        label = stringResource(
                            R.string.label_related_product_or_service
                        ),
                        value = plan.relatedProductOrService
                    )

                    OfferDetailRow(
                        label = stringResource(
                            R.string.label_age_category
                        ),
                        value = plan.ageCategory.toDisplayValue()
                    )

                    OfferDetailRow(
                        label = stringResource(
                            R.string.label_gender
                        ),
                        value = plan.gender.toDisplayValue()
                    )

                    OfferDetailRow(
                        label = stringResource(
                            R.string.label_name
                        ),
                        value = plan.name,
                        showDivider = false
                    )
                    HorizontalDivider(
                        color = appColors.border.copy(alpha = 0.6f)
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

                        StatusBadge(status = plan.status)
                    }
                }

                // بخش اطلاعات کمیسیون
                OfferDetailSection(
                    title = stringResource(
                        R.string.label_offer_commission_information
                    )
                ) {
                    OfferDetailRow(
                        label = stringResource(
                            R.string.label_commission_type
                        ),
                        value = plan.commissionType
                    )

                    OfferDetailRow(
                        label = stringResource(
                            R.string.label_commission_percent
                        ),
                        value = plan.commissionPercent.toPercentDisplay()
                    )

                    OfferDetailRow(
                        label = stringResource(
                            R.string.label_commission_amount
                        ),
                        value = plan.commissionAmount.toAmountDisplay(),
                        showDivider = false
                    )
                }

                // بخش اطلاعات تخفیف
                OfferDetailSection(
                    title = stringResource(
                        R.string.label_offer_discount_information
                    )
                ) {
                    OfferDetailRow(
                        label = stringResource(
                            R.string.label_discount_type
                        ),
                        value = plan.discountType
                    )

                    OfferDetailRow(
                        label = stringResource(
                            R.string.label_discount_percent
                        ),
                        value = plan.discountPercent.toPercentDisplay()
                    )

                    OfferDetailRow(
                        label = stringResource(
                            R.string.label_discount_amount
                        ),
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
private fun Double?.toPercentDisplay(): String {
    if (this == null) {
        return stringResource(R.string.value_not_available)
    }

    val formattedPercent = if (this % 1.0 == 0.0) {
        toInt().toString()
    } else {
        toString()
    }

    return stringResource(
        R.string.value_percent,
        formattedPercent
    )
}

@Composable
private fun Long?.toAmountDisplay(): String {
    if (this == null) {
        return stringResource(R.string.value_not_available)
    }

    val formattedAmount = NumberFormat
        .getNumberInstance(Locale.US)
        .format(this)

    return stringResource(
        R.string.value_amount_rial,
        formattedAmount
    )
}
