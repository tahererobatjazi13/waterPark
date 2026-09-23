package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "person")
data class PersonEntity(
    @PrimaryKey
    val personId: String,

    val name: String? = null,

    val description: String? = null,

    val gender: Int? = null,

    val mobile: String? = null,

    val phone1: String? = null,

    val status: Int? = null
)