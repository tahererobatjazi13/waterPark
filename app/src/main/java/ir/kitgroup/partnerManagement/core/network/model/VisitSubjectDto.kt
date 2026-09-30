package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class VisitSubjectDto(

    val subjectVisitId: String,

    val title: String?,

    val stateCode: Int?

)