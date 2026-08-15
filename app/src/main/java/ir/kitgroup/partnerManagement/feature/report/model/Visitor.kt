package ir.kitgroup.partnerManagement.feature.report.model

data class Visitor(
    val id: String,
    val name: String,
    val collectionVisited: Int,
    val cardsGiven: Int,
    val isEditable: Boolean = true
)

