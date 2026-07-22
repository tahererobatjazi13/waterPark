package ir.kitgroup.hotel.core.ui.util.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import ir.kitgroup.hotel.core.ui.model.VisitStatusStyle
import ir.kitgroup.hotel.core.ui.theme.HotelTheme
import ir.kitgroup.hotel.feature.dashboard.model.VisitStatus

import androidx.annotation.StringRes
import ir.kitgroup.hotel.R
import ir.kitgroup.hotel.core.ui.util.CardStatus
import ir.kitgroup.hotel.core.ui.util.VisitType

@Composable
fun VisitStatus.style(): VisitStatusStyle {

    val colors = HotelTheme.colors

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


@StringRes
fun CardStatus.labelRes(): Int {
    return when (this) {
        CardStatus.Used -> R.string.status_used
        CardStatus.Delivered -> R.string.status_delivered
    }
}


/*
git add .
git commit -m "layout direction"
git push -u origin master
git push
*/

/*
http://178.131.164.145:52439/WebService.asmx
*/
