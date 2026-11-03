package ir.kitgroup.partnerManagement.feature.meeting.ui.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Subject
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.model.MeetingWithDetail
import ir.kitgroup.partnerManagement.core.ui.components.*
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.MeetingStatus
import ir.kitgroup.partnerManagement.core.ui.util.MeetingType
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import ir.kitgroup.partnerManagement.core.network.model.AttachmentFileDto
import ir.kitgroup.partnerManagement.core.ui.util.SecondaryButtonVariant
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.WarningAmber
import ir.kitgroup.partnerManagement.feature.organization.ui.detail.warning.AddEditOrganizationWarningDialog
import ir.kitgroup.partnerManagement.feature.organization.ui.detail.warning.OrganizationWarningViewModel

@Composable
fun MeetingDetailScreen(
    meetingId: String,
    onEditClick: (String) -> Unit,
    onOrganizationClick: (String) -> Unit = {},
    onLocationClick: (latitude: Double, longitude: Double) -> Unit = { _, _ -> },
    onAssignStandClick: (organizationId: String, meetingId: String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier,
    onStatusChangeSuccess: () -> Unit,
    viewModel: MeetingDetailViewModel = hiltViewModel(),
    organizationWarningViewModel: OrganizationWarningViewModel = hiltViewModel()
) {
    val appColors = LocalPartnerManagementColors.current
    val context = LocalContext.current

    val meeting by viewModel.meeting.collectAsStateWithLifecycle()
    var showWarningDialog by rememberSaveable { mutableStateOf(false) }
    val uiState by organizationWarningViewModel.uiState.collectAsStateWithLifecycle()

    var showStatusDialog by rememberSaveable { mutableStateOf(false) }
    val uiStateChange by viewModel.uiStateChange.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    val detailUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val baseUrl by viewModel.baseUrlFlow.collectAsStateWithLifecycle()

    LaunchedEffect(meetingId) {
        if (meetingId.isNotBlank()) {
            viewModel.observeMeetingById(meetingId)
        }
    }

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
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

    val currentMeeting = meeting

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        bottomBar = {
            // نمایش BottomBar تنها در صورت وجود داده
            if (currentMeeting != null) {
                DualActionButtonBottomBar(
                    primaryText = stringResource(R.string.label_edit_visit),
                    secondaryText = stringResource(R.string.label_determination_status),
                    primaryIcon = Icons.Outlined.Edit,
                    secondaryIcon = Icons.Outlined.Cancel,
                    secondaryVariant = SecondaryButtonVariant.FILLED,
                    buttonHeight = 44.dp,
                    onPrimaryClick = { onEditClick(currentMeeting.meeting.meetingId) },
                    onSecondaryClick = {
                        if (!uiStateChange.isChangeStatus) {
                            showStatusDialog = true
                        }
                    }
                )
            }
        },

        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                ),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = appColors.screenBackground
        ) {
            if (currentMeeting == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    BouncingDotsIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    OrganizationInfoCard(
                        item = currentMeeting,
                        onOrganizationClick = onOrganizationClick
                    )

                    Spacer(Modifier.height(12.dp))
                    MeetingQuickActionsCard(
                        onAssignStand = {
                            onAssignStandClick(
                                currentMeeting.meeting.organizationId!!,
                                currentMeeting.meeting.meetingId
                            )
                        },
                        onAddWarning = {
                            organizationWarningViewModel.resetForm(
                                targetOrgId = currentMeeting.meeting.organizationId,
                                // این مهم است: meetingId را اینجا پاس می‌دهیم
                                targetMeetingId = currentMeeting.meeting.meetingId
                            )
                            showWarningDialog = true
                        }
                    )
                    Spacer(Modifier.height(12.dp))

                    VisitorInfoCard(
                        item = currentMeeting,
                        isImagesLoading = detailUiState.isImagesLoading,
                        attachments = detailUiState.attachments,
                        baseUrl = baseUrl,
                        onDownloadAttachment = { attachmentId, fileExtension ->
                            viewModel.downloadAttachmentManually(attachmentId, fileExtension)
                        }
                    )
                    val lat = currentMeeting.meeting.latitude
                    val lng = currentMeeting.meeting.longitude

                    if (lat != null && lng != null && lat != 0.0 && lng != 0.0) {
                        Spacer(Modifier.height(12.dp))

                        SectionTitle(stringResource(R.string.label_location))

                        Spacer(Modifier.height(8.dp))

                        MeetingLocationCard(
                            latitude = lat,
                            longitude = lng,
                            onLocationClick = onLocationClick
                        )
                    }

                    currentMeeting.meeting.description?.takeIf { it.isNotBlank() }?.let { desc ->
                        Spacer(Modifier.height(12.dp))

                        SectionTitle(stringResource(R.string.label_description))

                        Spacer(Modifier.height(8.dp))

                        CustomDescriptionCard(desc)
                    }

                    Spacer(Modifier.height(16.dp))
                }
            }
        }
    }
    if (showWarningDialog && currentMeeting != null) {
        key(uiState.warningRecordId ?: "new_warning_${showWarningDialog}") {
            AddEditOrganizationWarningDialog(
                warningTypes = uiState.warningTypes,
                isSubmitting = uiState.isLoading,
                initialWarningId = uiState.warningTypeId.takeIf { it.isNotBlank() },
                initialDescription = uiState.description,
                onDismiss = {
                    showWarningDialog = false
                    organizationWarningViewModel.resetForm(currentMeeting.meeting.organizationId)
                },
                onConfirm = { warningId, description ->
                    organizationWarningViewModel.submitWarning(

                        organizationId = currentMeeting.meeting.organizationId,
                        warningTypeId = warningId,
                        description = description,
                        onSuccessCallback = {
                            showWarningDialog = false
                            organizationWarningViewModel.resetForm(currentMeeting.meeting.organizationId)
                        }
                    )
                }
            )
        }
    }

    if (showStatusDialog && currentMeeting != null) {
        val initialStatus =
            MeetingStatus.fromId(currentMeeting.meeting.status)
        ChangeMeetingStatusDialog(
            initialStatus = initialStatus,
            isSubmitting = uiStateChange.isChangeStatus,
            onDismiss = { if (!uiStateChange.isChangeStatus) showStatusDialog = false },
            onConfirm = { newStatusId ->
                viewModel.changeStatusMeeting(
                    meetingId = currentMeeting.meeting.meetingId,
                    newStatus = newStatusId.id,
                    onErrorCallback = {
                        showStatusDialog = false
                    },
                    onSuccessCallback = {
                        showStatusDialog = false
                        onStatusChangeSuccess()
                    }
                )
            }
        )
    }
}

