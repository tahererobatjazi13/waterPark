package ir.kitgroup.partnerManagement.feature.organization.ui

import androidx.compose.foundation.background
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.HourglassBottom
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SerialsDetailBottomSheet(
    totalAssigned: Int,
    issuedCount: Int,
    usedCount: Int,
    revokedCount: Int,
    remainingCount: Int,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val appColors = LocalPartnerManagementColors.current
    val colors = MaterialTheme.colorScheme

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = colors.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // سربرگ شیت
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = stringResource(R.string.label_serials_detailed_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = colors.onSurface
                    )
                    Text(
                        text = stringResource(R.string.label_serials_detailed_desc),
                        style = MaterialTheme.typography.bodySmall,
                        color = appColors.textSecondary
                    )
                }

                FilledTonalButton(
                    onClick = onDismiss,
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = stringResource(R.string.action_close))
                }
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                color = colors.outlineVariant.copy(alpha = 0.5f)
            )

            // لیست آماری
            SerialDetailRow(
                title = stringResource(R.string.label_assigned_serials),
                count = totalAssigned,
                total = totalAssigned,
                color = colors.primary,
                icon = Icons.Outlined.ConfirmationNumber
            )

            SerialDetailRow(
                title = stringResource(R.string.label_serials_issued),
                count = issuedCount,
                total = totalAssigned,
                color = Color(0xFF0288D1),
                icon = Icons.Outlined.Assignment
            )

            SerialDetailRow(
                title = stringResource(R.string.label_serials_used),
                count = usedCount,
                total = totalAssigned,
                color = appColors.success,
                icon = Icons.Outlined.CheckCircle
            )

            SerialDetailRow(
                title = stringResource(R.string.label_serials_revoked),
                count = revokedCount,
                total = totalAssigned,
                color = colors.error,
                icon = Icons.Outlined.Cancel
            )

            SerialDetailRow(
                title = stringResource(R.string.label_serials_remaining),
                count = remainingCount,
                total = totalAssigned,
                color = Color(0xFFF57C00),
                icon = Icons.Outlined.HourglassBottom
            )
        }
    }
}
/**
 * آیتم لیست جزئیات سریال‌ها در باتم‌شیت
 */
@Composable
fun SerialDetailRow(
    title: String,
    count: Int,
    total: Int,
    color: Color,
    icon: ImageVector
) {
    val percentage = if (total > 0) ((count.toFloat() / total) * 100).toInt() else 0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(color.copy(alpha = 0.05f))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
            }
            Column {
                Text(
                    text = title,
                    style = typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$percentage% از کل",
                    style = typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text(
            text = "$count عدد",
            style = typography.titleMedium,
            color = color
        )
    }
}
