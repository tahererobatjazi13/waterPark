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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AssignmentInd
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Smartphone
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
import ir.kitgroup.partnerManagement.feature.contract.model.ContractUi
import ir.kitgroup.partnerManagement.feature.organization.model.OrganizationNotice
import ir.kitgroup.partnerManagement.feature.organization.model.VisitorOrganization
import ir.kitgroup.partnerManagement.feature.contract.ui.ContractListItem
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material.icons.outlined.Stars
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand_assignment_organization.InfoItem
import ir.kitgroup.partnerManagement.feature.organization.model.OrganizationModel
import ir.kitgroup.partnerManagement.feature.organization.model.OrganizationOfferTicketModel
import ir.kitgroup.partnerManagement.feature.organization.model.TicketSerialStatus
import ir.kitgroup.partnerManagement.feature.visits.model.VisitModel
import ir.kitgroup.partnerManagement.feature.visits.ui.DetailRow
import ir.kitgroup.partnerManagement.feature.visits.ui.VisitTypeChip


val demoOrganizations = listOf(
    OrganizationModel(
        1,
        "هتل پارسیان آزادی",
        city = "مشهد",
        region = "منطقه 2",
        "یوسفی",
        Status.ACTIVE,
        5, 0,
        36.2845,
        59.5892
    ),

    OrganizationModel(
        2,
        name = "سازمان پالاس",
        city = "مشهد",
        region = "منطقه 2",
        address = "قاسم آباد",
        Status.ACTIVE,
        4, 4,
        36.3562,
        59.5084
    ),

    OrganizationModel(
        3,
        name = "سازمان نوید",
        city = "مشهد",
        region = "منطقه 2",
        address = "پیروزی",
        status = Status.ACTIVE,
        3, 3,
        latitude = 36.3015,
        longitude = 59.5289
    ),

    OrganizationModel(
        4,
        name = "هتل مرکزی",
        city = "مشهد",
        region = "منطقه 2",
        address = "امام رضا",
        status = Status.ACTIVE,
        5, 5,
        latitude = 36.2820,
        longitude = 59.6190
    ),

    OrganizationModel(
        5,
        name = "کیوسک اطلس", city = "مشهد",
        region = "منطقه 2",
        address = "کوهسنگی",
        status = Status.ACTIVE,
        3, 3,
        latitude = 36.2736,
        longitude = 59.5694
    ),

    OrganizationModel(
        6,
        name = "هتل الماس", city = "مشهد",
        region = "منطقه 2",
        address = "پاستور",
        status = Status.ACTIVE,
        2, 2,
        latitude = 36.2994,
        longitude = 59.5772
    ),

    OrganizationModel(
        7,
        name = "هتل وفا", city = "مشهد",
        region = "منطقه 2",
        address = "وکیل آباد",
        status = Status.ACTIVE,
        3, 3,
        latitude = 36.3312,
        longitude = 59.4851
    ),

    OrganizationModel(
        8,
        name = "سازمان مهندسی", city = "مشهد",
        region = "منطقه 2",
        address = "فاطمی",
        status = Status.ACTIVE,
        4, 4,
        latitude = 36.3078,
        longitude = 59.5935
    ),

    OrganizationModel(
        9,
        name = "هتل امیر", city = "مشهد",
        region = "منطقه 2",
        address = " رضاییه",
        status = Status.ACTIVE,
        2, 2,
        latitude = 36.2768,
        longitude = 59.6385
    ),

    OrganizationModel(
        10,
        name = "آپارتمان ملل", city = "مشهد",
        region = "منطقه 2",
        address = " ستاری",
        status = Status.ACTIVE,
        3, 3,
        latitude = 36.3421,
        longitude = 59.5208
    ),

    OrganizationModel(
        11, "مهمانسرا اسپیناس", "مشهد", "منطقه 2", "مرکزی", Status.INACTIVE, 5, 5, 36.3051, 59.6059
    ),

    OrganizationModel(
        12,
        name = "چالیدره", city = "مشهد",
        region = "منطقه 2",
        address = "طرقبه",
        status = Status.INACTIVE,
        2, 2,
        latitude = 36.3198,
        longitude = 59.3482
    )
)


