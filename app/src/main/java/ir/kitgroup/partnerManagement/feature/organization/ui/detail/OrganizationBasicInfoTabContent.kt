package ir.kitgroup.partnerManagement.feature.organization.ui.detail

import android.content.Intent
import android.net.Uri
import android.view.ViewGroup
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import ir.kitgroup.partnerManagement.core.network.model.AttachmentFileDto
import ir.kitgroup.partnerManagement.core.ui.components.CustomDescriptionCard
import ir.kitgroup.partnerManagement.core.ui.components.ImageDetailSection
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationStatus
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate
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
    onDownloadAttachment: (attachmentId: String, extension: String?) -> Unit,
    attachments: List<AttachmentFileDto> = emptyList(),
    onLocationClick: (latitude: Double, longitude: Double) -> Unit = { _, _ -> },
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        // اطلاعات پایه سازمانی و قرارداد
        SectionTitle(stringResource(R.string.label_organization_basic_information))
        BasicInfoSection(
            organization = organization,
            activeVisitorName = activeVisitorName,
            activeContractName = activeContractName
        )

        // اطلاعات تماس با قابلیت اکشن سریع (تماس و ایمیل مستقیم)
        SectionTitle(stringResource(R.string.label_contact_information))
        InteractiveContactSection(
            organization = organization,
            onDial = { phone ->
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                context.startActivity(intent)
            },
            onEmail = { email ->
                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email"))
                context.startActivity(intent)
            }
        )
        // ویترین شاخص‌های کلیدی و تجاری سازمان (KPIs)
        BusinessOverviewCards(organization)


        //  موقعیت مکانی با نقشه تمام‌عرض
        SectionTitle(stringResource(R.string.label_location))
        AddressMapCard(
            item = organization,
            onLocationClick = onLocationClick
        )

        // ۵. توضیحات (در صورت وجود)
        if (!organization.organization.description.isNullOrBlank()) {
            SectionTitle(stringResource(R.string.label_description))
            CustomDescriptionCard(description = organization.organization.description.toString())
        }

        // ۶. فایل‌ها و تصاویر پیوست
        SectionTitle(stringResource(R.string.label_attachment))
        ImageDetailSection(
            isLoading = isImagesLoading,
            attachments = attachments,
            baseUrl = baseUrl,
            onDownloadClick = onDownloadAttachment
        )

        Spacer(Modifier.height(20.dp))
    }
}

/**
 * کارت‌های شاخص تحلیلی و تجاری (ویترین بالای تب برای خوانایی فوری)
 */
@Composable
private fun BusinessOverviewCards(item: OrganizationWithDetail) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // فروش بلیت
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                color = appColors.cardBackground,
                border = BorderStroke(0.8.dp, appColors.border)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ConfirmationNumber,
                        contentDescription = null,
                        tint = colors.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "${item.organization.ticketSaleCountHistory ?: 0}",
                        style = typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface
                    )
                    Text(
                        text = stringResource(R.string.label_ticket_sale_count_history),
                        style = typography.labelSmall,
                        color = appColors.textSecondary
                    )
                }
            }

            // ظرفیت مشتری
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                color = appColors.cardBackground,
                border = BorderStroke(0.8.dp, appColors.border)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Groups,
                        contentDescription = null,
                        tint = appColors.success,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "${item.organization.customerCapacity ?: 0}",
                        style = typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface
                    )
                    Text(
                        text = stringResource(R.string.label_customer_capacity),
                        style = typography.labelSmall,
                        color = appColors.textSecondary
                    )
                }
            }


        }

        Spacer(Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            // وضعیت استند
            val hasStand = item.organization.statusGetStand == true
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                color = if (hasStand) colors.primaryContainer.copy(alpha = 0.4f) else appColors.cardBackground,
                border = BorderStroke(
                    0.8.dp,
                    if (hasStand) colors.primary.copy(alpha = 0.5f) else appColors.border
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Storefront,
                        contentDescription = null,
                        tint = if (hasStand) colors.primary else appColors.textSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = if (hasStand) stringResource(R.string.label_has) else stringResource(
                            R.string.label_has_not
                        ),
                        style = typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (hasStand) colors.primary else colors.onSurface
                    )
                    Text(
                        text = stringResource(R.string.label_desire_receive_advertising_stands),
                        style = typography.labelSmall,
                        color = appColors.textSecondary
                    )
                }
            }


            // وضعیت  مهمان خارجی
            val hasForeignGuest = item.organization.statusExternalCustomer == true
            Surface(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp),
                color = if (hasStand) colors.primaryContainer.copy(alpha = 0.4f) else appColors.cardBackground,
                border = BorderStroke(
                    0.8.dp,
                    if (hasStand) colors.primary.copy(alpha = 0.5f) else appColors.border
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Public,
                        contentDescription = null,
                        tint = if (hasForeignGuest) colors.primary else appColors.textSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = if (hasForeignGuest) stringResource(R.string.label_has) else stringResource(
                            R.string.label_has_not
                        ),
                        style = typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (hasForeignGuest) colors.primary else colors.onSurface
                    )
                    Text(
                        text = stringResource(R.string.label_foreign_guest_reception),
                        style = typography.labelSmall,
                        color = appColors.textSecondary
                    )
                }
            }
        }
    }
}

