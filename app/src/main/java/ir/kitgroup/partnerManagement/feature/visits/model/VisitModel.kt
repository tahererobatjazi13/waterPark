package ir.kitgroup.partnerManagement.feature.visits.model

import androidx.compose.ui.graphics.vector.ImageVector

data class VisitModel(
    val id: Int,
    val title: String,
    val type: String,
    val person: String,
    val date: String,
    val icon: ImageVector
)


