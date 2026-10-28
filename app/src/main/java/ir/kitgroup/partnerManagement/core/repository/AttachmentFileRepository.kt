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
import java.io.File
import javax.inject.Inject

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
            val mimeType = contentResolver.getType(uri) ?: "application/octet-stream"
            val extension = when {
                mimeType.contains("pdf", ignoreCase = true) -> ".pdf"
                mimeType.contains("png", ignoreCase = true) -> ".png"
                else -> ".jpg"
            }
            // تابع کمکی برای خواندن بایت‌ها یا تبدیل Uri به File
            val inputStream = context.contentResolver.openInputStream(uri)
            val fileBytes = inputStream?.readBytes() ?: throw Exception("Cannot read file")
            withContext(Dispatchers.IO) {
                inputStream.close()
            }
            val mediaType = mimeType.toMediaTypeOrNull() ?: "image/*".toMediaTypeOrNull()
            val requestFile = fileBytes.toRequestBody(mediaType)

            val timestamp = System.currentTimeMillis()
            val originalFileName = name ?: "file_$timestamp$extension"

            val finalFileName = if (!originalFileName.endsWith(extension, ignoreCase = true)) {
                "$originalFileName$extension"
            } else {
                originalFileName
            }
            val body = MultipartBody.Part.createFormData("File", finalFileName, requestFile)
            val orgIdBody = organizationId.toRequestBody("text/plain".toMediaTypeOrNull())
            val meetingIdBody = meetingId?.toRequestBody("text/plain".toMediaTypeOrNull())
            val fileTypeBody = fileType.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val nameBody = finalFileName.toRequestBody("text/plain".toMediaTypeOrNull())
            val descBody = description?.toRequestBody("text/plain".toMediaTypeOrNull())

            val response = apiService.uploadAttachment(
                file = body,
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

    suspend fun saveAttachmentsToDatabase(
        orgId: String,
        meetingId: String?,
        attachments: List<AttachmentFileDto>,
        baseUrl: String
    ) {
        // خواندن رکوردهای موجود بر اساس orgId و meetingId برای حفظ localPath
        val existingEntities = if (meetingId == null) {
            attachmentFileDao.getOrganizationAttachments(orgId)
        } else {
            attachmentFileDao.getMeetingAttachments(orgId, meetingId)
        }.associateBy { it.attachmentFileId }

        val entities = attachments.map { dto ->
            val existing = existingEntities[dto.attachmentFileId]
            AttachmentFileEntity(
                attachmentFileId = dto.attachmentFileId,
                organizationId = orgId,
                meetingId = meetingId,
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

