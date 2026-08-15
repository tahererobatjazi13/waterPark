package ir.kitgroup.partnerManagement.feature.report.model

data class VisitorAnalysis(
    val id: String,
    val name: String,
    val code: String,
    val region: String,
    val phoneNumber: String,
    val totalVisits: Int,
    val successConversionCount: Int,
    val successConversionPercent: Double,
    val averageScore: Double,
    val assignedCards: Int
)
