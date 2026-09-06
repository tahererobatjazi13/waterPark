package ir.kitgroup.partnerManagement.feature.organization.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.feature.organization.model.PersonOrganization
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import ir.kitgroup.partnerManagement.core.ui.SessionViewModel
import ir.kitgroup.partnerManagement.core.ui.components.ActionIconButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomDescriptionField
import ir.kitgroup.partnerManagement.core.ui.components.CustomOutlinedButton
import ir.kitgroup.partnerManagement.core.ui.components.DeleteConfirmationDialog
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField
import ir.kitgroup.partnerManagement.core.ui.components.GradeSelector
import ir.kitgroup.partnerManagement.core.ui.theme.PartnerManagementTheme
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationDetailTab
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationStatus
import ir.kitgroup.partnerManagement.core.ui.util.Status
import ir.kitgroup.partnerManagement.core.ui.util.UserRole
import ir.kitgroup.partnerManagement.feature.advertising_stand.model.AdvertisingStandAssignment
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand_assignment_organization.DetailItem
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand_assignment_visitor.allocationItemIcon
import ir.kitgroup.partnerManagement.feature.contract.model.ContractUi
import ir.kitgroup.partnerManagement.feature.organization.model.OrganizationNotice
import ir.kitgroup.partnerManagement.feature.organization.model.VisitorOrganization
import ir.kitgroup.partnerManagement.feature.report.ui.visitor.VerticalDivider
import ir.kitgroup.partnerManagement.feature.contract.ui.ContractListItem
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.FilledTonalButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand_assignment_organization.InfoItem

