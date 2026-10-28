package ir.kitgroup.partnerManagement.feature.meeting.ui.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.model.MeetingWithDetail
import ir.kitgroup.partnerManagement.core.ui.SessionViewModel
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.DatePickerDialog
import ir.kitgroup.partnerManagement.core.ui.components.EmptyState
import ir.kitgroup.partnerManagement.core.ui.components.FilterSection
import ir.kitgroup.partnerManagement.core.ui.components.OfflinePendingFooterBanner
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.MeetingStatus
import ir.kitgroup.partnerManagement.core.ui.util.MeetingType
import ir.kitgroup.partnerManagement.core.ui.util.SyncStatus
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.UserRole
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate
import saman.zamani.persiandate.PersianDate

@Composable
fun MeetingsListScreen(
    onMeetingClick: (String) -> Unit,
    onAddMeetingClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MeetingsListViewModel = hiltViewModel(),
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    val allMeetings by viewModel.meetings.collectAsState()
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme

    val pickDateLabel = stringResource(R.string.label_pick_date)

    var searchQuery by remember { mutableStateOf("") }
    var selectedStartDate by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedStatus by rememberSaveable { mutableStateOf<Int?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    val roleCode by sessionViewModel.roleCode.collectAsState()
    val currentRole = remember(roleCode) { UserRole.fromCode(roleCode.toString()) }
    val isSupervisor = currentRole == UserRole.SUPERVISOR

    val today = remember { PersianDate() }
    val todayDate = remember(today) {
        normalizeDate(
            "${today.shYear}/${today.shMonth.toString().padStart(2, '0')}/${
                today.shDay.toString().padStart(2, '0')
            }"
        )
    }
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowMessage -> {
                    snackBarHostState.showSnackbar(event.message.asString(context))
                }

                is UiEvent.ShowError -> {
                    snackBarHostState.showSnackbar(event.error.asString(context))
                }
            }
        }
    }

    val statusOptions = remember { MeetingStatus.entries }

    val filteredItems = remember(
        allMeetings,
        searchQuery,
        selectedStartDate,
        selectedStatus,
        todayDate
    ) {
        allMeetings?.filter { item ->
            val matchesSearch = searchQuery.isBlank() ||
                    item.subjectVisitName.contains(searchQuery, ignoreCase = true) ||
                    item.organizationName.contains(searchQuery, ignoreCase = true) ||
                    item.visitorName.contains(searchQuery, ignoreCase = true) ||
                    item.meeting.visitDate?.contains(searchQuery, ignoreCase = true) == true

            val matchesStatus = selectedStatus == null || item.meeting.status == selectedStatus

            val matchesDateRange = selectedStartDate == null || run {
                val itemVisitDate = item.meeting.visitDate ?: return@run false
                val itemDate = normalizeDate(itemVisitDate)
                val startDate = normalizeDate(selectedStartDate!!)
                itemDate in startDate..todayDate
            }

            matchesSearch && matchesStatus && matchesDateRange
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colors.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    viewModel.checkCanNavigateToAddMeeting {
                        onAddMeetingClick()
                    }
                },
                containerColor = appColors.success,
                contentColor = colors.onPrimary,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth(0.55f)
                    .height(52.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.label_register_visit),
                        style = typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(start = 16.dp, end = 16.dp, top = 12.dp)
        ) {
            // کادر جستجو
            FilterSection(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                hint = stringResource(R.string.label_search)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // ردیف فیلترهای وضعیت و تاریخ
            MeetingFiltersRow(
                selectedDate = selectedStartDate ?: pickDateLabel,
                selectedStatus = selectedStatus,
                statusOptions = statusOptions,
                onDateClick = { showDatePicker = true },
                onClearDate = { selectedStartDate = null },
                onStatusSelected = { selectedStatus = it },
                onClearStatus = { selectedStatus = null },
                isDateFiltered = selectedStartDate != null
            )

            Spacer(modifier = Modifier.height(6.dp))

            when {
                filteredItems == null -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(36.dp),
                            color = colors.primary
                        )
                    }
                }

                filteredItems.isEmpty() -> {
                    EmptyState(
                        textRes = R.string.msg_no_item_found,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(top = 6.dp, bottom = 90.dp)
                    ) {
                        items(
                            items = filteredItems,
                            key = { it.meeting.meetingId }
                        ) { item ->
                            MeetingCard(
                                item = item,
                                isSupervisor = isSupervisor,
                                onClick = { onMeetingClick(item.meeting.meetingId) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismiss = { showDatePicker = false },
            onDateSelected = { date ->
                selectedStartDate = normalizeDate(
                    "${date.year}/${date.month.toString().padStart(2, '0')}/${
                        date.day.toString().padStart(2, '0')
                    }"
                )
                showDatePicker = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MeetingFiltersRow(
    selectedDate: String,
    selectedStatus: Int?,
    statusOptions: List<MeetingStatus>,
    onDateClick: () -> Unit,
    onClearDate: () -> Unit,
    onStatusSelected: (Int) -> Unit,
    onClearStatus: () -> Unit,
    isDateFiltered: Boolean
) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme
    var statusExpanded by remember { mutableStateOf(false) }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // لیبل نشان‌دهنده فیلترها
            item {
                Row(
                    modifier = Modifier.height(38.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_filter),
                        contentDescription = null,
                        tint = appColors.textSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.label_filters),
                        color = appColors.textSecondary,
                        style = typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // چیپ دراپ‌داون وضعیت
            item {
                ExposedDropdownMenuBox(
                    expanded = statusExpanded,
                    onExpandedChange = { statusExpanded = !statusExpanded }
                ) {
                    val statusText = selectedStatus?.let { id ->
                        MeetingStatus.fromId(id)?.let { stringResource(it.titleRes) }
                    } ?: stringResource(R.string.label_status)

                    val isStatusActive = selectedStatus != null
                    val bgColor =
                        if (isStatusActive) colors.primaryContainer.copy(alpha = 0.45f) else appColors.cardBackground
                    val contentColor = if (isStatusActive) colors.primary else appColors.textPrimary
                    val borderColor =
                        if (isStatusActive) colors.primary.copy(alpha = 0.5f) else appColors.border

                    Surface(
                        modifier = Modifier
                            .height(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { statusExpanded = !statusExpanded },
                        shape = RoundedCornerShape(10.dp),
                        color = bgColor,
                        border = BorderStroke(0.8.dp, borderColor)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterAlt,
                                contentDescription = null,
                                tint = contentColor,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = statusText,
                                style = typography.labelSmall,
                                fontWeight = if (isStatusActive) FontWeight.Bold else FontWeight.Normal,
                                color = contentColor,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = if (isStatusActive) Icons.Default.Close else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = if (isStatusActive) colors.primary else appColors.textSecondary,
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        if (isStatusActive) onClearStatus() else statusExpanded =
                                            !statusExpanded
                                    }
                            )
                        }
                    }

                    ExposedDropdownMenu(
                        expanded = statusExpanded,
                        onDismissRequest = { statusExpanded = false }
                    ) {
                        statusOptions.forEach { status ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = stringResource(id = status.titleRes),
                                        style = typography.labelMedium
                                    )
                                },
                                onClick = {
                                    onStatusSelected(status.id)
                                    statusExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // چیپ انتخاب تاریخ
            item {
                val bgColor =
                    if (isDateFiltered) colors.primaryContainer.copy(alpha = 0.45f) else appColors.cardBackground
                val contentColor = if (isDateFiltered) colors.primary else appColors.textPrimary
                val borderColor =
                    if (isDateFiltered) colors.primary.copy(alpha = 0.5f) else appColors.border

                Surface(
                    modifier = Modifier
                        .height(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onDateClick() },
                    shape = RoundedCornerShape(10.dp),
                    color = bgColor,
                    border = BorderStroke(0.8.dp, borderColor)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = contentColor,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = selectedDate,
                            style = typography.labelSmall,
                            fontWeight = if (isDateFiltered) FontWeight.Bold else FontWeight.Normal,
                            color = contentColor,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = if (isDateFiltered) Icons.Default.Close else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = if (isDateFiltered) colors.primary else appColors.textSecondary,
                            modifier = Modifier
                                .size(16.dp)
                                .clip(CircleShape)
                                .clickable {
                                    if (isDateFiltered) onClearDate() else onDateClick()
                                }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MeetingCard(
    item: MeetingWithDetail,
    isSupervisor: Boolean,
    onClick: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme
    val isPendingSync = item.meeting.syncStatus != SyncStatus.SYNCED
    val meetingType = remember(item.meeting.type) { MeetingType.fromValue(item.meeting.type) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(
            width = if (isPendingSync) 1.dp else 0.8.dp,
            color = if (isPendingSync) appColors.warning.copy(alpha = 0.7f) else appColors.border
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // ردیف اول: موضوع بازدید + نشان وضعیت
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.subjectVisitName,
                    style = typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = appColors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                StatusBadge(status = MeetingStatus.fromId(item.meeting.status))
            }

            // ردیف دوم: نام سازمان و نوع جلسه (در دو باکس کاربردی)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // نام سازمان
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Business,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = colors.primary
                    )
                    Text(
                        text = item.organizationName.ifBlank { "—" },
                        style = typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = appColors.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // چیپ نوع جلسه
                MeetingTypeChip(
                    text = stringResource(id = meetingType.titleRes),
                    icon = meetingType.icon
                )
            }
            if (isSupervisor && !item.visitorName.isNullOrBlank()) {
                val hasVisitor = !item.meeting.visitorName.isNullOrBlank() &&
                        item.meeting.visitorId != null

                if (hasVisitor) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                            tint = appColors.textSecondary
                        )
                        Text(
                            text = item.visitorName,
                            style = typography.labelMedium,
                            color = appColors.textSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            HorizontalDivider(
                thickness = 0.6.dp,
                color = appColors.border.copy(alpha = 0.5f)
            )

            // فوتر کارت: تاریخ و ساعت جلسه در یک ردیف منظم و خوانا
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // تاریخ شمسی
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = appColors.textSecondary
                    )
                    Text(
                        text = formatJalaliDate(item.meeting.visitDate ?: "—"),
                        style = typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        color = appColors.textPrimary
                    )
                }

                // ساعت جلسه در صورت وجود
                if (!item.meeting.visitTime.isNullOrBlank()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = appColors.textSecondary
                        )
                        Text(
                            text = item.meeting.visitTime!!,
                            style = typography.labelMedium,
                            color = appColors.textSecondary
                        )
                    }
                }
            }

            // بنر وضعیت سینک آفلاین در صورت نیاز
            if (isPendingSync) {
                Spacer(modifier = Modifier.height(4.dp))
                OfflinePendingFooterBanner()
            }
        }
    }
}

@Composable
fun MeetingTypeChip(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = colors.primaryContainer.copy(alpha = 0.35f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colors.primary,
                modifier = Modifier.size(13.dp)
            )
            Text(
                text = text,
                color = colors.primary,
                style = typography.labelSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}

private fun normalizeDate(date: String): String {
    val englishDigits = buildString {
        date.forEach { ch ->
            append(
                when (ch) {
                    '۰' -> '0'
                    '۱' -> '1'
                    '۲' -> '2'
                    '۳' -> '3'
                    '۴' -> '4'
                    '۵' -> '5'
                    '۶' -> '6'
                    '۷' -> '7'
                    '۸' -> '8'
                    '۹' -> '9'
                    else -> ch
                }
            )
        }
    }

    val parts = englishDigits.split("/")
    if (parts.size != 3) return englishDigits

    val year = parts[0].padStart(4, '0')
    val month = parts[1].padStart(2, '0')
    val day = parts[2].padStart(2, '0')
    return "$year/$month/$day"
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun MeetingsListScreenPreview() {
    AppScreenPreview {
        MeetingsListScreen(
            onMeetingClick = {},
            onAddMeetingClick = {}
        )
    }
}
