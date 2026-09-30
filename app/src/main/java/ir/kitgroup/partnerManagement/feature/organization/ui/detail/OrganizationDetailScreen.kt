package ir.kitgroup.partnerManagement.feature.organization.ui.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
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
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
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
import ir.kitgroup.partnerManagement.core.ui.components.DeleteConfirmationDialog
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationDetailTab
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationStatus
import ir.kitgroup.partnerManagement.core.ui.util.UserRole
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.database.entity.VisitorOrganizationEntity
import ir.kitgroup.partnerManagement.core.database.model.AssignedStandWithDetail
import ir.kitgroup.partnerManagement.core.database.model.WarningWithDetail
import ir.kitgroup.partnerManagement.core.ui.util.demoVisitorOrganizations
import ir.kitgroup.partnerManagement.feature.organization.ui.AddOrganizationWarningDialog
import ir.kitgroup.partnerManagement.feature.organization.ui.AddPersonBottomSheet
import ir.kitgroup.partnerManagement.feature.organization.ui.InactivationReasonDialog
import ir.kitgroup.partnerManagement.feature.organization.ui.rememberAddOrganizationFormState
import ir.kitgroup.partnerManagement.feature.organization.ui.rememberAddPersonFormState


@Composable
fun OrganizationDetailScreen(
    organizationId: String,
    onBack: () -> Unit,
    onEditClick: () -> Unit,
    onDisableClick: () -> Unit,
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
    organizationDetailViewModel: OrganizationDetailViewModel = hiltViewModel()
) {

    val organization by organizationDetailViewModel.organization
        .collectAsState()

    val meetings by organizationDetailViewModel.meetings
        .collectAsStateWithLifecycle()


    val persons by organizationDetailViewModel.persons
        .collectAsStateWithLifecycle()

    val contracts by organizationDetailViewModel.contracts
        .collectAsStateWithLifecycle()


    val assignedStands by organizationDetailViewModel.assignedStands
        .collectAsStateWithLifecycle()

    val warnings by organizationDetailViewModel.warnings
        .collectAsStateWithLifecycle()

    val assignedSerials by organizationDetailViewModel.assignedSerials
        .collectAsStateWithLifecycle()


    LaunchedEffect(organizationId) {
        organizationDetailViewModel.getOrganizationById(organizationId)
        organizationDetailViewModel.observeOrganizationMeetings(organizationId)
        organizationDetailViewModel.observeOrganizationPersons(organizationId)
        organizationDetailViewModel.observeOrganizationContracts(organizationId)
        organizationDetailViewModel.observeOrganizationWarnings(organizationId)
        organizationDetailViewModel.observeOrganizationAssignedStands(organizationId)
        organizationDetailViewModel.observeOrganizationAssignedSerials(organizationId)
    }


    val currentOrganization = organization ?: return
    val appColors = LocalPartnerManagementColors.current
    val roleCode by sessionViewModel.roleCode.collectAsState()

    val currentRole = remember(roleCode) { UserRole.fromCode(roleCode.toString()) }
    val isSupervisor = currentRole == UserRole.SUPERVISOR

    val formState = rememberAddOrganizationFormState()
    val personState = rememberAddPersonFormState()

    var showAddPersonSheet by rememberSaveable { mutableStateOf(false) }

    var selectedTab by rememberSaveable { mutableStateOf(OrganizationDetailTab.BASIC_INFO) }
    val visibleTabs = remember(isSupervisor) {
        OrganizationDetailTab.entries.filter { tab ->
            tab != OrganizationDetailTab.VISITOR || isSupervisor
        }
    }
    var showWarningDialog by rememberSaveable { mutableStateOf(false) }

    // متغیرهای وضعیت برای کنترل دیالوگ‌ها
    var showStatusDialog by rememberSaveable { mutableStateOf(false) }
    var showInactiveReasonDialog by rememberSaveable { mutableStateOf(false) }
    var currentOrganizationStatus by rememberSaveable { mutableStateOf(OrganizationStatus.ACTIVE) }
    var currentInactiveReason by rememberSaveable { mutableStateOf("") }

    var warningPendingDelete by remember { mutableStateOf<WarningWithDetail?>(null) }
    var visitorToDelete by remember { mutableStateOf<VisitorOrganizationEntity?>(null) }



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomHeader(
                title = R.string.label_details,
                showBackButton = true,
                onBackClick = onBack
            )
        },
        bottomBar = {
            Surface(
                color = appColors.screenBackground,
                tonalElevation = 4.dp,
                shadowElevation = 8.dp
            ) {
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                    ActionButtons(
                        onEdit = onEditClick,
                        onDetermination = { showStatusDialog = true }
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.primary
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
            Column(modifier = Modifier.fillMaxSize()) {
                LaunchedEffect(isSupervisor) {
                    if (!isSupervisor && selectedTab == OrganizationDetailTab.VISITOR) {
                        selectedTab = OrganizationDetailTab.BASIC_INFO
                    }
                }
                CompositionLocalProvider(
                    LocalLayoutDirection provides LayoutDirection.Rtl
                ) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(appColors.screenBackground)
                            .padding(
                                horizontal = 12.dp,
                                vertical = 8.dp
                            ),
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
                                onClick = {
                                    selectedTab = tab
                                }
                            )
                        }
                    }
                }

                when (selectedTab) {
                    OrganizationDetailTab.BASIC_INFO -> {
                        OrganizationBasicInfoTabContent(
                            organization = currentOrganization
                        )
                    }

                    OrganizationDetailTab.GENERAL_INFO -> {
                        OrganizationGeneralInfoTabContent(organization = currentOrganization)
                    }

                    OrganizationDetailTab.VISITS -> {
                        OrganizationMeetingsTabContent(
                            meetings = meetings,
                            onAddMeetingClick = onAddMeetingClick,
                            onMeetingClick = onMeetingClick
                        )
                    }

                    OrganizationDetailTab.PERSONS -> {
                        OrganizationPersonsTabContent(
                            persons = persons,
                            onAddPersonClick = {
                                formState.resetPersonFields(personState)
                                showAddPersonSheet = true
                            },
                        )
                    }

                    OrganizationDetailTab.VISITOR -> {
                        if (isSupervisor) {

                            OrganizationVisitorTabContent(
                                visitors = demoVisitorOrganizations,
                                onAssignVisitorClick = {
                                    onAssignVisitorClick(organizationId)
                                },
                                onEditVisitorClick = { id -> onEditVisitorClick(id) },
                                onDeleteVisitorClick = { id -> onDeleteVisitorClick(id) }
                            )
                        }
                    }

                    OrganizationDetailTab.STANDS -> {
                        OrganizationStandsTabContent(assignments = assignedStands,
                            onAssignStandsClick = { onAssignStandsClick(organizationId) },
                            onViewItemDetailsClick = { item ->
                                onViewItemDetailsClick(item)
                            })
                    }

                    OrganizationDetailTab.CONTRACTS -> {
                        OrganizationContractsTabContent(contracts = contracts,
                            onAssignContractClick = { onAssignContractClick(organizationId) },
                            onContractClick = { contract ->
                                onContractClick(contract)
                            })
                    }

                    OrganizationDetailTab.SERIALS -> {
                        OrganizationSerialsTabContent(
                            serials = assignedSerials,
                            onAddTicketClick = {
                                onAddTicketOfferClick()
                            },
                            onTicketClick = { ticket ->
                                // اکشن در صورت نیاز به روت خارجی یا کار با دیتای آیتم انتخابی
                            },
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    OrganizationDetailTab.WARNING -> {
                        OrganizationWarningTabContent(
                            warnings = warnings,
                            isSupervisor = isSupervisor,
                            onAddWarningClick = { showWarningDialog = true },
                            onDeleteWarningClick = { warnings ->
                                warningPendingDelete = warnings
                            }
                        )
                    }
                }
            }
        }
    }
    if (showWarningDialog) {
        AddOrganizationWarningDialog(
            onDismiss = {
                showWarningDialog = false
            },
            onConfirm = { warningType, description ->

                organizationDetailViewModel.addWarning(
                    organizationId = organizationId,
                    organizationName = currentOrganization.organization.name,
                    warningTypeId = warningType,
                    warningTypeName = warningType,
                    description = description
                )

                showWarningDialog = false
            }
        )
    }

    // دیالوگ تأیید حذف
    warningPendingDelete?.let { item ->
        DeleteConfirmationDialog(
            itemType = stringResource(R.string.label_warnings),
            itemName = item.warningTypeName,
            onConfirm = {
                organizationDetailViewModel.deleteWarning(
                    item.warning.warningId
                )
                warningPendingDelete = null
            },
            onDismiss = {
                warningPendingDelete = null
            }
        )
    }

    if (showStatusDialog) {
        ChangeOrganizationStatusDialog(
            initialStatus = currentOrganizationStatus,
            initialReason = currentInactiveReason,
            onDismiss = { showStatusDialog = false },
            onConfirm = { newStatus, reason ->
                currentOrganizationStatus = newStatus
                currentInactiveReason = reason
                showStatusDialog = false

                // در صورت نیاز به فراخوانی اکشن اختصاصی غیرفعال‌سازی
                if (newStatus == OrganizationStatus.INACTIVE) {
                    onDisableClick()
                }
                // viewModel.updateOrganizationStatus(organizationId, newStatus.id, reason)
            }
        )
    }


    if (showAddPersonSheet) {
        AddPersonBottomSheet(
            personState = personState,
            onDismiss = {
                showAddPersonSheet = false
            },
            onSavePerson = {
                if (personState.name.isNotBlank() && personState.phone1.isNotBlank()) {
                    /* formState.organizationPersons.add(
                         PersonOrganization(
                             name = personState.name.trim(),
                             mobile = personState.mobile.trim(),
                             phone = personState.phone.trim(),
                             status = personState.status,
                             gender = personState.gender,
                             description = personState.description.trim()
                         )
                     )*/

                    formState.resetPersonFields(personState)
                    showAddPersonSheet = false
                }
            }
        )
    }
    visitorToDelete?.let { visitor ->
        DeleteConfirmationDialog(
            itemType = stringResource(R.string.label_visitor),
            itemName = visitor.name!!,
            onConfirm = {
                // حذف از لیست محلی یا فراخوانی ViewModel
                //   visitors = visitors.filter { it.id != visitor.id }
                // viewModel.deleteNotice(notice.id)
                visitorToDelete = null
            },
            onDismiss = {
                visitorToDelete = null
            }
        )
    }

    if (showInactiveReasonDialog) {
        InactivationReasonDialog(
            initialReason = "",
            onDismiss = { showInactiveReasonDialog = false },
            onConfirm = { reason ->
                showInactiveReasonDialog = false
                currentOrganizationStatus = OrganizationStatus.INACTIVE
                // TODO: ارسال وضعیت غیرفعال و علت (reason) به ViewModel / API
                onDisableClick()
            }
        )
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
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            appColors.cardBackground
        },
        animationSpec = tween(250),
        label = "tabBackground"
    )

    val contentColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            appColors.textPrimary
        },
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
            .height(42.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        shadowElevation = elevation,
        border = if (!selected) {
            BorderStroke(
                width = 1.dp,
                color = appColors.border
            )
        } else {
            null
        }
    ) {
        Box(
            modifier = Modifier.padding(
                horizontal = 16.dp
            ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(tab.titleRes),
                color = contentColor,
                style = if (selected) {
                    typography.titleLarge
                } else {
                    typography.labelLarge
                },
                maxLines = 1
            )
        }
    }
}

