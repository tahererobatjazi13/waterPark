package ir.kitgroup.partnerManagement.feature.organization.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.NotInterested
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.RateReview
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomDialog
import ir.kitgroup.partnerManagement.core.ui.theme.RedContent
import ir.kitgroup.partnerManagement.core.ui.util.OrganizationDetailTab
import ir.kitgroup.partnerManagement.core.ui.util.Status
import ir.kitgroup.partnerManagement.feature.advertising_stand.model.AdvertisingStandAssignment
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand_assignment_visitor.DetailLine
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand_assignment_visitor.InfoLine
import ir.kitgroup.partnerManagement.feature.advertising_stand.ui.stand_assignment_visitor.allocationItemIcon
import ir.kitgroup.partnerManagement.feature.organization.model.OrganizationNotice
import ir.kitgroup.partnerManagement.feature.organization.model.VisitorOrganization
import ir.kitgroup.partnerManagement.feature.report.ui.visitor.VerticalDivider


@Composable
fun OrganizationDetailScreen(
    organizationId: Int,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onDisableClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    var selectedTab by rememberSaveable { mutableStateOf(OrganizationDetailTab.INFO) }
    var showNoticeDialog by rememberSaveable { mutableStateOf(false) }
    var showDisableDialog by rememberSaveable { mutableStateOf(false) }


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
            endDate = "1405/02/15"
        ),
        VisitorOrganization(
            id = 2,
            name = "سارا محمدی",
            startDate = "1405/02/05",
            endDate = "1405/02/15"
        ),
        VisitorOrganization(
            id = 3,
            name = "علی جعفری",
            startDate = "1405/02/05",
            endDate = "1405/02/15"
        )
    )

    val stands = listOf(
        AdvertisingStandAssignment(
            "1",
            "محمد احمدی",
            "هتل پارسیان آزادی",
            "رومیزی",
            "stand",
            5,
            "۱۴۰۳/۰۳/۲۲",
            Status.ACTIVE
        ),
        AdvertisingStandAssignment(
            "2",
            "سارا مرادی",
            "هتل اسپیناس پالاس",
            "دیواری",
            "wall",
            3,
            "۱۴۰۳/۰۳/۲۴",
            Status.DRAFT
        ),
        AdvertisingStandAssignment(
            "3",
            "علی رضایی",
            "هتل هما",
            "کیوسک",
            "kiosk",
            2,
            "۱۴۰۳/۰۳/۲۰",
            Status.CANCELLED
        ),
        AdvertisingStandAssignment(
            "4",
            "نازنین کریمی",
            "هتل بزرگ تهران",
            "رومیزی",
            "stand",
            4,
            "۱۴۰۳/۰۳/۲۵",
            Status.RETURNED
        ),
        AdvertisingStandAssignment(
            "3",
            "علی رضایی",
            "هتل هما",
            "کیوسک",
            "kiosk",
            2,
            "۱۴۰۳/۰۳/۲۰",
            Status.ACTIVE
        ),
        AdvertisingStandAssignment(
            "4",
            "نازنین کریمی",
            "هتل بزرگ تهران",
            "رومیزی",
            "stand",
            4,
            "۱۴۰۳/۰۳/۲۵",
            Status.DRAFT
        )
    )

    val notices = listOf(
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
            score = 1,
            description = "مغایرت جزئی در اطلاعات ثبت‌شده حساب کاربری", createdAt = "1405/05/02"
        )
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
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
                        onDisable = { showDisableDialog = true }
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

                ScrollableTabRow(
                    selectedTabIndex = selectedTab.ordinal,
                    containerColor = appColors.screenBackground,
                    contentColor = MaterialTheme.colorScheme.primary,
                    edgePadding = 16.dp,
                    divider = { HorizontalDivider(color = appColors.border) }
                ) {
                    OrganizationDetailTab.entries.forEach { tab ->
                        Tab(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            text = {
                                Text(
                                    text = stringResource(tab.titleRes),
                                    style = typography.titleMedium
                                )
                            }
                        )
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
                        OrganizationVisitorTabContent(
                            visitors = visitors,
                        )
                    }

                    OrganizationDetailTab.STANDS -> {
                        OrganizationStandsTabContent(assignments = stands)
                    }

                    OrganizationDetailTab.NOTICES -> {
                        OrganizationNoticesTabContent(notices,
                            onAddNoticeClick = { showNoticeDialog = true }
                        )
                    }

                }
            }
        }
    }

    if (showNoticeDialog) {
        AddOrganizationNoticeDialog(
            onDismiss = { showNoticeDialog = false },
            onConfirm = { noticeType, score, description ->
                showNoticeDialog = false
            }
        )
    }

    CustomDialog(
        isVisible = showDisableDialog,
        title = stringResource(R.string.label_deactivation),
        message = stringResource(R.string.msg_dialog_deactivate_organization),
        confirmText = stringResource(R.string.label_deactivation),
        dismissText = stringResource(R.string.label_cancellation),
        confirmColor = RedContent,
        onConfirm = {
            showDisableDialog = false
            onDisableClick()
        },
        onDismiss = { showDisableDialog = false },
    )
}