@Composable
fun OrganizationDetailScreen(
    organizationId: Int,
    onBack: () -> Unit,
    onEditClick: () -> Unit,
    onDisableClick: () -> Unit,
    onAddVisitClick: () -> Unit,
    onVisitClick: (Int) -> Unit,
    onAssignVisitorClick: (Int) -> Unit,
    onEditVisitorClick: (Int) -> Unit,
    onDeleteVisitorClick: (Int) -> Unit,
    onAssignStandsClick: (Int) -> Unit,
    onAssignContractClick: (Int) -> Unit,
    onViewItemDetailsClick: (AdvertisingStandAssignment) -> Unit,
    onContractClick: (ContractUi) -> Unit,
    onAddTicketOfferClick: () -> Unit,
    viewModel: SessionViewModel = hiltViewModel()
) {
    val organization = remember(organizationId) {
        demoOrganizations.find { it.receationcenterid == organizationId }
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
    var showNoticeDialog by rememberSaveable { mutableStateOf(false) }

    // متغیرهای وضعیت برای کنترل دیالوگ‌ها
    var showStatusDialog by rememberSaveable { mutableStateOf(false) }
    var showInactiveReasonDialog by rememberSaveable { mutableStateOf(false) }
    var currentOrganizationStatus by rememberSaveable { mutableStateOf(OrganizationStatus.ACTIVE) }
    var currentInactiveReason by rememberSaveable { mutableStateOf("") }

    var noticePendingDelete by remember { mutableStateOf<OrganizationNotice?>(null) }
    var visitorToDelete by remember { mutableStateOf<VisitorOrganization?>(null) }
    val mockVisits = listOf(
        VisitModel(
            id = 1,
            organizationName = "هتل قصر طلایی",
            visitType = "بازدید حضوری برنامه‌ریزی شده",
            visitorName = "علی محمدی",
            date = "۱۴۰۳/۰۲/۱۵ , 11:30",
            city = "مشهد",
            district = "خیابان آزادی",
            icon = Icons.Default.DirectionsWalk,
            status = Status.PLANNED
        ),
        VisitModel(
            id = 2,
            organizationName = "هتل الماس",
            visitType = "بازدید تلفنی",
            visitorName = "علی رضایی",
            date = "۱۴۰۳/۰۲/۱۷ , 10:30",
            city = "مشهد",
            district = "خیابان آزادی",
            icon = Icons.Default.Phone,
            status = Status.DONE
        ),
        VisitModel(
            id = 3,
            organizationName = "هتل پارسیان",
            visitType = "بازدید حضوری غیربرنامه‌ریزی شده",
            visitorName = "مریم رضایی",
            date = "۱۴۰۳/۰۲/۱۶ , 02:30",
            city = "مشهد",
            district = "احمد آباد",
            icon = Icons.Default.DirectionsWalk,
            status = Status.CANCELLED
        ),
        VisitModel(
            id = 4,
            organizationName = "سازمان برق",
            visitType = "بازدید تلفنی",
            visitorName = "مریم مفرد",
            date = "۱۴۰۳/۰۲/۱۸ , 10:30",
            city = "مشهد",
            district = "پاسداران",
            icon = Icons.Default.Phone,
            status = Status.PLANNED
        )
    )


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
            status = Status.INACTIVE,
            defaultDiscountPercent = 15.0,
            defaultCommissionPercent = 7.5,
            description = "پورسانت بر اساس درصد فروش هر فصل محاسبه و تسویه می‌شود."
        )
    )

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

    var notices = listOf(
        OrganizationNotice(
            id = 1,
            type = "تذکر شفاهی",
            score = 2,
            description = "تاخیر در پاسخ‌گویی به پیگیری‌های واحد بازاریابی",
            createdAt = "1405/05/02",
            registrarName = "احمد شفاهی"
        ),
        OrganizationNotice(
            id = 2,
            type = "تذکر کتبی",
            score = 4,
            description = "عدم رعایت استانداردهای قرارداد همکاری در ارائه استند تبلیغاتی",
            createdAt = "1405/05/02",
            registrarName = "رضا موسوی"
        ),
        OrganizationNotice(
            id = 3,
            type = "تذکر شفاهی",
            score = -3,
            description = "مغایرت جزئی در اطلاعات ثبت‌شده حساب کاربری", createdAt = "1405/05/02",
            registrarName = "پرهام شیری"
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
                            visits = mockVisits,
                            onAddVisitClick = onAddVisitClick,
                            onVisitClick = onVisitClick
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

    // دیالوگ تأیید حذف
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


    if (showAddPersonSheet) {
        AddPersonBottomSheet(
            personState = personState,
            onDismiss = {
                showAddPersonSheet = false
            },
            onSavePerson = {
                if (personState.name.isNotBlank() && personState.phone.isNotBlank()) {
                    formState.organizationPersons.add(
                        PersonOrganization(
                            name = personState.name.trim(),
                            mobile = personState.mobile.trim(),
                            phone = personState.phone.trim(),
                            status = personState.status,
                            gender = personState.gender,
                            description = personState.description.trim()
                        )
                    )

                    formState.resetPersonFields(personState)
                    showAddPersonSheet = false
                }
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
private fun OrganizationBasicInfoTabContent(organization: OrganizationModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionTitle(stringResource(R.string.label_organization_basic_information))
        BasicInfoSection(organization)

        SectionTitle(stringResource(R.string.label_contact_information))
        ContactInfoSection()

        SectionTitle(stringResource(R.string.label_location))
        AddressMapCard(
            city = organization.city,
            region = organization.region,
            address = organization.address
        )

        SectionTitle(stringResource(R.string.label_analytical_information))
        AnalysisInfoSection(grade = organization.grade)

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
private fun OrganizationGeneralInfoTabContent(organization: OrganizationModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionTitle(stringResource(R.string.label_organization_statistical_information))
        BasicGeneralSection(organization)
    }
}


// ======================= تب اصلی بازدیدها =======================
@Composable
fun OrganizationVisitsTabContent(
    visits: List<VisitModel>,
    onAddVisitClick: () -> Unit,
    onVisitClick: ((Int) -> Unit),
    modifier: Modifier = Modifier
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
                SectionTitle(title = stringResource(R.string.label_visits_list))
            }
            FilledTonalButton(
                onClick = onAddVisitClick,
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
                    text = stringResource(R.string.label_register_visit),
                    style = typography.titleLarge
                )
            }
        }


        if (visits.isEmpty()) {
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
                        text = stringResource(R.string.msg_no_visit_found),
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
                itemsIndexed(visits) { index, visit ->
                    VisitCard(
                        item = visit,
                        onClick = { onVisitClick(visit.id) },
                        onEditClick = { /*onEditVisitClick(item.id)*/ },
                        onDeleteClick = { /*visitPendingDelete = item */ }
                    )
                }

            }
        }
    }
}

