package ir.kitgroup.partnerManagement.core.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.dao.AttachmentFileDao
import ir.kitgroup.partnerManagement.core.database.dao.CityDao
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationChangeStatusDao
import ir.kitgroup.partnerManagement.core.database.dao.OrganizationDao
import ir.kitgroup.partnerManagement.core.database.dao.RegionDao
import ir.kitgroup.partnerManagement.core.database.entity.AttachmentFileEntity
import ir.kitgroup.partnerManagement.core.database.entity.CityEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationChangeStatusEntity
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationEntity
import ir.kitgroup.partnerManagement.core.database.entity.RegionEntity
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import ir.kitgroup.partnerManagement.core.network.apiService.ApiService
import ir.kitgroup.partnerManagement.core.network.exception.ApiRequestExecutor
import ir.kitgroup.partnerManagement.core.network.model.AttachmentFileDto
import ir.kitgroup.partnerManagement.core.network.model.AttachmentUploadResponse
import ir.kitgroup.partnerManagement.core.network.model.ModelResponse
import ir.kitgroup.partnerManagement.core.network.model.TicketIssueLineDto
import ir.kitgroup.partnerManagement.core.network.model.VisitorOrganizationDto
import ir.kitgroup.partnerManagement.core.ui.util.SyncStatus
import ir.kitgroup.partnerManagement.feature.contract.model.TerminateActiveContractRequest
import ir.kitgroup.partnerManagement.feature.dashboard.repository.SyncRepository
import ir.kitgroup.partnerManagement.feature.organization.model.ChangeStatusOrganizationRequest
import ir.kitgroup.partnerManagement.feature.organization.ui.add.model.AddOrganizationPersonRequest
import ir.kitgroup.partnerManagement.feature.organization.ui.add.model.CreateOrganizationRequest
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class OrganizationRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val organizationDao: OrganizationDao,
    private val cityDao: CityDao,
    private val regionDao: RegionDao,
    private val attachmentFileDao: AttachmentFileDao,
    private val organizationChangeStatusDao: OrganizationChangeStatusDao,
    private val apiService: ApiService,
    private val syncRepository: SyncRepository
) {

    fun observeAll() = organizationDao.observeAll()

    fun observeById(
        organizationId: String
    ): Flow<OrganizationWithDetail?> {
        return organizationDao.observeById(organizationId)
    }

    suspend fun upsertAll(
        items: List<OrganizationEntity>
    ) {
        organizationDao.upsertAll(items)
    }

    suspend fun upsert(
        item: OrganizationEntity
    ) {
        organizationDao.upsert(item)
    }

    suspend fun deleteAll() {
        organizationDao.deleteAll()
    }

    fun observeAllCity(): Flow<List<CityEntity>> {
        return cityDao.observeAll()
    }

    fun observeRegionByCityId(
        cityId: String
    ): Flow<List<RegionEntity>> {
        return regionDao.observeByCity(cityId)
    }

    suspend fun saveLocal(organization: OrganizationEntity) {
        organizationDao.upsert(organization)
    }


    suspend fun createOrganizationAndSync(request: CreateOrganizationRequest): Result<ModelResponse> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_organization_create)
        ) {
            apiService.createOrganization(request.copy(organizationId = null))
        }.fold(
            onSuccess = { response ->
                syncRepository.syncData()
                Result.success(
                    ModelResponse(
                        success = response.success,
                        message = response.message,
                        data = response.data,
                    )
                )

            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }

    suspend fun updateOrganizationAndSync(request: CreateOrganizationRequest): Result<String?> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_organization_update)
        ) {
            apiService.updateOrganization(request)
        }.fold(
            onSuccess = { response ->
                if (!request.organizationId.isNullOrBlank()) {
                    organizationDao.updateSyncStatus(
                        organizationId = request.organizationId,
                        status = SyncStatus.SYNCED,
                        isLocallyModified = false
                    )
                }
                syncRepository.syncData()
                Result.success(response.message)
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }

    suspend fun changeStatusOrganization(
        request: ChangeStatusOrganizationRequest
    ): Result<String?> {
        return ApiRequestExecutor.execute(
            defaultMessage = context.getString(R.string.error_organization_change_status)
        ) {
            apiService.changeStatusOrganization(request)
        }.fold(
            onSuccess = { response ->

                syncRepository.syncData()
                Result.success(response.message)
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
    suspend fun saveOfflineOrganizationStatusChange(
        organizationId: String,
        statusOrg: Int,
        deactiveDate: String?,
        reason: String?,
        visitDate: String?,
        subjectVisitId: String?
    ) {
        organizationChangeStatusDao.insert(
            OrganizationChangeStatusEntity(
                organizationId = organizationId,
                statusOrg = statusOrg,
                deactiveDate = deactiveDate!!,
                reason = reason!!,
                visitDate = visitDate!!,
                subjectVisitId = subjectVisitId!!
            )
        )

     /*   organizationDao.updateOrganizationStatusLocally(
            organizationId = organizationId,
            statusOrg = statusOrg,
            deactiveDate = deactiveDate,
            reason = reason,
            visitDate = visitDate,
            subjectVisitId = subjectVisitId
        )*/
    }


    suspend fun getOrganizationVisitors(
        organizationId: String
    ): Result<List<VisitorOrganizationDto>> {
        return try {
            val response = apiService.getOrganizationVisitors(organizationId)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string()
                    ?: "خطا در دریافت اطلاعات. کد: ${response.code()}"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getOrganizationTicketIssue(
        organizationId: String
    ): Result<List<TicketIssueLineDto>> {
        return try {
            val response = apiService.getOrganizationTicketIssue(organizationId)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string()
                    ?: "خطا در دریافت اطلاعات. کد: ${response.code()}"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addOrganizationPerson(
        request: AddOrganizationPersonRequest
    ): Result<ModelResponse> {
        return try {

            val response = apiService.addOrganizationPerson(request)

            if (response.isSuccessful) {

                val body = response.body()

                if (body != null) {

                    Log.d(
                        "AddOrganizationPerson",
                        "response = $body"
                    )

                    if (body.success) {
                        Result.success(body)
                    } else {
                        Result.failure(
                            Exception(
                                body.message
                            )
                        )
                    }

                } else {
                    Result.failure(
                        Exception("پاسخ خالی از سرور دریافت شد")
                    )
                }

            } else {

                val errorMessage =
                    response.errorBody()?.string()
                        ?.takeIf { it.isNotBlank() }
                        ?: "خطای سرور: ${response.code()}"

                Log.e(
                    "AddOrganizationPerson",
                    "HTTP ${response.code()} - $errorMessage"
                )

                Result.failure(
                    Exception(errorMessage)
                )
            }

        } catch (e: Exception) {

            Log.e(
                "AddOrganizationPerson",
                "Exception",
                e
            )

            Result.failure(
                Exception(
                    e.localizedMessage
                        ?: "خطا در برقراری ارتباط با سرور",
                    e
                )
            )
        }
    }

    suspend fun uploadOrganizationFile(
        context: Context,
        uri: Uri,
        organizationId: String,
        fileType: Int,
        name: String?,
        description: String?
    ): Result<AttachmentUploadResponse> {
        return try {
            // تابع کمکی برای خواندن بایت‌ها یا تبدیل Uri به File
            val inputStream = context.contentResolver.openInputStream(uri)
            val fileBytes = inputStream?.readBytes() ?: throw Exception("Cannot read file")
            inputStream.close()

            val requestFile = fileBytes.toRequestBody("image/*".toMediaTypeOrNull())
            val fileName = "image_${System.currentTimeMillis()}.jpg"

            val body = MultipartBody.Part.createFormData("File", fileName, requestFile)
            val orgIdBody = organizationId.toRequestBody("text/plain".toMediaTypeOrNull())
            val fileTypeBody = fileType.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val nameBody = (name ?: fileName).toRequestBody("text/plain".toMediaTypeOrNull())
            val descBody = description?.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = apiService.uploadAttachment(
                file = body,
                name = nameBody,
                organizationId = orgIdBody,
                meetingId = null,
                fileType = fileTypeBody,
                description = descBody
            )

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun downloadAndSaveFile(
        fileId: String,
        outputFile: File
    ): Result<Unit> {
        return try {
            val response = apiService.downloadAttachment(fileId)
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                body.byteStream().use { inputStream ->
                    outputFile.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception("Download failed with status: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getAttachmentsByOrgId(organizationId: String): Result<List<AttachmentFileDto>> {
        return try {
            val response = apiService.getOrganizationAttachments(organizationId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("خطا در دریافت پیوست‌ها: کد ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveAttachmentsToDb(
        orgId: String,
        attachments: List<AttachmentFileDto>,
        baseUrl: String
    ) {
        val entities = attachments.map { dto ->
            AttachmentFileEntity(
                attachmentFileId = dto.attachmentFileId,
                organizationId = orgId,
                fileType = dto.fileType,
                description = dto.description,
                fileUrl = dto.fileUrl
                    ?: "$baseUrl/api/OrgAttachmentFiles/download/${dto.attachmentFileId}",
                relativePath = dto.relativePath,
                mimeType = dto.mimeType,
                fileSize = dto.fileSize,
                createdOn = dto.createdOn,
                // localPath فعلاً null
                isHeader = (dto.fileType == 2)
            )
        }
        attachmentFileDao.upsertAll(entities)
    }
    suspend fun upsertUploadedAttachment(
        orgId: String,
        response: AttachmentUploadResponse,
        fileType: Int,
        description: String?
    ) {
        val entity = AttachmentFileEntity(
            attachmentFileId = response.attachmentFileId,
            organizationId = orgId,
            fileType = fileType,
            description = description,
            fileUrl = response.fileUrl,
            fileSize = response.fileSize,
            // localPath هنوز نداریم
        )
        attachmentFileDao.upsertAll(listOf(entity))
    }

}