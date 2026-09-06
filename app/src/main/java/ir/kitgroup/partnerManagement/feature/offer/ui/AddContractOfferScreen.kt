package ir.kitgroup.partnerManagement.feature.offer.ui

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
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun AddContractOfferScreen(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit = {}
) {
    val appColors = LocalPartnerManagementColors.current

    var offerTicketPlan by rememberSaveable { mutableStateOf("") }
    var isOfferTicketPlanExpanded by remember { mutableStateOf(false) }
    val offerTicketPlanList = remember { listOf("فروش بلیط تابستانه", "فروش بلیط تخفیف دار") }

    var contract by rememberSaveable { mutableStateOf("") }
    var isContractExpanded by remember { mutableStateOf(false) }
    val contractList = remember { listOf("قراداد همکاری", "قرارداد تابستانه") }


    var serialPrefix by rememberSaveable { mutableStateOf("") }
    var countSerial by rememberSaveable { mutableStateOf("") }
    var serialFrom by rememberSaveable { mutableStateOf("") }
    var serialTo by rememberSaveable { mutableStateOf("") }
    var title by rememberSaveable { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomHeader(
                title = R.string.label_contract_offer,
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
                    .padding(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Box(modifier = Modifier.fillMaxWidth()) {
                        CustomSelectorField(
                            value = offerTicketPlan,
                            label = stringResource(R.string.label_offer_plan),
                            placeholder = stringResource(R.string.hint_choose_offer_plan),
                            isExpanded = isOfferTicketPlanExpanded,
                            onClick = {
                                isOfferTicketPlanExpanded = !isOfferTicketPlanExpanded
                            }
                        )
                        DropdownMenu(
                            expanded = isOfferTicketPlanExpanded,
                            onDismissRequest = { isOfferTicketPlanExpanded = false },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(appColors.cardBackground)
                        ) {
                            offerTicketPlanList.forEachIndexed { index, offerTicketPlanItem ->
                                val backgroundColor =
                                    if (index % 2 == 0) appColors.cardBackground else appColors.cardBackgroundAlt
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = backgroundColor
                                ) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = offerTicketPlanItem,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = appColors.textPrimary
                                            )
                                        },
                                        onClick = {
                                            offerTicketPlan = offerTicketPlanItem
                                            isOfferTicketPlanExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Box(modifier = Modifier.fillMaxWidth()) {
                        CustomSelectorField(
                            value = contract,
                            label = stringResource(R.string.label_contract_item),
                            placeholder = stringResource(R.string.hint_choose_contract),
                            isExpanded = isContractExpanded,
                            onClick = {
                                isContractExpanded = !isContractExpanded
                            }
                        )
                        DropdownMenu(
                            expanded = isContractExpanded,
                            onDismissRequest = { isContractExpanded = false },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(appColors.cardBackground)
                        ) {
                            contractList.forEachIndexed { index, contractItem ->
                                val backgroundColor =
                                    if (index % 2 == 0) appColors.cardBackground else appColors.cardBackgroundAlt
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = backgroundColor
                                ) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = contractItem,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = appColors.textPrimary
                                            )
                                        },
                                        onClick = {
                                            contract = contractItem
                                            isContractExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    CustomEditTextField(
                        value = serialPrefix,
                        onValueChange = { serialPrefix = it },
                        label = stringResource(R.string.label_serial_prefix),
                        placeholder = stringResource(R.string.hint_enter_serial_prefix),
                        leadingIcon = null, isRequired = true
                    )
                    CustomEditTextField(
                        value = countSerial,
                        onValueChange = { countSerial = it },
                        label = stringResource(R.string.label_count_serial),
                        placeholder = stringResource(R.string.hint_enter_count_serial),
                        leadingIcon = null, isRequired = true
                    )

                    CustomEditTextField(
                        value = serialFrom,
                        onValueChange = { input ->
                            serialFrom = input.filter { it.isDigit() }
                        },
                        label = stringResource(R.string.label_serial_from),
                        placeholder = stringResource(R.string.hint_enter_serial_from),
                        leadingIcon = null,
                        isRequired = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )

                    CustomEditTextField(
                        value = serialTo,
                        onValueChange = { input ->
                            serialTo = input.filter { it.isDigit() }
                        },
                        label = stringResource(R.string.label_serial_to),
                        placeholder = stringResource(R.string.hint_enter_serial_to),
                        leadingIcon = null,
                        isRequired = false,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )

                    CustomEditTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = stringResource(R.string.label_title),
                        placeholder = stringResource(R.string.hint_enter_title),
                        leadingIcon = null, isRequired = false
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                CustomButton(
                    text = stringResource(R.string.label_registration),
                    onClick = onSaveClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun AddContractOfferScreenPreview() {
    AppScreenPreview {
        AddContractOfferScreen(
            onBackClick = {},
            onSaveClick = {}
        )
    }
}
