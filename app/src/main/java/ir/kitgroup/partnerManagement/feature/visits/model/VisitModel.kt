package ir.kitgroup.partnerManagement.feature.visits.model

import androidx.compose.ui.graphics.vector.ImageVector
import ir.kitgroup.partnerManagement.core.ui.util.Status

data class VisitModel(
    val id: Int,
    val organizationName: String,
    val visitType: String,
    val visitorName: String,
    val date: String,
    val city: String,
    val district: String,
    val icon: ImageVector,
    val status: Status,
)






