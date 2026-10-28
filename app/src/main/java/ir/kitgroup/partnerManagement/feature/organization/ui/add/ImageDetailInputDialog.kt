package ir.kitgroup.partnerManagement.feature.organization.ui.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomDescriptionField
import ir.kitgroup.partnerManagement.core.ui.components.CustomOutlinedButton
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField
import ir.kitgroup.partnerManagement.core.ui.util.AttachmentFileType

@Composable
fun ImageDetailInputDialog(
    allowedTypes: List<AttachmentFileType>,
    onDismiss: () -> Unit,
    onConfirm: (selectedFileType: Int, description: String) -> Unit
) {
    val context = LocalContext.current

    // نگاشت انواع به همراه عنوان متنی ترجمه‌شده با استفاده از context
    val typeOptions = remember(allowedTypes, context) {
        allowedTypes.map { it to context.getString(it.titleRes) }
    }

    var selectedType by remember(allowedTypes) {
        mutableStateOf(allowedTypes.firstOrNull() ?: AttachmentFileType.OTHER_IMAGES)
    }
    var description by rememberSaveable { mutableStateOf("") }

    val selectedTypeTitle = remember(selectedType, context) {
        context.getString(selectedType.titleRes)
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.label_file_specifications),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                DropdownSelectorField(
                    value = selectedTypeTitle,
                    label = stringResource(R.string.label_type),
                    placeholder = "",
                    items = typeOptions.map { it.second },
                    isRequired = true,
                    onItemSelected = { selectedTitle ->
                        typeOptions.firstOrNull { it.second == selectedTitle }?.let {
                            selectedType = it.first
                        }
                    }
                )

                CustomDescriptionField(
                    label = stringResource(R.string.label_description),
                    value = description,
                    onValueChange = { description = it },
                    placeholder = stringResource(R.string.hint_description)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CustomOutlinedButton(
                        text = stringResource(R.string.label_cancel),
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    )

                    CustomButton(
                        text = stringResource(R.string.label_confirm),
                        onClick = {
                            onConfirm(selectedType.id, description.trim())
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