/**
 * دیالوگ انتخاب وضعیت جدید بازدید
 */
@Composable
fun ChangeMeetingStatusDialog(
    initialStatus: MeetingStatus,
    isSubmitting: Boolean = false,
    onDismiss: () -> Unit,
    onConfirm: (MeetingStatus) -> Unit
) {
    var selectedStatus by rememberSaveable { mutableStateOf(initialStatus) }
    val statusEntries = remember { MeetingStatus.entries }
    val statusTitles = statusEntries.map { stringResource(it.titleRes) }

    Dialog(
        onDismissRequest = {
            if (!isSubmitting) onDismiss()
        }
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "تعیین وضعیت بازدید",
                    style = typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                DropdownSelectorField(
                    modifier = Modifier.fillMaxWidth(),
                    value = stringResource(selectedStatus.titleRes),
                    label = stringResource(R.string.label_status),
                    placeholder = "",
                    items = statusTitles,
                    isRequired = true,
                    onItemSelected = { selectedTitle ->
                        val index = statusTitles.indexOf(selectedTitle)
                        if (index >= 0) {
                            selectedStatus = statusEntries[index]
                        }
                    }
                )

                if (isSubmitting) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CustomButton(
                        text = stringResource(R.string.label_registration),
                        enabled = !isSubmitting,
                        onClick = { onConfirm(selectedStatus) },
                        modifier = Modifier.weight(1f)
                    )

                    CustomOutlinedButton(
                        text = stringResource(R.string.label_cancel),
                        enabled = !isSubmitting,
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun OrganizationInfoCard(
    item: MeetingWithDetail,
    onOrganizationClick: (String) -> Unit
) {
    val appColors = LocalPartnerManagementColors.current

    val locationText = item.organizationAddress
        .takeIf { it.isNotBlank() }
        ?: listOf(
            item.cityName,
            item.regionName
        )
            .filter { it.isNotBlank() }
            .joinToString("، ")
            .ifBlank { "-" }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onOrganizationClick(item.meeting.organizationId!!)
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(width = 0.7.dp, color = appColors.border)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = item.organizationName,
                    style = typography.titleLarge,
                    color = appColors.textPrimary
                )

                Spacer(Modifier.height(6.dp))

                LocationRow(location = locationText)

                Spacer(Modifier.height(8.dp))

                Rating(item.organizationGrade)
            }
        }
    }
}