@Composable
fun OrganizationDetailScreen(
    organizationId: Int,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onDisableClick: () -> Unit,
    onAssignVisitorClick: (Int) -> Unit,
    onEditVisitorClick: (Int) -> Unit,
    onDeleteVisitorClick: (Int) -> Unit,
    onAssignStandsClick: (Int) -> Unit,
    onAssignContractClick: (Int) -> Unit,
    onViewItemDetailsClick: (AdvertisingStandAssignment) -> Unit,
    viewModel: SessionViewModel = hiltViewModel()
) {
    val appColors = LocalPartnerManagementColors.current
    val role by viewModel.userRole.collectAsState()
    val isSupervisor = role == UserRole.SUPERVISOR.name

    var selectedTab by rememberSaveable { mutableStateOf(OrganizationDetailTab.INFO) }
    val visibleTabs = remember(isSupervisor) {
        OrganizationDetailTab.entries.filter { tab ->
            tab != OrganizationDetailTab.VISITOR || isSupervisor
        }
    }
    var showNoticeDialog by rememberSaveable { mutableStateOf(false) }

    // متغیرهای وضعیت برای کنترل دیالوگ‌ها
    var showStatusDialog by rememberSaveable { mutableStateOf(false) }
    val organizationStatusList = remember {
        listOf("ثبت اولیه", "فعال", "نارنجی", "غیرفعال", "معلق", "بسته شده")
    }
    var showInactiveReasonDialog by rememberSaveable { mutableStateOf(false) }
    var currentOrganizationStatus by rememberSaveable { mutableStateOf(OrganizationStatus.ACTIVE) }
    var currentInactiveReason by rememberSaveable { mutableStateOf("") }

    var noticePendingDelete by remember { mutableStateOf<OrganizationNotice?>(null) }
    var visitorToDelete by remember { mutableStateOf<VisitorOrganization?>(null) }

    val persons = listOf(
        PersonOrganization(
            name = "علی حسینی",
            mobile = "09121234567",
            phone = "02112345678",
            gender = "آقا",
            status = "فعال",
            description = "مدیر داخلی هتل"
        ),
        PersonOrganization(
            name = "مریم احمدی",
            mobile = "09351234567",
            phone = "",
            gender = "خانم",
            status = "فعال",
            description = "مسئول پذیرش"
        )
    )

    val visitors = listOf(
        VisitorOrganization(
            id = 1,
            name = "امیر حسین رضایی",
            startDate = "1405/02/05",
            endDate = "1405/02/15",
            status = Status.ACTIVE,

            ),
        VisitorOrganization(
            id = 2,
            name = "سارا محمدی",
            startDate = "1405/02/05",
            endDate = "1405/02/15", status = Status.ACTIVE,

            ),
        VisitorOrganization(
            id = 3,
            name = "علی جعفری",
            startDate = "1405/02/05",
            endDate = "1405/02/15", status = Status.INACTIVE,

            ),
        VisitorOrganization(
            id = 4,
            name = "مهین محمدی",
            startDate = "1405/02/05",
            endDate = "1405/02/15", status = Status.ACTIVE,

            ),
        VisitorOrganization(
            id = 5,
            name = "جعفر امری",
            startDate = "1405/02/05",
            endDate = "1405/02/15", status = Status.INACTIVE,

            )
    )

    val stands = listOf(
        AdvertisingStandAssignment(
            "1",
            "محمد احمدی",
            "هتل پارسیان آزادی",
            "تخصیص به بازاریاب",
            "stand",
            5,
            "۱۴۰۳/۰۳/۲۲", "امانی",
            Status.ACTIVE
        ),
        AdvertisingStandAssignment(
            "2",
            "سارا مرادی",
            "هتل اسپیناس پالاس",
            "تخصیص به بازاریاب",
            "wall",
            3,
            "۱۴۰۳/۰۳/۲۴", "تبلیغاتی",
            Status.DRAFT
        ),
        AdvertisingStandAssignment(
            "3",
            "علی رضایی",
            "هتل هما",
            "تخصیص به بازاریاب",
            "kiosk",
            2,
            "۱۴۰۳/۰۳/۲۰", "اجاره ای",
            Status.CANCELLED
        ),
        AdvertisingStandAssignment(
            "4",
            "نازنین کریمی",
            "هتل بزرگ تهران",
            "تخصیص به بازاریاب",
            "stand",
            4,
            "۱۴۰۳/۰۳/۲۵", "اجاره ای",
            Status.RETURNED
        ),
        AdvertisingStandAssignment(
            "5",
            "علی رضایی",
            "هتل هما",
            "جمع آوری",
            "kiosk",
            2,
            "۱۴۰۳/۰۳/۲۰", "تبلیغاتی",
            Status.ACTIVE
        ),
        AdvertisingStandAssignment(
            "6",
            "نازنین کریمی",
            "هتل بزرگ تهران",
            "عودت",
            "stand",
            4,
            "۱۴۰۳/۰۳/۲۵", "امانی",
            Status.DRAFT
        )
    )

    val contracts = listOf(
        ContractUi(
            id = "1",
            organizationName = "هتل اسپیناس پالاس",
            contractTitle = "قرارداد همکاری سال ۱۴۰۵",
            cooperationModel = "پورسانتی",
            settlementPeriodType = "ماهیانه",
            startDate = "1405/01/01",
            endDate = "1405/12/29",
            status = Status.ACTIVE,
            defaultDiscountPercent = 10.0,
            defaultCommissionPercent = 5.0,
            description = "توافق بر اساس نرخ‌نامه رسمی هتل با ۱۰٪ تخفیف برای مشتریان سازمانی."
        ),
        ContractUi(
            id = "2",
            organizationName = "هتل پارسیان آزادی",
            contractTitle = "قرارداد همکاری فصلی",
            cooperationModel = "بلیط تخفیف دار",
            settlementPeriodType = "روزانه",
            startDate = "1405/03/01",
            endDate = "1405/05/31",
            status = Status.ACTIVE,
            defaultDiscountPercent = 15.0,
            defaultCommissionPercent = 7.5,
            description = "پورسانت بر اساس درصد فروش هر فصل محاسبه و تسویه می‌شود."
        ),
        ContractUi(
            id = "3",
            organizationName = "هتل هما شیراز",
            contractTitle = "قرارداد همکاری شش‌ماهه",
            cooperationModel = "پورسانت و تخفیف",
            settlementPeriodType = "روزانه",
            startDate = "1404/10/01",
            endDate = "1405/03/31",
            status = Status.ACTIVE,
            defaultDiscountPercent = 12.0,
            defaultCommissionPercent = 6.0,
            description = "تمدید خودکار قرارداد در صورت تحقق سقف فروش تعیین‌شده."
        ),
        ContractUi(
            id = "4",
            organizationName = "هتل بزرگ تهران",
            contractTitle = "قرارداد همکاری سالانه",
            cooperationModel = "بلیط تخفیف دار",
            settlementPeriodType = "ماهانه",
            startDate = "1404/06/01",
            endDate = "1405/05/31",
            status = Status.INACTIVE,
            defaultDiscountPercent = 8.0,
            defaultCommissionPercent = 4.0,
            description = "قرارداد به‌دلیل عدم تمدید در پایان دوره، غیرفعال شده است."
        ),
        ContractUi(
            id = "5",
            organizationName = "هتل پردیس کیش",
            contractTitle = "قرارداد همکاری نوروز ۱۴۰۵",
            cooperationModel = "پورسانت و تخفیف",
            settlementPeriodType = "ماهانه",
            startDate = "1404/12/15",
            endDate = "1405/02/15",
            status = Status.INACTIVE,
            defaultDiscountPercent = 20.0,
            defaultCommissionPercent = 10.0,
            description = "پروژه ویژه نوروز با تخفیف پلکانی برای رزروهای گروهی."
        ),
        ContractUi(
            id = "6",
            organizationName = "هتل آسمان اصفهان",
            contractTitle = "قرارداد همکاری تابستانه",
            cooperationModel = "بلیط تخفیف دار",
            settlementPeriodType = "هفتگی",
            startDate = "1405/04/01",
            endDate = "1405/06/31",
            status = Status.ACTIVE,
            defaultDiscountPercent = 10.0,
            defaultCommissionPercent = 5.0,
            description = "تخفیف ۱۰٪ برای رزروهای بالای ۵ شب."
        )
    )

    var notices = listOf(
        OrganizationNotice(
            id = 1,
            type = "تذکر شفاهی",
            score = 2,
            description = "تاخیر در پاسخ‌گویی به پیگیری‌های واحد بازاریابی",
            createdAt = "1405/05/02"
        ),
        OrganizationNotice(
            id = 2,
            type = "تذکر کتبی",
            score = 4,
            description = "عدم رعایت استانداردهای قرارداد همکاری در ارائه استند تبلیغاتی",
            createdAt = "1405/05/02"
        ),
        OrganizationNotice(
            id = 3,
            type = "تذکر شفاهی",
            score = -3,
            description = "مغایرت جزئی در اطلاعات ثبت‌شده حساب کاربری", createdAt = "1405/05/02"
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomHeader(
                title = R.string.label_details,
                showBackButton = true,
                onBackClick = onBackClick
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
                        selectedTab = OrganizationDetailTab.INFO
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
                    OrganizationDetailTab.INFO -> {
                        OrganizationBasicInfoTabContent()
                    }

                    OrganizationDetailTab.PERSONS -> {
                        OrganizationPersonsTabContent(persons = persons)
                    }

                    OrganizationDetailTab.VISITOR -> {
                        if (isSupervisor) {

                            OrganizationVisitorTabContent(
                                visitors = visitors,
                                onAssignVisitorClick = {
                                    onAssignVisitorClick(organizationId)
                                },
                                onEditVisitorClick = { id -> onEditVisitorClick(id) },
                                onDeleteVisitorClick = { id -> onDeleteVisitorClick(id) }
                            )
                        }
                    }

                    OrganizationDetailTab.STANDS -> {
                        OrganizationStandsTabContent(assignments = stands,
                            onAssignStandsClick = { onAssignStandsClick(organizationId) },
                            onViewItemDetailsClick = { item ->
                                onViewItemDetailsClick(item)
                            })
                    }

                    OrganizationDetailTab.CONTRACTS -> {
                        OrganizationContractsTabContent(contracts = contracts,
                            onAssignContractClick = { onAssignContractClick(organizationId) })
                    }

                    OrganizationDetailTab.NOTICES -> {
                        OrganizationNoticesTabContent(
                            notices = notices,
                            isSupervisor = isSupervisor,
                            onAddNoticeClick = { showNoticeDialog = true },
                            onDeleteNoticeClick = { notice ->
                                noticePendingDelete = notice
                            }
                        )
                    }
                }
            }
        }
    }

    if (showNoticeDialog) {
        AddOrganizationNoticeDialog(
            onDismiss = { showNoticeDialog = false },
            onConfirm = { noticeType, description ->
                showNoticeDialog = false
            }
        )
    }

    // دیالوگ تأیید حذف در انتهای اسکرین
    noticePendingDelete?.let { notice ->
        DeleteConfirmationDialog(
            itemType = stringResource(R.string.label_warnings),
            itemName = notice.type,
            onConfirm = {
                // حذف از لیست محلی یا فراخوانی ViewModel
                notices = notices.filter { it.id != notice.id }
                // viewModel.deleteNotice(notice.id)
                noticePendingDelete = null
            },
            onDismiss = {
                noticePendingDelete = null
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


    visitorToDelete?.let { visitor ->
        DeleteConfirmationDialog(
            itemType = stringResource(R.string.label_visitor),
            itemName = visitor.name,
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
                    typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
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

    // دریافت لیست Enum و متن‌های ترجمه‌شده در اسکوپ Composable
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

                // دراپ‌داون انتخاب وضعیت
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
private fun OrganizationBasicInfoTabContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionTitle(stringResource(R.string.label_organization_statistical_information))
        BasicInfoSection()

        SectionTitle(stringResource(R.string.label_contact_information))
        ContactInfoSection()

        SectionTitle(stringResource(R.string.label_location))
        AddressMapCard(address = "تهران، بلوار آبشار، پارک آبی")

        SectionTitle(stringResource(R.string.label_analytical_information))
        AnalysisInfoSection()

        SectionTitle(stringResource(R.string.label_statuses))
        StatusInfoSection()

        SectionTitle(stringResource(R.string.label_description))
        DescriptionCard(description = "این سازمان از مشتریان فعال است...")

        SectionTitle(stringResource(R.string.label_images))
        ImagesSection(images = listOf(R.drawable.ic_logo))

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
private fun OrganizationPersonsTabContent(
    persons: List<PersonOrganization> = emptyList()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionTitle(stringResource(R.string.label_related_persons_list))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            itemsIndexed(persons) { index, person ->
                RelatedPersonDetailCard(index = index, person = person)
            }
        }
    }
}

@Composable
private fun OrganizationVisitorTabContent(
    visitors: List<VisitorOrganization> = emptyList(),
    onAssignVisitorClick: () -> Unit = {},
    onEditVisitorClick: (Int) -> Unit = {},
    onDeleteVisitorClick: (Int) -> Unit = {},
) {
    val appColors = LocalPartnerManagementColors.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                SectionTitle(title = stringResource(R.string.label_assignment_visitor_to_organization_list))
            }
            FilledTonalButton(
                onClick = onAssignVisitorClick,
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = appColors.success,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.label_new_assigning),
                    style = typography.titleLarge
                )
            }
        }
        if (visitors.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.WorkspacePremium,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.msg_no_visitor_found),
                        style = typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(
                    items = visitors,
                    key = { _, item -> item.id }
                ) { index, visitor ->
                    RelatedVisitorCard(
                        index = index,
                        visitor = visitor,
                        onEditClick = {
                            onEditVisitorClick(visitor.id)
                        },
                        onDeleteClick = {
                            onDeleteVisitorClick(visitor.id)
                        }
                    )
                }
            }
        }
