package ir.kitgroup.partnerManagement.feature.dashboard.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.ui.graphics.vector.ImageVector
import ir.kitgroup.partnerManagement.R

data class VisitItem(
    val time: String,
    val collectionName: String,
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
        R.string.label_done,
        Icons.Filled.Check
    ),
    VISITING(
        R.string.label_visiting,
        Icons.Filled.MoreHoriz
    ),
    PLANNED(
        R.string.label_planned,
        Icons.Filled.Schedule
    )
}
