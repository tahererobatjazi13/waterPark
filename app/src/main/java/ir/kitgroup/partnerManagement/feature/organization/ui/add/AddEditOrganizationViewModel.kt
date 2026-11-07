package ir.kitgroup.partnerManagement.feature.organization.ui.add
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.dao.AttachmentFileDao
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationEntity
import ir.kitgroup.partnerManagement.core.network.model.AttachmentUploadResponse
import ir.kitgroup.partnerManagement.core.repository.OrganizationRepository
import ir.kitgroup.partnerManagement.core.repository.VisitorRepository
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
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationPersonEntity
import ir.kitgroup.partnerManagement.core.database.mapper.toDto
import ir.kitgroup.partnerManagement.core.repository.AttachmentFileRepository
import ir.kitgroup.partnerManagement.core.repository.OrganizationPersonRepository
import ir.kitgroup.partnerManagement.core.ui.util.PersonOrganizationRole
import ir.kitgroup.partnerManagement.core.ui.util.isNetworkError
import ir.kitgroup.partnerManagement.core.ui.util.mapDtosToImageItems
import ir.kitgroup.partnerManagement.feature.organization.ui.add.model.AddOrganizationPersonRequest

@HiltViewModel
class AddEditOrganizationViewModel @Inject constructor(
    private val repository: OrganizationRepository,
    private val attachmentFileRepository: AttachmentFileRepository,
    private val visitorRepository: VisitorRepository,
    private val attachmentFileDao: AttachmentFileDao,
    @ApplicationContext private val context: Context,
    private val personRepository: OrganizationPersonRepository,
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

            // ۱. واکشی اطلاعات پایه (شهرها و ویزیتورها)
            val cities = repository.observeAllCity().firstOrNull().orEmpty()
            val visitors = try {
                visitorRepository.getAllVisitors().firstOrNull().orEmpty()
            } catch (e: Exception) {
                emptyList()
            }
            val defaultVisitorId = mainPreferences.getVisitorId()

            _uiState.update { state ->
                state.copy(
                    cities = cities,
                    visitors = visitors,
                    visitorId = if (state.visitorId.isNullOrBlank()) defaultVisitorId else state.visitorId
                )
            }

            // ۲. لود جزئیات در حالت ویرایش
            if (isEditMode && !organizationIdArg.isNullOrBlank()) {
                val orgWithDetail = repository.observeById(organizationIdArg).firstOrNull()
                if (orgWithDetail != null) {
                    val org = orgWithDetail.organization
                    if (!org.cityId.isNullOrBlank()) loadRegions(org.cityId)

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
                            description = org.description.orEmpty()
                        )
                    }
                }

                // ۳. مدیریت پیوست‌ها (کش و سرور)
                val currentBaseUrl =
                    mainPreferences.baseUrlFlow.firstOrNull().orEmpty().trimEnd('/')

                // الف: نمایش فوری از دیتابیس محلی
                val cachedEntities = try {
                    attachmentFileDao.getOrganizationAttachments(organizationIdArg)
                } catch (e: Exception) {
                    emptyList()
                }
                if (cachedEntities.isNotEmpty()) {
                    _uiState.update {
                        it.copy(
                            images = mapDtosToImageItems(
                                context,
                                cachedEntities.map { e -> e.toDto() },
                                currentBaseUrl
                            )
                        )
                    }
                }

                // ب: دریافت از سرور و آپدیت نهایی
                val result = attachmentFileRepository.getAttachmentsByOrgId(organizationIdArg)
                val dtoList = result.getOrDefault(emptyList())

                if (dtoList.isNotEmpty()) {
                    // ذخیره در دیتابیس (با meetingId واقعی)
                    attachmentFileRepository.saveAttachmentsToDatabase(
                        orgId = organizationIdArg,
                        meetingId = null,
                        attachments = dtoList,
                        baseUrl = currentBaseUrl
                    )

                    // فیلتر فایل‌های مربوط به سازمان (آن‌هایی که meetingid نال یا خالی هستند)
                    val orgOnlyList = dtoList.filter { it.meetingid.isNullOrBlank() }

                    // آپدیت UI با دیتاهای تازه
                    _uiState.update {
                        it.copy(
                            images = mapDtosToImageItems(
                                context,
                                orgOnlyList,
                                currentBaseUrl
                            )
                        )
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
        val filteredValue = value.filter { it.isDigit() }.take(11)

        _uiState.update { state ->
            state.copy(
                mobile = filteredValue,
                mobileError = when {
                    filteredValue.isBlank() -> null
                    filteredValue.length < 11 -> context.getString(R.string.error_invalid_mobile)
                    isValidIranianMobile(filteredValue) -> null
                    else -> context.getString(R.string.error_invalid_mobile)
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

    fun onVisitorSelected(visitorId: String) {
        _uiState.update { it.copy(visitorId = visitorId) }
        visitorRepository.getAllVisitors()
    }

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
                    UiText.DynamicString(context.getString(R.string.error_invalid_mobile))
                )
            )
            return
        }
        // اعتبارسنجی‌های فرم...
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

                val targetOrgId = if (state.isEditMode && !state.organizationId.isNullOrBlank()) {
                    state.organizationId
                } else {
                    UUID.randomUUID().toString()
                }
                val visitorIdToSubmit =
                    uiState.value.visitorId.ifBlank { mainPreferences.getVisitorId() }

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
                    visitorId = visitorIdToSubmit,
                    statusExternalCustomer = state.statusExternalCustomer,
                    englishName = state.englishName.trim().takeIf { it.isNotEmpty() },
                    description = state.description.trim().takeIf { it.isNotEmpty() },
                    customerCapacity = state.customerCapacity.toIntOrNull() ?: 0,
                    email = state.email.trim().takeIf { it.isNotEmpty() },
                    ticketSaleCountHistory = state.ticketSaleCountHistory.toIntOrNull() ?: 0
                )

                val defaultPersonName = state.ownerName.ifBlank { state.organizationName }.trim()
                Log.d("requestrequest222", request.toString())

                if (state.isEditMode && !targetOrgId.isNullOrBlank()) {
                    // --- ویرایش سازمان ---
                    val existingOrgWithDetail = repository.observeById(targetOrgId).firstOrNull()
                    val existingOrg = existingOrgWithDetail?.organization

                    val syncResult = repository.updateOrganizationAndSync(request)

                    syncResult.fold(
                        onSuccess = { serverMsg ->
                            val msg = if (!serverMsg.isNullOrBlank()) {
                                UiText.DynamicString(serverMsg)
                            } else {
                                UiText.StringResource(R.string.msg_organization_updated)
                            }
                            _uiEvent.emit(UiEvent.ShowMessage(msg))
                            delay(400)
                            onSuccessCallback()
                        },
                        onFailure = { error ->
                            if (error.isNetworkError()) {
                                val nextSyncStatus =
                                    if (existingOrg?.syncStatus == SyncStatus.SYNCED) {
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
                                    visitorId = visitorIdToSubmit,
                                    statusExternalCustomer = request.statusExternalCustomer,
                                    englishName = request.englishName,
                                    description = request.description,
                                    customerCapacity = request.customerCapacity,
                                    email = request.email,
                                    ticketSaleCountHistory = request.ticketSaleCountHistory,
                                    modifiedOn = isoTimestamp,
                                    flagInsUp = true,
                                    syncStatus = nextSyncStatus,
                                    isLocallyModified = true,
                                    lastModifiedTimestamp = currentTimestamp
                                )

                                repository.saveLocal(offlineEntity)
                                _uiEvent.emit(UiEvent.ShowMessage(UiText.StringResource(R.string.msg_changes_saved_offline_synchronized_connection)))
                                delay(400)
                                onSuccessCallback()
                            } else {
                                _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(error)))
                            }
                        }
                    )
                } else {
                    // --- ثبت سازمان جدید ---
                    val result = repository.createOrganizationAndSync(request)

                    result.fold(
                        onSuccess = { response ->
                            val resolvedOrgId =
                                response.data?.takeIf { it.isNotBlank() } ?: targetOrgId


                            createDefaultPersonForOrganization(
                                organizationId = resolvedOrgId,
                                personName = defaultPersonName,
                                mobile = state.mobile.trim(),
                                isOnline = true
                            )

                            if (_uiState.value.images.isNotEmpty()) {
                                uploadImagesForOrganization(resolvedOrgId)
                            }

                            val msg = response.message
                                ?.takeIf { it.isNotBlank() }
                                ?.let { UiText.DynamicString(it) }
                                ?: UiText.StringResource(R.string.msg_organization_created)

                            _uiEvent.emit(UiEvent.ShowMessage(msg))
                            delay(400)
                            onSuccessCallback()
                        },
                        onFailure = { error ->
                            if (error.isNetworkError()) {

                                val offlineEntity = OrganizationEntity(
                                    organizationId = targetOrgId,
                                    serverId = null,
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
                                    visitorId = visitorIdToSubmit,
                                    statusExternalCustomer = request.statusExternalCustomer,
                                    englishName = request.englishName,
                                    description = request.description,
                                    customerCapacity = request.customerCapacity,
                                    email = request.email,
                                    ticketSaleCountHistory = request.ticketSaleCountHistory,
                                    modifiedOn = isoTimestamp,
                                    flagInsUp = false,
                                    syncStatus = SyncStatus.PENDING_INSERT,
                                    isLocallyModified = true,
                                    lastModifiedTimestamp = currentTimestamp
                                )

                                repository.saveLocal(offlineEntity)
                                createDefaultPersonForOrganization(
                                    organizationId = targetOrgId,
                                    personName = defaultPersonName,
                                    mobile = state.mobile.trim(),
                                    isOnline = false
                                )
                                _uiEvent.emit(UiEvent.ShowMessage(UiText.StringResource(R.string.msg_saved_local_storage_awaiting_synchronization)))
                                delay(400)
                                onSuccessCallback()
                            } else {
                                _uiEvent.emit(UiEvent.ShowError(AppErrorMapper.map(error)))
                            }
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

    private fun uploadFileForOrganization(
        fileUri: Uri,
        organizationId: String,
        fileType: Int,
        fileName: String?,
        description: String?
    ) {
        viewModelScope.launch {
            _uploadState.value = DataState.Loading
            val result = attachmentFileRepository.uploadAttachmentFile(
                context = context,
                uri = fileUri,
                organizationId = organizationId,
                meetingId = null,
                fileType = fileType,
                name = fileName,
                description = description
            )

            result.onSuccess { response ->
                _uploadState.value = DataState.Success(response)
                _uiEvent.emit(UiEvent.ShowMessage(UiText.StringResource(R.string.msg_file_uploaded_successfully)))
                attachmentFileRepository.upsertUploadedAttachment(
                    orgId = organizationId,
                    meetingId = null,
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

    fun onAddLocalAttachment(uri: Uri, fileType: Int, description: String) {
        val (originalFileName, extension) = getFileNameAndExtension(uri)

        val currentList = _uiState.value.images.toMutableList()
        currentList.add(
            OrganizationImageItem(
                uri = uri,
                fileType = fileType,
                description = description
            )
        )
        _uiState.update { it.copy(images = currentList) }

        val currentOrgId = _uiState.value.organizationId
        if (_uiState.value.isEditMode && !currentOrgId.isNullOrBlank()) {
            uploadFileForOrganization(
                fileUri = uri,
                organizationId = currentOrgId,
                fileType = fileType,
                fileName = originalFileName.ifBlank { "org_${System.currentTimeMillis()}$extension" },
                description = description
            )
        }
    }


    private fun getFileNameAndExtension(uri: Uri): Pair<String, String> {
        var fileName = ""
        // تلاش برای خواندن نام اصلی فایل از ContentResolver
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1 && cursor.moveToFirst()) {
                fileName = cursor.getString(nameIndex) ?: ""
            }
        }

        // استخراج پسوند از نام فایل
        var ext = fileName.substringAfterLast('.', "").lowercase().trim()

        // اگر از روی نام پیدا نشد، از روی MimeType شناسایی شود
        if (ext.isEmpty()) {
            val mimeType = context.contentResolver.getType(uri) ?: ""
            ext = when {
                mimeType.contains("wordprocessingml", ignoreCase = true) ||
                        mimeType.contains("msword", ignoreCase = true) -> "docx"
                mimeType.contains("pdf", ignoreCase = true) -> "pdf"
                mimeType.contains("png", ignoreCase = true) -> "png"
                mimeType.contains("jpeg", ignoreCase = true) || mimeType.contains("jpg", ignoreCase = true) -> "jpg"
                else -> MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: "jpg"
            }
        }

        val finalExtension = if (ext.startsWith(".")) ext else ".$ext"
        val finalName = if (fileName.isNotBlank()) fileName else "file_${System.currentTimeMillis()}$finalExtension"

        return Pair(finalName, finalExtension)
    }

    fun onRemoveImage(index: Int) {
        val currentList = _uiState.value.images.toMutableList()
        if (index in currentList.indices) {
            currentList.removeAt(index)
            _uiState.update { it.copy(images = currentList) }
        }
    }

    private suspend fun uploadImagesForOrganization(organizationId: String) {
        val imagesToUpload = _uiState.value.images.filter { it.uploadedFileId == null }

        for (img in imagesToUpload) {
            val uri = img.uri ?: continue

            val (originalFileName, extension) = getFileNameAndExtension(uri)
            val fileName = if (originalFileName.isNotBlank()) {
                originalFileName
            } else {
                "org_${System.currentTimeMillis()}_${img.fileType}$extension"
            }

            try {
                val uploadResult = attachmentFileRepository.uploadAttachmentFile(
                    context = context,
                    uri = uri,
                    organizationId = organizationId,
                    meetingId = null,
                    fileType = img.fileType,
                    name = fileName,
                    description = img.description
                )

                uploadResult.fold(
                    onSuccess = { uploadResponse ->
                        attachmentFileRepository.upsertUploadedAttachment(
                            orgId = organizationId,
                            meetingId = null,
                            response = uploadResponse,
                            fileType = img.fileType,
                            description = img.description
                        )
                    },
                    onFailure = { error ->
                        error.printStackTrace()
                    }
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun createDefaultPersonForOrganization(
        organizationId: String,
        personName: String,
        mobile: String?,
        isOnline: Boolean
    ) {
        if (personName.isBlank() && mobile.isNullOrBlank()) return

        val roleId = PersonOrganizationRole.MANAGER.id
        if (isOnline) {
            val personRequest = AddOrganizationPersonRequest(
                organizationId = organizationId,
                fullName = personName.ifBlank { "مدیر سازمان" },
                role = roleId,
                mobile = mobile?.takeIf { it.isNotBlank() },
                phone = null,
                gender = null,
                description = "ثبت خودکار همزمان با ایجاد سازمان",
                isCommissionEligible = false
            )

            // ارسال به سرور؛ در صورت خطا، در دیتابیس محلی ذخیره می‌شود
            val result = repository.addOrganizationPerson(personRequest)
            result.onFailure {
                savePersonOffline(
                    orgId = organizationId,
                    name = personName,
                    role = roleId,
                    mobile = mobile,
                )
            }
        } else {
            // حالت آفلاین
            savePersonOffline(
                orgId = organizationId,
                name = personName,
                role = roleId,
                mobile = mobile,
            )
        }
    }

    private suspend fun savePersonOffline(
        orgId: String,
        name: String,
        role: Int,
        mobile: String?,
    ) {
        val offlinePerson = OrganizationPersonEntity(
            orgPersonId = UUID.randomUUID().toString(),
            organizationId = orgId,
            fullName = name.ifBlank { "مدیر سازمان" },
            role = role,
            mobile = mobile.orEmpty(),
            gender = null,
            description = "ثبت خودکار آفلاین",
            isCommissionEligible = false,
            syncStatus = SyncStatus.PENDING_INSERT,
            isLocallyModified = true
        )
        personRepository.saveLocal(offlinePerson)
    }

}
