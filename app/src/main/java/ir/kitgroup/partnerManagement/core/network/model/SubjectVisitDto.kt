package ir.kitgroup.partnerManagement.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class SubjectVisitDto(
    val subjectVisitId: Int,
    val name: String,
    val isActive: Boolean = true
)
