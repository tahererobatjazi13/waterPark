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
    CANCELLED
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
    Delivered(R.string.label_delivered),
    Pending(R.string.label_pending)
}


enum class AllocationStatus(
    val labelRes: Int,
    val icon: ImageVector
) {
    Delivered(
        R.string.label_delivered,
        Icons.Filled.Check
    ),
    Pending(
        R.string.label_pending,
        Icons.Filled.MoreHoriz
    ),
    Active(
        R.string.label_active,
        Icons.Filled.Check
    ),
    OutOfStock(
        R.string.label_out_of_stock,
        Icons.Filled.MoreHoriz
    )
}


