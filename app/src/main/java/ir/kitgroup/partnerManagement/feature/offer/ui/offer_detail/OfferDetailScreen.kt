package ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.OfferEntity
import ir.kitgroup.partnerManagement.core.database.model.OfferDetailWithProduct
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.CommissionType
import ir.kitgroup.partnerManagement.core.ui.util.DiscountType
import ir.kitgroup.partnerManagement.core.ui.util.GenderType
import ir.kitgroup.partnerManagement.core.ui.util.PersonCategory
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import ir.kitgroup.partnerManagement.core.ui.components.BouncingDotsIndicator
import ir.kitgroup.partnerManagement.core.ui.components.CustomDialog
import ir.kitgroup.partnerManagement.core.ui.components.DualActionButtonBottomBar
import ir.kitgroup.partnerManagement.core.ui.util.OfferDetailPlanStatus
import ir.kitgroup.partnerManagement.core.ui.util.SecondaryButtonVariant
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.toAmountDisplay

@Composable
fun OfferDetailScreen(
    onNavigateBack: () -> Unit,
    onEditClick: (offerDetailId: String) -> Unit,
    viewModel: OfferDetailViewModel = hiltViewModel()
) {
    val offerDetail by viewModel.offerDetail.collectAsStateWithLifecycle()
    val offer by viewModel.offer.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val appColors = LocalPartnerManagementColors.current
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    var showDeleteConfirmationDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowMessage -> {
                    snackBarHostState.showSnackbar(event.message.asString(context))
                }

                is UiEvent.ShowError -> {
                    snackBarHostState.showSnackbar(event.error.asString(context))
                }
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackBarHostState) },
        bottomBar = {
            if (offerDetail != null) {
                DualActionButtonBottomBar(
                    primaryText = stringResource(R.string.label_edit),
                    secondaryText = stringResource(R.string.label_delete),
                    onPrimaryClick = { onEditClick(viewModel.offerDetailId) },
                    onSecondaryClick = { showDeleteConfirmationDialog = true },
                    isSecondaryLoading = uiState.isLoading,
                    secondaryVariant = SecondaryButtonVariant.FILLED
                )
            }
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            shape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp
            ),
            color = MaterialTheme.colorScheme.background
        ) {
            val currentItem = offerDetail
            if (currentItem == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    BouncingDotsIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                OfferDetailContent(
                    item = currentItem,
                    offer = offer
                )
            }
        }
    }

    CustomDialog(
        isVisible = showDeleteConfirmationDialog,
        title = stringResource(R.string.title_delete_offer_detail),
        message = stringResource(R.string.msg_confirm_delete_offer_detail),
        confirmText = stringResource(R.string.label_delete),
        dismissText = stringResource(R.string.label_cancel),
        onConfirm = {
            if (!uiState.isLoading) {
                showDeleteConfirmationDialog = false
                viewModel.deleteOfferDetail(
                    onSuccessCallback = onNavigateBack
                )
            }
        },
        onDismiss = {
            if (!uiState.isLoading) {
                showDeleteConfirmationDialog = false
            }
        },
        confirmColor = MaterialTheme.colorScheme.error,
        dismissColor = appColors.textSecondary
    )

}

