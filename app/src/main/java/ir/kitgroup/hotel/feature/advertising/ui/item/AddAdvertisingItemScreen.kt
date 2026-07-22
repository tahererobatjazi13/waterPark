package ir.kitgroup.hotel.feature.advertising.ui.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ir.kitgroup.hotel.R
import ir.kitgroup.hotel.core.ui.components.CustomHeader
import ir.kitgroup.hotel.feature.advertising.model.AdvertisingItem
import ir.kitgroup.hotel.navigation.Screen


@Composable
fun AddAdvertisingItemScreen(
    itemId: String,
    onBackClick: () -> Unit,
    onSaveClick: (AdvertisingItem) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    val isEditMode = itemId != Screen.AdvertisingDetail.NEW_ITEM_ID

    var title by rememberSaveable {
        mutableStateOf("")
    }

    var type by rememberSaveable {
        mutableStateOf("DesktopStand")
    }

    var stockText by rememberSaveable {
        mutableStateOf("")
    }

    var isActive by rememberSaveable {
        mutableStateOf(true)
    }

    var iconName by rememberSaveable {
        mutableStateOf("stand")
    }

    var description by rememberSaveable {
        mutableStateOf("")
    }

    val typesMap = remember {
        listOf(
            "DesktopStand" to R.string.type_desktop_stand,
            "WallStand" to R.string.type_wall_stand,
            "Brochure" to R.string.type_brochure,
            "Other" to R.string.type_other
        )
    }

    val iconsMap = remember {
        listOf(
            "stand" to R.string.icon_stand,
            "brochure" to R.string.icon_brochure,
            "campaign" to R.string.icon_campaign,
            "location" to R.string.icon_location
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.primary)
    ) {
        CustomHeader(
            title = if (isEditMode) {
                R.string.label_edit_item
            } else {
                R.string.label_add_new_items
            },
            showBackButton = true,
            onBackClick = onBackClick
        )

        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp
            ),
            color = colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.label_item_title)) },
                    placeholder = { Text(stringResource(R.string.placeholder_item_title)) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorScheme.primary,
                        unfocusedBorderColor = colorScheme.outline,
                        focusedLabelColor = colorScheme.primary,
                        unfocusedLabelColor = colorScheme.onSurfaceVariant,
                        cursorColor = colorScheme.primary,
                        focusedTextColor = colorScheme.onSurface,
                        unfocusedTextColor = colorScheme.onSurface,
                        focusedContainerColor = colorScheme.surface,
                        unfocusedContainerColor = colorScheme.surface
                    )
                )

                val currentTypeLabelRes =
                    typesMap.firstOrNull { it.first == type }?.second ?: R.string.type_other

                DropdownField(
                    label = stringResource(R.string.label_item_type),
                    items = typesMap,
                    selectedLabel = stringResource(currentTypeLabelRes),
                    onItemSelected = { selectedKey -> type = selectedKey }
                )

                OutlinedTextField(
                    value = stockText,
                    onValueChange = { stockText = it.filter { ch -> ch.isDigit() } },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.label_stock)) },
                    placeholder = { Text(stringResource(R.string.placeholder_stock)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorScheme.primary,
                        unfocusedBorderColor = colorScheme.outline,
                        focusedLabelColor = colorScheme.primary,
                        unfocusedLabelColor = colorScheme.onSurfaceVariant,
                        cursorColor = colorScheme.primary,
                        focusedTextColor = colorScheme.onSurface,
                        unfocusedTextColor = colorScheme.onSurface,
                        focusedContainerColor = colorScheme.surface,
                        unfocusedContainerColor = colorScheme.surface
                    )
                )

                val currentIconLabelRes =
                    iconsMap.firstOrNull { it.first == iconName }?.second ?: R.string.icon_stand

                DropdownField(
                    label = stringResource(R.string.label_icon_category),
                    items = iconsMap,
                    selectedLabel = stringResource(currentIconLabelRes),
                    onItemSelected = { selectedKey -> iconName = selectedKey }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.label_active_status),
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onSurface
                    )
                    Switch(
                        checked = isActive,
                        onCheckedChange = { isActive = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = colorScheme.onPrimary,
                            checkedTrackColor = colorScheme.primary,
                            checkedBorderColor = colorScheme.primary,
                            uncheckedThumbColor = colorScheme.surface,
                            uncheckedTrackColor = colorScheme.outline,
                            uncheckedBorderColor = colorScheme.outline
                        )
                    )
                    Text(
                        text = if (isActive) {
                            stringResource(R.string.status_active)
                        } else {
                            stringResource(R.string.status_inactive)
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isActive) colorScheme.primary else colorScheme.onSurfaceVariant
                    )
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    label = { Text(stringResource(R.string.label_description)) },
                    placeholder = { Text(stringResource(R.string.placeholder_description)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorScheme.primary,
                        unfocusedBorderColor = colorScheme.outline,
                        focusedLabelColor = colorScheme.primary,
                        unfocusedLabelColor = colorScheme.onSurfaceVariant,
                        cursorColor = colorScheme.primary,
                        focusedTextColor = colorScheme.onSurface,
                        unfocusedTextColor = colorScheme.onSurface,
                        focusedContainerColor = colorScheme.surface,
                        unfocusedContainerColor = colorScheme.surface
                    )
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        val stock = stockText.toIntOrNull() ?: 0

                        val result = AdvertisingItem(
                            id = if (isEditMode) {
                                itemId
                            } else {
                                System.currentTimeMillis().toString()
                            },
                            title = title.trim(),
                            type = type,
                            stock = stock,
                            isActive = isActive,
                            iconName = iconName
                        )

                        onSaveClick(result)
                    },
                    enabled = title.isNotBlank(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = if (isEditMode) {
                            stringResource(R.string.btn_edit_item)
                        } else {
                            stringResource(R.string.btn_submit_item)
                        }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownField(
    label: String,
    items: List<Pair<String, Int>>,
    selectedLabel: String,
    onItemSelected: (String) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedLabel,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorScheme.primary,
                unfocusedBorderColor = colorScheme.outline,
                focusedLabelColor = colorScheme.primary,
                unfocusedLabelColor = colorScheme.onSurfaceVariant,
                focusedTextColor = colorScheme.onSurface,
                unfocusedTextColor = colorScheme.onSurface,
                focusedContainerColor = colorScheme.surface,
                unfocusedContainerColor = colorScheme.surface
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(item.second),
                            color = colorScheme.onSurface
                        )
                    },
                    onClick = {
                        onItemSelected(item.first)
                        expanded = false
                    }
                )
            }
        }
    }
}

