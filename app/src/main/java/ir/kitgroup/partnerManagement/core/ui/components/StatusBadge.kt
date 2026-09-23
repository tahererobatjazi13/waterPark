package ir.kitgroup.partnerManagement.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.core.ui.theme.PartnerManagementTheme
import ir.kitgroup.partnerManagement.core.ui.util.AppStatus
import ir.kitgroup.partnerManagement.core.ui.util.StatusColor

@Composable
fun StatusBadge(
    status: AppStatus,
    modifier: Modifier = Modifier
) {
    val colors = PartnerManagementTheme.colors

    val (containerColor, contentColor) = when (status.colorType) {
        StatusColor.SUCCESS -> colors.successContainer to colors.onSuccessContainer
        StatusColor.ERROR -> colors.errorContainer to colors.onErrorContainer
        StatusColor.WARNING -> colors.warningContainer to colors.onWarningContainer
        StatusColor.INFO -> colors.infoContainer to colors.onInfoContainer
        StatusColor.NEUTRAL -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        modifier = modifier,
        color = containerColor,
        contentColor = contentColor,
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            text = stringResource(status.titleRes),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 4.dp
            )
        )
    }
}
