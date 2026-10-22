package ir.kitgroup.partnerManagement.feature.organization.ui.detail


import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.components.GradeSelector
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import ir.kitgroup.partnerManagement.core.network.model.AttachmentFileDto
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationStatus
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.outlined.ImageNotSupported
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ir.kitgroup.partnerManagement.core.ui.util.AttachmentFileType
import android.view.ViewGroup
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun OrganizationBasicInfoTabContent(
    organization: OrganizationWithDetail,
    activeVisitorName: String? = null,
    activeContractName: String? = null,
    isImagesLoading: Boolean = false,
    baseUrl: String,
    attachments: List<AttachmentFileDto> = emptyList(),
    onLocationClick: (latitude: Double, longitude: Double) -> Unit = { _, _ -> },
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionTitle(stringResource(R.string.label_organization_basic_information))
        BasicInfoSection(
            organization,
            activeVisitorName = activeVisitorName,
            activeContractName = activeContractName
        )

        SectionTitle(stringResource(R.string.label_contact_information))
        ContactInfoSection(organization)

        SectionTitle(stringResource(R.string.label_location))
        AddressMapCard(
            item = organization,
            onLocationClick = onLocationClick
        )

        SectionTitle(stringResource(R.string.label_analytical_information))
        AnalysisInfoSection(organization)

        SectionTitle(stringResource(R.string.label_statuses))
        StatusInfoSection(organization)

        /*SectionTitle(stringResource(R.string.label_description))
        CustomDescriptionCard(description = "این سازمان از مشتریان فعال است...")
*/
        SectionTitle(stringResource(R.string.label_images))

        ImagesSection(
            isLoading = isImagesLoading,
            attachments = attachments,
            baseUrl = baseUrl
        )
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun BasicInfoSection(
    item: OrganizationWithDetail,
    activeVisitorName: String? = null,
    activeContractName: String? = null
) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        // کادر یکپارچه اطلاعات پایه سازمان
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = colors.surface),
            border = BorderStroke(1.dp, colors.outlineVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                InfoRowItem(
                    title = stringResource(R.string.label_organization_name),
                    value = item.organization.name.orEmpty(),
                    icon = Icons.Filled.Business,
                    isHighlighted = true
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.4f)
                )

                if (!activeVisitorName.isNullOrBlank()) {
                    InfoRowItem(
                        title = "ویزیتور منتخب",
                        value = activeVisitorName,
                        icon = Icons.Filled.Badge
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        thickness = 0.8.dp,
                        color = colors.outlineVariant.copy(alpha = 0.4f)
                    )
                }

                if (!activeContractName.isNullOrBlank()) {
                    InfoRowItem(
                        title = "قراداد فعال",
                        value = activeContractName,
                        icon = Icons.Filled.Handshake
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        thickness = 0.8.dp,
                        color = colors.outlineVariant.copy(alpha = 0.4f)
                    )
                }
                InfoRowItem(
                    title = stringResource(R.string.label_organization_type),
                    value = item.organization.organizationTypeName.orEmpty(),
                    icon = Icons.Filled.Category
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.4f)
                )

                InfoRowItem(
                    title = stringResource(R.string.label_organization_level),
                    value = item.organization.levelName.orEmpty(),
                    icon = Icons.Filled.WorkspacePremium
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.4f)
                )

                InfoRowItem(
                    title = stringResource(R.string.label_owner_name),
                    value = item.organization.ownerName.orEmpty(),
                    icon = Icons.Filled.ManageAccounts
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.4f)
                )
                InfoRowItem(
                    title = stringResource(R.string.label_mobile_owner),
                    value = item.organization.mobile.orEmpty(),
                    icon = Icons.Filled.Smartphone
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.4f)
                )
                InfoRowItem(
                    title = stringResource(R.string.label_english_name),
                    value = item.organization.englishName.orEmpty(),
                    icon = Icons.Filled.Language
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.4f)
                )

                InfoRowItem(
                    title = stringResource(R.string.label_national_id),
                    value = item.organization.nationalId.orEmpty(),
                    icon = Icons.Filled.Badge
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.4f)
                )

                // نمایش وضعیت
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.label_status),
                        style = typography.bodyMedium,
                        color = appColors.textSecondary
                    )

                    StatusBadge(
                        status = OrganizationStatus.fromId(item.organization.status)
                    )
                }


                // نمایش وضعیت
                val currentStatus = OrganizationStatus.fromId(item.organization.status)

                // نمایش دلیل غیرفعال‌سازی در صورت غیرفعال بودن سازمان
                if (currentStatus == OrganizationStatus.INACTIVE && !item.organization.reasonDeactive.isNullOrBlank()) {
                    InfoRowItem(
                        title = stringResource(R.string.label_inactivation_date),
                        value = formatJalaliDate(item.organization.deactiveDate),
                        icon = Icons.Filled.CalendarMonth
                    )
                    InfoRowItem(
                        title = stringResource(R.string.label_inactivation_reason_title),
                        value = item.organization.reasonDeactive,
                        icon = Icons.Filled.Description
                    )

                }
            }
        }
    }
}

