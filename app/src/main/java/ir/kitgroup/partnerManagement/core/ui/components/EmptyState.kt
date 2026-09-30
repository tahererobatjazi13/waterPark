package ir.kitgroup.partnerManagement.core.ui.components

import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.core.ui.theme.LocalPartnerManagementColors
import androidx.annotation.StringRes as StringRes1


@Composable
fun EmptyState(
    @StringRes1 textRes: Int,
    icon: ImageVector = Icons.Default.SearchOff,
    modifier: Modifier = Modifier,
    iconSize: Dp = 40.dp,
    spacing: Dp = 8.dp
) {
    val appColors = LocalPartnerManagementColors.current

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = appColors.textSecondary,
                modifier = Modifier.size(iconSize)
            )

            Spacer(modifier = Modifier.height(spacing))

            Text(
                text = stringResource(textRes),
                style = typography.bodyMedium,
                color = appColors.textSecondary
            )
        }
    }
}