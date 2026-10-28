package ir.kitgroup.partnerManagement.feature.organization.ui.detail.person

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Wc
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationPersonEntity
import ir.kitgroup.partnerManagement.core.ui.components.CustomDescriptionField
import ir.kitgroup.partnerManagement.core.ui.components.CustomEditTextField
import ir.kitgroup.partnerManagement.core.ui.components.DropdownSelectorField
import ir.kitgroup.partnerManagement.core.ui.components.EmptyState
import ir.kitgroup.partnerManagement.core.ui.components.OfflinePendingFooterBanner
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.PersonGenderType
import ir.kitgroup.partnerManagement.core.ui.util.PersonOrganizationRole
import ir.kitgroup.partnerManagement.core.ui.util.PersonOrganizationStatus
import ir.kitgroup.partnerManagement.core.ui.util.SyncStatus
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.components.DualActionButtonBottomBar

@Composable
fun OrganizationPersonsTabContent(
    organizationId: String,
    viewModel: OrganizationPersonViewModel = hiltViewModel(),
    onPhoneClick: (String) -> Unit = {}
) {
    val appColors = LocalPartnerManagementColors.current
    val context = LocalContext.current

    val persons by viewModel.persons.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()

    var showAddPersonSheet by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(organizationId) {
        viewModel.observeOrganizationPersons(organizationId)
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowMessage -> {
                    Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT)
                        .show()
                }
                is UiEvent.ShowError -> {
                    Toast.makeText(context, event.error.asString(context), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                SectionTitle(title = stringResource(R.string.label_related_persons_list))
            }
            FilledTonalButton(
                onClick = {
                    viewModel.resetForm()
                    showAddPersonSheet = true
                },
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = appColors.success,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.label_add_person),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (persons.isEmpty()) {
            EmptyState(
                textRes = R.string.msg_no_person_found,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                itemsIndexed(
                    items = persons,
                    key = { _, person -> person.orgPersonId }
                ) { index, person ->
                    PersonOrganizationCard(
                        index = index,
                        person = person,
                        onPhoneClick = onPhoneClick
                    )
                }
            }
        }
    }

    if (showAddPersonSheet) {
        AddPersonBottomSheet(
            formState = formState,
            onNameChange = viewModel::updateFullName,
            onMobileChange = viewModel::updateMobile,
            onPhoneChange = viewModel::updatePhone,
            onGenderChange = viewModel::updateGender,
            onRoleChange = viewModel::updateRole,
            onDescriptionChange = viewModel::updateDescription,
            onCommissionEligibleChange = viewModel::updateCommissionEligible,
            onDismiss = {
                if (!formState.isSaving) {
                    showAddPersonSheet = false
                }
            },
            onSave = {
                viewModel.submitPerson(organizationId) {
                    showAddPersonSheet = false
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddPersonBottomSheet(
    formState: AddOrganizationPersonUiState,
    onNameChange: (String) -> Unit,
    onMobileChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onGenderChange: (Int?) -> Unit,
    onRoleChange: (Int) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onCommissionEligibleChange: (Boolean) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current
    val genderList = remember { PersonGenderType.entries }
    val roleList = remember { PersonOrganizationRole.entries }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = appColors.screenBackground,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .imePadding()
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = stringResource(R.string.label_add_related_person),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = appColors.textPrimary
                )

                CustomEditTextField(
                    value = formState.fullName,
                    onValueChange = onNameChange,
                    label = stringResource(R.string.label_person_full_name),
                    placeholder = stringResource(R.string.hint_enter_person_full_name),
                    isRequired = true,
                    leadingIcon = null
                )

                TwoColumnRow(
                    start = {
                        CustomEditTextField(
                            value = formState.mobile,
                            onValueChange = onMobileChange,
                            label = stringResource(R.string.label_mobile),
                            placeholder = stringResource(R.string.hint_enter_mobile),
                            isRequired = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            leadingIcon = null
                        )
                    },
                    end = {
                        CustomEditTextField(
                            value = formState.phone,
                            onValueChange = onPhoneChange,
                            label = stringResource(R.string.label_phone),
                            placeholder = stringResource(R.string.hint_enter_phone),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            isRequired = false,
                            leadingIcon = null
                        )
                    }
                )

                val genderItems = genderList.map { stringResource(it.titleRes) }
                DropdownSelectorField(
                    value = PersonGenderType.fromId(formState.gender)
                        ?.let { stringResource(it.titleRes) }.orEmpty(),
                    label = stringResource(R.string.label_gender),
                    placeholder = stringResource(R.string.hint_choose_gender),
                    items = genderItems,
                    isRequired = false,
                    onItemSelected = { genderTitle ->
                        val selectedIndex = genderItems.indexOf(genderTitle)
                        if (selectedIndex >= 0) {
                            onGenderChange(genderList[selectedIndex].id)
                        }
                    }
                )

                val roleItems = roleList.map { stringResource(it.titleRes) }
                DropdownSelectorField(
                    value = PersonOrganizationRole.fromId(formState.role)
                        ?.let { stringResource(it.titleRes) }.orEmpty(),
                    label = stringResource(R.string.label_organization_role),
                    placeholder = stringResource(R.string.hint_choose_organization_role),
                    items = roleItems,
                    isRequired = true,
                    onItemSelected = { roleTitle ->
                        val selectedIndex = roleItems.indexOf(roleTitle)
                        if (selectedIndex >= 0) {
                            onRoleChange(roleList[selectedIndex].id)
                        }
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.label_update_wallet_balance),
                        style = MaterialTheme.typography.bodyLarge,
                        color = appColors.textPrimary
                    )
                    Switch(
                        checked = formState.isCommissionEligible,
                        onCheckedChange = onCommissionEligibleChange
                    )
                }

                CustomDescriptionField(
                    label = stringResource(R.string.label_description),
                    value = formState.description,
                    onValueChange = onDescriptionChange,
                    placeholder = stringResource(R.string.hint_description)
                )
            }

            DualActionButtonBottomBar(
                onPrimaryClick = onSave,
                onSecondaryClick = onDismiss,
                primaryText = stringResource(R.string.label_add_person),
                secondaryText = stringResource(R.string.label_cancel),
                primaryIcon = null,
                secondaryIcon = null,
                isPrimaryLoading = formState.isSaving,
                isSecondaryLoading = false,
                isPrimaryEnabled = formState.isFormValid,
                isSecondaryEnabled = !formState.isSaving,
                containerColor = appColors.screenBackground,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TwoColumnRow(
    start: @Composable () -> Unit,
    end: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) { start() }
        Box(modifier = Modifier.weight(1f)) { end() }
    }
}

@Composable
private fun PersonOrganizationCard(
    index: Int,
    person: OrganizationPersonEntity,
    onPhoneClick: (String) -> Unit
) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme
    val isPendingSync = person.syncStatus != SyncStatus.SYNCED
    val primaryContact = person.mobile?.takeIf { it.isNotBlank() } ?: person.phone

    val genderEnum = PersonGenderType.fromId(person.gender)
    val roleEnum = PersonOrganizationRole.fromId(person.role)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(
            width = if (isPendingSync) 1.dp else 0.7.dp,
            color = if (isPendingSync) appColors.warning.copy(alpha = 0.65f) else appColors.border
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // ردیف هدر کارت: نام شخص و وضعیت رابطه با سازمان
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "${index + 1}. ${person.fullName}".trim(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = appColors.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusBadge(status = PersonOrganizationStatus.fromId(person.statusRelation))
                    }
                }

            }

            // باکس مشخصات فردی، نقش سازمانی و راه‌های ارتباطی
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(colors.surfaceVariant.copy(alpha = 0.35f))
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // نمایش نقش سازمانی
                roleEnum?.let { role ->
                    PersonInfoRow(
                        icon = Icons.Default.Badge,
                        title = stringResource(R.string.label_organization_role),
                        value = stringResource(role.titleRes)
                    )
                }

                // نمایش جنسیت
                genderEnum?.let { gender ->
                    PersonInfoRow(
                        icon = Icons.Default.Wc,
                        title = stringResource(R.string.label_gender),
                        value = stringResource(gender.titleRes)
                    )
                }

                // شماره موبایل
                person.mobile?.takeIf { it.isNotBlank() }?.let { mobile ->
                    PersonInfoRow(
                        icon = Icons.Default.Smartphone,
                        title = stringResource(R.string.label_mobile),
                        value = mobile
                    )
                }

                // تلفن ثابت
                person.phone?.takeIf { it.isNotBlank() }?.let { phone ->
                    PersonInfoRow(
                        icon = Icons.Default.Phone,
                        title = stringResource(R.string.label_phone),
                        value = phone
                    )
                }

                // توضیحات تکمیلی در صورت وجود
                person.description?.takeIf { it.isNotBlank() }?.let { desc ->
                    PersonInfoRow(
                        icon = Icons.Default.Notes,
                        title = stringResource(R.string.label_description),
                        value = desc,
                        isMultiLine = true
                    )
                }
            }

            // بنر وضعیت سینک آفلاین
            if (isPendingSync) {
                Spacer(modifier = Modifier.height(2.dp))
                OfflinePendingFooterBanner()
            }
        }
    }
}

@Composable
private fun PersonInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String,
    isMultiLine: Boolean = false
) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = if (isMultiLine) Alignment.Top else Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .padding(top = if (isMultiLine) 2.dp else 0.dp)
                .size(16.dp),
            tint = colors.primary
        )
        Text(
            text = "$title:",
            style = MaterialTheme.typography.labelMedium,
            color = appColors.textSecondary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = appColors.textPrimary,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start,
            maxLines = if (isMultiLine) 3 else 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
