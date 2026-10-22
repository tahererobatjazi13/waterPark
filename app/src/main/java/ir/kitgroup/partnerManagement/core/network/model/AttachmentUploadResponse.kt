package ir.kitgroup.partnerManagement.core.network.model

import com.google.gson.annotations.SerializedName

data class AttachmentUploadResponse(
    @SerializedName("attachmentFileId")
    val attachmentFileId: String,
    
    @SerializedName("fileName")
    val fileName: String,
    
    @SerializedName("fileUrl")
    val fileUrl: String,
    
    @SerializedName("fileSize")
    val fileSize: Long
)
