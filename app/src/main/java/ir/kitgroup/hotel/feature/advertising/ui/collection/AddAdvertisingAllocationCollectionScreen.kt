package ir.kitgroup.hotel.feature.advertising.ui.collection

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.ViewInAr
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAdvertisingAllocationCollectionScreen(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    var selectedHotel by remember { mutableStateOf("هتل پارسیان آزادی تهران") }
    var selectedDate by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val items = remember {
        mutableStateListOf(
            AdvertisingHotelItemUi(
                title = "استند رومیزی",
                available = 15,
                delivered = 0,
                selected = false,
                icon = Icons.Filled.ViewInAr
            ),
            AdvertisingHotelItemUi(
                title = "بنر",
                available = 8,
                delivered = 0,
                selected = false,
                icon = Icons.Filled.Storefront
            ),
            AdvertisingHotelItemUi(
                title = "کیوسک",
                available = 3,
                delivered = 0,
                selected = false,
                icon = Icons.Filled.LocationCity
            )
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            AdvertisingCollectionTopBar(
                title = "تخصیص آیتم‌های تبلیغاتی",
                onBackClick = onBackClick
            )
        },
        containerColor = Color(0xFFF4F7FB)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
                .padding(bottom = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(18.dp))

            SectionTitle(
                title = "انتخاب هتل",
                icon = Icons.Filled.LocationCity
            )

            Spacer(modifier = Modifier.height(10.dp))

            DropdownLikeField(
                value = selectedHotel,
                placeholder = "هتل پارسیان آزادی تهران",
                onClick = {
                    // TODO open hotel picker
                }
            )

            Spacer(modifier = Modifier.height(22.dp))

            SectionTitle(
                title = "آیتم‌های تبلیغاتی",
                icon = Icons.Filled.Checklist
            )

            Spacer(modifier = Modifier.height(12.dp))

            items.forEachIndexed { index, item ->
                AdvertisingHotelItemCard(
                    item = item,
                    onToggleSelected = {
                        items[index] = item.copy(selected = !item.selected)
                    },
                    onIncrease = {
                        if (item.delivered < item.available) {
                            items[index] = item.copy(delivered = item.delivered + 1, selected = true)
                        }
                    },
                    onDecrease = {
                        if (item.delivered > 0) {
                            val newValue = item.delivered - 1
                            items[index] = item.copy(
                                delivered = newValue,
                                selected = newValue > 0
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(14.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "تاریخ تحویل *",
                color = Color(0xFF2D2D2D),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )

            Spacer(modifier = Modifier.height(10.dp))

            DateField(
                value = selectedDate,
                placeholder = "لطفاً تاریخ تحویل را انتخاب کنید",
                onClick = {
                    // TODO show date picker
                }
            )

            Spacer(modifier = Modifier.height(22.dp))

            SectionTitle(
                title = "توضیحات",
                icon = Icons.Filled.Message
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { if (it.length <= 500) description = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                placeholder = {
                    Text(
                        text = "توضیحات خود را وارد کنید...",
                        color = Color(0xFF9E9E9E)
                    )
                },
                maxLines = 5,
                shape = RoundedCornerShape(16.dp),
//                colors = TextFieldDefaults.outlinedTextFieldColors(
//                    focusedBorderColor = Color(0xFFE0E0E0),
//                    unfocusedBorderColor = Color(0xFFE0E0E0),
//                    focusedTextColor = Color(0xFF212121),
//                    unfocusedTextColor = Color(0xFF212121),
//                    containerColor = Color.White
//                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "${description.length}/500",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                color = Color(0xFF9E9E9E),
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            SaveButton(
                text = "ثبت و تحویل",
                onClick = onSaveClick
            )
        }
    }
}

@Composable
private fun AdvertisingCollectionTopBar(
    title: String,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF16A9E1))
            .padding(top = 18.dp, bottom = 24.dp)
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SectionTitle(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = Color(0xFF2B2B2B),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.width(6.dp))

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF2E8B57),
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
private fun DropdownLikeField(
    value: String,
    placeholder: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
            .background(Color.White, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowDown,
            contentDescription = null,
            tint = Color(0xFF7A7A7A)
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = value.ifBlank { placeholder },
            color = if (value.isBlank()) Color(0xFF9E9E9E) else Color(0xFF2B2B2B),
            fontSize = 15.sp,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun DateField(
    value: String,
    placeholder: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp)
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
            .background(Color.White, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.CalendarMonth,
            contentDescription = null,
            tint = Color(0xFF7A7A7A)
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = value.ifBlank { placeholder },
            color = if (value.isBlank()) Color(0xFF9E9E9E) else Color(0xFF2B2B2B),
            fontSize = 15.sp,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun AdvertisingHotelItemCard(
    item: AdvertisingHotelItemUi,
    onToggleSelected: () -> Unit,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFF0F0F0), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = item.selected,
            onCheckedChange = { onToggleSelected() },
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF2E8B57),
                uncheckedColor = Color(0xFFBDBDBD)
            )
        )

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier
                .size(54.dp)
                .background(Color(0xFFF2F6FF), RoundedCornerShape(27.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = Color(0xFF4A79D8),
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = item.title,
                color = Color(0xFF222222),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "موجودی",
                    color = Color(0xFF8A8A8A),
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "${item.available}",
                    color = Color(0xFF2E8B57),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "تعداد تحویلی",
                color = Color(0xFF555555),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                CounterButton(
                    text = "+",
                    onClick = onIncrease
                )

                CounterValue(value = item.delivered)

                CounterButton(
                    text = "−",
                    onClick = onDecrease
                )
            }
        }
    }
}

@Composable
private fun CounterButton(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(width = 36.dp, height = 36.dp)
            .border(1.dp, Color(0xFFB7D4B9), RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color(0xFF2E8B57),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CounterValue(value: Int) {
    Box(
        modifier = Modifier
            .width(48.dp)
            .height(36.dp)
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = value.toString(),
            color = Color(0xFF4A4A4A),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SaveButton(
    text: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0xFF2E8B57), RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Checklist,
            contentDescription = null,
            tint = Color.White
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private data class AdvertisingHotelItemUi(
    val title: String,
    val available: Int,
    val delivered: Int,
    val selected: Boolean,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun AddAdvertisingAllocationCollectionScreenPreview() {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        MaterialTheme {
            AddAdvertisingAllocationCollectionScreen(
                onBackClick = {},
                onSaveClick = {}
            )
        }
    }
}