@Composable
private fun OrganizationBasicInfoTabContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        SectionTitle(stringResource(R.string.label_organization_basic_information))
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
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        itemsIndexed(persons) { index, person ->
            RelatedPersonDetailCard(index = index, person = person)
        }
    }
}

@Composable
private fun OrganizationVisitorTabContent(
    visitors: List<VisitorOrganization> = emptyList()
) {
    if (visitors.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Groups,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(40.dp)
                )

                Text(
                    text = "هیچ بازدیدکننده‌ای ثبت نشده است",
                    style = typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            itemsIndexed(visitors) { index, visitor ->
                RelatedVisitorCard(index = index, visitor = visitor)
            }
        }
    }
}


@Composable
private fun OrganizationNoticesTabContent(
    notices: List<OrganizationNotice>,
    onAddNoticeClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
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
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(notices) { index, notice ->
                    OrganizationNoticeCard(
                        index = index,
                        notice = notice
                    )
                }
            }
        }
    }
}

@Composable
private fun OrganizationNoticeCard(
    index: Int,
    notice: OrganizationNotice
) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        ),
        border = BorderStroke(1.dp, colors.outlineVariant)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(colors.tertiaryContainer, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.RateReview,
                        contentDescription = null,
                        tint = colors.onTertiaryContainer,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(Modifier.width(10.dp))

                Text(
                    text = "تذکر ${index + 1}: ${notice.type}",
                    style = typography.titleMedium,
                    color = colors.onSurface,
                    modifier = Modifier.weight(1f)
                )

                NoticeScoreChip(score = notice.score)
            }

            HorizontalDivider(color = colors.outlineVariant.copy(alpha = 0.5f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${stringResource(R.string.label_description)}:",
                    style = typography.labelMedium,
                    color = colors.onSurfaceVariant,
                    maxLines = 1
                )

                Spacer(Modifier.width(6.dp))

                Text(
                    text = notice.description.ifBlank { "-" },
                    style = typography.bodyMedium,
                    color = colors.onSurface,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Composable
private fun NoticeScoreChip(
    score: Int
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        shape = RoundedCornerShape(50),
        color = when (score) {
            in 1..2 -> colors.errorContainer
            in 3..4 -> colors.tertiaryContainer
            else -> colors.primaryContainer
        }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = when (score) {
                    in 1..2 -> colors.error
                    in 3..4 -> colors.onTertiaryContainer
                    else -> colors.primary
                },
                modifier = Modifier.size(14.dp)
            )

            Text(
                text = score.toString(),
                style = typography.titleMedium,
                color = when (score) {
                    in 1..2 -> colors.error
                    in 3..4 -> colors.onTertiaryContainer
                    else -> colors.primary
                }
            )
        }
    }
}

@Composable
private fun OrganizationStandsTabContent(
    assignments: List<AdvertisingStandAssignment>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (assignments.isEmpty()) {
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
                        imageVector = Icons.Filled.WorkspacePremium,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(40.dp)
                    )

                    Text(
                        text = "هیچ استندی تخصیص داده نشده است",
                        style = typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(
                    items = assignments,
                    key = { _, item -> item.id }
                ) { index, item ->
                    AdvertisingStandAssignmentCard(
                        index = index,
                        assignment = item
                    )
                }
            }
        }
    }
}

@Composable
private fun AdvertisingStandAssignmentCard(
    index: Int,
    assignment: AdvertisingStandAssignment
) {
    val colors = MaterialTheme.colorScheme
    val itemIcon = allocationItemIcon(assignment.itemIconName)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surface
        ),
        border = BorderStroke(1.dp, colors.outlineVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoLine(
                    label = stringResource(R.string.label_visitor_name),
                    value = assignment.visitorName,
                    leadingIcon = Icons.Default.PersonOutline,
                    modifier = Modifier.weight(1f)
                )

                StatusBadge(assignment.status)
            }

            HorizontalDivider(
                thickness = 0.6.dp,
                color =  LocalPartnerManagementColors.current.border
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DetailLine(
                        label = stringResource(R.string.label_organization_name),
                        value = assignment.organizationName,
                        icon = Icons.Default.Business
                    )

                    DetailLine(
                        label = stringResource(R.string.label_allocated_date),
                        value = assignment.allocatedDate,
                        icon = Icons.Default.CalendarMonth
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    DetailLine(
                        label = stringResource(R.string.label_item_type),
                        value = assignment.itemType,
                        icon = itemIcon
                    )

                    DetailLine(
                        label = stringResource(R.string.label_allocated_count),
                        value = "${assignment.count} ${stringResource(R.string.label_unit_count)}",
                        icon = Icons.Default.Inventory2
                    )
                }
            }
        }
    }
}

