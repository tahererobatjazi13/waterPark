package ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail_list.add

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
import androidx.hilt.navigation.compose.hiltViewModel
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.*
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.CommissionType
import ir.kitgroup.partnerManagement.core.ui.util.DiscountType
import ir.kitgroup.partnerManagement.core.ui.util.GenderType
import ir.kitgroup.partnerManagement.core.ui.util.OfferPlanLineStatus
import ir.kitgroup.partnerManagement.core.ui.util.PersonCategory
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent

@Composable
fun AddOfferDetailScreen(
    offerDetailId: String,               // <-- پارامتر جدید: شناسه واقعی آفر
    initialPlanHeader: String = "",
    viewModel: AddOfferDetailViewModel = hiltViewModel(),
    onSaveSuccess: () -> Unit // <-- کال‌بک بازگشت پس از ثبت موفق
) {
    val uiState by viewModel.uiState.collectAsState()

    val appColors = LocalPartnerManagementColors.current

    val planHeaderName = initialPlanHeader
    val context = androidx.compose.ui.platform.LocalContext.current

    var productServiceId by rememberSaveable { mutableStateOf("") }
    var productServiceName by rememberSaveable { mutableStateOf("") }
    var isProductServiceExpanded by remember { mutableStateOf(false) }


    val saveMessage by viewModel.saveMessage.collectAsState(initial = null)

// مشاهده تغییرات پیام ذخیره سازی
    LaunchedEffect(saveMessage) {
        saveMessage?.let { message ->
            android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT)
                .show()
            onSaveSuccess()

        }
    }
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowMessage -> {
                    snackbarHostState.showSnackbar(event.message.asString(context))
                    onSaveSuccess()
                }

                is UiEvent.ShowError -> {
                    snackbarHostState.showSnackbar(event.error.asString(context))
                }
            }
        }
    }
// تعریف لیست‌ها و Map بر مبنای متون ترجمه‌شده
    val personCategoryMap = remember(context) {
        PersonCategory.entries.associateBy { context.getString(it.titleRes) }
    }

    val genderMap = remember(context) {
        GenderType.entries.associateBy { context.getString(it.titleRes) }
    }

    val commissionTypeMap = remember(context) {
        CommissionType.entries.associateBy { context.getString(it.titleRes) }
    }

    val discountTypeMap = remember(context) {
        DiscountType.entries.associateBy { context.getString(it.titleRes) }
    }

    val statusMap = remember(context) {
        OfferPlanLineStatus.entries.associateBy { context.getString(it.titleRes) }
    }

    var selectedPersonCategory by rememberSaveable { mutableStateOf(PersonCategory.UNASSIGNED) }
    var selectedGender by rememberSaveable { mutableStateOf(GenderType.NOT_REQUIRED) }

    var selectedCommissionType by rememberSaveable { mutableStateOf(CommissionType.NONE) }
    var commissionPercent by rememberSaveable { mutableStateOf("") }
    var commissionAmount by rememberSaveable { mutableStateOf("") }

    var selectedDiscountType by rememberSaveable { mutableStateOf(DiscountType.NONE) }
    var discountPercent by rememberSaveable { mutableStateOf("") }
    var discountAmount by rememberSaveable { mutableStateOf("") }

    var selectedStatus by rememberSaveable { mutableStateOf(OfferPlanLineStatus.ACTIVE) }
    val isSaving by viewModel.isSaving.collectAsState()
