package ir.kitgroup.partnerManagement.feature.organization.ui.add

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.CityEntity
import ir.kitgroup.partnerManagement.core.database.entity.PersonEntity
import ir.kitgroup.partnerManagement.core.database.entity.RegionEntity
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomDescriptionField
import ir.kitgroup.partnerManagement.core.ui.components.CustomEditTextField
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.CustomOutlinedButton
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField
import ir.kitgroup.partnerManagement.core.ui.components.GradeSelector
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.components.YesNoSwitchRow
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.DataState
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationLevel
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationStatus
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationType
import ir.kitgroup.partnerManagement.core.ui.util.Status
import java.util.UUID
import android.view.ViewGroup
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.core.ui.util.PersonGenderType
import ir.kitgroup.partnerManagement.core.ui.util.PersonOrganizationRole
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun AddOrganizationScreen(
    organizationId: String?,
    onBack: () -> Unit,
    onSuccessCreate: () -> Unit = {},
    onSelectLocation: () -> Unit = {},
    selectedLatitude: Double? = null,
    selectedLongitude: Double? = null,
    onAddImage: () -> Unit = {},
    viewModel: AddOrganizationViewModel = hiltViewModel()
) {

    val cities by viewModel.cities.collectAsState()
    val regions by viewModel.regions.collectAsState()
    val appColors = LocalPartnerManagementColors.current

    val isEditMode = !organizationId.isNullOrBlank()

    val formState = viewModel.formState
    val personState = rememberAddPersonFormState()

    val existingOrganization by remember(organizationId) {
        if (organizationId != null) {
            viewModel.observeOrganization(organizationId)
        } else {
            kotlinx.coroutines.flow.flowOf(null)
        }
    }.collectAsStateWithLifecycle(initialValue = null)

    var formInitialized by remember(organizationId) {
        mutableStateOf(false)
    }

    LaunchedEffect(existingOrganization) {

        val organization = existingOrganization
            ?: return@LaunchedEffect

        if (formInitialized) return@LaunchedEffect

        formInitialized = true

        formState.organizationName =
            organization.organization.name.orEmpty()

        formState.organizationType =
            organization.organization.organizationType ?: 0

        formState.organizationLevel =
            organization.organization.level ?: 0

        formState.ownerName =
            organization.organization.ownerName.orEmpty()

        formState.englishName =
            organization.organization.englishName.orEmpty()

        formState.nationalId =
            organization.organization.nationalId.orEmpty()

        formState.landLine =
            organization.organization.landLine.orEmpty()

        formState.phone =
            organization.organization.phone.orEmpty()

        formState.mobile =
            organization.organization.mobile.orEmpty()

        formState.email =
            organization.organization.email.orEmpty()

        formState.cityId =
            organization.organization.cityId.orEmpty()

        formState.regionId =
            organization.organization.regionId.orEmpty()

        if (formState.cityId.isNotBlank()) {
            viewModel.loadRegions(formState.cityId)
        }

        formState.address =
            organization.organization.address.orEmpty()

        formState.description =
            organization.organization.document.orEmpty()

        formState.latitude =
            organization.organization.latitude

        formState.longitude =
            organization.organization.longitude

        formState.organizationGrade =
            organization.organization.grade ?: 0

        formState.ticketSaleCountHistory =
            organization.organization.ticketSaleCountHistory
                ?.toString()
                .orEmpty()

        formState.customerCapacity =
            organization.organization.customerCapacity
                ?.toString()
                .orEmpty()

        formState.statusExtenalCutomer =
            organization.organization.statusExternalCustomer
                ?: false

        formState.statusGetStand =
            organization.organization.statusGetStand
                ?: false

        formState.organizationStatus =
            OrganizationStatus.fromId(
                organization.organization.status
            )
        formState.inactiveReason = ""
    }
    var showAddPersonSheet by rememberSaveable { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val createState by viewModel.createState.collectAsState()
    val context = LocalContext.current
    val isLoading = createState is DataState.Loading

    LaunchedEffect(selectedLatitude, selectedLongitude) {
        selectedLatitude?.let {
            formState.latitude = it
        }
        selectedLongitude?.let {
            formState.longitude = it
        }
    }

    // دریافت پیام‌های اعتبارسنجی
    LaunchedEffect(Unit) {
        viewModel.toastEvent.collect { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Long
            )
        }
    }

    // دریافت نتیجه ذخیره‌سازی
    LaunchedEffect(createState) {
        when (val state = createState) {
            is DataState.Success -> {
                val message = state.data.message?.takeIf { it.isNotBlank() }
                    ?: if (organizationId.isNullOrBlank()) {
                        "سازمان با موفقیت ایجاد شد"
                    } else {
                        "اطلاعات سازمان با موفقیت ویرایش شد"
                    }

                android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()

                viewModel.resetState()
                onSuccessCreate()
            }

            is DataState.Error -> {
                snackbarHostState.showSnackbar(
                    message = state.error.asString(context),
                    duration = androidx.compose.material3.SnackbarDuration.Long
                )
                viewModel.resetState()
            }

            else -> Unit
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                )
            )
        },
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
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            AddOrganizationContent(
                state = formState,
                isLoading = isLoading,
                cities = cities,
                regions = regions,
                onCitySelected = { cityId ->
                    formState.cityId = cityId
                    formState.regionId = ""
                    viewModel.loadRegions(cityId)
                },
                onRegionSelected = { regionId ->
                    formState.regionId = regionId
                },
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
                onSaveClick = {
                    viewModel.saveOrganization(
                        state = formState,
                        organizationId = organizationId
                    )
                },
                onCancel = onBack
            )
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.2f))
                        .clickable(enabled = false) {},
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }

/*
    if (showAddPersonSheet) {
        AddPersonBottomSheet(
            personState = personState,
            onDismiss = {
                showAddPersonSheet = false
            },
            onSavePerson = {
                if (personState.name.isNotBlank() && personState.mobile.isNotBlank()) {
                    formState.organizationPersons.add(
                        PersonEntity(
                            personId = UUID.randomUUID().toString(),
                            name = personState.name.trim(),
                            description = personState.description.trim(),
                            gender = personState.gender,
                            mobile = personState.mobile.trim(),
                            phone1 = personState.mobile.trim(),
                            status = personState.status
                        )
                    )

                    formState.resetPersonFields(personState)
                    showAddPersonSheet = false
                }
            }
        )
    }
*/
}


@Composable
private fun AddOrganizationContent(
    state: AddOrganizationFormState,
    isLoading: Boolean,
    cities: List<CityEntity>,
    regions: List<RegionEntity>,
    onCitySelected: (String) -> Unit,
    onRegionSelected: (String) -> Unit,
    onSelectLocation: () -> Unit,
    onAddImage: () -> Unit,
    onRemoveImage: (Int) -> Unit,
    onAddPersonClick: () -> Unit,
    onRemovePerson: (Int) -> Unit,
    onSaveClick: () -> Unit,
    onCancel: () -> Unit
) {
    var showInactiveReasonDialog by remember { mutableStateOf(false) }
    var previousStatus by remember { mutableStateOf(state.organizationStatus) }


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
            state = state,
            cities = cities,
            regions = regions,
            onCitySelected = onCitySelected,
            onRegionSelected = onRegionSelected,
            address = state.address,
            onAddressChange = { state.address = it },
            onSelectLocation = onSelectLocation
        )
        OrganizationAnalysisSection(state)

        OrganizationStatusSection(state)

        /* OrganizationRelatedPersonsSection(
             persons = state.organizationPersons,
             onAddPersonClick = onAddPersonClick,
             onRemovePerson = onRemovePerson
         )*/

        OrganizationDescriptionSection(state)

        /* OrganizationImagesSection(
             images = state.images,
             onAddImage = onAddImage,
             onRemoveImage = onRemoveImage
         )*/

        OrganizationFormActionButtons(
            isLoading = isLoading,
            onSaveClick = onSaveClick,
            onCancel = onCancel
        )
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

                // ثبت وضعیت و علت
                state.organizationStatus = OrganizationStatus.INACTIVE
                state.inactiveReason = reason

                // بستن دیالوگ
                showInactiveReasonDialog = false

                // ذخیره اطلاعات
                onSaveClick()
            }
        )
    }

}