//        AssignVisitor(
//            onAssignVisitorClick = onAssignVisitorClick
//        )
    }
}

@Composable
private fun AssignVisitor(
    onAssignVisitorClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    CustomButton(
        text = stringResource(R.string.label_assigning_visitor_organization),
        onClick = onAssignVisitorClick,
        fillMaxWidth = true,
        height = 46.dp,
        cornerRadius = 12.dp,
        textStyle = typography.titleLarge,
        icon = Icons.Default.Add,
        colors = ButtonDefaults.buttonColors(
            containerColor = appColors.success,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier
    )
}

@Composable
private fun OrganizationNoticesTabContent(
    notices: List<OrganizationNotice>,
    isSupervisor: Boolean,
    onAddNoticeClick: () -> Unit,
    onDeleteNoticeClick: (OrganizationNotice) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionTitle(title = stringResource(R.string.label_warnings_organization))

        CustomButton(
            text = stringResource(R.string.label_register_organization_notice),
            onClick = onAddNoticeClick,
            fillMaxWidth = true,
            height = 46.dp,
            cornerRadius = 12.dp,
            textStyle = typography.titleLarge,
            icon = Icons.Filled.RateReview,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        )

        if (notices.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.RateReview,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(40.dp)
                    )

                    Text(
                        text = stringResource(R.string.label_no_notice_added),
                        style = typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(
                    items = notices,
                    key = { _, item -> item.id }
                ) { index, notice ->
                    OrganizationNoticeCard(
                        index = index,
                        notice = notice,
                        isSupervisor = isSupervisor,
                        onDeleteClick = { onDeleteNoticeClick(notice) }
                    )
                }
            }
        }
    }
}


