package ir.kitgroup.partnerManagement.navigation


sealed class Screen(val route: String) {

    data object Dashboard : Screen("dashboard")
    data object Collections : Screen("collections")
    data object Visits : Screen("visits")
    data object Cards : Screen("cards")
    data object Profile : Screen("profile")

    data object Login : Screen("login")
    data object Map : Screen("map")

    object RegisterVisit : Screen("register_visit?visitId={visitId}&organizationId={organizationId}") {
        fun createRoute(
            visitId: Int? = null,
            organizationId: Int? = null
        ): String {
            val params = mutableListOf<String>()
            if (visitId != null && visitId != -1) {
                params.add("visitId=$visitId")
            }
            if (organizationId != null && organizationId != -1) {
                params.add("organizationId=$organizationId")
            }
            return if (params.isEmpty()) "register_visit" else "register_visit?${params.joinToString("&")}"
        }
    }



    data object RegisterCard : Screen("register_card")

    data object VisitDetail : Screen("visit_detail/{visitId}") {
        fun createRoute(visitId: Int): String {
            return "visit_detail/$visitId"
        }
    }


    data object AddOrganization : Screen("add_organization")

    data object EditOrganization : Screen("add_organization/{organizationId}") {
        fun createRoute(organizationId: Int) = "add_organization/$organizationId"
    }

    data object OrganizationDetail : Screen("organization_detail/{organizationId}") {
        fun createRoute(organizationId: Int) = "organization_detail/$organizationId"
    }

    /*    data object AddOrganization : Screen("add_organization/{organizationId}") {
            fun createRoute(organizationId: Int) = "add_organization/$organizationId"
        }*/

    object AssignVisitor :
        Screen("assign_visitor/{organizationId}")
    // Advertising Stand
    data object AdvertisingStandMenu : Screen("advertising_stand_menu")

    data object AdvertisingStandsList : Screen("advertising_stand_list")

    data object AdvertisingStandDetail : Screen(
        route = "advertising_stand_detail/{itemId}"
    ) {
        const val ITEM_ID_ARGUMENT = "itemId"
        const val NEW_ITEM_ID = "new"

        fun createRoute(
            itemId: String = NEW_ITEM_ID
        ): String {
            return "advertising_stand_detail/$itemId"
        }
    }


    data object AdvertisingStandAssignmentVisitorList :
        Screen("advertising_stand_assignment_visitor_list")

    data object AddAdvertisingStandAssignmentVisitor :
        Screen("add_advertising_stand_assignment_visitor")

    data object AdvertisingStandAssignmentOrganizationList :
        Screen("advertising_stand_assignment_organization_list")

    data object AddAdvertisingStandAssignmentOrganization : Screen("add_stand_assignment?organizationId={organizationId}") {
        fun createRoute(organizationId: Int? = null): String {
            return if (organizationId != null) {
                "add_stand_assignment?organizationId=$organizationId"
            } else {
                "add_stand_assignment"
            }
        }
    }

    data object AdvertisingStandAssignmentVisitorDetail : Screen(
        route = "advertising_stand_assignment_visitor_detail/{assignmentId}"
    ) {
        fun createRoute(assignmentId: String): String {
            return "advertising_stand_assignment_visitor_detail/$assignmentId"
        }
    }

    //report
    data object ReportMenu : Screen("report_menu")
    data object VisitorList : Screen("visitor_list")
    data object VisitorsPerformance : Screen("visitors_performance_dashboard")
    data object ReportContract : Screen("report_contract")
    data object CollectionPerformance : Screen("collection_performance")

    data object AddContract : Screen("add_contract?organizationId={organizationId}") {
        fun createRoute(organizationId: Int? = null): String {
            return if (organizationId != null) {
                "add_contract?organizationId=$organizationId"
            } else {
                "add_contract"
            }
        }
    }

    data object ContractsList : Screen("contract_list")

    data object ContractDetail : Screen("contract_detail/{contractId}") {
        fun createRoute(contractId: String) = "contract_detail/$contractId"
    }

    data object AddContractOffer : Screen("add_contract_Offer")

    // plan_list
    data object OfferTicketPlanList : Screen("offer_ticket_Plan_list")
    data object AddOfferTicketPlan : Screen("add_Offer_ticket_plan")

    // plan_line
    data object OfferTicketPlanLineList : Screen("offer_ticket_Plan_line_list/{offerId}") {
        fun createRoute(offerId: String) = "offer_ticket_Plan_line_list/$offerId"
    }

    data object AddOfferTicketPlanLineDetail :
        Screen("add_offer_ticket_plan_line_detail?planName={planName}") {
        fun createRoute(planName: String = "") =
            "add_offer_ticket_plan_line_detail?planName=$planName"
    }

    // plan_detail
    object OfferTicketPlanLineDetail : Screen("offer_ticket_plan_line_detail/{lineId}") {
        fun createRoute(lineId: String) = "offer_ticket_plan_line_detail/$lineId"
    }
}
