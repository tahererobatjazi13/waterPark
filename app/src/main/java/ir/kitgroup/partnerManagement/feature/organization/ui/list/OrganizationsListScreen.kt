package ir.kitgroup.partnerManagement.feature.organization.ui.list

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.AttachmentFileEntity
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import ir.kitgroup.partnerManagement.core.ui.components.*
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationStatus
import ir.kitgroup.partnerManagement.core.ui.util.SyncStatus
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import java.io.File

@Composable
fun OrganizationsListScreen(
    onOrganizationClick: (String) -> Unit,
    onAddOrganizationClick: () -> Unit,
    onLocationClick: ((latitude: Double, longitude: Double) -> Unit)? = null,
    modifier: Modifier = Modifier,
    organizationsListViewModel: OrganizationsListViewModel = hiltViewModel()
) {
    val appColors = LocalPartnerManagementColors.current
    val organizations by organizationsListViewModel.organizations.collectAsStateWithLifecycle()
    val statusOptions = remember { OrganizationStatus.entries }

    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedStatus by rememberSaveable { mutableStateOf<OrganizationStatus?>(null) }
    var isWarningOnly by rememberSaveable { mutableStateOf(false) }
    val baseUrl by organizationsListViewModel.baseUrlFlow.collectAsStateWithLifecycle()

    LaunchedEffect(organizationsListViewModel.uiEvent) {
        organizationsListViewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowMessage -> snackBarHostState.showSnackbar(
                    event.message.asString(
                        context
                    )
                )

                is UiEvent.ShowError -> snackBarHostState.showSnackbar(event.error.asString(context))
            }
        }
    }

    // فیلتر هوشمند و بهینه‌شده
    val filteredOrganizations =
        remember(organizations, searchQuery, selectedStatus, isWarningOnly) {
            organizations?.filter { item ->
                val query = searchQuery.trim()
                val matchesSearch = query.isBlank() ||
                        item.organization.name.orEmpty().contains(query, ignoreCase = true) ||
                        item.organization.address.orEmpty().contains(query, ignoreCase = true) ||
                        item.organization.phone.orEmpty().contains(query) ||
                        item.cityName.contains(query, ignoreCase = true) ||
                        item.regionName.contains(query, ignoreCase = true)

                val matchesStatus =
                    selectedStatus == null || item.organization.status == selectedStatus!!.id
                val matchesWarning = !isWarningOnly || (item.organization.countWarning ?: 0) > 0

                matchesSearch && matchesStatus && matchesWarning
            }
        }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    organizationsListViewModel.checkCanNavigateToAddOrganization {
                        onAddOrganizationClick()
                    }
                },
                containerColor = appColors.success,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .height(50.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.label_add_new_organization),
                        style = typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, top = 12.dp)
            ) {
                // جستجو و پاک کردن سریع
                FilterSection(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    hint = "جستجوی نام سازمان، تلفن یا آدرس..."
                )

               // Spacer(modifier = Modifier.height(4.dp))

                // چیپ‌های فیلتر سریع (تک‌لمسی و ارگونومیک)
                OrganizationsQuickFilterChips(
                    selectedStatus = selectedStatus,
                    statusOptions = statusOptions,
                    isWarningOnly = isWarningOnly,
                    onSelectStatus = { selectedStatus = if (selectedStatus == it) null else it },
                    onToggleWarningOnly = { isWarningOnly = !isWarningOnly },
                    onResetFilters = {
                        selectedStatus = null
                        isWarningOnly = false
                        searchQuery = ""
                    }
                )

             //   Spacer(modifier = Modifier.height(6.dp))

                // نوار وضعیت تعداد نتایج و ریست سریع
                if (filteredOrganizations != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                     Text(
                            text = "${filteredOrganizations.size} سازمان یافت شد",
                            style = typography.labelMedium,
                            color = appColors.textSecondary
                        )

                        if (selectedStatus != null || isWarningOnly || searchQuery.isNotBlank()) {
                            Text(
                                text = "حذف همه فیلترها",
                                style = typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        selectedStatus = null
                                        isWarningOnly = false
                                        searchQuery = ""
                                    }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // لیست سازمان‌ها
                when {
                    organizations == null -> {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(36.dp),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    filteredOrganizations?.isEmpty() == true -> {
                        EmptyState(
                            textRes = R.string.msg_no_item_found,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        )
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(top = 4.dp, bottom = 84.dp)
                        ) {
                            itemsIndexed(
                                items = filteredOrganizations!!,
                                key = { _, item -> item.organization.organizationId }
                            ) { index, organization ->
                                val orgThumbnail =
                                    organization.attachments.firstOrNull { it.isHeader }
                                        ?: organization.attachments.firstOrNull()

                                ModernOrganizationCard(
                                    baseUrl = baseUrl,
                                    item = organization,
                                    number = index + 1,
                                    thumbnail = orgThumbnail,
                                    onClick = { onOrganizationClick(organization.organization.organizationId) },
                                    onPhoneClick = { phone ->
                                        val intent =
                                            Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                                        context.startActivity(intent)
                                    },
                                    onNavigateMap = { lat, lng ->
                                        if (onLocationClick != null) {
                                            onLocationClick(lat, lng)
                                        } else {
                                            val gmmIntentUri =
                                                Uri.parse("geo:$lat,$lng?q=$lat,$lng")
                                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                            context.startActivity(mapIntent)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * چیپ‌های فیلتر افقی تک‌لمسی (جایگزین منوی Dropdown کند)
 */
@Composable
private fun OrganizationsQuickFilterChips(
    selectedStatus: OrganizationStatus?,
    statusOptions: List<OrganizationStatus>,
    isWarningOnly: Boolean,
    onSelectStatus: (OrganizationStatus) -> Unit,
    onToggleWarningOnly: () -> Unit,
    onResetFilters: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // چیپ اخطاردارها
            item {
                FilterChip(
                    selected = isWarningOnly,
                    onClick = onToggleWarningOnly,
                    label = {
                        Text(
                            text = stringResource(R.string.label_has_warnings),
                            style = typography.labelMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.WarningAmber,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (isWarningOnly) appColors.warning else appColors.textSecondary
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = appColors.warning.copy(alpha = 0.15f),
                        selectedLabelColor = appColors.warning
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (isWarningOnly) appColors.warning else appColors.border,
                        selectedBorderColor = appColors.warning,
                        enabled = true,
                        selected = isWarningOnly
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            // چیپ‌های وضعیت‌ها
            items(statusOptions) { status ->
                val isSelected = selectedStatus == status
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectStatus(status) },
                    label = {
                        Text(
                            text = stringResource(status.titleRes),
                            style = typography.labelMedium
                        )
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.primary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (isSelected) MaterialTheme.colorScheme.primary else appColors.border,
                        selectedBorderColor = MaterialTheme.colorScheme.primary,
                        enabled = true,
                        selected = isSelected
                    )
                )
            }
        }
    }
}

/**
 * کارت مدرن سازمان با پشتیبانی از اقدامات سریع (تماس، نقشه) و چیدمان روان
 */
@Composable
fun ModernOrganizationCard(
    baseUrl: String,
    item: OrganizationWithDetail,
    number: Int,
    thumbnail: AttachmentFileEntity? = null,
    onClick: () -> Unit,
    onPhoneClick: (String) -> Unit,
    onNavigateMap: (Double, Double) -> Unit
) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme
    val isPendingSync = item.organization.syncStatus != SyncStatus.SYNCED

    val warningCount = item.organization.countWarning ?: 0
    val orgGrade = item.organization.grade ?: 0
    val orgName = item.organization.name.orEmpty()
    val phone = item.organization.phone?.trim()

    val lat = item.organization.latitude
    val lng = item.organization.longitude
    val hasValidLocation = lat != 0.0 && lng != 0.0

    // ساخت آدرس سرور کاملاً امن بر اساس شناسه یا مسیر نسبی
    val serverImageUrl: String? = when {
        thumbnail == null -> null
        !thumbnail.attachmentFileId.isNullOrBlank() -> {
            "${baseUrl.trimEnd('/')}/api/OrgAttachmentFiles/download/${thumbnail.attachmentFileId}"
        }

        !thumbnail.fileUrl.isNullOrBlank() -> {
            val path = thumbnail.fileUrl!!.removePrefix("/")
            "${baseUrl.trimEnd('/')}/$path"
        }

        else -> null
    }

    val imageModel: Any = when {
        !thumbnail?.localPath.isNullOrBlank() && File(thumbnail!!.localPath!!).exists() -> {
            File(thumbnail.localPath!!)
        }

        !serverImageUrl.isNullOrBlank() -> serverImageUrl
        else -> R.drawable.ic_logo_placeholder
    }

    val locationText = when {
        !item.organization.address.isNullOrBlank() -> item.organization.address
        item.cityName.isNotBlank() || item.regionName.isNotBlank() -> {
            listOf(item.cityName, item.regionName).filter { it.isNotBlank() }.joinToString("، ")
        }

        else -> "-"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface),
        border = BorderStroke(
            width = if (isPendingSync) 1.dp else 0.7.dp,
            color = if (isPendingSync) appColors.warning.copy(alpha = 0.7f) else appColors.border
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // ردیف اول: تصویر، عنوان، رتبه و نشان وضعیت
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // آواتار سازمان
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageModel)
                        .crossfade(true)
                        .placeholder(R.drawable.ic_logo_placeholder)
                        .error(R.drawable.ic_logo_placeholder)
                        .build(),
                    contentDescription = orgName,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$number. $orgName",
                            style = typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = colors.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        StatusBadge(status = OrganizationStatus.fromId(item.organization.status))
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Rating(orgGrade)

                        if (warningCount > 0) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = appColors.warning.copy(alpha = 0.15f),
                                border = BorderStroke(width = 0.5.dp, color = appColors.warning)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.WarningAmber,
                                        contentDescription = null,
                                        tint = appColors.warning,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = "$warningCount تذکر",
                                        style = typography.labelSmall,
                                        color = appColors.warning,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = appColors.border.copy(alpha = 0.5f), thickness = 0.6.dp)
            Spacer(modifier = Modifier.height(8.dp))

            // ردیف دوم: آدرس سازمان + اکشن‌های سریع (تماس و مسیریابی)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // آیکون و متن آدرس
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = appColors.textSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = locationText,
                        style = typography.bodySmall,
                        color = appColors.textSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // دکمه‌های اکشن سریع میدانی (Quick Actions)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!phone.isNullOrBlank()) {
                        IconButton(
                            onClick = { onPhoneClick(phone) },
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "تماس سریع",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    if (hasValidLocation) {
                        IconButton(
                            onClick = { onNavigateMap(lat!!, lng!!) },
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    color = appColors.success.copy(alpha = 0.15f),
                                    shape = CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Directions,
                                contentDescription = "مسیریابی",
                                tint = appColors.success,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // نشانگر تغییرات آفلاین
            if (isPendingSync) {
                Spacer(modifier = Modifier.height(8.dp))
                OfflinePendingFooterBanner()
            }
        }
    }
}