@Composable
private fun OrganizationNoticeCard(
    index: Int,
    notice: OrganizationNotice,
    isSupervisor: Boolean,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // هدر کارت شامل شماره، نوع تذکر، چیپ امتیاز و دکمه حذف
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(
                                color = MaterialTheme.colorScheme.tertiaryContainer,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.RateReview,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Text(
                        text = "تذکر ${index + 1}: ${notice.type}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    NoticeScoreChip(score = notice.score)

                    Spacer(modifier = Modifier.width(4.dp))

                    // دکمه حذف تذکر فقط برای سرپرست
                    if (isSupervisor) {
                        ActionIconButton(
                            icon = Icons.Default.DeleteOutline,
                            contentDescription = stringResource(R.string.label_delete),
                            onClick = onDeleteClick,
                            tint = MaterialTheme.colorScheme.error,
                            backgroundColor = MaterialTheme.colorScheme.errorContainer
                        )

                    }
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )

            // توضیحات تذکر
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "توضیحات:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = notice.description.ifBlank { "-" },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}


@Composable
private fun NoticeScoreChip(
    score: Int
) {
    val isPositive = score > 0

    // رنگ پس‌زمینه چیپ
    val chipColor = if (isPositive) {
        PartnerManagementTheme.colors.successContainer
    } else {
        PartnerManagementTheme.colors.errorContainer
    }

    // رنگ متن
    val contentColor = if (isPositive) {
        PartnerManagementTheme.colors.success
    } else {
        PartnerManagementTheme.colors.error
    }

    val scoreText = when {
        score > 0 -> "+$score امتیاز"
        score < 0 -> "$score امتیاز"
        else -> "0 امتیاز"
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = chipColor
    ) {
        Box(
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 4.dp
            ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = scoreText,
                style = typography.labelMedium,
                color = contentColor
            )
        }
    }
}

@Composable
private fun OrganizationStandsTabContent(
    assignments: List<AdvertisingStandAssignment>,
    onAssignStandsClick: () -> Unit = {},
    onViewItemDetailsClick: (AdvertisingStandAssignment) -> Unit,
) {
    val appColors = LocalPartnerManagementColors.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                SectionTitle(title = stringResource(R.string.label_assignment_stands_to_organization_list))
            }

            Spacer(modifier = Modifier.width(8.dp))
            FilledTonalButton(
                onClick = onAssignStandsClick,
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = appColors.success,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.label_new_allocation),
                    style = typography.titleLarge
                )
            }
        }
        if (assignments.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.WorkspacePremium,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.msg_no_stand_found),
                        style = typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(
                    items = assignments,
                    key = { _, item -> item.id }
                ) { index, item ->
                    AdvertisingStandAssignmentCard(
                        index = index,
                        advertisingStandAssignment = item,
                        onViewDetailsClick = { onViewItemDetailsClick(item) }
                    )
                }
            }
        }

        // AssignStands(onAssignStandsClick = onAssignStandsClick)
    }
}