@Composable
private fun StandInfoRow(
    title: String,
    value: String
) {
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "$title:",
            style = typography.labelMedium,
            color = colors.onSurfaceVariant,
            maxLines = 1
        )

        Text(
            text = value.ifBlank { "-" },
            style = typography.labelMedium,
            color = colors.onSurface,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )
    }
}


private fun standIconByName(name: String): ImageVector {
    return when (name.lowercase()) {
        "stand" -> Icons.Filled.WorkspacePremium
        "wall" -> Icons.Filled.LocationOn
        "kiosk" -> Icons.Filled.Business
        else -> Icons.Filled.RateReview
    }
}

@Composable
private fun BasicInfoSection() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        InfoCard(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.label_organization_name),
            value = "هتل آبی پارسیان",
            icon = Icons.Filled.Business,
            isMainTitle = true
        )

        TwoColumnInfoRow(
            start = {
                InfoCard(
                    title = stringResource(R.string.label_organization_type),
                    value = "هتل",
                    icon = Icons.Filled.Category
                )
            },
            end = {
                InfoCard(
                    title = stringResource(R.string.label_organization_level),
                    value = "معمولی",
                    icon = Icons.Filled.WorkspacePremium
                )
            }
        )

        TwoColumnInfoRow(
            start = {
                InfoCard(
                    title = stringResource(R.string.label_owner_name),
                    value = "حسینی",
                    icon = Icons.Filled.ManageAccounts
                )
            },
            end = {
                InfoCard(
                    title = stringResource(R.string.label_english_name),
                    value = "parsian",
                    icon = Icons.Filled.Language
                )
            }
        )

        TwoColumnInfoRow(
            start = {
                InfoCard(
                    title = stringResource(R.string.label_national_id),
                    value = "125",
                    icon = Icons.Filled.Badge
                )
            },
            end = {
                InfoCard(
                    title = stringResource(R.string.label_organization_status),
                    value = "فعال",
                    icon = Icons.Filled.Verified
                )
            }
        )
    }
}

@Composable
private fun ContactInfoSection() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        TwoColumnInfoRow(
            start = {
                InfoCard(
                    title = stringResource(R.string.label_organization_land_line),
                    value = "025685",
                    icon = Icons.Filled.Phone
                )
            },
            end = {
                InfoCard(
                    title = stringResource(R.string.label_phone),
                    value = "02112345678",
                    icon = Icons.Filled.Phone
                )
            }
        )

        TwoColumnInfoRow(
            start = {
                InfoCard(
                    title = stringResource(R.string.label_mobile),
                    value = "09129876543",
                    icon = Icons.Filled.Smartphone
                )
            },
            end = {
                InfoCard(
                    title = stringResource(R.string.label_email),
                    value = "info@parsian.com",
                    icon = Icons.Filled.Email
                )
            }
        )
    }
}

@Composable
private fun AnalysisInfoSection() {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        GradeDisplayCard(
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
private fun GradeDisplayCard(
    grade: Int
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = stringResource(R.string.label_organization_grade),
                style = typography.labelSmall,
                color = colors.onSurfaceVariant
            )

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                for (i in 5 downTo 1) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = if (i <= grade) {
                            androidx.compose.ui.graphics.Color(0xFFFFC107)
                        } else {
                            colors.outlineVariant
                        },
                        modifier = Modifier.size(28.dp)
                    )

                    if (i != 1) {
                        Spacer(Modifier.width(4.dp))
                    }
                }
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
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColors.cardBackgroundAlt
        ),
        border = BorderStroke(
            width = 1.dp,
            color = appColors.border.copy(alpha = 0.7f)
        )
    ) {
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
    visitor: VisitorOrganization
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColors.cardBackgroundAlt
        ),
        border = BorderStroke(
            width = 1.dp,
            color = appColors.border.copy(alpha = 0.7f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${index + 1}. ${visitor.name}".trim(),
                    style = typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

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

                VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)

                VisitorStatItem(
                    date = visitor.endDate,
                    label = stringResource(R.string.label_end_date),
                    icon = Icons.Default.CalendarMonth,
                    modifier = Modifier.weight(1f)
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
            Text(
                text = date,
                style = typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

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
    onDisable: () -> Unit
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
            height = 38.dp,
            cornerRadius = 12.dp,
            textStyle = typography.titleLarge,
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
            icon = Icons.Filled.Edit,
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                contentColor = colors.onPrimary
            )
        )

        CustomButton(
            text = stringResource(R.string.label_deactivation),
            onClick = onDisable,
            fillMaxWidth = false,
            modifier = Modifier.weight(1f),
            height = 38.dp,
            cornerRadius = 12.dp,
            textStyle = typography.titleLarge,
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
            icon = Icons.Filled.NotInterested,
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.error,
                contentColor = colors.onError
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
