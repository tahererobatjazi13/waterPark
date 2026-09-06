package ir.kitgroup.partnerManagement.feature.offer.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
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
import ir.kitgroup.partnerManagement.core.ui.components.*
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors

@Composable
fun AddOfferTicketPlanLineDetailScreen(
    initialPlanHeader: String = "", // مقدار دریافتی از صفحه قبل
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit = {}
) {
    val appColors = LocalPartnerManagementColors.current

    // سربرگ طرح غیرقابل ویرایش است
    val planHeader = initialPlanHeader

    var productService by rememberSaveable { mutableStateOf("") }
    var isProductServiceExpanded by remember { mutableStateOf(false) }
    val productServiceList = remember { listOf("بلیط بزرگسال", "بلیط خردسال") }

    var personCategory by rememberSaveable { mutableStateOf("") }
    var isPersonCategoryExpanded by remember { mutableStateOf(false) }
    val personCategoryList = remember { listOf("بزرگسال", "خردسال", "نوزاد", "سالمند") }

    var gender by rememberSaveable { mutableStateOf("") }
    val genderList = remember {
        listOf("مرد", "زن", "نیاز نیست")
    }

    var commissionType by rememberSaveable { mutableStateOf("") }
    val commissionTypeList = remember {
        listOf("بدون پورسانت", "درصدی", "مبلغ ثابت")
    }

    var commissionPercent by rememberSaveable { mutableStateOf("") }
    var commissionAmount by rememberSaveable { mutableStateOf("") }

    var discountType by rememberSaveable { mutableStateOf("") }
    val discountTypeList = remember {
        listOf("بدون تخفیف", "درصدی", "مبلغ ثابت")
    }

    var discountPercent by rememberSaveable { mutableStateOf("") }
    var discountAmount by rememberSaveable { mutableStateOf("") }

    var status by rememberSaveable { mutableStateOf("فعال") }

    val statusList = remember {
        listOf("فعال", "موقتا متوقف", "ابطال شده")
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomHeader(
                title = R.string.label_offer_product_service,
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
                    // سربرگ طرح (فقط خواندنی و غیرقابل ویرایش)
                    CustomEditTextField(
                        value = planHeader,
                        onValueChange = {},
                        label = stringResource(R.string.label_plan_header),
                        placeholder = "",
                        readOnly = true,
                        isRequired = false,
                        leadingIcon = null
                    )

                    // کالا / خدمت مرتبط
                    Box(modifier = Modifier.fillMaxWidth()) {
                        CustomSelectorField(
                            value = productService,
                            label = stringResource(R.string.label_related_product_service),
                            placeholder = stringResource(R.string.hint_choose_related_product_service),
                            isExpanded = isProductServiceExpanded,
                            onClick = {
                                isProductServiceExpanded = !isProductServiceExpanded
                            }
                        )
                        DropdownMenu(
                            expanded = isProductServiceExpanded,
                            onDismissRequest = { isProductServiceExpanded = false },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(appColors.cardBackground)
                        ) {
                            productServiceList.forEachIndexed { index, productServiceItem ->
                                val backgroundColor =
                                    if (index % 2 == 0) appColors.cardBackground else appColors.cardBackgroundAlt
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = backgroundColor
                                ) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = productServiceItem,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = appColors.textPrimary
                                            )
                                        },
                                        onClick = {
                                            productService = productServiceItem
                                            isProductServiceExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // رده سنی
                    Box(modifier = Modifier.fillMaxWidth()) {
                        CustomSelectorField(
                            value = personCategory,
                            isRequired = false,
                            label = stringResource(R.string.label_age_category),
                            placeholder = stringResource(R.string.hint_choose_age_category),
                            isExpanded = isPersonCategoryExpanded,
                            onClick = {
                                isPersonCategoryExpanded = !isPersonCategoryExpanded
                            }
                        )
                        DropdownMenu(
                            expanded = isPersonCategoryExpanded,
                            onDismissRequest = { isPersonCategoryExpanded = false },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(appColors.cardBackground)
                        ) {
                            personCategoryList.forEachIndexed { index, personCategoryItem ->
                                val backgroundColor =
                                    if (index % 2 == 0) appColors.cardBackground else appColors.cardBackgroundAlt
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = backgroundColor
                                ) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = personCategoryItem,
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = appColors.textPrimary
                                            )
                                        },
                                        onClick = {
                                            personCategory = personCategoryItem
                                            isPersonCategoryExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // جنسیت
                    DropdownSelectorField(
                        value = gender,
                        label = stringResource(R.string.label_gender),
                        placeholder = stringResource(R.string.hint_choose_gender),
                        items = genderList,
                        isRequired = false,
                        onItemSelected = {
                            gender = it
                        }
                    )

                    // نوع پورسانت
                    DropdownSelectorField(
                        value = commissionType,
                        label = stringResource(R.string.label_commission_type),
                        placeholder = stringResource(R.string.hint_choose_commission_type),
                        items = commissionTypeList,
                        isRequired = true,
                        onItemSelected = { selected ->
                            commissionType = selected
                            if (selected != "درصدی") commissionPercent = ""
                            if (selected != "مبلغ ثابت") commissionAmount = ""
                        }
                    )

                    // فیلد شرطی درصد پورسانت
                    AnimatedVisibility(
                        visible = commissionType == "درصدی",
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        CustomEditTextField(
                            value = commissionPercent,
                            onValueChange = { commissionPercent = it },
                            label = stringResource(R.string.label_commission_percent),
                            placeholder = stringResource(R.string.hint_enter_commission_percent),
                            isRequired = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            leadingIcon = null
                        )
                    }

                    // فیلد شرطی مبلغ پورسانت
                    AnimatedVisibility(
                        visible = commissionType == "مبلغ ثابت",
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        CustomEditTextField(
                            value = commissionAmount,
                            onValueChange = { commissionAmount = it },
                            label = stringResource(R.string.label_commission_amount),
                            placeholder = stringResource(R.string.hint_enter_commission_amount),
                            isRequired = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            leadingIcon = null
                        )
                    }

                    // نوع تخفیف
                    DropdownSelectorField(
                        value = discountType,
                        label = stringResource(R.string.label_discount_type),
                        placeholder = stringResource(R.string.label_choose_discount_type),
                        items = discountTypeList,
                        isRequired = true,
                        onItemSelected = { selected ->
                            discountType = selected
                            if (selected != "درصدی") discountPercent = ""
                            if (selected != "مبلغ ثابت") discountAmount = ""
                        }
                    )

                    // فیلد شرطی درصد تخفیف
                    AnimatedVisibility(
                        visible = discountType == "درصدی",
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        CustomEditTextField(
                            value = discountPercent,
                            onValueChange = { discountPercent = it },
                            label = stringResource(R.string.label_discount_percent),
                            placeholder = stringResource(R.string.hint_enter_discount_percent),
                            isRequired = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            leadingIcon = null
                        )
                    }

                    // فیلد شرطی مبلغ تخفیف
                    AnimatedVisibility(
                        visible = discountType == "مبلغ ثابت",
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        CustomEditTextField(
                            value = discountAmount,
                            onValueChange = { discountAmount = it },
                            label = stringResource(R.string.label_discount_amount),
                            placeholder = stringResource(R.string.hint_enter_discount_amount),
                            isRequired = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            leadingIcon = null
                        )
                    }

                    // وضعیت طرح
                    DropdownSelectorField(
                        value = status,
                        label = stringResource(R.string.label_plan_status),
                        placeholder = "",
                        items = statusList,
                        isRequired = false,
                        onItemSelected = { status = it }
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                // دکمه ثبت
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
private fun AddOfferTicketPlanLineDetailScreenPreview() {
    AppScreenPreview {
        AddOfferTicketPlanLineDetailScreen(
            initialPlanHeader = "فروش تابستانه",
            onBackClick = {},
            onSaveClick = {}
        )
    }
}