/*
@Composable
private fun AssignStands(
    onAssignStandsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    CustomButton(
        text = stringResource(R.string.label_assignment_stands_to_organization),
        onClick = onAssignStandsClick,
        fillMaxWidth = true,
        height = 46.dp,
        cornerRadius = 12.dp,
        textStyle = typography.titleLarge,
        icon = Icons.Default.Add,
        colors = ButtonDefaults.buttonColors(
            containerColor = appColors.success,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier
    )
}*/

@Composable
private fun AdvertisingStandAssignmentCard(
    index: Int,
    advertisingStandAssignment: AdvertisingStandAssignment,
    onViewDetailsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    val itemIcon = allocationItemIcon(
        advertisingStandAssignment.itemIconName
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onViewDetailsClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(0.5.dp, appColors.border)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                InfoItem(
                    label = stringResource(R.string.label_organization_receiving_name),
                    value = advertisingStandAssignment.organizationName,
                    icon = Icons.Default.Business
                )
                StatusBadge(advertisingStandAssignment.status)

            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = 0.5.dp,
                color = appColors.border
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DetailItem(
                        label = stringResource(R.string.label_assignment_type),
                        value = advertisingStandAssignment.assignmentType,
                        icon = Icons.Default.Category
                    )
                    DetailItem(
                        stringResource(R.string.label_delivery_organization_date),
                        advertisingStandAssignment.allocatedDate,
                        Icons.Default.CalendarMonth
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DetailItem(
                        label = stringResource(R.string.label_assignment_mode),
                        value = advertisingStandAssignment.assignmentMode,
                        icon = Icons.Default.AssignmentInd
                    )
                    DetailItem(
                        stringResource(R.string.label_allocated_count),
                        "${advertisingStandAssignment.count} ${stringResource(R.string.label_unit_count)}",
                        Icons.Default.Inventory2
                    )
                }
            }
        }
    }
}