@Composable
fun VisitCard(
    item: VisitModel,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColors.cardBackground
        ),
        border = BorderStroke(
            width = 0.7.dp,
            color = appColors.border
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // ردیف اول: نوع بازدید (راست/شروع) و بج وضعیت (چپ/پایان)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                VisitTypeChip(
                    text = item.visitType,
                    icon = item.icon
                )

                StatusBadge(status = item.status)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // ردیف نام بازدیدکننده
            DetailRow(
                icon = Icons.Default.Person,
                text = item.visitorName
            )

            Spacer(modifier = Modifier.height(6.dp))

            // ردیف تاریخ و دکمه‌های اکشن در یک راستا برای کاهش ارتفاع کارت
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                DetailRow(
                    icon = Icons.Default.DateRange,
                    text = item.date
                )
                /*
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
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
                                }*/
            }
        }
    }
}

@Composable
private fun OrganizationPersonsTabContent(
    persons: List<PersonOrganization> = emptyList(),
    onAddPersonClick: () -> Unit = {},
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
                SectionTitle(title = stringResource(R.string.label_related_persons_list))
            }
            FilledTonalButton(
                onClick = onAddPersonClick,
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
                    text = stringResource(R.string.label_add_person),
                    style = typography.titleLarge
                )
            }
        }
        if (persons.isEmpty()) {
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
                        text = stringResource(R.string.msg_no_person_found),
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
                itemsIndexed(persons) { index, person ->
                    RelatedPersonDetailCard(index = index, person = person)
                }
            }
        }
    }
    /*    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        )
       {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    SectionTitle(title = stringResource(R.string.label_related_persons_list))
                }
                FilledTonalButton(
                    onClick = onAddPersonClick,
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
                        text = stringResource(R.string.label_add_person),
                        style = typography.titleLarge
                    )
                }
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                itemsIndexed(persons) { index, person ->
                    RelatedPersonDetailCard(index = index, person = person)
                }
            }
        }*/
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
    }
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
            //هدر کارت
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
                            .background(MaterialTheme.colorScheme.tertiaryContainer, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.RateReview,
                            null,
                            tint = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Text(
                        text = "تذکر ${index + 1}: ${notice.type}",
                        style = typography.titleMedium,
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

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // توضیحات تذکر
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "توضیحات:",
                    style = typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = notice.description.ifBlank { "-" },
                    style = typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // --- تاریخ و ثبت‌کننده ---
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ثبت‌کننده
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = notice.registrarName,
                        style = typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )


                }
                // تاریخ
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = notice.createdAt, // یا تاریخ تبدیل شده
                        style = typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                // ---------------------------------
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

    }
}

