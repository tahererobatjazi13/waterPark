package ir.kitgroup.partnerManagement.feature.organization.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomDescriptionField
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle

@Composable
fun AddOrganizationNoticeDialog(
    onDismiss: () -> Unit,
    onConfirm: (
        noticeType: String,
        description: String
    ) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    var selectedType by rememberSaveable { mutableStateOf("") }

    val warningsTypeList = remember {
        listOf(
            "کیفیت خدمات",
            "مالی و تسویه حساب",
            "قرارداد و تعهدات",
            "استند تبلیغتی",
            "سایر"
        )
    }


    var description by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colors.surface,
        shape = RoundedCornerShape(20.dp),
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                SectionTitle(stringResource(R.string.label_register_organization_notice))

                DropdownSelectorField(
                    value = selectedType,
                    label = stringResource(R.string.label_warnings_type),
                    placeholder = stringResource(R.string.hint_choose_warnings_type),
                    items = warningsTypeList,
                    isRequired = true,
                    onItemSelected = { selectedType = it }
                )

                CustomDescriptionField(
                    label = stringResource(R.string.label_notice_description),
                    value = description,
                    onValueChange = { description = it },
                    placeholder = stringResource(R.string.hint_notice_description)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
        },
        confirmButton = {
            CustomButton(
                text = stringResource(R.string.label_register_notice),
                onClick = {
                    onConfirm(
                        selectedType,
                        description.trim()
                    )
                },
              fillMaxWidth = false,
                height = 38.dp,
                cornerRadius = 12.dp,
                textStyle = typography.titleMedium,
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                )
        },
        dismissButton = {
            Box(
                modifier = Modifier.padding(end = 12.dp)
            ) {
                CustomButton(
                    text = stringResource(R.string.label_cancel),
                    onClick = onDismiss,
                    fillMaxWidth = false,
                    height = 38.dp,
                    cornerRadius = 12.dp,
                    textStyle = typography.titleMedium,
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.surfaceVariant,
                        contentColor = colors.onSurfaceVariant
                    )
                )
            }
        }
    )
}