@Composable
private fun OrganizationContractsTabContent(
    contracts: List<ContractUi>,
    onAssignContractClick: () -> Unit = {}
) {
    val appColors = LocalPartnerManagementColors.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                SectionTitle(title = stringResource(R.string.label_contract_organization_list))
            }

            Spacer(modifier = Modifier.width(8.dp))

            FilledTonalButton(
                onClick = onAssignContractClick,
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = appColors.success,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.label_new_contract),
                    style = typography.titleLarge
                )
            }
        }

        if (contracts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.WorkspacePremium,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.msg_no_contract_found),
                        style = typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(
                    items = contracts,
                    key = { _, item -> item.id }
                ) { _, contract ->
                    ContractListItem(
                        contract = contract,
                        onClick = { /* onContractClick(contract) */ }
                    )
                }
            }
        }
    }
}


@Composable
private fun BasicInfoSection(
    totalNoticesCount: Int = 3,
    assignedStandsCount: Int = 5
) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        // ۱. کارت‌های متریک آماری بالا (بدون تغییر)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatMetricCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_total_warnings),
                count = totalNoticesCount,
                unit = stringResource(R.string.label_warnings),
                icon = Icons.Outlined.WarningAmber,
                accentColor = if (totalNoticesCount > 0) appColors.warning else appColors.success,
                containerColor = if (totalNoticesCount > 0) appColors.warning.copy(alpha = 0.06f) else colors.surface
            )

            StatMetricCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_assigned_stands),
                count = assignedStandsCount,
                unit = stringResource(R.string.label_unit_count),
                icon = Icons.Filled.Storefront,
                accentColor = colors.primary,
                containerColor = colors.primary.copy(alpha = 0.06f)
            )
        }

        SectionTitle(stringResource(R.string.label_organization_basic_information))

        // ۲. کادر یکپارچه اطلاعات پایه سازمان
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = colors.surface),
            border = BorderStroke(1.dp, colors.outlineVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                InfoRowItem(
                    title = stringResource(R.string.label_organization_name),
                    value = "هتل آبی پارسیان",
                    icon = Icons.Filled.Business,
                    isHighlighted = true
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.4f)
                )

                InfoRowItem(
                    title = stringResource(R.string.label_organization_type),
                    value = "هتل",
                    icon = Icons.Filled.Category
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.4f)
                )

                InfoRowItem(
                    title = stringResource(R.string.label_organization_level),
                    value = "معمولی",
                    icon = Icons.Filled.WorkspacePremium
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.4f)
                )

                InfoRowItem(
                    title = stringResource(R.string.label_owner_name),
                    value = "حسینی",
                    icon = Icons.Filled.ManageAccounts
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.4f)
                )

                InfoRowItem(
                    title = stringResource(R.string.label_english_name),
                    value = "parsian",
                    icon = Icons.Filled.Language
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.4f)
                )

                InfoRowItem(
                    title = stringResource(R.string.label_national_id),
                    value = "125",
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

                    StatusBadge(status = Status.ACTIVE)
                }

            }
        }
    }
}


