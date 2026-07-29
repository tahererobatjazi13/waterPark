package ir.kitgroup.partnerManagement.feature.advertising.ui.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomDescriptionField
import ir.kitgroup.partnerManagement.core.ui.components.CustomEditTextField
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.CustomSelectorField
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.AllocationStatus
import ir.kitgroup.partnerManagement.feature.advertising.model.AdvertisingItem
import ir.kitgroup.partnerManagement.navigation.Screen

@Composable
fun AddAdvertisingItemScreen(
    itemId: String,
    onBackClick: () -> Unit,
    onSaveClick: (AdvertisingItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val appColors = LocalPartnerManagementColors.current
    val isEditMode = itemId != Screen.AdvertisingDetail.NEW_ITEM_ID

    var title by rememberSaveable { mutableStateOf("") }
    var type by rememberSaveable { mutableStateOf("بروشور") }
    var isTypeExpanded by remember { mutableStateOf(false) }

    var stockText by rememberSaveable { mutableStateOf("") }
    var isActive by rememberSaveable { mutableStateOf(true) }
    var iconName by rememberSaveable { mutableStateOf("استند") }
    var description by rememberSaveable { mutableStateOf("") }

    val typeList = remember { listOf("رومیزی استند", "دیواری استند", "بروشور", "سایر") }
    val iconList = remember { listOf("استند", "بروشور ", "کمپین", "محل") }
    var isIconExpanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CustomHeader(
                title = if (isEditMode) R.string.label_edit_item else R.string.label_add_new_items,
                showBackButton = true,
                onBackClick = onBackClick
            )
        },
        containerColor = colorScheme.primary
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = appColors.screenBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    CustomEditTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = stringResource(R.string.label_item_title),
                        placeholder = stringResource(R.string.hint_enter_item_title),
                        leadingIcon = null,
                    )

                    Box(modifier = Modifier.fillMaxWidth()) {
                        CustomSelectorField(
                            value = type,
                            label = stringResource(R.string.label_choose_visitor),
                            placeholder = stringResource(R.string.hint_choose_visitor),
                            isExpanded = isTypeExpanded,
                            onClick = {
                                isTypeExpanded = !isTypeExpanded
                            }
                        )
                        DropdownMenu(
                            expanded = isTypeExpanded,
                            onDismissRequest = { isTypeExpanded = false },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(appColors.cardBackground)
                        ) {
                            typeList.forEachIndexed { index, typeItem ->
                                val backgroundColor =
                                    if (index % 2 == 0) appColors.cardBackground else appColors.cardBackgroundAlt
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = backgroundColor
                                ) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = typeItem,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = appColors.textPrimary
                                            )
                                        },
                                        onClick = {
                                            type = typeItem
                                            isTypeExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    CustomEditTextField(
                        value = stockText,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() }) {
                                stockText = newValue
                            }
                        },
                        label = stringResource(R.string.label_stock),
                        placeholder = stringResource(R.string.hint_stock_count),
                        leadingIcon = rememberVectorPainter(Icons.Default.Numbers),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )

                    Box(modifier = Modifier.fillMaxWidth()) {
                        CustomSelectorField(
                            value = iconName,
                            label = stringResource(R.string.label_icon_category),
                            placeholder = stringResource(R.string.hint_choose_stand_icon),
                            isExpanded = isIconExpanded,
                            onClick = {
                                isIconExpanded = !isIconExpanded
                            }
                        )
                        DropdownMenu(
                            expanded = isIconExpanded,
                            onDismissRequest = { isIconExpanded = false },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(appColors.cardBackground)
                        ) {
                            iconList.forEachIndexed { index, iconItem ->
                                val backgroundColor =
                                    if (index % 2 == 0) appColors.cardBackground else appColors.cardBackgroundAlt
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = backgroundColor
                                ) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = iconItem,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = appColors.textPrimary
                                            )
                                        },
                                        onClick = {
                                            iconName = iconItem
                                            isIconExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

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
                                uncheckedTrackColor = appColors.border,
                                uncheckedBorderColor = appColors.border
                            )
                        )
                        Text(
                            text = if (isActive) stringResource(R.string.label_active) else stringResource(
                                R.string.label_inactive
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isActive) colorScheme.primary else colorScheme.onSurfaceVariant
                        )
                    }

                    CustomDescriptionField(
                        label = stringResource(R.string.label_description),
                        value = description,
                        onValueChange = { newValue ->
                            description = newValue
                        },
                        placeholder = stringResource(R.string.hint_description)
                    )
                }

                CustomButton(
                    text = if (isEditMode) stringResource(R.string.label_edit_save_item) else stringResource(R.string.label_submit_item),
                    enabled = true,
                    onClick = {
                        if (title.isBlank()) return@CustomButton
                        val stock = stockText.toIntOrNull() ?: 0
                        val result = AdvertisingItem(
                            id = if (isEditMode) itemId else System.currentTimeMillis().toString(),
                            title = title.trim(),
                            type = type,
                            stock = stock,
                            isActive = isActive,
                            iconName = iconName,
                            status=AllocationStatus.Pending
                        )
                        onSaveClick(result)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )

            }
        }
    }
}


@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun AddAdvertisingItemScreenPreview() {
    AppScreenPreview {
        AddAdvertisingItemScreen(
            itemId = "new",
            onBackClick = {},
            onSaveClick = {}
        )
    }
}
