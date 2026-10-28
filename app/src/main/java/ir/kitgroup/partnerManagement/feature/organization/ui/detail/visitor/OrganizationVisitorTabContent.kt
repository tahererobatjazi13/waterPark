package ir.kitgroup.partnerManagement.feature.organization.ui.detail.visitor

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.network.model.VisitorOrganizationDto
import ir.kitgroup.partnerManagement.core.ui.components.BouncingDotsIndicator
import ir.kitgroup.partnerManagement.core.ui.components.CustomButton
import ir.kitgroup.partnerManagement.core.ui.components.EmptyState
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.components.StatusBadge
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import ir.kitgroup.partnerManagement.core.ui.util.DataState
import ir.kitgroup.partnerManagement.core.ui.util.VisitorOrganizationStatus
import ir.kitgroup.partnerManagement.core.ui.util.formatJalaliDate
import ir.kitgroup.partnerManagement.core.ui.util.isInternetAvailable

@Composable
fun OrganizationVisitorTabContent(
    organizationVisitorViewModel: OrganizationVisitorViewModel = hiltViewModel(),
    onAssignVisitorClick: () -> Unit = {},
    onEditVisitorClick: (String) -> Unit = {},
    onDeleteVisitorClick: (String) -> Unit = {},
) {
    val context = LocalContext.current
    val appColors = LocalPartnerManagementColors.current
    val visitorsState by organizationVisitorViewModel.visitorsState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        val online = isInternetAvailable(context)
        organizationVisitorViewModel.ensureVisitorsLoaded(online)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // نوار عنوان بالای لیست به همراه دکمه تخصیص جدید
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                SectionTitle(title = stringResource(R.string.label_assignment_visitor_to_organization_list))
            }

  /*          FilledTonalButton(
                onClick = onAssignVisitorClick,
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
                    text = stringResource(R.string.label_new_assigning),
                    style = typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }*/
        }

        when (val state = visitorsState) {
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
                val errorMessage = state.error.asString(context)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = errorMessage,
                        style = typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    CustomButton(
                        text = stringResource(R.string.label_retry),
                        onClick = {
                            val online = isInternetAvailable(context)
                            organizationVisitorViewModel.retry(online)
                        }
                    )
                }
            }

            is DataState.Success -> {
                val visitors = state.data
                if (visitors.isEmpty()) {
                    EmptyState(
                        textRes = R.string.msg_no_visitor_found,
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
                            items = visitors,
                            key = { _, item -> item.orgVisitorId }
                        ) { index, visitor ->
                            VisitorOrganizationCard(
                                index = index,
                                visitor = visitor,
                                onEditClick = { onEditVisitorClick(visitor.orgVisitorId) },
                                onDeleteClick = { onDeleteVisitorClick(visitor.orgVisitorId) }
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
}

@Composable
private fun VisitorOrganizationCard(
    index: Int,
    visitor: VisitorOrganizationDto,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.cardBackground),
        border = BorderStroke(0.7.dp, appColors.border)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // ردیف اطلاعات اصلی: آواتار نمادین، نام و وضعیت
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // آواتار نمادین ویزیتور
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(colors.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = colors.primary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = "${index + 1}. ${visitor.name.orEmpty()}".trim(),
                        style = typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = appColors.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                StatusBadge(status = VisitorOrganizationStatus.fromId(visitor.statusRelation ?: 0))
            }

            // باکس بازه زمانی فعالیت (تاریخ شروع و پایان تفکیک‌شده در دو ستون تمیز)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(colors.surfaceVariant.copy(alpha = 0.35f))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DateInfoItem(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.label_start_date),
                    date = formatJalaliDate(visitor.dateStart),
                    icon = Icons.Default.CalendarMonth
                )

                DateInfoItem(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.label_end_date),
                    date = formatJalaliDate(visitor.dateEnd),
                    icon = Icons.Default.EventBusy
                )
            }

            // دکمه‌های عملیاتی (ویرایش / حذف)
        /*    Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = colors.primary.copy(alpha = 0.1f),
                    modifier = Modifier.size(36.dp)
                ) {
                    IconButton(onClick = onEditClick) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = stringResource(R.string.label_edit),
                            tint = colors.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = colors.error.copy(alpha = 0.1f),
                    modifier = Modifier.size(36.dp)
                ) {
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = stringResource(R.string.label_delete),
                            tint = colors.error,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }*/
        }
    }
}

@Composable
private fun DateInfoItem(
    label: String,
    date: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = colors.primary
        )
        Column {
            Text(
                text = label,
                style = typography.labelSmall,
                color = appColors.textSecondary
            )
            Text(
                text = date.ifBlank { "—" },
                style = typography.labelMedium,
                fontWeight = FontWeight.SemiBold,
                color = appColors.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
