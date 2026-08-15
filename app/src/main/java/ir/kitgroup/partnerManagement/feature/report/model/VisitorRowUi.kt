package ir.kitgroup.partnerManagement.feature.report.model

import androidx.compose.ui.graphics.vector.ImageVector

data class VisitorRowUi(
    val id: String,
    val name: String,
    val visits: Int,
    val calls: Int,
    val introLetters: Int,
    val tutorials: Int,
    val receivedCards: Int,
)

data class VisitorsDashboardUi(
    val startDatePersian: String,
    val endDatePersian: String,
    val kpis: List<KpiUi>,
    val rows: List<VisitorRowUi>,
)

data class KpiUi(
    val title: String,
    val value: String,
    val icon: ImageVector,
)