@Composable
private fun ContactInfoSection() {
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
                    value = "025685",
                    icon = Icons.Filled.Phone
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.4f)
                )

                InfoRowItem(
                    title = stringResource(R.string.label_phone),
                    value = "0512336888",
                    icon = Icons.Filled.Phone
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.4f)
                )

                InfoRowItem(
                    title = stringResource(R.string.label_mobile),
                    value = "09129876543",
                    icon = Icons.Filled.Smartphone
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    thickness = 0.8.dp,
                    color = colors.outlineVariant.copy(alpha = 0.4f)
                )

                InfoRowItem(
                    title = stringResource(R.string.label_email),
                    value = "info@parsian.com",
                    icon = Icons.Filled.Email
                )
            }
        }
    }
}

@Composable
private fun AnalysisInfoSection() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        GradeSelector(
            grade = 4
        )

        TwoColumnInfoRow(
            start = {
                InfoCard(
                    title = stringResource(R.string.label_ticket_sale_count_history),
                    value = "1250",
                    icon = Icons.Filled.Star
                )
            },
            end = {
                InfoCard(
                    title = stringResource(R.string.label_customer_capacity),
                    value = "350 نفر",
                    icon = Icons.Filled.Groups
                )
            }
        )
    }
}

@Composable
private fun StatusInfoSection() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        TwoColumnInfoRow(
            start = {
                BooleanStatusCard(
                    title = stringResource(R.string.label_foreign_guest_reception),
                    enabled = true
                )
            },
            end = {
                BooleanStatusCard(
                    title = stringResource(R.string.label_desire_receive_advertising_stands),
                    enabled = false
                )
            }
        )
    }
}


@Composable
fun StatMetricCard(
    title: String,
    count: Int,
    unit: String,
    icon: ImageVector,
    accentColor: Color,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = containerColor,
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.25f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // آیکون در کانتینر مربعی با گوشه‌های گرد و پس‌زمینه محو رنگ اصلی
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(Modifier.width(10.dp))

            // بخش متنی و عدد برجسته
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = title,
                    style = typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )

                Spacer(Modifier.height(2.dp))

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "$count",
                        style = typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = accentColor
                    )
                    Text(
                        text = unit,
                        style = typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 3.dp)
                    )
                }
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
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant
            )
        }

        Text(
            text = value.ifBlank { "—" },
            style = if (isHighlighted) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isHighlighted) FontWeight.ExtraBold else FontWeight.Medium,
            color = if (isHighlighted) colors.primary else colors.onSurface
        )
    }
}

@Composable
private fun InfoCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    isMainTitle: Boolean = false
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                    vertical = if (isMainTitle) 14.dp else 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(if (isMainTitle) 38.dp else 30.dp)
                    .background(colors.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(if (isMainTitle) 20.dp else 15.dp)
                )
            }

            Spacer(Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = typography.labelSmall,
                    color = colors.onSurfaceVariant,
                    maxLines = 1
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = value.ifBlank { "-" },
                    style = if (isMainTitle) typography.titleMedium else typography.titleSmall,
                    color = colors.onSurface,
                    maxLines = if (isMainTitle) 2 else 1
                )
            }
        }
    }
}

@Composable
private fun TwoColumnInfoRow(
    start: @Composable () -> Unit,
    end: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            start()
        }

        Box(modifier = Modifier.weight(1f)) {
            end()
        }
    }
}

@Composable
private fun AddressMapCard(
    address: String
) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        ),
        border = BorderStroke(1.dp, colors.outlineVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 120.dp, height = 92.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.LocationOn,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(36.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.label_address),
                    style = typography.labelMedium,
                    color = colors.onSurfaceVariant
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = address.ifBlank { "-" },
                    style = typography.bodyMedium,
                    color = colors.onSurface,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
private fun BooleanStatusCard(
    title: String,
    enabled: Boolean
) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        ),
        border = BorderStroke(1.dp, colors.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = title,
                style = typography.labelSmall,
                color = colors.onSurfaceVariant
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = if (enabled) stringResource(R.string.label_has) else stringResource(R.string.label_has_not),
                style = typography.titleSmall,
                color = colors.onSurface
            )
        }
    }
}


