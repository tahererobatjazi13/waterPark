package ir.kitgroup.partnerManagement.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.theme.PartnerManagementTheme
import ir.kitgroup.partnerManagement.core.ui.util.Status

@Composable
fun StatusBadge(
    status: Status,
    modifier: Modifier = Modifier
) {
    val containerColor: Color
    val contentColor: Color
    val text: String

    when (status) {
        Status.ACTIVE -> {
            containerColor = PartnerManagementTheme.colors.successContainer
            contentColor = PartnerManagementTheme.colors.onSuccessContainer
            text = stringResource(R.string.status_active)
        }

        Status.INACTIVE -> {
            containerColor = PartnerManagementTheme.colors.errorContainer
            contentColor = PartnerManagementTheme.colors.onErrorContainer
            text = stringResource(R.string.status_inactive)
        }

        Status.USED -> {
            containerColor = PartnerManagementTheme.colors.successContainer
            contentColor = PartnerManagementTheme.colors.onSuccessContainer
            text = stringResource(R.string.status_used)
        }

        Status.DELIVERED -> {
            containerColor = PartnerManagementTheme.colors.infoContainer
            contentColor = PartnerManagementTheme.colors.onInfoContainer
            text = stringResource(R.string.status_delivered)
        }

        Status.DRAFT -> {
            containerColor = PartnerManagementTheme.colors.warningContainer
            contentColor = PartnerManagementTheme.colors.onWarningContainer
            text = stringResource(R.string.status_draft)
        }

        Status.BLOCKED -> {
            containerColor = PartnerManagementTheme.colors.errorContainer
            contentColor = PartnerManagementTheme.colors.onErrorContainer
            text = stringResource(R.string.status_blocked)
        }
        Status.DONE -> {
            containerColor = PartnerManagementTheme.colors.successContainer
            contentColor = PartnerManagementTheme.colors.onSuccessContainer
            text = stringResource(R.string.status_done)
        }

        Status.PLANNED -> {
            containerColor = PartnerManagementTheme.colors.warningContainer
            contentColor = PartnerManagementTheme.colors.onWarningContainer
            text = stringResource(R.string.status_cancelled)
        }
        Status.CANCELLED -> {
            containerColor = PartnerManagementTheme.colors.errorContainer
            contentColor = PartnerManagementTheme.colors.onErrorContainer
            text = stringResource(R.string.status_cancelled)
        }
        Status.RETURNED -> {
            containerColor = PartnerManagementTheme.colors.errorContainer
            contentColor = PartnerManagementTheme.colors.onErrorContainer
            text = stringResource(R.string.status_returned)
        }
        Status.ORANGE -> {
            containerColor = PartnerManagementTheme.colors.warningContainer
            contentColor = PartnerManagementTheme.colors.onWarningContainer
            text = stringResource(R.string.status_orange)
        }
        Status.CLOSED-> {
            containerColor = PartnerManagementTheme.colors.errorContainer
            contentColor = PartnerManagementTheme.colors.onErrorContainer
            text = stringResource(R.string.status_closed)
        }
    }

    Badge(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 5.dp
            )
        )
    }
}