@Composable
private fun OrganizationBasicInfoSection(
    state: AddOrganizationFormState
) {

    val organizationTypeOptions = OrganizationType.entries.map { level ->
        level to stringResource(level.titleRes)
    }

    val selectedOrganizationType = organizationTypeOptions
        .firstOrNull { it.first.code == state.organizationType }


    val organizationLevelOptions = OrganizationLevel.entries.map { level ->
        level to stringResource(level.titleRes)
    }

    val selectedOrganizationLevel = organizationLevelOptions
        .firstOrNull { it.first.code == state.organizationLevel }

    val statusEntries = listOf(
        OrganizationStatus.INITIAL_REGISTRATION,
        OrganizationStatus.ACTIVE
    )
    //val statusEntries = remember { OrganizationStatus.entries }
    val statusTitles = statusEntries.map { stringResource(it.titleRes) }


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
        value = selectedOrganizationType?.second.orEmpty(),
        label = stringResource(R.string.label_organization_type),
        placeholder = stringResource(R.string.hint_choose_organization_type),
        items = organizationTypeOptions.map { it.second },
        isRequired = true,
        onItemSelected = { selectedTitle ->

            val selectedType = organizationTypeOptions
                .firstOrNull { it.second == selectedTitle }
                ?.first

            selectedType?.let {
                state.organizationType = it.code
            }
        }
    )

    DropdownSelectorField(
        value = selectedOrganizationLevel?.second.orEmpty(),
        label = stringResource(R.string.label_organization_level),
        placeholder = stringResource(R.string.hint_choose_organization_level),
        items = organizationLevelOptions.map { it.second },
        isRequired = false,
        onItemSelected = { selectedTitle ->

            val selectedLevel = organizationLevelOptions
                .firstOrNull { it.second == selectedTitle }
                ?.first

            selectedLevel?.let {
                state.organizationLevel = it.code
            }
        }
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
                    if (selectedIndex != -1) {
                        state.organizationStatus = statusEntries[selectedIndex]
                    }

                    /*  val selectedEnum =
                          if (selectedIndex != -1) {
                              statusEntries[selectedIndex]
                          } else {
                              OrganizationStatus.INITIAL_REGISTRATION
                          }

                      if (selectedEnum == OrganizationStatus.INACTIVE) {
                          previousStatus = state.organizationStatus
                          showInactiveReasonDialog = true
                      } else {
                          state.organizationStatus = selectedEnum
                          state.inactiveReason = ""
                      }*/
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
    state: AddOrganizationFormState,
    cities: List<CityEntity>,
    regions: List<RegionEntity>,
    onCitySelected: (String) -> Unit,
    onRegionSelected: (String) -> Unit,
    address: String,
    onAddressChange: (String) -> Unit,
    onSelectLocation: () -> Unit
) {

    SectionTitle(
        title = stringResource(R.string.label_location),
        titleColor = MaterialTheme.colorScheme.primary
    )
    val selectedCity = cities.firstOrNull {
        it.cityId == state.cityId
    }

    val selectedRegion = regions.firstOrNull {
        it.regionId == state.regionId
    }
    TwoColumnRow(
        start = {
            DropdownSelectorField(
                value = selectedCity?.name.orEmpty(),
                label = stringResource(R.string.label_city),
                placeholder = stringResource(R.string.hint_choose_city),
                items = cities.mapNotNull { it.name },
                onItemSelected = { selectedName ->
                    val city = cities.firstOrNull {
                        it.name == selectedName
                    }

                    city?.let {
                        onCitySelected(it.cityId)
                    }
                },
                isRequired = false
            )
        },
        end = {
            DropdownSelectorField(
                value = selectedRegion?.name.orEmpty(),
                label = stringResource(R.string.label_region),
                placeholder = if (state.cityId == null) {
                    "ابتدا شهر را انتخاب کنید"
                } else {
                    stringResource(R.string.hint_choose_region)
                },
                items = regions.mapNotNull { it.name },
                onItemSelected = { selectedName ->
                    val region = regions.firstOrNull {
                        it.name == selectedName
                    }

                    region?.let {
                        onRegionSelected(it.regionId)
                    }
                },
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
        latitude = state.latitude,
        longitude = state.longitude,
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isRequired = false,
                leadingIcon = null
            )
        },
        end = {
            CustomEditTextField(
                value = state.customerCapacity,
                onValueChange = { state.customerCapacity = it },
                label = stringResource(R.string.label_customer_capacity),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
    isLoading: Boolean,
    onSaveClick: () -> Unit,
    onCancel: () -> Unit
) {
    Spacer(Modifier.height(8.dp))
    println("onSaveClick = 3")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CustomButton(
            text = stringResource(R.string.label_save),
            onClick = onSaveClick,
            enabled = !isLoading,
            modifier = Modifier.weight(1f)
        )

        CustomOutlinedButton(
            text = stringResource(R.string.label_cancel),
            onClick = onCancel,
            enabled = !isLoading,
            modifier = Modifier.weight(1f)
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
private fun MapCard(
    latitude: Double?,
    longitude: Double?,
    onSelectLocation: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current

    val hasLocation =
        latitude != null && longitude != null
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        if (!hasLocation) {

            // هنوز موقعیت انتخاب نشده
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(appColors.cardBackgroundAlt)
                    .border(
                        BorderStroke(
                            1.dp,
                            appColors.border
                        ),
                        RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Button(
                    onClick = onSelectLocation,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {

                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )

                    Text(
                        text = stringResource(
                            R.string.label_select_location
                        )
                    )
                }
            }

        } else {

            // موقعیت انتخاب شده
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(appColors.cardBackgroundAlt)
                    .border(
                        BorderStroke(
                            1.dp,
                            appColors.border
                        ),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(10.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text(
                        text = stringResource(
                            R.string.label_location_selected
                        ),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                LocationPreviewMap(
                    latitude = latitude!!,
                    longitude = longitude!!,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Text(
                    text = stringResource(
                        R.string.label_latitude_longitude,
                        latitude,
                        longitude
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = appColors.textSecondary
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                OutlinedButton(
                    onClick = onSelectLocation,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null
                    )

                    Spacer(
                        modifier = Modifier.width(6.dp)
                    )

                    Text(
                        text = stringResource(
                            R.string.label_change_location
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun LocationPreviewMap(
    latitude: Double,
    longitude: Double,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val point = remember(latitude, longitude) {
        GeoPoint(latitude, longitude)
    }

    AndroidView(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp)),

        factory = { ctx ->

            Configuration.getInstance().userAgentValue =
                ctx.packageName

            MapView(ctx).apply {

                setTileSource(
                    TileSourceFactory.MAPNIK
                )

                setMultiTouchControls(false)

                controller.setZoom(16.0)
                controller.setCenter(point)

                val marker = Marker(this).apply {

                    position = point

                    setAnchor(
                        Marker.ANCHOR_CENTER,
                        Marker.ANCHOR_BOTTOM
                    )

                    title = context.getString(
                        R.string.label_organization_location
                    )
                }

                overlays.add(marker)

                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        },

        update = { mapView ->

            val newPoint = GeoPoint(
                latitude,
                longitude
            )

            mapView.controller.animateTo(
                newPoint
            )

            val marker =
                mapView.overlays
                    .filterIsInstance<Marker>()
                    .firstOrNull()

            if (marker != null) {
                marker.position = newPoint
            }

            mapView.invalidate()
        }
    )
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
/*

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun AddOrganizationScreenPreview() {
    AppScreenPreview {
        AddOrganizationScreen(
            organizationId = "",
            onBack = {},
            onCancel = {},
            onSuccessCreate = {},
            onSelectLocation = {},
            onAddImage = {}
        )
    }
}
*/
