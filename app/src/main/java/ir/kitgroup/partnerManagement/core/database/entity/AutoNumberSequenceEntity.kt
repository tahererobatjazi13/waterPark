package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auto_number_sequence")
data class AutoNumberSequenceEntity(

    @PrimaryKey
    val autoNumberSequenceId: String,

    val name: String? = null,

    val currentNumber: Int? = null,

    val prefix: String? = null,

    val format: String? = null,

    val step: Int? = null,

    val recreationCenterId: String? = null
)