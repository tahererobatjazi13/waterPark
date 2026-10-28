package ir.kitgroup.partnerManagement.feature.organization.ui.detail.warning

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.TrendingFlat
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.WarningTypeEntity
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomDescriptionField
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.theme.PartnerManagementTheme

@Composable
fun AddEditOrganizationWarningDialog(
    warningTypes: List<WarningTypeEntity>,
    isSubmitting: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (warningId: String, description: String) -> Unit,
    initialWarningId: String? = null,
    initialDescription: String = ""
) {
    val isEditMode = !initialWarningId.isNullOrBlank()
    val scrollState = rememberScrollState()
    val appColors = LocalPartnerManagementColors.current

    var selectedWarningId by rememberSaveable(initialWarningId) {
        mutableStateOf(initialWarningId)
    }

    var description by rememberSaveable(initialDescription) {
        mutableStateOf(initialDescription)
    }

    // کش کردن نوع تذکر انتخاب‌شده جهت دسترسی سریع به مشخصات و امتیاز
    val selectedWarningType = remember(selectedWarningId, warningTypes) {
        warningTypes.firstOrNull { it.warningTypeId == selectedWarningId }
    }

    // فهرست عناوین بدون مقادیر نال یا خالی
    val warningTitles = remember(warningTypes) {
        warningTypes.mapNotNull { it.title?.trim() }.filter { it.isNotEmpty() }
    }

    Dialog(
        onDismissRequest = {
            if (!isSubmitting) onDismiss()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = !isSubmitting,
            dismissOnClickOutside = !isSubmitting
        )
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = appColors.cardBackground
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // هدر دیالوگ همراه با آیکون و تشخیص وضعیت ثبت یا ویرایش
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    MaterialTheme.colorScheme.tertiaryContainer,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.RateReview,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onTertiaryContainer,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column {
                            Text(
                                text = if (isEditMode) {
                                    stringResource(R.string.label_edit) + " " + stringResource(R.string.label_warnings)
                                } else {
                                    stringResource(R.string.label_register_organization_warning)
                                },
                                style = typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = appColors.textPrimary
                            )
                        }
                    }

                    // انتخاب‌گر نوع تذکر
                    DropdownSelectorField(
                        value = selectedWarningType?.title.orEmpty(),
                        label = stringResource(R.string.label_warnings_type),
                        placeholder = stringResource(R.string.hint_choose_warnings_type),
                        items = warningTitles,
                        isRequired = true,
                        onItemSelected = { selectedTitle ->
                            val matchedEntity =
                                warningTypes.firstOrNull { it.title?.trim() == selectedTitle.trim() }
                            selectedWarningId = matchedEntity?.warningTypeId.orEmpty()
                        }
                    )

                    // نمایش بج پیش‌نمایش امتیاز نوع تذکر انتخاب‌شده
                    AnimatedVisibility(
                        visible = selectedWarningType?.score != null,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        selectedWarningType?.score?.let { score ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "امتیاز لحاظ‌شده:",
                                    style = typography.labelMedium,
                                    color = appColors.textSecondary
                                )
                                DialogScoreBadge(score = score)
                            }
                        }
                    }

                    // فیلد توضیحات تذکر
                    CustomDescriptionField(
                        label = stringResource(R.string.label_description),
                        value = description,
                        onValueChange = { description = it },
                        placeholder = stringResource(R.string.hint_description)
                    )

                    // دکمه‌های کنترلی تایید و لغو
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CustomButton(
                            text = stringResource(R.string.label_cancel),
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            enabled = !isSubmitting,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        CustomButton(
                            text = if (isEditMode) {
                                stringResource(R.string.label_edit)
                            } else {
                                stringResource(R.string.label_register_warning)
                            },
                            onClick = {
                                selectedWarningId?.takeIf { it.isNotBlank() }?.let { id ->
                                    onConfirm(id, description.trim())
                                }
                            },
                            modifier = Modifier.weight(1f),
                            enabled = !isSubmitting && !selectedWarningId.isNullOrBlank(),
                            isLoading = isSubmitting
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogScoreBadge(score: Int) {
    val isPositive = score > 0
    val isZero = score == 0

    val chipColor = when {
        isPositive -> PartnerManagementTheme.colors.successContainer
        isZero -> MaterialTheme.colorScheme.surfaceVariant
        else -> PartnerManagementTheme.colors.errorContainer
    }

    val contentColor = when {
        isPositive -> PartnerManagementTheme.colors.success
        isZero -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> PartnerManagementTheme.colors.error
    }

    val icon = when {
        isPositive -> Icons.Default.ArrowUpward
        isZero -> Icons.Default.TrendingFlat
        else -> Icons.Default.ArrowDownward
    }

    val scoreText = when {
        score > 0 -> "+$score"
        else -> "$score"
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = chipColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(13.dp)
            )
            Text(
                text = "$scoreText امتیاز",
                style = typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        }
    }
}
