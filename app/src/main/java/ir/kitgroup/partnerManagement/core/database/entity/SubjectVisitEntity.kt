package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subject_visit")
data class SubjectVisitEntity(
    @PrimaryKey val subjectVisitId: Int,
    val name: String,
    val isActive: Boolean = true
)