@Composable
private fun RelatedPersonDetailCard(
    index: Int,
    person: PersonOrganization
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(0.5.dp, appColors.border)
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "${index + 1}. ${person.name}".trim(),
                        style = typography.titleMedium,
                        color = appColors.textPrimary,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusBadge(
                            status = personStatusToStatus(person.status)
                        )

                        if (person.gender.isNotBlank()) {
                            Text(
                                text = person.gender,
                                style = typography.labelSmall,
                                color = appColors.textSecondary
                            )
                        }
                    }
                }
            }

            PersonInfoRow(
                title = stringResource(R.string.label_mobile),
                value = person.mobile
            )

            PersonInfoRow(
                title = stringResource(R.string.label_phone),
                value = person.phone
            )

            if (person.description.isNotBlank()) {
                PersonInfoRow(
                    title = stringResource(R.string.label_description),
                    value = person.description
                )
            }
        }
    }
}

@Composable
private fun RelatedVisitorCard(
    index: Int,
    visitor: VisitorOrganization,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(0.5.dp, appColors.border)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // ── ردیف اول: شماره + نام | StatusBadge | دکمه‌های اکشن ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // شماره و نام به سمت راست (شروع)
                Text(
                    text = "${index + 1}. ${visitor.name}".trim(),
                    style = typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

                // بج وضعیت
                StatusBadge(visitor.status)

            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── تاریخ شروع و پایان ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                VisitorStatItem(
                    date = visitor.startDate,
                    label = stringResource(R.string.label_start_date),
                    icon = Icons.Default.CalendarMonth,
                    modifier = Modifier.weight(1f)
                )

                VerticalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    thickness = 1.dp
                )

                VisitorStatItem(
                    date = visitor.endDate,
                    label = stringResource(R.string.label_end_date),
                    icon = Icons.Default.CalendarMonth,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    space = 8.dp,
                    alignment = Alignment.End
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                ActionIconButton(
                    icon = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.label_edit),
                    onClick = onEditClick,
                    tint = MaterialTheme.colorScheme.primary,
                    backgroundColor = appColors.cardBackgroundAlt
                )

                ActionIconButton(
                    icon = Icons.Default.DeleteOutline,
                    contentDescription = stringResource(R.string.label_delete),
                    onClick = onDeleteClick,
                    tint = MaterialTheme.colorScheme.error,
                    backgroundColor = MaterialTheme.colorScheme.errorContainer
                )

            }
        }
    }
}


@Composable
fun VisitorStatItem(
    date: String,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.height(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = label,
                style = typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = date,
                style = typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun DescriptionCard(
    description: String
) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        ),
        border = BorderStroke(1.dp, colors.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = description.ifBlank { "-" },
                style = typography.bodyMedium,
                color = colors.onSurface,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Start
            )
        }
    }
}

@Composable
private fun ImagesSection(
    images: List<Int>
) {
    if (images.isEmpty()) {
        Text(
            text = "تصویری ثبت نشده است",
            style = typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        return
    }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp)
    ) {
        items(images.size) { index ->
            ImageCard(
                imageRes = images[index]
            )
        }
    }
}

@Composable
private fun ImageCard(
    imageRes: Int
) {
    Image(
        painter = painterResource(imageRes),
        contentDescription = null,
        modifier = Modifier
            .size(width = 110.dp, height = 88.dp)
            .clip(RoundedCornerShape(14.dp)),
        contentScale = ContentScale.Crop
    )
}


@Composable
private fun ActionButtons(
    onEdit: () -> Unit,
    onDetermination: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

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

@Composable
private fun PersonInfoRow(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    if (value.isBlank()) return

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "$title:",
            style = typography.labelMedium,
            color = appColors.textSecondary,
            maxLines = 1
        )

        Text(
            text = value,
            style = typography.labelMedium,
            color = appColors.textPrimary,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )
    }
}
