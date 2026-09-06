package ir.kitgroup.partnerManagement.feature.advertising_stand.model

import ir.kitgroup.partnerManagement.core.ui.util.Status

data class AdvertisingStandAssignment(
    val id: String,
    val visitorName: String,
    val organizationName: String,
    val assignmentType: String,
    val itemIconName: String,
    val count: Int,
    val allocatedDate: String,
    val assignmentMode: String,
    val status: Status
)

