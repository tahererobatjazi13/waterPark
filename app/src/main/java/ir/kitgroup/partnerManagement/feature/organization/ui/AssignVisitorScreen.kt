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
import ir.kitgroup.partnerManagement.core.ui.util.Status
import ir.kitgroup.partnerManagement.feature.organization.model.OrganizationModel
import saman.zamani.persiandate.PersianDate
import java.util.Locale
import ir.kitgroup.partnerManagement.core.ui.components.DatePickerDialog
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField

@Composable
fun AssignVisitorScreen(
    organizationId: Int? = null,
    onBackClick: () -> Unit, onSaveClick: () -> Unit,
) {
    val appColors = LocalPartnerManagementColors.current
    var selectedOrganization by rememberSaveable { mutableStateOf<OrganizationModel?>(null) }
    var showOrganizationSheet by remember { mutableStateOf(false) }
    val organizationList =
        listOf(
            OrganizationModel(1, "هتل پارسیان آزادی", "مشهد،یوسفی", Status.ACTIVE, 5,latitude =36.2972,longitude =59.6067),
            OrganizationModel(2, " سازمان پالاس", "مشهد،قاسم آباد", Status.ACTIVE, 4  , latitude =36.3109,longitude =59.5492),
            OrganizationModel(3, "سازمان نوید", "مشهد،پیروزی", Status.ACTIVE, 3,latitude =36.2972,longitude =59.6067),
            OrganizationModel(4, "هتل مرکزی", "مشهد، امام رضا", Status.ACTIVE, 5,latitude =36.2972,longitude =59.6067),
            OrganizationModel(5, "کیوسک اطلس", "مشهد، کوهسنگی", Status.ACTIVE, 3,latitude =36.2972,longitude =59.6067),
            OrganizationModel(6, "هتل الماس", "مشهد، پاستور", Status.ACTIVE, 2,latitude =36.2972,longitude =59.6067),
            OrganizationModel(7, "هتل وفا", "مشهد، وکیال آباد", Status.ACTIVE, 3 , latitude =36.3109,longitude =59.5492),
            OrganizationModel(8, "سازمان مهندسی", "مشهد، فاطمی", Status.ACTIVE, 4 , latitude =36.3109,longitude =59.5492),
            OrganizationModel(9, "هتل امیر", "مشهد، رضاییه", Status.ACTIVE, 2 , latitude =36.3109,longitude =59.5492),
            OrganizationModel(10, "آپارتمان ملل", "مشهد، ستاری", Status.ACTIVE, 3 , latitude =36.3109,longitude =59.5492),
            OrganizationModel(11, "مهمانسرا اسپیناس", "مشهد، مرکزی", Status.INACTIVE, 5 , latitude =36.3109,longitude =59.5492),
            OrganizationModel(12, "چالیدره", "مشهد، طرقبه", Status.INACTIVE, 2 , latitude =36.3109,longitude =59.5492)
        )

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
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CustomSelectorField(
                    value = selectedOrganization?.name ?: "",
                    label = stringResource(R.string.label_organization_name),
                    placeholder = stringResource(R.string.hint_choose_organization_name),
                    isExpanded = showOrganizationSheet,
                    onClick = { showOrganizationSheet = true }
                )

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

    if (showOrganizationSheet) {
        OrganizationBottomSheet(
            list = organizationList,
            onDismiss = { showOrganizationSheet = false },
            onItemSelected = { item ->
                selectedOrganization = item
                showOrganizationSheet = false
            }
        )
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
            organizationId = 0,
            onBackClick = {},
            onSaveClick = {})
    }
}