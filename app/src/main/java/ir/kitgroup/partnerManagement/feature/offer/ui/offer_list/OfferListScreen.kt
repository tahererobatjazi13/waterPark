package ir.kitgroup.partnerManagement.feature.offer.ui.offer_list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.database.entity.OfferEntity
import ir.kitgroup.partnerManagement.core.ui.SessionViewModel
import ir.kitgroup.partnerManagement.core.ui.components.EmptyState
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.OfferPlanStatus
import ir.kitgroup.partnerManagement.core.ui.util.UiEvent
import ir.kitgroup.partnerManagement.core.ui.util.UserRole
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate

@Composable
fun OfferListScreen(
    onAddClick: () -> Unit,
    onOfferClick: (OfferEntity) -> Unit,
    viewModel: OfferListViewModel = hiltViewModel(),
    sessionViewModel: SessionViewModel = hiltViewModel()
) {
    val offers by viewModel.offers.collectAsStateWithLifecycle()
    val appColors = LocalPartnerManagementColors.current
    val roleCode by sessionViewModel.roleCode.collectAsState()

    val currentRole = remember(roleCode) {
        UserRole.fromCode(roleCode.toString())
    }

    val isSupervisor = currentRole == UserRole.SUPERVISOR

    val context = androidx.compose.ui.platform.LocalContext.current

    val snackbarHostState =
        remember { SnackbarHostState() }

    LaunchedEffect(viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowMessage -> {
                    snackbarHostState.showSnackbar(
                        event.message.asString(context)
                    )
                }

                is UiEvent.ShowError -> {
                    snackbarHostState.showSnackbar(
                        event.error.asString(context)
                    )
                }
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        floatingActionButton = {
            if (isSupervisor) {
                ExtendedFloatingActionButton(
                    onClick = {
                        viewModel.checkCanNavigateToAddOffer {
                            onAddClick()
                        }
                    },
                    containerColor = appColors.success,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(52.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = stringResource(R.string.label_new_plan),
                            style = typography.titleLarge
                        )
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp
            ),
            color =
            MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 4.dp
                    )
            ) {

                if (offers.isEmpty()) {

                    EmptyState(
                        textRes = R.string.msg_no_item_found,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )

                } else {

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(
                            vertical = 8.dp
                        )
                    ) {
                        items(
                            items = offers,
                            key = { plan ->
                                plan.offerId
                            }
                        ) { offer ->

                            OfferListItem(
                                offer = offer,
                                onClick = {
                                    onOfferClick(offer)
                                }
                            )
                        }

                        // فاصله برای اینکه آخرین کارت
                        // زیر FAB قرار نگیرد
                        item {
                            Spacer(
                                modifier = Modifier.height(60.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OfferListItem(
    offer: OfferEntity,
    onClick: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColors.cardBackground
        ),
        border = BorderStroke(
            width = 0.8.dp,
            color = appColors.border.copy(alpha = 0.8f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = offer.title ?: "-",
                    style = typography.titleLarge,
                    color = appColors.textPrimary,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                StatusBadge(
                    status = OfferPlanStatus.fromId(offer.statusOffer)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = appColors.textTertiary,
                    modifier = Modifier.size(20.dp)
                )
            }

            offer.code
                ?.takeIf { it.isNotBlank() }
                ?.let { code ->

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.label_offer_code) + ":",
                            style = typography.bodySmall,
                            color = appColors.textSecondary
                        )

                        Text(
                            text = code,
                            style = typography.labelMedium,
                            color = appColors.textPrimary
                        )
                    }

                    HorizontalDivider(
                        color = appColors.border.copy(alpha = 0.5f),
                        thickness = 0.6.dp
                    )
                }

            PlanDateRangeDisplay(
                fromDate = formatJalaliDate(offer.fromDate),
                toDate = formatJalaliDate(offer.toDate)
            )
        }
    }
}

@Composable
private fun PlanDateRangeDisplay(
    fromDate: String,
    toDate: String,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.DateRange,
                contentDescription = null,
                tint = appColors.textSecondary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = stringResource(R.string.label_validity_period),
                style = typography.labelMedium,
                color = appColors.textSecondary
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = fromDate,
                style = typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "تا",
                style = typography.labelLarge,
                color = appColors.textTertiary
            )
            Text(
                text = toDate,
                style = typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
