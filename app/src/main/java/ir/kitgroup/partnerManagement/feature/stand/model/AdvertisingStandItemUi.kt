package ir.kitgroup.partnerManagement.feature.stand.model

import ir.kitgroup.partnerManagement.core.database.entity.StandTypeEntity

data class StandSelectionUiModel(
    val entity: StandTypeEntity,
    val count: Int = 1,
    val isSelected: Boolean = false
)