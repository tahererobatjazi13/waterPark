package ir.kitgroup.partnerManagement.feature.offer.ui.offer_list.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.*
import ir.kitgroup.partnerManagement.core.ui.util.OfferPlanStatus
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate
import saman.zamani.persiandate.PersianDate
import java.util.Locale

@Composable
fun AddEditOfferScreen(
    offerId: String? = null,
    onSuccessSave: () -> Unit,
    viewModel: AddEditOfferViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    var showValidFromDatePicker by remember { mutableStateOf(false) }
    var showValidToDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(offerId) {
        if (!offerId.isNullOrBlank()) {
            viewModel.loadOffer(offerId)
        } else {
            val today = PersianDate()
            val defaultDate =
                "${today.shYear}/${String.format(Locale.US, "%02d", today.shMonth)}/" +
                        String.format(Locale.US, "%02d", today.shDay)

            viewModel.onFromDateChange(defaultDate)
            viewModel.onToDateChange(defaultDate)
        }
    }

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowMessage -> {
                    snackbarHostState.showSnackbar(event.message.asString(context))
                    onSuccessSave()
                }

                is UiEvent.ShowError -> {
                    snackbarHostState.showSnackbar(event.error.asString(context))
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CustomEditTextField(
                        value = uiState.offerName,
                        onValueChange = viewModel::onNameChange,
                        label = stringResource(R.string.label_offer_name),
                        placeholder = stringResource(R.string.hint_enter_offer_name),
                        leadingIcon = null,
                        isRequired = true
                    )

                    CustomDateTimeFields(
                        label = stringResource(R.string.label_valid_from_date),
                        date = if (uiState.fromDate.isNotBlank()) formatJalaliDate(uiState.fromDate) else "",
                        onDateClick = { showValidFromDatePicker = true },
                        showTime = false,
                        isRequired = true
                    )

                    CustomDateTimeFields(
                        label = stringResource(R.string.label_valid_to_date),
                        date = if (uiState.toDate.isNotBlank()) formatJalaliDate(uiState.toDate) else "",
                        onDateClick = { showValidToDatePicker = true },
                        showTime = false,
                        isRequired = true
                    )

                    val statusMap = remember {
                        OfferPlanStatus.entries.associateBy { context.getString(it.titleRes) }
                    }

                    DropdownSelectorField(
                        value = stringResource(uiState.status.titleRes),
                        label = stringResource(R.string.label_status),
                        placeholder = "",
                        items = statusMap.keys.toList(),
                        isRequired = true,
                        onItemSelected = { selectedTitle ->
                            statusMap[selectedTitle]?.let { selectedStatus ->
                                viewModel.onStatusChange(selectedStatus)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                CustomButton(
                    text = if (uiState.isEditMode) "ویرایش طرح" else stringResource(R.string.label_registration),
                    onClick = {
                        // متد submitPlan بدون پاس دادن callback هدایت می‌شود؛ خروج بعد از ShowMessage مدیریت شده است
                        viewModel.submitPlan(onSuccess = {})
                    },
                    enabled = uiState.offerName.isNotBlank() && !uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )
            }
        }
    }

    if (showValidFromDatePicker) {
        DatePickerDialog(
            onDismiss = { showValidFromDatePicker = false },
            onDateSelected = { date ->
                viewModel.onFromDateChange(
                    "${date.year}/${
                        String.format(
                            Locale.US,
                            "%02d",
                            date.month
                        )
                    }/${String.format(Locale.US, "%02d", date.day)}"
                )
                showValidFromDatePicker = false
            }
        )
    }

    if (showValidToDatePicker) {
        DatePickerDialog(
            onDismiss = { showValidToDatePicker = false },
            onDateSelected = { date ->
                viewModel.onToDateChange(
                    "${date.year}/${
                        String.format(
                            Locale.US,
                            "%02d",
                            date.month
                        )
                    }/${String.format(Locale.US, "%02d", date.day)}"
                )
                showValidToDatePicker = false
            }
        )
    }
}
