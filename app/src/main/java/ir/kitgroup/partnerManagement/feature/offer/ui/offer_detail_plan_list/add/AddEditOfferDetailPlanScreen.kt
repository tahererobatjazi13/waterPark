package ir.kitgroup.partnerManagement.feature.offer.ui.offer_detail_plan_list.add

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.ProductEntity
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomEditTextField
import ir.kitgroup.partnerManagement.core.ui.components.CustomSelectorField
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.CommissionType
import ir.kitgroup.partnerManagement.core.ui.util.DiscountType
import ir.kitgroup.partnerManagement.core.ui.util.GenderType
import ir.kitgroup.partnerManagement.core.ui.util.OfferDetailPlanStatus
import ir.kitgroup.partnerManagement.core.ui.util.PersonCategory
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent

@Composable
fun AddEditOfferDetailPlanScreen(
    offerTicketPlanId: String? = null,
    offerTicketPlanLineId: String? = null,
    offerTicketPlanName: String = "",
    onSuccessSave: () -> Unit,
    viewModel: AddEditOfferDetailPlanViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(offerTicketPlanId, offerTicketPlanLineId) {
        viewModel.initialize(
            offerTicketPlanId = offerTicketPlanId,
            offerTicketPlanLineId = offerTicketPlanLineId,
            offerTicketPlanName = offerTicketPlanName
        )
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

    AddEditOfferDetailPlanContent(
        uiState = uiState,
        snackBarHostState = snackBarHostState,
        onProductSelected = viewModel::onProductSelected,
        onPersonCategoryChange = viewModel::onPersonCategoryChange,
        onGenderChange = viewModel::onGenderChange,
        onCommissionTypeChange = viewModel::onCommissionTypeChange,
        onCommissionPercentChange = viewModel::onCommissionPercentChange,
        onCommissionAmountChange = viewModel::onCommissionAmountChange,
        onDiscountTypeChange = viewModel::onDiscountTypeChange,
        onDiscountPercentChange = viewModel::onDiscountPercentChange,
        onDiscountAmountChange = viewModel::onDiscountAmountChange,
        onStatusChange = viewModel::onStatusChange,
        onSubmit = { viewModel.submitOfferDetailPlan(onSuccessSave) },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun AddEditOfferDetailPlanContent(
    uiState: AddEditOfferDetailPlanUiState,
    snackBarHostState: SnackbarHostState,
    onProductSelected: (ProductEntity) -> Unit,
    onPersonCategoryChange: (PersonCategory) -> Unit,
    onGenderChange: (GenderType) -> Unit,
    onCommissionTypeChange: (CommissionType) -> Unit,
    onCommissionPercentChange: (String) -> Unit,
    onCommissionAmountChange: (String) -> Unit,
    onDiscountTypeChange: (DiscountType) -> Unit,
    onDiscountPercentChange: (String) -> Unit,
    onDiscountAmountChange: (String) -> Unit,
    onStatusChange: (OfferDetailPlanStatus) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val appColors = LocalPartnerManagementColors.current
    var isProductServiceExpanded by remember { mutableStateOf(false) }

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
        OfferDetailPlanStatus.entries.associateBy { context.getString(it.titleRes) }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        containerColor = MaterialTheme.colorScheme.background
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
                        // هدر طرح
                        if (uiState.offerTicketPlanName.isNotBlank()) {
                            CustomEditTextField(
                                value = uiState.offerTicketPlanName,
                                onValueChange = {},
                                label = stringResource(R.string.label_plan_header),
                                placeholder = "",
                                readOnly = true,
                                isRequired = false,
                                leadingIcon = null
                            )
                        }

                        // انتخاب محصول یا خدمت
                        Box(modifier = Modifier.fillMaxWidth()) {
                            CustomSelectorField(
                                value = uiState.productServiceName,
                                label = stringResource(R.string.label_related_product_service),
                                placeholder = stringResource(R.string.hint_choose_related_product_service),
                                isExpanded = isProductServiceExpanded,
                                onClick = { isProductServiceExpanded = !isProductServiceExpanded }
                            )
                            DropdownMenu(
                                expanded = isProductServiceExpanded,
                                onDismissRequest = { isProductServiceExpanded = false },
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .background(appColors.cardBackground)
                            ) {
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
                                                    text = product.name.orEmpty(),
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    color = appColors.textPrimary
                                                )
                                            },
                                            onClick = {
                                                onProductSelected(product)
                                                isProductServiceExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        // رده سنی
                        DropdownSelectorField(
                            value = stringResource(uiState.personCategory.titleRes),
                            label = stringResource(R.string.label_age_category),
                            placeholder = stringResource(R.string.hint_choose_age_category),
                            items = personCategoryMap.keys.toList(),
                            isRequired = false,
                            onItemSelected = { selectedTitle ->
                                personCategoryMap[selectedTitle]?.let(onPersonCategoryChange)
                            }
                        )

                        // جنسیت
                        DropdownSelectorField(
                            value = stringResource(uiState.gender.titleRes),
                            label = stringResource(R.string.label_gender),
                            placeholder = stringResource(R.string.hint_choose_gender),
                            items = genderMap.keys.toList(),
                            isRequired = false,
                            onItemSelected = { selectedTitle ->
                                genderMap[selectedTitle]?.let(onGenderChange)
                            }
                        )

                        // نوع پورسانت
                        DropdownSelectorField(
                            value = stringResource(uiState.commissionType.titleRes),
                            label = stringResource(R.string.label_commission_type),
                            placeholder = stringResource(R.string.hint_choose_commission_type),
                            items = commissionTypeMap.keys.toList(),
                            isRequired = true,
                            onItemSelected = { selectedTitle ->
                                commissionTypeMap[selectedTitle]?.let(onCommissionTypeChange)
                            }
                        )

                        // فیلدهای شرطی پورسانت
                        AnimatedVisibility(
                            visible = uiState.commissionType == CommissionType.PERCENTAGE,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            CustomEditTextField(
                                value = uiState.commissionPercent,
                                onValueChange = onCommissionPercentChange,
                                label = stringResource(R.string.label_commission_percent),
                                placeholder = stringResource(R.string.hint_enter_commission_percent),
                                isRequired = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                leadingIcon = null
                            )
                        }

                        AnimatedVisibility(
                            visible = uiState.commissionType == CommissionType.FIXED_AMOUNT,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            CustomEditTextField(
                                value = uiState.commissionAmount,
                                onValueChange = onCommissionAmountChange,
                                label = stringResource(R.string.label_commission_amount),
                                placeholder = stringResource(R.string.hint_enter_commission_amount),
                                isRequired = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                leadingIcon = null
                            )
                        }

                        // نوع تخفیف
                        DropdownSelectorField(
                            value = stringResource(uiState.discountType.titleRes),
                            label = stringResource(R.string.label_discount_type),
                            placeholder = stringResource(R.string.label_choose_discount_type),
                            items = discountTypeMap.keys.toList(),
                            isRequired = true,
                            onItemSelected = { selectedTitle ->
                                discountTypeMap[selectedTitle]?.let(onDiscountTypeChange)
                            }
                        )

                        // فیلدهای شرطی تخفیف
                        AnimatedVisibility(
                            visible = uiState.discountType == DiscountType.PERCENTAGE,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            CustomEditTextField(
                                value = uiState.discountPercent,
                                onValueChange = onDiscountPercentChange,
                                label = stringResource(R.string.label_discount_percent),
                                placeholder = stringResource(R.string.hint_enter_discount_percent),
                                isRequired = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                leadingIcon = null
                            )
                        }

                        AnimatedVisibility(
                            visible = uiState.discountType == DiscountType.FIXED_AMOUNT,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            CustomEditTextField(
                                value = uiState.discountAmount,
                                onValueChange = onDiscountAmountChange,
                                label = stringResource(R.string.label_discount_amount),
                                placeholder = stringResource(R.string.hint_enter_discount_amount),
                                isRequired = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                leadingIcon = null
                            )
                        }

                        // وضعیت ردیف طرح
                        DropdownSelectorField(
                            value = stringResource(uiState.status.titleRes),
                            label = stringResource(R.string.label_plan_status),
                            placeholder = "",
                            items = statusMap.keys.toList(),
                            isRequired = false,
                            onItemSelected = { selectedTitle ->
                                statusMap[selectedTitle]?.let(onStatusChange)
                            }
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    CustomButton(
                        text = if (uiState.isEditMode) {
                            stringResource(R.string.label_edit_plan)
                        } else {
                            stringResource(R.string.label_registration)
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
}
