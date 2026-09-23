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
import ir.kitgroup.partnerManagement.feature.organization.ui.SerialsDetailBottomSheet

@Composable
fun OrganizationGeneralInfoTabContent(organization: OrganizationEntity) {
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
    organization: OrganizationEntity,
    totalWarningsCount: Int = 3,
    totalScore: Int = 85,
    activeMarketersCount: Int = 6,
    assignedStandsCount: Int = 5,
    completedVisitsCount: Int = 12,
    plannedVisitsCount: Int = 4,
    totalAssignedSerials: Int = 1000,
    issuedSerialsCount: Int = 800,
    usedSerialsCount: Int = 620,
    revokedSerialsCount: Int = 30
) {
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme

    val remainingSerialsCount =
        (totalAssignedSerials - usedSerialsCount - revokedSerialsCount).coerceAtLeast(0)
    val usedProgress =
        if (totalAssignedSerials > 0) usedSerialsCount.toFloat() / totalAssignedSerials else 0f
    var showSerialsDetailSheet by rememberSaveable { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        // ================= ردیف اول: تجمیع تذکرات و تجمیع امتیاز =================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // تجمیع تذکرات
            StatMetricCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_total_warnings),
                count = totalWarningsCount,
                unit = stringResource(R.string.label_warnings_unit),
                icon = Icons.Outlined.WarningAmber,
                accentColor = if (totalWarningsCount > 0) appColors.warning else appColors.success,
                containerColor = if (totalWarningsCount > 0) appColors.warning.copy(alpha = 0.06f) else colors.surface
            )

            // تجمیع امتیاز
            StatMetricCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_total_score),
                count = totalScore,
                unit = stringResource(R.string.label_score_unit),
                icon = Icons.Outlined.Stars,
                accentColor = if (totalScore >= 0) appColors.success else colors.error,
                containerColor = if (totalScore >= 0) appColors.success.copy(alpha = 0.06f) else colors.error.copy(
                    alpha = 0.06f
                )
            )
        }

        // ================= ردیف دوم: بازاریابان فعال و استندهای اختصاص‌یافته =================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // بازاریابان فعال
            StatMetricCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_active_marketers),
                count = activeMarketersCount,
                unit = stringResource(R.string.label_marketers_unit),
                icon = Icons.Outlined.Group,
                accentColor = Color(0xFF0288D1),
                containerColor = Color(0xFF0288D1).copy(alpha = 0.06f)
            )

            // استندهای اختصاص‌یافته
            StatMetricCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_assigned_stands),
                count = assignedStandsCount,
                unit = stringResource(R.string.label_stands_unit),
                icon = Icons.Filled.Storefront,
                accentColor = colors.primary,
                containerColor = colors.primary.copy(alpha = 0.06f)
            )
        }

        // ================= ردیف دوم: استندها + بازدیدهای انجام‌شده + برنامه‌ریزی‌شده =================
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            MiniMetricCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_completed_visits),
                count = completedVisitsCount,
                unit = stringResource(R.string.label_visits_unit),
                icon = Icons.Outlined.TaskAlt,
                accentColor = appColors.success
            )

            MiniMetricCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.label_planned_visits),
                count = plannedVisitsCount,
                unit = stringResource(R.string.label_visits_unit),
                icon = Icons.Outlined.CalendarMonth,
                accentColor = Color(0xFF0288D1)
            )
        }

        Spacer(Modifier.height(4.dp))

        // ================= کادر اصلی اختصاصی سریال‌ها (مینیمال و حرفه‌ای با دکمه جزئیات) =================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(
                containerColor = colors.primary.copy(alpha = 0.04f)
            ),
            border = BorderStroke(1.dp, colors.primary.copy(alpha = 0.2f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // هدر کادر سریال
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
                                .background(colors.primary.copy(alpha = 0.12f)),
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
                                text = stringResource(R.string.label_assigned_serials),
                                style = typography.titleMedium,
                                color = colors.onSurface
                            )
                            Text(
                                text = "$totalAssignedSerials ${stringResource(R.string.label_unit_count)}",
                                style = typography.labelSmall,
                                color = colors.primary,
                            )
                        }
                    }

                    // دکمه باز شدن باتم‌شیت
                    TextButton(
                        onClick = { showSerialsDetailSheet = true },
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.label_action_view_details),
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

                // نوار درصد پیشرفت مصرف سریال‌ها
                LinearProgressIndicator(
                    progress = { usedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = colors.primary,
                    trackColor = colors.outlineVariant.copy(alpha = 0.3f)
                )

                // ردیف خلاصه: مصرف‌شده و باقی‌مانده
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(colors.primary)
                        )
                        Text(
                            text = stringResource(R.string.label_serials_used) + ":",
                            style = typography.bodySmall,
                            color = appColors.textSecondary
                        )
                        Text(
                            text = "$usedSerialsCount",
                            style = typography.titleSmall,
                            color = colors.onSurface
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(appColors.warning)
                        )
                        Text(
                            text = stringResource(R.string.label_serials_remaining) + ":",
                            style = typography.bodySmall,
                            color = appColors.textSecondary
                        )
                        Text(
                            text = "$remainingSerialsCount",
                            style = typography.titleSmall,
                            color = appColors.warning
                        )
                    }
                }
            }
        }


        // باز شدن باتم‌شیت جزئیات سریال‌ها در صورت کلیک
        if (showSerialsDetailSheet) {
            SerialsDetailBottomSheet(
                totalAssigned = totalAssignedSerials,
                issuedCount = issuedSerialsCount,
                usedCount = usedSerialsCount,
                revokedCount = revokedSerialsCount,
                remainingCount = remainingSerialsCount,
                onDismiss = { showSerialsDetailSheet = false }
            )
        }
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

