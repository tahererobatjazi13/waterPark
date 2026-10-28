package ir.kitgroup.partnerManagement.feature.organization.ui.add


import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.CityEntity
import ir.kitgroup.partnerManagement.core.database.entity.RegionEntity
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomDescriptionField
import ir.kitgroup.partnerManagement.core.ui.components.CustomEditTextField
import ir.kitgroup.partnerManagement.core.ui.components.CustomOutlinedButton
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField
import ir.kitgroup.partnerManagement.core.ui.components.GradeSelector
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.components.YesNoSwitchRow
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationLevel
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationStatus
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationType
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import ir.kitgroup.partnerManagement.core.database.entity.VisitorEntity
import ir.kitgroup.partnerManagement.core.ui.SessionViewModel
import ir.kitgroup.partnerManagement.core.ui.util.UserRole
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.kitgroup.partnerManagement.core.network.model.AttachmentUploadResponse
import ir.kitgroup.partnerManagement.core.ui.components.ImageAddEditSection
import ir.kitgroup.partnerManagement.core.ui.util.AttachmentContext
import ir.kitgroup.partnerManagement.core.ui.util.AttachmentFileType
import ir.kitgroup.partnerManagement.core.ui.util.DataState
import ir.kitgroup.partnerManagement.core.ui.util.isDocumentUri


