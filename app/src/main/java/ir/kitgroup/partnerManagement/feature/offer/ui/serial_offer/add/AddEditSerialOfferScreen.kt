package ir.kitgroup.partnerManagement.feature.offer.ui.serial_offer.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import ir.kitgroup.partnerManagement.core.ui.components.*
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.ContractOfferStatus
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent

@Composable
fun AddEditSerialOfferScreen(
    organizationId: String? = null,
    meetingId: String? = null,
    onSaveSuccess: () -> Unit = {},
    viewModel: AddEditSerialOfferViewModel = hiltViewModel()
) {
    val appColors = LocalPartnerManagementColors.current
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var isOfferExpanded by remember { mutableStateOf(false) }
    var isContractExpanded by remember { mutableStateOf(false) }

    val statusMap = remember {
        ContractOfferStatus.entries.associateWith { context.getString(it.titleRes) }
    }

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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding()
            ) {
                CustomButton(
                    text = stringResource(
                        if (uiState.isEditMode) R.string.label_update_serial_offer else R.string.label_assign_serial_offer
                    ),
                    isLoading = uiState.isLoading,
                    enabled = !uiState.isLoading && !uiState.isInitialLoading,
                    onClick = {
                        viewModel.submitSerialOffer(
                            organizationId = organizationId,
                            meetingId = meetingId,
                            onSuccessCallback = onSaveSuccess
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                )
            }
        }
    ) { innerPadding ->
        if (uiState.isInitialLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                BouncingDotsIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // انتخاب طرح آفر (Offer Plan)
                Box(modifier = Modifier.fillMaxWidth()) {
                    CustomSelectorField(
                        value = uiState.selectedOfferPlanTitle,
                        label = stringResource(R.string.label_offer_plan),
                        placeholder = stringResource(R.string.hint_choose_offer_plan),
                        isRequired = true,
                        isExpanded = isOfferExpanded,
                        onClick = { isOfferExpanded = !isOfferExpanded }
                    )

                    DropdownMenu(
                        expanded = isOfferExpanded,
                        onDismissRequest = { isOfferExpanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(appColors.cardBackground)
                    ) {
                        uiState.offerPlans.forEachIndexed { index, item ->
                            val bg =
                                if (index % 2 == 0) appColors.cardBackground else appColors.cardBackgroundAlt
                            Surface(modifier = Modifier.fillMaxWidth(), color = bg) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = item.title.orEmpty(),
                                            color = appColors.textPrimary
                                        )
                                    },
                                    onClick = {
                                        viewModel.onSelectOfferPlan(item)
                                        isOfferExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // انتخاب قرارداد (Contract)
                Box(modifier = Modifier.fillMaxWidth()) {
                    CustomSelectorField(
                        value = uiState.selectedContractTitle,
                        label = stringResource(R.string.label_contract_item),
                        placeholder = stringResource(R.string.hint_choose_contract),
                        isRequired = true,
                        isExpanded = isContractExpanded,
                        onClick = { isContractExpanded = !isContractExpanded }
                    )

                    DropdownMenu(
                        expanded = isContractExpanded,
                        onDismissRequest = { isContractExpanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(appColors.cardBackground)
                    ) {
                        uiState.contracts.forEachIndexed { index, item ->
                            val bg =
                                if (index % 2 == 0) appColors.cardBackground else appColors.cardBackgroundAlt
                            Surface(modifier = Modifier.fillMaxWidth(), color = bg) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = item.title.orEmpty(),
                                            color = appColors.textPrimary
                                        )
                                    },
                                    onClick = {
                                        viewModel.onSelectContract(item)
                                        isContractExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // پیشوند سریال
                CustomEditTextField(
                    value = uiState.serialPrefix,
                    onValueChange = viewModel::onSerialPrefixChange,
                    label = stringResource(R.string.label_serial_prefix),
                    placeholder = stringResource(R.string.hint_enter_serial_prefix),
                    leadingIcon = null,
                    isRequired = false
                )

                // تعداد سریال
                CustomEditTextField(
                    value = uiState.countSerialText,
                    onValueChange = viewModel::onCountSerialChange,
                    label = stringResource(R.string.label_count_serial),
                    placeholder = stringResource(R.string.hint_enter_count_serial),
                    leadingIcon = null,
                    isRequired = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                // سریال شروع
                CustomEditTextField(
                    value = uiState.serialFromText,
                    onValueChange = viewModel::onSerialFromChange,
                    label = stringResource(R.string.label_serial_from),
                    placeholder = stringResource(R.string.hint_enter_serial_from),
                    leadingIcon = null,
                    isRequired = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                // وضعیت
                DropdownSelectorField(
                    value = statusMap.entries.firstOrNull { it.key.id == uiState.selectedStatusId }?.value.orEmpty(),
                    label = stringResource(R.string.label_contract_offer_Status),
                    placeholder = stringResource(R.string.hint_choose_status),
                    items = statusMap.values.toList(),
                    isRequired = false,
                    onItemSelected = { selectedTitle ->
                        val status =
                            statusMap.entries.firstOrNull { it.value == selectedTitle }?.key
                                ?: ContractOfferStatus.DRAFT
                        viewModel.onStatusChange(status.id)
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
