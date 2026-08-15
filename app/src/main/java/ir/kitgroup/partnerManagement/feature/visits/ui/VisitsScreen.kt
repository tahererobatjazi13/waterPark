package ir.kitgroup.partnerManagement.feature.visits.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.layout.ContentScale
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
import ir.kitgroup.partnerManagement.core.ui.theme.PartnerManagementTheme
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.feature.visits.model.VisitModel
import saman.zamani.persiandate.PersianDate
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import ir.kitgroup.partnerManagement.core.ui.components.DeleteConfirmationDialog
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.util.Status

@Composable
fun VisitsScreen(
    onVisitClick: (Int) -> Unit,
    onEditVisitClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    val allVisitorsLabel = stringResource(R.string.label_all_visitors)
    val pickDateLabel = stringResource(R.string.label_pick_date)

    var searchQuery by remember { mutableStateOf("") }
    var selectedVisitor by remember { mutableStateOf(allVisitorsLabel) }
    var selectedStartDate by rememberSaveable { mutableStateOf<String?>(null) }
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
        mutableStateOf(
            listOf(
                VisitModel(
                    id = 1,
                    title = "هتل قصر طلایی",
                    type = "بازدید حضوری برنامه‌ریزی شده",
                    person = "علی محمدی",
                    date = "۱۴۰۳/۰۲/۱۵ , 11:30",
                    icon = Icons.Default.DirectionsWalk,
                    status = Status.PLANNED
                ),
                VisitModel(
                    id = 2,
                    title = "هتل الماس",
                    type = "بازدید تلفنی",
                    person = "علی رضایی",
                    date = "۱۴۰۳/۰۲/۱۷ , 10:30",
                    icon = Icons.Default.Phone,
                    status = Status.DONE
                ),
                VisitModel(
                    id = 3,
                    title = "هتل پارسیان آزادی",
                    type = "بازدید حضوری غیربرنامه‌ریزی شده",
                    person = "مریم رضایی",
                    date = "۱۴۰۳/۰۲/۱۶ , 02:30",
                    icon = Icons.Default.DirectionsWalk,
                    status = Status.CANCELLED
                ),
                VisitModel(
                    id = 4,
                    title = "هتل پارسیان",
                    type = "بازدید تلفنی",
                    person = "مریم مفرد",
                    date = "۱۴۰۳/۰۲/۱۸ , 10:30",
                    icon = Icons.Default.Phone,
                    status = Status.PLANNED
                )
            )
        )
    }

    val visitorOptions = remember(items, allVisitorsLabel) {
        listOf(allVisitorsLabel) + items.map { it.person }.distinct()
    }

    val filteredItems = remember(
        items,
        searchQuery,
        selectedVisitor,
        selectedStartDate,
        todayDate,
        allVisitorsLabel
    ) {
        items.filter { item ->
            val matchesSearch = searchQuery.isBlank() ||
                    item.title.contains(searchQuery, ignoreCase = true) ||
                    item.person.contains(searchQuery, ignoreCase = true) ||
                    item.date.contains(searchQuery, ignoreCase = true)

            val matchesVisitor =
                selectedVisitor == allVisitorsLabel || item.person == selectedVisitor

            val matchesDateRange = selectedStartDate == null || run {
                val itemDate = normalizeDate(item.date)
                val startDate = normalizeDate(selectedStartDate!!)
                itemDate in startDate..todayDate
            }

            matchesSearch && matchesVisitor && matchesDateRange
        }
    }

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
                    onVisitorSelected = { selectedVisitor = it },
                    onDateClick = { showDatePicker = true },
                    onClearDate = { selectedStartDate = null },
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
                                style = MaterialTheme.typography.bodyMedium,
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
                                onClick = {
                                    onVisitClick(item.id)
                                },
                                onEditClick = {
                                    onEditVisitClick(item.id)
                                },
                                onDeleteClick = {
                                    visitPendingDelete = item
                                }
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
            itemName = visit.title,
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
    onVisitorSelected: (String) -> Unit,
    onDateClick: () -> Unit,
    onClearDate: () -> Unit,
    isDateFiltered: Boolean
) {
    val appColors = LocalPartnerManagementColors.current
    var visitorExpanded by remember { mutableStateOf(false) }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { },
                colors = ButtonDefaults.buttonColors(
                    containerColor = PartnerManagementTheme.colors.infoContainer,
                    contentColor = PartnerManagementTheme.colors.onInfoContainer
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                modifier = Modifier
                    .height(40.dp)
                    .wrapContentWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_filter),
                    contentDescription = null,
                    tint = PartnerManagementTheme.colors.info,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.label_filters),
                    color = PartnerManagementTheme.colors.onInfoContainer,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            ExposedDropdownMenuBox(
                expanded = visitorExpanded,
                onExpandedChange = { visitorExpanded = !visitorExpanded }
            ) {
                Surface(
                    modifier = Modifier
                        .height(40.dp),
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
                            style = MaterialTheme.typography.labelSmall,
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
                                    style = MaterialTheme.typography.labelMedium
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

            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                modifier = Modifier
                    .height(40.dp)
                    .clickable { onDateClick() },
                shape = RoundedCornerShape(8.dp),
                color = appColors.cardBackground,
                border = BorderStroke(1.dp, appColors.border)
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
                        style = MaterialTheme.typography.labelSmall,
                        color = appColors.textPrimary,
                        maxLines = 1
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
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
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
                    text = item.type,
                    icon = item.icon
                )

                Spacer(modifier = Modifier.height(6.dp))

                DetailRow(
                    icon = Icons.Default.Person,
                    text = item.person
                )

                Spacer(modifier = Modifier.height(6.dp))

                DetailRow(
                    icon = Icons.Default.DateRange,
                    text = item.date
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = 8.dp,
                        alignment = Alignment.End
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onEditClick,
                        modifier = Modifier.size(38.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.EditNote,
                            contentDescription = stringResource(R.string.label_edit_visit_item),
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.size(38.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = stringResource(R.string.label_delete_visit),
                            modifier = Modifier.size(21.dp)
                        )
                    }
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
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun VisitTypeChip(
    text: String,
    icon: ImageVector
) {
    val appColors = LocalPartnerManagementColors.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(horizontal = 6.dp, vertical = 4.dp),
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
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
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
            onEditVisitClick = {}
        )
    }
}
