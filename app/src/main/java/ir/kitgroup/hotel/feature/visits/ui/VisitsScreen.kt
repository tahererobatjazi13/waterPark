package ir.kitgroup.hotel.feature.visits.ui

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ir.kitgroup.hotel.R
import ir.kitgroup.hotel.core.ui.components.CustomHeader
import ir.kitgroup.hotel.core.ui.components.DatePickerDialog
import ir.kitgroup.hotel.core.ui.components.FilterSection
import ir.kitgroup.hotel.core.ui.theme.HotelTheme
import ir.kitgroup.hotel.feature.visits.model.VisitModel
import ir.kitgroup.hotel.navigation.Screen
import saman.zamani.persiandate.PersianDate


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun VisitsScreen(navController: NavController) {
    val allVisitorsLabel = stringResource(R.string.label_all_visitors)
    val pickDateLabel = stringResource(R.string.label_pick_date)


    var searchQuery by remember { mutableStateOf("") }
    var selectedVisitor by remember { mutableStateOf(allVisitorsLabel) }
    var selectedStartDate by rememberSaveable { mutableStateOf<String?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }

    val today = remember { PersianDate() }
    val todayDate = remember(today) {
        normalizeDate(
            "${today.shYear}/${today.shMonth.toString().padStart(2, '0')}/${
                today.shDay.toString().padStart(2, '0')
            }"
        )
    }

    val items = remember {
        listOf(
            VisitModel(
                1,
                "هتل قصر طلایی",
                "بازدید حضوری",
                "علی محمدی",
                "۱۴۰۳/۰۲/۱۵",
                Icons.Default.DirectionsWalk
            ),
            VisitModel(
                2,
                "هتل پارسیان آزادی",
                "تماس تلفنی",
                "مریم رضایی",
                "۱۴۰۳/۰۲/۱۶",
                Icons.Default.Phone
            ),
            VisitModel(
                3,
                "هتل الماس",
                "بازدید حضوری",
                "علی رضایی",
                "۱۴۰۳/۰۲/۱۷",
                Icons.Default.DirectionsWalk
            ),
            VisitModel(
                4,
                "هتل پارسیان",
                "تماس تلفنی",
                "مریم مفرد",
                "۱۴۰۳/۰۲/۱۸",
                Icons.Default.Phone
            ),
        )
    }

    val visitorOptions = remember(items, allVisitorsLabel) {
        listOf(allVisitorsLabel) + items.map { it.person }.distinct()
    }


    val filteredItems = remember(items, searchQuery, selectedVisitor, selectedStartDate, todayDate, allVisitorsLabel) {
        items.filter { item ->
            val matchesSearch =
                searchQuery.isBlank() ||
                        item.title.contains(searchQuery, ignoreCase = true) ||
                        item.person.contains(searchQuery, ignoreCase = true) ||
                        item.date.contains(searchQuery, ignoreCase = true)

            val matchesVisitor =
                selectedVisitor == allVisitorsLabel || item.person == selectedVisitor

            val matchesDateRange =
                selectedStartDate == null || run {
                    val itemDate = normalizeDate(item.date)
                    val startDate = normalizeDate(selectedStartDate!!)
                    itemDate in startDate..todayDate
                }

            matchesSearch && matchesVisitor && matchesDateRange
        }
    }

    Column(
        modifier = Modifier
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
            color = MaterialTheme.colorScheme.background
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
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.SearchOff,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(40.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = stringResource(R.string.msg_no_item_found),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(vertical = 8.dp),
                    ) {
                        items(filteredItems, key = { it.id }) { item ->
                            VisitCard(
                                item = item,
                                onClick = {
                                    navController.navigate(Screen.VisitDetail.createRoute(item.id))
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
            onDismiss = {
                showDatePicker = false
            },
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
private fun VisitFiltersRow(
    selectedVisitor: String,
    visitorOptions: List<String>,
    selectedDate: String,
    onVisitorSelected: (String) -> Unit,
    onDateClick: () -> Unit,
    onClearDate: () -> Unit, // این پارامتر جدید است
    isDateFiltered: Boolean // برای تشخیص اینکه آیا تاریخ فیلتر شده است یا نه
) {

    var visitorExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { },
            colors = ButtonDefaults.buttonColors(
                containerColor = HotelTheme.colors.infoContainer,
                contentColor = HotelTheme.colors.onInfoContainer
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
                tint = HotelTheme.colors.info,
                modifier = Modifier.size(14.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = stringResource(R.string.label_filters),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
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
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.9f)
                )
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
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
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
        // در بخش Date Surface:
        Surface(
            modifier = Modifier
                .height(40.dp)
                .clickable { onDateClick() }, // باز شدن دیالوگ
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.9f))
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
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.width(4.dp))

                // اینجا منطق تغییر آیکون: اگر فیلتر فعال است ضربدر نشان بده
                Icon(
                    imageVector = if (isDateFiltered) Icons.Default.Close else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .size(16.dp)
                        .clickable {
                            if (isDateFiltered) onClearDate()
                        } // حذف فیلتر
                )
            }
        }
    }
    }


    @Composable
    fun VisitCard(
        item: VisitModel,
        onClick: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(
                width = 0.7.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.90f)
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
                        .size(90.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    VisitTypeChip(
                        text = item.type,
                        icon = item.icon
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    DetailRow(Icons.Default.Person, item.person)

                    Spacer(modifier = Modifier.height(6.dp))

                    DetailRow(Icons.Default.DateRange, item.date)
                }
            }
        }
    }

    @Composable
    fun DetailRow(icon: ImageVector, text: String) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = text,
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }

    @Composable
    fun VisitTypeChip(
        text: String,
        icon: ImageVector
    ) {
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
                color = MaterialTheme.colorScheme.onSurface,
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