@Composable
fun AddEditOrganizationScreen(
    onSuccessSave: () -> Unit,
    onSelectLocationClick: (() -> Unit)? = null,
    selectedLatitude: Double? = null,
    selectedLongitude: Double? = null,
    viewModel: AddEditOrganizationViewModel = hiltViewModel(),
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    val roleCode by sessionViewModel.roleCode.collectAsState()
    val currentRole = remember(roleCode) { UserRole.fromCode(roleCode.toString()) }
    val isSupervisor = currentRole == UserRole.SUPERVISOR

    val baseUrl by viewModel.baseUrlFlow.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val uploadState by viewModel.uploadState.collectAsStateWithLifecycle()


    // متغیرهای موقت برای نگهداری URI و نمایش دیالوگ تنظیمات تصویر
    var pendingImageUri by remember { mutableStateOf<Uri?>(null) }
    var showImageDetailDialog by remember { mutableStateOf(false) }


    LaunchedEffect(selectedLatitude, selectedLongitude) {
        if (selectedLatitude != null && selectedLongitude != null) {
            viewModel.onLocationChange(selectedLatitude, selectedLongitude)
        }
    }

    // لانچر انتخاب تصویر از گالری دستگاه
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            pendingImageUri = it
            showImageDetailDialog = true
        }
    }

    if (showImageDetailDialog && pendingImageUri != null) {
        val isDocument = remember(pendingImageUri) {
            isDocumentUri(context, pendingImageUri!!)
        }

        val allowedTypes = remember(isDocument) {
            AttachmentFileType.getAvailableTypes(
                context = AttachmentContext.ORGANIZATION,
                isDocument = isDocument
            )
        }

        ImageDetailInputDialog(
            allowedTypes = allowedTypes,
            onDismiss = {
                pendingImageUri = null
                showImageDetailDialog = false
            },
            onConfirm = { selectedFileType, description ->
                pendingImageUri?.let { uri ->
                    viewModel.onAddLocalAttachment(
                        uri = uri,
                        fileType = selectedFileType,
                        description = description
                    )
                }
                pendingImageUri = null
                showImageDetailDialog = false
            }
        )
    }

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowMessage -> snackBarHostState.showSnackbar(
                    event.message.asString(
                        context
                    )
                )

                is UiEvent.ShowError -> snackBarHostState.showSnackbar(event.error.asString(context))
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        bottomBar = {
            Surface(
                tonalElevation = 3.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    CustomButton(
                        text = if (uiState.isEditMode) {
                            stringResource(R.string.label_edit_organization)
                        } else {
                            stringResource(R.string.label_create_organization)
                        },
                        isLoading = uiState.isLoading,
                        enabled = uiState.isFormValid && uploadState !is DataState.Loading,
                        onClick = { viewModel.submitOrganization(onSuccessCallback = onSuccessSave) },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    ) { paddingValues ->
        AddOrganizationContent(
            uiState = uiState,
            uploadState = uploadState,
            baseUrl = baseUrl,
            isEditMode = viewModel.isEditMode,
            isSupervisor = isSupervisor,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            onOrganizationNameChange = viewModel::onOrganizationNameChange,
            onOrganizationTypeChange = viewModel::onOrganizationTypeChange,
            onOrganizationLevelChange = viewModel::onOrganizationLevelChange,
            onOrganizationStatusChange = viewModel::onOrganizationStatusChange,
            onOwnerNameChange = viewModel::onOwnerNameChange,
            onEnglishNameChange = viewModel::onEnglishNameChange,
            onNationalIdChange = viewModel::onNationalIdChange,
            onLandLineChange = viewModel::onLandLineChange,
            onPhoneChange = viewModel::onPhoneChange,
            onMobileChange = viewModel::onMobileChange,
            onEmailChange = viewModel::onEmailChange,
            onAddressChange = viewModel::onAddressChange,
            onOrganizationGradeChange = viewModel::onOrganizationGradeChange,
            onTicketSaleCountChange = viewModel::onTicketSaleCountChange,
            onCustomerCapacityChange = viewModel::onCustomerCapacityChange,
            onStatusExternalCustomerChange = viewModel::onStatusExternalCustomerChange,
            onStatusGetStandChange = viewModel::onStatusGetStandChange,
            onDescriptionChange = viewModel::onDescriptionChange,
            onCitySelected = viewModel::onCitySelected,
            onRegionSelected = viewModel::onRegionSelected,
            onSelectLocationClick = onSelectLocationClick ?: {},
            onAddImageClick = {
                val mimeTypes = arrayOf(
                    "image/*",
                    "application/pdf",
                    "application/msword",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                )
                imagePickerLauncher.launch(mimeTypes)
            },

            onRemoveImageClick = viewModel::onRemoveImage,
            onVisitorSelected = viewModel::onVisitorSelected,
        )
    }
}


@Composable
fun AddOrganizationContent(
    uiState: AddEditOrganizationUiState,
    uploadState: DataState<AttachmentUploadResponse>,
    baseUrl: String,
    isEditMode: Boolean,
    isSupervisor: Boolean,
    modifier: Modifier = Modifier,
    onOrganizationNameChange: (String) -> Unit,
    onOrganizationTypeChange: (Int) -> Unit,
    onOrganizationLevelChange: (Int) -> Unit,
    onOrganizationStatusChange: (OrganizationStatus) -> Unit,
    onOwnerNameChange: (String) -> Unit,
    onEnglishNameChange: (String) -> Unit,
    onNationalIdChange: (String) -> Unit,
    onLandLineChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onMobileChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onVisitorSelected: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onOrganizationGradeChange: (Int) -> Unit,
    onTicketSaleCountChange: (String) -> Unit,
    onCustomerCapacityChange: (String) -> Unit,
    onStatusExternalCustomerChange: (Boolean) -> Unit,
    onStatusGetStandChange: (Boolean) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onCitySelected: (String) -> Unit,
    onRegionSelected: (String) -> Unit,
    onSelectLocationClick: () -> Unit,
    onAddImageClick: () -> Unit,
    onRemoveImageClick: (Int) -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // اطلاعات پایه سازمان
        OrganizationBasicInfoSection(
            uiState = uiState,
            onOrganizationNameChange = onOrganizationNameChange,
            onOrganizationTypeChange = onOrganizationTypeChange,
            onOrganizationLevelChange = onOrganizationLevelChange,
            onOwnerNameChange = onOwnerNameChange,
            onEnglishNameChange = onEnglishNameChange,
            onNationalIdChange = onNationalIdChange,
            onMobileChange = onMobileChange,
            visitors = uiState.visitors,
            onVisitorSelected = onVisitorSelected,
            isSupervisor = isSupervisor
        )

        // اطلاعات تماس و آدرس
        OrganizationContactSection(
            uiState = uiState,
            onLandLineChange = onLandLineChange,
            onPhoneChange = onPhoneChange,
            onEmailChange = onEmailChange
        )

        // موقعیت مکانی (شهر، منطقه، آدرس متنی و نقشه)
        OrganizationLocationSection(
            uiState = uiState,
            cities = uiState.cities,
            regions = uiState.regions,
            onCitySelected = onCitySelected,
            onRegionSelected = onRegionSelected,
            onAddressChange = onAddressChange,
            onSelectLocation = onSelectLocationClick
        )

        // تحلیل و جزئیات تجاری
        OrganizationAnalysisSection(
            uiState = uiState,
            onGradeChange = onOrganizationGradeChange,
            onTicketSaleCountChange = onTicketSaleCountChange,
            onCustomerCapacityChange = onCustomerCapacityChange
        )

        //  وضعیت‌های جانبی (پذیرش میهمان خارجی و استند تبلیغاتی)
        OrganizationStatusSection(
            uiState = uiState,
            onOrganizationStatusChange = onOrganizationStatusChange,
            onExternalCustomerChange = onStatusExternalCustomerChange,
            onGetStandChange = onStatusGetStandChange
        )

        // توضیحات تکمیلی
        OrganizationDescriptionSection(
            description = uiState.description,
            onDescriptionChange = onDescriptionChange
        )
        ImageAddEditSection(
            images = uiState.images,
            baseUrl = baseUrl,
            isEditMode = isEditMode,
            onAddImage = onAddImageClick,
            onRemoveImage = onRemoveImageClick
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}


@Composable
fun OrganizationBasicInfoSection(
    uiState: AddEditOrganizationUiState,
    onOrganizationNameChange: (String) -> Unit,
    onOrganizationTypeChange: (Int) -> Unit,
    onOrganizationLevelChange: (Int) -> Unit,
    onOwnerNameChange: (String) -> Unit,
    onEnglishNameChange: (String) -> Unit,
    onNationalIdChange: (String) -> Unit,
    onMobileChange: (String) -> Unit,
    visitors: List<VisitorEntity>,
    onVisitorSelected: (String) -> Unit,
    isSupervisor: Boolean,

    ) {
    // گزینه‌های نوع سازمان
    val organizationTypeOptions = OrganizationType.entries.map { type ->
        type to stringResource(type.titleRes)
    }
    val selectedOrganizationType = organizationTypeOptions
        .firstOrNull { it.first.code == uiState.organizationType }

    // گزینه‌های سطح سازمان
    val organizationLevelOptions = OrganizationLevel.entries.map { level ->
        level to stringResource(level.titleRes)
    }
    val selectedOrganizationLevel = organizationLevelOptions
        .firstOrNull { it.first.code == uiState.organizationLevel }

    val selectedVisitor = visitors.firstOrNull { it.visitorId == uiState.visitorId }


    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SectionTitle(
            title = stringResource(R.string.label_organization_basic_information),
            titleColor = MaterialTheme.colorScheme.primary
        )

        CustomEditTextField(
            value = uiState.organizationName,
            onValueChange = onOrganizationNameChange,
            label = stringResource(R.string.label_organization_name),
            placeholder = stringResource(R.string.hint_enter_organization_name),
            isRequired = true,
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
                    onOrganizationTypeChange(it.code)
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
                    onOrganizationLevelChange(it.code)
                }
            }
        )
        if (isSupervisor && !uiState.isEditMode) {
            DropdownSelectorField(
                value = selectedVisitor?.name.orEmpty(),
                label = stringResource(R.string.label_visitor_name),
                placeholder = stringResource(R.string.hint_choose_visitor),
                items = visitors.mapNotNull { it.name },
                onItemSelected = { selectedName ->
                    visitors.firstOrNull { it.name == selectedName }?.let {
                        onVisitorSelected(it.visitorId)
                    }
                },
                isRequired = false
            )
        }
        if (!uiState.isEditMode) {

            TwoColumnRow(
                start = {
                    CustomEditTextField(
                        value = uiState.ownerName,
                        onValueChange = onOwnerNameChange,
                        label = stringResource(R.string.label_owner_name),
                        placeholder = stringResource(R.string.hint_enter_owner_name),
                        isRequired = true,
                        leadingIcon = null
                    )
                },
                end = {
                    CustomEditTextField(
                        value = uiState.mobile,
                        onValueChange = onMobileChange,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        label = stringResource(R.string.label_mobile_owner),
                        placeholder = stringResource(R.string.hint_enter_mobile),
                        isRequired = true,
                        errorMessage = uiState.mobileError,
                        leadingIcon = null
                    )
                }
            )
        }

        TwoColumnRow(
            start = {
                CustomEditTextField(
                    value = uiState.nationalId,
                    onValueChange = onNationalIdChange,
                    label = stringResource(R.string.label_national_id),
                    placeholder = stringResource(R.string.hint_enter_national_id),
                    isRequired = false,
                    leadingIcon = null
                )
            },
            end = {
                CustomEditTextField(
                    value = uiState.englishName,
                    onValueChange = onEnglishNameChange,
                    label = stringResource(R.string.label_english_name),
                    placeholder = stringResource(R.string.hint_enter_english_name),
                    isRequired = false,
                    leadingIcon = null
                )
            }
        )

        // نمایش علت غیرفعال‌سازی در صورت وجود در وضعیت INACTIVE
        if (uiState.organizationStatus == OrganizationStatus.INACTIVE && uiState.inactiveReason.isNotBlank()) {
            Spacer(modifier = Modifier.height(8.dp))
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
                        text = uiState.inactiveReason,
                        style = typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
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
    uiState: AddEditOrganizationUiState,
    onLandLineChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onEmailChange: (String) -> Unit
) {
    SectionTitle(
        title = stringResource(R.string.label_contact_information),
        titleColor = MaterialTheme.colorScheme.primary
    )

    TwoColumnRow(
        start = {
            CustomEditTextField(
                value = uiState.landLine,
                onValueChange = onLandLineChange,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = stringResource(R.string.label_organization_land_line),
                placeholder = stringResource(R.string.hint_enter_land_line),
                isRequired = false,
                leadingIcon = null
            )
        },
        end = {
            CustomEditTextField(
                value = uiState.phone,
                onValueChange = onPhoneChange,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = stringResource(R.string.label_phone),
                placeholder = stringResource(R.string.hint_enter_phone),
                isRequired = false,
                leadingIcon = null
            )
        }
    )

    CustomEditTextField(
        value = uiState.email,
        onValueChange = onEmailChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        label = stringResource(R.string.label_email),
        placeholder = stringResource(R.string.hint_enter_email),
        isRequired = false,
        leadingIcon = null
    )

}


@Composable
private fun OrganizationLocationSection(
    uiState: AddEditOrganizationUiState,
    cities: List<CityEntity>,
    regions: List<RegionEntity>,
    onCitySelected: (String) -> Unit,
    onRegionSelected: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onSelectLocation: () -> Unit
) {
    SectionTitle(
        title = stringResource(R.string.label_location),
        titleColor = MaterialTheme.colorScheme.primary
    )

    val selectedCity = cities.firstOrNull { it.cityId == uiState.cityId }
    val selectedRegion = regions.firstOrNull { it.regionId == uiState.regionId }

    TwoColumnRow(
        start = {
            DropdownSelectorField(
                value = selectedCity?.name.orEmpty(),
                label = stringResource(R.string.label_city),
                placeholder = stringResource(R.string.hint_choose_city),
                items = cities.mapNotNull { it.name },
                onItemSelected = { selectedName ->
                    cities.firstOrNull { it.name == selectedName }?.let {
                        onCitySelected(it.cityId)
                    }
                },
                isRequired = true
            )
        },
        end = {
            DropdownSelectorField(
                value = selectedRegion?.name.orEmpty(),
                label = stringResource(R.string.label_region),
                placeholder = stringResource(R.string.hint_choose_region),
                items = regions.mapNotNull { it.name },
                onItemSelected = { selectedName ->
                    regions.firstOrNull { it.name == selectedName }?.let {
                        onRegionSelected(it.regionId)
                    }
                },
                isRequired = true
            )
        }
    )

    CustomEditTextField(
        value = uiState.address,
        onValueChange = onAddressChange,
        label = stringResource(R.string.label_address),
        placeholder = stringResource(R.string.hint_enter_address),
        isRequired = false,
        leadingIcon = null
    )

    MapCard(
        latitude = uiState.latitude,
        longitude = uiState.longitude,
        onSelectLocation = onSelectLocation
    )
}


@Composable
fun OrganizationAnalysisSection(
    uiState: AddEditOrganizationUiState,
    onGradeChange: (Int) -> Unit,
    onTicketSaleCountChange: (String) -> Unit,
    onCustomerCapacityChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SectionTitle(
            title = stringResource(R.string.label_analytical_information),
            titleColor = MaterialTheme.colorScheme.primary
        )

        GradeSelector(
            grade = uiState.organizationGrade,
            onGradeChange = onGradeChange
        )

        TwoColumnRow(
            start = {
                CustomEditTextField(
                    value = uiState.ticketSaleCountHistory,
                    onValueChange = onTicketSaleCountChange,
                    label = stringResource(R.string.label_ticket_sale_count_history),
                    placeholder = "",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isRequired = false,
                    leadingIcon = null
                )
            },
            end = {
                CustomEditTextField(
                    value = uiState.customerCapacity,
                    onValueChange = onCustomerCapacityChange,
                    label = stringResource(R.string.label_customer_capacity),
                    placeholder = "",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isRequired = false,
                    leadingIcon = null
                )
            }
        )
    }
}


