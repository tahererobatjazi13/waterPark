package ir.kitgroup.partnerManagement.core.ui.util

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.graphics.vector.ImageVector
import ir.kitgroup.partnerManagement.R
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MoreHoriz

enum class UserRole {
    SUPERVISOR,
    VISITOR
}

enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}

enum class Status {
    ACTIVE,
    INACTIVE,
    USED,
    DELIVERED,
    DRAFT,
    BLOCKED,
    DONE,
    PLANNED,
    CANCELLED,
    RETURNED
}

enum class VisitType {
    SCHEDULED_IN_PERSON
}

enum class CardStatusFilter(
    @StringRes val titleRes: Int,
    val icon: ImageVector
) {
    All(
        titleRes = R.string.label_all_status,
        icon = Icons.Default.List
    ),
    Used(
        titleRes = R.string.label_used,
        icon = Icons.Default.CheckCircle
    ),
    Delivered(
        titleRes = R.string.label_delivered,
        icon = Icons.Default.LocalShipping
    )
}

enum class AllocationFilterTab(val titleRes: Int) {
    All(R.string.label_all),
    Active(R.string.label_active),
    Draft(R.string.label_draft),
    Returned(R.string.label_returned),
    Cancelled(R.string.label_cancelled)
}



enum class OrganizationDetailTab(val titleRes: Int) {
    INFO(R.string.label_organization_basic_information),
    PERSONS(R.string.label_related_persons_list),
    VISITOR(R.string.label_assignment_visitor_to_organization_list),
    STANDS(R.string.label_assignment_stands_to_organization_list),
    NOTICES(R.string.label_warnings_organization),

}