@Composable
private fun ContactInfoSection(
    item: OrganizationWithDetail
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        val colors = MaterialTheme.colorScheme

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = colors.surface),
            border = BorderStroke(1.dp, colors.outlineVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                InfoRowItem(
                    title = stringResource(R.string.label_organization_land_line),
                    value = item.organization.landLine.orEmpty(),
                    icon = Icons.Filled.Phone
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.4f)
                )

                InfoRowItem(
                    title = stringResource(R.string.label_phone),
                    value = item.organization.phone.orEmpty(),
                    icon = Icons.Filled.Phone
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.4f)
                )


                InfoRowItem(
                    title = stringResource(R.string.label_email),
                    value = item.organization.email.orEmpty(),
                    icon = Icons.Filled.Email
                )
            }
        }
    }
}

@Composable
private fun InfoRowItem(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    isHighlighted: Boolean = false
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = if (isHighlighted) colors.primary else colors.onSurfaceVariant
            )
            Text(
                text = title,
                style = typography.labelMedium,
                color = colors.onSurfaceVariant
            )
        }

        Text(
            text = value.ifBlank { "—" },
            style = if (isHighlighted) typography.titleLarge else typography.labelLarge,
            color = if (isHighlighted) colors.primary else colors.onSurface
        )
    }
}


@Composable
private fun AddressMapCard(
    item: OrganizationWithDetail,
    onLocationClick: (latitude: Double, longitude: Double) -> Unit
) {
    val context = LocalContext.current

    val colors = MaterialTheme.colorScheme
    val lat = item.organization.latitude
    val lng = item.organization.longitude
    val hasLocation = lat != null && lng != null


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = true) {
                onLocationClick(lat!!, lng!!)
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = BorderStroke(1.dp, colors.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // باکس آیکون موقعیت مکانی / پیش‌نمایش نقشه
            Box(
                modifier = Modifier
                    .size(width = 110.dp, height = 96.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                if (hasLocation) {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { ctx ->
                            Configuration.getInstance().userAgentValue = ctx.packageName
                            MapView(ctx).apply {
                                setTileSource(TileSourceFactory.MAPNIK)
                                setMultiTouchControls(false) // برای جلوگیری از تداخل اسکرول
                                controller.setZoom(15.0)
                                controller.setCenter(GeoPoint(lat!!, lng!!))
                                overlays.add(Marker(this).apply {
                                    position = GeoPoint(lat, lng)
                                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                })
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                            }
                        }
                    )
                } else {
                    // در صورتی که مختصاتی ثبت نشده باشد
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint = colors.primary,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            // ستون نمایش جزئیات شهر، منطقه و آدرس
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // نمایش شهر و منطقه در صورت وجود
                val locationScope = listOf(item.cityName.trim(), item.regionName.trim())
                    .filter { it.isNotBlank() }
                    .joinToString("، ")

                if (locationScope.isNotBlank()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${stringResource(R.string.label_city)} / ${stringResource(R.string.label_region)}:",
                            style = typography.labelSmall,
                            color = colors.onSurfaceVariant
                        )
                        Text(
                            text = locationScope,
                            style = typography.labelMedium,
                            color = colors.onSurface
                        )
                    }
                }

                // عنوان و متن آدرس
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = stringResource(R.string.label_address) + ":",
                        style = typography.labelSmall,
                        color = colors.onSurfaceVariant
                    )

                    Text(
                        text = item.organization.address?.ifBlank { "-" } ?: "-",
                        style = typography.bodyMedium,
                        color = colors.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun AnalysisInfoSection(
    item: OrganizationWithDetail,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // ── بخش درجه‌بندی ──
        GradeSelector(
            grade = item.organization.grade ?: 0
        )

        // ── کارت یکپارچه اطلاعات آماری/تحلیلی ──
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = colors.surface
            ),
            border = BorderStroke(1.dp, colors.outlineVariant)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                // سابقه تعداد فروش بلیت
                InfoRowItem(
                    title = stringResource(R.string.label_ticket_sale_count_history),
                    value = item.organization.ticketSaleCountHistory.toString(),
                    icon = Icons.Outlined.ConfirmationNumber,
                    isHighlighted = false
                )

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.5f)
                )

                // ظرفیت مشتریان
                InfoRowItem(
                    title = stringResource(R.string.label_customer_capacity),
                    value = item.organization.customerCapacity.toString(),
                    icon = Icons.Outlined.Groups,
                    isHighlighted = false
                )
            }
        }
    }
}

