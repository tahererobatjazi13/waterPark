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

enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}

enum class Status {
    ACTIVE,
    INACTIVE,
    USED,
    DELIVERED
}

enum class VisitType(
    val route: String,
    val titleRes: Int,
    val resultTitleRes: Int,
    val resultHintRes: Int,
    val showFields: Boolean
) {

    PHONE(
        route = "phone",
        titleRes = R.string.label_register_visit_phone,
        resultTitleRes = R.string.label_visit_phone_result,
        resultHintRes = R.string.hint_visit_phone_result,
        showFields = false
    ),

    PHYSICAL(
        route = "physical",
        titleRes = R.string.label_register_visit_physical,
        resultTitleRes = R.string.label_visit_physical_result,
        resultHintRes = R.string.hint_visit_physical_result,
        showFields = true
    );

    companion object {

        fun fromRoute(value: String?): VisitType {
            return entries.firstOrNull { it.route == value } ?: PHYSICAL
        }
    }
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

enum class AdvertisingFilterTab(@StringRes val titleRes: Int) {
    All(R.string.label_all),
    Active(R.string.label_active),
    OutOfStock(R.string.label_out_of_stock)
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


