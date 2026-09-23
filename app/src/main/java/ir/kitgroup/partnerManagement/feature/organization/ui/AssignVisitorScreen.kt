package ir.kitgroup.partnerManagement.feature.organization.ui

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
import ir.kitgroup.partnerManagement.core.ui.components.AppScreenPreview
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomDateTimeFields
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.components.CustomSelectorField
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import saman.zamani.persiandate.PersianDate
import java.util.Locale
import ir.kitgroup.partnerManagement.core.ui.components.DatePickerDialog
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField

@Composable
fun AssignVisitorScreen(
    organizationId: String? = null,
    onBack: () -> Unit,
    onSaveClick: () -> Unit = {},
) {
    val appColors = LocalPartnerManagementColors.current


    var visitor by rememberSaveable { mutableStateOf("") }
    var isVisitorExpanded by remember { mutableStateOf(false) }
    val visitorList = remember { listOf("امید حیدری", "ندا احمدی", "حسین میری") }

    val today = remember { PersianDate() }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    var startDate by rememberSaveable {
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
    var endDate by rememberSaveable {
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
    var status by rememberSaveable { mutableStateOf("فعال") }

    val statusList = remember {
        listOf("فعال", "غیر فعال", "ثبت متوقف")
    }
    Scaffold(
        topBar = {
            CustomHeader(
                title = R.string.label_assigning_visitor_organization,
                showBackButton = true,
                onBackClick = onBack
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
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {


                Box(modifier = Modifier.fillMaxWidth()) {
                    CustomSelectorField(
                        value = visitor,
                        label = stringResource(R.string.label_choose_visitor),
                        placeholder = stringResource(R.string.hint_choose_visitor),
                        isExpanded = isVisitorExpanded,
                        onClick = {
                            isVisitorExpanded = !isVisitorExpanded
                        }
                    )
                    DropdownMenu(
                        expanded = isVisitorExpanded,
                        onDismissRequest = { isVisitorExpanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(appColors.cardBackground)
                    ) {
                        visitorList.forEachIndexed { index, visitorItem ->
                            val backgroundColor =
                                if (index % 2 == 0) appColors.cardBackground else appColors.cardBackgroundAlt
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = backgroundColor
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = visitorItem,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = appColors.textPrimary
                                        )
                                    },
                                    onClick = {
                                        visitor = visitorItem
                                        isVisitorExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                CustomDateTimeFields(
                    label = stringResource(R.string.label_start_date),
                    date = startDate,
                    onDateClick = { showStartDatePicker = true },
                    showTime = false,
                    isRequired = false
                )

                /*   CustomDateTimeFields(
                      label = stringResource(R.string.label_end_date),
                      date = endDate,
                      onDateClick = { showEndDatePicker = true },
                      showTime = false
                  )*/

                // وضعیت
                DropdownSelectorField(
                    value = status,
                    label = stringResource(R.string.label_status),
                    placeholder = "",
                    items = statusList,
                    isRequired = false,
                    onItemSelected = { status = it }
                )

                Spacer(Modifier.weight(1f))

                CustomButton(
                    text = stringResource(R.string.label_registration),
                    onClick = onSaveClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }



    if (showStartDatePicker) {
        DatePickerDialog(
            onDismiss = {
                showStartDatePicker = false
            },
            onDateSelected = { date ->
                startDate = "${date.year}/${String.format(Locale.US, "%02d", date.month)}/${
                    String.format(Locale.US, "%02d", date.day)
                }"
                showStartDatePicker = false
            }
        )
    }

    if (showEndDatePicker) {
        DatePickerDialog(
            onDismiss = {
                showEndDatePicker = false
            },
            onDateSelected = { date ->
                endDate = "${date.year}/${String.format(Locale.US, "%02d", date.month)}/${
                    String.format(Locale.US, "%02d", date.day)
                }"
                showEndDatePicker = false
            }
        )
    }
}


@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
private fun RegisterVisitScreenPreview() {
    AppScreenPreview {
        AssignVisitorScreen(
            organizationId = "",
            onBack = {},
            onSaveClick = {})
    }
}