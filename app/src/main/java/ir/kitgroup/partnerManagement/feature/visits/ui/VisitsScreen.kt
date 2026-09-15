package ir.kitgroup.partnerManagement.feature.visits.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.DatePickerDialog
import ir.kitgroup.partnerManagement.core.ui.components.FilterSection
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.feature.visits.model.VisitModel
import saman.zamani.persiandate.PersianDate
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition

import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import ir.kitgroup.partnerManagement.core.ui.components.ActionIconButton
import ir.kitgroup.partnerManagement.core.ui.components.DeleteConfirmationDialog
import ir.kitgroup.partnerManagement.core.ui.components.LocationRow
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.util.Status
import ir.kitgroup.partnerManagement.core.ui.util.extensions.toDisplayName

@Composable
fun VisitsScreen(
    onVisitClick: (Int) -> Unit,
    onAddVisitClick: () -> Unit,
    onEditVisitClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    val allVisitorsLabel = stringResource(R.string.label_all_visitors)
    val pickDateLabel = stringResource(R.string.label_pick_date)

    var searchQuery by remember { mutableStateOf("") }
    var selectedVisitor by remember { mutableStateOf(allVisitorsLabel) }
    var selectedStartDate by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedStatus by rememberSaveable { mutableStateOf<Status?>(null) } // <-- استیت وضعیت
    var showDatePicker by remember { mutableStateOf(false) }
    var visitPendingDelete by remember { mutableStateOf<VisitModel?>(null) }

    val today = remember { PersianDate() }
    val todayDate = remember(today) {
        normalizeDate(
            "${today.shYear}/${today.shMonth.toString().padStart(2, '0')}/${
                today.shDay.toString().padStart(2, '0')
            }"
        )
    }

    var items by remember {
        mutableStateOf(demoVisitItems)
    }

    val visitorOptions = remember(items, allVisitorsLabel) {
        listOf(allVisitorsLabel) + items.map { it.visitorName }.distinct()
    }

    // لیست وضعیت‌های قابل انتخاب
    val statusOptions = remember {
        listOf(Status.PLANNED, Status.DONE, Status.CANCELLED)
    }

    // اعمال فیلتر وضعیت در جستجو
    val filteredItems = remember(
        items,
        searchQuery,
        selectedVisitor,
        selectedStartDate,
        selectedStatus,
        todayDate,
        allVisitorsLabel
    ) {
        items.filter { item ->
            val matchesSearch = searchQuery.isBlank() ||
                    item.organizationName.contains(searchQuery, ignoreCase = true) ||
                    item.visitorName.contains(searchQuery, ignoreCase = true) ||
                    item.date.contains(searchQuery, ignoreCase = true)

            val matchesVisitor =
                selectedVisitor == allVisitorsLabel || item.visitorName == selectedVisitor

            val matchesStatus = selectedStatus == null || item.status == selectedStatus

            val matchesDateRange = selectedStartDate == null || run {
                val itemDate = normalizeDate(item.date)
                val startDate = normalizeDate(selectedStartDate!!)
                itemDate in startDate..todayDate
            }

            matchesSearch && matchesVisitor && matchesStatus && matchesDateRange
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddVisitClick,
                containerColor = LocalPartnerManagementColors.current.success,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
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
                        style = typography.titleLarge
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            CustomHeader(
                title = R.string.label_visits_list,
                showBackButton = false
            )

            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = appColors.screenBackground
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 16.dp, end = 16.dp, top = 12.dp)
                ) {
                    FilterSection(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        hint = stringResource(R.string.label_search)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    VisitFiltersRow(
                        selectedVisitor = selectedVisitor,
                        visitorOptions = visitorOptions,
                        selectedDate = selectedStartDate ?: pickDateLabel,
                        selectedStatus = selectedStatus,
                        statusOptions = statusOptions,
                        onVisitorSelected = { selectedVisitor = it },
                        onDateClick = { showDatePicker = true },
                        onClearDate = { selectedStartDate = null },
                        onStatusSelected = { selectedStatus = it },
                        onClearStatus = { selectedStatus = null },
                        isDateFiltered = selectedStartDate != null
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (filteredItems.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.SearchOff,
                                    contentDescription = null,
                                    tint = appColors.textSecondary,
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = stringResource(R.string.msg_no_item_found),
                                    style = typography.bodyMedium,
                                    color = appColors.textSecondary
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(vertical = 8.dp),
                        ) {
                            items(
                                items = filteredItems,
                                key = { it.id }
                            ) { item ->
                                VisitCard(
                                    item = item,
                                    onClick = { onVisitClick(item.id) },
                                    onEditClick = { onEditVisitClick(item.id) },
                                    onDeleteClick = { visitPendingDelete = item }
                                )
                            }
                            item {
                                Spacer(modifier = Modifier.height(80.dp))
                            }
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

    visitPendingDelete?.let { visit ->
        DeleteConfirmationDialog(
            itemType = stringResource(R.string.label_visit),
            itemName = visit.organizationName,
            onConfirm = {
                items = items.filter { it.id != visit.id }
                visitPendingDelete = null
            },
            onDismiss = {
                visitPendingDelete = null
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VisitFiltersRow(
    selectedVisitor: String,
    visitorOptions: List<String>,
    selectedDate: String,
    selectedStatus: Status?,
    statusOptions: List<Status>,
    onVisitorSelected: (String) -> Unit,
    onDateClick: () -> Unit,
    onClearDate: () -> Unit,
    onStatusSelected: (Status) -> Unit,
    onClearStatus: () -> Unit,
    isDateFiltered: Boolean
) {
    val appColors = LocalPartnerManagementColors.current
    var visitorExpanded by remember { mutableStateOf(false) }
    var statusExpanded by remember { mutableStateOf(false) }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // برچسب فیلترها
            item {
                Row(
                    modifier = Modifier
                        .height(40.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_filter),
                        contentDescription = null,
                        tint = appColors.textSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(R.string.label_filters),
                        color = appColors.textSecondary,
                        style = typography.titleSmall
                    )
                }
            }

            // فیلتر وضعیت
            item {
                ExposedDropdownMenuBox(
                    expanded = statusExpanded,
                    onExpandedChange = { statusExpanded = !statusExpanded }
                ) {
                    Surface(
                        modifier = Modifier
                            .height(40.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = appColors.cardBackground,
                        border = BorderStroke(
                            1.dp,
                            if (selectedStatus != null) MaterialTheme.colorScheme.primary else appColors.border
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.FilterAlt,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = selectedStatus?.toDisplayName() ?: "وضعیت",
                                style = typography.labelSmall,
                                color = if (selectedStatus != null) MaterialTheme.colorScheme.primary else appColors.textPrimary,
                                maxLines = 1,
                                modifier = Modifier.clickable { statusExpanded = true }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = if (selectedStatus != null) Icons.Default.Close else Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = appColors.textSecondary,
                                modifier = Modifier
                                    .size(16.dp)
                                    .clickable {
                                        if (selectedStatus != null) {
                                            onClearStatus()
                                        } else {
                                            statusExpanded = !statusExpanded
                                        }
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
                                        text = status.toDisplayName(),
                                        style = typography.labelMedium
                                    )
                                },
                                onClick = {
                                    onStatusSelected(status)
                                    statusExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // فیلتر تاریخ
            item {
                Surface(
                    modifier = Modifier
                        .height(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = appColors.cardBackground,
                    border = BorderStroke(
                        1.dp,
                        if (isDateFiltered) MaterialTheme.colorScheme.primary else appColors.border
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = selectedDate,
                            style = typography.labelSmall,
                            color = if (isDateFiltered) MaterialTheme.colorScheme.primary else appColors.textPrimary,
                            maxLines = 1,
                            modifier = Modifier.clickable { onDateClick() }
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = if (isDateFiltered) Icons.Default.Close else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = appColors.textSecondary,
                            modifier = Modifier
                                .size(16.dp)
                                .clickable {
                                    if (isDateFiltered) {
                                        onClearDate()
                                    } else {
                                        onDateClick()
                                    }
                                }
                        )
                    }
                }
            }
            /*
                        // فیلتر بازاریاب
                        item {
                            ExposedDropdownMenuBox(
                                expanded = visitorExpanded,
                                onExpandedChange = { visitorExpanded = !visitorExpanded }
                            ) {
                                Surface(
                                    modifier = Modifier
                                        .height(40.dp)
                                        .menuAnchor(),
                                    shape = RoundedCornerShape(8.dp),
                                    color = appColors.cardBackground,
                                    border = BorderStroke(1.dp, appColors.border)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .clickable { visitorExpanded = true }
                                            .padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = selectedVisitor,
                                            style = typography.labelSmall,
                                            color = appColors.textPrimary,
                                            maxLines = 1
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowDown,
                                            contentDescription = null,
                                            tint = appColors.textSecondary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                ExposedDropdownMenu(
                                    expanded = visitorExpanded,
                                    onDismissRequest = { visitorExpanded = false }
                                ) {
                                    visitorOptions.forEach { visitor ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = visitor,
                                                    style = typography.labelMedium
                                                )
                                            },
                                            onClick = {
                                                onVisitorSelected(visitor)
                                                visitorExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }*/
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

                DetailRow(
                    icon = Icons.Default.Person,
                    text = item.visitorName
                )

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
}

@Composable
fun DetailRow(icon: ImageVector, text: String) {
    val appColors = LocalPartnerManagementColors.current
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = appColors.textSecondary
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            textAlign = TextAlign.End,
            color = appColors.textSecondary,
            style = typography.labelMedium
        )
    }
}
@Composable
fun VisitTypeChip(
    text: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(14.dp)
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = text,
            color = appColors.textPrimary,
            style = typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
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
private fun VisitScreenPreview() {
    AppScreenPreview {
        VisitsScreen(
            onVisitClick = {},
            onAddVisitClick = {},
            onEditVisitClick = {}
        )
    }
}
