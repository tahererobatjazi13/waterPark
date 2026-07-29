package ir.kitgroup.partnerManagement.feature.visits.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.*
import ir.kitgroup.partnerManagement.core.ui.util.VisitType
import saman.zamani.persiandate.PersianDate
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.ui.tooling.preview.Preview
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.Status
import ir.kitgroup.partnerManagement.feature.collaborative_collection.model.CollectionModel
import ir.kitgroup.partnerManagement.feature.collaborative_collection.ui.CollaborativeBottomSheet
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterVisitScreen(
    visitType: VisitType,
    onBackClick: () -> Unit,
    onSubmitClick: () -> Unit = {}
) {
    var showCollaborativeSheet by remember { mutableStateOf(false) }
    var selectedCollaborative by rememberSaveable { mutableStateOf<CollectionModel?>(null) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val today = remember { PersianDate() }
    var visitDate by rememberSaveable {
        mutableStateOf(
            "${today.shYear}/${String.format(Locale.US, "%02d", today.shMonth)}/${
                String.format(
                    Locale.US,
                    "%02d",
                    today.shDay
                )
            }"
        )
    }

    val calendar = remember { Calendar.getInstance() }
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = calendar.get(Calendar.MINUTE)

    var visitTime by rememberSaveable {
        mutableStateOf(String.format(Locale.US, "%02d:%02d", currentHour, currentMinute))
    }
    val timePickerState = rememberTimePickerState(
        initialHour = currentHour,
        initialMinute = currentMinute,
        is24Hour = true
    )


    var visitResult by rememberSaveable { mutableStateOf("") }
    var recipientName by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    val titleRes = visitType.titleRes
    val appColors = LocalPartnerManagementColors.current

    val collaborativelist = listOf(

        CollectionModel(
            1,
            "هتل پارسیان آزادی", "مشهد،یوسفی", Status.ACTIVE,
            5
        ),
        CollectionModel(
            2,
            " مجموعه پالاس", "مشهد،قاسم آباد", Status.ACTIVE, 4
        ),
        CollectionModel(3, "سازمان نوید", "مشهد،پیروزی", Status.ACTIVE, 3),
        CollectionModel(4, "هتل مرکزی", "مشهد، امام رضا", Status.ACTIVE, 5),
        CollectionModel(5, "کیوسک اطلس", "مشهد، کوهسنگی", Status.ACTIVE, 3),
        CollectionModel(6, "هتل الماس", "مشهد، پاستور", Status.ACTIVE, 2),
        CollectionModel(7, "هتل وفا", "مشهد، وکیال آباد", Status.ACTIVE, 3),
        CollectionModel(8, "سازمان مهندسی", "مشهد، فاطمی", Status.ACTIVE, 4),
        CollectionModel(9, "هتل امیر", "مشهد، رضاییه", Status.ACTIVE, 2),
        CollectionModel(10, "آپارتمان ملل", "مشهد، ستاری", Status.ACTIVE, 3),
        CollectionModel(11, "مهمانسرا اسپیناس", "مشهد، مرکزی", Status.INACTIVE, 5),
        CollectionModel(12, "چالیدره", "مشهد، طرقبه", Status.INACTIVE, 2)
    )
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomHeader(
                title = titleRes,
                showBackButton = true,
                onBackClick = onBackClick
            )
        },
        containerColor = MaterialTheme.colorScheme.primary
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = appColors.screenBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
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

    if (showTimePicker) {
        TimePickerDialog(
            timePickerState = timePickerState,
            onConfirm = { hour, minute ->
                visitTime = String.format(Locale.US, "%02d:%02d", hour, minute)
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismiss = {
                showDatePicker = false
            },
            onDateSelected = { date ->
                visitDate = "${date.year}/${String.format(Locale.US, "%02d", date.month)}/${
                    String.format(Locale.US, "%02d", date.day)
                }"
                showDatePicker = false
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

        CustomDateTimeFields(
            label = stringResource(R.string.label_date_time),
            date = visitDate,
            onDateClick = onDateClick,
            time = visitTime,
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
        CustomButton(
            text = stringResource(R.string.label_submit_visit),
            onClick = onSubmitClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        )
    }
}


@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun RegisterVisitScreenPreview() {
        AppScreenPreview {
            RegisterVisitScreen(
                onBackClick = {},
                visitType = VisitType.PHONE,
                onSubmitClick = {},
            )
    }
}
