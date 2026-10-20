package ir.kitgroup.partnerManagement.feature.organization.ui.detail.ticket

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.network.model.TicketIssueLineDto
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.EmptyState
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.DataState
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate
import java.text.DecimalFormat

@Composable
fun OrganizationTicketIssueTabContent(
    ticketIssueState: DataState<List<TicketIssueLineDto>>,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTicketLine by remember { mutableStateOf<TicketIssueLineDto?>(null) }

    Column(
        modifier = modifier
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
                SectionTitle(title = "لیست بلیط‌های صادر شده")
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
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            is DataState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "خطا در دریافت لیست بلیط‌ها",
                        style = typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    CustomButton(
                        text = "تلاش مجدد",
                        onClick = onRetry
                    )
                }
            }

            is DataState.Success -> {
                val tickets = ticketIssueState.data
                if (tickets.isEmpty()) {
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
                        contentPadding = PaddingValues(bottom = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        itemsIndexed(
                            items = tickets,
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
                            text = "${index + 1}. ${ticket.finalProductServiceName ?: ticket.productServiceName ?: "بدون عنوان"}",
                            style = typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = appColors.textPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        ticket.serialNumber?.let { serial ->
                            Text(
                                text = "سریال: $serial",
                                style = typography.labelSmall,
                                color = appColors.textSecondary
                            )
                        }
                    }
                }

                // بج وضعیت مصرف
                TicketStatusBadge(
                    statusName = ticket.ticketStatusName ?: ticket.statusUsageName ?: "نامشخص",
                    isSuccess = ticket.ticketStatus == 1 || ticket.statusUsage == 1
                )
            }

            HorizontalDivider(thickness = 0.5.dp, color = appColors.border)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TicketInfoCompactItem(
                    label = "تعداد کل / مصرفی:",
                    value = "${ticket.count ?: 0} / ${ticket.realCount ?: 0}"
                )

                TicketInfoCompactItem(
                    label = "مبلغ کل:",
                    value = "${formatPrice(ticket.sumPrice)} ریال"
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TicketInfoCompactItem(
                    label = "تاریخ مصرف:",
                    value = formatJalaliDate(ticket.usageDate)
                )

                TicketInfoCompactItem(
                    label = "ساعت:",
                    value = ticket.useTime ?: "-"
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
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
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
                            text = "جزئیات بلیط صادر شده",
                            style = typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = appColors.textPrimary
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "بستن",
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
                    DetailRowItem(label = "عنوان سرویس/کالا", value = ticket.finalProductServiceName ?: ticket.productServiceName ?: "-")
                    DetailRowItem(label = "شماره سریال", value = ticket.serialNumber ?: "-")
                    DetailRowItem(label = "شماره صدور", value = ticket.issueNumber ?: "-")
                    DetailRowItem(label = "مشتری", value = ticket.customerName ?: "-")
                    DetailRowItem(label = "وضعیت بلیط", value = ticket.ticketStatusName ?: "-")
                    DetailRowItem(label = "وضعیت مصرف", value = ticket.statusUsageName ?: "-")
                    DetailRowItem(label = "تاریخ و زمان مصرف", value = "${formatJalaliDate(ticket.usageDate)} - ساعت ${ticket.useTime ?: "-"}")
                    DetailRowItem(label = "تاریخ تایید", value = formatJalaliDate(ticket.approvedDate))
                    DetailRowItem(label = "تعداد کل", value = ticket.count?.toString() ?: "0")
                    DetailRowItem(label = "تعداد واقعی مصرف شده", value = ticket.realCount?.toString() ?: "0")

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), thickness = 0.5.dp, color = appColors.border)

                    DetailRowItem(label = "قیمت واحد", value = "${formatPrice(ticket.unitPrice)} ریال")
                    DetailRowItem(label = "مبلغ تخفیف", value = "${formatPrice(ticket.discountPrice)} ریال (${ticket.discountAmount ?: 0}%)")
                    DetailRowItem(label = "قیمت خالص", value = "${formatPrice(ticket.netPrice)} ریال")
                    DetailRowItem(label = "جمع کل پرداختی", value = "${formatPrice(ticket.sumPrice)} ریال", isHighlighted = true)

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), thickness = 0.5.dp, color = appColors.border)

                    DetailRowItem(label = "مبلغ پورسانت واحد", value = "${formatPrice(ticket.commissionAmount)} ریال")
                    DetailRowItem(label = "پورسانت کل", value = "${formatPrice(ticket.totalCommission)} ریال")
                }

                Spacer(modifier = Modifier.height(16.dp))

                CustomButton(
                    text = "بستن",
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                )
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

@Composable
private fun TicketStatusBadge(
    statusName: String,
    isSuccess: Boolean
) {
    val appColors = LocalPartnerManagementColors.current
    val bgColor = if (isSuccess) appColors.successContainer else appColors.warningContainer
    val contentColor = if (isSuccess) appColors.onSuccessContainer else appColors.onWarningContainer

    Box(
        modifier = Modifier
            .background(color = bgColor, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = statusName,
            style = typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = contentColor
        )
    }
}

private fun formatPrice(price: Long?): String {
    if (price == null) return "0"
    return try {
        DecimalFormat("#,###").format(price)
    } catch (_: Exception) {
        price.toString()
    }
}
