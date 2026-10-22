package ir.kitgroup.partnerManagement.feature.organization.ui.detail.ticket

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.network.model.TicketIssueLineDto
import ir.kitgroup.partnerManagement.core.ui.components.BouncingDotsIndicator
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.EmptyState
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.DataState
import ir.kitgroup.partnerManagement.core.ui.util.TicketStatus
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate
import ir.kitgroup.partnerManagement.core.ui.util.formatPrice
import ir.kitgroup.partnerManagement.core.ui.util.isInternetAvailable

@Composable
fun OrganizationTicketIssueTabContent(
    organizationTicketIssueViewModel: OrganizationTicketIssueViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val ticketIssueState by organizationTicketIssueViewModel.ticketIssueState.collectAsStateWithLifecycle()
    var selectedTicketLine by remember { mutableStateOf<TicketIssueLineDto?>(null) }

    LaunchedEffect(Unit) {
        val online = isInternetAvailable(context)
        organizationTicketIssueViewModel.ensureTicketIssuesLoaded(online)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                SectionTitle(title = stringResource(R.string.label_ticket_issue_list))
            }
        }

        when (ticketIssueState) {
            is DataState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    BouncingDotsIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            is DataState.Error -> {
                val errorMessage = (ticketIssueState as DataState.Error).error.asString(context)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = errorMessage,
                        style = typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomButton(
                        text = stringResource(R.string.label_retry),
                        onClick = {
                            val online = isInternetAvailable(context)
                            organizationTicketIssueViewModel.retry(online)
                        }
                    )
                }
            }

            is DataState.Success -> {
                val ticketIssues = (ticketIssueState as DataState.Success).data
                if (ticketIssues.isEmpty()) {
                    EmptyState(
                        textRes = R.string.msg_no_item_found,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        itemsIndexed(
                            items = ticketIssues,
                            key = { _, item -> item.ticketIssueLineId }
                        ) { index, item ->
                            TicketIssueLineCard(
                                index = index,
                                ticket = item,
                                onClick = { selectedTicketLine = item }
                            )
                        }
                    }
                }
            }

            is DataState.Idle -> {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }

    // دیالوگ نمایش جزئیات کامل
    selectedTicketLine?.let { ticket ->
        TicketIssueLineDetailDialog(
            ticket = ticket,
            onDismiss = { selectedTicketLine = null }
        )
    }
}


@Composable
private fun TicketIssueLineCard(
    index: Int,
    ticket: TicketIssueLineDto,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(0.6.dp, appColors.border)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ConfirmationNumber,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column {
                        Text(
                            text = stringResource(
                                R.string.label_ticket_product_title,
                                index + 1,
                                ticket.finalProductServiceName
                                    ?: ticket.productServiceName
                                    ?: stringResource(R.string.label_untitled)
                            ),
                            style = typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = appColors.textPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        ticket.serialNumber?.let { serial ->
                            Text(
                                text = stringResource(R.string.label_serial_number_value, serial),
                                style = typography.labelSmall,
                                color = appColors.textSecondary
                            )
                        }
                    }
                }

                // بج وضعیت مصرف
                StatusBadge(status = TicketStatus.fromId(ticket.ticketStatus ?: 0))


            }

            HorizontalDivider(thickness = 0.5.dp, color = appColors.border)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TicketInfoCompactItem(
                    label = stringResource(R.string.label_total_used_count),
                    value = "${ticket.count ?: 0} / ${ticket.realCount ?: 0}"
                )

                TicketInfoCompactItem(
                    label = stringResource(R.string.label_total_amount),
                    value = stringResource(
                        R.string.label_price_with_currency,
                        formatPrice(ticket.sumPrice)
                    )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TicketInfoCompactItem(
                    label = stringResource(R.string.label_usage_date),
                    value = formatJalaliDate(ticket.usageDate)
                )

                TicketInfoCompactItem(
                    label = stringResource(R.string.label_time),
                    value = ticket.useTime ?: stringResource(R.string.label_not_available)
                )
            }
        }
    }
}

