package ir.kitgroup.partnerManagement.feature.organization.ui.detail

import ir.kitgroup.partnerManagement.core.network.model.AttachmentFileDto

data class AttachmentsFileUiState(
    val isLoading: Boolean = false,
    val isImagesLoading: Boolean = false,
    val attachments: List<AttachmentFileDto> = emptyList(),
    val errorMessage: String? = null
)
