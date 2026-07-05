package ir.kitgroup.hotel.feature.collaborative_collection.model

import ir.kitgroup.hotel.core.ui.util.CollectionStatus


data class CollectionModel(
    val id: Int,
    val name: String,
    val location: String,
    val status: CollectionStatus,
    val rating: Int
)