@Composable
private fun TicketIssueLineDetailDialog(
    ticket: TicketIssueLineDto,
    onDismiss: () -> Unit
) {
    val appColors = LocalPartnerManagementColors.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .padding(vertical = 24.dp),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.background,
            tonalElevation = 6.dp
        ) {
            CompositionLocalProvider(
                LocalLayoutDirection provides LayoutDirection.Rtl
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    // هدر دیالوگ
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = stringResource(R.string.title_ticket_issue_details),
                                style = typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = appColors.textPrimary
                            )
                        }

                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.label_close),
                                tint = appColors.textSecondary
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 10.dp),
                        thickness = 0.8.dp,
                        color = appColors.border
                    )

                    // محتوای اسکرول‌پذیر جزئیات
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        DetailRowItem(
                            label = stringResource(R.string.label_product_service_title),
                            value = ticket.finalProductServiceName ?: ticket.productServiceName
                            ?: "-"
                        )
                        DetailRowItem(
                            label = stringResource(R.string.label_serial_number),
                            value = ticket.serialNumber
                                ?: stringResource(R.string.label_not_available)
                        )
                        DetailRowItem(
                            label = stringResource(R.string.label_issue_number),
                            value = ticket.issueNumber
                                ?: stringResource(R.string.label_not_available)
                        )
                        DetailRowItem(
                            label = stringResource(R.string.label_customer),
                            value = ticket.customerName
                                ?: stringResource(R.string.label_not_available)
                        )
                        DetailRowItem(
                            label = stringResource(R.string.label_ticket_status),
                            value = ticket.ticketStatusName
                                ?: stringResource(R.string.label_not_available)
                        )
                        DetailRowItem(
                            label = stringResource(R.string.label_usage_status),
                            value = ticket.statusUsageName
                                ?: stringResource(R.string.label_not_available)
                        )
                        DetailRowItem(
                            label = stringResource(R.string.label_usage_datetime),
                            value = stringResource(
                                R.string.label_datetime_value,
                                formatJalaliDate(ticket.usageDate),
                                ticket.useTime ?: "—"
                            )
                        )
                        DetailRowItem(
                            label = stringResource(R.string.label_approval_date),
                            value = formatJalaliDate(ticket.approvedDate)
                        )
                        DetailRowItem(
                            label = stringResource(R.string.label_total_count),
                            value = ticket.count?.toString() ?: stringResource(R.string.label_zero)
                        )
                        DetailRowItem(
                            label = stringResource(R.string.label_actual_used_count),
                            value = ticket.realCount?.toString()
                                ?: stringResource(R.string.label_zero)
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 4.dp),
                            thickness = 0.5.dp,
                            color = appColors.border
                        )

                        DetailRowItem(
                            label = stringResource(R.string.label_unit_price),
                            value = stringResource(
                                R.string.label_price_with_currency,
                                formatPrice(ticket.unitPrice)
                            )
                        )
                        DetailRowItem(
                            label = stringResource(R.string.label_discount_amount),
                            value = stringResource(
                                R.string.label_discount_value,
                                formatPrice(ticket.discountPrice),
                                (ticket.discountAmount ?: 0).toString()
                            )
                        )
                        DetailRowItem(
                            label = stringResource(R.string.label_net_price),
                            value = stringResource(
                                R.string.label_price_with_currency,
                                formatPrice(ticket.netPrice)
                            )
                        )
                        DetailRowItem(
                            label = stringResource(R.string.label_total_paid),
                            value = stringResource(
                                R.string.label_price_with_currency,
                                formatPrice(ticket.sumPrice)
                            ),
                            isHighlighted = true
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 4.dp),
                            thickness = 0.5.dp,
                            color = appColors.border
                        )

                        DetailRowItem(
                            label = stringResource(R.string.label_unit_commission),
                            value = stringResource(
                                R.string.label_price_with_currency,
                                formatPrice(ticket.commissionAmount)
                            )
                        )
                        DetailRowItem(
                            label = stringResource(R.string.label_total_commission),
                            value = stringResource(
                                R.string.label_price_with_currency,
                                formatPrice(ticket.totalCommission)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomButton(
                        text = stringResource(R.string.label_close),
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailRowItem(
    label: String,
    value: String,
    isHighlighted: Boolean = false
) {
    val appColors = LocalPartnerManagementColors.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = typography.labelMedium,
            color = appColors.textSecondary
        )
        Text(
            text = value,
            style = if (isHighlighted) typography.titleSmall.copy(fontWeight = FontWeight.Bold) else typography.bodyMedium,
            color = if (isHighlighted) MaterialTheme.colorScheme.primary else appColors.textPrimary
        )
    }
}

@Composable
private fun TicketInfoCompactItem(
    label: String,
    value: String
) {
    val appColors = LocalPartnerManagementColors.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = typography.labelSmall,
            color = appColors.textSecondary
        )
        Text(
            text = value,
            style = typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = appColors.textPrimary
        )
    }
}


