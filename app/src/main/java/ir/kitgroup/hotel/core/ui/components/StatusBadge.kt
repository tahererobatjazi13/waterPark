package ir.kitgroup.hotel.core.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ir.kitgroup.hotel.R
import ir.kitgroup.hotel.core.ui.theme.HotelTheme
import ir.kitgroup.hotel.core.ui.util.CollectionStatus

@Composable
fun CollectionStatusBadge(status: CollectionStatus) {
    val containerColor: Color
    val contentColor: Color
    val text: String

    when (status) {

        CollectionStatus.ACTIVE -> {
            containerColor = HotelTheme.colors.successContainer
            contentColor = HotelTheme.colors.onSuccessContainer
            text = stringResource(R.string.label_status_active)
        }

        CollectionStatus.INACTIVE -> {
            containerColor = HotelTheme.colors.errorContainer
            contentColor = HotelTheme.colors.onErrorContainer
            text = stringResource(R.string.label_status_inactive)
        }
    }

    Badge(
        containerColor = containerColor,
        contentColor = contentColor
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall
        )
    }
}