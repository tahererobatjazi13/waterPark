package ir.kitgroup.hotel.core.ui.util

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.vector.ImageVector
import ir.kitgroup.hotel.R
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocalShipping

enum class ThemeMode {
    LIGHT,
    DARK,
    SYSTEM
}

enum class CollectionStatus {
    ACTIVE,
    INACTIVE
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

enum class CardStatus {
    Used, Delivered
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

enum class AdvertisingFilterTab(@StringRes val titleRes: Int) {
    All(R.string.tab_all),
    Active(R.string.tab_active),
    OutOfStock(R.string.tab_out_of_stock)
}

enum class AllocationStatus {
    Delivered,
    Pending
}


enum class AllocationFilterTab(val titleRes: Int) {
    All(R.string.filter_all),
    Delivered(R.string.filter_delivered),
    Pending(R.string.filter_pending)
}

