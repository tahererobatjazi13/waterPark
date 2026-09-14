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
    RETURNED,
    ORANGE,
    CLOSED
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
        titleRes = R.string.status_used,
        icon = Icons.Default.CheckCircle
    ),
    Delivered(
        titleRes = R.string.status_delivered,
        icon = Icons.Default.LocalShipping
    )
}

enum class AllocationFilterTab(val titleRes: Int) {
    All(R.string.label_all),
    Active(R.string.status_active),
    Draft(R.string.status_draft),
    Returned(R.string.status_returned),
    Cancelled(R.string.status_cancelled)
}


enum class OrganizationDetailTab(
    @StringRes val titleRes: Int
) {
    INFO(
        titleRes = R.string.tab_organization_info
    ),

    PERSONS(
        titleRes = R.string.tab_organization_persons
    ),

    VISITOR(
        titleRes = R.string.tab_organization_visitor
    ),

    STANDS(
        titleRes = R.string.tab_organization_stands
    ),

    CONTRACTS(
        titleRes = R.string.tab_organization_contracts
    ),
    TICKETS(titleRes =R.string.tab_tickets_offers),

    NOTICES(
        titleRes = R.string.tab_organization_notices
    )

}


enum class OrganizationStatus(
    val id: Int,
    @StringRes val titleRes: Int
) {
    INITIAL_REGISTRATION(1, R.string.status_initial_registration),
    ACTIVE(2, R.string.status_active),
    ORANGE(3, R.string.status_orange),
    INACTIVE(4, R.string.status_inactive),
    SUSPENDED(5, R.string.status_suspended),
    CLOSED(6, R.string.status_closed);

    companion object {
        fun fromId(id: Int?): OrganizationStatus =
            entries.find { it.id == id } ?: INITIAL_REGISTRATION
    }
}
