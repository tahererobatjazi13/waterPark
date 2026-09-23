package ir.kitgroup.partnerManagement.feature.organization.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.PersonEntity
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomDescriptionField
import ir.kitgroup.partnerManagement.core.ui.components.CustomEditTextField
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.CustomOutlinedButton
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField
import ir.kitgroup.partnerManagement.core.ui.components.GradeSelector
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.components.YesNoSwitchRow
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationStatus
import ir.kitgroup.partnerManagement.core.ui.util.Status

@Composable
fun AddOrganizationScreen(
    organizationId: String?,
    onBack: () -> Unit,
    onCancel: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    onSelectLocation: () -> Unit = {},
    onAddImage: () -> Unit = {},
) {
    val appColors = LocalPartnerManagementColors.current
    val isEditMode = organizationId != null

    val formState = rememberAddOrganizationFormState()
    val personState = rememberAddPersonFormState()

    var showAddPersonSheet by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomHeader(
                title = if (isEditMode) {
                    R.string.label_edit_organization
                } else {
                    R.string.label_add_new_organization
                },
                showBackButton = true,
                onBackClick = onBack
            )
        },
        containerColor = MaterialTheme.colorScheme.primary
    ) { innerPadding ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = appColors.screenBackground
        ) {
            AddOrganizationContent(
                state = formState,
                onSelectLocation = onSelectLocation,
                onAddImage = onAddImage,
                onRemoveImage = { index ->
                    formState.images.removeAt(index)
                },
                onAddPersonClick = {
                    formState.resetPersonFields(personState)
                    showAddPersonSheet = true
                },
                onRemovePerson = { index ->
                    formState.organizationPersons.removeAt(index)
                },
                onSaveClick = onSaveClick,
                onCancel = onCancel
            )
        }
    }

    if (showAddPersonSheet) {
        AddPersonBottomSheet(
            personState = personState,
            onDismiss = {
                showAddPersonSheet = false
            },
            onSavePerson = {
                if (personState.name.isNotBlank() && personState.phone1.isNotBlank()) {
                    formState.organizationPersons.add(
                        PersonEntity(
                            personId = java.util.UUID.randomUUID().toString(),
                            name = personState.name.trim(),
                            description = personState.description.trim(),
                            gender = personState.gender,
                            mobile = personState.mobile.trim(),
                            phone1 = personState.phone1.trim(),
                            status = personState.status
                        )
                    )

                    formState.resetPersonFields(personState)
                    showAddPersonSheet = false
                }
            }
        )
    }
}


@Composable
private fun AddOrganizationContent(
    state: AddOrganizationFormState,
    onSelectLocation: () -> Unit,
    onAddImage: () -> Unit,
    onRemoveImage: (Int) -> Unit,
    onAddPersonClick: () -> Unit,
    onRemovePerson: (Int) -> Unit,
    onSaveClick: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OrganizationBasicInfoSection(state)

        OrganizationContactSection(state)

        OrganizationLocationSection(
            city = state.city,
            onCityChange = { state.city = it },
            district = state.region,
            onDistrictChange = { state.region = it },
            address = state.address,
            onAddressChange = { state.address = it },
            onSelectLocation = onSelectLocation
        )

        OrganizationAnalysisSection(state)

        OrganizationStatusSection(state)

        OrganizationRelatedPersonsSection(
            persons = state.organizationPersons,
            onAddPersonClick = onAddPersonClick,
            onRemovePerson = onRemovePerson
        )

        OrganizationDescriptionSection(state)

        OrganizationImagesSection(
            images = state.images,
            onAddImage = onAddImage,
            onRemoveImage = onRemoveImage
        )

        OrganizationFormActionButtons(
            onSaveClick = onSaveClick,
            onCancel = onCancel
        )
    }
}


