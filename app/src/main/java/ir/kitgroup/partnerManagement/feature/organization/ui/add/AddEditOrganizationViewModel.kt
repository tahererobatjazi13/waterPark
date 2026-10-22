package ir.kitgroup.partnerManagement.feature.organization.ui.add

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationEntity
import ir.kitgroup.partnerManagement.core.network.model.AttachmentUploadResponse
import ir.kitgroup.partnerManagement.core.repository.OrganizationRepository
import ir.kitgroup.partnerManagement.core.ui.model.OrganizationImageItem
import ir.kitgroup.partnerManagement.core.ui.util.AppErrorMapper
import ir.kitgroup.partnerManagement.core.ui.util.DataState
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationStatus
import ir.kitgroup.partnerManagement.core.ui.util.SyncStatus
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.UiText
import ir.kitgroup.partnerManagement.core.ui.util.datastore.MainPreferences
import ir.kitgroup.partnerManagement.core.ui.util.isValidIranianMobile
import ir.kitgroup.partnerManagement.feature.organization.ui.add.model.CreateOrganizationRequest
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class AddEditOrganizationViewModel @Inject constructor(
    private val repository: OrganizationRepository,
    @ApplicationContext private val context: Context,
    private val mainPreferences: MainPreferences,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val organizationIdArg: String? = savedStateHandle["organizationId"]
    val isEditMode: Boolean = !organizationIdArg.isNullOrBlank()

    val baseUrlFlow: StateFlow<String> = mainPreferences.baseUrlFlow
        .map { it?.trim()?.trimEnd('/').orEmpty() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ""
        )

    private val _uiState = MutableStateFlow(
        AddEditOrganizationUiState(
            organizationId = organizationIdArg,
            isEditMode = isEditMode
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>(extraBufferCapacity = 1)
    val uiEvent = _uiEvent.asSharedFlow()

    private var regionsJob: Job? = null

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isInitialLoading = true) }

            // واکشی لیست شهرها
            val cities = repository.observeAllCity().firstOrNull().orEmpty()
            _uiState.update { it.copy(cities = cities) }

            // در صورتی که ویرایش باشد، لود کردن جزئیات سازمان
            if (isEditMode && !organizationIdArg.isNullOrBlank()) {
                val orgWithDetail = repository.observeById(organizationIdArg).firstOrNull()
                if (orgWithDetail != null) {
                    val org = orgWithDetail.organization

                    // لود منطقه‌های متناظر با شهر
                    if (!org.cityId.isNullOrBlank()) {
                        loadRegions(org.cityId)
                    }

                    _uiState.update { current ->
                        current.copy(
                            organizationName = org.name.orEmpty(),
                            organizationType = org.organizationType ?: 0,
                            organizationLevel = org.level ?: 0,
                            organizationStatus = OrganizationStatus.fromId(org.status),
                            ownerName = org.ownerName.orEmpty(),
                            englishName = org.englishName.orEmpty(),
                            nationalId = org.nationalId.orEmpty(),
                            landLine = org.landLine.orEmpty(),
                            phone = org.phone.orEmpty(),
                            mobile = org.mobile.orEmpty(),
                            email = org.email.orEmpty(),
                            address = org.address.orEmpty(),
                            cityId = org.cityId.orEmpty(),
                            regionId = org.regionId.orEmpty(),
                            latitude = org.latitude,
                            longitude = org.longitude,
                            organizationGrade = org.grade ?: 1,
                            ticketSaleCountHistory = org.ticketSaleCountHistory?.toString() ?: "",
                            customerCapacity = org.customerCapacity?.toString() ?: "",
                            statusExternalCustomer = org.statusExternalCustomer == true,
                            statusGetStand = org.statusGetStand == true,
                            description = org.document.orEmpty(),
                            //   organizationPersons = orgWithDetail.organization.persons.map { it.toPersonEntity() }
                        )
                    }
                    val currentBaseUrl =
                        mainPreferences.baseUrlFlow.firstOrNull().orEmpty().trimEnd('/')

                    val attachmentsResult = repository.getAttachmentsByOrgId(organizationIdArg)
                    attachmentsResult.onSuccess { attachments ->
                        val loadedImages = attachments.map { attachment ->
                            val fullUrl =
                                "$currentBaseUrl/api/OrgAttachmentFiles/download/${attachment.attachmentFileId}"
                            OrganizationImageItem(
                                uri = null,
                                fileType = attachment.fileType ,
                                description = attachment.description.orEmpty(),
                                uploadedFileId = attachment.attachmentFileId,
                                serverImageUrl = fullUrl
                            )
                        }

                        _uiState.update { it.copy(images = loadedImages) }
                    }
                }
            }

            _uiState.update { it.copy(isInitialLoading = false) }
        }
    }


    // متدهای دریافت تغییرات فرم UI
    fun onOrganizationNameChange(value: String) =
        _uiState.update { it.copy(organizationName = value) }

    fun onOrganizationTypeChange(value: Int) = _uiState.update { it.copy(organizationType = value) }
    fun onOrganizationLevelChange(value: Int) =
        _uiState.update { it.copy(organizationLevel = value) }

    fun onOrganizationStatusChange(value: OrganizationStatus) =
        _uiState.update { it.copy(organizationStatus = value) }

    fun onOwnerNameChange(value: String) = _uiState.update { it.copy(ownerName = value) }
    fun onEnglishNameChange(value: String) = _uiState.update { it.copy(englishName = value) }
    fun onNationalIdChange(value: String) = _uiState.update { it.copy(nationalId = value) }
    fun onLandLineChange(value: String) = _uiState.update { it.copy(landLine = value) }
    fun onPhoneChange(value: String) = _uiState.update { it.copy(phone = value) }
    fun onMobileChange(value: String) {
        _uiState.update { state ->
            state.copy(
                mobile = value,
                mobileError = if (value.isBlank() || isValidIranianMobile(value)) {
                    null
                } else {
                    context.getString(R.string.error_invalid_mobile)
                }
            )
        }
    }

    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value) }
    fun onAddressChange(value: String) = _uiState.update { it.copy(address = value) }
    fun onOrganizationGradeChange(value: Int) =
        _uiState.update { it.copy(organizationGrade = value) }

    fun onTicketSaleCountChange(value: String) =
        _uiState.update { it.copy(ticketSaleCountHistory = value) }

    fun onCustomerCapacityChange(value: String) =
        _uiState.update { it.copy(customerCapacity = value) }

    fun onStatusExternalCustomerChange(value: Boolean) =
        _uiState.update { it.copy(statusExternalCustomer = value) }

    fun onStatusGetStandChange(value: Boolean) = _uiState.update { it.copy(statusGetStand = value) }
    fun onDescriptionChange(value: String) = _uiState.update { it.copy(description = value) }
    fun onLocationChange(lat: Double?, lng: Double?) =
        _uiState.update { it.copy(latitude = lat, longitude = lng) }

    fun onCitySelected(cityId: String) {
        _uiState.update { it.copy(cityId = cityId, regionId = "") }
        loadRegions(cityId)
    }

    fun onRegionSelected(regionId: String) {
        _uiState.update { it.copy(regionId = regionId) }
    }

    private fun loadRegions(cityId: String) {
        regionsJob?.cancel()
        _uiState.update { it.copy(regions = emptyList()) }
        if (cityId.isBlank()) return

        regionsJob = viewModelScope.launch {
            repository.observeRegionByCityId(cityId).collect { regionsList ->
                _uiState.update { it.copy(regions = regionsList) }
            }
        }
    }

    private fun getCurrentIsoUtcTimestamp(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return dateFormat.format(Date())
    }

    /**
     * ثبت و ویرایش سازمان با معماری آنلاین / آفلاین
     */
    fun submitOrganization(onSuccessCallback: () -> Unit) {
        val state = _uiState.value
        if (state.isLoading) return

        if (!isValidIranianMobile(state.mobile)) {
            _uiEvent.tryEmit(
                UiEvent.ShowError(
                    UiText.DynamicString(
                        context.getString(R.string.error_invalid_mobile)
                    )
                )
            )
            return
        }
        // اعتبارسنجی فرم
        if (state.organizationName.isBlank()) {
            _uiEvent.tryEmit(UiEvent.ShowError(UiText.StringResource(R.string.label_organization_name_required)))
            return
        }
        if (state.organizationType == 0) {
            _uiEvent.tryEmit(UiEvent.ShowError(UiText.StringResource(R.string.label_organization_type_required)))
            return
        }
        if (state.cityId.isBlank()) {
            _uiEvent.tryEmit(UiEvent.ShowError(UiText.StringResource(R.string.label_city_name_required)))
            return
        }
        if (state.regionId.isBlank()) {
            _uiEvent.tryEmit(UiEvent.ShowError(UiText.StringResource(R.string.label_region_name_required)))
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val currentTimestamp = System.currentTimeMillis()
                val isoTimestamp = getCurrentIsoUtcTimestamp()
                val targetOrgId = state.organizationId
                val currentVisitorId = mainPreferences.getVisitorId()

                val request = CreateOrganizationRequest(
                    organizationId = targetOrgId,
                    name = state.organizationName.trim(),
                    ownerName = state.ownerName.trim().takeIf { it.isNotEmpty() },
                    nationalId = state.nationalId.trim().takeIf { it.isNotEmpty() },
                    organizationType = state.organizationType,
                    grade = state.organizationGrade,
                    statusGetStand = state.statusGetStand,
                    level = state.organizationLevel,
                    status = state.organizationStatus.id,
                    phone = state.phone.trim().takeIf { it.isNotEmpty() },
                    landLine = state.landLine.trim().takeIf { it.isNotEmpty() },
                    mobile = state.mobile.trim().takeIf { it.isNotEmpty() },
                    address = state.address.trim().takeIf { it.isNotEmpty() },
                    cityId = state.cityId,
                    regionId = state.regionId,
                    latitude = state.latitude,
                    longitude = state.longitude,
                    visitorId =currentVisitorId,
                    statusExternalCustomer = state.statusExternalCustomer,
                    englishName = state.englishName.trim().takeIf { it.isNotEmpty() },
                    document = state.description.trim().takeIf { it.isNotEmpty() },
                    customerCapacity = state.customerCapacity.toIntOrNull() ?: 0,
                    email = state.email.trim().takeIf { it.isNotEmpty() },
                    ticketSaleCountHistory = state.ticketSaleCountHistory.toIntOrNull() ?: 0
                )

                if (state.isEditMode && !targetOrgId.isNullOrBlank()) {
                    // --- ویرایش سازمان ---
                    val existingOrgWithDetail = repository.observeById(targetOrgId).firstOrNull()
                    val existingOrg = existingOrgWithDetail?.organization

                    val syncResult = repository.updateOrganizationAndSync(request)

                    syncResult.fold(
                        onSuccess = { serverMsg ->

                            val msg =
                                if (!serverMsg.isNullOrBlank()) UiText.DynamicString(serverMsg)
                                else UiText.StringResource(R.string.msg_organization_updated)
                            _uiEvent.emit(UiEvent.ShowMessage(msg))
                            delay(400)
                            onSuccessCallback()
                        },
                        onFailure = {
                            val nextSyncStatus = if (existingOrg?.syncStatus == SyncStatus.SYNCED) {
                                SyncStatus.PENDING_UPDATE
                            } else {
                                existingOrg?.syncStatus ?: SyncStatus.PENDING_INSERT
                            }

                            val offlineEntity = OrganizationEntity(
                                organizationId = targetOrgId,
                                name = request.name,
                                ownerName = request.ownerName,
                                nationalId = request.nationalId,
                                organizationType = request.organizationType,
                                grade = request.grade,
                                statusGetStand = request.statusGetStand,
                                level = request.level,
                                status = request.status,
                                phone = request.phone,
                                landLine = request.landLine,
                                mobile = request.mobile,
                                address = request.address,
                                cityId = request.cityId,
                                regionId = request.regionId,
                                latitude = request.latitude,
                                longitude = request.longitude,
                                visitorId =currentVisitorId,
                                statusExternalCustomer = request.statusExternalCustomer,
                                englishName = request.englishName,
                                document = request.document,
                                customerCapacity = request.customerCapacity,
                                email = request.email,
                                ticketSaleCountHistory = request.ticketSaleCountHistory,
                                modifiedOn = isoTimestamp,
                                syncStatus = nextSyncStatus,
                                isLocallyModified = true,
                                lastModifiedTimestamp = currentTimestamp
                            )

                            repository.saveLocal(offlineEntity)
                            _uiEvent.emit(UiEvent.ShowMessage(UiText.StringResource(R.string.msg_changes_saved_offline_synchronized_connection)))
                            delay(400)
                            onSuccessCallback()
                        }
                    )
                } else {
                    // --- ثبت سازمان جدید ---
                    val result = repository.createOrganizationAndSync(request)

                    result.fold(
                        onSuccess = { response ->
                            val newOrganizationId = response.data
                            if (newOrganizationId.isNullOrBlank()) {
                                _uiEvent.emit(
                                    UiEvent.ShowError(
                                        UiText.DynamicString("شناسه سازمان از سمت سرور دریافت نشد.")
                                    )
                                )
                                return@fold
                            }

                            // ۱. ابتدا منتظر می‌مانیم تا تمام عکس‌ها آپلود شوند
                            if (_uiState.value.images.isNotEmpty()) {
                                uploadImagesForNewOrganization(newOrganizationId)
                            }

                            // ۲. نمایش پیام موفقیت پس از آپلود کامل
                            val msg = response.message
                                ?.takeIf { it.isNotBlank() }
                                ?.let { UiText.DynamicString(it) }
                                ?: UiText.StringResource(R.string.msg_organization_created)

                            _uiEvent.emit(UiEvent.ShowMessage(msg))

                            // ۳. اکنون با خیال راحت صفحه بسته می‌شود
                            delay(400)
                            onSuccessCallback()
                        }, onFailure = {
                            val localUUID = UUID.randomUUID().toString()

                            val offlineEntity = OrganizationEntity(
                                organizationId = localUUID,
                                name = request.name,
                                ownerName = request.ownerName,
                                nationalId = request.nationalId,
                                organizationType = request.organizationType,
                                grade = request.grade,
                                statusGetStand = request.statusGetStand,
                                level = request.level,
                                status = request.status,
                                phone = request.phone,
                                landLine = request.landLine,
                                mobile = request.mobile,
                                address = request.address,
                                cityId = request.cityId,
                                regionId = request.regionId,
                                latitude = request.latitude,
                                longitude = request.longitude,
                                visitorId =currentVisitorId,
                                statusExternalCustomer = request.statusExternalCustomer,
                                englishName = request.englishName,
                                document = request.document,
                                customerCapacity = request.customerCapacity,
                                email = request.email,
                                ticketSaleCountHistory = request.ticketSaleCountHistory,
                                modifiedOn = isoTimestamp,
                                syncStatus = SyncStatus.PENDING_INSERT,
                                isLocallyModified = true,
                                lastModifiedTimestamp = currentTimestamp
                            )

                            repository.saveLocal(offlineEntity)
                            _uiEvent.emit(UiEvent.ShowMessage(UiText.StringResource(R.string.msg_saved_local_storage_awaiting_synchronization)))
                            delay(400)
                            onSuccessCallback()
                        }
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(e)))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private val _uploadState =
        MutableStateFlow<DataState<AttachmentUploadResponse>>(DataState.Idle)
    val uploadState = _uploadState.asStateFlow()

    fun uploadFileForOrganization(
        fileUri: Uri,
        organizationId: String,
        fileType: Int,
        fileName: String?,
        description: String?
    ) {
        viewModelScope.launch {
            _uploadState.value = DataState.Loading
            val result = repository.uploadOrganizationFile(
                context = context,
                uri = fileUri,
                organizationId = organizationId,
                fileType = fileType,
                name = fileName,
                description = description
            )

            result.onSuccess { response ->
                _uploadState.value = DataState.Success(response)
                _uiEvent.emit(UiEvent.ShowMessage(UiText.StringResource(R.string.msg_file_uploaded_successfully)))
                repository.upsertUploadedAttachment(
                    orgId = organizationId,
                    response = response,
                    fileType = fileType,
                    description = description
                )
            }.onFailure { error ->
                _uploadState.value = DataState.Error(
                    error.localizedMessage?.let { UiText.DynamicString(it) }
                        ?: UiText.StringResource(R.string.error_unknown)
                )
            }
        }
    }

    fun onAddLocalImage(uri: Uri, fileType: Int, description: String) {
        val currentList = _uiState.value.images.toMutableList()
        currentList.add(
            OrganizationImageItem(
                uri = uri,
                fileType = fileType,
                description = description
            )
        )
        _uiState.update { it.copy(images = currentList) }

        // اگر در حالت ویرایش هستیم و organizationId سروری داریم، می‌توانیم بلافاصله آپلود کنیم:
        val currentOrgId = _uiState.value.organizationId
        if (_uiState.value.isEditMode && !currentOrgId.isNullOrBlank()) {
            uploadFileForOrganization(
                fileUri = uri,
                organizationId = currentOrgId,
                fileType = fileType,
                fileName = "org_${System.currentTimeMillis()}.jpg",
                description = description
            )
        }
    }

    fun onRemoveImage(index: Int) {
        val currentList = _uiState.value.images.toMutableList()
        if (index in currentList.indices) {
            currentList.removeAt(index)
            _uiState.update { it.copy(images = currentList) }
        }
    }

    private suspend fun uploadImagesForNewOrganization(organizationId: String) {
        val imagesToUpload = _uiState.value.images.filter { it.uploadedFileId == null }

        for (img in imagesToUpload) {
            val fileName = "org_${System.currentTimeMillis()}_${img.fileType}.jpg"

            repository.uploadOrganizationFile(
                context = context,
                uri = img.uri!!,
                organizationId = organizationId,
                fileType = img.fileType,
                name = fileName,
                description = img.description
            ).fold(
                onSuccess = { uploadResponse ->
                    // در صورت نیاز به ذخیره شناسه فایل آپلود شده
                },
                onFailure = { error ->
                    // لاگ خطا در صورت بروز مشکل در آپلود هر تصویر
                    error.printStackTrace()
                }
            )
        }
    }


}
