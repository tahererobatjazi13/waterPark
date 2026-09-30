package ir.kitgroup.partnerManagement.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "visit_subjects")
data class VisitSubjectEntity(

    @PrimaryKey
    val subjectVisitId: String,

    val title: String? = null,

    val stateCode: Int? = null,

    )