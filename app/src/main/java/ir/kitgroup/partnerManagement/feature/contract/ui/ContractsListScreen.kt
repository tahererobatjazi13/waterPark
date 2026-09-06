package ir.kitgroup.partnerManagement.feature.contract.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.CustomHeader
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.util.Status
import ir.kitgroup.partnerManagement.feature.contract.model.ContractUi
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight

@Composable
fun ContractsListScreen(
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
    onContractClick: (ContractUi) -> Unit,
    contracts: List<ContractUi> = demoContracts()
) {
    val appColors = LocalPartnerManagementColors.current

    Scaffold(
        topBar = {
            CustomHeader(
                title = R.string.label_contract_organization,
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
                    .padding(16.dp)
            ) {
                ContractsHeader(onAddClick = onAddClick)

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = contracts,
                        key = { contract -> contract.id }
                    ) { contract ->
                        ContractListItem(
                            contract = contract,
                            onClick = { onContractClick(contract) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
 fun ContractListItem(
    contract: ContractUi,
    onClick: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = LocalPartnerManagementColors.current.cardBackground
        ),
        border = BorderStroke(
            width = 0.7.dp,
            color = LocalPartnerManagementColors.current.border
        )
    )
    {
        Column(modifier = Modifier.padding(14.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = contract.organizationName,
                    style = typography.titleLarge,
                    color = appColors.textPrimary,
                    modifier = Modifier.weight(1f)
                )

                StatusBadge(status = contract.status)

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = appColors.textSecondary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = contract.contractTitle,
                style = typography.labelMedium,
                color = appColors.textSecondary
            )

            Spacer(modifier = Modifier.height(6.dp))
            HorizontalDivider(
                thickness = 0.6.dp,
                color = LocalPartnerManagementColors.current.border
            )
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                ContractInfoItem(
                    label = stringResource(R.string.label_cooperation_model),
                    value = contract.cooperationModel,
                    modifier = Modifier.weight(1f)
                )

                ContractInfoItem(
                    label = stringResource(R.string.label_settlement_period_type),
                    value = contract.settlementPeriodType,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ContractInfoItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Column(
        modifier = modifier
    ) {
        Text(
            text = label,
            style = typography.labelSmall,
            color = appColors.textSecondary
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = value,
            style = typography.bodyMedium,
            color = appColors.textPrimary
        )
    }
}

@Composable
private fun ContractsHeader(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.label_contract_organization),
            style = typography.titleMedium,
            color = appColors.textPrimary
        )

        CustomButton(
            text = stringResource(R.string.label_new_contract),
            onClick = onAddClick,
            fillMaxWidth = false,
            height = 38.dp,
            textStyle = typography.titleLarge,
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            icon = Icons.Default.Add,
            colors = ButtonDefaults.buttonColors(
                containerColor = appColors.success,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}

fun demoContracts(): List<ContractUi> = listOf(
    ContractUi(
        id = "1",
        organizationName = "هتل اسپیناس پالاس",
        contractTitle = "قرارداد همکاری سال ۱۴۰۵",
        cooperationModel = "پورسانتی",
        settlementPeriodType = "ماهیانه",
        startDate = "1405/01/01",
        endDate = "1405/12/29",
        status = Status.ACTIVE,
        defaultDiscountPercent = 10.0,
        defaultCommissionPercent = 5.0,
        description = "توافق بر اساس نرخ‌نامه رسمی هتل با ۱۰٪ تخفیف برای مشتریان سازمانی."
    ),
    ContractUi(
        id = "2",
        organizationName = "هتل پارسیان آزادی",
        contractTitle = "قرارداد همکاری فصلی",
        cooperationModel = "بلیط تخفیف دار",
        settlementPeriodType = "روزانه",
        startDate = "1405/03/01",
        endDate = "1405/05/31",
        status = Status.ACTIVE,
        defaultDiscountPercent = 15.0,
        defaultCommissionPercent = 7.5,
        description = "پورسانت بر اساس درصد فروش هر فصل محاسبه و تسویه می‌شود."
    ),
    ContractUi(
        id = "3",
        organizationName = "هتل هما شیراز",
        contractTitle = "قرارداد همکاری شش‌ماهه",
        cooperationModel = "پورسانت و تخفیف",
        settlementPeriodType = "روزانه",
        startDate = "1404/10/01",
        endDate = "1405/03/31",
        status = Status.ACTIVE,
        defaultDiscountPercent = 12.0,
        defaultCommissionPercent = 6.0,
        description = "تمدید خودکار قرارداد در صورت تحقق سقف فروش تعیین‌شده."
    ),
    ContractUi(
        id = "4",
        organizationName = "هتل بزرگ تهران",
        contractTitle = "قرارداد همکاری سالانه",
        cooperationModel = "بلیط تخفیف دار",
        settlementPeriodType = "ماهانه",
        startDate = "1404/06/01",
        endDate = "1405/05/31",
        status = Status.INACTIVE,
        defaultDiscountPercent = 8.0,
        defaultCommissionPercent = 4.0,
        description = "قرارداد به‌دلیل عدم تمدید در پایان دوره، غیرفعال شده است."
    ),
    ContractUi(
        id = "5",
        organizationName = "هتل پردیس کیش",
        contractTitle = "قرارداد همکاری نوروز ۱۴۰۵",
        cooperationModel = "پورسانت و تخفیف",
        settlementPeriodType = "ماهانه",
        startDate = "1404/12/15",
        endDate = "1405/02/15",
        status = Status.INACTIVE,
        defaultDiscountPercent = 20.0,
        defaultCommissionPercent = 10.0,
        description = "پروژه ویژه نوروز با تخفیف پلکانی برای رزروهای گروهی."
    ),
    ContractUi(
        id = "6",
        organizationName = "هتل آسمان اصفهان",
        contractTitle = "قرارداد همکاری تابستانه",
        cooperationModel = "بلیط تخفیف دار",
        settlementPeriodType = "هفتگی",
        startDate = "1405/04/01",
        endDate = "1405/06/31",
        status = Status.ACTIVE,
        defaultDiscountPercent = 10.0,
        defaultCommissionPercent = 5.0,
        description = "تخفیف ۱۰٪ برای رزروهای بالای ۵ شب."
    )
)
