package ir.kitgroup.partnerManagement.feature.offer.ui.serial_offer

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.*
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.core.ui.util.ContractOfferStatus

@Composable
fun AddContractOfferScreen(
    organizationId: String? = null,
    meetingId: String? = null,
    onSaveSuccess: () -> Unit = {},
    viewModel: AddSerialOfferViewModel = hiltViewModel()
) {
    val appColors = LocalPartnerManagementColors.current
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isSaving by viewModel.isSaving.collectAsStateWithLifecycle()

    var isOfferExpanded by remember { mutableStateOf(false) }
    var isContractExpanded by remember { mutableStateOf(false) }

    val statusMap = remember {
        ContractOfferStatus.entries.associateWith { context.getString(it.titleRes) }
    }


    LaunchedEffect(Unit) {
        viewModel.saveMessage.collect { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.saveSuccessEvent.collect {
            onSaveSuccess()
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
                .padding(20.dp)
        ) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Offer Plan
                Box(modifier = Modifier.fillMaxWidth()) {
                    CustomSelectorField(
                        value = uiState.selectedOfferPlanTitle,
                        label = stringResource(R.string.label_offer_plan),
                        placeholder = stringResource(R.string.hint_choose_offer_plan),
                        isExpanded = isOfferExpanded,
                        onClick = { isOfferExpanded = !isOfferExpanded }
                    )

                    DropdownMenu(
                        expanded = isOfferExpanded,
                        onDismissRequest = { isOfferExpanded = false },
                        modifier = Modifier.fillMaxWidth(0.9f).background(appColors.cardBackground)
                    ) {
                        uiState.offerPlans.forEachIndexed { index, item ->
                            val bg =
                                if (index % 2 == 0) appColors.cardBackground else appColors.cardBackgroundAlt
                            Surface(modifier = Modifier.fillMaxWidth(), color = bg) {
                                DropdownMenuItem(
                                    text = { Text(item.title) },
                                    onClick = {
                                        viewModel.onSelectOfferPlan(item)
                                        isOfferExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Contract
                Box(modifier = Modifier.fillMaxWidth()) {
                    CustomSelectorField(
                        value = uiState.selectedContractTitle,
                        label = stringResource(R.string.label_contract_item),
                        placeholder = stringResource(R.string.hint_choose_contract),
                        isExpanded = isContractExpanded,
                        onClick = { isContractExpanded = !isContractExpanded }
                    )

                    DropdownMenu(
                        expanded = isContractExpanded,
                        onDismissRequest = { isContractExpanded = false },
                        modifier = Modifier.fillMaxWidth(0.9f).background(appColors.cardBackground)
                    ) {
                        uiState.contracts.forEachIndexed { index, item ->
                            val bg =
                                if (index % 2 == 0) appColors.cardBackground else appColors.cardBackgroundAlt
                            Surface(modifier = Modifier.fillMaxWidth(), color = bg) {
                                DropdownMenuItem(
                                    text = { Text(item.title) },
                                    onClick = {
                                        viewModel.onSelectContract(item)
                                        isContractExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                CustomEditTextField(
                    value = uiState.serialPrefix,
                    onValueChange = viewModel::onSerialPrefixChange,
                    label = stringResource(R.string.label_serial_prefix),
                    placeholder = stringResource(R.string.hint_enter_serial_prefix),
                    leadingIcon = null,
                    isRequired = false
                )

                CustomEditTextField(
                    value = uiState.countSerialText,
                    onValueChange = viewModel::onCountSerialChange,
                    label = stringResource(R.string.label_count_serial),
                    placeholder = stringResource(R.string.hint_enter_count_serial),
                    leadingIcon = null,
                    isRequired = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                CustomEditTextField(
                    value = uiState.serialFromText,
                    onValueChange = viewModel::onSerialFromChange,
                    label = stringResource(R.string.label_serial_from),
                    placeholder = stringResource(R.string.hint_enter_serial_from),
                    leadingIcon = null,
                    isRequired = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                DropdownSelectorField(
                    value = statusMap.entries.firstOrNull { it.key.id == uiState.selectedStatusId }?.value ?: "",
                    label = stringResource(R.string.label_contract_offer_Status),
                    placeholder = stringResource(R.string.hint_choose_status),
                    items = statusMap.values.toList(),
                    isRequired = false,
                    onItemSelected = { selectedTitle ->
                        val status = statusMap.entries.firstOrNull { it.value == selectedTitle }?.key
                            ?: ContractOfferStatus.DRAFT
                        viewModel.onStatusChange(status.id)
                    }
                )

                Spacer(Modifier.height(10.dp))
            }

            CustomButton(
                text = stringResource(R.string.label_registration),
                enabled = !isSaving,
                onClick = { viewModel.save(organizationId = organizationId, meetingId = meetingId) },
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
            )
        }
    }
}
