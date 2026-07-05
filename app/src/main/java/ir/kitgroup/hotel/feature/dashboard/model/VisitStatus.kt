package ir.kitgroup.hotel.feature.dashboard.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.graphics.vector.ImageVector
import ir.kitgroup.hotel.R

data class VisitItem(
    val time: String,
    val hotelName: String,
    val city: String,
    val district: String,
    val rating: Int,
    val status: VisitStatus
)

enum class VisitStatus(
    val labelRes: Int,
    val icon: ImageVector
) {
    DONE(
        R.string.status_done,
        Icons.Filled.Check
    ),
    VISITING(
        R.string.status_visiting,
        Icons.Filled.MoreHoriz
    ),
    PLANNED(
        R.string.status_planned,
        Icons.Filled.Schedule
    )
}
