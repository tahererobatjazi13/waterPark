package ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail_plan_list.list

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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.OfferEntity
import ir.kitgroup.partnerManagement.core.database.model.OfferDetailWithProduct
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.EmptyState
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.CommissionType
import ir.kitgroup.partnerManagement.core.ui.util.DiscountType
import ir.kitgroup.partnerManagement.core.ui.util.OfferDetailPlanStatus
import ir.kitgroup.partnerManagement.core.ui.util.OfferPlanStatus
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate

@Composable
fun OfferDetailPlanListScreen(
    onAddOfferDetailPlanClick: (planId: String, planTitle: String) -> Unit,
    onOfferDetailPlanClick: (OfferDetailWithProduct) -> Unit,
    viewModel: OfferDetailPlanListViewModel = hiltViewModel(),
) {
    val headerOffer by viewModel.headerOffer.collectAsStateWithLifecycle()
    val offerDetails by viewModel.offerDetails.collectAsStateWithLifecycle()
    val appColors = LocalPartnerManagementColors.current

    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowMessage -> snackBarHostState.showSnackbar(
                    event.message.asString(
                        context
                    )
                )

                is UiEvent.ShowError -> snackBarHostState.showSnackbar(event.error.asString(context))
            }
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
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
                            val plan = headerOffer
                            val planId = plan?.offerId.orEmpty()
                            val title = plan?.title?.takeIf { it.isNotBlank() }.orEmpty()

                            if (planId.isNotBlank()) {
                                onAddOfferDetailPlanClick(planId, title)
                            }
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
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            val currentHeader = headerOffer

            if (currentHeader == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(36.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(
                        top = 16.dp,
                        bottom = innerPadding.calculateBottomPadding() + 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // هدر طرح
                    item(key = "header_summary") {
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

                    // وضعیت‌های لیست جزئیات
                    when {
                        offerDetails == null -> {
                            item(key = "loading_state") {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 48.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(36.dp),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }

                        offerDetails!!.isEmpty() -> {
                            item(key = "empty_state") {
                                EmptyState(
                                    textRes = R.string.msg_no_offers_details_found,
                                    icon = Icons.Outlined.Inbox,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 48.dp)
                                )
                            }
                        }

                        else -> {
                            itemsIndexed(
                                items = offerDetails!!,
                                key = { _, item -> item.offerDetail.offerDetailId }
                            ) { index, item ->
                                OfferPlanLineListItem(
                                    rowIndex = index + 1,
                                    item = item,
                                    onClick = { onOfferDetailPlanClick(item) }
                                )
                            }
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
                    text = stringResource(R.string.label_to),
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

    val displayName = item.resolvedProductName
    val commissionTitle = remember(plan.commissionType) {
        CommissionType.fromId(plan.commissionType)
    }
    val discountTitle = remember(plan.discountType) {
        DiscountType.fromId(plan.discountType)
    }
    val lineStatusEnum = remember(plan.status) {
        OfferDetailPlanStatus.fromId(plan.status)
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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

                Text(
                    text = displayName,
                    style = typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                StatusBadge(status = lineStatusEnum)

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
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OfferLineDetailRow(
                    label = stringResource(R.string.label_commission_type),
                    value = stringResource(commissionTitle.titleRes),
                    valueColor = appColors.textPrimary
                )

                OfferLineDetailRow(
                    label = stringResource(R.string.label_discount_type),
                    value = stringResource(discountTitle.titleRes),
                    valueColor = appColors.textPrimary
                )
            }
        }
    }
}

@Composable
private fun OfferLineDetailRow(
    label: String,
    value: String,
    valueColor: Color,
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
