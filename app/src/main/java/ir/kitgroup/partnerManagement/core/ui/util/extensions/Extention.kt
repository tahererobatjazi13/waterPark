package ir.kitgroup.partnerManagement.core.ui.util.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ir.kitgroup.partnerManagement.core.ui.model.VisitStatusStyle
import ir.kitgroup.partnerManagement.core.ui.theme.PartnerManagementTheme.colors
import ir.kitgroup.partnerManagement.core.ui.util.AllocationStatus

/*
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

    */
/*    VisitStatus.VISITING -> VisitStatusStyle(
            badgeContainer = colors.infoContainer,
            badgeContent = colors.onInfoContainer,
            iconContainer = colors.info,
            iconTint = Color.White
        )*//*


        VisitStatus.PLANNED -> VisitStatusStyle(
            badgeContainer = colors.cardBackgroundAlt,
            badgeContent = colors.textSecondary,
            iconContainer = colors.textSecondary,
            iconTint = Color.White
        )

        VisitStatus.CANCELLED -> VisitStatusStyle(
            badgeContainer = colors.errorContainer,
            badgeContent = colors.onErrorContainer,
            iconContainer = colors.error,
            iconTint = Color.White
        )
    }
}
*/

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



/*
git add .
git commit -m "change screens"
git push -u origin master
git push
*/

/*
http://178.131.164.145:52439/WebService.asmx
*/
