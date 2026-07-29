package ir.kitgroup.partnerManagement.feature.collaborative_collection.model

import ir.kitgroup.partnerManagement.core.ui.util.Status

data class CollectionModel(
    val id: Int,
    val name: String,
    val location: String,
    val status: Status,
    val rating: Int
)
