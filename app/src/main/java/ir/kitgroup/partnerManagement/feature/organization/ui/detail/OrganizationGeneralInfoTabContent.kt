package ir.kitgroup.partnerManagement.feature.organization.ui.detail


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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.components.SectionTitle
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Stars
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import ir.kitgroup.partnerManagement.core.database.entity.OrganizationEntity
import ir.kitgroup.partnerManagement.core.database.model.OrganizationWithDetail
import ir.kitgroup.partnerManagement.feature.organization.ui.SerialsDetailBottomSheet

@Composable
fun OrganizationGeneralInfoTabContent(organization: OrganizationWithDetail) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SectionTitle(stringResource(R.string.label_organization_statistical_information))
        BasicGeneralSection(organization)
    }
}

@Composable
private fun BasicGeneralSection(
    item: OrganizationWithDetail
) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme

    // -----------------------------
    // Organization statistics
    // -----------------------------
    val warningCount = item.organization.countWarning ?: 0
    val totalScore = item.organization.sumScore ?: 0
    val activeMarketers = item.organization.countVisitorActive ?: 0
    val assignedStands = item.organization.countStandAssign ?: 0

    val completedVisits = item.organization.visitCount ?: 0
    val plannedVisits = item.organization.programingVisitCount ?: 0

    // -----------------------------
    // Serial statistics
    // -----------------------------
    val assignedSerials = item.organization.assignedSerialCount ?: 0
    val usedSerials = item.organization.usingSerialCount ?: 0
    val issuedSerials = item.organization.issuedSerialCount ?: 0
    val cancelledSerials = item.organization.cancelledSerialCount ?: 0
    val remainingSerials = item.organization.remainingSerialCount ?: 0

    val usedProgress = if (assignedSerials > 0) {
        (usedSerials.toFloat() / assignedSerials.toFloat())
            .coerceIn(0f, 1f)
    } else {
        0f
    }

    var showSerialsDetailSheet by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // =========================
        // تذکرات و امتیاز
        // =========================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatMetricCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_total_warnings),
                count = warningCount,
                unit = stringResource(R.string.label_warnings_unit),
                icon = Icons.Outlined.WarningAmber,
                accentColor = if (warningCount > 0) {
                    appColors.warning
                } else {
                    appColors.success
                },
                containerColor = if (warningCount > 0) {
                    appColors.warning.copy(alpha = 0.06f)
                } else {
                    colors.surface
                }
            )

            StatMetricCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_total_score),
                count = totalScore,
                unit = stringResource(R.string.label_score_unit),
                icon = Icons.Outlined.Stars,
                accentColor = if (totalScore >= 0) {
                    appColors.success
                } else {
                    colors.error
                },
                containerColor = if (totalScore >= 0) {
                    appColors.success.copy(alpha = 0.06f)
                } else {
                    colors.error.copy(alpha = 0.06f)
                }
            )
        }

        // =========================
        // بازاریابان و استندها
        // =========================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatMetricCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_active_marketers),
                count = activeMarketers,
                unit = stringResource(R.string.label_marketers_unit),
                icon = Icons.Outlined.Group,
                accentColor = appColors.info,
                containerColor = appColors.info.copy(alpha = 0.06f)
            )

            StatMetricCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_assigned_stands),
                count = assignedStands,
                unit = stringResource(R.string.label_stands_unit),
                icon = Icons.Filled.Storefront,
                accentColor = appColors.purple,
                containerColor = appColors.purple.copy(alpha = 0.06f)
            )
        }

        // =========================
        // بازدیدها
        // =========================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MiniMetricCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_completed_visits),
                count = completedVisits,
                unit = stringResource(R.string.label_visits_unit),
                icon = Icons.Outlined.TaskAlt,
                accentColor = appColors.success
            )

            MiniMetricCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_planned_visits),
                count = plannedVisits,
                unit = stringResource(R.string.label_visits_unit),
                icon = Icons.Outlined.CalendarMonth,
                accentColor = Color(0xFF0288D1)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // =========================
        // سریال‌ها
        // =========================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = colors.primary.copy(alpha = 0.04f)
            ),
            border = BorderStroke(
                width = 1.dp,
                color = colors.primary.copy(alpha = 0.2f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    colors.primary.copy(alpha = 0.12f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ConfirmationNumber,
                                contentDescription = null,
                                tint = colors.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column {
                            Text(
                                text = stringResource(
                                    R.string.label_assigned_serials
                                ),
                                style = typography.titleMedium,
                                color = colors.onSurface
                            )

                            Text(
                                text = "$assignedSerials ${
                                    stringResource(R.string.label_unit_count)
                                }",
                                style = typography.labelSmall,
                                color = colors.primary
                            )
                        }
                    }

                    TextButton(
                        onClick = {
                            showSerialsDetailSheet = true
                        },
                        contentPadding = PaddingValues(
                            horizontal = 10.dp,
                            vertical = 4.dp
                        )
                    ) {
                        Text(
                            text = stringResource(
                                R.string.label_action_view_details
                            ),
                            style = typography.titleSmall,
                            color = colors.primary
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = colors.primary
                        )
                    }
                }

                // Progress
                LinearProgressIndicator(
                    progress = { usedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = colors.primary,
                    trackColor = colors.outlineVariant.copy(alpha = 0.3f)
                )

                // Used / Remaining
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    SerialSummaryItem(
                        label = stringResource(R.string.label_serials_used),
                        value = usedSerials,
                        color = colors.primary
                    )

                    SerialSummaryItem(
                        label = stringResource(R.string.label_serials_remaining),
                        value = remainingSerials,
                        color = appColors.warning
                    )
                }
            }
        }

        // =========================
        // Serial details
        // =========================
        if (showSerialsDetailSheet) {
            SerialsDetailBottomSheet(
                totalAssigned = assignedSerials,
                issuedCount = issuedSerials,
                usedCount = usedSerials,
                cancelledCount = cancelledSerials,
                remainingCount = remainingSerials,
                onDismiss = {
                    showSerialsDetailSheet = false
                }
            )
        }
    }
}

@Composable
private fun SerialSummaryItem(
    label: String,
    value: Int,
    color: Color
) {
    val colors = MaterialTheme.colorScheme
    val appColors = LocalPartnerManagementColors.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )

        Text(
            text = "$label:",
            style = typography.bodySmall,
            color = appColors.textSecondary
        )

        Text(
            text = value.toString(),
            style = typography.titleSmall,
            color = if (color == appColors.warning) {
                appColors.warning
            } else {
                colors.onSurface
            }
        )
    }
}

/**
 * مینی کارت متریک برای نمایش ۳ تایی فشرده و متوازن
 */
@Composable
private fun MiniMetricCard(
    title: String,
    count: Int,
    unit: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = "$count",
                style = typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = title,
                style = typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Composable
fun StatMetricCard(
    title: String,
    count: Int,
    unit: String,
    icon: ImageVector,
    accentColor: Color,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = containerColor,
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.25f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = title,
                    style = typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )

                Spacer(Modifier.height(2.dp))

                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "$count",
                        style = typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = accentColor
                    )
                    Text(
                        text = unit,
                        style = typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 3.dp)
                    )
                }
            }
        }
    }
}

