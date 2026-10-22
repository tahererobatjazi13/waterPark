package ir.kitgroup.partnerManagement.core.ui.model

import android.net.Uri

data class OrganizationImageItem(
    val uri: Uri?,
    val fileType: Int = 1,
    val description: String = "",
    val uploadedFileId: String? = null,
    val serverImageUrl: String? = null,

)
