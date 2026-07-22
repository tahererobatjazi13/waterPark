package ir.kitgroup.hotel.feature.advertising.ui.visitor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAdvertisingVisitorAllocationScreen(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    // State ها برای مدیریت ورودی‌ها
    var visitorName by remember { mutableStateOf("") }
    var itemType by remember { mutableStateOf("") }
    var count by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            AllocationDetailTopBar(onBackClick)
        },
        containerColor = Color(0xFF00B0F0) // رنگ آبی هدر مطابق تصویر
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // ۱. انتخاب ویزیتور
                AllocationInputField(
                    label = "انتخاب ویزیتور",
                    placeholder = "لطفاً ویزیتور را انتخاب کنید",
                    value = visitorName,
                    onValueChange = { visitorName = it },
                    isSelection = true,
                    isRequired = true
                )

                // ۲. نوع استند
                AllocationInputField(
                    label = "نوع استند (رومیزی / دیواری / کیوسک)",
                    placeholder = "لطفاً نوع استند را انتخاب کنید",
                    value = itemType,
                    onValueChange = { itemType = it },
                    isSelection = true,
                    isRequired = true
                )

                // ۳. تعداد
                AllocationInputField(
                    label = "تعداد",
                    placeholder = "لطفاً تعداد را وارد کنید",
                    value = count,
                    onValueChange = { count = it },
                    leadingIcon = Icons.Default.Numbers,
                    keyboardType = KeyboardType.Number,
                    isRequired = true
                )

                // ۴. تاریخ تخصیص
                AllocationInputField(
                    label = "تاریخ تخصیص",
                    placeholder = "لطفاً تاریخ تخصیص را انتخاب کنید",
                    value = date,
                    onValueChange = { date = it },
                    leadingIcon = Icons.Default.CalendarMonth,
                    isSelection = true,
                    isRequired = true
                )

                // ۵. توضیحات
                AllocationInputField(
                    label = "توضیحات",
                    placeholder = "توضیحات را وارد کنید...",
                    value = description,
                    onValueChange = { description = it },
                    isSingleLine = false,
                    modifier = Modifier.height(120.dp),
                    counter = "${description.length} / 500"
                )

                Spacer(modifier = Modifier.height(10.dp))

                // ۶. دکمه ثبت تخصیص
                Button(
                    onClick = onSaveClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B5EDF))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Text(
                            text = "ثبت تخصیص",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AllocationDetailTopBar(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "ثبت تخصیص",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
        }
    }
}

@Composable
fun AllocationInputField(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    isRequired: Boolean = false,
    isSelection: Boolean = false,
    leadingIcon: ImageVector? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    isSingleLine: Boolean = true,
    modifier: Modifier = Modifier,
    counter: String? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // لیبل فیلد
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            if (isRequired) {
                Text("*", color = Color.Red, fontSize = 14.sp)
            }
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }

        // فیلد ورودی
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    placeholder,
                    fontSize = 13.sp,
                    color = Color.LightGray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            },
            leadingIcon = {
                if (isSelection) {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color.Gray)
                } else if (leadingIcon != null) {
                    Icon(leadingIcon, contentDescription = null, tint = Color.Gray)
                }
            },
            shape = RoundedCornerShape(12.dp),
            singleLine = isSingleLine,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedBorderColor = Color(0xFF00B0F0)
            ),
            readOnly = isSelection, // اگر انتخابگر است، تایپ نشود
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End)
        )

        // شمارنده کاراکتر (برای توضیحات)
        if (counter != null) {
            Text(
                text = counter,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                fontSize = 11.sp,
                color = Color.Gray,
                textAlign = TextAlign.Start
            )
        }
    }
}
