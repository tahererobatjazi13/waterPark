package ir.kitgroup.partnerManagement.navigation


sealed class Screen(val route: String) {

    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Map : Screen("map")
    data object Settings : Screen("settings")
    data object ThemeMode : Screen("themeMode")


    // Organization
    data object AddOrganization : Screen("add_organization")

    data object EditOrganization : Screen("add_organization/{organizationId}") {
        fun createRoute(organizationId: String) = "add_organization/$organizationId"
    }

    data object OrganizationDetail : Screen("organization_detail/{organizationId}") {
        fun createRoute(organizationId: String) = "organization_detail/$organizationId"
    }

    data object AssignVisitor :
        Screen("assign_visitor/{organizationId}")

    // Meetings
    data object MeetingDetail : Screen("meeting_detail/{meetingId}") {
        fun createRoute(meetingId: String): String {
            return "meeting_detail/$meetingId"
        }
    }
    data object AddMeeting:
        Screen("add_meeting?meetingId={meetingId}&organizationId={organizationId}") {
        fun createRoute(
            meetingId: String? = null,
            organizationId: String? = null
        ): String {
            val params = mutableListOf<String>()
            if (meetingId != null) {
                params.add("meetingId=$meetingId")
            }
            if (organizationId != null) {
                params.add("organizationId=$organizationId")
            }
            return if (params.isEmpty()) "add_meeting" else "add_meeting?${
                params.joinToString(
                    "&"
                )
            }"
        }
    }


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

    data object AddAdvertisingStandAssignmentOrganization :
        Screen("add_stand_assignment?organizationId={organizationId}") {
        fun createRoute(organizationId: String? = null): String {
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

    //Report
    data object ReportMenu : Screen("report_menu")
    data object VisitorList : Screen("visitor_list")
    data object VisitorsPerformance : Screen("visitors_performance_dashboard")
    data object ReportContract : Screen("report_contract")
    data object OrganizationPerformance : Screen("organization_performance")


    // Contracts
    data object ContractsList : Screen("contract_list")

    data object ContractDetail : Screen("contract_detail/{contractId}") {
        fun createRoute(contractId: String) = "contract_detail/$contractId"
    }

    data object AddContract : Screen("add_contract?organizationId={organizationId}") {
        fun createRoute(organizationId: String? = null): String {
            return if (organizationId != null) {
                "add_contract?organizationId=$organizationId"
            } else {
                "add_contract"
            }
        }
    }

    data object AddContractOffer : Screen("add_contract_Offer")

    // plan_list
    data object AddOfferTicketPlan : Screen("add_Offer_ticket_plan")

    // plan_line
    data object OfferTicketPlanLineList : Screen("offer_ticket_Plan_line_list/{offerTicketPlanId}") {
        fun createRoute(offerTicketPlanId: String) = "offer_ticket_Plan_line_list/$offerTicketPlanId"
    }

    data object AddOfferTicketPlanLineDetail :
        Screen("add_offer_ticket_plan_line_detail?planName={offerTicketPlanLineName}") {
        fun createRoute(offerTicketPlanLineName: String = "") =
            "add_offer_ticket_plan_line_detail?offerTicketPlanLineName=$offerTicketPlanLineName"
    }

    // plan_detail
    data object OfferTicketPlanLineDetail : Screen("offer_ticket_plan_line_detail/{offerTicketPlanLineId}") {
        fun createRoute(offerTicketPlanLineId: String) = "offer_ticket_plan_line_detail/$offerTicketPlanLineId"
    }
}
