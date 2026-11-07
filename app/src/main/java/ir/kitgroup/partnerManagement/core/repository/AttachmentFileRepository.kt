package ir.kitgroup.partnerManagement.core.repository

import android.content.Context
import android.net.Uri
import ir.kitgroup.partnerManagement.core.database.dao.AttachmentFileDao
import ir.kitgroup.partnerManagement.core.database.entity.AttachmentFileEntity
import ir.kitgroup.partnerManagement.core.network.apiService.ApiService
import ir.kitgroup.partnerManagement.core.network.model.AttachmentFileDto
import ir.kitgroup.partnerManagement.core.network.model.AttachmentUploadResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class AttachmentFileRepository @Inject constructor(
    private val attachmentFileDao: AttachmentFileDao,
    private val apiService: ApiService
) {

    suspend fun uploadAttachmentFile(
        context: Context,
        uri: Uri,
        organizationId: String,
        meetingId: String?,
        fileType: Int,
        name: String?,
        description: String?
    ): Result<AttachmentUploadResponse> {
        return try {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(uri)
                ?: return Result.failure(Exception("Cannot read file"))
            val fileBytes = inputStream.readBytes()
            inputStream.close()

            val mimeType = contentResolver.getType(uri) ?: "application/octet-stream"

            val requestFile = fileBytes.toRequestBody(mimeType.toMediaTypeOrNull())

            val finalFileName = name?.takeIf { it.isNotBlank() } ?: "file_${System.currentTimeMillis()}"

            val fileBody = MultipartBody.Part.createFormData("File", finalFileName, requestFile)
            val orgIdBody = organizationId.toRequestBody("text/plain".toMediaTypeOrNull())
            val meetingIdBody = meetingId?.toRequestBody("text/plain".toMediaTypeOrNull())
            val fileTypeBody = fileType.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val nameBody = finalFileName.toRequestBody("text/plain".toMediaTypeOrNull())
            val descBody = description?.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = apiService.uploadAttachment(
                file = fileBody,
                name = nameBody,
                organizationId = orgIdBody,
                meetingId = meetingIdBody,
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
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val parent = outputFile.parentFile
            if (parent != null && !parent.exists() && !parent.mkdirs()) {
                return@withContext Result.failure(
                    IOException("امکان ایجاد پوشه ذخیره‌سازی وجود ندارد.")
                )
            }

            val response = apiService.downloadAttachment(fileId)

            if (!response.isSuccessful) {
                return@withContext Result.failure(HttpException(response))
            }

            val body = response.body()
                ?: return@withContext Result.failure(
                    IOException("محتوای فایل از سرور دریافت نشد.")
                )

            body.byteStream().use { inputStream ->
                outputFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
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

    suspend fun saveAttachmentsToDatabase(
        orgId: String,
        meetingId: String?,
        attachments: List<AttachmentFileDto>,
        baseUrl: String
    ) {
        // خواندن رکوردهای موجود برای حفظ localPath
        val existingEntities = try {
            attachmentFileDao.getOrganizationAttachments(orgId).associateBy { it.attachmentFileId }
        } catch (_: Exception) {
            emptyMap()
        }

        val entities = attachments.map { dto ->
            val existing = existingEntities[dto.attachmentFileId]

            val actualMeetingId = dto.meetingid?.takeIf { it.isNotBlank() } ?: meetingId

            AttachmentFileEntity(
                attachmentFileId = dto.attachmentFileId,
                organizationId = orgId,
                meetingId = actualMeetingId,
                fileType = dto.fileType,
                description = dto.description,
                fileUrl = dto.fileUrl
                    ?: "$baseUrl/api/OrgAttachmentFiles/download/${dto.attachmentFileId}",
                relativePath = dto.relativePath,
                mimeType = dto.mimeType,
                fileSize = dto.fileSize,
                createdOn = dto.createdOn,
                localPath = existing?.localPath,
                isHeader = (dto.fileType == 2)
            )
        }
        attachmentFileDao.upsertAll(entities)
    }


    suspend fun upsertUploadedAttachment(
        orgId: String,
        meetingId: String?,
        response: AttachmentUploadResponse,
        fileType: Int,
        description: String?
    ) {
        val entity = AttachmentFileEntity(
            attachmentFileId = response.attachmentFileId,
            organizationId = orgId,
            meetingId = meetingId,
            fileType = fileType,
            description = description,
            fileUrl = response.fileUrl,
            fileSize = response.fileSize,
            // localPath هنوز نداریم
        )
        attachmentFileDao.upsertAll(listOf(entity))
    }


}

