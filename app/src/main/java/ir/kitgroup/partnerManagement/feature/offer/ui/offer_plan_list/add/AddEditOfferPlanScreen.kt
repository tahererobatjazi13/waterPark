package ir.kitgroup.partnerManagement.feature.offer.ui.offer_plan_list.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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

@Composable
fun AddEditOfferScreen(
    onSuccessSave: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddEditOfferPlanViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowMessage -> {
                    snackBarHostState.showSnackbar(event.message.asString(context))
                }

                is UiEvent.ShowError -> {
                    snackBarHostState.showSnackbar(event.error.asString(context))
                }
            }
        }
    }

    AddEditOfferContent(
        uiState = uiState,
        snackBarHostState = snackBarHostState,
        onNameChange = viewModel::onNameChange,
        onFromDateSelected = viewModel::onFromDateChange,
        onToDateSelected = viewModel::onToDateChange,
        onStatusChange = viewModel::onStatusChange,
        onSubmit = { viewModel.submitOfferPlan(onSuccessSave) },
        modifier = modifier
    )
}

@Composable
fun AddEditOfferContent(
    uiState: AddEditOfferPlanUiState,
    snackBarHostState: SnackbarHostState,
    onNameChange: (String) -> Unit,
    onFromDateSelected: (year: Int, month: Int, day: Int) -> Unit,
    onToDateSelected: (year: Int, month: Int, day: Int) -> Unit,
    onStatusChange: (OfferPlanStatus) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showValidFromDatePicker by remember { mutableStateOf(false) }
    var showValidToDatePicker by remember { mutableStateOf(false) }

    val statusMap = remember {
        OfferPlanStatus.entries.associateBy { context.getString(it.titleRes) }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            if (uiState.isInitialLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(36.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
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
                            onValueChange = onNameChange,
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
                            placeholder = stringResource(R.string.hint_select_end_date),
                            showTime = false,
                            isRequired = true
                        )

                        DropdownSelectorField(
                            value = stringResource(uiState.status.titleRes),
                            label = stringResource(R.string.label_status),
                            placeholder = "",
                            items = statusMap.keys.toList(),
                            isRequired = false,
                            onItemSelected = { selectedTitle ->
                                statusMap[selectedTitle]?.let { onStatusChange(it) }
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    CustomButton(
                        text = if (uiState.isEditMode) {
                            stringResource(R.string.label_edit_plan)
                        } else {
                            stringResource(R.string.label_register_plan)
                        },
                        isLoading = uiState.isLoading,
                        enabled = !uiState.isLoading,
                        onClick = onSubmit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    )
                }
            }
        }
    }

    if (showValidFromDatePicker) {
        DatePickerDialog(
            onDismiss = { showValidFromDatePicker = false },
            onDateSelected = { date ->
                onFromDateSelected(date.year, date.month, date.day)
                showValidFromDatePicker = false
            }
        )
    }

    if (showValidToDatePicker) {
        DatePickerDialog(
            onDismiss = { showValidToDatePicker = false },
            onDateSelected = { date ->
                onToDateSelected(date.year, date.month, date.day)
                showValidToDatePicker = false
            }
        )
    }
}
