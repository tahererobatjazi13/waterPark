package ir.kitgroup.hotel.core.ui.util

import ir.kitgroup.hotel.R

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

