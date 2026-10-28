package ir.kitgroup.partnerManagement.feature.organization.ui.detail

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import ir.kitgroup.partnerManagement.navigation.Screen
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import ir.kitgroup.partnerManagement.core.ui.SessionViewModel
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomDescriptionField
import ir.kitgroup.partnerManagement.core.ui.components.CustomOutlinedButton
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationDetailTab
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationStatus
import ir.kitgroup.partnerManagement.core.ui.util.UserRole
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.database.entity.VisitSubjectEntity
import ir.kitgroup.partnerManagement.core.database.model.AssignedStandWithDetail
import ir.kitgroup.partnerManagement.core.database.model.WarningWithDetail
import ir.kitgroup.partnerManagement.core.ui.components.DatePickerDialog
import ir.kitgroup.partnerManagement.core.ui.util.SecondaryButtonVariant
import ir.kitgroup.partnerManagement.feature.organization.ui.detail.stand.OrganizationStandsTabContent
import ir.kitgroup.partnerManagement.feature.organization.ui.detail.ticket.OrganizationTicketIssueTabContent
import ir.kitgroup.partnerManagement.feature.organization.ui.detail.visitor.OrganizationVisitorTabContent
import ir.kitgroup.partnerManagement.feature.organization.ui.detail.warning.OrganizationWarningTabContent
import ir.kitgroup.partnerManagement.core.ui.components.DualActionButtonBottomBar
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.convertMillisToIso
import ir.kitgroup.partnerManagement.core.ui.util.convertMillisToJalali
import ir.kitgroup.partnerManagement.core.ui.util.jalaliToMillis
import ir.kitgroup.partnerManagement.feature.organization.ui.detail.contract.OrganizationContractsTabContent
import ir.kitgroup.partnerManagement.feature.organization.ui.detail.meeting.OrganizationMeetingsTabContent
import ir.kitgroup.partnerManagement.feature.organization.ui.detail.person.OrganizationPersonsTabContent
import ir.kitgroup.partnerManagement.feature.organization.ui.detail.serial.OrganizationSerialsTabContent
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import ir.kitgroup.partnerManagement.core.ui.components.*