@Composable
private fun VisitorInfoCard(
    item: MeetingWithDetail,
    isImagesLoading: Boolean,
    attachments: List<AttachmentFileDto>,
    baseUrl: String,
    onDownloadAttachment: (attachmentId: String, extension: String?) -> Unit
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(width = 0.7.dp, color = appColors.border)
    ) {
        Column {
            val meetingType = MeetingType.fromValue(item.meeting.type)

            InfoRow(
                label = stringResource(R.string.label_visit_type),
                value = stringResource(id = meetingType.titleRes),
                icon = meetingType.icon
            )

            HorizontalDivider(color = appColors.border.copy(alpha = 0.5f))

            InfoRow(
                label = stringResource(R.string.label_visit_subject),
                value = item.subjectVisitName,
                icon = Icons.Default.Subject
            )

            val hasVisitor = !item.meeting.visitorName.isNullOrBlank() &&
                    item.meeting.visitorId != null

            if (hasVisitor) {
                HorizontalDivider(color = appColors.border.copy(alpha = 0.5f))

                InfoRow(
                    label = stringResource(R.string.label_visitor_name),
                    value = item.meeting.visitorName,
                    icon = Icons.Default.Badge
                )
            }
            val hasPerson = !item.meeting.organizationPersonName.isNullOrBlank() &&
                    item.meeting.organizationPersonId != null

            if (hasPerson) {
                HorizontalDivider(color = appColors.border.copy(alpha = 0.5f))

                InfoRow(
                    label = stringResource(R.string.label_organization_person) + ":",
                    value = item.meeting.organizationPersonName,
                    icon = Icons.Default.Badge
                )
            }

            HorizontalDivider(color = appColors.border.copy(alpha = 0.5f))

            InfoRow(
                label = stringResource(R.string.label_visit_Scheduled_date),
                value = formatJalaliDate(item.meeting.visitDate),
                icon = Icons.Default.Event
            )

            HorizontalDivider(color = appColors.border.copy(alpha = 0.5f))

            InfoRow(
                label = stringResource(R.string.label_visit_real_date),
                value = formatJalaliDate(item.meeting.visitRealDate),
                icon = Icons.Default.EventAvailable
            )

            HorizontalDivider(color = appColors.border.copy(alpha = 0.5f))

            InfoRow(
                label = stringResource(R.string.label_visit_time),
                value = item.meeting.visitTime,
                icon = Icons.Default.Schedule
            )

            HorizontalDivider(color = appColors.border.copy(alpha = 0.5f))

            InfoRow(
                label = stringResource(R.string.label_visit_status),
                value = null,
                icon = Icons.Default.EventAvailable,
                status = item.meeting.status
            )
            HorizontalDivider(color = appColors.border.copy(alpha = 0.5f))

            if (attachments.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 14.dp),
                ) {
                    SectionTitle(stringResource(R.string.label_attachment))
                    Spacer(Modifier.height(8.dp))
                    ImageDetailSection(
                        isLoading = isImagesLoading,
                        attachments = attachments,
                        baseUrl = baseUrl,
                        onDownloadClick = onDownloadAttachment
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String? = null,
    icon: ImageVector,
    status: Int? = null
) {
    val appColors = LocalPartnerManagementColors.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "$label :",
                style = typography.labelMedium,
                color = appColors.textSecondary
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        if (status != null) {
            StatusBadge(status = MeetingStatus.fromId(status))
        } else {
            Text(
                text = value ?: "-",
                style = typography.labelMedium,
                color = appColors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
private fun MeetingLocationCard(
    latitude: Double,
    longitude: Double,
    onLocationClick: (latitude: Double, longitude: Double) -> Unit
) {
    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme
    val appColors = LocalPartnerManagementColors.current

    val customMarkerDrawable = remember(context) {
        ContextCompat.getDrawable(context, R.drawable.ic_location_pin)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onLocationClick(latitude, longitude)
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(width = 0.7.dp, color = appColors.border)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(16.dp))
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
                        isHorizontalMapRepetitionEnabled = false
                        isVerticalMapRepetitionEnabled = false
                        setBuiltInZoomControls(false)
                        controller.setZoom(15.0)
                        val point = GeoPoint(latitude, longitude)
                        controller.setCenter(point)

                        val marker = Marker(this).apply {
                            position = point
                            customMarkerDrawable?.let { icon = it }
                            setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                            closeInfoWindow()
                        }
                        overlays.add(marker)
                        onResume()
                    }
                },
                update = { mapView ->
                    val point = GeoPoint(latitude, longitude)
                    mapView.controller.setCenter(point)
                    val marker = mapView.overlays.filterIsInstance<Marker>().firstOrNull()
                    if (marker != null) {
                        marker.position = point
                        customMarkerDrawable?.let { marker.icon = it }
                    }
                    mapView.postInvalidate()
                },
                onRelease = { mapView ->
                    mapView.onPause()
                    mapView.onDetach()
                }
            )
        }
    }
}


@Composable
fun MeetingQuickActionsCard(
    onAddWarning: () -> Unit,
    onAssignStand: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(
            width = 0.8.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
                )
                Text(
                    text = "عملیات برای این بازدید",
                    style = typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {


                // تخصیص استند
                ActionItemButton(
                    title = "تخصیص استند",
                    icon = Icons.Outlined.Campaign,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    onClick = onAssignStand,
                    modifier = Modifier.weight(1f)
                )

                // ثبت تذکر
                ActionItemButton(
                    title = "ثبت تذکر",
                    icon = Icons.Outlined.WarningAmber,
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    onClick = onAddWarning,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ActionItemButton(
    title: String,
    icon: ImageVector,
    containerColor: androidx.compose.ui.graphics.Color,

    contentColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = contentColor,
                modifier = Modifier.size(22.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = title,
                style = typography.titleMedium,
                color = contentColor,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

