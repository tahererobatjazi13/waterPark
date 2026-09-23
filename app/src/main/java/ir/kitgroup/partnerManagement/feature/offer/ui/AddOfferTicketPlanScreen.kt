package ir.kitgroup.partnerManagement.feature.offer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.OfferTicketPlanEntity
import ir.kitgroup.partnerManagement.core.ui.components.*
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.OfferPlanStatus
import saman.zamani.persiandate.PersianDate
import java.util.Locale
import java.util.UUID

@Composable
fun AddOfferTicketPlanScreen(
    onBackClick: () -> Unit,
    onSaveClick: (OfferTicketPlanEntity) -> Unit = {}
) {
    val appColors = LocalPartnerManagementColors.current

    var offerName by rememberSaveable { mutableStateOf("") }
    var offerCode by rememberSaveable { mutableStateOf("") }

    val today = remember { PersianDate() }
    var showValidFromDatePicker by remember { mutableStateOf(false) }
    var showValidToDatePicker by remember { mutableStateOf(false) }

    var validFromDate by rememberSaveable {
        mutableStateOf(
            "${today.shYear}/${String.format(Locale.US, "%02d", today.shMonth)}/${
                String.format(Locale.US, "%02d", today.shDay)
            }"
        )
    }
    var validToDate by rememberSaveable {
        mutableStateOf(
            "${today.shYear}/${String.format(Locale.US, "%02d", today.shMonth)}/${
                String.format(Locale.US, "%02d", today.shDay)
            }"
        )
    }

    var selectedStatus by rememberSaveable { mutableStateOf(OfferPlanStatus.ACTIVE) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomHeader(
                title = R.string.label_create_plan_offer,
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
                    CustomEditTextField(
                        value = offerName,
                        onValueChange = { offerName = it },
                        label = stringResource(R.string.label_offer_name),
                        placeholder = stringResource(R.string.hint_enter_offer_name),
                        leadingIcon = null,
                        isRequired = true
                    )

                    CustomEditTextField(
                        value = offerCode,
                        onValueChange = { input ->
                            offerCode = input.filter { it.isDigit() }
                        },
                        label = stringResource(R.string.label_offer_code),
                        placeholder = stringResource(R.string.hint_enter_offer_code),
                        leadingIcon = null,
                        isRequired = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )

                    CustomDateTimeFields(
                        label = stringResource(R.string.label_valid_from_date),
                        date = validFromDate,
                        onDateClick = { showValidFromDatePicker = true },
                        showTime = false,
                        isRequired = true
                    )

                    CustomDateTimeFields(
                        label = stringResource(R.string.label_valid_to_date),
                        date = validToDate,
                        onDateClick = { showValidToDatePicker = true },
                        showTime = false,
                        isRequired = true
                    )

                    DropdownSelectorField(
                        value = stringResource(selectedStatus.titleRes),
                        label = stringResource(R.string.label_status),
                        placeholder = "",
                        items = OfferPlanStatus.entries.map { stringResource(it.titleRes) },
                        isRequired = true,
                        onItemSelected = { selectedTitle ->
                            OfferPlanStatus.entries.find {
                                /* تطبیق آیتم انتخاب شده */
                                false
                            }
                        }
                    )
                    

                    Spacer(modifier = Modifier.height(10.dp))
                }

                CustomButton(
                    text = stringResource(R.string.label_registration),
                    onClick = {
                        val newPlan = OfferTicketPlanEntity(
                            offerTicketPlanId = UUID.randomUUID().toString(),
                            name = offerName.trim(),
                            code = offerCode.trim(),
                            recreationCenterId = null,
                            validFromDate = validFromDate,
                            validToDate = validToDate,
                            status = selectedStatus.id
                        )
                        onSaveClick(newPlan)
                    },
                    enabled = offerName.isNotBlank() && offerCode.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )
            }
        }
    }

    if (showValidFromDatePicker) {
        DatePickerDialog(
            onDismiss = {
                showValidFromDatePicker = false
            },
            onDateSelected = { date ->
                validFromDate = "${date.year}/${String.format(Locale.US, "%02d", date.month)}/${
                    String.format(Locale.US, "%02d", date.day)
                }"
                showValidFromDatePicker = false
            }
        )
    }

    if (showValidToDatePicker) {
        DatePickerDialog(
            onDismiss = {
                showValidToDatePicker = false
            },
            onDateSelected = { date ->
                validToDate = "${date.year}/${String.format(Locale.US, "%02d", date.month)}/${
                    String.format(Locale.US, "%02d", date.day)
                }"
                showValidToDatePicker = false
            }
        )
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun AddOfferTicketPlanScreenPreview() {
    AppScreenPreview {
        AddOfferTicketPlanScreen(
            onBackClick = {},
            onSaveClick = {}
        )
    }
}