@Composable
private fun OrganizationStatusSection(
    uiState: AddEditOrganizationUiState,
    onExternalCustomerChange: (Boolean) -> Unit,
    onGetStandChange: (Boolean) -> Unit,
    onOrganizationStatusChange: (OrganizationStatus) -> Unit,

    ) {
    // وضعیت‌های سازمان
    val statusEntries = listOf(
        OrganizationStatus.INITIAL_REGISTRATION,
        OrganizationStatus.ACTIVE
    )
    val statusTitles = statusEntries.map { stringResource(it.titleRes) }

    SectionTitle(
        title = stringResource(R.string.label_statuses),
        titleColor = MaterialTheme.colorScheme.primary
    )
    if (!uiState.isEditMode) {
        DropdownSelectorField(
            value = stringResource(uiState.organizationStatus.titleRes),
            label = stringResource(R.string.label_status),
            placeholder = "",
            items = statusTitles,
            isRequired = false,
            onItemSelected = { selectedTitle ->
                val selectedIndex = statusTitles.indexOf(selectedTitle)
                if (selectedIndex != -1) {
                    onOrganizationStatusChange(statusEntries[selectedIndex])
                }
            }
        )
    }
    YesNoSwitchRow(
        title = stringResource(R.string.label_foreign_guest_reception),
        checked = uiState.statusExternalCustomer,
        onCheckedChange = onExternalCustomerChange
    )

    YesNoSwitchRow(
        title = stringResource(R.string.label_desire_receive_advertising_stands),
        checked = uiState.statusGetStand,
        onCheckedChange = onGetStandChange
    )
}