@Composable
private fun OrganizationBasicInfoSection(
    state: AddOrganizationFormState
) {
    val organizationTypeList = remember {
        listOf("هتل", "اداره دولتی", "شرکت خصوصی", "کیوسک", "سازمان / نهاد")
    }

    val organizationLevelList = remember {
        listOf("معمولی", "متوسط", "متوسط رو به بالا", "عالی")
    }
    val statusEntries = remember { OrganizationStatus.entries }
    val statusTitles = statusEntries.map { stringResource(it.titleRes) }

    var showInactiveReasonDialog by remember { mutableStateOf(false) }
    var previousStatus by remember { mutableStateOf(state.organizationStatus) }

    SectionTitle(
        title = stringResource(R.string.label_organization_basic_information),
        titleColor = MaterialTheme.colorScheme.primary
    )

    CustomEditTextField(
        value = state.organizationName,
        onValueChange = { state.organizationName = it },
        label = stringResource(R.string.label_organization_name),
        placeholder = stringResource(R.string.hint_enter_organization_name),
        leadingIcon = null
    )

    DropdownSelectorField(
        value = state.organizationType,
        label = stringResource(R.string.label_organization_type),
        placeholder = stringResource(R.string.hint_choose_organization_type),
        items = organizationTypeList,
        onItemSelected = { state.organizationType = it }
    )

    DropdownSelectorField(
        value = state.organizationLevel,
        label = stringResource(R.string.label_organization_level),
        placeholder = stringResource(R.string.hint_choose_organization_level),
        items = organizationLevelList,
        isRequired = false,
        onItemSelected = { state.organizationLevel = it }
    )

    TwoColumnRow(
        start = {
            CustomEditTextField(
                value = state.ownerName,
                onValueChange = { state.ownerName = it },
                label = stringResource(R.string.label_owner_name),
                placeholder = stringResource(R.string.hint_enter_owner_name),
                isRequired = false,
                leadingIcon = null
            )
        },
        end = {
            CustomEditTextField(
                value = state.englishName,
                onValueChange = { state.englishName = it },
                label = stringResource(R.string.label_english_name),
                placeholder = stringResource(R.string.hint_enter_english_name),
                isRequired = false,
                leadingIcon = null
            )
        }
    )

    TwoColumnRow(
        start = {
            CustomEditTextField(
                value = state.nationalId,
                onValueChange = { state.nationalId = it },
                label = stringResource(R.string.label_national_id),
                placeholder = stringResource(R.string.hint_enter_national_id),
                isRequired = false,
                leadingIcon = null
            )
        },
        end = {
            DropdownSelectorField(
                value = stringResource(state.organizationStatus.titleRes),
                label = stringResource(R.string.label_status),
                placeholder = "",
                items = statusTitles,
                isRequired = false,
                onItemSelected = { selectedTitle ->
                    val selectedIndex = statusTitles.indexOf(selectedTitle)
                    val selectedEnum =
                        if (selectedIndex != -1) statusEntries[selectedIndex] else OrganizationStatus.INITIAL_REGISTRATION

                    if (selectedEnum == OrganizationStatus.INACTIVE) {
                        previousStatus = state.organizationStatus
                        showInactiveReasonDialog = true
                    } else {
                        state.organizationStatus = selectedEnum
                        state.inactiveReason = ""
                    }
                }
            )
        }
    )

    // نمایش علت غیرفعال‌سازی فقط در وضعیت INACTIVE
    if (state.organizationStatus == OrganizationStatus.INACTIVE && state.inactiveReason.isNotBlank()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.25f)
            ),
            border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = stringResource(R.string.label_recorded_inactive_reason),
                    style = typography.labelMedium,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = state.inactiveReason,
                    style = typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
    if (showInactiveReasonDialog) {
        InactivationReasonDialog(
            initialReason = state.inactiveReason,
            onDismiss = {
                showInactiveReasonDialog = false
                if (state.inactiveReason.isBlank()) {
                    state.organizationStatus = previousStatus
                }
            },
            onConfirm = { reason ->
                state.organizationStatus = OrganizationStatus.INACTIVE
                state.inactiveReason = reason
                showInactiveReasonDialog = false
            }
        )
    }
}