// سینک کردن مقادیر دیتابیس با فیلدهای فرم در حالت ویرایش
    LaunchedEffect(uiState.isEditMode, uiState.offerTicketPlanLineId) {
        if (uiState.isEditMode) {
            productServiceId = uiState.productServiceId
            productServiceName = uiState.productServiceName

            selectedPersonCategory = PersonCategory.fromId(uiState.personCategory)
            selectedGender = GenderType.fromId(uiState.gender)

            selectedCommissionType = CommissionType.fromId(uiState.commissionType)
            commissionPercent = uiState.commissionPercent
            commissionAmount = uiState.commissionAmount

            selectedDiscountType = DiscountType.fromId(uiState.discountType)
            discountPercent = uiState.discountPercent
            discountAmount = uiState.discountAmount

            selectedStatus = OfferPlanLineStatus.fromId(uiState.status)
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
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
                    // سربرگ طرح (فقط خواندنی)
                    CustomEditTextField(
                        value = planHeaderName,
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
                            value = productServiceName,
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
                            // استفاده از لیست واقعی به جای mock
                            uiState.availableProducts.forEachIndexed { index, product ->
                                val backgroundColor =
                                    if (index % 2 == 0) appColors.cardBackground else appColors.cardBackgroundAlt

                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = backgroundColor
                                ) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = product.name ?: "", // استفاده از نام واقعی
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = appColors.textPrimary
                                            )
                                        },
                                        onClick = {
                                            productServiceId =
                                                product.productId // استفاده از ID واقعی
                                            productServiceName = product.name ?: ""
                                            isProductServiceExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // رده سنی (PersonCategory)
                    DropdownSelectorField(
                        value = stringResource(selectedPersonCategory.titleRes),
                        label = stringResource(R.string.label_age_category),
                        placeholder = stringResource(R.string.hint_choose_age_category),
                        items = remember(personCategoryMap) { personCategoryMap.keys.toList() },
                        isRequired = false,
                        onItemSelected = { selectedTitle ->
                            personCategoryMap[selectedTitle]?.let {
                                selectedPersonCategory = it
                            }
                        }
                    )


                    // جنسیت (GenderType)
                    DropdownSelectorField(
                        value = stringResource(selectedGender.titleRes),
                        label = stringResource(R.string.label_gender),
                        placeholder = stringResource(R.string.hint_choose_gender),
                        items = remember(genderMap) { genderMap.keys.toList() },
                        isRequired = false,
                        onItemSelected = { selectedTitle ->
                            genderMap[selectedTitle]?.let {
                                selectedGender = it
                            }
                        }
                    )


                    // نوع پورسانت (CommissionType)
                    DropdownSelectorField(
                        value = stringResource(selectedCommissionType.titleRes),
                        label = stringResource(R.string.label_commission_type),
                        placeholder = stringResource(R.string.hint_choose_commission_type),
                        items = remember(commissionTypeMap) { commissionTypeMap.keys.toList() },
                        isRequired = true,
                        onItemSelected = { selectedTitle ->
                            commissionTypeMap[selectedTitle]?.let {
                                selectedCommissionType = it
                                if (it != CommissionType.PERCENTAGE) commissionPercent = ""
                                if (it != CommissionType.FIXED_AMOUNT) commissionAmount = ""
                            }
                        }
                    )


                    // فیلد شرطی درصد پورسانت
                    AnimatedVisibility(
                        visible = selectedCommissionType == CommissionType.PERCENTAGE,
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
                        visible = selectedCommissionType == CommissionType.FIXED_AMOUNT,
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

                    // نوع تخفیف (DiscountType)
                    DropdownSelectorField(
                        value = stringResource(selectedDiscountType.titleRes),
                        label = stringResource(R.string.label_discount_type),
                        placeholder = stringResource(R.string.label_choose_discount_type),
                        items = remember(discountTypeMap) { discountTypeMap.keys.toList() },
                        isRequired = true,
                        onItemSelected = { selectedTitle ->
                            discountTypeMap[selectedTitle]?.let {
                                selectedDiscountType = it
                                if (it != DiscountType.PERCENTAGE) discountPercent = ""
                                if (it != DiscountType.FIXED_AMOUNT) discountAmount = ""
                            }
                        }
                    )


                    // فیلد شرطی درصد تخفیف
                    AnimatedVisibility(
                        visible = selectedDiscountType == DiscountType.PERCENTAGE,
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
                        visible = selectedDiscountType == DiscountType.FIXED_AMOUNT,
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

                    // وضعیت ردیف طرح (OfferPlanLineStatus)
                    DropdownSelectorField(
                        value = stringResource(selectedStatus.titleRes),
                        label = stringResource(R.string.label_plan_status),
                        placeholder = "",
                        items = remember(statusMap) { statusMap.keys.toList() },
                        isRequired = false,
                        onItemSelected = { selectedTitle ->
                            statusMap[selectedTitle]?.let {
                                selectedStatus = it
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // در داخل AddOfferDetailScreen
                CustomButton(
                    text = if (isSaving) "در حال ثبت..." else stringResource(R.string.label_registration),
                    enabled = !isSaving,
                    onClick = {
                        val discountPercentInt = discountPercent.toIntOrNull() ?: 0
                        val discountAmountInt = discountAmount.toIntOrNull() ?: 0
                        val commissionPercentInt = commissionPercent.toIntOrNull() ?: 0
                        val commissionAmountInt = commissionAmount.toIntOrNull() ?: 0

                        // فراخوانی متد ViewModel
                        viewModel.saveOfferDetail(
                            offerTicketPlanLineId = if (uiState.isEditMode) uiState.offerTicketPlanLineId else null,
                            offerTicketPlanId = if (uiState.isEditMode) uiState.offerTicketPlanId else offerDetailId,
                            productServiceId = productServiceId,
                            discountType = selectedDiscountType.id,
                            discountPercent = discountPercentInt,
                            discountAmount = discountAmountInt,
                            commissionType = selectedCommissionType.id,
                            commissionPercent = commissionPercentInt,
                            commissionAmount = commissionAmountInt,
                            personCategory = selectedPersonCategory.id,
                            gender = selectedGender.id,
                            status = selectedStatus.id,
                            isEdit = uiState.isEditMode
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )
            }
        }
    }
}
