package ir.kitgroup.partnerManagement.feature.meeting.ui

import android.widget.Toast
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
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
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
import ir.kitgroup.partnerManagement.core.ui.util.DataState
import ir.kitgroup.partnerManagement.core.ui.util.MeetingStatus
import ir.kitgroup.partnerManagement.core.ui.util.MeetingType
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate

@Composable
fun MeetingDetailScreen(
    meetingId: String,
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit,
    onStatusChanged: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MeetingDetailViewModel = hiltViewModel()
) {
    val appColors = LocalPartnerManagementColors.current
    val context = LocalContext.current

    val meeting by viewModel.meeting.collectAsStateWithLifecycle()
    val isUpdatingStatus by viewModel.isUpdatingStatus.collectAsStateWithLifecycle()
    val statusUpdateState by viewModel.statusUpdateState.collectAsStateWithLifecycle()

    var showStatusDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(meetingId) {
        if (meetingId.isNotBlank()) {
            viewModel.observeMeetingById(meetingId)
        }
    }

    // بازخورد به کاربر پس از موفقیت یا خطا در تغییر وضعیت
    LaunchedEffect(statusUpdateState) {
        when (val state = statusUpdateState) {
            is DataState.Success -> {
                showStatusDialog = false
                viewModel.resetStatusState()
                Toast.makeText(context, "وضعیت بازدید با موفقیت تغییر یافت", Toast.LENGTH_SHORT)
                    .show()
                onStatusChanged()
            }

            is DataState.Error -> {
                Toast.makeText(context, state.error.asString(context), Toast.LENGTH_LONG).show()
                viewModel.resetStatusState()
            }

            else -> Unit
        }
    }

    val currentMeeting = meeting

    Scaffold(
        topBar = {
            CustomHeader(
                title = R.string.label_meeting_details,
                showBackButton = true,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            if (currentMeeting != null) {
                Surface(
                    color = appColors.screenBackground,
                    tonalElevation = 4.dp,
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // دکمه ویرایش بازدید
                        CustomButton(
                            text = stringResource(R.string.label_edit_visit),
                            onClick = { onEditClick(currentMeeting.meeting.meetingId) },
                            fillMaxWidth = false,
                            modifier = Modifier.weight(1f),
                            height = 42.dp,
                            cornerRadius = 12.dp,
                            textStyle = typography.titleLarge,
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )

                        // دکمه تعیین وضعیت بازدید
                        CustomButton(
                            text = stringResource(R.string.label_determination_status),
                            onClick = {
                                if (!isUpdatingStatus) {
                                    showStatusDialog = true
                                }
                            },
                            fillMaxWidth = false,
                            modifier = Modifier.weight(1f),
                            height = 42.dp,
                            cornerRadius = 12.dp,
                            textStyle = typography.titleLarge,
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = appColors.error,
                                contentColor = appColors.errorContainer
                            )
                        )
                    }
                }
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
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    OrganizationInfoCard(item = currentMeeting)

                    Spacer(Modifier.height(12.dp))

                    VisitorInfoCard(item = currentMeeting)

                    val lat =
                        currentMeeting.meeting.latitude?.replace(",", ".")?.toDoubleOrNull() ?: 0.0
                    val lng =
                        currentMeeting.meeting.longitude?.replace(",", ".")?.toDoubleOrNull() ?: 0.0
                    val hasValidLocation = lat != 0.0 && lng != 0.0

                    if (hasValidLocation) {
                        Spacer(Modifier.height(12.dp))

                        SectionTitle(stringResource(R.string.label_location))

                        Spacer(Modifier.height(8.dp))

                        LocationCard()
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

    // دیالوگ تغییر وضعیت بازدید
    if (showStatusDialog && currentMeeting != null) {
        val initialStatus =
            MeetingStatus.fromId(currentMeeting.meeting.status) ?: MeetingStatus.entries.first()
        ChangeMeetingStatusDialog(
            initialStatus = initialStatus,
            isSaving = isUpdatingStatus,
            onDismiss = { showStatusDialog = false },
            onConfirm = { selectedStatus ->
                /* viewModel.updateMeetingStatus(
                     meetingId = currentMeeting.meeting.meetingId,
                     newStatus = selectedStatus.id
                 ) {
                     // با استفاده از اثر LaunchedEffect رویداد موفقیت اجرا می‌شود
                 }*/
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
    isSaving: Boolean = false,
    onDismiss: () -> Unit,
    onConfirm: (MeetingStatus) -> Unit
) {
    var selectedStatus by rememberSaveable { mutableStateOf(initialStatus) }
    val statusEntries = remember { MeetingStatus.entries }
    val statusTitles = statusEntries.map { stringResource(it.titleRes) }

    Dialog(
        onDismissRequest = {
            if (!isSaving) onDismiss()
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

                if (isSaving) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CustomButton(
                        text = stringResource(R.string.label_registration),
                        enabled = !isSaving,
                        onClick = { onConfirm(selectedStatus) },
                        modifier = Modifier.weight(1f)
                    )

                    CustomOutlinedButton(
                        text = stringResource(R.string.label_cancel),
                        enabled = !isSaving,
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun OrganizationInfoCard(item: MeetingWithDetail) {
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
        modifier = Modifier.fillMaxWidth(),
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
private fun VisitorInfoCard(item: MeetingWithDetail) {
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
                    item.meeting.visitorId !=null

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
private fun LocationCard() {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(width = 0.7.dp, color = appColors.border)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(12.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(44.dp)
                    .align(Alignment.Center)
            )

            Icon(
                imageVector = Icons.Default.Fullscreen,
                contentDescription = null,
                tint = appColors.textPrimary,
                modifier = Modifier
                    .size(22.dp)
                    .align(Alignment.TopEnd)
            )
        }
    }
}