@Composable
private fun AdvertisingStandAssignmentCard(
    index: Int,
    advertisingStandAssignment: AdvertisingStandAssignment,
    onViewDetailsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

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
    onAssignContractClick: () -> Unit = {},
    onContractClick: (ContractUi) -> Unit,

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
                        onClick = { onContractClick(contract) }
                    )
                }
            }
        }
    }
}

@Composable
fun OrganizationTicketsTabContent(
    tickets: List<OrganizationOfferTicketModel>,
    onAddTicketClick: () -> Unit,
    onTicketClick: (OrganizationOfferTicketModel) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTicketForDetail by remember { mutableStateOf<OrganizationOfferTicketModel?>(null) }
    val appColors = LocalPartnerManagementColors.current

    Column(modifier = modifier.fillMaxSize()) {
        // هدر تب شامل عنوان و دکمه افزودن
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Box(modifier = Modifier.weight(1f)) {
                SectionTitle(title = stringResource(R.string.label_tickets_offers_list))
            }

            Spacer(modifier = Modifier.width(8.dp))

            FilledTonalButton(
                onClick = onAddTicketClick,
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
                    text = stringResource(R.string.label_contract_offer),
                    style = typography.titleLarge
                )
            }
        }

        if (tickets.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.ConfirmationNumber,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(R.string.msg_no_tickets_offers_found),
                        style = typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tickets, key = { it.id }) { ticket ->
                    TicketOfferCard(
                        ticket = ticket,
                        onClick = {
                            selectedTicketForDetail = ticket
                            onTicketClick(ticket)
                        }
                    )
                }
            }
        }
    }

    // دیالوگ نمایش جزئیات کامل آفر با کلیک روی کارت
    selectedTicketForDetail?.let { ticket ->
        TicketOfferDetailDialog(
            ticket = ticket,
            onDismiss = { selectedTicketForDetail = null }
        )
    }
}

