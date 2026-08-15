package ir.kitgroup.partnerManagement.feature.visits.model

import androidx.compose.ui.graphics.vector.ImageVector
import ir.kitgroup.partnerManagement.core.ui.util.Status

data class VisitDetailModel(
    val id: Int,
    val title: String,
    val type: String,
    val subject: String,
    val rating: Int,
    val person: String,
    val scheduledDate: String,
    val date: String,
    val time: String,
    val status: Status,
    val icon: ImageVector,
    val location: String,
    val description: String
)
