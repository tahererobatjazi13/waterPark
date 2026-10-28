package ir.kitgroup.partnerManagement.core.ui.components

import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.network.model.AttachmentFileDto
import ir.kitgroup.partnerManagement.core.ui.model.OrganizationImageItem
import ir.kitgroup.partnerManagement.core.ui.util.AttachmentFileType
import ir.kitgroup.partnerManagement.feature.organization.ui.detail.ImagePreviewDialog

@Composable
fun ImageAddEditSection(
    images: List<OrganizationImageItem>,
    baseUrl: String,
    isEditMode: Boolean = false,
    onAddImage: () -> Unit,
    onRemoveImage: (Int) -> Unit
) {
    val context = LocalContext.current
    var previewData by remember { mutableStateOf<Pair<String, AttachmentFileDto>?>(null) }

    previewData?.let { (imageUrl, attachmentDto) ->
        ImagePreviewDialog(
            imageUrl = imageUrl,
            attachment = attachmentDto,
            onDismiss = { previewData = null }
        )
    }

    SectionTitle(
        title = stringResource(R.string.label_attachment),
        titleColor = MaterialTheme.colorScheme.primary
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        item {
            AddImageCard(onClick = onAddImage)
        }

        itemsIndexed(images) { index, item ->
            // ۱. تشخیص دقیق پسوند و نوع سند بر اساس Uri یا مسیر اینترنتی
            val cleanExt = remember(item.uri, item.serverImageUrl) {
                resolveFileExtension(context, item.uri, item.serverImageUrl)
            }
            val isPdf = cleanExt == "pdf"
            val isWord = cleanExt in listOf("doc", "docx")
            val isDocument = isPdf || isWord

            // ۲. ساخت آدرس معتبر برای نمایش Coil یا پیش‌نمایش
            val serverDownloadUrl = item.uploadedFileId?.takeIf { it.isNotBlank() }?.let { id ->
                "${baseUrl.trimEnd('/')}/api/OrgAttachmentFiles/download/$id"
            }

            val imageModel: Any? = when {
                // اگر فایل محلی توسط کاربر از گالری انتخاب شده است
                item.uri != null -> item.uri
                // اگر فایل قبلاً روی سرور آپلود شده است، اولویت با اندپوینت رسمی دانلود است
                !serverDownloadUrl.isNullOrBlank() -> serverDownloadUrl
                // اگر URL مستقیمی ست شده بود
                !item.serverImageUrl.isNullOrBlank() -> {
                    if (item.serverImageUrl!!.startsWith("http", ignoreCase = true)) {
                        item.serverImageUrl
                    } else {
                        "${baseUrl.trimEnd('/')}/${item.serverImageUrl!!.removePrefix("/")}"
                    }
                }

                else -> null
            }

            val fileTypeEnum = AttachmentFileType.fromId(item.fileType)
            val fileTypeTitle = fileTypeEnum?.titleRes?.let { stringResource(it) }
                ?: stringResource(R.string.attachment_type_other)

            Box(
                modifier = Modifier
                    .size(130.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        if (!isDocument && imageModel != null) {
                            val dto = AttachmentFileDto(
                                attachmentFileId = item.uploadedFileId ?: "",
                                fileType = item.fileType,
                                name = null,
                                description = item.description
                            )
                            previewData = Pair(imageModel.toString(), dto)
                        }
                    }
            ) {
                when {
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
                                    modifier = Modifier.size(40.dp),
                                    tint = Color.White
                                )
                                Text(
                                    text = "PDF",
                                    style = typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

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
                                    modifier = Modifier.size(40.dp),
                                    tint = Color.White
                                )
                                Text(
                                    text = cleanExt.uppercase().ifEmpty { "DOCX" },
                                    style = typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    else -> {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(imageModel)
                                .crossfade(true)
                                .placeholder(R.drawable.ic_logo_placeholder)
                                .error(R.drawable.ic_logo_placeholder)
                                .build(),
                            contentDescription = item.description,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                // نوار شفاف پایین با عنوان نوع ضمیمه
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    color = Color.Black.copy(alpha = 0.65f)
                ) {
                    Text(
                        text = fileTypeTitle,
                        style = typography.labelSmall,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier.padding(vertical = 3.dp, horizontal = 4.dp)
                    )
                }
                val canDelete = !isEditMode || item.uploadedFileId.isNullOrBlank()

                // دکمه حذف
                if (canDelete) {

                    IconButton(
                        onClick = { onRemoveImage(index) },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(4.dp)
                            .size(24.dp)
                            .background(Color.Red.copy(alpha = 0.85f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.label_delete),
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * متد کمکی برای استخراج مطمئن پسوند فایل چه از طریق Uri محلی (ContentResolver) و چه از URL
 */
private fun resolveFileExtension(
    context: android.content.Context,
    uri: Uri?,
    serverUrl: String?
): String {
    if (uri != null) {
        // ۱. تلاش با MimeType از ContentResolver
        val mimeType = context.contentResolver.getType(uri)
        if (!mimeType.isNullOrBlank()) {
            val extFromMime = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
            if (!extFromMime.isNullOrBlank()) return extFromMime.lowercase()
        }
        // ۲. تلاش با DisplayName از Cursor
        try {
            context.contentResolver.query(
                uri,
                arrayOf(OpenableColumns.DISPLAY_NAME),
                null,
                null,
                null
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1) {
                        val name = cursor.getString(nameIndex)
                        val ext = name?.substringAfterLast('.', "")
                        if (!ext.isNullOrBlank()) return ext.lowercase()
                    }
                }
            }
        } catch (_: Exception) {
        }
    }

    // ۳. اگر سروری بود یا Uri نداشت
    val target = uri?.toString() ?: serverUrl.orEmpty()
    return target.substringAfterLast('.', "").substringBefore('?').substringBefore('/').lowercase()
}


@Composable
fun AddImageCard(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(90.dp, 130.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                1.dp,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.CloudUpload,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(R.string.label_add_file),
                style = typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }
    }
}