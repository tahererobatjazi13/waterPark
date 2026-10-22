package ir.kitgroup.partnerManagement.feature.organization.ui.add

import ir.kitgroup.partnerManagement.core.network.model.AttachmentFileDto
import ir.kitgroup.partnerManagement.core.ui.util.AttachmentFileType
import ir.kitgroup.partnerManagement.feature.organization.ui.detail.ImagePreviewDialog
import android.net.Uri
import androidx.compose.foundation.BorderStroke
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
import ir.kitgroup.partnerManagement.core.ui.components.CustomOutlinedButton
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField
import ir.kitgroup.partnerManagement.core.ui.components.GradeSelector
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.components.YesNoSwitchRow
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationLevel
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationStatus
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationType
import android.view.ViewGroup
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ir.kitgroup.partnerManagement.core.ui.model.OrganizationImageItem
import ir.kitgroup.partnerManagement.feature.organization.ui.detail.ImagePreviewDialog

@Composable
fun AddEditOrganizationScreen(
    onSuccessSave: () -> Unit,
    onSelectLocationClick: (() -> Unit)? = null,
    selectedLatitude: Double? = null,
    selectedLongitude: Double? = null,
    viewModel: AddEditOrganizationViewModel = hiltViewModel()
) {

    val baseUrl by viewModel.baseUrlFlow.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }


    // متغیرهای موقت برای نگهداری URI و نمایش دیالوگ تنظیمات تصویر
    var pendingImageUri by remember { mutableStateOf<Uri?>(null) }
    var showImageDetailDialog by remember { mutableStateOf(false) }

    LaunchedEffect(selectedLatitude, selectedLongitude) {
        selectedLatitude?.let {
            uiState.latitude = it
        }
        selectedLongitude?.let {
            uiState.longitude = it
        }
    }


    // لانچر انتخاب تصویر از گالری دستگاه
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            pendingImageUri = it
            showImageDetailDialog = true
        }
    }
    if (showImageDetailDialog && pendingImageUri != null) {
        ImageDetailInputDialog(
            onDismiss = {
                pendingImageUri = null
                showImageDetailDialog = false
            },
            onConfirm = { selectedFileType, description ->
                pendingImageUri?.let { uri ->
                    viewModel.onAddLocalImage(
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
                        enabled = uiState.isFormValid,
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
            baseUrl = baseUrl,
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
                imagePickerLauncher.launch("image/*")
            },
            onRemoveImageClick = viewModel::onRemoveImage,
        )
    }
}


@Composable
fun AddOrganizationContent(
    uiState: AddEditOrganizationUiState,
    baseUrl: String,
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
        OrganizationImagesSection(
            images = uiState.images,
            baseUrl = baseUrl,
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
                isRequired = false
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
                isRequired = false
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
fun OrganizationImagesSection(
    images: List<OrganizationImageItem>,
    baseUrl: String,
    onAddImage: () -> Unit,
    onRemoveImage: (Int) -> Unit
) {
    // نگهداری آدرس تصویر و مدل انتخابی جهت نمایش در ImagePreviewDialog
    var previewData by remember { mutableStateOf<Pair<String, AttachmentFileDto>?>(null) }

    // نمایش دیالوگ پیش‌نمایش
    previewData?.let { (imageUrl, attachmentDto) ->
        ImagePreviewDialog(
            imageUrl = imageUrl,
            attachment = attachmentDto,
            onDismiss = { previewData = null }
        )
    }

    SectionTitle(
        title = stringResource(R.string.label_images),
        titleColor = MaterialTheme.colorScheme.primary
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        item {
            AddImageCard(onClick = onAddImage)
        }

        itemsIndexed(images) { index, item ->
            // استخراج آدرس کامل تصویر (چه Uri لوکال و چه شناسه سرور)
            val resolvedImageUrl = item.uri?.toString()
                ?: item.serverImageUrl
                ?: item.uploadedFileId?.let { id ->
                    if (baseUrl.isNotBlank()) "${baseUrl.trimEnd('/')}/api/OrgAttachmentFiles/download/$id" else null
                }.orEmpty()

            val imageModel = item.uri ?: resolvedImageUrl

            val fileTypeEnum = AttachmentFileType.fromId(item.fileType)
            val fileTypeTitle = fileTypeEnum?.titleRes?.let { stringResource(it) }
                ?: stringResource(R.string.attachment_type_other_images)

            Box(
                modifier = Modifier
                    .size(130.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {

                        val dto = AttachmentFileDto(
                            attachmentFileId = item.uploadedFileId ?: "",
                            fileType = item.fileType,
                            name = null,
                            description = item.description
                        )
                        previewData = Pair(resolvedImageUrl, dto)
                    }
            ) {
                // نمایش بندانگشتی تصویر با Coil
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageModel)
                        .crossfade(true)
                        .placeholder(R.drawable.ic_image_placeholder)
                        .error(R.drawable.ic_image_placeholder)
                        .build(),
                    contentDescription = item.description,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // برچسب نوع عکس (Badge)
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    color = Color.Black.copy(alpha = 0.65f)
                ) {
                    Text(
                        text = fileTypeTitle,
                        style = typography.labelSmall,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier.padding(vertical = 3.dp, horizontal = 4.dp)
                    )
                }

                // دکمه حذف تصویر
                IconButton(
                    onClick = { onRemoveImage(index) },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp)
                        .size(20.dp)
                        .background(Color.Red.copy(alpha = 0.85f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.label_delete),
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
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
            /* Text(
                 text = stringResource(R.string.label_maximum_10_images),
                 style = typography.labelSmall,
                 color = appColors.border,
                 textAlign = TextAlign.Center
             )*/
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