/**
 * اطلاعات پایه سازمانی با چیدمان تمیز
 */
@Composable
private fun BasicInfoSection(
    organization: OrganizationWithDetail,
    activeVisitorName: String? = null,
    activeContractName: String? = null
) {
    val colors = MaterialTheme.colorScheme
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = BorderStroke(0.8.dp, appColors.border)
    ) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            if (!activeContractName.isNullOrBlank()) {
                DetailRow(
                    title = "قرارداد فعال",
                    value = activeContractName,
                    icon = Icons.Filled.Handshake,
                    valueColor = colors.primary
                )
                ItemDivider()
            }

            if (!activeVisitorName.isNullOrBlank()) {
                DetailRow(
                    title = "ویزیتور اختصاصی",
                    value = activeVisitorName,
                    icon = Icons.Filled.Badge
                )
                ItemDivider()
            }

            DetailRow(
                title = stringResource(R.string.label_organization_type),
                value = organization.organization.organizationTypeName.orEmpty(),
                icon = Icons.Filled.Category
            )
            ItemDivider()

            DetailRow(
                title = stringResource(R.string.label_organization_level),
                value = organization.organization.levelName.orEmpty(),
                icon = Icons.Filled.WorkspacePremium
            )
            ItemDivider()

            DetailRow(
                title = stringResource(R.string.label_owner_name),
                value = organization.organization.ownerName.orEmpty(),
                icon = Icons.Filled.ManageAccounts
            )
            ItemDivider()

            DetailRow(
                title = stringResource(R.string.label_english_name),
                value = organization.organization.englishName.orEmpty(),
                icon = Icons.Filled.Language
            )
            ItemDivider()

            DetailRow(
                title = stringResource(R.string.label_national_id),
                value = organization.organization.nationalId.orEmpty(),
                icon = Icons.Filled.Badge
            )

            // اطلاعات غیرفعال‌سازی در صورت وجود
            val currentStatus = OrganizationStatus.fromId(organization.organization.status)
            if (currentStatus == OrganizationStatus.INACTIVE && !organization.organization.reasonDeactive.isNullOrBlank()) {
                ItemDivider()
                DetailRow(
                    title = stringResource(R.string.label_inactivation_date),
                    value = formatJalaliDate(organization.organization.deactiveDate),
                    icon = Icons.Filled.CalendarMonth,
                    valueColor = colors.error
                )
                ItemDivider()
                DetailRow(
                    title = stringResource(R.string.label_inactivation_reason_title),
                    value = organization.organization.reasonDeactive.orEmpty(),
                    icon = Icons.Filled.Description,
                    valueColor = colors.error
                )
            }
        }
    }
}

/**
 * سکشن تماس تعاملی با دکمه‌های مستقیم تماس تلفنی و ارسال ایمیل
 */
@Composable
private fun InteractiveContactSection(
    organization: OrganizationWithDetail,
    onDial: (String) -> Unit,
    onEmail: (String) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = BorderStroke(0.8.dp, appColors.border)
    ) {
        Column(modifier = Modifier.padding(vertical = 4.dp)) {
            // شماره موبایل مدیر
            val mobile = organization.organization.mobile?.trim().orEmpty()
            ActionableContactRow(
                title = stringResource(R.string.label_mobile_owner),
                value = mobile,
                icon = Icons.Filled.Smartphone,
                actionIcon = if (mobile.isNotBlank()) Icons.Filled.Call else null,
                onActionClick = { onDial(mobile) }
            )

            ItemDivider()

            // تلفن ثابت
            val landline = organization.organization.landLine?.trim().orEmpty()
            ActionableContactRow(
                title = stringResource(R.string.label_organization_land_line),
                value = landline,
                icon = Icons.Filled.Phone,
                actionIcon = if (landline.isNotBlank()) Icons.Filled.Call else null,
                onActionClick = { onDial(landline) }
            )

            ItemDivider()

            // تلفن همراه دوم / پشتیبان
            val phone = organization.organization.phone?.trim().orEmpty()
            ActionableContactRow(
                title = stringResource(R.string.label_phone),
                value = phone,
                icon = Icons.Filled.Phone,
                actionIcon = if (phone.isNotBlank()) Icons.Filled.Call else null,
                onActionClick = { onDial(phone) }
            )

            // ایمیل
            val email = organization.organization.email?.trim().orEmpty()
            if (email.isNotBlank()) {
                ItemDivider()
                ActionableContactRow(
                    title = stringResource(R.string.label_email),
                    value = email,
                    icon = Icons.Filled.Email,
                    actionIcon = Icons.Filled.Email,
                    onActionClick = { onEmail(email) }
                )
            }
        }
    }
}

