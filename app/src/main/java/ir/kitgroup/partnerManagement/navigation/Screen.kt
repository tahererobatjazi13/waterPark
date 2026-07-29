package ir.kitgroup.partnerManagement.navigation


sealed class Screen(val route: String) {

    data object Dashboard : Screen("dashboard")
    data object Collections : Screen("collections")
    data object Visits : Screen("visits")
    data object Cards : Screen("cards")
    data object Profile : Screen("profile")

    data object Login : Screen("login")

    data object RegisterVisit : Screen("register_visit")
    data object AddCollection : Screen("register_collection")
    data object RegisterCard : Screen("register_card")

    data object VisitDetail : Screen("visit_detail/{visitId}") {
        fun createRoute(visitId: Int): String {
            return "visit_detail/$visitId"
        }
    }

    data object CollectionDetail : Screen("collection_detail/{collectionId}") {
        fun createRoute(collectionId: Int): String {
            return "collection_detail/$collectionId"
        }
    }

    data object AdvertisingMenu : Screen("advertising_menu")

    data object AdvertisingItems : Screen("advertising_items")

    data object AdvertisingDetail : Screen("advertising_detail/{itemId}") {

        const val ITEM_ID_ARGUMENT = "itemId"
        const val NEW_ITEM_ID = "new"

        fun createRoute(itemId: String = NEW_ITEM_ID): String {
            return "advertising_detail/$itemId"
        }
    }

       data object AdvertisingAllocationVisitor :
        Screen("advertising_allocation_visitor")

    data object AdvertisingAllocationCollection :
        Screen("advertising_allocation_collection")


    data object AddAdvertisingAllocationVisitor :
        Screen("add_advertising_allocation_visitor")


    data object AddAdvertisingAllocationCollection :
        Screen("add_advertising_allocation_collection")
}
