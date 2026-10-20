package ir.kitgroup.partnerManagement.feature.organization.ui.detail.warning

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.WarningTypeEntity
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomDescriptionField
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField

@Composable
fun AddEditOrganizationWarningDialog(
    warningTypes: List<WarningTypeEntity>,
    isSubmitting: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (warningId: String, description: String) -> Unit,
    initialWarningId: String? = null,
    initialDescription: String = "",
    modifier: Modifier = Modifier
){
    var selectedWarningId by rememberSaveable(initialWarningId) {
        mutableStateOf(initialWarningId)
    }

    var description by rememberSaveable(initialDescription) {
        mutableStateOf(initialDescription)
    }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val selectedWarningType = remember(selectedWarningId, warningTypes) {
        warningTypes.firstOrNull { it.warningTypeId == selectedWarningId }
    }
    val warningTitles = warningTypes.mapNotNull { it.title }
    Dialog(
        onDismissRequest = {
            if (!isSubmitting) onDismiss()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth(0.92f)
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // هدر دیالوگ
                Text(
                    text = stringResource(R.string.label_register_organization_warning),
                    style = typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                DropdownSelectorField(
                    value = selectedWarningType?.title.orEmpty(),
                    label = stringResource(R.string.label_warnings_type),
                    placeholder = stringResource(R.string.hint_choose_warnings_type),
                    items = warningTitles,
                    isRequired = true,
                    onItemSelected = { title ->
                        selectedWarningId = warningTypes
                            .firstOrNull { it.title == title }
                            ?.warningTypeId
                            .orEmpty()
                    })

                // توضیحات
                CustomDescriptionField(
                    label = stringResource(R.string.label_description),
                    value = description,
                    onValueChange = { description = it },
                    placeholder = stringResource(R.string.hint_description)
                )

                // دکمه‌های عملیات (لغو و ثبت تذکر)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // دکمه لغو
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

                    // 👇 فاصله استاندارد بین دکمه لغو و ثبت
                    Spacer(modifier = Modifier.width(12.dp))

                    // دکمه ثبت تذکر
                    CustomButton(
                        text = stringResource(R.string.label_register_warning),
                        onClick = {
                            selectedWarningId?.let { warningId ->
                                onConfirm(warningId, description)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !isSubmitting && selectedWarningId != null,
                        isLoading = isSubmitting
                    )
                }
            }
        }
    }
}