@Composable
fun ChangeOrganizationStatusDialog(
    initialStatus: OrganizationStatus,
    initialReason: String = "",
    onDismiss: () -> Unit,
    onConfirm: (status: OrganizationStatus, reason: String) -> Unit
) {
    var selectedStatus by rememberSaveable { mutableStateOf(initialStatus) }
    var inactiveReason by rememberSaveable { mutableStateOf(initialReason) }
    var isError by remember { mutableStateOf(false) }

    // دریافت لیست Enum
    val statusEntries = remember { OrganizationStatus.entries }
    val statusTitles = statusEntries.map { stringResource(it.titleRes) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
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
                    text = stringResource(R.string.label_determination_organization_status),
                    style = typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                // انتخاب وضعیت
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
                            val newStatus = statusEntries[index]
                            selectedStatus = newStatus

                            if (newStatus != OrganizationStatus.INACTIVE) {
                                inactiveReason = ""
                                isError = false
                            }
                        }
                    }
                )


                // فیلد علت فقط در صورت انتخاب غیرفعال نمایش داده می‌شود
                AnimatedVisibility(
                    visible = selectedStatus == OrganizationStatus.INACTIVE,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        CustomDescriptionField(
                            value = inactiveReason,
                            onValueChange = {
                                inactiveReason = it
                                if (it.trim().isNotBlank()) {
                                    isError = false
                                }
                            },
                            label = stringResource(R.string.label_inactivation_reason_field),
                            placeholder = stringResource(R.string.hint_enter_inactivation_reason)
                        )

                        if (isError) {
                            Text(
                                text = stringResource(R.string.error_inactivation_reason_required),
                                color = MaterialTheme.colorScheme.error,
                                style = typography.labelSmall,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }

                // دکمه‌های تایید و انصراف
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CustomButton(
                        text = stringResource(R.string.label_registration),
                        onClick = {
                            if (selectedStatus == OrganizationStatus.INACTIVE && inactiveReason.trim()
                                    .isBlank()
                            ) {
                                isError = true
                            } else {
                                val finalReason =
                                    if (selectedStatus == OrganizationStatus.INACTIVE) inactiveReason.trim() else ""
                                onConfirm(selectedStatus, finalReason)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )

                    CustomOutlinedButton(
                        text = stringResource(R.string.label_cancel),
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}


@Composable
private fun ActionButtons(
    onEdit: () -> Unit,
    onDetermination: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        CustomButton(
            text = stringResource(R.string.label_edit_organization),
            onClick = onEdit,
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

        CustomButton(
            text = stringResource(R.string.label_determination_organization_status),
            onClick = onDetermination,
            fillMaxWidth = false,
            modifier = Modifier.weight(1f),
            height = 42.dp,
            cornerRadius = 12.dp,
            textStyle = typography.titleLarge,
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LocalPartnerManagementColors.current.error,
                contentColor = LocalPartnerManagementColors.current.errorContainer
            )
        )
    }
}
