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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.R.string.label_mobile
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomDescriptionField
import ir.kitgroup.partnerManagement.core.ui.components.CustomEditTextField
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.CustomOutlinedButton
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.components.YesNoSwitchRow
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.Status
import ir.kitgroup.partnerManagement.feature.organization.model.PersonOrganization

@Composable
fun AddOrganizationScreen(
    onBackClick: () -> Unit,
    onCancel: () -> Unit,
    onSaveClick: () -> Unit,
    onSelectLocation: () -> Unit,
    onAddImage: () -> Unit,
) {
    val appColors = LocalPartnerManagementColors.current

    val formState = rememberAddOrganizationFormState()
    val personState = rememberAddPersonFormState()

    var showAddPersonSheet by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomHeader(
                title = R.string.label_add_new_organization,
                showBackButton = true,
                onBackClick = onBackClick
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
                if (personState.name.isNotBlank() && personState.phone.isNotBlank()) {
                    formState.organizationPersons.add(
                        PersonOrganization(
                            name = personState.name.trim(),
                            mobile = personState.mobile.trim(),
                            phone = personState.phone.trim(),
                            status = personState.status,
                            gender = personState.gender,
                            description = personState.description.trim()
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

    val organizationStatusList = remember {
        listOf("ثبت اولیه", "فعال", "غیرفعال", "معلق")
    }

    SectionTitle(stringResource(R.string.label_organization_basic_information))

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
                value = state.organizationStatus,
                label = stringResource(R.string.label_status),
                placeholder = "",
                items = organizationStatusList,
                isRequired = false,
                onItemSelected = { state.organizationStatus = it }
            )
        }
    )
}

@Composable
private fun OrganizationContactSection(
    state: AddOrganizationFormState
) {
    SectionTitle(stringResource(R.string.label_contact_information))

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
    address: String,
    onAddressChange: (String) -> Unit,
    onSelectLocation: () -> Unit
) {
    SectionTitle(stringResource(R.string.label_location))

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
    SectionTitle(stringResource(R.string.label_analytical_information))

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
    SectionTitle(stringResource(R.string.label_statuses))

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
    persons: List<PersonOrganization>,
    onAddPersonClick: () -> Unit,
    onRemovePerson: (Int) -> Unit
) {
    SectionTitle(stringResource(R.string.label_related_persons))

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
    SectionTitle(stringResource(R.string.label_images))

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
private fun AddPersonBottomSheet(
    personState: AddPersonFormState,
    onDismiss: () -> Unit,
    onSavePerson: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current

    val genderList = remember {
        listOf("آقا", "خانم")
    }

    val statusList = remember {
        listOf("پیش‌نویس", "فعال", "غیرفعال", "مسدود شده")
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
                        value = personState.phone,
                        onValueChange = { personState.phone = it },
                        label = stringResource(R.string.label_phone),
                        placeholder = stringResource(R.string.hint_enter_phone),
                        isRequired = true,
                        leadingIcon = null
                    )
                },
                end = {
                    CustomEditTextField(
                        value = personState.mobile,
                        onValueChange = { personState.mobile = it },
                        label = stringResource(R.string.label_mobile),
                        placeholder = stringResource(R.string.hint_enter_mobile),
                        isRequired = false,
                        leadingIcon = null
                    )
                }
            )

            DropdownSelectorField(
                value = personState.gender,
                label = stringResource(R.string.label_gender),
                placeholder = stringResource(R.string.hint_choose_gender),
                items = genderList,
                isRequired = false,
                onItemSelected = {
                    personState.gender = it
                }
            )

            DropdownSelectorField(
                value = personState.status,
                label = stringResource(R.string.label_person_status),
                placeholder = stringResource(R.string.hint_choose_status),
                items = statusList,
                isRequired = false,
                onItemSelected = {
                    personState.status = it
                }
            )

            CustomDescriptionField(
                label = stringResource(R.string.label_person_description),
                value = personState.description,
                onValueChange = { personState.description = it },
                placeholder = stringResource(R.string.hint_description)
            )

            AddPersonBottomSheetActions(
                enabled = personState.name.isNotBlank() && personState.phone.isNotBlank(),
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
    persons: List<PersonOrganization>,
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
                            stringResource(R.string.label_no_person_added)
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
    person: PersonOrganization,
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
                        StatusBadge(
                            status = personStatusToStatus(person.status)
                        )

                        if (person.gender.isNotBlank()) {
                            Text(
                                text = person.gender,
                                style = typography.labelSmall,
                                color = appColors.textSecondary
                            )
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
                title = stringResource(label_mobile),
                value = person.mobile
            )

            PersonInfoRow(
                title = stringResource(R.string.label_phone),
                value = person.phone
            )

            if (person.description.isNotBlank()) {
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
private fun GradeSelector(
    grade: Int,
    onGradeChange: (Int) -> Unit
) {
    val appColors = LocalPartnerManagementColors.current
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.label_organization_grade),
                style = typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(1.dp, appColors.border, RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (i in 5 downTo 1) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onGradeChange(i) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = if (i <= grade) Icons.Default.Star else Icons.Outlined.StarBorder,
                        contentDescription = null,
                        tint = if (i <= grade) Color(0xFFFFC107) else appColors.border
                    )
                    Text(
                        text = i.toString(),
                        style = typography.labelSmall,
                        color = appColors.border
                    )
                }
            }
        }
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
                            tint = Color.White
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
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme {
            AddOrganizationScreen(
                onBackClick = {},
                onCancel = {},
                onSaveClick = {},
                onSelectLocation = {},
                onAddImage = {}
            )
        }
    }
}
