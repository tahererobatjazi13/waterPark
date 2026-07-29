package ir.kitgroup.partnerManagement.core.ui.util.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ir.kitgroup.partnerManagement.core.ui.model.VisitStatusStyle
import ir.kitgroup.partnerManagement.core.ui.theme.PartnerManagementTheme
import ir.kitgroup.partnerManagement.feature.dashboard.model.VisitStatus
import androidx.annotation.StringRes
import ir.kitgroup.partnerManagement.R
import ir.kitgroup.partnerManagement.core.ui.theme.PartnerManagementTheme.colors
import ir.kitgroup.partnerManagement.core.ui.util.AllocationStatus
import ir.kitgroup.partnerManagement.core.ui.util.Status

@Composable
fun VisitStatus.style(): VisitStatusStyle {

    val colors = PartnerManagementTheme.colors

    return when (this) {

        VisitStatus.DONE -> VisitStatusStyle(
            badgeContainer = colors.successContainer,
            badgeContent = colors.onSuccessContainer,
            iconContainer = colors.success,
            iconTint = Color.White
        )

        VisitStatus.VISITING -> VisitStatusStyle(
            badgeContainer = colors.infoContainer,
            badgeContent = colors.onInfoContainer,
            iconContainer = colors.info,
            iconTint = Color.White
        )

        VisitStatus.PLANNED -> VisitStatusStyle(
            badgeContainer = colors.cardBackgroundAlt,
            badgeContent = colors.textSecondary,
            iconContainer = colors.textSecondary,
            iconTint = Color.White
        )
    }
}

@Composable
fun AllocationStatus.style(): VisitStatusStyle {
    return when (this) {
        AllocationStatus.Delivered -> VisitStatusStyle(
            badgeContainer = colors.successContainer,
            badgeContent = colors.onSuccessContainer,
            iconContainer = colors.success,
            iconTint = Color.White
        )

        AllocationStatus.Pending -> VisitStatusStyle(
            badgeContainer = colors.warningContainer,
            badgeContent = colors.onWarningContainer,
            iconContainer = colors.warning,
            iconTint = Color.White
        )

        AllocationStatus.Active -> VisitStatusStyle(
            badgeContainer = colors.successContainer,
            badgeContent = colors.onSuccessContainer,
            iconContainer = colors.success,
            iconTint = Color.White
        )

        AllocationStatus.OutOfStock -> VisitStatusStyle(
            badgeContainer = colors.warningContainer,
            badgeContent = colors.onWarningContainer,
            iconContainer = colors.warning,
            iconTint = Color.White
        )
    }
}

@StringRes
fun Status.labelRes(): Int {
    return when (this) {
        Status.ACTIVE -> R.string.label_active
        Status.INACTIVE -> R.string.label_inactive
        Status.USED -> R.string.label_used
        Status.DELIVERED -> R.string.label_delivered
    }
}


/*
git add .
git commit -m "add change app name & clean"
git push -u origin master
git push
*/

/*
http://178.131.164.145:52439/WebService.asmx
*/
