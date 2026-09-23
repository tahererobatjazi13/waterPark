package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class SubjectVisitDto(

    val subjectVisitId: String,

    val name: String? = null,

    val recreationCenterId: String? = null
)