@Composable
fun InactivationReasonDialog(
    initialReason: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var reasonText by rememberSaveable { mutableStateOf(initialReason) }
    var isError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.label_inactivation_reason_title),
                    style = typography.titleLarge,
                    color = MaterialTheme.colorScheme.error
                )

                Text(
                    text = stringResource(R.string.label_inactivation_reason_description),
                    style = typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                CustomDescriptionField(
                    label = stringResource(R.string.label_inactivation_reason_field),
                    value = reasonText,
                    onValueChange = {
                        reasonText = it
                        if (it.isNotBlank()) isError = false
                    },
                    placeholder = stringResource(R.string.hint_enter_inactivation_reason)
                )

                if (isError) {
                    Text(
                        text = stringResource(R.string.error_inactivation_reason_required),
                        style = typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

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
                        text = stringResource(R.string.label_registration),
                        onClick = {
                            if (reasonText.trim().isNotBlank()) {
                                onConfirm(reasonText.trim())
                            } else {
                                isError = true
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    )
                }
            }
        }
    }
}


@Composable
private fun OrganizationContactSection(
    state: AddOrganizationFormState
) {
    SectionTitle(
        title = stringResource(R.string.label_contact_information),
        titleColor = MaterialTheme.colorScheme.primary
    )

    TwoColumnRow(
        start = {
            CustomEditTextField(
                value = state.landLine,
                onValueChange = { state.landLine = it },
                label = stringResource(R.string.label_organization_land_line),
                placeholder = stringResource(R.string.hint_enter_land_line),
                isRequired = false,
                leadingIcon = null
            )
        },
        end = {
            CustomEditTextField(
                value = state.phone,
                onValueChange = { state.phone = it },
                label = stringResource(R.string.label_phone),
                placeholder = stringResource(R.string.hint_enter_phone),
                isRequired = false,
                leadingIcon = null
            )
        }
    )

    TwoColumnRow(
        start = {
            CustomEditTextField(
                value = state.mobile,
                onValueChange = { state.mobile = it },
                label = stringResource(R.string.label_mobile),
                placeholder = stringResource(R.string.hint_enter_mobile),
                isRequired = false,
                leadingIcon = null
            )
        },
        end = {
            CustomEditTextField(
                value = state.email,
                onValueChange = { state.email = it },
                label = stringResource(R.string.label_email),
                placeholder = stringResource(R.string.hint_enter_email),
                isRequired = false,
                leadingIcon = null
            )
        }
    )
}

@Composable
private fun OrganizationLocationSection(
    city: String,
    onCityChange: (String) -> Unit,
    district: String,
    onDistrictChange: (String) -> Unit,
    address: String,
    onAddressChange: (String) -> Unit,
    onSelectLocation: () -> Unit
) {
    // لیست‌های نمونه (این لیست‌ها می‌توانند از ریپوزیتوری یا دیتابیس بیایند)
    val cityList = remember { listOf("تهران", "مشهد", "اصفهان", "شیراز", "تبریز") }
    val districtList = remember { listOf("منطقه ۱", "منطقه ۲", "منطقه ۳", "مرکز شهر", "حاشیه شهر") }

    SectionTitle(
        title = stringResource(R.string.label_location),
        titleColor = MaterialTheme.colorScheme.primary
    )

    TwoColumnRow(
        start = {
            DropdownSelectorField(
                value = city,
                label = stringResource(R.string.label_city),
                placeholder = stringResource(R.string.hint_choose_city),
                items = cityList,
                onItemSelected = onCityChange,
                isRequired = false
            )
        },
        end = {
            DropdownSelectorField(
                value = district,
                label = stringResource(R.string.label_region),
                placeholder = stringResource(R.string.hint_choose_region),
                items = districtList,
                onItemSelected = onDistrictChange,
                isRequired = false
            )
        }
    )

    CustomEditTextField(
        value = address,
        onValueChange = onAddressChange,
        label = stringResource(R.string.label_address),
        placeholder = stringResource(R.string.hint_enter_address),
        isRequired = false,
        leadingIcon = null
    )

    MapCard(
        onSelectLocation = onSelectLocation
    )
}


