package ir.kitgroup.partnerManagement.feature.organization.ui.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomDescriptionField
import ir.kitgroup.partnerManagement.core.ui.components.CustomOutlinedButton
import ir.kitgroup.partnerManagement.core.ui.util.AttachmentFileType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageDetailInputDialog(
    onDismiss: () -> Unit,
    onConfirm: (fileType: Int, description: String) -> Unit
) {
    var selectedType by remember { mutableStateOf(AttachmentFileType.SIGNBOARD_IMAGE) }
    var descriptionText by remember { mutableStateOf("") }
    var expandedDropdown by remember { mutableStateOf(false) }

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
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = stringResource(R.string.label_image_specifications), // یا اگر در ریسورس ندارید: "مشخصات تصویر"
                    style = typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                // انتخاب نوع عکس با Dropdown
                ExposedDropdownMenuBox(
                    expanded = expandedDropdown,
                    onExpandedChange = { expandedDropdown = !expandedDropdown }
                ) {
                    OutlinedTextField(
                        value = stringResource(selectedType.titleRes),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.label_image_type)) }, // یا "نوع تصویر"
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedDropdown,
                        onDismissRequest = { expandedDropdown = false }
                    ) {
                        AttachmentFileType.entries.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(text = stringResource(type.titleRes)) },
                                onClick = {
                                    selectedType = type
                                    expandedDropdown = false
                                }
                            )
                        }
                    }
                }

                // فیلد وارد کردن نام یا توضیحات عکس
                CustomDescriptionField(
                    label = stringResource(R.string.label_description),
                    value = descriptionText,
                    onValueChange = { descriptionText = it },
                    placeholder =""
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
                        text = stringResource(R.string.label_confirm_and_add),
                        onClick = {
                            onConfirm(selectedType.id, descriptionText.trim())
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