@Composable
fun TicketOfferCard(
    ticket: OrganizationOfferTicketModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        onClick = onClick,

        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(0.5.dp, appColors.border)
    )
    {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ticket.offerPlanName,
                    style = typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                StatusBadge(status = Status.ACTIVE)

            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = ticket.title,
                style = typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 10.dp),
                thickness = 0.6.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.label_serial_range),
                        style = typography.labelSmall,
                        color = appColors.textSecondary
                    )
                    Text(
                        text = "${ticket.serialPrefix} : ${ticket.startSerial} - ${ticket.endSerial}",
                        style = typography.titleMedium
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = stringResource(R.string.label_serial_count),
                        style = typography.labelSmall,
                        color = appColors.textSecondary
                    )
                    Text(
                        text = "${ticket.serialCount} عدد",
                        style = typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
fun TicketOfferDetailDialog(
    ticket: OrganizationOfferTicketModel,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.action_close))
            }
        },
        title = {
            Text(
                text = stringResource(R.string.label_ticket_offer_detail),
                style = typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DetailItemRow(
                    label = stringResource(R.string.label_offer_plan),
                    value = ticket.offerPlanName
                )
                DetailItemRow(
                    label = stringResource(R.string.label_contract),
                    value = ticket.contractName
                )
                DetailItemRow(
                    label = stringResource(R.string.label_org_name),
                    value = ticket.organizationName
                )
                DetailItemRow(
                    label = stringResource(R.string.label_cooperation_model),
                    value = ticket.cooperationModel
                )
                DetailItemRow(
                    label = stringResource(R.string.label_serial_prefix),
                    value = ticket.serialPrefix
                )
                DetailItemRow(
                    label = stringResource(R.string.label_serial_count),
                    value = ticket.serialCount.toString()
                )
                DetailItemRow(
                    label = stringResource(R.string.label_start_serial),
                    value = ticket.startSerial.toString()
                )
                DetailItemRow(
                    label = stringResource(R.string.label_end_serial),
                    value = ticket.endSerial.toString()
                )
                DetailItemRow(
                    label = stringResource(R.string.label_serial_status),
                    value = stringResource(ticket.status.labelRes)
                )
                DetailItemRow(
                    label = stringResource(R.string.label_last_used_serial),
                    value = ticket.lastUsedSerial?.toString() ?: "---"
                )
                DetailItemRow(
                    label = stringResource(R.string.label_owner),
                    value = ticket.ownerName.ifEmpty { "---" })
                DetailItemRow(label = stringResource(R.string.label_title), value = ticket.title)
            }
        }
    )
}

@Composable
private fun DetailItemRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "$label:",
            style = typography.bodySmall,
            color = LocalPartnerManagementColors.current.textSecondary,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            style = typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(0.6f)
        )
    }
}


