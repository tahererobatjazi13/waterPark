package ir.kitgroup.partnerManagement.core.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.outlined.ImageNotSupported
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.network.model.AttachmentFileDto
import ir.kitgroup.partnerManagement.core.ui.util.AttachmentFileType
import ir.kitgroup.partnerManagement.core.ui.util.openDownloadedFile
import ir.kitgroup.partnerManagement.feature.organization.ui.detail.ImagePreviewDialog
import java.io.File

@Composable
fun ImageDetailSection(
    isLoading: Boolean,
    attachments: List<AttachmentFileDto>,
    baseUrl: String,
    onDownloadClick: (attachmentId: String, extension: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedAttachmentForPreview by remember {
        mutableStateOf<Pair<AttachmentFileDto, Any>?>(
            null
        )
    }

    if (isLoading) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(96.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.size(28.dp), strokeWidth = 2.5.dp)
        }
        return
    }

    if (attachments.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ImageNotSupported,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = stringResource(R.string.msg_no_file_found),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        return
    }

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp)
    ) {
        items(
            items = attachments,
            key = { it.attachmentFileId }
        ) { attachment ->
            val cleanExt = remember(attachment.fileExtension, attachment.fileUrl) {
                val extFromField =
                    attachment.fileExtension?.trim()?.removePrefix(".")?.lowercase().orEmpty()
                if (extFromField.isNotEmpty()) {
                    extFromField
                } else {
                    // اگر فیلد fileExtension خالی بود، از انتهای fileUrl استخراج کن
                    attachment.fileUrl?.substringAfterLast('.', "")?.substringBefore('?')
                        ?.trim()?.removePrefix(".")?.lowercase().orEmpty()
                }
            }

            val isPdf = cleanExt == "pdf"
            val isWord = cleanExt in listOf("doc", "docx")
            val isDocument = isPdf || isWord

            val imagesDir = File(context.filesDir, "attachments")
            val extensionWithDot = if (cleanExt.isNotEmpty()) ".$cleanExt" else ".jpg"
            val localAttachmentFile =
                File(imagesDir, "${attachment.attachmentFileId}$extensionWithDot")

            // ۳. ساخت آدرس دانلود استاندارد سرور
            val serverDownloadUrl =
                "${baseUrl.trimEnd('/')}/api/OrgAttachmentFiles/download/${attachment.attachmentFileId}"

            // اولویت با فایل محلی است؛ اگر وجود نداشت به اندپوینت معتبر دانلود اشاره می‌کند
            val imageModel: Any = when {
                localAttachmentFile.exists() -> localAttachmentFile
                !attachment.fileUrl.isNullOrBlank() && File(attachment.fileUrl).exists() -> File(
                    attachment.fileUrl
                )

                else -> serverDownloadUrl
            }

            Log.d(
                "ImageDetailSection",
                "Resolved cleanExt: '$cleanExt', isPdf: $isPdf, imageModel: $imageModel"
            )

            ImageCard(
                imageUrl = imageModel,
                attachment = attachment,
                isPdf = isPdf,
                isWord = isWord,
                onClick = {
                    if (isDocument) {
                        if (localAttachmentFile.exists()) {
                            openDownloadedFile(context, localAttachmentFile)
                        } else {
                            onDownloadClick(attachment.attachmentFileId, attachment.fileExtension)
                        }
                    } else {
                        // پاس دادن مدل معتبر به دیالوگ پیش‌نمایش
                        selectedAttachmentForPreview = Pair(attachment, imageModel)
                    }
                },
                onDownloadClick = {
                    onDownloadClick(attachment.attachmentFileId, attachment.fileExtension)
                }
            )
        }
    }

    // پیش‌نمایش تمام صفحه تصویر
    selectedAttachmentForPreview?.let { (attachment, model) ->
        ImagePreviewDialog(
            imageUrl = model.toString(),
            attachment = attachment,
            onDismiss = { selectedAttachmentForPreview = null }
        )
    }
}

@Composable
private fun ImageCard(
    imageUrl: Any?,
    attachment: AttachmentFileDto,
    isPdf: Boolean,
    isWord: Boolean,
    onClick: () -> Unit,
    onDownloadClick: () -> Unit
) {
    val fileTypeEnum = AttachmentFileType.fromId(attachment.fileType)

    Box(
        modifier = Modifier
            .size(130.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick() }
    ) {
        when {
            // ۱. سند PDF
            isPdf -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFFE53935), Color(0xFFB71C1C))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PictureAsPdf,
                            contentDescription = "PDF Document",
                            modifier = Modifier.size(42.dp),
                            tint = Color.White
                        )
                        Text(
                            text = "PDF",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // ۲. سند Word
            isWord -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF1E88E5), Color(0xFF0D47A1))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = "Word Document",
                            modifier = Modifier.size(42.dp),
                            tint = Color.White
                        )
                        Text(
                            text = (attachment.fileExtension?.uppercase()?.removePrefix(".")
                                ?: "DOCX"),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // ۳. تصویر (JPG, PNG و غیره)
            else -> {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .placeholder(R.drawable.ic_logo_placeholder)
                        .error(R.drawable.ic_logo_placeholder)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // گرادینت پایین کارت جهت خوانایی برچسب
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.75f)
                        )
                    )
                )
        )

        // دکمه دانلود در گوشه بالا راست
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(6.dp)
                .size(30.dp)
                .clickable { onDownloadClick() },
            shape = CircleShape,
            color = Color.Black.copy(alpha = 0.55f),
            contentColor = Color.White
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Default.FileDownload,
                    contentDescription = "دانلود",
                    modifier = Modifier.size(17.dp)
                )
            }
        }

        // برچسب نوع ضمیمه در پایین
        Surface(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(6.dp),
            shape = RoundedCornerShape(6.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
        ) {
            val titleText = fileTypeEnum?.let {
                stringResource(it.titleRes)
            } ?: stringResource(R.string.attachment_type_other)

            Text(
                text = titleText,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                maxLines = 1
            )
        }
    }
}