@Composable
private fun OfferDetailContent(
    item: OfferDetailWithProduct,
    offer: OfferEntity?
) {
    val plan = item.offerDetail
    val displayName = item.resolvedProductName

    val personCategoryEnum =
        remember(plan.personCategory) { PersonCategory.fromId(plan.personCategory) }
    val genderEnum = remember(plan.gender) { GenderType.fromId(plan.gender) }
    val commissionTypeEnum =
        remember(plan.commissionType) { CommissionType.fromId(plan.commissionType) }
    val discountTypeEnum = remember(plan.discountType) { DiscountType.fromId(plan.discountType) }
    val lineStatusEnum = remember(plan.status) { OfferDetailPlanStatus.fromId(plan.status) }

    val offerTitle = offer?.title?.takeIf { it.isNotBlank() } ?: "-"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        //  بخش اطلاعات اصلی طرح
        OfferDetailSection(
            title = stringResource(R.string.label_offer_main_information),
            headerTrailingContent = {
                StatusBadge(status = lineStatusEnum)
            }
        ) {
            OfferDetailRow(
                label = stringResource(R.string.label_plan_header),
                value = offerTitle,
            )

            OfferDetailRow(
                label = stringResource(R.string.label_related_product_or_service),
                value = displayName
            )

            OfferDetailRow(
                label = stringResource(R.string.label_age_category),
                value = stringResource(personCategoryEnum.titleRes)
            )

            OfferDetailRow(
                label = stringResource(R.string.label_gender),
                value = stringResource(genderEnum.titleRes),
                showDivider = false
            )
        }

        // بخش اطلاعات پورسانت / کمیسیون
        OfferDetailSection(
            title = stringResource(R.string.label_offer_commission_information)
        ) {
            OfferDetailRow(
                label = stringResource(R.string.label_commission_type),
                value = stringResource(commissionTypeEnum.titleRes),
                showDivider = commissionTypeEnum != CommissionType.NONE
            )

            when (commissionTypeEnum) {
                CommissionType.PERCENTAGE -> {
                    OfferDetailRow(
                        label = stringResource(R.string.label_commission_percent),
                        value = plan.commissionPercent?.toString().toPercentDisplay(),
                        showDivider = false
                    )
                }

                CommissionType.FIXED_AMOUNT -> {
                    OfferDetailRow(
                        label = stringResource(R.string.label_commission_amount),
                        value = plan.commissionAmount?.toString().toAmountDisplay(),
                        showDivider = false
                    )
                }

                CommissionType.NONE -> {
                    // در حالت بدون پورسانت، سطری برای مبلغ یا درصد نمایش داده نمی‌شود
                }
            }
        }

        // بخش اطلاعات تخفیف
        OfferDetailSection(
            title = stringResource(R.string.label_offer_discount_information)
        ) {
            OfferDetailRow(
                label = stringResource(R.string.label_discount_type),
                value = stringResource(discountTypeEnum.titleRes),
                showDivider = discountTypeEnum != DiscountType.NONE
            )

            when (discountTypeEnum) {
                DiscountType.PERCENTAGE -> {
                    OfferDetailRow(
                        label = stringResource(R.string.label_discount_percent),
                        value = plan.discountPercent?.toString().toPercentDisplay(),
                        showDivider = false
                    )
                }

                DiscountType.FIXED_AMOUNT -> {
                    OfferDetailRow(
                        label = stringResource(R.string.label_discount_amount),
                        value = plan.discountAmount?.toString().toAmountDisplay(),
                        showDivider = false
                    )
                }

                DiscountType.NONE -> {
                    // در حالت بدون تخفیف، سطری برای مبلغ یا درصد نمایش داده نمی‌شود
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
private fun OfferDetailSection(
    title: String,
    headerTrailingContent: (@Composable () -> Unit)? = null,
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
            width = 0.8.dp,
            color = appColors.border
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                headerTrailingContent?.invoke()
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(
                color = appColors.border.copy(alpha = 0.5f),
                thickness = 0.6.dp
            )
            Spacer(modifier = Modifier.height(4.dp))

            content()
        }
    }
}

@Composable
private fun OfferDetailRow(
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color? = null,
    showDivider: Boolean = true
) {
    val appColors = LocalPartnerManagementColors.current

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = appColors.textSecondary
            )

            Text(
                text = value,
                style = MaterialTheme.typography.labelLarge,
                color = valueColor ?: appColors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (showDivider) {
            HorizontalDivider(
                color = appColors.border.copy(alpha = 0.4f),
                thickness = 0.6.dp
            )
        }
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
