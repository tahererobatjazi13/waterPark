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
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomDescriptionField
import ir.kitgroup.partnerManagement.core.ui.components.CustomEditTextField
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.CustomSelectorField
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.feature.advertising_stand.model.AdvertisingStandItem
import ir.kitgroup.partnerManagement.navigation.Screen

@Composable
fun AddAdvertisingStandScreen(
    itemId: String,
    onBackClick: () -> Unit,
    onSaveClick: (AdvertisingStandItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val appColors = LocalPartnerManagementColors.current
    val isEditMode = itemId != Screen.AdvertisingStandDetail.NEW_ITEM_ID

    var standName by rememberSaveable { mutableStateOf("") }

    var standType by rememberSaveable { mutableStateOf("") }
    var isStandTypeExpanded by remember { mutableStateOf(false) }
    val standTypeList = remember {
        listOf(
            "بنری", "استند بروشور", "استند رومیزی", "بک لایت", "رول آپ",
            "ایکس استند", "پاپ آپ", "کیوسک", "راهنما", "برندینگ", "سایر"
        )
    }

    var installationType by rememberSaveable { mutableStateOf("") }
    var isInstallationTypeExpanded by remember { mutableStateOf(false) }
    val installationTypeList = remember {
        listOf(
            "ثابت", "پرتال", "دیواری", "آویزی", "ایستاده",
            "رومیزی"
        )
    }

    var displayType by rememberSaveable { mutableStateOf("") }
    var isDisplayTypeExpanded by remember { mutableStateOf(false) }
    val displayTypeList = remember {
        listOf(
            "چاپی", "دیجیتال", "تعاملی"
        )
    }
    var description by rememberSaveable { mutableStateOf("") }

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

                    CustomEditTextField(
                        value = standName,
                        onValueChange = { standName = it },
                        label = stringResource(R.string.label_stand_name),
                        placeholder = stringResource(R.string.hint_enter_stand_name),
                        leadingIcon = null,
                    )

                    Box(modifier = Modifier.fillMaxWidth()) {
                        CustomSelectorField(
                            value = standType,
                            label = stringResource(R.string.label_stand_type),
                            placeholder = stringResource(R.string.hint_choose_stand_type),
                            isExpanded = isStandTypeExpanded,
                            onClick = {
                                isStandTypeExpanded = !isStandTypeExpanded
                            }
                        )
                        DropdownMenu(
                            expanded = isStandTypeExpanded,
                            onDismissRequest = { isStandTypeExpanded = false },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(appColors.cardBackground)
                        ) {
                            standTypeList.forEachIndexed { index, standTypeItem ->
                                val backgroundColor =
                                    if (index % 2 == 0) appColors.cardBackground else appColors.cardBackgroundAlt
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = backgroundColor
                                ) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = standTypeItem,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = appColors.textPrimary
                                            )
                                        },
                                        onClick = {
                                            standType = standTypeItem
                                            isStandTypeExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Box(modifier = Modifier.fillMaxWidth()) {
                        CustomSelectorField(
                            value = installationType,
                            label = stringResource(R.string.label_installation_type),
                            placeholder = stringResource(R.string.hint_choose_installation_type),
                            isExpanded = isInstallationTypeExpanded,
                            onClick = {
                                isInstallationTypeExpanded = !isInstallationTypeExpanded
                            }
                        )
                        DropdownMenu(
                            expanded = isInstallationTypeExpanded,
                            onDismissRequest = { isInstallationTypeExpanded = false },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(appColors.cardBackground)
                        ) {
                            installationTypeList.forEachIndexed { index, installationTypeItem ->
                                val backgroundColor =
                                    if (index % 2 == 0) appColors.cardBackground else appColors.cardBackgroundAlt
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = backgroundColor
                                ) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = installationTypeItem,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = appColors.textPrimary
                                            )
                                        },
                                        onClick = {
                                            installationType = installationTypeItem
                                            isInstallationTypeExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Box(modifier = Modifier.fillMaxWidth()) {
                        CustomSelectorField(
                            value = displayType,
                            label = stringResource(R.string.label_display_type),
                            placeholder = stringResource(R.string.hint_choose_display_type),
                            isExpanded = isDisplayTypeExpanded,
                            onClick = {
                                isDisplayTypeExpanded = !isDisplayTypeExpanded
                            }
                        )
                        DropdownMenu(
                            expanded = isDisplayTypeExpanded,
                            onDismissRequest = { isDisplayTypeExpanded = false },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(appColors.cardBackground)
                        ) {
                            displayTypeList.forEachIndexed { index, displayTypeItem ->
                                val backgroundColor =
                                    if (index % 2 == 0) appColors.cardBackground else appColors.cardBackgroundAlt
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = backgroundColor
                                ) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = displayTypeItem,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = appColors.textPrimary
                                            )
                                        },
                                        onClick = {
                                            displayType = displayTypeItem
                                            isDisplayTypeExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    CustomDescriptionField(
                        label = stringResource(R.string.label_description),
                        value = description,
                        onValueChange = { description = it },
                        placeholder = stringResource(R.string.hint_description)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                CustomButton(
                    text = if (isEditMode) stringResource(R.string.label_save) else stringResource(
                        R.string.label_submit_stand
                    ),
                    enabled = true,
                    onClick = {
                        if (standName.isBlank()) return@CustomButton
                        val result = AdvertisingStandItem(
                            id = if (isEditMode) itemId else System.currentTimeMillis().toString(),
                            name = standName.trim(),
                            standType = standType,
                            installationType = installationType,
                            displayType = displayType,
                            description = description
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
private fun AddAdvertisingStandScreenPreview() {
    AppScreenPreview {
        AddAdvertisingStandScreen(
            itemId = "new",
            onBackClick = {},
            onSaveClick = {}
        )
    }
}
