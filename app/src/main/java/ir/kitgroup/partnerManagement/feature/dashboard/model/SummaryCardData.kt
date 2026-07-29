package ir.kitgroup.partnerManagement.feature.dashboard.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class SummaryCardData(
    val icon: ImageVector,
    val iconColor: Color,
    val value: String,
    val title: String,
    val accent: Color
)