@Composable
private fun BasicInfoSection(
    organization: OrganizationModel
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
                    value = organization.name,
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
private fun BasicGeneralSection(
    organization: OrganizationModel,
    totalNoticesCount: Int = 3,
    totalScore: Int = 85,
    activeMarketersCount: Int = 6,
    assignedStandsCount: Int = 5,
    completedVisitsCount: Int = 12,
    plannedVisitsCount: Int = 4,
    totalAssignedSerials: Int = 1000,
    issuedSerialsCount: Int = 800,
    usedSerialsCount: Int = 620,
    revokedSerialsCount: Int = 30
) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme

    val remainingSerialsCount =
        (totalAssignedSerials - usedSerialsCount - revokedSerialsCount).coerceAtLeast(0)
    val usedProgress =
        if (totalAssignedSerials > 0) usedSerialsCount.toFloat() / totalAssignedSerials else 0f
    var showSerialsDetailSheet by rememberSaveable { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        // ================= ردیف اول: تجمیع تذکرات و تجمیع امتیاز =================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // تجمیع تذکرات
            StatMetricCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_total_warnings),
                count = totalNoticesCount,
                unit = stringResource(R.string.label_warnings_unit),
                icon = Icons.Outlined.WarningAmber,
                accentColor = if (totalNoticesCount > 0) appColors.warning else appColors.success,
                containerColor = if (totalNoticesCount > 0) appColors.warning.copy(alpha = 0.06f) else colors.surface
            )

            // تجمیع امتیاز
            StatMetricCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_total_score),
                count = totalScore,
                unit = stringResource(R.string.label_score_unit),
                icon = Icons.Outlined.Stars,
                accentColor = if (totalScore >= 0) appColors.success else colors.error,
                containerColor = if (totalScore >= 0) appColors.success.copy(alpha = 0.06f) else colors.error.copy(
                    alpha = 0.06f
                )
            )
        }

        // ================= ردیف دوم: بازاریابان فعال و استندهای اختصاص‌یافته =================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // بازاریابان فعال
            StatMetricCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_active_marketers),
                count = activeMarketersCount,
                unit = stringResource(R.string.label_marketers_unit),
                icon = Icons.Outlined.Group,
                accentColor = Color(0xFF0288D1),
                containerColor = Color(0xFF0288D1).copy(alpha = 0.06f)
            )

            // استندهای اختصاص‌یافته
            StatMetricCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_assigned_stands),
                count = assignedStandsCount,
                unit = stringResource(R.string.label_stands_unit),
                icon = Icons.Filled.Storefront,
                accentColor = colors.primary,
                containerColor = colors.primary.copy(alpha = 0.06f)
            )
        }

        // ================= ردیف دوم: استندها + بازدیدهای انجام‌شده + برنامه‌ریزی‌شده =================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            MiniMetricCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_completed_visits),
                count = completedVisitsCount,
                unit = stringResource(R.string.label_visits_unit),
                icon = Icons.Outlined.TaskAlt,
                accentColor = appColors.success
            )

            MiniMetricCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_planned_visits),
                count = plannedVisitsCount,
                unit = stringResource(R.string.label_visits_unit),
                icon = Icons.Outlined.CalendarMonth,
                accentColor = Color(0xFF0288D1)
            )
        }

        Spacer(Modifier.height(4.dp))

        // ================= کادر اصلی اختصاصی سریال‌ها (مینیمال و حرفه‌ای با دکمه جزئیات) =================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = colors.primary.copy(alpha = 0.04f)
            ),
            border = BorderStroke(1.dp, colors.primary.copy(alpha = 0.2f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // هدر کادر سریال
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(colors.primary.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ConfirmationNumber,
                                contentDescription = null,
                                tint = colors.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = stringResource(R.string.label_assigned_serials),
                                style = typography.titleMedium,
                                color = colors.onSurface
                            )
                            Text(
                                text = "$totalAssignedSerials ${stringResource(R.string.label_unit_count)}",
                                style = typography.labelSmall,
                                color = colors.primary,
                            )
                        }
                    }

                    // دکمه باز شدن باتم‌شیت
                    TextButton(
                        onClick = { showSerialsDetailSheet = true },
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.label_action_view_details),
                            style = typography.titleSmall,
                            color = colors.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = colors.primary
                        )
                    }
                }

                // نوار درصد پیشرفت مصرف سریال‌ها
                LinearProgressIndicator(
                    progress = { usedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = colors.primary,
                    trackColor = colors.outlineVariant.copy(alpha = 0.3f)
                )

                // ردیف خلاصه: مصرف‌شده و باقی‌مانده
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(colors.primary)
                        )
                        Text(
                            text = stringResource(R.string.label_serials_used) + ":",
                            style = typography.bodySmall,
                            color = appColors.textSecondary
                        )
                        Text(
                            text = "$usedSerialsCount",
                            style = typography.titleSmall,
                            color = colors.onSurface
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(appColors.warning)
                        )
                        Text(
                            text = stringResource(R.string.label_serials_remaining) + ":",
                            style = typography.bodySmall,
                            color = appColors.textSecondary
                        )
                        Text(
                            text = "$remainingSerialsCount",
                            style = typography.titleSmall,
                            color = appColors.warning
                        )
                    }
                }
            }
        }


        // باز شدن باتم‌شیت جزئیات سریال‌ها در صورت کلیک
        if (showSerialsDetailSheet) {
            SerialsDetailBottomSheet(
                totalAssigned = totalAssignedSerials,
                issuedCount = issuedSerialsCount,
                usedCount = usedSerialsCount,
                revokedCount = revokedSerialsCount,
                remainingCount = remainingSerialsCount,
                onDismiss = { showSerialsDetailSheet = false }
            )
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
private fun AnalysisInfoSection(
    grade: Int,
    ticketSaleCount: String = "1250",
    customerCapacity: String = "350 نفر",
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // ── بخش درجه‌بندی ──
        GradeSelector(
            grade = grade
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
                    value = ticketSaleCount,
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
                    value = customerCapacity,
                    icon = Icons.Outlined.Groups,
                    isHighlighted = false
                )
            }
        }
    }
}


