package ir.kitgroup.partnerManagement.feature.stand.model

import com.google.gson.annotations.SerializedName

data class CreateAssignStandRequest(
    @SerializedName("standAssignmentId")
    val standAssignmentId: String? = null,

    @SerializedName("advertisingStandId")
    val advertisingStandId: String,

    @SerializedName("visitorId")
    val visitorId: String? = null,

    @SerializedName("organizationId")
    val organizationId: String,

    @SerializedName("meetingId")
    val meetingId: String? = null,

    @SerializedName("count")
    val count: Int,

    @SerializedName("assignmentType")
    val assignmentType: Int,

    @SerializedName("assignmentMode")
    val assignmentMode: Int,

    @SerializedName("assignmentDate")
    val assignmentDate: String,

    @SerializedName("plannedReturnDate")
    val plannedReturnDate: String? = null,

    @SerializedName("deliveryToVisitorDate")
    val deliveryToVisitorDate: String? = null,

    @SerializedName("deliveryToOrgDate")
    val deliveryToOrgDate: String? = null,

    @SerializedName("description")
    val description: String? = null,
/*
    @SerializedName("roleCode")
    val roleCode: Int,*/

    @SerializedName("status")
    val status: Int
)