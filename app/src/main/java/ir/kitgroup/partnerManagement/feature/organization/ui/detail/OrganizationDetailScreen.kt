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
import ir.kitgroup.partnerManagement.core.database.entity.ContractEntity
import ir.kitgroup.partnerManagement.core.ui.util.demoMeetings
import ir.kitgroup.partnerManagement.core.ui.util.demoOrganizations
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationWarningEntity
import ir.kitgroup.partnerManagement.core.database.entity.StandAssignmentEntity
import ir.kitgroup.partnerManagement.core.database.entity.VisitorOrganizationEntity
import ir.kitgroup.partnerManagement.core.ui.util.demoContracts
import ir.kitgroup.partnerManagement.core.ui.util.demoOrganizationPersons
import ir.kitgroup.partnerManagement.core.ui.util.demoOrganizationWarnings
import ir.kitgroup.partnerManagement.core.ui.util.demoStandAssignments
import ir.kitgroup.partnerManagement.core.ui.util.demoVisitorOrganizations
import ir.kitgroup.partnerManagement.feature.organization.model.OrganizationOfferTicketModel
import ir.kitgroup.partnerManagement.feature.organization.model.TicketSerialStatus
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
    onViewItemDetailsClick: (StandAssignmentEntity) -> Unit,
    onContractClick: (ContractEntity) -> Unit,
    onAddTicketOfferClick: () -> Unit,
    viewModel: SessionViewModel = hiltViewModel()
) {
    val organization = remember(organizationId) {
        demoOrganizations.find { it.organizationId == organizationId }
    }

    val appColors = LocalPartnerManagementColors.current
    val role by viewModel.userRole.collectAsState()
    val isSupervisor = role == UserRole.SUPERVISOR.name

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

    var warningPendingDelete by remember { mutableStateOf<OrganizationWarningEntity?>(null) }
    var visitorToDelete by remember { mutableStateOf<VisitorOrganizationEntity?>(null) }


/*
    val stands = listOf(
        AdvertisingStandAssignment(
            "1",
            "محمد احمدی",
            "هتل پارسیان آزادی",
            "تخصیص به بازاریاب",
            "stand",
            5,
            "۱۴۰۳/۰۳/۲۲", "امانی",
            1
        ),
        AdvertisingStandAssignment(
            "2",
            "سارا مرادی",
            "هتل اسپیناس پالاس",
            "تخصیص به بازاریاب",
            "wall",
            3,
            "۱۴۰۳/۰۳/۲۴", "تبلیغاتی",
            1
        ),
        AdvertisingStandAssignment(
            "3",
            "علی رضایی",
            "هتل هما",
            "تخصیص به بازاریاب",
            "kiosk",
            2,
            "۱۴۰۳/۰۳/۲۰", "اجاره ای",
            0
        ),
        AdvertisingStandAssignment(
            "4",
            "نازنین کریمی",
            "هتل بزرگ تهران",
            "تخصیص به بازاریاب",
            "stand",
            4,
            "۱۴۰۳/۰۳/۲۵", "اجاره ای",
            3
        ),
        AdvertisingStandAssignment(
            "5",
            "علی رضایی",
            "هتل هما",
            "جمع آوری",
            "kiosk",
            2,
            "۱۴۰۳/۰۳/۲۰", "تبلیغاتی",
            2
        ),
        AdvertisingStandAssignment(
            "6",
            "نازنین کریمی",
            "هتل بزرگ تهران",
            "عودت",
            "stand",
            4,
            "۱۴۰۳/۰۳/۲۵", "امانی",
            1
        )
    )
*/


    val demoOrganizationOfferTickets = listOf(
        OrganizationOfferTicketModel(
            id = 1,
            offerPlanName = "فروش بلیط تابستانه",
            contractName = "قرارداد فروش مهمانپذیر شبنم",
            organizationName = "مهمانپذیر شبنم",
            cooperationModel = "بلیط تخفیف دار",
            serialPrefix = "DC",
            serialCount = 7,
            startSerial = 100L,
            endSerial = 106L,
            status = TicketSerialStatus.DRAFT,
            lastUsedSerial = null,
            title = "DC - قرارداد فروش مهمانپذیر شبنم - فروش بلیط تابستانه -سریال 100 - 106",
            ownerName = "مدیر سیستم"
        ),
        OrganizationOfferTicketModel(
            id = 2,
            offerPlanName = "طرح تخفیف پاییزه",
            contractName = "قرارداد فروش مهمانپذیر شبنم",
            organizationName = "مهمانپذیر شبنم",
            cooperationModel = "بلیط تخفیف دار",
            serialPrefix = "DC",
            serialCount = 10,
            startSerial = 107L,
            endSerial = 116L,
            status = TicketSerialStatus.ACTIVE,
            lastUsedSerial = 109L,
            title = "DC - قرارداد فروش مهمانپذیر شبنم - طرح تخفیف پاییزه -سریال 107 - 116",
            ownerName = "کاربر بازاریاب"
        )
    )

    val organizationOfferTickets = remember { demoOrganizationOfferTickets }

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
                        OrganizationBasicInfoTabContent(organization = organization!!)
                    }

                    OrganizationDetailTab.GENERAL_INFO -> {
                        OrganizationGeneralInfoTabContent(organization = organization!!)
                    }

                    OrganizationDetailTab.VISITS -> {
                        OrganizationVisitsTabContent(
                            visits = demoMeetings,
                            onAddMeetingClick = onAddMeetingClick,
                            onMeetingClick = onMeetingClick
                        )
                    }

                    OrganizationDetailTab.PERSONS -> {
                        OrganizationPersonsTabContent(
                            persons = demoOrganizationPersons,
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
                        OrganizationStandsTabContent(assignments = demoStandAssignments,
                            onAssignStandsClick = { onAssignStandsClick(organizationId) },
                            onViewItemDetailsClick = { item ->
                                onViewItemDetailsClick(item)
                            })
                    }

                    OrganizationDetailTab.CONTRACTS -> {
                        OrganizationContractsTabContent(contracts = demoContracts(),
                            onAssignContractClick = { onAssignContractClick(organizationId) },
                            onContractClick = { contract ->
                                onContractClick(contract)
                            })
                    }

                    OrganizationDetailTab.TICKETS -> {
                        OrganizationTicketsTabContent(
                            tickets = organizationOfferTickets, // لیست آفرها پاس داده شود
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
                            warnings = demoOrganizationWarnings,
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
            onDismiss = { showWarningDialog = false },
            onConfirm = { warningType, description ->
                showWarningDialog = false
            }
        )
    }

    // دیالوگ تأیید حذف
    warningPendingDelete?.let { warning ->
        DeleteConfirmationDialog(
            itemType = stringResource(R.string.label_warnings),
            itemName = warning.name!!,
            onConfirm = {
                // حذف از لیست محلی یا فراخوانی ViewModel
                //   demoOrganizationWarnings = demoOrganizationWarnings.filter { it.organizationWarningId != warning.organizationWarningId }
                // viewModel.deleteNotice(notice.id)
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
