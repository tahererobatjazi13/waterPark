package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "base_setting")
data class BaseSettingEntity(

    @PrimaryKey
    val baseSettingId: String,

    val name: String? = null,

    val key: String? = null,

    val value: String? = null
)