@Composable
private fun OrganizationAnalysisSection(
    state: AddOrganizationFormState
) {
    SectionTitle(
        title = stringResource(R.string.label_analytical_information),
        titleColor = MaterialTheme.colorScheme.primary
    )
    GradeSelector(
        grade = state.organizationGrade,
        onGradeChange = { state.organizationGrade = it }
    )

    TwoColumnRow(
        start = {
            CustomEditTextField(
                value = state.ticketSaleCountHistory,
                onValueChange = { state.ticketSaleCountHistory = it },
                label = stringResource(R.string.label_ticket_sale_count_history),
                placeholder = "",
                isRequired = false,
                leadingIcon = null
            )
        },
        end = {
            CustomEditTextField(
                value = state.customerCapacity,
                onValueChange = { state.customerCapacity = it },
                label = stringResource(R.string.label_customer_capacity),
                placeholder = "",
                isRequired = false,
                leadingIcon = null
            )
        }
    )
}

@Composable
private fun OrganizationStatusSection(
    state: AddOrganizationFormState
) {
    SectionTitle(
        title = stringResource(R.string.label_statuses),
        titleColor = MaterialTheme.colorScheme.primary
    )
    YesNoSwitchRow(
        title = stringResource(R.string.label_foreign_guest_reception),
        checked = state.statusExtenalCutomer,
        onCheckedChange = { state.statusExtenalCutomer = it }
    )

    YesNoSwitchRow(
        title = stringResource(R.string.label_desire_receive_advertising_stands),
        checked = state.statusGetStand,
        onCheckedChange = { state.statusGetStand = it }
    )
}

@Composable
private fun OrganizationRelatedPersonsSection(
    persons: List<PersonEntity>,
    onAddPersonClick: () -> Unit,
    onRemovePerson: (Int) -> Unit
) {
    SectionTitle(
        title = stringResource(R.string.label_related_persons),
        titleColor = MaterialTheme.colorScheme.primary
    )
    OrganizationPersonsSummarySection(
        persons = persons,
        onAddPersonClick = onAddPersonClick,
        onRemovePerson = onRemovePerson
    )
}

@Composable
private fun OrganizationDescriptionSection(
    state: AddOrganizationFormState
) {
    CustomDescriptionField(
        label = stringResource(R.string.label_description),
        value = state.description,
        onValueChange = { state.description = it },
        placeholder = stringResource(R.string.hint_description)
    )
}

@Composable
private fun OrganizationImagesSection(
    images: List<Int>,
    onAddImage: () -> Unit,
    onRemoveImage: (Int) -> Unit
) {
    SectionTitle(
        title = stringResource(R.string.label_images),
        titleColor = MaterialTheme.colorScheme.primary
    )
    CollectionImagesSection(
        images = images,
        onAddImage = onAddImage,
        onRemoveImage = onRemoveImage
    )
}

