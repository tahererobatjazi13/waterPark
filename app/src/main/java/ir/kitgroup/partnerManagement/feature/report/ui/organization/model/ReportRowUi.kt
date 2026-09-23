package ir.kitgroup.partnerManagement.feature.report.ui.organization.model

import ir.kitgroup.partnerManagement.core.ui.util.Status

data class ReportRowUi(
    val id: String,
    val collectionName: String,
    val contractDate: String,
    val cooperate: String,
    val status: Int
)

data class ReportDashboardUi(
    val rows: List<ReportRowUi>,
    val totalIntro: Int,
    val totalSerial: Int,
    val totalManual: Int
)
