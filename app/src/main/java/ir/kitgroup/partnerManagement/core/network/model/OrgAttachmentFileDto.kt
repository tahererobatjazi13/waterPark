package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class OrgAttachmentFileDto(
    val orgAttachmentFileId: String,

    val name: String? = null,

    val organizationId: String? = null,

    val fileType: Int? = null,

    val originalFileName: String? = null,

    val storedFileName: String? = null,

    val relativePath: String? = null,

    val fileExtension: String? = null,

    val mimeType: String? = null,

    val fileSize: Int? = null,

    val fileUrl: String? = null,

    val fileHash: String? = null,

    val storageStatus: Int? = null,

    val description: String? = null,

    val sourceCreate: Int? = null,

    val meetingId: String? = null
)