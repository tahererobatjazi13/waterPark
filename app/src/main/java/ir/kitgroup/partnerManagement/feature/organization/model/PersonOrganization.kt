package ir.kitgroup.partnerManagement.feature.organization.model

data class PersonOrganization(
    val id: Long = System.currentTimeMillis(),
    val name: String,
    val mobile: String,
    val phone: String,
    val status: String,
    val gender: String,
    val description: String
)