@Composable
private fun OrganizationFormActionButtons(
    onSaveClick: () -> Unit,
    onCancel: () -> Unit
) {
    Spacer(Modifier.height(8.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CustomButton(
            text = stringResource(R.string.label_save),
            onClick = onSaveClick,
            modifier = Modifier.weight(1f)
        )

        CustomOutlinedButton(
            text = stringResource(R.string.label_cancel),
            onClick = onCancel,
            modifier = Modifier.weight(1f)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPersonBottomSheet(
    personState: AddPersonFormState,
    onDismiss: () -> Unit,
    onSavePerson: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current

    val genderList = remember {
        listOf("مرد", "زن")
    }

    val statusList = remember {
        listOf("پیش نویس", "فعال", "غیرفعال", "مسدود شده")
    }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = 60.dp),
        containerColor = appColors.screenBackground,
        dragHandle = {
            BottomSheetDefaults.DragHandle()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = stringResource(R.string.label_add_related_person),
                style = typography.titleLarge,
                color = appColors.textPrimary
            )

            CustomEditTextField(
                value = personState.name,
                onValueChange = { personState.name = it },
                label = stringResource(R.string.label_person_full_name),
                placeholder = stringResource(R.string.hint_enter_person_full_name),
                isRequired = true,
                leadingIcon = null
            )

            TwoColumnRow(
                start = {
                    CustomEditTextField(
                        value = personState.mobile,
                        onValueChange = { personState.mobile = it },
                        label = stringResource(R.string.label_mobile),
                        placeholder = stringResource(R.string.hint_enter_mobile),
                        isRequired = true,
                        leadingIcon = null
                    )
                },
                end = {
                    CustomEditTextField(
                        value = personState.phone1,
                        onValueChange = { personState.phone1 = it },
                        label = stringResource(R.string.label_phone),
                        placeholder = stringResource(R.string.hint_enter_phone),
                        isRequired = false,
                        leadingIcon = null
                    )
                }
            )
            DropdownSelectorField(
                value = when (personState.gender) {
                    1 -> "مرد"
                    2 -> "زن"
                    else -> ""
                },
                label = stringResource(R.string.label_gender),
                placeholder = stringResource(R.string.hint_choose_gender),
                items = genderList,
                isRequired = false,
                onItemSelected = { selectedGender ->
                    personState.gender = when (selectedGender) {
                        "مرد" -> 1
                        "زن" -> 2
                        else -> 0
                    }
                }
            )

            DropdownSelectorField(
                value = personState.status.toString(),
                label = stringResource(R.string.label_status),
                placeholder = stringResource(R.string.hint_choose_status),
                items = statusList,
                isRequired = false,
                onItemSelected = {
                    personState.status = 1
                }
            )

            CustomDescriptionField(
                label = stringResource(R.string.label_description),
                value = personState.description,
                onValueChange = { personState.description = it },
                placeholder = stringResource(R.string.hint_description)
            )

            AddPersonBottomSheetActions(
                enabled = personState.name.isNotBlank() && personState.phone1.isNotBlank(),
                onDismiss = onDismiss,
                onSavePerson = onSavePerson
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun AddPersonBottomSheetActions(
    enabled: Boolean,
    onDismiss: () -> Unit,
    onSavePerson: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CustomOutlinedButton(
            text = stringResource(R.string.label_cancel),
            onClick = onDismiss,
            modifier = Modifier.weight(1f)
        )

        CustomButton(
            text = stringResource(R.string.label_add_person),
            onClick = onSavePerson,
            modifier = Modifier.weight(1f),
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = appColors.border.copy(alpha = 0.4f),
                disabledContentColor = appColors.textSecondary
            )
        )
    }
}

@Composable
private fun OrganizationPersonsSummarySection(
    persons: List<PersonEntity>,
    onAddPersonClick: () -> Unit,
    onRemovePerson: (Int) -> Unit
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColors.cardBackground
        ),
        border = BorderStroke(1.dp, appColors.border)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.label_related_persons),
                        style = typography.titleMedium,
                        color = appColors.textPrimary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = if (persons.isEmpty()) {
                            stringResource(R.string.msg_no_person_found)
                        } else {
                            stringResource(
                                R.string.label_persons_count,
                                persons.size
                            )
                        },
                        style = typography.labelMedium,
                        color = appColors.textSecondary
                    )
                }

                CustomButton(
                    text = stringResource(R.string.label_add_person),
                    onClick = onAddPersonClick,
                    fillMaxWidth = false,
                    height = 38.dp,
                    cornerRadius = 12.dp,
                    textStyle = typography.labelMedium,
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )

            }

            if (persons.isNotEmpty()) {
                HorizontalDivider(
                    color = appColors.border.copy(alpha = 0.6f)
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    persons.forEachIndexed { index, person ->
                        OrganizationPersonCard(
                            person = person,
                            index = index,
                            onRemoveClick = {
                                onRemovePerson(index)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OrganizationPersonCard(
    person: PersonEntity,
    index: Int,
    onRemoveClick: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColors.cardBackgroundAlt
        ),
        border = BorderStroke(
            width = 1.dp,
            color = appColors.border.copy(alpha = 0.7f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "${index + 1}. ${person.name}".trim(),
                        style = typography.titleSmall,
                        color = appColors.textPrimary,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        person.gender?.let { genderCode ->
                            val genderTitle = when (genderCode) {
                                1 -> "مرد"
                                2 -> "زن"
                                else -> ""
                            }
                            if (genderTitle.isNotEmpty()) {
                                Text(
                                    text = genderTitle,
                                    style = typography.labelSmall,
                                    color = appColors.textSecondary
                                )
                            }
                        }

                    }
                }

                IconButton(
                    onClick = onRemoveClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.label_delete),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            PersonInfoRow(
                title = stringResource(R.string.label_mobile),
                value = person.mobile!!
            )

            PersonInfoRow(
                title = stringResource(R.string.label_phone),
                value = person.phone1!!
            )

            if (person.description!!.isNotBlank()) {
                PersonInfoRow(
                    title = stringResource(R.string.label_description),
                    value = person.description
                )
            }
        }
    }
}

fun personStatusToStatus(status: String): Status {
    return when (status) {
        "فعال" -> Status.ACTIVE
        "غیرفعال" -> Status.INACTIVE
        "پیش‌نویس" -> Status.DRAFT
        "مسدود شده" -> Status.BLOCKED
        else -> Status.INACTIVE
    }
}


@Composable
private fun TwoColumnRow(
    start: @Composable () -> Unit,
    end: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) { start() }
        Box(modifier = Modifier.weight(1f)) { end() }
    }
}


