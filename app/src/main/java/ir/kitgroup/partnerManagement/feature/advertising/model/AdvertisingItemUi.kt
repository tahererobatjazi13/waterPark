package ir.kitgroup.partnerManagement.feature.advertising.model

import androidx.compose.ui.graphics.vector.ImageVector

data class AdvertisingItemUi(
    val title: String,
    val available: Int,
    val delivered: Int,
    val selected: Boolean,
    val icon: ImageVector
)