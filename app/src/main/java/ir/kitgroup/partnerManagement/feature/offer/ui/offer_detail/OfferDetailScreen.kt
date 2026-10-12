package ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail

import android.widget.Toast
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.material3.MaterialTheme.typography

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.OfferEntity
import ir.kitgroup.partnerManagement.core.database.model.OfferDetailWithProduct
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.CommissionType
import ir.kitgroup.partnerManagement.core.ui.util.DiscountType
import ir.kitgroup.partnerManagement.core.ui.util.GenderType
import ir.kitgroup.partnerManagement.core.ui.util.OfferPlanLineStatus
import ir.kitgroup.partnerManagement.core.ui.util.PersonCategory
import kotlinx.coroutines.flow.collectLatest
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import kotlinx.coroutines.flow.collectLatest

@Composable
fun OfferDetailScreen(

    onNavigateBack: () -> Unit,
    onNavigateToEdit: (offerDetailId: String) -> Unit,
    viewModel: OfferDetailViewModel = hiltViewModel()
) {
    val planWithProduct by viewModel.offerDetail.collectAsStateWithLifecycle()
    val parentOffer by viewModel.parentOffer.collectAsStateWithLifecycle()
    val appColors = LocalPartnerManagementColors.current
    val isDeleting by viewModel.isDeleting.collectAsStateWithLifecycle()

    val context = LocalContext.current

    var showDeleteConfirmationDialog by rememberSaveable { mutableStateOf(false) }

    // دریافت رویدادهای حذف و بازگشت
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is OfferDetailViewModel.UiEvent.DeleteSuccess -> {
                    Toast.makeText(context, "با موفقیت حذف شد", Toast.LENGTH_SHORT).show()
                    onNavigateBack()
                }

                is OfferDetailViewModel.UiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (planWithProduct != null) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = appColors.cardBackground,
                    tonalElevation = 8.dp,
                    border = BorderStroke(width = 0.5.dp, color = appColors.border)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // دکمه ویرایش
                        Button(
                            onClick = { onNavigateToEdit(viewModel.offerDetailId) },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            enabled = !isDeleting
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "ویرایش", style = MaterialTheme.typography.labelLarge)
                        }

                        // دکمه حذف
                        OutlinedButton(
                            onClick = { showDeleteConfirmationDialog = true },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            enabled = !isDeleting
                        ) {
                            if (isDeleting) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.error
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = "حذف", style = MaterialTheme.typography.labelLarge)
                            }
                        }
                    }
                }
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
            val currentItem = planWithProduct
            if (currentItem == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                OfferDetailContent(
                    item = currentItem,
                    parentOffer = parentOffer
                )
            }
        }
    }

    // دیالوگ تأیید حذف
    if (showDeleteConfirmationDialog) {
        AlertDialog(
            onDismissRequest = {
                if (!isDeleting) showDeleteConfirmationDialog = false
            },
            title = {
                Text(
                    text = "حذف سطر طرح",
                    style = MaterialTheme.typography.titleMedium,
                    color = appColors.textPrimary
                )
            },
            text = {
                Text(
                    text = "آیا از حذف این سطر از طرح اطمینان دارید؟ این عملیات قابل بازگشت نیست.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = appColors.textSecondary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirmationDialog = false
                        viewModel.deleteOfferDetail()
                    }
                ) {
                    Text(text = "حذف شود", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteConfirmationDialog = false }
                ) {
                    Text(text = "انصراف", color = appColors.textSecondary)
                }
            },
            containerColor = appColors.cardBackground,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
private fun OfferDetailContent(
    item: OfferDetailWithProduct,
    parentOffer: OfferEntity?
) {
    val plan = item.offerDetail
    val appColors = LocalPartnerManagementColors.current
    val displayName = item.resolvedProductName

    val personCategoryEnum =
        remember(plan.personCategory) { PersonCategory.fromId(plan.personCategory) }
    val genderEnum = remember(plan.gender) { GenderType.fromId(plan.gender) }
    val commissionTypeEnum =
        remember(plan.commissionType) { CommissionType.fromId(plan.commissionType) }
    val discountTypeEnum = remember(plan.discountType) { DiscountType.fromId(plan.discountType) }
    val lineStatusEnum = remember(plan.status) { OfferPlanLineStatus.fromId(plan.status) }

    // جلوگیری از کرش در صورت null بودن یا لود نشدن طرح والد
    val offerPlanTitle = parentOffer?.title?.takeIf { it.isNotBlank() } ?: "-"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        // ۱. بخش اطلاعات اصلی طرح
        OfferDetailSection(
            title = stringResource(R.string.label_offer_main_information),
            headerTrailingContent = {
                StatusBadge(status = lineStatusEnum)
            }
        ) {
            OfferDetailRow(
                label = stringResource(R.string.label_plan_header),
                value = offerPlanTitle,
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

        // ۲. بخش اطلاعات پورسانت / کمیسیون
        OfferDetailSection(
            title = stringResource(R.string.label_offer_commission_information)
        ) {
            OfferDetailRow(
                label = stringResource(R.string.label_commission_type),
                value = stringResource(commissionTypeEnum.titleRes)
            )

            OfferDetailRow(
                label = stringResource(R.string.label_commission_percent),
                value = plan.commissionPercent?.toString().toPercentDisplay()
            )

            OfferDetailRow(
                label = stringResource(R.string.label_commission_amount),
                value = plan.commissionAmount?.toString().toAmountDisplay(),
                showDivider = false
            )
        }

        // ۳. بخش اطلاعات تخفیف
        OfferDetailSection(
            title = stringResource(R.string.label_offer_discount_information)
        ) {
            OfferDetailRow(
                label = stringResource(R.string.label_discount_type),
                value = stringResource(discountTypeEnum.titleRes)
            )

            OfferDetailRow(
                label = stringResource(R.string.label_discount_percent),
                value = plan.discountPercent?.toString().toPercentDisplay()
            )

            OfferDetailRow(
                label = stringResource(R.string.label_discount_amount),
                value = plan.discountAmount?.toString().toAmountDisplay(),
                showDivider = false
            )
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