@Composable
private fun MapCard(
    onSelectLocation: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(appColors.cardBackgroundAlt)
            .border(
                BorderStroke(1.dp, appColors.border),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Button(
            onClick = onSelectLocation,
            modifier = Modifier.align(Alignment.Center),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = stringResource(R.string.label_select_location)
            )
        }
    }
}


@Composable
private fun CollectionImagesSection(
    images: List<Int>,
    onAddImage: () -> Unit,
    onRemoveImage: (Int) -> Unit
) {
    Column {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            item {
                AddImageCard(onClick = onAddImage)
            }

            itemsIndexed(images) { index, imageRes ->
                Box(
                    modifier = Modifier
                        .size(92.dp, 110.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Image(
                        painter = painterResource(imageRes),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    IconButton(
                        onClick = { onRemoveImage(index) },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .size(24.dp)
                            .background(Color.Red, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AddImageCard(
    onClick: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current
    Box(
        modifier = Modifier
            .size(92.dp, 110.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                1.dp,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.CloudUpload,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(R.string.label_add_image),
                style = typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.label_maximum_10_images),
                style = typography.labelSmall,
                color = appColors.border,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PersonInfoRow(
    title: String,
    value: String
) {
    val appColors = LocalPartnerManagementColors.current

    if (value.isBlank()) return

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "$title:",
            style = typography.labelMedium,
            color = appColors.textSecondary
        )

        Text(
            text = value,
            style = typography.labelMedium,
            color = appColors.textPrimary,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun AddOrganizationScreenPreview() {
    AppScreenPreview {
        AddOrganizationScreen(
            organizationId = "",
            onBack = {},
            onCancel = {},
            onSaveClick = {},
            onSelectLocation = {},
            onAddImage = {}
        )
    }
}
