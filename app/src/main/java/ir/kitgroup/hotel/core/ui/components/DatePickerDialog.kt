package ir.kitgroup.hotel.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ir.kitgroup.hotel.core.ui.theme.HotelTheme.colors
import saman.zamani.persiandate.PersianDate
import ir.kitgroup.hotel.core.ui.theme.LocalHotelColors

data class JalaliDate(
    val year: Int,
    val month: Int,
    val day: Int
)

@Composable
fun DatePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (JalaliDate) -> Unit
) {
    val colors = LocalHotelColors.current

    val today = PersianDate()

    var year by remember { mutableStateOf(today.shYear) }
    var month by remember { mutableStateOf(today.shMonth) }

    val days = getDaysInMonth(month)


    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        containerColor = colors.cardBackground,
        text = {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    TextButton(
                        onClick = {
                            month--
                            if (month < 1) {
                                month = 12
                                year--
                            }
                        }
                    ) {
                        Text("ماه قبل")
                    }

                    Text(
                        text = "$year / ${getMonthName(month)}",
                        style = MaterialTheme.typography.titleMedium,
                        color = colors.textPrimary
                    )

                    TextButton(
                        onClick = {
                            month++
                            if (month > 12) {
                                month = 1
                                year++
                            }
                        }
                    ) {
                        Text("ماه بعد")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier.height(250.dp)
                ) {

                    items(days) { day ->

                        val isToday =
                            year == today.shYear &&
                                    month == today.shMonth &&
                                    day == today.shDay

                        val backgroundColor =
                            if (isToday)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)

                        val textColor =
                            if (isToday)
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme.onSurface

                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(36.dp)
                                .background(
                                    color = backgroundColor,
                                    shape = CircleShape
                                )
                                .clickable {
                                    onDateSelected(
                                        JalaliDate(
                                            year = year,
                                            month = month,
                                            day = day
                                        )
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {

                            Text(
                                text = day.toString(),
                                color = colors.textSecondary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {

                        onDateSelected(
                            JalaliDate(
                                today.shYear,
                                today.shMonth,
                                today.shDay
                            )
                        )
                    }
                ) {
                    Text("امروز")
                }
            }
        }
    )
}

fun getDaysInMonth(month: Int): List<Int> {

    val days = when (month) {
        1, 2, 3, 4, 5, 6 -> 31
        7, 8, 9, 10, 11 -> 30
        12 -> 29
        else -> 30
    }

    return (1..days).toList()
}

fun getMonthName(month: Int): String {

    return when (month) {
        1 -> "فروردین"
        2 -> "اردیبهشت"
        3 -> "خرداد"
        4 -> "تیر"
        5 -> "مرداد"
        6 -> "شهریور"
        7 -> "مهر"
        8 -> "آبان"
        9 -> "آذر"
        10 -> "دی"
        11 -> "بهمن"
        12 -> "اسفند"
        else -> ""
    }
}

@Composable
fun DaysOfWeek() {

    val days = listOf(
        "ش",
        "ی",
        "د",
        "س",
        "چ",
        "پ",
        "ج"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        days.forEach {
            Text(
                text = it,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = colors.textSecondary
            )
        }
    }
}

@Composable
fun JalaliCalendar(
    onDateSelected: (JalaliDate) -> Unit
) {

    var year by remember { mutableIntStateOf(1403) }
    var month by remember { mutableIntStateOf(1) }
    val today = remember { PersianDate() }
    val days = getDaysInMonth(month)

    Column {

        CalendarHeader(
            year = year,
            month = month,
            onPrev = {
                if (month == 1) {
                    month = 12
                    year--
                } else {
                    month--
                }
            },
            onNext = {
                if (month == 12) {
                    month = 1
                    year++
                } else {
                    month++
                }
            }
        )

        Spacer(Modifier.height(12.dp))

        DaysOfWeek()

        Spacer(Modifier.height(8.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7)
        ) {

            items(days) { day ->

                val isToday =
                    year == today.shYear &&
                            month == today.shMonth &&
                            day == today.shDay

                val backgroundColor =
                    if (isToday)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)

                val textColor =
                    if (isToday)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurface

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(36.dp)
                        .background(
                            color = backgroundColor,
                            shape = CircleShape
                        )
                        .clickable {
                            onDateSelected(
                                JalaliDate(
                                    year = year,
                                    month = month,
                                    day = day
                                )
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = day.toString(),
                        color = textColor,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun CalendarHeader(
    year: Int,
    month: Int,
    onPrev: () -> Unit,
    onNext: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(onClick = onPrev) {
            Icon(Icons.Default.KeyboardArrowLeft, null)
        }

        Text(
            text = "${persianMonths[month - 1]} $year",
            style = MaterialTheme.typography.titleMedium
        )

        IconButton(onClick = onNext) {
            Icon(Icons.Default.KeyboardArrowRight, null)
        }
    }
}

@Composable
fun DayItem(
    day: Int,
    onClick: () -> Unit
) {

    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(40.dp)
            .clip(CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = day.toString(),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun JalaliRangeCalendar(
    onRangeSelected: (JalaliDate, JalaliDate) -> Unit
) {

    var startDate by remember { mutableStateOf<JalaliDate?>(null) }
    var endDate by remember { mutableStateOf<JalaliDate?>(null) }

    JalaliCalendar { selected ->

        if (startDate == null) {
            startDate = selected
        } else if (endDate == null) {
            endDate = selected
            onRangeSelected(startDate!!, endDate!!)
        } else {
            startDate = selected
            endDate = null
        }
    }
}

val persianMonths = listOf(
    "فروردین",
    "اردیبهشت",
    "خرداد",
    "تیر",
    "مرداد",
    "شهریور",
    "مهر",
    "آبان",
    "آذر",
    "دی",
    "بهمن",
    "اسفند"
)
