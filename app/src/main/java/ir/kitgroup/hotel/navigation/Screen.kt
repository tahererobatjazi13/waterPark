package ir.kitgroup.hotel.navigation


sealed class Screen(val route: String) {

    data object RegisterVisit : Screen("register_visit")

    data object VisitDetail : Screen("visit_detail/{visitId}") {
        fun createRoute(visitId: Int) = "visit_detail/$visitId"
    }
}