@Composable
private fun StatusInfoSection(
    item: OrganizationWithDetail,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        ),
        border = BorderStroke(1.dp, colors.outlineVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            // ── پذیرش مهمان خارجی ──
            InfoRowItem(
                title = stringResource(R.string.label_foreign_guest_reception),
                value = if (item.organization.statusExternalCustomer == true) {
                    stringResource(R.string.label_has)
                } else {
                    stringResource(R.string.label_has_not)
                },
                icon = Icons.Outlined.Public,
                isHighlighted = false
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 14.dp),
                thickness = 0.8.dp,
                color = colors.outlineVariant.copy(alpha = 0.5f)
            )

            // ── تمایل به دریافت استند تبلیغاتی ──
            InfoRowItem(
                title = stringResource(R.string.label_desire_receive_advertising_stands),
                value = if (item.organization.statusGetStand == true) {
                    stringResource(R.string.label_has)
                } else {
                    stringResource(R.string.label_has_not)
                },
                icon = Icons.Outlined.Storefront,
                isHighlighted = false
            )
        }
    }
}


@Composable
fun ImagesSection(
    isLoading: Boolean,
    attachments: List<AttachmentFileDto>,
    baseUrl: String,
    modifier: Modifier = Modifier
) {
    // نگهداری عکسی که کاربر روی آن کلیک کرده تا در دیالوگ باز شود
    var selectedAttachmentForPreview by remember { mutableStateOf<AttachmentFileDto?>(null) }

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
                    text = stringResource(R.string.msg_no_images_registered),
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
            val imageUrl =
                "${baseUrl.trimEnd('/')}/api/OrgAttachmentFiles/download/${attachment.attachmentFileId}"
            Log.d("imageUrllllll1", imageUrl.toString())
            Log.d("imageUrllllll2", attachment.toString())

            ImageCard(
                imageUrl = imageUrl,
                attachment = attachment,
                onClick = { selectedAttachmentForPreview = attachment }
            )
        }
    }

    // دیالوگ پیش‌نمایش تصویر به صورت بزرگ همراه با توضیحات
    selectedAttachmentForPreview?.let { attachment ->
        val fullImageUrl =
            "${baseUrl.trimEnd('/')}/api/OrgAttachmentFiles/download/${attachment.attachmentFileId}"

        ImagePreviewDialog(
            imageUrl = fullImageUrl,
            attachment = attachment,
            onDismiss = { selectedAttachmentForPreview = null }
        )
    }
}


@Composable
private fun ImageCard(
    imageUrl: Any?,
    attachment: AttachmentFileDto,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val fileTypeEnum = AttachmentFileType.fromId(attachment.fileType ?: 3)

    Box(
        modifier = Modifier
            .size(130.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable { onClick() }
    ) {
        // تصویر بندانگشتی
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUrl)
                .crossfade(true)
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_placeholder)
                .build(),
            contentDescription = attachment.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // گرادینت پایین کارت جهت خوانایی متن
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .align(Alignment.BottomCenter)
                .background(
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            androidx.compose.ui.graphics.Color.Transparent,
                            androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.75f)
                        )
                    )
                )
        )

        // برچسب نوع تصویر
        Surface(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(6.dp),
            shape = RoundedCornerShape(6.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f)
        ) {
            val titleText = fileTypeEnum?.let {
                stringResource(it.titleRes)
            } ?: stringResource(R.string.attachment_type_other_images)

            Text(
                text = titleText,
                style = typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                maxLines = 1
            )
        }

    }
}
