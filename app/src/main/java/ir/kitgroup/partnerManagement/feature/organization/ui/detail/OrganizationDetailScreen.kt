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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
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
import ir.kitgroup.partnerManagement.core.ui.components.DeleteConfirmationDialog
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
import ir.kitgroup.partnerManagement.core.ui.util.DataState
import ir.kitgroup.partnerManagement.core.ui.util.SecondaryButtonVariant
import ir.kitgroup.partnerManagement.feature.organization.ui.add.InactivationReasonDialog
import ir.kitgroup.partnerManagement.feature.organization.ui.detail.stand.OrganizationStandsTabContent
import ir.kitgroup.partnerManagement.feature.organization.ui.detail.ticket.OrganizationTicketIssueTabContent
import ir.kitgroup.partnerManagement.feature.organization.ui.detail.visitor.OrganizationVisitorTabContent
import ir.kitgroup.partnerManagement.feature.organization.ui.detail.warning.OrganizationWarningTabContent
import ir.kitgroup.partnerManagement.navigation.Screen
import ir.kitgroup.partnerManagement.core.ui.components.DualActionButtonBottomBar
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.convertMillisToIso
import ir.kitgroup.partnerManagement.core.ui.util.convertMillisToJalali
import ir.kitgroup.partnerManagement.core.ui.util.jalaliToMillis
import ir.kitgroup.partnerManagement.feature.organization.ui.detail.person.OrganizationPersonsTabContent
import ir.kitgroup.partnerManagement.feature.organization.ui.detail.serial.OrganizationSerialsTabContent


