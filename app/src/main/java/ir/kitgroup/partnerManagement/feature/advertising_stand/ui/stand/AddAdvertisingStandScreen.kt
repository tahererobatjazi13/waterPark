package ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.AdvertisingStandEntity
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomDescriptionField
import ir.kitgroup.partnerManagement.core.ui.components.CustomEditTextField
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.CustomSelectorField
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.DisplayType
import ir.kitgroup.partnerManagement.core.ui.util.InstallationType
import ir.kitgroup.partnerManagement.core.ui.util.StandType
import ir.kitgroup.partnerManagement.navigation.Screen
import java.util.UUID

@Composable
fun AddAdvertisingStandScreen(
    itemId: String,
    onBackClick: () -> Unit,
    onSaveClick: (AdvertisingStandEntity) -> Unit,
    modifier: Modifier = Modifier,
    initialEntity: AdvertisingStandEntity? = null
) {
    val colorScheme = MaterialTheme.colorScheme
    val appColors = LocalPartnerManagementColors.current
    val isEditMode = itemId != Screen.AdvertisingStandDetail.NEW_ITEM_ID

    var standName by rememberSaveable { mutableStateOf(initialEntity?.name ?: "") }
    val standCode by rememberSaveable { mutableStateOf(initialEntity?.code ?: "") }
    var description by rememberSaveable { mutableStateOf(initialEntity?.description ?: "") }

    // Stand Type (Int?)
    var selectedStandType by rememberSaveable { mutableStateOf(initialEntity?.standType) }
    var isStandTypeExpanded by remember { mutableStateOf(false) }

    // Installation Type (Enum)
    var selectedInstallationType by rememberSaveable {
        mutableStateOf(InstallationType.fromId(initialEntity?.installationType))
    }
    var isInstallationTypeExpanded by remember { mutableStateOf(false) }

    // Display Type (Enum)
    var selectedDisplayType by rememberSaveable {
        mutableStateOf(DisplayType.fromId(initialEntity?.displayType))
    }
    var isDisplayTypeExpanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CustomHeader(
                title = if (isEditMode) R.string.label_edit_stand else R.string.label_add_new_stand,
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
                    // فیلد نام استند
                    CustomEditTextField(
                        value = standName,
                        onValueChange = { standName = it },
                        label = stringResource(R.string.label_stand_name),
                        placeholder = stringResource(R.string.hint_enter_stand_name),
                        leadingIcon = null,
                    )

                    // نوع استند (StandType)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        CustomSelectorField(
                            value = StandType.fromId(selectedStandType)
                                ?.let { stringResource(it.titleRes) } ?: "",
                            label = stringResource(R.string.label_stand_type),
                            placeholder = stringResource(R.string.hint_choose_stand_type),
                            isExpanded = isStandTypeExpanded,
                            onClick = { isStandTypeExpanded = !isStandTypeExpanded }
                        )
                        DropdownMenu(
                            expanded = isStandTypeExpanded,
                            onDismissRequest = { isStandTypeExpanded = false },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(appColors.cardBackground)
                        ) {
                            StandType.entries.forEachIndexed { index, type ->
                                val backgroundColor =
                                    if (index % 2 == 0) appColors.cardBackground else appColors.cardBackgroundAlt
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = backgroundColor
                                ) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = stringResource(type.titleRes),
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = appColors.textPrimary
                                            )
                                        },
                                        onClick = {
                                            selectedStandType = type.id // ذخیره ID در متغیر
                                            isStandTypeExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // دراپ‌داون نحوه نصب (InstallationType Enum)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        CustomSelectorField(
                            value = selectedInstallationType?.let { stringResource(it.titleRes) }
                                ?: "",
                            label = stringResource(R.string.label_installation_type),
                            placeholder = stringResource(R.string.hint_choose_installation_type),
                            isExpanded = isInstallationTypeExpanded,
                            onClick = { isInstallationTypeExpanded = !isInstallationTypeExpanded }
                        )
                        DropdownMenu(
                            expanded = isInstallationTypeExpanded,
                            onDismissRequest = { isInstallationTypeExpanded = false },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(appColors.cardBackground)
                        ) {
                            InstallationType.entries.forEachIndexed { index, type ->
                                val backgroundColor =
                                    if (index % 2 == 0) appColors.cardBackground else appColors.cardBackgroundAlt
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = backgroundColor
                                ) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = stringResource(type.titleRes),
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = appColors.textPrimary
                                            )
                                        },
                                        onClick = {
                                            selectedInstallationType = type
                                            isInstallationTypeExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // دراپ‌داون نوع نمایش (DisplayType Enum)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        CustomSelectorField(
                            value = selectedDisplayType?.let { stringResource(it.titleRes) } ?: "",
                            label = stringResource(R.string.label_display_type),
                            placeholder = stringResource(R.string.hint_choose_display_type),
                            isExpanded = isDisplayTypeExpanded,
                            onClick = { isDisplayTypeExpanded = !isDisplayTypeExpanded }
                        )
                        DropdownMenu(
                            expanded = isDisplayTypeExpanded,
                            onDismissRequest = { isDisplayTypeExpanded = false },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(appColors.cardBackground)
                        ) {
                            DisplayType.entries.forEachIndexed { index, type ->
                                val backgroundColor =
                                    if (index % 2 == 0) appColors.cardBackground else appColors.cardBackgroundAlt
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = backgroundColor
                                ) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = stringResource(type.titleRes),
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = appColors.textPrimary
                                            )
                                        },
                                        onClick = {
                                            selectedDisplayType = type
                                            isDisplayTypeExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // فیلد توضیحات
                    CustomDescriptionField(
                        label = stringResource(R.string.label_description),
                        value = description,
                        onValueChange = { description = it },
                        placeholder = stringResource(R.string.hint_description)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                // دکمه ذخیره
                CustomButton(
                    text = if (isEditMode) {
                        stringResource(R.string.label_save)
                    } else {
                        stringResource(R.string.label_submit_stand)
                    },
                    enabled = standName.isNotBlank(),
                    onClick = {
                        if (standName.isBlank()) return@CustomButton

                        val generatedId = if (isEditMode) itemId else UUID.randomUUID().toString()

                        val entity = AdvertisingStandEntity(
                            advertisingStandId = generatedId,
                            name = standName.trim(),
                            code = standCode.ifBlank { null },
                            description = description.trim().ifBlank { null },
                            displayType = selectedDisplayType?.id,
                            installationType = selectedInstallationType?.id,
                            recreationCenterId = initialEntity?.recreationCenterId,
                            standType = selectedStandType
                        )
                        onSaveClick(entity)
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
private fun AddAdvertisingStandScreenPreview() {
    AppScreenPreview {
        AddAdvertisingStandScreen(
            itemId = "new",
            onBackClick = {},
            onSaveClick = {}
        )
    }
}