@Composable
fun OrganizationDetailScreen(
    organizationId: String,
    onEditClick: (organizationId: String) -> Unit,
    onDisableClick: () -> Unit,
    onStatusChangeSuccess: () -> Unit,
    onMeetingClick: (String) -> Unit,
    onAddMeetingClick: () -> Unit,
    onAssignVisitorClick: (String) -> Unit,
    onEditVisitorClick: (String) -> Unit,
    onDeleteVisitorClick: (String) -> Unit,
    onAssignStandsClick: (String) -> Unit,
    onAssignContractClick: (String) -> Unit,
    onViewItemDetailsClick: (AssignedStandWithDetail) -> Unit,
    onContractClick: (ContractEntity) -> Unit,
    onAddTicketOfferClick: () -> Unit,
    sessionViewModel: SessionViewModel = hiltViewModel(),
    organizationDetailViewModel: OrganizationDetailViewModel = hiltViewModel(),
    navController: NavController
) {
    val baseUrl by organizationDetailViewModel.baseUrlFlow.collectAsStateWithLifecycle()
    val detailUiState by organizationDetailViewModel.uiState.collectAsStateWithLifecycle()
    val organization by organizationDetailViewModel.organization.collectAsState()

    var warningToEdit by remember { mutableStateOf<WarningWithDetail?>(null) }

    LaunchedEffect(organizationId) {
        organizationDetailViewModel.getOrganizationById(organizationId)
        organizationDetailViewModel.loadAttachments()
    }

    val appColors = LocalPartnerManagementColors.current
    val roleCode by sessionViewModel.roleCode.collectAsState()
    val currentRole = remember(roleCode) { UserRole.fromCode(roleCode.toString()) }
    val isSupervisor = currentRole == UserRole.SUPERVISOR

    var selectedTab by rememberSaveable { mutableStateOf(OrganizationDetailTab.BASIC_INFO) }
    val visibleTabs = remember(isSupervisor) {
        OrganizationDetailTab.entries.filter { tab ->
            tab != OrganizationDetailTab.VISITOR || isSupervisor
        }
    }

    var showStatusDialog by rememberSaveable { mutableStateOf(false) }
    var showInactiveReasonDialog by rememberSaveable { mutableStateOf(false) }
    var currentOrganizationStatus by rememberSaveable { mutableStateOf(OrganizationStatus.ACTIVE) }

    val activeVisitorName by organizationDetailViewModel.activeVisitorName.collectAsState()
    val activeContractName by organizationDetailViewModel.activeContractName.collectAsState()
    val visitSubjects by organizationDetailViewModel.visitSubjects.collectAsStateWithLifecycle()

    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val uiStateChange by organizationDetailViewModel.uiStateChange.collectAsStateWithLifecycle()

    LaunchedEffect(organizationDetailViewModel.uiEvent) {
        organizationDetailViewModel.uiEvent.collect { event ->
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        bottomBar = {
            if (organization != null && selectedTab == OrganizationDetailTab.BASIC_INFO) {
                Surface(
                    color = appColors.screenBackground,
                    tonalElevation = 4.dp,
                    shadowElevation = 8.dp
                ) {
                    DualActionButtonBottomBar(
                        primaryText = stringResource(R.string.label_edit_organization),
                        secondaryText = stringResource(R.string.label_determination_organization_status),
                        primaryIcon = Icons.Outlined.Edit,
                        secondaryIcon = Icons.Outlined.Cancel,
                        secondaryVariant = SecondaryButtonVariant.FILLED,
                        buttonHeight = 44.dp,
                        onPrimaryClick = { onEditClick(organizationId) },
                        onSecondaryClick = {
                            if (!uiStateChange.isChangeStatus) {
                                showStatusDialog = true
                            }
                        }
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                ),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = appColors.screenBackground
        ) {
            if (organization == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                val currentOrganization = organization!!

                Column(modifier = Modifier.fillMaxSize()) {
                    LaunchedEffect(isSupervisor) {
                        if (!isSupervisor && selectedTab == OrganizationDetailTab.VISITOR) {
                            selectedTab = OrganizationDetailTab.BASIC_INFO
                        }
                    }

                    // هدر یکپارچه سازمان در بالای صفحه برای دسترسی سریع میدانی
                    OrganizationSummaryHeader(
                        organization = currentOrganization
                    )

                    // تب‌های افقی مدرن
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(appColors.screenBackground)
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 2.dp)
                        ) {
                            items(
                                items = visibleTabs,
                                key = { it.name }
                            ) { tab ->
                                OrganizationDetailTabItem(
                                    tab = tab,
                                    selected = selectedTab == tab,
                                    onClick = { selectedTab = tab }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // محتوای تب انتخاب‌شده
                    Box(modifier = Modifier.fillMaxSize()) {
                        when (selectedTab) {
                            OrganizationDetailTab.BASIC_INFO -> {
                                OrganizationBasicInfoTabContent(
                                    organization = currentOrganization,
                                    activeVisitorName = activeVisitorName,
                                    activeContractName = activeContractName,
                                    isImagesLoading = detailUiState.isImagesLoading,
                                    attachments = detailUiState.attachments,
                                    baseUrl = baseUrl,
                                    onDownloadAttachment = { attachmentId, fileExtension ->
                                        organizationDetailViewModel.downloadAttachmentManually(
                                            attachmentId,
                                            fileExtension
                                        )
                                    },
                                    onLocationClick = { lat, lng ->
                                        navController.navigate("map_screen?lat=$lat&lng=$lng&isReadOnly=true")
                                    }
                                )
                            }

                            OrganizationDetailTab.GENERAL_INFO -> {
                                OrganizationGeneralInfoTabContent(organization = currentOrganization)
                            }

                            OrganizationDetailTab.VISITS -> {
                                OrganizationMeetingsTabContent(
                                    organizationId = organizationId,
                                    onAddMeetingClick = onAddMeetingClick,
                                    onMeetingClick = onMeetingClick
                                )
                            }

                            OrganizationDetailTab.PERSONS -> {
                                OrganizationPersonsTabContent(organizationId = organizationId)
                            }

                            OrganizationDetailTab.STANDS -> {
                                OrganizationStandsTabContent(
                                    organizationId = organizationId,
                                    onAssignStandsClick = { onAssignStandsClick(organizationId) },
                                    onViewItemDetailsClick = { item -> onViewItemDetailsClick(item) }
                                )
                            }

                            OrganizationDetailTab.CONTRACTS -> {
                                OrganizationContractsTabContent(
                                    organizationId = organizationId,
                                    onAssignContractClick = { onAssignContractClick(organizationId) },
                                    onContractClick = { contract -> onContractClick(contract) }
                                )
                            }

                            OrganizationDetailTab.WARNING -> {
                                OrganizationWarningTabContent(
                                    organizationId = organizationId,
                                    isSupervisor = isSupervisor,
                                    onAddWarningClick = {
                                        warningToEdit = null
                                        // showWarningDialog = true
                                    },
                                    onEditWarningClick = { item ->
                                        warningToEdit = item
                                        // showWarningDialog = true
                                    }
                                )
                            }

                            OrganizationDetailTab.SERIALS -> {
                                OrganizationSerialsTabContent(
                                    organizationId = organizationId,
                                    onAddTicketClick = {
                                        val activeContractId =
                                            organization?.organization!!.activeContractId
                                        val hasActiveContract = !activeContractId.isNullOrBlank() &&
                                                activeContractId != "00000000-0000-0000-0000-000000000000"

                                        if (hasActiveContract) {
                                            onAddTicketOfferClick()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "این سازمان قرارداد همکاری فعالی ندارد. ابتدا قرارداد ثبت کنید.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    },
                                    onTicketClick = { ticket ->
                                        navController.navigate(
                                            Screen.AssignedSerialDetail.createRoute(
                                                ticket.assignedSerial.serialAssignmentId
                                            )
                                        )
                                    }
                                )
                            }

                            OrganizationDetailTab.TICKET -> {
                                OrganizationTicketIssueTabContent()
                            }

                            OrganizationDetailTab.VISITOR -> {
                                OrganizationVisitorTabContent(
                                    onAssignVisitorClick = { onAssignVisitorClick(organizationId) },
                                    onEditVisitorClick = { id -> onEditVisitorClick(id) },
                                    onDeleteVisitorClick = { id -> onDeleteVisitorClick(id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showStatusDialog) {
        ChangeOrganizationStatusDialog(
            initialStatus = OrganizationStatus.ACTIVE,
            visitSubjects = visitSubjects,
            initialReason = "",
            isSubmitting = uiStateChange.isChangeStatus,
            onDismiss = {
                if (!uiStateChange.isChangeStatus) {
                    showStatusDialog = false
                }
            },
            onConfirm = { newStatus, deactiveDate, reason, visitDate, subjectVisitId ->
                organizationDetailViewModel.changeStatusOrganization(
                    organizationId = organizationId,
                    status = newStatus,
                    deactiveDate = deactiveDate,
                    reason = reason,
                    visitDate = visitDate,
                    subjectVisitId = subjectVisitId,
                    onErrorCallback = { showStatusDialog = false },
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
 * هدر خلاصه سازمان با دسترسی سریع به تماس و نقشه
 */
@Composable
private fun OrganizationSummaryHeader(
    organization: ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail,
) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme

    val lightGreenBorder = appColors.success.copy(alpha = 0.25f)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(0.8.dp, lightGreenBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = organization.organization.name.orEmpty(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = colors.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                StatusBadge(status = OrganizationStatus.fromId(organization.organization.status))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Rating(organization.organization.grade ?: 0)
            }
        }
    }
}


@Composable
private fun OrganizationDetailTabItem(
    tab: OrganizationDetailTab,
    selected: Boolean,
    onClick: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current

    val backgroundColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primary else appColors.cardBackground,
        animationSpec = tween(250),
        label = "tabBackground"
    )

    val contentColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.onPrimary else appColors.textPrimary,
        animationSpec = tween(200),
        label = "tabContent"
    )

    val elevation by animateDpAsState(
        targetValue = if (selected) 3.dp else 0.dp,
        animationSpec = tween(250),
        label = "tabElevation"
    )

    Surface(
        modifier = Modifier
            .height(38.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(10.dp),
        color = backgroundColor,
        shadowElevation = elevation,
        border = if (!selected) BorderStroke(1.dp, appColors.border) else null
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 14.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(tab.titleRes),
                color = contentColor,
                style = if (selected) MaterialTheme.typography.titleMedium else MaterialTheme.typography.labelMedium,
                maxLines = 1
            )
        }
    }
}

@Composable
fun ChangeOrganizationStatusDialog(
    initialStatus: OrganizationStatus = OrganizationStatus.ACTIVE,
    visitSubjects: List<VisitSubjectEntity>,
    initialReason: String = "",
    isSubmitting: Boolean = false,
    onDismiss: () -> Unit,
    onConfirm: (
        status: OrganizationStatus,
        deactiveDate: String?,
        reason: String?,
        visitDate: String?,
        subjectVisitId: String?
    ) -> Unit
) {
    val statusEntries = remember {
        OrganizationStatus.entries.filter { it != OrganizationStatus.INITIAL_REGISTRATION }
    }
    val statusTitles = statusEntries.map { stringResource(it.titleRes) }

    var selectedStatus by rememberSaveable { mutableStateOf(initialStatus) }
    var inactiveReason by rememberSaveable { mutableStateOf(initialReason) }
    var deactiveDateMillis by rememberSaveable { mutableStateOf(System.currentTimeMillis()) }
    var visitDateMillis by rememberSaveable { mutableStateOf(System.currentTimeMillis()) }
    var selectedSubjectId by rememberSaveable { mutableStateOf<String?>(null) }

    var reasonError by remember { mutableStateOf(false) }
    var subjectError by remember { mutableStateOf(false) }

    var showDeactiveDatePicker by remember { mutableStateOf(false) }
    var showVisitDatePicker by remember { mutableStateOf(false) }

    val isInactive = selectedStatus == OrganizationStatus.INACTIVE
    val isUrgentReferral = selectedStatus == OrganizationStatus.ORANGE

    val deactiveDateIso = remember(deactiveDateMillis) { convertMillisToIso(deactiveDateMillis) }
    val visitDateIso = remember(visitDateMillis) { convertMillisToIso(visitDateMillis) }

    val visitDateDisplay = remember(visitDateMillis) { convertMillisToJalali(visitDateMillis) }
    val deactiveDateDisplay =
        remember(deactiveDateMillis) { convertMillisToJalali(deactiveDateMillis) }

    Dialog(onDismissRequest = onDismiss) {
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
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.label_determination_organization_status),
                    style = MaterialTheme.typography.titleMedium,
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
                        if (index != -1) {
                            selectedStatus = statusEntries[index]
                            reasonError = false
                            subjectError = false
                        }
                    }
                )

                AnimatedVisibility(visible = isInactive) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        StatusDateRow(
                            label = "تاریخ غیرفعال‌سازی: $deactiveDateDisplay",
                            onClick = { showDeactiveDatePicker = true }
                        )
                        CustomDescriptionField(
                            value = inactiveReason,
                            onValueChange = {
                                inactiveReason = it
                                reasonError = false
                            },
                            label = stringResource(R.string.label_inactivation_reason_field),
                            placeholder = ""
                        )
                        if (reasonError) {
                            Text(
                                text = stringResource(R.string.error_inactivation_reason_required),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }

                AnimatedVisibility(visible = isUrgentReferral) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        StatusDateRow(
                            label = "تاریخ بازدید: $visitDateDisplay",
                            onClick = { showVisitDatePicker = true }
                        )
                        DropdownSelectorField(
                            modifier = Modifier.fillMaxWidth(),
                            value = visitSubjects.firstOrNull { it.subjectVisitId == selectedSubjectId }?.title
                                ?: "",
                            label = "موضوع بازدید",
                            placeholder = "",
                            items = visitSubjects.mapNotNull { it.title },
                            isRequired = true,
                            onItemSelected = { title ->
                                selectedSubjectId =
                                    visitSubjects.find { it.title == title }?.subjectVisitId
                                subjectError = false
                            }
                        )
                        if (subjectError) {
                            Text(
                                text = "انتخاب موضوع بازدید الزامی است.",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }

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
                        onClick = {
                            when {
                                isInactive && inactiveReason.trim().isBlank() -> {
                                    reasonError = true
                                }

                                isUrgentReferral && selectedSubjectId == null -> {
                                    subjectError = true
                                }

                                isInactive -> {
                                    onConfirm(
                                        selectedStatus,
                                        deactiveDateIso,
                                        inactiveReason.trim(),
                                        null,
                                        null
                                    )
                                }

                                isUrgentReferral -> {
                                    onConfirm(
                                        selectedStatus,
                                        null,
                                        null,
                                        visitDateIso,
                                        selectedSubjectId
                                    )
                                }

                                else -> {
                                    onConfirm(selectedStatus, null, null, null, null)
                                }
                            }
                        },
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

    if (showDeactiveDatePicker) {
        DatePickerDialog(
            initialMillis = deactiveDateMillis,
            onDismiss = { showDeactiveDatePicker = false },
            onDateSelected = { jalali ->
                deactiveDateMillis = jalaliToMillis(jalali)
                showDeactiveDatePicker = false
            }
        )
    }

    if (showVisitDatePicker) {
        DatePickerDialog(
            initialMillis = visitDateMillis,
            onDismiss = { showVisitDatePicker = false },
            onDateSelected = { jalali ->
                visitDateMillis = jalaliToMillis(jalali)
                showVisitDatePicker = false
            }
        )
    }
}

@Composable
private fun StatusDateRow(label: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, LocalPartnerManagementColors.current.border)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

