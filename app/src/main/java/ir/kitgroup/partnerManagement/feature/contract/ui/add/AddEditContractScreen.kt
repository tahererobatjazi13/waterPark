package ir.kitgroup.partnerManagement.feature.contract.ui.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomDateTimeFields
import ir.kitgroup.partnerManagement.core.ui.components.CustomEditTextField
import ir.kitgroup.partnerManagement.core.ui.components.CustomSelectorField
import ir.kitgroup.partnerManagement.core.ui.components.DatePickerDialog
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField
import ir.kitgroup.partnerManagement.core.ui.util.ContractStatus
import ir.kitgroup.partnerManagement.core.ui.util.CooperationModel
import ir.kitgroup.partnerManagement.core.ui.util.SettlementPeriodType
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.feature.organization.ui.OrganizationListBottomSheet

@Composable
fun AddEditContractScreen(
    onSuccessSave: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddEditContractViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowMessage -> snackBarHostState.showSnackbar(
                    event.message.asString(
                        context
                    )
                )

                is UiEvent.ShowError -> snackBarHostState.showSnackbar(event.error.asString(context))
            }
        }
    }

    AddContractContent(
        uiState = uiState,
        snackBarHostState = snackBarHostState,
        isPreselected = !viewModel.organizationIdArg.isNullOrBlank(),
        onOrganizationSelected = viewModel::onOrganizationSelected,
        onStartDateSelected = viewModel::onStartDateChange,
        onSettlementPeriodTypeChange = viewModel::onSettlementPeriodTypeChange,
        onCooperationModelChange = viewModel::onCooperationModelChange,
        onDefaultDiscountPercentChange = viewModel::onDefaultDiscountPercentChange,
        onDefaultCommissionPercentChange = viewModel::onDefaultCommissionPercentChange,
        onStatusChange = viewModel::onStatusChange,
        onSubmit = { viewModel.submitContract(onSuccessSave) },
        modifier = modifier
    )
}

@Composable
fun AddContractContent(
    uiState: AddEditContractUiState,
    snackBarHostState: SnackbarHostState,
    isPreselected: Boolean,
    onOrganizationSelected: (OrganizationWithDetail) -> Unit,
    onStartDateSelected: (year: Int, month: Int, day: Int) -> Unit,
    onSettlementPeriodTypeChange: (SettlementPeriodType) -> Unit,
    onCooperationModelChange: (CooperationModel) -> Unit,
    onDefaultDiscountPercentChange: (String) -> Unit,
    onDefaultCommissionPercentChange: (String) -> Unit,
    onStatusChange: (ContractStatus) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showOrganizationSheet by remember { mutableStateOf(false) }
    var showStartDatePicker by remember { mutableStateOf(false) }

    val cooperationModelMap = remember {
        CooperationModel.entries.associateBy { context.getString(it.titleRes) }
    }

    val settlementPeriodTypeMap = remember {
        SettlementPeriodType.entries.associateBy { context.getString(it.titleRes) }
    }

    val contractStatusMap = remember {
        ContractStatus.entries.associateBy { context.getString(it.titleRes) }
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
                        if (!isPreselected) {
                            CustomSelectorField(
                                value = uiState.organizationName,
                                label = stringResource(R.string.label_organization_name),
                                placeholder = stringResource(R.string.hint_choose_organization_name),
                                isExpanded = showOrganizationSheet,
                                isRequired = true,
                                onClick = { showOrganizationSheet = true }
                            )
                        }

                        DropdownSelectorField(
                            value = stringResource(uiState.cooperationModel.titleRes),
                            label = stringResource(R.string.label_cooperation_model),
                            placeholder = stringResource(R.string.hint_choose_cooperation_model),
                            items = cooperationModelMap.keys.toList(),
                            isRequired = true,
                            onItemSelected = { title ->
                                cooperationModelMap[title]?.let { onCooperationModelChange(it) }
                            }
                        )

                        DropdownSelectorField(
                            value = stringResource(uiState.settlementPeriodType.titleRes),
                            label = stringResource(R.string.label_settlement_period_type),
                            placeholder = stringResource(R.string.hint_choose_settlement_period_type),
                            items = settlementPeriodTypeMap.keys.toList(),
                            isRequired = true,
                            onItemSelected = { title ->
                                settlementPeriodTypeMap[title]?.let {
                                    onSettlementPeriodTypeChange(
                                        it
                                    )
                                }
                            }
                        )

                        CustomDateTimeFields(
                            label = stringResource(R.string.label_start_date),
                            date = uiState.startDate,
                            onDateClick = { showStartDatePicker = true },
                            showTime = false,
                            isRequired = true
                        )

                        DropdownSelectorField(
                            value = stringResource(uiState.status.titleRes),
                            label = stringResource(R.string.label_contract_Status),
                            placeholder = stringResource(R.string.hint_choose_status),
                            items = contractStatusMap.keys.toList(),
                            isRequired = true,
                            onItemSelected = { title ->
                                contractStatusMap[title]?.let { onStatusChange(it) }
                            }
                        )

                        CustomEditTextField(
                            value = uiState.defaultCommissionPercent,
                            onValueChange = onDefaultCommissionPercentChange,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            label = stringResource(R.string.label_default_commission_percent),
                            placeholder = stringResource(R.string.hint_enter_default_commission_percent),
                            isRequired = false,
                            leadingIcon = null
                        )

                        CustomEditTextField(
                            value = uiState.defaultDiscountPercent,
                            onValueChange = onDefaultDiscountPercentChange,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            label = stringResource(R.string.label_default_discount_percent),
                            placeholder = stringResource(R.string.hint_enter_default_discount_percent),
                            isRequired = false,
                            leadingIcon = null
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    CustomButton(
                        text = if (uiState.isEditMode) {
                            stringResource(R.string.label_edit_contract)
                        } else {
                            stringResource(R.string.label_create_contract)
                        },
                        isLoading = uiState.isLoading,
                        enabled = uiState.isFormValid,
                        onClick = onSubmit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    )
                }
            }
        }
    }

    if (showOrganizationSheet && !isPreselected) {
        OrganizationListBottomSheet(
            list = uiState.organizations,
            onDismiss = { showOrganizationSheet = false },
            onItemSelected = { item ->
                onOrganizationSelected(item)
                showOrganizationSheet = false
            }
        )
    }

    if (showStartDatePicker) {
        DatePickerDialog(
            onDismiss = { showStartDatePicker = false },
            onDateSelected = { date ->
                onStartDateSelected(date.year, date.month, date.day)
                showStartDatePicker = false
            }
        )
    }
}

