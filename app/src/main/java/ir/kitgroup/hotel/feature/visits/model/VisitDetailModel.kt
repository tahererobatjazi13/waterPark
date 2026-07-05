package ir.kitgroup.hotel.feature.visits.model

import androidx.compose.ui.graphics.vector.ImageVector
import ir.kitgroup.hotel.feature.dashboard.model.VisitStatus

data class VisitDetailModel(
    val id: Int,
    val title: String,
    val type: String,
    val rating: Int,
    val person: String,
    val date: String,
    val time: String,
    val status: VisitStatus,
    val icon: ImageVector,
    val location: String,
    val description: String
)