@Composable
fun OrganizationDetailScreen(
    organizationId: String,
    onEditClick: (organizationId: String) -> Unit,
    onDisableClick: () -> Unit,
    onStatusChangeSuccess: () -> Unit,
    onWarningSuccess: () -> Unit,
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


    val addPersonState by organizationDetailViewModel.addPersonState
        .collectAsStateWithLifecycle()

    var showAddPersonSheet by rememberSaveable { mutableStateOf(false) }

    var warningToEdit by remember { mutableStateOf<WarningWithDetail?>(null) }


    LaunchedEffect(organizationId) {
        organizationDetailViewModel.getOrganizationById(organizationId)
        organizationDetailViewModel.observeOrganizationMeetings(organizationId)
        organizationDetailViewModel.observeOrganizationPersons(organizationId)
        organizationDetailViewModel.observeOrganizationContracts(organizationId)
        // organizationDetailViewModel.observeOrganizationWarnings(organizationId)
        organizationDetailViewModel.observeOrganizationAssignedStands(organizationId)
        organizationDetailViewModel.loadAttachments()
    }

    val currentOrganization = organization ?: return
    val appColors = LocalPartnerManagementColors.current
    val roleCode by sessionViewModel.roleCode.collectAsState()

    val currentRole = remember(roleCode) { UserRole.fromCode(roleCode.toString()) }
    val isSupervisor = currentRole == UserRole.SUPERVISOR

    /*
        val formState = rememberAddOrganizationFormState()
        val personState = rememberAddPersonFormState()
    */

    var selectedTab by rememberSaveable { mutableStateOf(OrganizationDetailTab.BASIC_INFO) }
    val visibleTabs = remember(isSupervisor) {
        OrganizationDetailTab.entries.filter { tab ->
            tab != OrganizationDetailTab.VISITOR || isSupervisor
        }
    }
    var showWarningDialog by rememberSaveable { mutableStateOf(false) }
    var showStatusDialog by rememberSaveable { mutableStateOf(false) }
    var showInactiveReasonDialog by rememberSaveable { mutableStateOf(false) }
    var currentOrganizationStatus by rememberSaveable { mutableStateOf(OrganizationStatus.ACTIVE) }


    val activeVisitorName by organizationDetailViewModel.activeVisitorName.collectAsState()
    val activeContractName by organizationDetailViewModel.activeContractName.collectAsState()

    // متغیرهای وضعیت برای کنترل دیالوگ‌ها
    val statusUpdateState by organizationDetailViewModel.statusUpdateState
        .collectAsStateWithLifecycle()

    val visitSubjects by organizationDetailViewModel.visitSubjects
        .collectAsStateWithLifecycle()
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
// بستن دیالوگ و بازگشت پس از موفقیت
// بستن دیالوگ و بازگشت به لیست سازمان‌ها همراه با فعال‌سازی پرچم سینک
    LaunchedEffect(statusUpdateState) {
        when (val state = statusUpdateState) {

            is DataState.Success -> {
                showStatusDialog = false
                organizationDetailViewModel.resetStatusUpdateState()

                Toast.makeText(
                    context,
                    "وضعیت سازمان با موفقیت ثبت شد",
                    Toast.LENGTH_SHORT
                ).show()

                onStatusChangeSuccess()
            }

            is DataState.Error -> {
                showStatusDialog = false

                snackBarHostState.showSnackbar(
                    state.error.asString(context)
                )

                organizationDetailViewModel.resetStatusUpdateState()
            }

            else -> Unit
        }
    }


    LaunchedEffect(addPersonState) {
        when (val state = addPersonState) {

            is DataState.Success -> {

                organizationDetailViewModel.resetAddPersonState()

                showAddPersonSheet = false

                //  formState.resetPersonFields(personState)

                organizationDetailViewModel
                    .observeOrganizationPersons(organizationId)

                Toast.makeText(
                    context,
                    "شخص با موفقیت ثبت شد",
                    Toast.LENGTH_SHORT
                ).show()
            }

            is DataState.Error -> {

                snackBarHostState.showSnackbar(
                    state.error.asString(context)
                )

                organizationDetailViewModel.resetAddPersonState()
            }

            else -> Unit
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },

        bottomBar = {
            if (selectedTab == OrganizationDetailTab.BASIC_INFO) {

                Surface(
                    color = appColors.screenBackground,
                    tonalElevation = 4.dp,
                    shadowElevation = 8.dp
                ) {
                    DualActionButtonBottomBar(
                        primaryText = stringResource(R.string.label_edit_organization),
                        secondaryText = stringResource(
                            R.string.label_determination_organization_status
                        ),
                        primaryIcon = Icons.Outlined.Edit,
                        secondaryIcon = Icons.Outlined.Cancel,
                        secondaryVariant = SecondaryButtonVariant.FILLED,
                        buttonHeight = 44.dp,
                        onPrimaryClick = {
                            onEditClick(organizationId)
                        },
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
            color = MaterialTheme.colorScheme.background
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
                            organization = currentOrganization,
                            activeVisitorName = activeVisitorName,
                            activeContractName = activeContractName,
                            isImagesLoading = detailUiState.isImagesLoading,
                            attachments = detailUiState.attachments,
                            baseUrl = baseUrl,
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
                            meetings = meetings,
                            onAddMeetingClick = onAddMeetingClick,
                            onMeetingClick = onMeetingClick
                        )
                    }

                    OrganizationDetailTab.PERSONS -> {
                        OrganizationPersonsTabContent(
                            persons = persons,
                            onAddPersonClick = {
                                //     formState.resetPersonFields(personState)
                                showAddPersonSheet = true
                            },
                        )
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

                    OrganizationDetailTab.WARNING -> {

                        // داخل فراخوانی OrganizationWarningTabContent:
                        OrganizationWarningTabContent(
                            organizationId = organizationId,
                            isSupervisor = isSupervisor,
                            onAddWarningClick = {
                                warningToEdit = null
                                showWarningDialog = true
                            },
                            onEditWarningClick = { item ->
                                warningToEdit = item
                                showWarningDialog = true
                            }
                        )
                    }

                    OrganizationDetailTab.SERIALS -> {
                        OrganizationSerialsTabContent(organizationId = organizationId,
                            onAddTicketClick = {
                                val activeContractId = organization?.organization!!.activeContractId
                                val hasActiveContract = !activeContractId.isNullOrBlank() &&
                                        activeContractId != "00000000-0000-0000-0000-000000000000"

                                if (hasActiveContract) {
                                    onAddTicketOfferClick() // فراخوانی کالبک نویگیشن
                                } else {
                                    // نمایش پیام خطا با Toast یا Snackbar
                                    Toast.makeText(
                                        context,
                                        "این سازمان قرارداد همکاری فعالی ندارد. ابتدا قرارداد ثبت کنید.",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    // یا اگر در صفحه اسنک‌بار دارید:
                                    // scope.launch { snackBarHostState.showSnackbar("این سازمان قرارداد همکاری فعالی ندارد.") }
                                }
                            },
                            onTicketClick = { ticket ->
                                navController.navigate(
                                    Screen.AssignedSerialDetail.createRoute(ticket.assignedSerial.serialAssignmentId)
                                )
                            }
                        )
                    }

                    OrganizationDetailTab.TICKET -> {
                        OrganizationTicketIssueTabContent(
                        )
                    }

                    OrganizationDetailTab.VISITOR -> {
                        OrganizationVisitorTabContent(
                            onAssignVisitorClick = {
                                onAssignVisitorClick(organizationId)
                            },
                            onEditVisitorClick = { id -> onEditVisitorClick(id) },
                            onDeleteVisitorClick = { id -> onDeleteVisitorClick(id) }
                        )
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
            isSubmitting = statusUpdateState is DataState.Loading,
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
                    subjectVisitId = subjectVisitId, onSuccessCallback = {                    showStatusDialog = false
                    }
                )
            }
        )
    }


    if (showAddPersonSheet) {
        /* AddPersonBottomSheet(
             personState = personState,

             onDismiss = {
                 showAddPersonSheet = false
                 organizationDetailViewModel.resetAddPersonState()
             },

             onSavePerson = {

                 if (
                     personState.name.isBlank() ||
                     personState.mobile.isBlank() ||
                     personState.role == null
                 ) {
                     return@AddPersonBottomSheet
                 }

                 organizationDetailViewModel.addOrganizationPerson(
                     organizationId = organizationId,
                     fullName = personState.name,
                     role = personState.role!!,
                     mobile = personState.mobile,
                     phone = personState.phone,
                     gender = personState.gender,
                     description = personState.description,
                     isCommissionEligible =
                     personState.isCommissionEligible
                 )
             }
         )*/
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

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPersonBottomSheet(
    personState: AddPersonFormState,
    onDismiss: () -> Unit,
    onSavePerson: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current

    val genderList = remember { PersonGenderType.entries }
    val roleList = remember { PersonOrganizationRole.entries }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = appColors.screenBackground,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f) // تعیین ارتفاع در کانتنت داخلی به جای شیت بیرونی
                .imePadding() // کیبورد مستقیماً کانتنت را به بالا هل می‌دهد
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = stringResource(R.string.label_add_related_person),
                style = typography.titleLarge,
                color = appColors.textPrimary
            )

            // نام و نام خانوادگی
            CustomEditTextField(
                value = personState.name,
                onValueChange = { personState.name = it },
                label = stringResource(R.string.label_person_full_name),
                placeholder = stringResource(R.string.hint_enter_person_full_name),
                isRequired = true,
                leadingIcon = null
            )

            // دو ستون شماره همراه و تلفن
            TwoColumnRow(
                start = {
                    CustomEditTextField(
                        value = personState.mobile,
                        onValueChange = { personState.mobile = it },
                        label = stringResource(R.string.label_mobile),
                        placeholder = stringResource(R.string.hint_enter_mobile),
                        isRequired = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone
                        ),
                        leadingIcon = null
                    )
                },
                end = {
                    CustomEditTextField(
                        value = personState.phone,
                        onValueChange = { personState.phone = it },
                        label = stringResource(R.string.label_phone),
                        placeholder = stringResource(R.string.hint_enter_phone),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone
                        ),
                        isRequired = false,
                        leadingIcon = null
                    )
                }
            )

            // جنسیت
            val genderItems = genderList.map { stringResource(it.titleRes) }
            DropdownSelectorField(
                value = PersonGenderType
                    .fromId(personState.gender)
                    ?.let { stringResource(it.titleRes) }
                    ?: "",
                label = stringResource(R.string.label_gender),
                placeholder = stringResource(R.string.hint_choose_gender),
                items = genderItems,
                isRequired = false,
                onItemSelected = { selectedGender ->
                    val index = genderItems.indexOf(selectedGender)
                    if (index >= 0) {
                        personState.gender = genderList[index].id
                    }
                }
            )

            // نقش شخص در سازمان
            val roleItems = roleList.map { stringResource(it.titleRes) }
            DropdownSelectorField(
                value = PersonOrganizationRole
                    .fromId(personState.role)
                    ?.let { stringResource(it.titleRes) }
                    ?: "",
                label = stringResource(R.string.label_organization_role),
                placeholder = stringResource(R.string.hint_choose_organization_role),
                items = roleItems,
                isRequired = true,
                onItemSelected = { selectedRole ->
                    val selectedIndex = roleItems.indexOf(selectedRole)
                    if (selectedIndex >= 0) {
                        personState.role = roleList[selectedIndex].id
                    }
                }
            )

            // بروزرسانی مانده کیف پول
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.label_update_wallet_balance),
                    style = typography.bodyLarge,
                    color = appColors.textPrimary
                )

                Switch(
                    checked = personState.isCommissionEligible,
                    onCheckedChange = { personState.isCommissionEligible = it }
                )
            }

            // توضیحات
            CustomDescriptionField(
                label = stringResource(R.string.label_description),
                value = personState.description,
                onValueChange = { personState.description = it },
                placeholder = stringResource(R.string.hint_description)
            )

            // دکمه‌ها
            AddPersonBottomSheetActions(
                enabled = personState.name.isNotBlank() &&
                        personState.mobile.isNotBlank() &&
                        personState.role != null,
                onDismiss = onDismiss,
                onSavePerson = onSavePerson
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
*/


@Composable
private fun AddPersonBottomSheetActions(
    enabled: Boolean,
    onDismiss: () -> Unit,
    onSavePerson: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CustomOutlinedButton(
            text = stringResource(R.string.label_cancel),
            onClick = onDismiss,
            modifier = Modifier.weight(1f)
        )

        CustomButton(
            text = stringResource(R.string.label_add_person),
            onClick = onSavePerson,
            modifier = Modifier.weight(1f),
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = appColors.border.copy(alpha = 0.4f),
                disabledContentColor = appColors.textSecondary
            )
        )
    }
}


@Composable
private fun TwoColumnRow(
    start: @Composable () -> Unit,
    end: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) { start() }
        Box(modifier = Modifier.weight(1f)) { end() }
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
                                style = typography.labelSmall
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
                                style = typography.labelSmall
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


// تابع کمکی برای ردیف‌های تاریخ در دیالوگ
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
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, style = typography.bodyMedium)
        }
    }
}

