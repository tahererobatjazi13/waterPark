package ir.kitgroup.partnerManagement.feature.advertising_stand.model

import ir.kitgroup.partnerManagement.core.database.entity.AdvertisingStandEntity

data class StandSelectionUiModel(
    val entity: AdvertisingStandEntity,
    val count: Int = 1,
    val isSelected: Boolean = false
)