/**
 * ردیف تماس همراه با دکمه Action
 */
@Composable
private fun ActionableContactRow(
    title: String,
    value: String,
    icon: ImageVector,
    actionIcon: ImageVector? = null,
    onActionClick: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme
    val appColors = LocalPartnerManagementColors.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = appColors.textSecondary
            )
            Column {
                Text(
                    text = title,
                    style = typography.labelSmall,
                    color = appColors.textSecondary
                )
                Text(
                    text = value.ifBlank { "—" },
                    style = typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = colors.onSurface
                )
            }
        }

        if (actionIcon != null && value.isNotBlank() && value != "—") {
            IconButton(
                onClick = onActionClick,
                modifier = Modifier
                    .size(34.dp)
                    .background(colors.primaryContainer.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

/**
 * کارت موقعیت مکانی با نقشه تمام‌عرض باکیفیت و کلیک‌پذیر
 */
@Composable
private fun AddressMapCard(
    item: OrganizationWithDetail,
    onLocationClick: (latitude: Double, longitude: Double) -> Unit
) {
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme
    val appColors = LocalPartnerManagementColors.current
    val latitude = item.organization.latitude
    val longitude = item.organization.longitude

    val hasLocation = remember(latitude, longitude) {
        latitude != null && longitude != null &&
                kotlin.math.abs(latitude) > 0.001 &&
                kotlin.math.abs(longitude) > 0.001
    }

    val customMarkerDrawable = remember(context) {
        ContextCompat.getDrawable(context, R.drawable.ic_location_pin)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = hasLocation,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (hasLocation) {
                    onLocationClick(latitude!!, longitude!!)
                }
            },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = BorderStroke(0.8.dp, appColors.border)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // پیش‌نمایش نقشه تمام‌عرض (ارتفاع ۱۱۰dp)
            if (hasLocation) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(colors.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { ctx ->
                            Configuration.getInstance().userAgentValue = ctx.packageName
                            MapView(ctx).apply {
                                setTileSource(TileSourceFactory.MAPNIK)
                                setMultiTouchControls(false)
                                isClickable = false
                                setBuiltInZoomControls(false)
                                controller.setZoom(15.0)
                                controller.setCenter(GeoPoint(latitude!!, longitude!!))

                                val marker = Marker(this).apply {
                                    position = GeoPoint(latitude, longitude)
                                    customMarkerDrawable?.let { icon = it }
                                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                    closeInfoWindow()
                                }
                                overlays.add(marker)

                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                            }
                        },
                        update = { view ->
                            val marker = view.overlays.filterIsInstance<Marker>().firstOrNull()
                            if (marker?.position?.latitude != latitude || marker?.position?.longitude != longitude) {
                                val newPoint = GeoPoint(latitude!!, longitude!!)
                                marker?.position = newPoint
                                customMarkerDrawable?.let { marker!!.icon = it }
                                view.controller.setCenter(newPoint)
                                view.postInvalidate()
                            }
                        }
                    )
                }
            }

            // اطلاعات شهر، منطقه و آدرس کامل
            val locationScope = listOf(item.cityName.trim(), item.regionName.trim())
                .filter { it.isNotBlank() }
                .joinToString("، ")

            if (locationScope.isNotBlank()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = colors.primary
                    )
                    Text(
                        text = locationScope,
                        style = typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface
                    )
                }
            }

            Text(
                text = item.organization.address?.ifBlank { "آدرس ثبت نشده است" }
                    ?: "آدرس ثبت نشده است",
                style = typography.bodyMedium,
                color = appColors.textSecondary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * ردیف استاندارد نمایش مشخصه و مقدار
 */
@Composable
private fun DetailRow(
    title: String,
    value: String,
    icon: ImageVector,
    valueColor: androidx.compose.ui.graphics.Color? = null
) {
    val colors = MaterialTheme.colorScheme
    val appColors = LocalPartnerManagementColors.current

    Row(
        modifier = Modifier
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
                tint = appColors.textSecondary
            )
            Text(
                text = title,
                style = typography.labelMedium,
                color = appColors.textSecondary
            )
        }

        Text(
            text = value.ifBlank { "—" },
            style = typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = valueColor ?: colors.onSurface
        )
    }
}

@Composable
private fun ItemDivider() {
    val colors = MaterialTheme.colorScheme
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 14.dp),
        thickness = 0.6.dp,
        color = colors.outlineVariant.copy(alpha = 0.35f)
    )
}
