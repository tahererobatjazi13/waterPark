package ir.kitgroup.partnerManagement.core.network.model

import com.google.gson.annotations.SerializedName

data class AttachmentFileDto(
    @SerializedName("attachmentFileId")
    val attachmentFileId: String,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("originalFileName")
    val originalFileName: String? = null,

    @SerializedName("storedFileName")
    val storedFileName: String? = null,

    @SerializedName("relativePath")
    val relativePath: String? = null,

    @SerializedName("fileExtension")
    val fileExtension: String? = null,

    @SerializedName("mimeType")
    val mimeType: String? = null,

    @SerializedName("fileSize")
    val fileSize: Long = 0L,

    @SerializedName("fileUrl")
    val fileUrl: String? = null,

    @SerializedName("fileType")
    val fileType: Int = 0,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("createdOn")
    val createdOn: String? = null
)