@Composable
private fun OrganizationDescriptionSection(
    description: String,
    onDescriptionChange: (String) -> Unit
) {
    CustomDescriptionField(
        label = stringResource(R.string.label_description),
        value = description,
        onValueChange = onDescriptionChange,
        placeholder = stringResource(R.string.hint_description)
    )
}


@Composable
private fun MapCard(
    latitude: Double?,
    longitude: Double?,
    onSelectLocation: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current

    val hasLocation = latitude != null && longitude != null && latitude != 0.0 && longitude != 0.0

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (!hasLocation) {
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
                        style = typography.titleSmall,
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
                        .height(160.dp)
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
                    style = typography.bodySmall,
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
    val lifecycleOwner = LocalLifecycleOwner.current

    val customMarkerDrawable = remember(context) {
        ContextCompat.getDrawable(context, R.drawable.ic_location_pin)
    }

    val point = remember(latitude, longitude) {
        GeoPoint(latitude, longitude)
    }

    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(false)
            isClickable = false
            setBuiltInZoomControls(false)
            controller.setZoom(16.5)
            controller.setCenter(point)

            val marker = Marker(this).apply {
                position = point
                customMarkerDrawable?.let { icon = it }
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                title = context.getString(R.string.label_organization_location)
                closeInfoWindow()
            }
            overlays.add(marker)
        }
    }

    DisposableEffect(lifecycleOwner, mapView) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_DESTROY -> mapView.onDetach()
                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            mapView.onPause()
            mapView.onDetach()
        }
    }

    AndroidView(
        modifier = modifier.clip(RoundedCornerShape(12.dp)),
        factory = { mapView },
        update = { view ->
            val marker = view.overlays.filterIsInstance<Marker>().firstOrNull()
            if (marker?.position?.latitude != latitude || marker.position?.longitude != longitude) {
                marker?.position = point
                // اطمینان از اعمال آیکون در آپدیت
                customMarkerDrawable?.let { marker?.icon = it }
                view.controller.setCenter(point)
                view.postInvalidate()
            }
        }
    )
}

