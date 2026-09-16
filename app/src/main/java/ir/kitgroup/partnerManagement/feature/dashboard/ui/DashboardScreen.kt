package ir.kitgroup.partnerManagement.feature.dashboard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.theme.*
import ir.kitgroup.partnerManagement.core.ui.util.SideCurvedHeaderShape
import ir.kitgroup.partnerManagement.feature.dashboard.model.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import ir.kitgroup.partnerManagement.core.ui.components.LocationRow
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import ir.kitgroup.partnerManagement.core.ui.SessionViewModel
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.util.Status
import ir.kitgroup.partnerManagement.core.ui.util.UserRole
import ir.kitgroup.partnerManagement.feature.visits.model.VisitModel
import ir.kitgroup.partnerManagement.feature.visits.ui.VisitTypeChip
import ir.kitgroup.partnerManagement.navigation.Screen

@Composable
fun DashboardScreen(
    navController: NavController, viewModel: SessionViewModel = hiltViewModel()
) {

    val role by viewModel.userRole.collectAsState()
    val isSupervisor = role == UserRole.SUPERVISOR.name

    val summaryItems = listOf(
        SummaryCardData(
            Icons.Filled.Domain,
            Orange,
            "34",
            stringResource(R.string.label_total_views_today),
            Orange
        ),
        SummaryCardData(
            Icons.Filled.Person,
            Green,
            "18",
            stringResource(R.string.label_in_person_visits),
            Green
        ),
        SummaryCardData(
            Icons.Filled.Call,
            Blue,
            "16",
            stringResource(R.string.label_phone_calls),
            Blue
        )
    )

    val visits = listOf(
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
            organizationName = "سازمان پالاس",
            visitType = "بازدید تلفنی",
            visitorName = "علی رضایی",
            date = "۱۴۰۳/۰۲/۱۷ , 10:30", city = "مشهد",
            district = "خیابان آزادی",
            icon = Icons.Default.Phone,
            status = Status.DONE
        ),
        VisitModel(
            id = 3,
            organizationName = "هتل پردیسان",
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
            date = "۱۴۰۳/۰۲/۱۸ , 10:30", city = "مشهد",
            district = "پاسداران",
            icon = Icons.Default.Phone,
            status = Status.PLANNED
        )
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        DashboardHeader()
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item { SummarySection(summaryItems) }
                item { Spacer(Modifier.width(6.dp)) }
                item {
                    QuickActionsSection(
                        onQuickContractClick = { navController.navigate(Screen.AddContract.route) },
                        onQuickOrganizationClick = { navController.navigate(Screen.AddOrganization.route) },
                        onQuickVisitClick = { navController.navigate(Screen.RegisterVisit.createRoute()) },
                        onQuickContractOfferClick = { navController.navigate(Screen.AddContractOffer.route) }
                    )
                }
                item { Spacer(Modifier.width(6.dp)) }
                item {
                    QuickAccessSection(
                        onContractClick = { navController.navigate(Screen.ContractsList.route) },
                        onAdvertisingClick = {
                            if (isSupervisor) {
                                navController.navigate(Screen.AdvertisingStandMenu.route)
                            } else {
                                navController.navigate(
                                    Screen.AdvertisingStandAssignmentOrganizationList.route
                                )
                            }
                        },
                        onReportClick = { navController.navigate(Screen.ReportMenu.route) },
                        onMapClick = {
                            navController.navigate(
                                Screen.Map.route
                            )
                        }
                    )
                }
                item { Spacer(Modifier.width(6.dp)) }
                item {
                    SectionTitle(
                        stringResource(R.string.label_schedule_title),
                        Icons.Default.Schedule
                    )
                }
                items(
                    items = visits,
                    key = { it.id }
                ) { item ->
                    val isEditableScheduledPhysicalVisit =
                        item.status == Status.PLANNED

                    VisitCard(
                        item = item,
                        isSupervisor = isSupervisor,
                        onClick = {
                            if (isEditableScheduledPhysicalVisit) {
                                navController.navigate(
                                    Screen.RegisterVisit.createRoute(
                                        visitId = item.id
                                    )
                                )
                            } else {
                                navController.navigate(
                                    Screen.VisitDetail.createRoute(
                                        visitId = item.id
                                    )
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DashboardHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = SideCurvedHeaderShape(50f)
            )
            .windowInsetsPadding(WindowInsets.statusBars)
            .height(60.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.greeting_text),
            color = MaterialTheme.colorScheme.onPrimary,
            style = typography.titleLarge
        )
        //   NotificationIcon()
    }
}

@Composable
private fun NotificationIcon() {
    Box(
        modifier = Modifier.size(32.dp),
        contentAlignment = Alignment.Center
    ) {

        Icon(
            painter = painterResource(R.drawable.ic_bell),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(24.dp)
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(4.dp, (-4).dp)
                .size(16.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.error),
            contentAlignment = Alignment.Center
        ) {
            Text("9", color = MaterialTheme.colorScheme.onError, fontSize = 9.sp)
        }
    }
}


@Composable
private fun SummarySection(items: List<SummaryCardData>) {

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

        SectionTitle(stringResource(R.string.label_summary_title))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            items.forEach {
                SummaryCard(it, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun SummaryCard(data: SummaryCardData, modifier: Modifier) {
    Card(
        modifier = modifier.height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = data.accent.copy(alpha = 0.08f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(data.iconColor.copy(.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(data.icon, null, tint = data.iconColor)
            }

            Text(
                data.value,
                color = MaterialTheme.colorScheme.onSurface,
                style = typography.displayMedium
            )

            Text(
                data.title,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = typography.labelMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(20))
                    .background(data.accent)
            )
        }
    }
}


@Composable
private fun QuickActionsSection(
    onQuickContractClick: () -> Unit,
    onQuickOrganizationClick: () -> Unit,
    onQuickVisitClick: () -> Unit,
    onQuickContractOfferClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SectionTitle(stringResource(R.string.label_shortcut_title))

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickActionCard(
                title = stringResource(R.string.label_contract),
                icon = Icons.Filled.AssignmentTurnedIn,
                onClick = onQuickContractClick,
                modifier = Modifier.weight(1f)
            )

            QuickActionCard(
                title = stringResource(R.string.label_add_new_organization),
                icon = Icons.Filled.AddBusiness,
                onClick = onQuickOrganizationClick,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickActionCard(
                title = stringResource(R.string.label_register_visit),
                icon = Icons.Filled.FactCheck,
                onClick = onQuickVisitClick,
                modifier = Modifier.weight(1f)
            )
            QuickActionCard(
                title = stringResource(R.string.label_contract_offer),
                icon = Icons.Filled.FactCheck,
                onClick = onQuickContractOfferClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun QuickActionCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier
) {

    Card(
        modifier = modifier
            .height(64.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color.Transparent)
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                        )
                    )
                )
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Icon(
                icon,
                null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(18.dp)
            )

            Spacer(Modifier.width(6.dp))

            Text(
                title, color = MaterialTheme.colorScheme.onPrimary, style = typography.labelMedium
            )
        }
    }
}

@Composable
fun QuickAccessSection(
    onContractClick: () -> Unit,
    onAdvertisingClick: () -> Unit,
    onReportClick: () -> Unit,
    onMapClick: () -> Unit
) {
    Column {

        SectionTitle(stringResource(R.string.label_quick_access))

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickAccessCard(
                title = stringResource(R.string.label_contract_organization),
                icon = Icons.Filled.Inventory2,
                backgroundColor = PartnerManagementTheme.colors.successContainer,
                iconTint = PartnerManagementTheme.colors.onSuccessContainer,
                onClick = onContractClick,
                modifier = Modifier.weight(1f)
            )
            QuickAccessCard(
                title = stringResource(R.string.label_advertising_stands),
                icon = Icons.Filled.Inventory2,
                backgroundColor = PartnerManagementTheme.colors.warningContainer,
                iconTint = PartnerManagementTheme.colors.onWarningContainer,
                onClick = onAdvertisingClick,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickAccessCard(
                title = stringResource(R.string.label_reports),
                icon = Icons.Filled.Assessment,
                backgroundColor = PartnerManagementTheme.colors.purpleContainer,
                iconTint = PartnerManagementTheme.colors.onPurpleContainer,
                onClick = onReportClick,
                modifier = Modifier.weight(1f)
            )
            QuickAccessCard(
                title = stringResource(R.string.label_map),
                icon = Icons.Filled.Map,
                backgroundColor = PartnerManagementTheme.colors.infoContainer,
                iconTint = PartnerManagementTheme.colors.onInfoContainer,
                onClick = onMapClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun QuickAccessCard(
    title: String,
    icon: ImageVector,
    backgroundColor: Color,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(104.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = BorderStroke(
            width = 1.dp,
            color = iconTint.copy(alpha = 0.10f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            pressedElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 12.dp,
                    vertical = 10.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = title,
                style = typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/*
@Composable
fun VisitCard(
    item: VisitModel,
    isSupervisor: Boolean,
    onClick: () -> Unit,
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
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.organizationName,
                        style = typography.titleLarge,
                        color = appColors.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    StatusBadge(item.status)

                }

                Spacer(modifier = Modifier.height(8.dp))

                VisitTypeChip(
                    text = item.visitType,
                    icon = item.icon
                )

                Spacer(modifier = Modifier.height(6.dp))

                // نمایش نام بازاریاب صرفاً در صورتی که نقش سرپرست باشد و مقدار داشته باشد
                if (isSupervisor && !item.visitorName.isNullOrBlank()) {
                    DetailRow(
                        icon = Icons.Default.Person,
                        text = item.visitorName
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))

                DetailRow(
                    icon = Icons.Default.DateRange,
                    text = item.date
                )

                Spacer(modifier = Modifier.height(6.dp))

                LocationRow(
                    location = "${item.city}، ${item.district}"
                )
                Spacer(modifier = Modifier.height(6.dp))

            }
        }
    }
}
*/



@Composable
private fun VisitCard(
    item: VisitModel,
    isSupervisor: Boolean,
    onClick: () -> Unit
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
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = item.organizationName,
                    style = typography.titleLarge
                )
                Spacer(modifier = Modifier.height(6.dp))

                VisitTypeChip(
                    text = item.visitType,
                    icon = item.icon
                )
                Spacer(modifier = Modifier.height(6.dp))

                // نمایش نام بازاریاب صرفاً در صورتی که نقش سرپرست باشد و مقدار داشته باشد
                if (isSupervisor && item.visitorName.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = LocalPartnerManagementColors.current.textSecondary
                        )
                        Text(
                            text = item.visitorName,
                            style = typography.labelMedium,
                            color = LocalPartnerManagementColors.current.textSecondary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))

                LocationRow(
                    location = "${item.city}، ${item.district}"
                )
            }

            VisitTimeAndStatus(item)
        }
    }
}


@Composable
fun VisitTimeAndStatus(item: VisitModel) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = item.date,
            style = typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        StatusBadge(item.status)
    }
}

