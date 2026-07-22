package ir.kitgroup.hotel.feature.card.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
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
import saman.zamani.persiandate.PersianDate
import androidx.compose.ui.Alignment
import ir.kitgroup.hotel.core.ui.util.CollectionStatus
import ir.kitgroup.hotel.feature.collaborative_collection.model.CollectionModel
import ir.kitgroup.hotel.feature.collaborative_collection.ui.CollaborativeBottomSheet


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("DefaultLocale")
@Composable
fun CardRegistrationScreen(
    navController: NavController,
    onSubmitClick: () -> Unit = {}
) {

    var cardNumber by rememberSaveable { mutableStateOf("") }
    var showCollaborativeSheet by remember { mutableStateOf(false) }
    var selectedCollaborative by rememberSaveable { mutableStateOf<CollectionModel?>(null) }
    var recipientName by rememberSaveable { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    val today = PersianDate()
    var cardDate by rememberSaveable {
        mutableStateOf("${today.shYear}/${today.shMonth}/${today.shDay}")
    }
    var description by rememberSaveable { mutableStateOf("") }

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
            title = R.string.label_registration_delivery_card,
            showBackButton = true,
            onBackClick = { navController.popBackStack() }
        )
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            CardRegistrationContent(
                cardNumber = cardNumber,
                onCardNumberResultChange = { cardNumber = it },
                selectedCollaborative = selectedCollaborative,
                isCollaborativeExpanded = showCollaborativeSheet,
                onCollaborativeClick = { showCollaborativeSheet = true },
                recipientName = recipientName,
                onRecipientResultChange = { recipientName = it },
                cardDate = cardDate,
                onDateClick = { showDatePicker = true },
                description = description,
                onDescriptionChange = {
                    if (it.length <= 500) description = it
                },
                onSubmitClick = onSubmitClick
            )
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismiss = {
                showDatePicker = false
            },
            onDateSelected = { date ->
                cardDate =
                    "${date.year}/${date.month}/${date.day}"
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
private fun CardRegistrationContent(
    cardNumber: String,
    onCardNumberResultChange: (String) -> Unit,
    selectedCollaborative: CollectionModel?,
    isCollaborativeExpanded: Boolean,
    onCollaborativeClick: () -> Unit,
    recipientName: String,
    onRecipientResultChange: (String) -> Unit,
    cardDate: String,
    onDateClick: () -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    onSubmitClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomEditTextField(
            value = cardNumber,
            onValueChange = onCardNumberResultChange,
            label = stringResource(R.string.label_card_number),
            placeholder = stringResource(R.string.hint_enter_card_number),
            leadingIcon = null,
        )
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
            label = stringResource(R.string.label_recipient_name),
            placeholder = stringResource(R.string.hint_enter_recipient_name),
            leadingIcon = null,
        )

        VisitDateFields(
            visitDate = cardDate,
            onDateClick = onDateClick,
        )

        CustomDescriptionField(
            label = stringResource(R.string.label_description),
            value = description,
            onValueChange = onDescriptionChange,
            placeholder = stringResource(R.string.hint_description)
        )

        Button(
            onClick = onSubmitClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = stringResource(R.string.label_registration),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun VisitDateFields(
    visitDate: String,
    onDateClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.label_date_time),
            style = typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(colors.surface, RoundedCornerShape(12.dp))
                .border(1.dp, colors.outline, RoundedCornerShape(12.dp)),

            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onDateClick() }
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = colors.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = visitDate,
                    style = typography.bodyMedium,
                    color = colors.onSurface
                )
            }
        }
    }
}