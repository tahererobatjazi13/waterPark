package ir.kitgroup.hotel.feature.visits.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ir.kitgroup.hotel.R
import ir.kitgroup.hotel.core.ui.components.*
import ir.kitgroup.hotel.core.ui.util.VisitType
import saman.zamani.persiandate.PersianDate
import androidx.compose.material3.rememberTimePickerState
import ir.kitgroup.hotel.core.ui.util.CollectionStatus
import ir.kitgroup.hotel.feature.collaborative_collection.model.CollectionModel
import ir.kitgroup.hotel.feature.collaborative_collection.ui.CollaborativeBottomSheet
import java.time.LocalTime


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("DefaultLocale")
@Composable
fun RegisterVisitScreen(
    navController: NavController,
    visitType: VisitType,
    onSubmitClick: () -> Unit = {}
) {
    var showCollaborativeSheet by remember { mutableStateOf(false) }
    var selectedCollaborative by rememberSaveable { mutableStateOf<CollectionModel?>(null) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val today = PersianDate()

    var visitDate by rememberSaveable {
        mutableStateOf("${today.shYear}/${today.shMonth}/${today.shDay}")
    }
    val now = remember { LocalTime.now() }

    var visitTime by rememberSaveable {
        mutableStateOf(
            String.format("%02d:%02d", now.hour, now.minute)
        )
    }


    var visitResult by rememberSaveable { mutableStateOf("") }
    var recipientName by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    val titleRes = visitType.titleRes

    val collaborativelist = listOf(

        CollectionModel(
            1,
            "هتل پارسیان آزادی", "مشهد،یوسفی", CollectionStatus.ACTIVE,
            5
        ),
        CollectionModel(
            2,
            " مجموعه پالاس", "مشهد،قاسم آباد", CollectionStatus.ACTIVE, 4
        ),
        CollectionModel(3, "سازمان نوید", "مشهد،پیروزی", CollectionStatus.ACTIVE, 3),
        CollectionModel(4, "هتل مرکزی", "مشهد، امام رضا", CollectionStatus.ACTIVE, 5),
        CollectionModel(5, "کیوسک اطلس", "مشهد، کوهسنگی", CollectionStatus.ACTIVE, 3),
        CollectionModel(6, "هتل الماس", "مشهد، پاستور", CollectionStatus.ACTIVE, 2),
        CollectionModel(7, "هتل وفا", "مشهد، وکیال آباد", CollectionStatus.ACTIVE, 3),
        CollectionModel(8, "سازمان مهندسی", "مشهد، فاطمی", CollectionStatus.ACTIVE, 4),
        CollectionModel(9, "هتل امیر", "مشهد، رضاییه", CollectionStatus.ACTIVE, 2),
        CollectionModel(10, "آپارتمان ملل", "مشهد، ستاری", CollectionStatus.ACTIVE, 3),
        CollectionModel(11, "مهمانسرا اسپیناس", "مشهد، مرکزی", CollectionStatus.INACTIVE, 5),
        CollectionModel(12, "چالیدره", "مشهد، طرقبه", CollectionStatus.INACTIVE, 2)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        CustomHeader(
            title = titleRes,
            showBackButton = true,
            onBackClick = { navController.popBackStack() }
        )
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                RegisterVisitContent(
                    visitType = visitType,
                    selectedCollaborative = selectedCollaborative,
                    isCollaborativeExpanded = showCollaborativeSheet,

                    onCollaborativeClick = { showCollaborativeSheet = true },
                    visitDate = visitDate,
                    visitTime = visitTime,
                    onDateClick = { showDatePicker = true },
                    onTimeClick = { showTimePicker = true },
                    recipientName = recipientName,
                    onRecipientResultChange = { recipientName = it },
                    visitResult = visitResult,
                    onVisitResultChange = { visitResult = it },
                    description = description,
                    onDescriptionChange = {
                        if (it.length <= 500) description = it
                    },
                    onSubmitClick = onSubmitClick
                )

            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismiss = {
                showDatePicker = false
            },
            onDateSelected = { date ->
                visitDate =
                    "${date.year}/${date.month}/${date.day}"

                showDatePicker = false
            }
        )
    }
    val timePickerState = rememberTimePickerState(
        initialHour = now.hour,
        initialMinute = now.minute,
        is24Hour = true
    )
    if (showTimePicker) {

        TimePickerDialog(
            timePickerState = timePickerState,
            onConfirm = { hour, minute ->

                visitTime = String.format("%02d:%02d", hour, minute)

                showTimePicker = false
            },
            onDismiss = {
                showTimePicker = false
            }
        )
    }
    if (showCollaborativeSheet) {

        CollaborativeBottomSheet(
            list = collaborativelist,
            onDismiss = { showCollaborativeSheet = false },
            onItemSelected = { item ->
                selectedCollaborative = item
                showCollaborativeSheet = false
            }
        )
    }
}

@Composable
private fun RegisterVisitContent(
    visitType: VisitType,
    selectedCollaborative: CollectionModel?,
    isCollaborativeExpanded: Boolean,
    onCollaborativeClick: () -> Unit,
    visitDate: String,
    visitTime: String,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit,
    recipientName: String,
    onRecipientResultChange: (String) -> Unit,
    visitResult: String,
    onVisitResultChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    onSubmitClick: () -> Unit
) {
    val resultTitleRes = visitType.resultTitleRes
    val resultHintRes = visitType.resultHintRes

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        CustomSelectorField(
            value = selectedCollaborative?.name ?: "",
            label = stringResource(R.string.label_name_collaborative),
            placeholder = stringResource(R.string.hint_choose_collaborative),
            isExpanded = isCollaborativeExpanded,
            onClick = onCollaborativeClick
        )

        CustomEditTextField(
            value = recipientName,
            onValueChange = onRecipientResultChange,
            label = stringResource(R.string.label_name_recipient),
            placeholder = stringResource(R.string.hint_enter_name_recipient),
            leadingIcon = null,
        )

        VisitDateTimeFields(
            visitDate = visitDate,
            onDateClick = onDateClick,
            visitTime = visitTime,
            onTimeClick = onTimeClick
        )

        CustomEditTextField(
            value = visitResult,
            onValueChange = onVisitResultChange,
            label = stringResource(resultTitleRes),
            placeholder = stringResource(resultHintRes),
            leadingIcon = null,
        )

        CustomDescriptionField(
            label = stringResource(R.string.label_description),
            value = description,
            onValueChange = onDescriptionChange,
            placeholder = stringResource(R.string.hint_description)
        )

        if (visitType.showFields) {
            ImageUploadField(
                label = stringResource(R.string.label_visit_image)
            )

            GpsLocationField(
                label = stringResource(R.string.label_register_gps)
            )
        }

        Button(
            onClick = onSubmitClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = stringResource(R.string.label_submit_visit),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