@Composable
private fun StatusInfoSection(
    hasForeignGuests: Boolean = false,
    wantsAdvertisingStands: Boolean = false,
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
                value = if (hasForeignGuests) {
                    stringResource(R.string.label_has)
                } else {
                    stringResource(R.string.label_has_not)
                },
                icon = Icons.Outlined.Public,
                isHighlighted = hasForeignGuests
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 14.dp),
                thickness = 0.8.dp,
                color = colors.outlineVariant.copy(alpha = 0.5f)
            )

            // ── تمایل به دریافت استند تبلیغاتی ──
            InfoRowItem(
                title = stringResource(R.string.label_desire_receive_advertising_stands),
                value = if (wantsAdvertisingStands) {
                    stringResource(R.string.label_has)
                } else {
                    stringResource(R.string.label_has_not)
                },
                icon = Icons.Outlined.Storefront,
                isHighlighted = wantsAdvertisingStands
            )
        }
    }
}

/**
 * مینی کارت متریک برای نمایش ۳ تایی فشرده و متوازن
 */
@Composable
private fun MiniMetricCard(
    title: String,
    count: Int,
    unit: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = "$count",
                style = typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = title,
                style = typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
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

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = title,
                    style = typography.labelMedium,
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
    city: String = "",
    region: String = "",
    address: String = ""
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
            // باکس آیکون موقعیت مکانی / پیش‌نمایش نقشه
            Box(
                modifier = Modifier
                    .size(width = 110.dp, height = 96.dp)
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

            // ستون نمایش جزئیات شهر، منطقه و آدرس
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // نمایش شهر و منطقه در صورت وجود
                val locationScope = listOf(city.trim(), region.trim())
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
                        text = address.ifBlank { "-" },
                        style = typography.bodyMedium,
                        color = colors.onSurface
                    )
                }
            }
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${index + 1}. ${visitor.name}".trim(),
                    style = typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                StatusBadge(visitor.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                // تاریخ شروع
                VisitorStatItem(
                    label = stringResource(R.string.label_start_date),
                    date = visitor.startDate,
                    icon = Icons.Default.CalendarMonth
                )

                // تاریخ خاتمه
                VisitorStatItem(
                    label = stringResource(R.string.label_end_date),
                    date = visitor.endDate,
                    icon = Icons.Default.CalendarMonth
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
private fun VisitorStatItem(
    label: String,
    date: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = colors.onSurfaceVariant
        )

        // عنوان (مثلاً: تاریخ شروع:)
        Text(
            text = "$label:",
            style = typography.labelMedium,
            color = colors.onSurfaceVariant
        )

        // مقدار تاریخ
        Text(
            text = date.ifBlank { "—" },
            style = typography.labelLarge,
            color = colors.onSurface
        )
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
